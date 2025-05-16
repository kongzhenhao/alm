package com.xl.alm.job.dur.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xl.alm.job.dur.constant.DurationConstant;
import com.xl.alm.job.dur.entity.DiscountCurveEntity;
import com.xl.alm.job.dur.entity.KeyDurationDiscountCurveEntity;
import com.xl.alm.job.dur.entity.KeyDurationParameterEntity;
import com.xl.alm.job.dur.mapper.DiscountCurveMapper;
import com.xl.alm.job.dur.mapper.KeyDurationDiscountCurveMapper;
import com.xl.alm.job.dur.mapper.KeyDurationParameterMapper;
import com.xl.alm.job.dur.service.KeyDurationDiscountCurveService;
import com.xl.alm.job.dur.util.DurationUtil;
import com.xl.alm.job.dur.util.ValueSetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 关键久期折现曲线服务实现类
 *
 * @author AI
 */
@Slf4j
@Service
public class KeyDurationDiscountCurveServiceImpl implements KeyDurationDiscountCurveService {

    @Autowired
    private KeyDurationParameterMapper keyDurationParameterMapper;

    @Autowired
    private DiscountCurveMapper discountCurveMapper;

    @Autowired
    private KeyDurationDiscountCurveMapper keyDurationDiscountCurveMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateKeyDurationDiscountCurve(String accountPeriod) {
        log.info("开始计算关键久期折现曲线，账期：{}", accountPeriod);

        try {
            // 1. 删除已有数据
            log.info("删除已有关键久期折现曲线数据，账期：{}", accountPeriod);

            // 使用直接SQL删除，确保数据被完全删除
            try {
                // 先尝试使用Mapper的delete方法
                int deletedCount = keyDurationDiscountCurveMapper.deleteByAccountPeriod(accountPeriod);
                log.info("已删除{}条关键久期折现曲线数据，账期：{}", deletedCount, accountPeriod);

                // 等待一段时间，确保删除操作完成
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    log.warn("等待过程被中断", ie);
                    Thread.currentThread().interrupt(); // 重新设置中断标志
                }
            } catch (Exception e) {
                log.error("删除关键久期折现曲线数据失败", e);
                // 继续执行，不抛出异常
            }

            // 2. 获取关键久期参数数据
            List<KeyDurationParameterEntity> parameterList = getKeyDurationParameterList(accountPeriod);
            if (parameterList.isEmpty()) {
                log.warn("未找到关键久期参数数据，账期：{}", accountPeriod);
                return false;
            }
            log.info("获取到{}条关键久期参数数据，账期：{}", parameterList.size(), accountPeriod);

            // 3. 获取折现曲线数据
            Map<String, DiscountCurveEntity> curveMap = getDiscountCurveMap(accountPeriod);
            if (curveMap.isEmpty()) {
                log.warn("未找到折现曲线数据，账期：{}", accountPeriod);
                return false;
            }
            log.info("获取到{}条折现曲线数据，账期：{}", curveMap.size(), accountPeriod);

            // 4. 计算关键久期折现曲线
            List<KeyDurationDiscountCurveEntity> resultList = calculateKeyDurationDiscountCurve(
                    accountPeriod, parameterList, curveMap);
            log.info("计算得到{}条关键久期折现曲线数据，账期：{}", resultList.size(), accountPeriod);

            // 5. 批量保存结果
            int successCount = 0;
            int skipCount = 0;
            int errorCount = 0;

            // 使用Set记录已处理的唯一键，避免重复插入
            Set<String> processedKeys = new HashSet<>();

            // 先查询数据库中已存在的记录
            List<KeyDurationDiscountCurveEntity> existingEntities = getExistingEntities(accountPeriod);
            Set<String> existingKeys = new HashSet<>();

            for (KeyDurationDiscountCurveEntity entity : existingEntities) {
                String key = entity.getAccountPeriod() + "-" + entity.getCurveType() + "-" +
                            entity.getKeyDuration() + "-" + entity.getStressDirection() + "-" +
                            entity.getDurationType();
                existingKeys.add(key);
            }

            log.info("数据库中已存在{}条关键久期折现曲线数据，账期：{}", existingKeys.size(), accountPeriod);

            for (KeyDurationDiscountCurveEntity entity : resultList) {
                try {
                    // 构建唯一键
                    String uniqueKey = entity.getAccountPeriod() + "-" + entity.getCurveType() + "-" +
                                      entity.getKeyDuration() + "-" + entity.getStressDirection() + "-" +
                                      entity.getDurationType();

                    // 检查是否已处理过该唯一键
                    if (processedKeys.contains(uniqueKey)) {
                        log.debug("跳过重复记录：{}", uniqueKey);
                        skipCount++;
                        continue;
                    }

                    // 检查数据库中是否已存在该记录
                    if (existingKeys.contains(uniqueKey)) {
                        // 记录已存在，跳过
                        log.debug("记录已存在，跳过插入：{}", uniqueKey);
                        skipCount++;
                    } else {
                        // 记录不存在，尝试插入
                        try {
                            keyDurationDiscountCurveMapper.insert(entity);
                            successCount++;

                            // 记录已处理的唯一键
                            processedKeys.add(uniqueKey);
                            existingKeys.add(uniqueKey);
                        } catch (Exception e) {
                            // 如果插入失败，可能是因为并发插入导致的唯一键冲突
                            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                                log.warn("插入时发生唯一键冲突，跳过：{}", uniqueKey);
                                skipCount++;
                            } else {
                                // 其他错误
                                throw e;
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("插入关键久期折现曲线数据失败：账期={}，曲线类型={}，关键期限点={}，压力方向={}，久期类型={}",
                            entity.getAccountPeriod(), entity.getCurveType(), entity.getKeyDuration(),
                            entity.getStressDirection(), entity.getDurationType(), e);
                    errorCount++;
                }
            }

            log.info("计算关键久期折现曲线完成，账期：{}，共{}条数据，成功插入{}条，跳过{}条，失败{}条",
                    accountPeriod, resultList.size(), successCount, skipCount, errorCount);
            return true;
        } catch (Exception e) {
            log.error("计算关键久期折现曲线失败，账期：{}", accountPeriod, e);
            throw e;
        }
    }

    /**
     * 获取已存在的关键久期折现曲线实体列表
     *
     * @param accountPeriod 账期
     * @return 已存在的关键久期折现曲线实体列表
     */
    private List<KeyDurationDiscountCurveEntity> getExistingEntities(String accountPeriod) {
        LambdaQueryWrapper<KeyDurationDiscountCurveEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KeyDurationDiscountCurveEntity::getAccountPeriod, accountPeriod)
                .eq(KeyDurationDiscountCurveEntity::getIsDel, 0);
        return keyDurationDiscountCurveMapper.selectList(queryWrapper);
    }

    /**
     * 获取关键久期参数列表
     *
     * @param accountPeriod 账期
     * @return 关键久期参数列表
     */
    private List<KeyDurationParameterEntity> getKeyDurationParameterList(String accountPeriod) {
        LambdaQueryWrapper<KeyDurationParameterEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KeyDurationParameterEntity::getAccountPeriod, accountPeriod)
                .eq(KeyDurationParameterEntity::getIsDel, 0);
        return keyDurationParameterMapper.selectList(queryWrapper);
    }

    /**
     * 获取折现曲线数据
     *
     * 注意：根据DiscountCurveEntity类的定义，curveType字段的定义是：
     * 01:低档,02:中档
     * 但在DurationConstant类中，定义是：
     * CURVE_TYPE_MIDDLE = "01"
     * CURVE_TYPE_LOW = "02"
     * 这里需要进行转换，确保正确处理曲线类型
     *
     * @param accountPeriod 账期
     * @return 折现曲线数据Map，key为曲线类型
     */
    private Map<String, DiscountCurveEntity> getDiscountCurveMap(String accountPeriod) {
        Map<String, DiscountCurveEntity> curveMap = new HashMap<>();

        // 查询修正久期的中档和低档曲线
        LambdaQueryWrapper<DiscountCurveEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DiscountCurveEntity::getAccountPeriod, accountPeriod)
                .eq(DiscountCurveEntity::getDurationType, DurationConstant.DURATION_TYPE_MODIFIED)
                .eq(DiscountCurveEntity::getBpType, DurationConstant.BP_TYPE_ZERO)
                .in(DiscountCurveEntity::getCurveType, "01", "02") // 01:低档,02:中档
                .eq(DiscountCurveEntity::getIsDel, 0);

        List<DiscountCurveEntity> curveList = discountCurveMapper.selectList(queryWrapper);
        for (DiscountCurveEntity entity : curveList) {
            // 进行曲线类型转换，确保正确处理曲线类型
            String curveType = entity.getCurveType();
            // 根据DiscountCurveEntity类的定义，01:低档,02:中档
            // 转换为DurationConstant类的定义，CURVE_TYPE_MIDDLE = "01", CURVE_TYPE_LOW = "02"
            if ("01".equals(curveType)) {
                // 01:低档 -> CURVE_TYPE_LOW = "02"
                entity.setCurveType(DurationConstant.CURVE_TYPE_LOW);
                log.info("曲线类型转换：01(低档) -> {}(低档)", DurationConstant.CURVE_TYPE_LOW);
            } else if ("02".equals(curveType)) {
                // 02:中档 -> CURVE_TYPE_MIDDLE = "01"
                entity.setCurveType(DurationConstant.CURVE_TYPE_MIDDLE);
                log.info("曲线类型转换：02(中档) -> {}(中档)", DurationConstant.CURVE_TYPE_MIDDLE);
            }
            curveMap.put(entity.getCurveType(), entity);
        }

        return curveMap;
    }

    /**
     * 计算关键久期折现曲线
     *
     * 注意：根据需求，需要保持曲线类型的正确性
     * - 中档曲线(01)保持为中档曲线(01)
     * - 低档曲线(02)保持为低档曲线(02)
     *
     * @param accountPeriod 账期
     * @param parameterList 关键久期参数列表
     * @param curveMap 折现曲线数据Map
     * @return 关键久期折现曲线列表
     */
    private List<KeyDurationDiscountCurveEntity> calculateKeyDurationDiscountCurve(
            String accountPeriod,
            List<KeyDurationParameterEntity> parameterList,
            Map<String, DiscountCurveEntity> curveMap) {

        List<KeyDurationDiscountCurveEntity> resultList = new ArrayList<>();

        // 遍历每个关键期限点
        for (KeyDurationParameterEntity parameter : parameterList) {
            String keyDuration = parameter.getKeyDuration();
            Map<Integer, BigDecimal> parameterValueMap = ValueSetUtil.parseValueMap(parameter.getParameterValSet());

            // 遍历每个曲线类型（中档/低档）
            for (Map.Entry<String, DiscountCurveEntity> entry : curveMap.entrySet()) {
                String curveType = entry.getKey();
                DiscountCurveEntity curveEntity = entry.getValue();
                Map<Integer, BigDecimal> curveValueMap = ValueSetUtil.parseValueMap(curveEntity.getCurveValSet());
                Map<Integer, String> dateMap = ValueSetUtil.parseDateMap(curveEntity.getCurveValSet());

                // 记录原始曲线类型
                String originalCurveType = curveType;

                // 记录曲线类型信息
                log.debug("处理曲线：账期={}，关键期限点={}，曲线类型={}({})",
                        accountPeriod, keyDuration, curveType,
                        DurationConstant.CURVE_TYPE_MIDDLE.equals(curveType) ? "中档" : "低档");

                // 计算上升曲线
                KeyDurationDiscountCurveEntity upEntity = new KeyDurationDiscountCurveEntity();
                upEntity.setAccountPeriod(accountPeriod);
                upEntity.setCurveType(originalCurveType); // 保持原始曲线类型
                upEntity.setKeyDuration(keyDuration);
                upEntity.setStressDirection(DurationConstant.STRESS_DIRECTION_UP);
                upEntity.setDurationType(DurationConstant.DURATION_TYPE_KEY);
                upEntity.setCurveValSet(calculateCurveValSet(curveValueMap, parameterValueMap, dateMap, true));
                upEntity.setCreateTime(new java.util.Date());
                upEntity.setIsDel(0);
                resultList.add(upEntity);

                log.debug("生成上升曲线：账期={}，关键期限点={}，曲线类型={}({}), 压力方向=上升",
                        accountPeriod, keyDuration, originalCurveType,
                        DurationConstant.CURVE_TYPE_MIDDLE.equals(originalCurveType) ? "中档" : "低档");

                // 计算下降曲线
                KeyDurationDiscountCurveEntity downEntity = new KeyDurationDiscountCurveEntity();
                downEntity.setAccountPeriod(accountPeriod);
                downEntity.setCurveType(originalCurveType); // 保持原始曲线类型
                downEntity.setKeyDuration(keyDuration);
                downEntity.setStressDirection(DurationConstant.STRESS_DIRECTION_DOWN);
                downEntity.setDurationType(DurationConstant.DURATION_TYPE_KEY);
                downEntity.setCurveValSet(calculateCurveValSet(curveValueMap, parameterValueMap, dateMap, false));
                downEntity.setCreateTime(new java.util.Date());
                downEntity.setIsDel(0);
                resultList.add(downEntity);

                log.debug("生成下降曲线：账期={}，关键期限点={}，曲线类型={}({}), 压力方向=下降",
                        accountPeriod, keyDuration, originalCurveType,
                        DurationConstant.CURVE_TYPE_MIDDLE.equals(originalCurveType) ? "中档" : "低档");
            }
        }

        return resultList;
    }

    /**
     * 计算曲线值集
     *
     * @param curveValueMap 原始曲线值Map
     * @param parameterValueMap 参数值Map
     * @param dateMap 日期Map
     * @param isUp 是否上升
     * @return 曲线值集
     */
    private String calculateCurveValSet(
            Map<Integer, BigDecimal> curveValueMap,
            Map<Integer, BigDecimal> parameterValueMap,
            Map<Integer, String> dateMap,
            boolean isUp) {

        Map<String, Object> resultMap = new HashMap<>();

        for (int i = 0; i <= 1272; i++) {
            BigDecimal curveValue = curveValueMap.getOrDefault(i, BigDecimal.ZERO);
            BigDecimal parameterValue = parameterValueMap.getOrDefault(i, BigDecimal.ZERO);

            // 计算新的曲线值
            BigDecimal newCurveValue;
            if (isUp) {
                newCurveValue = curveValue.add(parameterValue);
            } else {
                newCurveValue = curveValue.subtract(parameterValue);
            }

            // 构建结果
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("date", dateMap.get(i));
            itemMap.put("value", newCurveValue);

            resultMap.put(String.valueOf(i), itemMap);
        }

        return DurationUtil.toJsonString(resultMap);
    }
}
