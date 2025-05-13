package com.xl.alm.job.dur.service.impl;

import com.xl.alm.job.dur.constant.CalculationConstant;
import com.xl.alm.job.dur.entity.DiscountFactorEntity;
import com.xl.alm.job.dur.entity.LiabilityCashFlowEntity;
import com.xl.alm.job.dur.entity.LiabilityCashFlowSummaryEntity;
import com.xl.alm.job.dur.mapper.DiscountFactorMapper;
import com.xl.alm.job.dur.mapper.LiabilityCashFlowMapper;
import com.xl.alm.job.dur.mapper.LiabilityCashFlowSummaryMapper;
import com.xl.alm.job.dur.service.LiabilityCashFlowSummaryService;
import com.xl.alm.job.dur.util.ValueSetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.FieldVector;
import org.apache.commons.math3.util.BigReal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负债现金流汇总及现值计算服务实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class LiabilityCashFlowSummaryServiceImpl implements LiabilityCashFlowSummaryService {

    @Autowired
    private LiabilityCashFlowMapper liabilityCashFlowMapper;

    @Autowired
    private DiscountFactorMapper discountFactorMapper;

    @Autowired
    private LiabilityCashFlowSummaryMapper liabilityCashFlowSummaryMapper;

    /**
     * 执行负债现金流汇总及现值计算
     *
     * @param accountPeriod 账期
     * @return 处理结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateLiabilityCashFlowSummary(String accountPeriod) {
        log.info("开始执行负债现金流汇总及现值计算，账期：{}", accountPeriod);
        try {
            // 步骤1：加载因子表
            Map<String, Map<Integer, BigDecimal>> factorMap = loadDiscountFactors(accountPeriod);
            log.info("加载因子表完成，共加载{}条记录", factorMap.size());

            // 步骤2：现金流汇总
            Map<String, Map<Integer, BigDecimal>> cashFlowValueMap = new HashMap<>();
            Map<String, Map<Integer, String>> cashFlowDateMap = new HashMap<>();
            aggregateCashFlows(accountPeriod, cashFlowValueMap, cashFlowDateMap);
            log.info("现金流汇总完成，共汇总{}条记录", cashFlowValueMap.size());

            // 步骤3：计算现金流现值
            List<LiabilityCashFlowSummaryEntity> summaryEntities = calculatePresentValues(
                    cashFlowValueMap, cashFlowDateMap, factorMap);
            log.info("计算现金流现值完成，共计算{}条记录", summaryEntities.size());

            // 步骤4：数据入表
            // 先删除已有数据
            liabilityCashFlowSummaryMapper.deleteLiabilityCashFlowSummaryEntityByPeriod(accountPeriod);
            // 批量插入新数据
            if (!summaryEntities.isEmpty()) {
                liabilityCashFlowSummaryMapper.batchInsertLiabilityCashFlowSummaryEntity(summaryEntities);
            }
            log.info("数据入表完成，共插入{}条记录", summaryEntities.size());

            return true;
        } catch (Exception e) {
            log.error("执行负债现金流汇总及现值计算失败", e);
            throw e;
        }
    }

    /**
     * 加载折现因子
     *
     * @param accountPeriod 账期
     * @return 因子Map，key为account_period|factor_type|bp_type|duration_type，value为因子值Map
     */
    private Map<String, Map<Integer, BigDecimal>> loadDiscountFactors(String accountPeriod) {
        Map<String, Map<Integer, BigDecimal>> factorMap = new HashMap<>();

        List<DiscountFactorEntity> factorEntities = discountFactorMapper.selectDiscountFactorEntityListByPeriod(accountPeriod);
        for (DiscountFactorEntity entity : factorEntities) {
            String key = entity.getAccountPeriod() + "|" + entity.getFactorType() + "|" +
                         entity.getBpType() + "|" + entity.getDurationType();
            Map<Integer, BigDecimal> valueMap = ValueSetUtil.parseValueMap(entity.getFactorValSet());
            factorMap.put(key, valueMap);
        }

        return factorMap;
    }

    /**
     * 汇总现金流
     *
     * @param accountPeriod 账期
     * @param cashFlowValueMap 现金流值Map
     * @param cashFlowDateMap 现金流日期Map
     */
    private void aggregateCashFlows(String accountPeriod,
                                   Map<String, Map<Integer, BigDecimal>> cashFlowValueMap,
                                   Map<String, Map<Integer, String>> cashFlowDateMap) {
        List<LiabilityCashFlowEntity> cashFlowEntities = liabilityCashFlowMapper.selectLiabilityCashFlowEntityListByPeriod(accountPeriod);

        for (LiabilityCashFlowEntity entity : cashFlowEntities) {
            String key = entity.getAccountPeriod() + "|" + entity.getCashFlowType() + "|" +
                         entity.getBpType() + "|" + entity.getDurationType() + "|" +
                         entity.getDesignType() + "|" + entity.getIsShortTerm();

            Map<Integer, BigDecimal> valueMap = ValueSetUtil.parseValueMap(entity.getCashFlowValSet());
            Map<Integer, String> dateMap = ValueSetUtil.parseDateMap(entity.getCashFlowValSet());

            if (cashFlowValueMap.containsKey(key)) {
                // 如果已存在相同key的记录，累加值
                cashFlowValueMap.compute(key, (k, existingValueMap) -> ValueSetUtil.mergeValueMaps(existingValueMap, valueMap));
            } else {
                // 否则，添加新记录
                cashFlowValueMap.put(key, valueMap);
                cashFlowDateMap.put(key, dateMap);
            }
        }
    }

    /**
     * 计算现金流现值
     *
     * @param cashFlowValueMap 现金流值Map
     * @param cashFlowDateMap 现金流日期Map
     * @param factorMap 因子Map
     * @return 负债现金流汇总实体列表
     */
    private List<LiabilityCashFlowSummaryEntity> calculatePresentValues(
            Map<String, Map<Integer, BigDecimal>> cashFlowValueMap,
            Map<String, Map<Integer, String>> cashFlowDateMap,
            Map<String, Map<Integer, BigDecimal>> factorMap) {

        List<LiabilityCashFlowSummaryEntity> summaryEntities = new ArrayList<>();

        for (Map.Entry<String, Map<Integer, BigDecimal>> entry : cashFlowValueMap.entrySet()) {
            String key = entry.getKey();
            Map<Integer, BigDecimal> valueMap = entry.getValue();
            Map<Integer, String> dateMap = cashFlowDateMap.get(key);

            // 解析key
            String[] keyParts = key.split("\\|");
            String keyAccountPeriod = keyParts[0];
            String keyCashFlowType = keyParts[1];
            String keyBpType = keyParts[2];
            String keyDurationType = keyParts[3];
            String keyDesignType = keyParts[4];
            String keyIsShortTerm = keyParts[5];

            // 获取因子类型
            String factorType = ValueSetUtil.getFactorType(keyDesignType, keyIsShortTerm);

            // 构建因子Map的key
            String factorKey = keyAccountPeriod + "|" + factorType + "|" + keyBpType + "|" + keyDurationType;
            Map<Integer, BigDecimal> factorValueMap = factorMap.get(factorKey);

            if (factorValueMap == null) {
                log.warn("未找到对应的折现因子，factorKey={}", factorKey);
                continue;
            }

            // 计算现金流现值
            Map<Integer, BigDecimal> presentValueMap = calculatePresentValue(valueMap, factorValueMap, keyCashFlowType);

            // 创建负债现金流汇总实体
            LiabilityCashFlowSummaryEntity summaryEntity = new LiabilityCashFlowSummaryEntity();
            summaryEntity.setAccountPeriod(keyAccountPeriod);
            summaryEntity.setCashFlowType(keyCashFlowType);
            summaryEntity.setBpType(keyBpType);
            summaryEntity.setDurationType(keyDurationType);
            summaryEntity.setDesignType(keyDesignType);
            summaryEntity.setIsShortTerm(keyIsShortTerm);
            summaryEntity.setCashValSet(ValueSetUtil.buildValueSet(valueMap, dateMap));
            summaryEntity.setPresentCashValSet(ValueSetUtil.buildValueSet(presentValueMap, dateMap));

            summaryEntities.add(summaryEntity);
        }

        return summaryEntities;
    }

    /**
     * 计算现金流现值
     */
    private Map<Integer, BigDecimal> calculatePresentValue(
            Map<Integer, BigDecimal> cashValueMap,
            Map<Integer, BigDecimal> factorValueMap,
            String cashFlowType) {

        Map<Integer, BigDecimal> presentValueMap = new HashMap<>();

        int maxIndex = 1272;

        if ("01".equals(cashFlowType)) { // 流入
            for (int i = 0; i <= maxIndex; i++) {
                if (i == maxIndex) {
                    presentValueMap.put(i, BigDecimal.ZERO);
                    continue;
                }

                BigReal[] cashFlowVector = extractBigRealArray(cashValueMap, i + 1, maxIndex);
                BigReal[] factorVector = extractFactorBigRealArray(factorValueMap, i, i + 1, maxIndex, -1);
                BigDecimal presentValue = calculateDotProduct(cashFlowVector, factorVector);

                presentValueMap.put(i, presentValue.setScale(CalculationConstant.RESULT_SCALE, RoundingMode.HALF_UP));
            }

        } else { // 流出
            for (int i = 0; i <= maxIndex; i++) {
                BigReal[] cashFlowVector = extractBigRealArray(cashValueMap, i, maxIndex);
                BigReal[] factorVector = extractFactorBigRealArray(factorValueMap, i, i, maxIndex, 0);
                BigDecimal presentValue = calculateDotProduct(cashFlowVector, factorVector);

                presentValueMap.put(i, presentValue.setScale(CalculationConstant.RESULT_SCALE, RoundingMode.HALF_UP));
            }
        }

        return presentValueMap;
    }

    /**
     * 计算向量内积
     */
    private BigDecimal calculateDotProduct(BigReal[] vector1, BigReal[] vector2) {
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("向量长度不一致");
        }
        FieldVector<BigReal> fieldVector1 = new ArrayFieldVector<>(vector1);
        FieldVector<BigReal> fieldVector2 = new ArrayFieldVector<>(vector2);
        return fieldVector1.dotProduct(fieldVector2).bigDecimalValue();
    }

    /**
     * 直接从 Map 中提取值并创建 BigReal 数组
     */
    private BigReal[] extractBigRealArray(Map<Integer, BigDecimal> map, int startIndex, int endIndex) {
        BigReal[] array = new BigReal[endIndex - startIndex + 1];
        for (int i = startIndex, idx = 0; i <= endIndex; i++, idx++) {
            array[idx] = new BigReal(map.getOrDefault(i, BigDecimal.ZERO));
        }
        return array;
    }

    /**
     * 直接从因子Map中提取值并创建 BigReal 数组
     */
    private BigReal[] extractFactorBigRealArray(Map<Integer, BigDecimal> factorValueMap, int baseIndex, int startIndex, int endIndex, int offset) {
        BigReal[] array = new BigReal[endIndex - startIndex + 1];
        for (int j = startIndex, idx = 0; j <= endIndex; j++, idx++) {
            int factorIndex = j - baseIndex + offset;
            BigDecimal value = factorIndex >= 0 && factorIndex <= 1272 ?
                    factorValueMap.getOrDefault(factorIndex, BigDecimal.ZERO) : BigDecimal.ZERO;
            array[idx] = new BigReal(value);
        }
        return array;
    }
}
