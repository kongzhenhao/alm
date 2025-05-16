package com.xl.alm.job.dur.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xl.alm.job.dur.constant.DurationConstant;
import com.xl.alm.job.dur.entity.KeyDurationDiscountCurveEntity;
import com.xl.alm.job.dur.entity.KeyDurationDiscountFactorsEntity;
import com.xl.alm.job.dur.mapper.KeyDurationDiscountCurveMapper;
import com.xl.alm.job.dur.mapper.KeyDurationDiscountFactorsMapper;
import com.xl.alm.job.dur.service.KeyDurationDiscountFactorsService;
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
 * 关键久期折现因子服务实现类
 *
 * @author AI
 */
@Slf4j
@Service
public class KeyDurationDiscountFactorsServiceImpl implements KeyDurationDiscountFactorsService {

    @Autowired
    private KeyDurationDiscountCurveMapper keyDurationDiscountCurveMapper;

    @Autowired
    private KeyDurationDiscountFactorsMapper keyDurationDiscountFactorsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateKeyDurationDiscountFactors(String accountPeriod) {
        log.info("开始计算关键久期折现因子，账期：{}", accountPeriod);

        try {
            // 1. 删除已有数据（使用物理删除而不是逻辑删除，避免唯一索引冲突）
            try {
                log.info("开始删除已有关键久期折现因子数据，账期：{}", accountPeriod);

                // 先尝试使用Mapper的delete方法
                LambdaQueryWrapper<KeyDurationDiscountFactorsEntity> deleteWrapper = new LambdaQueryWrapper<>();
                deleteWrapper.eq(KeyDurationDiscountFactorsEntity::getAccountPeriod, accountPeriod);
                int deleteCount = keyDurationDiscountFactorsMapper.delete(deleteWrapper);
                log.info("物理删除关键久期折现因子记录：{}条", deleteCount);

                // 为确保数据被完全删除，再执行一次直接的SQL删除
                String deleteSql = "DELETE FROM t_dur_key_duration_discount_factors WHERE account_period = '" + accountPeriod + "'";
                keyDurationDiscountFactorsMapper.deleteBySql(deleteSql);
                log.info("执行SQL删除关键久期折现因子记录完成");

                // 等待一段时间，确保删除操作完成
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    log.warn("等待过程被中断", ie);
                    Thread.currentThread().interrupt(); // 重新设置中断标志
                }
            } catch (Exception e) {
                log.error("删除关键久期折现因子记录失败", e);
                throw e;
            }

            // 2. 获取关键久期折现曲线数据
            List<KeyDurationDiscountCurveEntity> curveList = getKeyDurationDiscountCurveList(accountPeriod);
            if (curveList.isEmpty()) {
                log.warn("未找到关键久期折现曲线数据，账期：{}", accountPeriod);
                return false;
            }
            log.info("获取到{}条关键久期折现曲线数据，账期：{}", curveList.size(), accountPeriod);

            // 3. 计算关键久期折现因子
            List<KeyDurationDiscountFactorsEntity> resultList = calculateKeyDurationDiscountFactors(
                    accountPeriod, curveList);
            log.info("计算得到{}条关键久期折现因子数据，账期：{}", resultList.size(), accountPeriod);

            // 4. 保存结果（使用单条插入避免批量插入可能的问题）
            if (!resultList.isEmpty()) {
                log.info("开始保存关键久期折现因子记录，共{}条", resultList.size());
                int successCount = 0;
                int skipCount = 0;
                int errorCount = 0;

                // 使用Set记录已处理的唯一键，避免重复插入
                Set<String> processedKeys = new HashSet<>();

                for (KeyDurationDiscountFactorsEntity entity : resultList) {
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
                        LambdaQueryWrapper<KeyDurationDiscountFactorsEntity> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(KeyDurationDiscountFactorsEntity::getAccountPeriod, entity.getAccountPeriod())
                                .eq(KeyDurationDiscountFactorsEntity::getCurveType, entity.getCurveType())
                                .eq(KeyDurationDiscountFactorsEntity::getKeyDuration, entity.getKeyDuration())
                                .eq(KeyDurationDiscountFactorsEntity::getStressDirection, entity.getStressDirection())
                                .eq(KeyDurationDiscountFactorsEntity::getDurationType, entity.getDurationType())
                                .eq(KeyDurationDiscountFactorsEntity::getIsDel, 0);

                        KeyDurationDiscountFactorsEntity existingEntity = keyDurationDiscountFactorsMapper.selectOne(queryWrapper);

                        if (existingEntity != null) {
                            // 记录已存在，跳过
                            log.debug("记录已存在，跳过插入：{}", uniqueKey);
                            skipCount++;
                        } else {
                            // 记录不存在，插入
                            keyDurationDiscountFactorsMapper.insert(entity);
                            successCount++;

                            // 记录已处理的唯一键
                            processedKeys.add(uniqueKey);
                        }
                    } catch (Exception e) {
                        // 记录详细的错误信息
                        log.error("插入关键久期折现因子记录失败：账期={}, 曲线类型={}, 关键期限点={}, 应力方向={}, 久期类型={}, 错误信息={}",
                                entity.getAccountPeriod(),
                                entity.getCurveType(),
                                entity.getKeyDuration(),
                                entity.getStressDirection(),
                                entity.getDurationType(),
                                e.getMessage());
                        errorCount++;

                        // 继续处理下一条记录，不中断整个过程
                    }
                }
                log.info("关键久期折现因子记录处理结果：共{}条，成功插入{}条，跳过{}条，失败{}条",
                        resultList.size(), successCount, skipCount, errorCount);
            }

            log.info("计算关键久期折现因子完成，账期：{}，共{}条数据", accountPeriod, resultList.size());
            return true;
        } catch (Exception e) {
            log.error("计算关键久期折现因子失败，账期：{}", accountPeriod, e);
            throw e;
        }
    }

    /**
     * 获取关键久期折现曲线列表
     *
     * @param accountPeriod 账期
     * @return 关键久期折现曲线列表
     */
    private List<KeyDurationDiscountCurveEntity> getKeyDurationDiscountCurveList(String accountPeriod) {
        // 查询所有关键久期折现曲线数据
        LambdaQueryWrapper<KeyDurationDiscountCurveEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(KeyDurationDiscountCurveEntity::getAccountPeriod, accountPeriod)
                .eq(KeyDurationDiscountCurveEntity::getDurationType, DurationConstant.DURATION_TYPE_KEY)
                .eq(KeyDurationDiscountCurveEntity::getIsDel, 0);

        List<KeyDurationDiscountCurveEntity> curveList = keyDurationDiscountCurveMapper.selectList(queryWrapper);

        // 记录查询到的曲线数量和详细信息
        log.info("查询到关键久期折现曲线数量：{}", curveList.size());

        // 统计各种类型的数量
        Map<String, Integer> curveTypeCount = new HashMap<>();
        Map<String, Integer> stressDirectionCount = new HashMap<>();
        Map<String, Integer> keyDurationCount = new HashMap<>();

        for (KeyDurationDiscountCurveEntity curve : curveList) {
            // 统计曲线类型
            String curveType = curve.getCurveType();
            curveTypeCount.put(curveType, curveTypeCount.getOrDefault(curveType, 0) + 1);

            // 统计应力方向
            String stressDirection = curve.getStressDirection();
            stressDirectionCount.put(stressDirection, stressDirectionCount.getOrDefault(stressDirection, 0) + 1);

            // 统计关键期限点
            String keyDuration = curve.getKeyDuration();
            keyDurationCount.put(keyDuration, keyDurationCount.getOrDefault(keyDuration, 0) + 1);
        }

        // 输出统计信息
        log.info("曲线类型统计：{}", curveTypeCount);
        log.info("应力方向统计：{}", stressDirectionCount);
        log.info("关键期限点统计：{}", keyDurationCount);

        return curveList;
    }

    /**
     * 计算关键久期折现因子
     *
     * @param accountPeriod 账期
     * @param curveList 关键久期折现曲线列表
     * @return 关键久期折现因子列表
     */
    private List<KeyDurationDiscountFactorsEntity> calculateKeyDurationDiscountFactors(
            String accountPeriod,
            List<KeyDurationDiscountCurveEntity> curveList) {

        List<KeyDurationDiscountFactorsEntity> resultList = new ArrayList<>();

        // 记录原始曲线数量
        log.info("原始关键久期折现曲线数量：{}", curveList.size());

        // 遍历每条折现曲线
        for (KeyDurationDiscountCurveEntity curve : curveList) {
            // 记录当前处理的曲线信息
            log.debug("处理曲线：账期={}, 曲线类型={}, 关键期限点={}, 应力方向={}, 久期类型={}",
                    curve.getAccountPeriod(), curve.getCurveType(), curve.getKeyDuration(),
                    curve.getStressDirection(), curve.getDurationType());

            // 解析曲线值集
            Map<Integer, BigDecimal> curveValueMap = ValueSetUtil.parseValueMap(curve.getCurveValSet());
            Map<Integer, String> dateMap = ValueSetUtil.parseDateMap(curve.getCurveValSet());

            // 计算折现因子
            Map<Integer, BigDecimal> factorValueMap = calculateDiscountFactors(curveValueMap);

            // 根据曲线类型和应力方向设置正确的组合
            String curveType = curve.getCurveType();
            String stressDirection = curve.getStressDirection();
            String keyDuration = curve.getKeyDuration();
            String durationType = curve.getDurationType();

            // 保持原始曲线类型和应力方向
            String correctedCurveType = curveType;
            String correctedStressDirection = stressDirection;

            // 记录原始组合
            log.debug("原始组合：曲线类型={}, 应力方向={}", curveType, stressDirection);

            // 记录使用的组合信息
            log.info("使用组合：[{}] {} {}",
                    keyDuration,
                    DurationConstant.CURVE_TYPE_MIDDLE.equals(curveType) ? "中档" : "低档",
                    DurationConstant.STRESS_DIRECTION_UP.equals(stressDirection) ? "上升" : "下降");

            // 构建唯一键，使用修正后的值
            String uniqueKey = accountPeriod + "-" + correctedCurveType + "-" + keyDuration + "-" +
                              correctedStressDirection + "-" + durationType;

            // 记录唯一键信息
            log.debug("唯一键：{}", uniqueKey);

            // 创建折现因子实体
            KeyDurationDiscountFactorsEntity entity = new KeyDurationDiscountFactorsEntity();
            entity.setAccountPeriod(accountPeriod);
            entity.setKeyDuration(keyDuration);
            entity.setDurationType(durationType);
            entity.setCurveType(correctedCurveType);
            entity.setStressDirection(correctedStressDirection);
            entity.setFactorValSet(buildFactorValSet(factorValueMap, dateMap));
            entity.setCreateTime(new java.util.Date());
            entity.setIsDel(0);

            // 将实体添加到结果列表
            resultList.add(entity);
        }

        log.info("计算得到{}条关键久期折现因子记录", resultList.size());
        return resultList;
    }

    /**
     * 计算折现因子
     *
     * @param curveValueMap 曲线值Map
     * @return 折现因子Map
     */
    private Map<Integer, BigDecimal> calculateDiscountFactors(Map<Integer, BigDecimal> curveValueMap) {
        Map<Integer, BigDecimal> factorValueMap = new HashMap<>();

        // t=0时，折现因子为1
        factorValueMap.put(0, BigDecimal.ONE);

        // t>0时，折现因子 = 1/(1+r)^(t/12)
        for (int i = 1; i <= 1272; i++) {
            BigDecimal rate = curveValueMap.getOrDefault(i, BigDecimal.ZERO);
            BigDecimal factor = DurationUtil.calculateDiscountFactor(rate, i);
            factorValueMap.put(i, factor);
        }

        return factorValueMap;
    }

    /**
     * 构建因子值集
     *
     * @param factorValueMap 因子值Map
     * @param dateMap 日期Map
     * @return 因子值集
     */
    private String buildFactorValSet(Map<Integer, BigDecimal> factorValueMap, Map<Integer, String> dateMap) {
        Map<String, Object> resultMap = new HashMap<>();

        for (int i = 0; i <= 1272; i++) {
            BigDecimal factorValue = factorValueMap.getOrDefault(i, BigDecimal.ZERO);

            // 构建结果
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("date", dateMap.get(i));
            itemMap.put("value", factorValue);

            resultMap.put(String.valueOf(i), itemMap);
        }

        return DurationUtil.toJsonString(resultMap);
    }
}
