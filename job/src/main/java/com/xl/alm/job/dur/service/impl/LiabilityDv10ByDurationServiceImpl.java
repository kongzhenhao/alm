package com.xl.alm.job.dur.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xl.alm.job.dur.constant.CalculationConstant;
import com.xl.alm.job.dur.constant.DurationConstant;
import com.xl.alm.job.dur.entity.KeyDurationDiscountFactorsEntity;
import com.xl.alm.job.dur.entity.LiabilityDv10ByDurationEntity;
import com.xl.alm.job.dur.entity.LiabilityCashFlowSummaryEntity;
import com.xl.alm.job.dur.mapper.KeyDurationDiscountFactorsMapper;
import com.xl.alm.job.dur.mapper.LiabilityDv10ByDurationMapper;
import com.xl.alm.job.dur.mapper.LiabilityCashFlowSummaryMapper;
import com.xl.alm.job.dur.service.LiabilityDv10ByDurationService;
import com.xl.alm.job.dur.util.ValueSetUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
// 移除不再使用的导入
// import org.apache.commons.math3.linear.ArrayFieldVector;
// import org.apache.commons.math3.linear.FieldVector;
// import org.apache.commons.math3.util.BigReal;

/**
 * 分中短负债基点价值DV10服务实现类
 *
 * @author AI
 */
@Slf4j
@Service
public class LiabilityDv10ByDurationServiceImpl implements LiabilityDv10ByDurationService {

    @Autowired
    private LiabilityDv10ByDurationMapper liabilityDv10ByDurationMapper;

    @Autowired
    private LiabilityCashFlowSummaryMapper liabilityCashFlowSummaryMapper;

    @Autowired
    private KeyDurationDiscountFactorsMapper keyDurationDiscountFactorsMapper;

    /**
     * 计算分中短负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @return 执行结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateLiabilityDv10ByDuration(String accountPeriod) {
        log.info("【DV10计算】开始计算分中短负债基点价值DV10，账期：{}", accountPeriod);
        long startTime = System.currentTimeMillis();

        try {
            // 1. 删除已有数据
            log.info("【DV10计算-步骤1】开始删除已有数据，账期：{}", accountPeriod);
            liabilityDv10ByDurationMapper.deleteByAccountPeriod(accountPeriod);
            log.info("【DV10计算-步骤1】删除已有数据完成，耗时：{}ms", System.currentTimeMillis() - startTime);
            long stepTime = System.currentTimeMillis();

            // 2. 获取负债现金流汇总数据
            log.info("【DV10计算-步骤2】开始获取负债现金流汇总数据，账期：{}", accountPeriod);
            List<LiabilityCashFlowSummaryEntity> cashFlowSummaryList = getCashFlowSummaryList(accountPeriod);
            if (cashFlowSummaryList.isEmpty()) {
                log.warn("【DV10计算-步骤2】未找到负债现金流汇总数据，账期：{}", accountPeriod);
                return false;
            }
            log.info("【DV10计算-步骤2】获取负债现金流汇总数据完成，共{}条记录，耗时：{}ms",
                    cashFlowSummaryList.size(), System.currentTimeMillis() - stepTime);
            stepTime = System.currentTimeMillis();

            // 3. 获取关键久期折现因子数据
            log.info("【DV10计算-步骤3】开始获取关键久期折现因子数据，账期：{}", accountPeriod);
            Map<String, Map<Integer, BigDecimal>> factorMap = loadKeyDurationDiscountFactors(accountPeriod);
            if (factorMap.isEmpty()) {
                log.warn("【DV10计算-步骤3】未找到关键久期折现因子数据，账期：{}", accountPeriod);
                return false;
            }
            log.info("【DV10计算-步骤3】获取关键久期折现因子数据完成，共{}条记录，耗时：{}ms",
                    factorMap.size(), System.currentTimeMillis() - stepTime);
            stepTime = System.currentTimeMillis();

            // 4. 计算分中短负债基点价值DV10
            log.info("【DV10计算-步骤4】开始计算分中短负债基点价值DV10，账期：{}", accountPeriod);
            List<LiabilityDv10ByDurationEntity> resultList = calculateLiabilityDv10(
                    accountPeriod, cashFlowSummaryList, factorMap);

            if (resultList.isEmpty()) {
                log.warn("【DV10计算-步骤4】计算分中短负债基点价值DV10结果为空，账期：{}", accountPeriod);
                return true;
            }

            log.info("【DV10计算-步骤4】计算分中短负债基点价值DV10完成，共{}条记录，耗时：{}ms",
                    resultList.size(), System.currentTimeMillis() - stepTime);
            stepTime = System.currentTimeMillis();

            // 5. 批量保存结果
            log.info("【DV10计算-步骤5】开始批量保存计算结果，账期：{}", accountPeriod);
            try {
                // 使用批量插入替代单条插入
                int batchSize = 2000; // 增加批次大小，减少数据库交互次数
                int totalSize = resultList.size();
                int batchCount = (totalSize + batchSize - 1) / batchSize; // 计算批次数

                log.info("【DV10计算-步骤5】开始批量保存数据，共{}条记录，分{}批处理，每批{}条",
                        totalSize, batchCount, batchSize);

                for (int i = 0; i < totalSize; i += batchSize) {
                    long batchStartTime = System.currentTimeMillis();
                    int endIndex = Math.min(i + batchSize, totalSize);
                    List<LiabilityDv10ByDurationEntity> batchList = resultList.subList(i, endIndex);

                    // 执行批量插入
                    liabilityDv10ByDurationMapper.batchInsert(batchList);

                    log.info("【DV10计算-步骤5】批量保存第{}批数据完成，{}条记录，耗时：{}ms",
                            (i / batchSize) + 1, batchList.size(), System.currentTimeMillis() - batchStartTime);
                }

                log.info("【DV10计算-步骤5】批量保存结果完成，共{}条记录，耗时：{}ms",
                        resultList.size(), System.currentTimeMillis() - stepTime);
            } catch (Exception e) {
                log.error("【DV10计算-步骤5】批量保存数据失败", e);
                throw e;
            }

            log.info("【DV10计算】计算分中短负债基点价值DV10完成，账期：{}，共{}条数据，总耗时：{}ms",
                    accountPeriod, resultList.size(), System.currentTimeMillis() - startTime);
            return true;
        } catch (Exception e) {
            log.error("【DV10计算】计算分中短负债基点价值DV10失败，账期：{}", accountPeriod, e);
            throw e;
        }
    }

    /**
     * 获取负债现金流汇总列表
     *
     * 根据要求，只获取基点类型为0bp、修正久期的数据
     *
     * @param accountPeriod 账期
     * @return 负债现金流汇总列表
     */
    private List<LiabilityCashFlowSummaryEntity> getCashFlowSummaryList(String accountPeriod) {
        long startTime = System.currentTimeMillis();
        log.debug("【DV10计算-数据加载】开始查询负债现金流汇总数据，账期：{}", accountPeriod);

        // 查询数据，只获取基点类型为0bp、修正久期的数据
        List<LiabilityCashFlowSummaryEntity> result = liabilityCashFlowSummaryMapper.selectLiabilityCashFlowSummaryEntityListByCondition(
                accountPeriod,
                null, // 现金流类型，不限制
                "03", // 基点类型，03:0bp
                "01", // 久期类型，01:修正久期
                null, // 设计类型，不限制
                null  // 是否为中短期险种，不限制
        );

        log.debug("【DV10计算-数据加载】查询负债现金流汇总数据完成，共{}条记录，耗时：{}ms",
                result.size(), System.currentTimeMillis() - startTime);

        return result;
    }

    /**
     * 加载关键久期折现因子
     *
     * 根据文档步骤2中的要求，需要读取关键久期折现因子表中的数据：
     * - 对每个期限点(0,0.5,1,2,3,4,5,6,7,8,10,12,15,20,25,30,35,40,45,50年)
     * - 读取上升和下降情况的折现因子值集，每个值集包含1273项(0-1272)
     *
     * @param accountPeriod 账期
     * @return 因子Map，key为keyDuration|curveType|stressDirection，value为因子值Map
     */
    private Map<String, Map<Integer, BigDecimal>> loadKeyDurationDiscountFactors(String accountPeriod) {
        long startTime = System.currentTimeMillis();
        log.debug("【DV10计算-数据加载】开始加载关键久期折现因子，账期：{}", accountPeriod);

        try {
            // 查询所有关键久期折现因子数据，包括中档和低档曲线类型
            LambdaQueryWrapper<KeyDurationDiscountFactorsEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(KeyDurationDiscountFactorsEntity::getAccountPeriod, accountPeriod)
                    .eq(KeyDurationDiscountFactorsEntity::getDurationType, DurationConstant.DURATION_TYPE_KEY)
                    .in(KeyDurationDiscountFactorsEntity::getCurveType, Arrays.asList("01", "02")) // 中档和低档
                    .in(KeyDurationDiscountFactorsEntity::getStressDirection, Arrays.asList("01", "02")) // 上升和下降
                    .eq(KeyDurationDiscountFactorsEntity::getIsDel, 0);

            List<KeyDurationDiscountFactorsEntity> factorEntities = keyDurationDiscountFactorsMapper.selectList(queryWrapper);
            log.debug("【DV10计算-数据加载】查询关键久期折现因子数据完成，共{}条记录，耗时：{}ms",
                    factorEntities.size(), System.currentTimeMillis() - startTime);

            if (factorEntities.isEmpty()) {
                log.warn("【DV10计算-数据加载】未找到关键久期折现因子数据，账期：{}", accountPeriod);
                return Collections.emptyMap();
            }

            // 预估Map大小，避免扩容
            int initialCapacity = (int)(factorEntities.size() / 0.75) + 1;

            // 创建一个线程安全的Map用于并行处理
            Map<String, Map<Integer, BigDecimal>> concurrentFactorMap = new ConcurrentHashMap<>(initialCapacity);

            // 使用并行流处理解析值集
            long parseStartTime = System.currentTimeMillis();
            log.info("【DV10计算-数据加载】开始并行解析关键久期折现因子值集，共{}条记录", factorEntities.size());

            // 并行处理解析值集
            factorEntities.parallelStream().forEach(entity -> {
                try {
                    // 构建键：keyDuration|curveType|stressDirection
                    // curveType: 01-中档
                    // stressDirection: 01-上升, 02-下降
                    String key = entity.getKeyDuration() + "|" + entity.getCurveType() + "|" + entity.getStressDirection();

                    // 使用优化的值集解析方法
                    Map<Integer, BigDecimal> valueMap = ValueSetUtil.parseValueMap(entity.getFactorValSet());

                    // 存入并发Map
                    concurrentFactorMap.put(key, valueMap);

                    log.debug("【DV10计算-数据加载】解析关键久期折现因子值集，keyDuration={}，curveType={}，stressDirection={}，数据点数={}",
                            entity.getKeyDuration(), entity.getCurveType(), entity.getStressDirection(), valueMap.size());
                } catch (Exception e) {
                    log.error("【DV10计算-数据加载】解析关键久期折现因子值集失败，keyDuration={}，curveType={}，stressDirection={}",
                            entity.getKeyDuration(), entity.getCurveType(), entity.getStressDirection(), e);
                }
            });

            log.info("【DV10计算-数据加载】解析关键久期折现因子值集完成，共{}条记录，耗时：{}ms",
                    concurrentFactorMap.size(), System.currentTimeMillis() - parseStartTime);

            // 检查是否包含所有必要的关键期限点
            String[] keyDurations = {"0", "0.5", "1", "2", "3", "4", "5", "6", "7", "8", "10", "12", "15", "20", "25", "30", "35", "40", "45", "50"};
            String[] curveTypes = {"01", "02"}; // 中档和低档
            String[] stressDirections = {"01", "02"}; // 上升和下降

            for (String keyDuration : keyDurations) {
                for (String curveType : curveTypes) {
                    for (String stressDirection : stressDirections) {
                        String key = keyDuration + "|" + curveType + "|" + stressDirection;
                        if (!concurrentFactorMap.containsKey(key)) {
                            log.warn("【DV10计算-数据加载】缺少关键期限点{}的{}{}折现因子",
                                    keyDuration,
                                    "01".equals(curveType) ? "中档" : "低档",
                                    "01".equals(stressDirection) ? "上升" : "下降");
                        }
                    }
                }
            }

            return concurrentFactorMap;
        } catch (Exception e) {
            log.error("【DV10计算-数据加载】加载关键久期折现因子失败", e);
            return Collections.emptyMap();
        }
    }

    /**
     * 计算分中短负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @param cashFlowSummaryList 负债现金流汇总列表
     * @param factorMap 关键久期折现因子Map
     * @return 分中短负债基点价值DV10列表
     */
    private List<LiabilityDv10ByDurationEntity> calculateLiabilityDv10(
            String accountPeriod,
            List<LiabilityCashFlowSummaryEntity> cashFlowSummaryList,
            Map<String, Map<Integer, BigDecimal>> factorMap) {

        long startTime = System.currentTimeMillis();
        log.debug("【DV10计算-核心计算】开始计算分中短负债基点价值DV10，账期：{}", accountPeriod);

        try {
            // 按设计类型和中短期标志分组处理（不按现金流类型分组）
            log.debug("【DV10计算-核心计算】开始按设计类型和中短期标志分组，账期：{}", accountPeriod);
            Map<String, List<LiabilityCashFlowSummaryEntity>> groupedCashFlows = groupCashFlowsByDesignAndShortTerm(cashFlowSummaryList);
            log.debug("【DV10计算-核心计算】现金流分组完成，共{}个分组，耗时：{}ms",
                    groupedCashFlows.size(), System.currentTimeMillis() - startTime);

            if (groupedCashFlows.isEmpty()) {
                log.warn("【DV10计算-核心计算】没有有效的现金流分组数据，账期：{}", accountPeriod);
                return Collections.emptyList();
            }

            // 创建结果列表
            List<LiabilityDv10ByDurationEntity> resultList = new ArrayList<>();

            // 用于存储每个设计类型和中短期标志组合的流入和流出DV10值
            Map<String, Map<String, BigDecimal>> inflowDv10Map = new HashMap<>(); // key: designType|shortTermFlag, value: Map<keyDuration, dv10Value>
            Map<String, Map<String, BigDecimal>> outflowDv10Map = new HashMap<>(); // key: designType|shortTermFlag, value: Map<keyDuration, dv10Value>

            // 用于跟踪已经创建的记录，避免重复
            Set<String> createdRecords = new HashSet<>(); // key: accountPeriod|cashFlowType|designType|shortTermFlag|valueType

            // 顺序处理各个分组
            long groupStartTime = System.currentTimeMillis();
            log.debug("【DV10计算-核心计算】开始处理各个分组，共{}个分组", groupedCashFlows.size());

            // 顺序处理各个分组（每个分组包含同一设计类型和中短期标志的所有现金流）
            for (Map.Entry<String, List<LiabilityCashFlowSummaryEntity>> entry : groupedCashFlows.entrySet()) {
                try {
                    long singleGroupStartTime = System.currentTimeMillis();
                    String key = entry.getKey();
                    List<LiabilityCashFlowSummaryEntity> groupCashFlows = entry.getValue();

                    // 解析分组键
                    String[] keyParts = key.split("\\|");
                    String cashFlowType = keyParts[0];
                    String designType = keyParts[1];
                    String shortTermFlag = keyParts[2];

                    log.debug("【DV10计算-核心计算】处理分组：现金流类型={}，设计类型={}，中短期标志={}，记录数={}",
                            cashFlowType, designType, shortTermFlag, groupCashFlows.size());

                    // 计算当前分组的上升和下降现值
                    Map<String, Map<String, BigDecimal>> presentValues = calculatePresentValues(groupCashFlows, factorMap, cashFlowType);
                    log.debug("【DV10计算-核心计算】计算现值完成，上升现值期限点数量={}，下降现值期限点数量={}",
                            presentValues.get("up").size(), presentValues.get("down").size());

                    // 获取上升和下降现值
                    Map<String, BigDecimal> upValues = presentValues.get("up");
                    Map<String, BigDecimal> downValues = presentValues.get("down");

                    // 计算DV10值
                    Map<String, BigDecimal> dv10Values = calculateDv10Values(upValues, downValues);
                    log.debug("【DV10计算-核心计算】计算DV10值完成，期限点数量={}", dv10Values.size());

                    // 创建上升现值记录 - 注意：这里存储的是上升现值，而不是DV10值
                    log.info("【DV10计算-记录创建】创建上升现值记录：账期={}，现金流类型={}，设计类型={}，中短期标志={}，值类型=01",
                            accountPeriod, cashFlowType, designType, shortTermFlag);
                    logPeriodValues("上升现值", upValues);
                    createDv10Record(resultList, accountPeriod, cashFlowType, designType, shortTermFlag, "01", upValues, createdRecords);

                    // 创建下降现值记录 - 注意：这里存储的是下降现值，而不是DV10值
                    log.info("【DV10计算-记录创建】创建下降现值记录：账期={}，现金流类型={}，设计类型={}，中短期标志={}，值类型=02",
                            accountPeriod, cashFlowType, designType, shortTermFlag);
                    logPeriodValues("下降现值", downValues);
                    createDv10Record(resultList, accountPeriod, cashFlowType, designType, shortTermFlag, "02", downValues, createdRecords);

                    // 创建DV10值记录 - 注意：这里存储的是DV10值
                    log.info("【DV10计算-记录创建】创建DV10值记录：账期={}，现金流类型={}，设计类型={}，中短期标志={}，值类型=03",
                            accountPeriod, cashFlowType, designType, shortTermFlag);
                    logPeriodValues("DV10值", dv10Values);
                    createDv10Record(resultList, accountPeriod, cashFlowType, designType, shortTermFlag, "03", dv10Values, createdRecords);

                    // 存储上升、下降现值和DV10值，用于后续计算净值
                    String dsKey = designType + "|" + shortTermFlag;
                    if ("01".equals(cashFlowType)) {
                        // 流入记录
                        inflowDv10Map.put(dsKey, dv10Values);
                        inflowDv10Map.put(dsKey + "|01", upValues);
                        inflowDv10Map.put(dsKey + "|02", downValues);
                    } else if ("02".equals(cashFlowType)) {
                        // 流出记录
                        outflowDv10Map.put(dsKey, dv10Values);
                        outflowDv10Map.put(dsKey + "|01", upValues);
                        outflowDv10Map.put(dsKey + "|02", downValues);
                    }

                    log.debug("【DV10计算-核心计算】处理分组完成，设计类型={}，中短期标志={}，耗时：{}ms",
                            designType, shortTermFlag, System.currentTimeMillis() - singleGroupStartTime);
                } catch (Exception e) {
                    log.error("【DV10计算-核心计算】处理分组失败，key={}", entry.getKey(), e);
                }
            }

            // 处理完所有分组后，计算净值DV10
            log.debug("【DV10计算-净值计算】开始计算净值DV10，流入分组数={}，流出分组数={}",
                    inflowDv10Map.size(), outflowDv10Map.size());

            // 获取所有设计类型和中短期标志组合
            Set<String> allDsKeys = new HashSet<>();
            allDsKeys.addAll(inflowDv10Map.keySet());
            allDsKeys.addAll(outflowDv10Map.keySet());

            // 为每个组合计算净值DV10
            for (String dsKey : allDsKeys) {
                String[] keyParts = dsKey.split("\\|");
                String designType = keyParts[0];
                String shortTermFlag = keyParts[1];

                Map<String, BigDecimal> inflowValues = inflowDv10Map.getOrDefault(dsKey, Collections.emptyMap());
                Map<String, BigDecimal> outflowValues = outflowDv10Map.getOrDefault(dsKey, Collections.emptyMap());

                // 获取所有期限点
                Set<String> allKeyDurations = new HashSet<>();
                allKeyDurations.addAll(inflowValues.keySet());
                allKeyDurations.addAll(outflowValues.keySet());

                // 计算净值
                Map<String, BigDecimal> netValues = new HashMap<>();
                for (String keyDuration : allKeyDurations) {
                    BigDecimal inflowValue = inflowValues.getOrDefault(keyDuration, BigDecimal.ZERO);
                    BigDecimal outflowValue = outflowValues.getOrDefault(keyDuration, BigDecimal.ZERO);
                    BigDecimal netValue = inflowValue.add(outflowValue);
                    netValues.put(keyDuration, netValue);
                }

                // 创建净值记录 - 注意：净值不再单独存储，而是分别存储在流入和流出记录中
                log.info("【DV10计算-记录创建】计算净值：账期={}，设计类型={}，中短期标志={}",
                        accountPeriod, designType, shortTermFlag);
                logPeriodValues("净值", netValues);

                // 计算并创建流入上升现值记录 - 注意：这里存储的是上升现值，而不是DV10值
                Map<String, BigDecimal> inflowUpValues = inflowDv10Map.getOrDefault(dsKey + "|01", Collections.emptyMap());
                log.info("【DV10计算-记录创建】创建流入上升现值记录：账期={}，现金流类型=01，设计类型={}，中短期标志={}，值类型=01",
                        accountPeriod, designType, shortTermFlag);
                logPeriodValues("流入上升现值", inflowUpValues);
                createDv10Record(resultList, accountPeriod, "01", designType, shortTermFlag, "01", inflowUpValues, createdRecords);

                // 计算并创建流出上升现值记录 - 注意：这里存储的是上升现值，而不是DV10值
                Map<String, BigDecimal> outflowUpValues = outflowDv10Map.getOrDefault(dsKey + "|01", Collections.emptyMap());
                log.info("【DV10计算-记录创建】创建流出上升现值记录：账期={}，现金流类型=02，设计类型={}，中短期标志={}，值类型=01",
                        accountPeriod, designType, shortTermFlag);
                logPeriodValues("流出上升现值", outflowUpValues);
                createDv10Record(resultList, accountPeriod, "02", designType, shortTermFlag, "01", outflowUpValues, createdRecords);

                // 计算并创建流入下降现值记录 - 注意：这里存储的是下降现值，而不是DV10值
                Map<String, BigDecimal> inflowDownValues = inflowDv10Map.getOrDefault(dsKey + "|02", Collections.emptyMap());
                log.info("【DV10计算-记录创建】创建流入下降现值记录：账期={}，现金流类型=01，设计类型={}，中短期标志={}，值类型=02",
                        accountPeriod, designType, shortTermFlag);
                logPeriodValues("流入下降现值", inflowDownValues);
                createDv10Record(resultList, accountPeriod, "01", designType, shortTermFlag, "02", inflowDownValues, createdRecords);

                // 计算并创建流出下降现值记录 - 注意：这里存储的是下降现值，而不是DV10值
                Map<String, BigDecimal> outflowDownValues = outflowDv10Map.getOrDefault(dsKey + "|02", Collections.emptyMap());
                log.info("【DV10计算-记录创建】创建流出下降现值记录：账期={}，现金流类型=02，设计类型={}，中短期标志={}，值类型=02",
                        accountPeriod, designType, shortTermFlag);
                logPeriodValues("流出下降现值", outflowDownValues);
                createDv10Record(resultList, accountPeriod, "02", designType, shortTermFlag, "02", outflowDownValues, createdRecords);

                // 计算并创建流入DV10值记录 - 注意：这里存储的是DV10值
                Map<String, BigDecimal> inflowDv10Values = inflowDv10Map.getOrDefault(dsKey, Collections.emptyMap());
                log.info("【DV10计算-记录创建】创建流入DV10值记录：账期={}，现金流类型=01，设计类型={}，中短期标志={}，值类型=03",
                        accountPeriod, designType, shortTermFlag);
                logPeriodValues("流入DV10值", inflowDv10Values);
                createDv10Record(resultList, accountPeriod, "01", designType, shortTermFlag, "03", inflowDv10Values, createdRecords);

                // 计算并创建流出DV10值记录 - 注意：这里存储的是DV10值
                Map<String, BigDecimal> outflowDv10Values = outflowDv10Map.getOrDefault(dsKey, Collections.emptyMap());
                log.info("【DV10计算-记录创建】创建流出DV10值记录：账期={}，现金流类型=02，设计类型={}，中短期标志={}，值类型=03",
                        accountPeriod, designType, shortTermFlag);
                logPeriodValues("流出DV10值", outflowDv10Values);
                createDv10Record(resultList, accountPeriod, "02", designType, shortTermFlag, "03", outflowDv10Values, createdRecords);
                log.debug("【DV10计算-净值计算】创建净值记录：设计类型={}，中短期标志={}，期限点数量={}",
                        designType, shortTermFlag, netValues.size());
            }

            log.debug("【DV10计算-核心计算】DV10计算完成，共生成{}条记录，总耗时：{}ms",
                    resultList.size(), System.currentTimeMillis() - groupStartTime);
            return resultList;
        } catch (Exception e) {
            log.error("【DV10计算-核心计算】计算分中短负债基点价值DV10失败", e);
            return Collections.emptyList();
        }
    }

    /**
     * 按现金流类型、设计类型和中短期标志分组
     *
     * @param cashFlowSummaryList 负债现金流汇总列表
     * @return 分组后的Map，key为cashFlowType|designType|shortTermFlag
     */
    private Map<String, List<LiabilityCashFlowSummaryEntity>> groupCashFlowsByDesignAndShortTerm(
            List<LiabilityCashFlowSummaryEntity> cashFlowSummaryList) {

        Map<String, List<LiabilityCashFlowSummaryEntity>> groupedCashFlows = new HashMap<>();

        for (LiabilityCashFlowSummaryEntity entity : cashFlowSummaryList) {
            // 按现金流类型、设计类型和中短期标志分组
            String key = entity.getCashFlowType() + "|" + entity.getDesignType() + "|" + entity.getIsShortTerm();

            if (!groupedCashFlows.containsKey(key)) {
                groupedCashFlows.put(key, new ArrayList<>());
            }

            groupedCashFlows.get(key).add(entity);
        }

        log.info("【DV10计算-分组】按现金流类型、设计类型和中短期标志分组，共{}个分组", groupedCashFlows.size());
        for (Map.Entry<String, List<LiabilityCashFlowSummaryEntity>> entry : groupedCashFlows.entrySet()) {
            log.info("【DV10计算-分组】分组key={}，记录数={}", entry.getKey(), entry.getValue().size());
        }

        return groupedCashFlows;
    }



    /**
     * 计算现值
     *
     * 根据文档步骤2中的计算现值部分：
     * 流入现金流：PV_up[i] = ∑[j=1,1272]CF_in[j]×DF_up[i,j-1]
     * 流出现金流：PV_up[i] = ∑[j=0,1272]CF_out[j]×DF_up[i,j]
     *
     * @param cashFlowList 现金流列表
     * @param factorMap 折现因子Map
     * @param cashFlowType 现金流类型
     * @return 包含上升和下降现值的Map，key为"up"或"down"，value为各期限点的现值Map
     */
    private Map<String, Map<String, BigDecimal>> calculatePresentValues(
            List<LiabilityCashFlowSummaryEntity> cashFlowList,
            Map<String, Map<Integer, BigDecimal>> factorMap,
            String cashFlowType) {

        long startTime = System.currentTimeMillis();
        log.debug("【DV10计算-DV10值计算】开始计算DV10值，现金流类型：{}", cashFlowType);

        // 定义关键期限点列表
        String[] keyDurations = {"0", "0.5", "1", "2", "3", "4", "5", "6", "7", "8", "10", "12", "15", "20", "25", "30", "35", "40", "45", "50"};

        // 结果Map，包含上升和下降现值
        Map<String, Map<String, BigDecimal>> resultMap = new HashMap<>(2);
        Map<String, BigDecimal> upPresentValues = new HashMap<>(keyDurations.length);
        Map<String, BigDecimal> downPresentValues = new HashMap<>(keyDurations.length);
        resultMap.put("up", upPresentValues);
        resultMap.put("down", downPresentValues);

        try {
            // 首先合并所有现金流
            Map<Integer, BigDecimal> mergedCashFlowValueMap = new HashMap<>(1273); // 预分配足够大小

            // 合并所有现金流记录
            long parseStartTime = System.currentTimeMillis();
            log.debug("【DV10计算-现值计算】开始解析和合并现金流值集，记录数：{}", cashFlowList.size());

            // 并行处理解析和合并现金流值集
            Map<Integer, BigDecimal> concurrentMergedMap = new ConcurrentHashMap<>(1273);

            cashFlowList.parallelStream().forEach(cashFlow -> {
                try {
                    // 解析现金流值集
                    Map<Integer, BigDecimal> cashFlowValueMap = ValueSetUtil.parseValueMap(cashFlow.getCashValSet());

                    // 合并现金流值
                    for (Map.Entry<Integer, BigDecimal> entry : cashFlowValueMap.entrySet()) {
                        Integer index = entry.getKey();
                        BigDecimal value = entry.getValue();

                        concurrentMergedMap.merge(index, value, BigDecimal::add);
                    }
                } catch (Exception e) {
                    log.error("【DV10计算-现值计算】解析现金流值集失败", e);
                }
            });

            // 将并行处理结果转移到结果Map
            mergedCashFlowValueMap.putAll(concurrentMergedMap);

            log.debug("【DV10计算-现值计算】解析和合并现金流值集完成，共{}个数据点，耗时：{}ms",
                    mergedCashFlowValueMap.size(), System.currentTimeMillis() - parseStartTime);

            // 确定计算参数
            boolean isInflow = "01".equals(cashFlowType);
            int maxIndex = 1272;

            // 过滤零值，提高计算效率
            Map<Integer, BigDecimal> filteredCashFlowMap = new HashMap<>();
            for (Map.Entry<Integer, BigDecimal> entry : mergedCashFlowValueMap.entrySet()) {
                if (entry.getValue().compareTo(BigDecimal.ZERO) != 0) {
                    filteredCashFlowMap.put(entry.getKey(), entry.getValue());
                }
            }

            if (filteredCashFlowMap.isEmpty()) {
                log.debug("【DV10计算-现值计算】没有有效的现金流数据，现金流类型：{}", cashFlowType);
                // 为所有期限点设置默认值
                for (String keyDuration : keyDurations) {
                    upPresentValues.put(keyDuration, BigDecimal.ZERO);
                    downPresentValues.put(keyDuration, BigDecimal.ZERO);
                }
                return resultMap;
            }

            log.debug("【DV10计算-现值计算】有效现金流数据点数量: {}", filteredCashFlowMap.size());

            // 预先计算所有关键期限点的现值
            long calcStartTime = System.currentTimeMillis();
            log.debug("【DV10计算-现值计算】开始计算各关键期限点的现值，共{}个期限点", keyDurations.length);

            // 逐个处理各个关键期限点的计算，确保计算正确
            for (String keyDuration : keyDurations) {
                try {
                    long pointStartTime = System.currentTimeMillis();

                    // 获取上升和下降折现因子
                    // 获取中档曲线的折现因子
                    String upFactorKeyMiddle = keyDuration + "|01|01"; // 中档，上升
                    String downFactorKeyMiddle = keyDuration + "|01|02"; // 中档，下降

                    // 获取低档曲线的折现因子
                    String upFactorKeyLow = keyDuration + "|02|01"; // 低档，上升
                    String downFactorKeyLow = keyDuration + "|02|02"; // 低档，下降

                    // 根据中短期险种标志选择折现因子
                    // 从第一条现金流记录中获取中短期险种标志
                    String shortTermFlag = "N"; // 默认为非中短期险种
                    if (!cashFlowList.isEmpty()) {
                        shortTermFlag = cashFlowList.get(0).getIsShortTerm();
                    }

                    Map<Integer, BigDecimal> upFactorValueMap;
                    Map<Integer, BigDecimal> downFactorValueMap;

                    if ("Y".equals(shortTermFlag)) {
                        // 中短期险种使用低档曲线折现因子
                        upFactorValueMap = factorMap.get(upFactorKeyLow);
                        downFactorValueMap = factorMap.get(downFactorKeyLow);
                        log.debug("【DV10计算-现值计算】中短期险种(Y)使用低档曲线折现因子，关键期限点：{}", keyDuration);
                    } else {
                        // 非中短期险种使用中档曲线折现因子
                        upFactorValueMap = factorMap.get(upFactorKeyMiddle);
                        downFactorValueMap = factorMap.get(downFactorKeyMiddle);
                        log.debug("【DV10计算-现值计算】非中短期险种(N)使用中档曲线折现因子，关键期限点：{}", keyDuration);
                    }

                    // 如果选择的折现因子不存在，尝试使用另一种曲线类型的折现因子
                    if (upFactorValueMap == null) {
                        upFactorValueMap = "Y".equals(shortTermFlag) ?
                            factorMap.get(upFactorKeyMiddle) : factorMap.get(upFactorKeyLow);
                        log.debug("【DV10计算-现值计算】首选折现因子不存在，使用备选上升折现因子，关键期限点：{}", keyDuration);
                    }

                    if (downFactorValueMap == null) {
                        downFactorValueMap = "Y".equals(shortTermFlag) ?
                            factorMap.get(downFactorKeyMiddle) : factorMap.get(downFactorKeyLow);
                        log.debug("【DV10计算-现值计算】首选折现因子不存在，使用备选下降折现因子，关键期限点：{}", keyDuration);
                    }

                    if (upFactorValueMap != null && downFactorValueMap != null) {
                        // 计算上升情况下的现值
                        BigDecimal pvUp = calculatePresentValueTraditional(filteredCashFlowMap, upFactorValueMap, isInflow, maxIndex, keyDuration);

                        // 计算下降情况下的现值
                        BigDecimal pvDown = calculatePresentValueTraditional(filteredCashFlowMap, downFactorValueMap, isInflow, maxIndex, keyDuration);

                        // 存储上升和下降现值
                        upPresentValues.put(keyDuration, pvUp);
                        downPresentValues.put(keyDuration, pvDown);

                        log.debug("【DV10计算-现值计算】计算关键期限点{}的现值完成，上升现值={}，下降现值={}，耗时：{}ms",
                                keyDuration, pvUp, pvDown, System.currentTimeMillis() - pointStartTime);
                    } else {
                        if (upFactorValueMap == null) {
                            log.debug("【DV10计算-现值计算】未找到关键期限点{}的上升折现因子", keyDuration);
                        }
                        if (downFactorValueMap == null) {
                            log.debug("【DV10计算-现值计算】未找到关键期限点{}的下降折现因子", keyDuration);
                        }
                        upPresentValues.put(keyDuration, BigDecimal.ZERO);
                        downPresentValues.put(keyDuration, BigDecimal.ZERO);
                    }
                } catch (Exception e) {
                    log.error("【DV10计算-现值计算】计算关键期限点{}的现值失败", keyDuration, e);
                    upPresentValues.put(keyDuration, BigDecimal.ZERO);
                    downPresentValues.put(keyDuration, BigDecimal.ZERO);
                }
            }

            log.debug("【DV10计算-现值计算】计算现值完成，共{}个期限点，耗时：{}ms",
                    upPresentValues.size(), System.currentTimeMillis() - calcStartTime);
        } catch (Exception e) {
            log.error("【DV10计算-现值计算】计算现值异常", e);
            // 发生异常时，为所有关键期限点设置默认值
            for (String keyDuration : keyDurations) {
                upPresentValues.put(keyDuration, BigDecimal.ZERO);
                downPresentValues.put(keyDuration, BigDecimal.ZERO);
            }
        }

        log.debug("【DV10计算-现值计算】计算现值总耗时：{}ms", System.currentTimeMillis() - startTime);
        return resultMap;
    }

    // 已删除未使用的calculatePresentValue方法

    /**
     * 计算DV10值
     *
     * 根据文档步骤2中的计算DV10值部分：
     * DV10[i] = -(PV_up[i] - PV_down[i])/2
     *
     * @param upValues 上升现值Map
     * @param downValues 下降现值Map
     * @return DV10值Map
     */
    private Map<String, BigDecimal> calculateDv10Values(
            Map<String, BigDecimal> upValues,
            Map<String, BigDecimal> downValues) {

        Map<String, BigDecimal> dv10Values = new HashMap<>(upValues.size());

        // 获取所有期限点
        Set<String> allKeyDurations = new HashSet<>();
        allKeyDurations.addAll(upValues.keySet());
        allKeyDurations.addAll(downValues.keySet());

        // 计算每个期限点的DV10值
        for (String keyDuration : allKeyDurations) {
            BigDecimal pvUp = upValues.getOrDefault(keyDuration, BigDecimal.ZERO);
            BigDecimal pvDown = downValues.getOrDefault(keyDuration, BigDecimal.ZERO);

            // 计算DV10值: -(PV_up - PV_down)/2
            BigDecimal diff = pvUp.subtract(pvDown);
            BigDecimal dv10 = diff.negate().divide(new BigDecimal("2"), CalculationConstant.PROCESS_SCALE, RoundingMode.HALF_UP);

            // 记录原始计算结果，便于调试
            log.debug("【DV10计算-DV10值计算】关键期限点{}的原始DV10值：pvUp={}, pvDown={}, diff={}, dv10={}",
                    keyDuration, pvUp, pvDown, diff, dv10);

            // 检查DV10值是否超出数据库字段范围
            dv10 = checkAndAdjustDv10Value(dv10, keyDuration);

            // 存储结果
            dv10Values.put(keyDuration, dv10);
        }

        return dv10Values;
    }

    /**
     * 使用传统方式计算现值
     * 严格按照文档要求处理流入和流出现金流的索引差异
     *
     * 根据文档步骤2中的计算DV10现值部分：
     * 流入现金流：PV_up[i] = ∑[j=1,1272]CF_in[j]×DF_up[i,j-1]
     * 流出现金流：PV_up[i] = ∑[j=0,1272]CF_out[j]×DF_up[i,j]
     *
     * @param cashFlowValueMap 现金流值Map
     * @param factorValueMap 折现因子值Map
     * @param isInflow 是否为流入现金流
     * @param maxIndex 最大索引
     * @param keyDuration 关键期限点，用于日志记录
     * @return 现值
     */
    private BigDecimal calculatePresentValueTraditional(
            Map<Integer, BigDecimal> cashFlowValueMap,
            Map<Integer, BigDecimal> factorValueMap,
            boolean isInflow,
            int maxIndex,
            String keyDuration) {

        long startTime = System.currentTimeMillis();
        log.debug("【DV10计算-现值计算】开始计算现值，期限点：{}，是否为流入：{}", keyDuration, isInflow);

        // 检查输入数据
        if (cashFlowValueMap == null || cashFlowValueMap.isEmpty()) {
            log.warn("【DV10计算-现值计算】现金流值Map为空，期限点：{}，是否为流入：{}", keyDuration, isInflow);
            return BigDecimal.ZERO;
        }

        if (factorValueMap == null || factorValueMap.isEmpty()) {
            log.warn("【DV10计算-现值计算】折现因子值Map为空，期限点：{}，是否为流入：{}", keyDuration, isInflow);
            return BigDecimal.ZERO;
        }

        // 记录输入数据的基本信息
        log.debug("【DV10计算-现值计算】现金流值Map大小：{}，折现因子值Map大小：{}，期限点：{}，是否为流入：{}",
                cashFlowValueMap.size(), factorValueMap.size(), keyDuration, isInflow);

        BigDecimal presentValue = BigDecimal.ZERO;
        int nonZeroCount = 0;
        int calculationCount = 0;
        BigDecimal largestProduct = BigDecimal.ZERO; // 记录最大的乘积
        int largestProductIndex = -1; // 记录最大乘积对应的索引

        if (isInflow) { // 流入
            // 严格按照文档要求：流入现金流从序号1开始，对应折现因子从序号0开始
            // 公式: PV = ∑[j=1,1272]CF_in[j]×DF[j-1]
            for (int j = 1; j <= maxIndex; j++) {
                BigDecimal cashFlow = cashFlowValueMap.getOrDefault(j, BigDecimal.ZERO);
                if (cashFlow.compareTo(BigDecimal.ZERO) != 0) {
                    nonZeroCount++;
                    int factorIndex = j - 1; // 折现因子索引 = 现金流索引 - 1
                    BigDecimal factor = factorValueMap.getOrDefault(factorIndex, BigDecimal.ZERO);

                    // 使用高精度进行乘法计算
                    BigDecimal product = cashFlow.multiply(factor);
                    presentValue = presentValue.add(product);
                    calculationCount++;

                    // 记录最大的乘积
                    if (product.abs().compareTo(largestProduct.abs()) > 0) {
                        largestProduct = product;
                        largestProductIndex = j;
                    }

                    // 对于0期限点，记录前10个非零现金流的计算详情
                    if ("0".equals(keyDuration) && nonZeroCount <= 10) {
                        log.debug("【DV10计算-现值计算】流入现金流计算详情[{}]：j={}, 现金流={}, 折现因子索引={}, 折现因子={}, 乘积={}, 累计现值={}",
                                nonZeroCount, j, cashFlow, factorIndex, factor, product, presentValue);
                    }

                    if (calculationCount % 100 == 0) {
                        log.debug("【DV10计算-现值计算】流入现金流计算进度：已处理{}个非零现金流", calculationCount);
                    }
                }
            }
        } else { // 流出
            // 严格按照文档要求：流出现金流从序号0开始，对应折现因子也从序号0开始
            // 公式: PV = ∑[j=0,1272]CF_out[j]×DF[j]
            for (int j = 0; j <= maxIndex; j++) {
                BigDecimal cashFlow = cashFlowValueMap.getOrDefault(j, BigDecimal.ZERO);
                if (cashFlow.compareTo(BigDecimal.ZERO) != 0) {
                    nonZeroCount++;
                    int factorIndex = j; // 折现因子索引 = 现金流索引
                    BigDecimal factor = factorValueMap.getOrDefault(factorIndex, BigDecimal.ZERO);

                    // 使用高精度进行乘法计算
                    BigDecimal product = cashFlow.multiply(factor);
                    presentValue = presentValue.add(product);
                    calculationCount++;

                    // 记录最大的乘积
                    if (product.abs().compareTo(largestProduct.abs()) > 0) {
                        largestProduct = product;
                        largestProductIndex = j;
                    }

                    // 对于0期限点，记录前10个非零现金流的计算详情
                    if ("0".equals(keyDuration) && nonZeroCount <= 10) {
                        log.debug("【DV10计算-现值计算】流出现金流计算详情[{}]：j={}, 现金流={}, 折现因子索引={}, 折现因子={}, 乘积={}, 累计现值={}",
                                nonZeroCount, j, cashFlow, factorIndex, factor, product, presentValue);
                    }

                    if (calculationCount % 100 == 0) {
                        log.debug("【DV10计算-现值计算】流出现金流计算进度：已处理{}个非零现金流", calculationCount);
                    }
                }
            }
        }

        // 记录最大乘积的详情
        if (largestProductIndex >= 0) {
            int factorIndex = isInflow ? largestProductIndex - 1 : largestProductIndex;
            log.debug("【DV10计算-现值计算】最大乘积详情：期限点={}, 是否为流入={}, 索引={}, 现金流={}, 折现因子索引={}, 折现因子={}, 乘积={}",
                    keyDuration, isInflow, largestProductIndex,
                    cashFlowValueMap.getOrDefault(largestProductIndex, BigDecimal.ZERO),
                    factorIndex, factorValueMap.getOrDefault(factorIndex, BigDecimal.ZERO),
                    largestProduct);
        }

        // 确保结果精度一致
        BigDecimal result = presentValue.setScale(CalculationConstant.PROCESS_SCALE, RoundingMode.HALF_EVEN);

        log.debug("【DV10计算-现值计算】计算现值完成，期限点：{}，是否为流入：{}，非零现金流数量：{}，实际计算次数：{}，现值结果：{}，耗时：{}ms",
                keyDuration, isInflow, nonZeroCount, calculationCount, result, System.currentTimeMillis() - startTime);
        return result;
    }







    /**
     * 创建单个DV10记录
     *
     * @param resultList 结果列表
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param designType 设计类型
     * @param shortTermFlag 中短期标志
     * @param valueType 现值类型
     * @param dv10Values DV10值Map
     * @param createdRecords 已创建记录的集合，用于避免重复
     */
    private void createDv10Record(
            List<LiabilityDv10ByDurationEntity> resultList,
            String accountPeriod,
            String cashFlowType,
            String designType,
            String shortTermFlag,
            String valueType,
            Map<String, BigDecimal> dv10Values,
            Set<String> createdRecords) {

        // 将设计类型转换为代码值
        String designTypeCode = convertDesignTypeToCode(designType);

        // 创建唯一键，用于检查是否已经创建过该记录
        String recordKey = accountPeriod + "|" + cashFlowType + "|" + designTypeCode + "|" + shortTermFlag + "|" + valueType;

        // 检查是否已经创建过该记录
        if (createdRecords.contains(recordKey)) {
            log.warn("【DV10计算-记录创建】跳过重复记录：账期={}，现金流类型={}，设计类型={}，中短期标志={}，值类型={}",
                    accountPeriod, cashFlowType, designTypeCode, shortTermFlag, valueType);
            return;
        }

        // 检查DV10值是否全为0
        boolean allZero = true;
        for (BigDecimal value : dv10Values.values()) {
            if (value != null && value.compareTo(BigDecimal.ZERO) != 0) {
                allZero = false;
                break;
            }
        }

        // 如果所有值都为0，记录日志但仍然创建记录（仅在调试级别）
        if (allZero && log.isDebugEnabled()) {
            log.debug("【DV10计算-记录创建】所有DV10值都为0，账期={}，现金流类型={}，设计类型={}，中短期标志={}，值类型={}",
                    accountPeriod, cashFlowType, designTypeCode, shortTermFlag, valueType);
        }

        Date now = new Date();

        log.debug("【DV10计算-记录创建】设计类型转换：{}转换为代码{}", designType, designTypeCode);

        LiabilityDv10ByDurationEntity entity = new LiabilityDv10ByDurationEntity();
        entity.setAccountPeriod(accountPeriod);
        entity.setCashFlowType(cashFlowType);
        entity.setDesignType(designTypeCode);
        entity.setShortTermFlag(shortTermFlag);
        entity.setValueType(valueType);

        // 设置DV10值
        setDv10Values(entity, dv10Values);

        // 设置创建和更新时间
        entity.setCreateTime(now);
        entity.setUpdateTime(now); // 添加更新时间

        // 设置创建者和更新者（如果需要）
        entity.setCreateBy("system");
        entity.setUpdateBy("system");

        entity.setIsDel(0);

        // 记录创建的记录信息
        String valueTypeDesc = "01".equals(valueType) ? "上升现值" : ("02".equals(valueType) ? "下降现值" : "DV10值");
        log.debug("【DV10计算-记录创建】创建{}记录：账期={}，现金流类型={}，设计类型={}，中短期标志={}，值类型={}，期限点数量={}",
                valueTypeDesc, accountPeriod, cashFlowType, designTypeCode, shortTermFlag, valueType, dv10Values.size());

        // 添加到已创建记录集合
        createdRecords.add(recordKey);

        resultList.add(entity);
    }

    // 已删除未使用的createDv10Records方法

    /**
     * 将设计类型转换为代码值
     *
     * @param designType 设计类型
     * @return 设计类型代码
     */
    private String convertDesignTypeToCode(String designType) {
        if (designType == null) {
            return "00";
        }

        switch (designType) {
            case "传统险":
                return "01";
            case "分红险":
                return "02";
            case "万能险":
                return "03";
            case "投连险":
                return "04";
            default:
                // 如果已经是代码形式（如"01"、"02"等），则直接返回
                if (designType.matches("^\\d{2}$")) {
                    return designType;
                }
                // 否则截取前两个字符，如果不足两个字符，则补0
                return (designType.length() >= 2) ?
                        designType.substring(0, 2) :
                        (designType + "0").substring(0, 2);
        }
    }

    /**
     * 设置DV10值
     *
     * @param entity DV10实体
     * @param values 值Map，根据valueType的不同，可能是上升现值、下降现值或DV10值
     */
    private void setDv10Values(LiabilityDv10ByDurationEntity entity, Map<String, BigDecimal> values) {
        // 直接使用传入的值，不进行DV10计算
        // 根据valueType的不同，values可能是上升现值、下降现值或DV10值
        entity.setTerm0(checkAndAdjustDv10Value(values.getOrDefault("0", BigDecimal.ZERO), "0"));
        entity.setTerm05(checkAndAdjustDv10Value(values.getOrDefault("0.5", BigDecimal.ZERO), "0.5"));
        entity.setTerm1(checkAndAdjustDv10Value(values.getOrDefault("1", BigDecimal.ZERO), "1"));
        entity.setTerm2(checkAndAdjustDv10Value(values.getOrDefault("2", BigDecimal.ZERO), "2"));
        entity.setTerm3(checkAndAdjustDv10Value(values.getOrDefault("3", BigDecimal.ZERO), "3"));
        entity.setTerm4(checkAndAdjustDv10Value(values.getOrDefault("4", BigDecimal.ZERO), "4"));
        entity.setTerm5(checkAndAdjustDv10Value(values.getOrDefault("5", BigDecimal.ZERO), "5"));
        entity.setTerm6(checkAndAdjustDv10Value(values.getOrDefault("6", BigDecimal.ZERO), "6"));
        entity.setTerm7(checkAndAdjustDv10Value(values.getOrDefault("7", BigDecimal.ZERO), "7"));
        entity.setTerm8(checkAndAdjustDv10Value(values.getOrDefault("8", BigDecimal.ZERO), "8"));
        entity.setTerm10(checkAndAdjustDv10Value(values.getOrDefault("10", BigDecimal.ZERO), "10"));
        entity.setTerm12(checkAndAdjustDv10Value(values.getOrDefault("12", BigDecimal.ZERO), "12"));
        entity.setTerm15(checkAndAdjustDv10Value(values.getOrDefault("15", BigDecimal.ZERO), "15"));
        entity.setTerm20(checkAndAdjustDv10Value(values.getOrDefault("20", BigDecimal.ZERO), "20"));
        entity.setTerm25(checkAndAdjustDv10Value(values.getOrDefault("25", BigDecimal.ZERO), "25"));
        entity.setTerm30(checkAndAdjustDv10Value(values.getOrDefault("30", BigDecimal.ZERO), "30"));
        entity.setTerm35(checkAndAdjustDv10Value(values.getOrDefault("35", BigDecimal.ZERO), "35"));
        entity.setTerm40(checkAndAdjustDv10Value(values.getOrDefault("40", BigDecimal.ZERO), "40"));
        entity.setTerm45(checkAndAdjustDv10Value(values.getOrDefault("45", BigDecimal.ZERO), "45"));
        entity.setTerm50(checkAndAdjustDv10Value(values.getOrDefault("50", BigDecimal.ZERO), "50"));
    }

    /**
     * 检查并调整值，确保不超出数据库字段范围
     * 数据库字段类型为decimal(18,10)，最多可存储18位数字，其中10位是小数部分
     *
     * @param value 值（可能是上升现值、下降现值或DV10值）
     * @param keyDuration 关键期限点
     * @return 调整后的值
     */
    private BigDecimal checkAndAdjustDv10Value(BigDecimal value, String keyDuration) {
        if (value == null) {
            return BigDecimal.ZERO;
        }

        // 记录原始值，便于调试
        BigDecimal originalValue = value;

        // 数据库字段为decimal(18,10)，理论上最大值约为10^8
        // 但实际上我们需要支持更大的值，如251亿（25149662179.1768）
        // 注意：这里我们不再限制最大值，而是在日志中记录超出数据库字段范围的值
        // 后续需要修改数据库表定义，将decimal(18,10)改为decimal(30,10)或更大
        BigDecimal dbMaxValue = new BigDecimal("99999999.9999999999"); // 数据库字段的理论最大值
        BigDecimal dbMinValue = dbMaxValue.negate(); // 数据库字段的理论最小值

        // 检查是否超出数据库字段范围，但不进行截断
        if (value.compareTo(dbMaxValue) > 0 || value.compareTo(dbMinValue) < 0) {
            // 记录警告日志，提醒用户需要修改数据库表定义
            log.warn("【DV10计算-值超出范围】关键期限点{}的值{}超出数据库字段范围[-99999999.9999999999, 99999999.9999999999]，" +
                    "请考虑修改数据库表定义，将decimal(18,10)改为decimal(30,10)或更大",
                    keyDuration, value);
        }

        // 不再使用固定的最大值限制，使用非常大的值，实际上不会限制
        BigDecimal maxValue = new BigDecimal("9999999999999999999999.9999999999");
        BigDecimal minValue = maxValue.negate();

        // 检查是否超出范围
        if (value.compareTo(maxValue) > 0) {
            // 只在调试级别记录日志，避免大量警告日志
            log.debug("【DV10计算-值调整】关键期限点{}的值超出最大值范围，已调整为最大值",
                    keyDuration);
            value = maxValue;
        } else if (value.compareTo(minValue) < 0) {
            // 只在调试级别记录日志，避免大量警告日志
            log.debug("【DV10计算-值调整】关键期限点{}的值超出最小值范围，已调整为最小值",
                    keyDuration);
            value = minValue;
        }

        // 确保小数位数不超过10位，使用HALF_EVEN舍入模式（银行家舍入法），更适合财务计算
        BigDecimal adjustedValue = value.setScale(10, RoundingMode.HALF_EVEN);

        // 如果调整后的值与原始值差异较大，记录日志（仅在trace级别）
        if (adjustedValue.subtract(originalValue).abs().compareTo(new BigDecimal("0.000001")) > 0 && log.isTraceEnabled()) {
            log.trace("【DV10计算-值调整】关键期限点{}的值从{}调整为{}",
                    keyDuration, originalValue, adjustedValue);
        }

        return adjustedValue;
    }

    /**
     * 记录各期限点的值
     *
     * @param valueType 值类型描述
     * @param values 各期限点的值Map
     */
    private void logPeriodValues(String valueType, Map<String, BigDecimal> values) {
        // 定义关键期限点列表，按顺序排列
        String[] keyDurations = {"0", "0.5", "1", "2", "3", "4", "5", "6", "7", "8", "10", "12", "15", "20", "25", "30", "35", "40", "45", "50"};

        // 构建日志消息
        StringBuilder sb = new StringBuilder();
        sb.append("【DV10计算-期限点值】").append(valueType).append("：");

        for (String keyDuration : keyDurations) {
            BigDecimal value = values.getOrDefault(keyDuration, BigDecimal.ZERO);
            sb.append(keyDuration).append("=").append(value).append(", ");
        }

        // 移除最后的逗号和空格
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }

        // 输出日志
        log.info(sb.toString());
    }
}
