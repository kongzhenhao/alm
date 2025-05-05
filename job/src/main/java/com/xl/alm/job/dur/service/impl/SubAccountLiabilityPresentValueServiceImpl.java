package com.xl.alm.job.dur.service.impl;

import com.xl.alm.job.dur.entity.LiabilityCashFlowSummaryEntity;
import com.xl.alm.job.dur.entity.SubAccountLiabilityPresentValueEntity;
import com.xl.alm.job.dur.mapper.LiabilityCashFlowSummaryMapper;
import com.xl.alm.job.dur.mapper.SubAccountLiabilityPresentValueMapper;
import com.xl.alm.job.dur.service.SubAccountLiabilityPresentValueService;
import com.xl.alm.job.dur.util.ValueSetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.FieldVector;
import org.apache.commons.math3.util.BigReal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

/**
 * 分账户负债现金流现值汇总服务实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class SubAccountLiabilityPresentValueServiceImpl implements SubAccountLiabilityPresentValueService {

    /**
     * 计算精度 - 小数位数
     */
    private static final int SCALE = 8;

    /**
     * 计算过程中的精度 - 小数位数
     */
    private static final int CALCULATION_SCALE = 16;

    /**
     * 高精度计算上下文
     */
    private static final MathContext HIGH_PRECISION_CONTEXT = new MathContext(16, RoundingMode.HALF_UP);

    @Autowired
    private LiabilityCashFlowSummaryMapper liabilityCashFlowSummaryMapper;

    @Autowired
    private SubAccountLiabilityPresentValueMapper subAccountLiabilityPresentValueMapper;

    /**
     * 执行分账户负债现金流现值汇总
     *
     * @param accountPeriod 账期
     * @return 处理结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateSubAccountLiabilityPresentValue(String accountPeriod) {
        log.info("开始执行分账户负债现金流现值汇总，账期：{}", accountPeriod);
        try {
            // 步骤1：负债现金流现值汇总去掉中短期维度后进行汇总
            List<SubAccountLiabilityPresentValueEntity> presentValueEntities = aggregatePresentValues(accountPeriod);
            log.info("负债现金流现值汇总完成，共汇总{}条记录", presentValueEntities.size());

            // 步骤2：数据入表
            // 先删除已有数据
            subAccountLiabilityPresentValueMapper.deleteSubAccountLiabilityPresentValueEntityByPeriod(accountPeriod);
            // 批量插入新数据
            if (!presentValueEntities.isEmpty()) {
                subAccountLiabilityPresentValueMapper.batchInsertSubAccountLiabilityPresentValueEntity(presentValueEntities);
            }
            log.info("数据入表完成，共插入{}条记录", presentValueEntities.size());

            return true;
        } catch (Exception e) {
            log.error("执行分账户负债现金流现值汇总失败", e);
            throw e;
        }
    }

    /**
     * 汇总负债现金流现值
     *
     * @param accountPeriod 账期
     * @return 分账户负债现金流现值汇总实体列表
     */
    private List<SubAccountLiabilityPresentValueEntity> aggregatePresentValues(String accountPeriod) {
        // 查询负债现金流汇总数据
        List<LiabilityCashFlowSummaryEntity> cashFlowSummaryEntities =
                liabilityCashFlowSummaryMapper.selectLiabilityCashFlowSummaryEntityListByPeriod(accountPeriod);

        if (cashFlowSummaryEntities.isEmpty()) {
            log.warn("未找到账期{}的负债现金流汇总数据", accountPeriod);
            return Collections.emptyList();
        }

        // 按照account_period,cash_flow_type,bp_type,duration_type,design_type作为key进行汇总
        Map<String, Map<Integer, BigDecimal>> valueMapByKey = new HashMap<>();
        Map<String, Map<Integer, String>> dateMapByKey = new HashMap<>();

        for (LiabilityCashFlowSummaryEntity entity : cashFlowSummaryEntities) {
            // 构建key，去掉is_short_term维度
            String key = entity.getAccountPeriod() + "|" + entity.getCashFlowType() + "|" +
                    entity.getBpType() + "|" + entity.getDurationType() + "|" + entity.getDesignType();

            // 解析现金流现值值集
            Map<Integer, BigDecimal> valueMap = ValueSetUtil.parseValueMap(entity.getPresentCashValSet());
            Map<Integer, String> dateMap = ValueSetUtil.parseDateMap(entity.getPresentCashValSet());

            if (valueMapByKey.containsKey(key)) {
                // 如果已存在相同key的记录，累加值
                Map<Integer, BigDecimal> existingValueMap = valueMapByKey.get(key);
                valueMapByKey.put(key, ValueSetUtil.mergeValueMaps(existingValueMap, valueMap));
            } else {
                // 否则，添加新记录
                valueMapByKey.put(key, valueMap);
                dateMapByKey.put(key, dateMap);
            }
        }

        // 构建分账户负债现金流现值汇总实体列表
        List<SubAccountLiabilityPresentValueEntity> presentValueEntities = new ArrayList<>();

        for (String key : valueMapByKey.keySet()) {
            String[] parts = key.split("\\|");
            String keyAccountPeriod = parts[0];
            String keyCashFlowType = parts[1];
            String keyBpType = parts[2];
            String keyDurationType = parts[3];
            String keyDesignType = parts[4];

            Map<Integer, BigDecimal> valueMap = valueMapByKey.get(key);
            Map<Integer, String> dateMap = dateMapByKey.get(key);

            // 使用BigDecimal矩阵操作处理现金流现值
            Map<Integer, BigDecimal> processedValueMap = processPresentValues(valueMap);

            // 创建分账户负债现金流现值汇总实体
            SubAccountLiabilityPresentValueEntity presentValueEntity = new SubAccountLiabilityPresentValueEntity();
            presentValueEntity.setAccountPeriod(keyAccountPeriod);
            presentValueEntity.setCashFlowType(keyCashFlowType);
            presentValueEntity.setBpType(keyBpType);
            presentValueEntity.setDurationType(keyDurationType);
            presentValueEntity.setDesignType(keyDesignType);
            presentValueEntity.setPresentCashValSet(ValueSetUtil.buildValueSet(processedValueMap, dateMap));

            presentValueEntities.add(presentValueEntity);
        }

        return presentValueEntities;
    }

    /**
     * 使用BigDecimal矩阵操作处理现金流现值
     *
     * @param valueMap 现金流现值Map
     * @return 处理后的现金流现值Map
     */
    private Map<Integer, BigDecimal> processPresentValues(Map<Integer, BigDecimal> valueMap) {
        Map<Integer, BigDecimal> processedValueMap = new HashMap<>();

        // 获取所有有效索引
        List<Integer> validIndices = new ArrayList<>(valueMap.keySet());
        Collections.sort(validIndices);

        if (validIndices.isEmpty()) {
            return processedValueMap;
        }

        // 创建BigReal数组
        BigReal[] valueArray = new BigReal[validIndices.size()];
        for (int i = 0; i < validIndices.size(); i++) {
            int index = validIndices.get(i);
            BigDecimal value = valueMap.getOrDefault(index, BigDecimal.ZERO);
            valueArray[i] = new BigReal(value);
        }

        // 创建向量
        FieldVector<BigReal> vector = new ArrayFieldVector<>(valueArray);

        // 对每个有效索引处理现金流现值
        for (int i = 0; i < validIndices.size(); i++) {
            int index = validIndices.get(i);
            BigReal value = vector.getEntry(i);

            // 将处理后的值转回BigDecimal并保存
            processedValueMap.put(index, value.bigDecimalValue().setScale(SCALE, RoundingMode.HALF_UP));
        }

        return processedValueMap;
    }
}
