package com.xl.alm.job.dur.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xl.alm.job.dur.entity.LiabilityCashFlowSummaryEntity;
import com.xl.alm.job.dur.entity.LiabilityDurationSummaryEntity;
import com.xl.alm.job.dur.entity.SubAccountLiabilityDurationEntity;
import com.xl.alm.job.dur.entity.SubAccountLiabilityPresentValueEntity;
import com.xl.alm.job.dur.mapper.LiabilityCashFlowSummaryMapper;
import com.xl.alm.job.dur.mapper.LiabilityDurationSummaryMapper;
import com.xl.alm.job.dur.mapper.SubAccountLiabilityDurationMapper;
import com.xl.alm.job.dur.mapper.SubAccountLiabilityPresentValueMapper;
import com.xl.alm.job.dur.service.SubAccountLiabilityDurationService;
import com.xl.alm.job.dur.util.ValueSetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.util.BigReal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 分账户负债久期汇总服务实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class SubAccountLiabilityDurationServiceImpl implements SubAccountLiabilityDurationService {

    @Autowired
    private LiabilityDurationSummaryMapper liabilityDurationSummaryMapper;

    @Autowired
    private SubAccountLiabilityDurationMapper subAccountLiabilityDurationMapper;

    @Autowired
    private LiabilityCashFlowSummaryMapper liabilityCashFlowSummaryMapper;

    @Autowired
    private SubAccountLiabilityPresentValueMapper subAccountLiabilityPresentValueMapper;

    /**
     * 计算精度 - 小数位数
     */
    private static final int SCALE = 8;

    /**
     * 计算过程中的精度 - 小数位数
     */
    private static final int CALCULATION_SCALE = 16;

    /**
     * 基点差值 0.01 (1%)
     */
    private static final BigDecimal BP_DIFF = new BigDecimal("0.01");

    /**
     * 执行分账户负债久期汇总
     *
     * @param accountPeriod 账期
     * @return 处理结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateSubAccountLiabilityDuration(String accountPeriod) {
        log.info("开始执行分账户负债久期汇总，账期：{}", accountPeriod);
        try {
            // 步骤1：修正久期汇总
            List<SubAccountLiabilityDurationEntity> modifiedDurationEntities = aggregateModifiedDuration(accountPeriod);
            log.info("修正久期汇总完成，共汇总{}条记录", modifiedDurationEntities.size());

            // 步骤2：有效久期汇总
            List<SubAccountLiabilityDurationEntity> effectiveDurationEntities = aggregateEffectiveDuration(accountPeriod);
            log.info("有效久期汇总完成，共汇总{}条记录", effectiveDurationEntities.size());

            // 步骤3：数据入表
            // 先删除已有数据
            subAccountLiabilityDurationMapper.deleteSubAccountLiabilityDurationEntityByPeriod(accountPeriod);

            // 合并修正久期和有效久期记录
            List<SubAccountLiabilityDurationEntity> allDurationEntities = new ArrayList<>();
            allDurationEntities.addAll(modifiedDurationEntities);
            allDurationEntities.addAll(effectiveDurationEntities);

            // 批量插入新数据
            if (!allDurationEntities.isEmpty()) {
                subAccountLiabilityDurationMapper.batchInsertSubAccountLiabilityDurationEntity(allDurationEntities);
            }
            log.info("数据入表完成，共插入{}条记录", allDurationEntities.size());

            return true;
        } catch (Exception e) {
            log.error("执行分账户负债久期汇总失败", e);
            throw e;
        }
    }

    /**
     * 修正久期汇总
     *
     * @param accountPeriod 账期
     * @return 修正久期汇总实体列表
     */
    private List<SubAccountLiabilityDurationEntity> aggregateModifiedDuration(String accountPeriod) {
        List<SubAccountLiabilityDurationEntity> durationEntities = new ArrayList<>();

        log.info("开始执行修正久期汇总，账期：{}", accountPeriod);

        try {
            // 步骤1: 按account_period=传入参数,cash_flow_type,duration_type=01,design_type,is_short_term作为关联条件
            // 关联TB0005和TB0007表查出TB0005表的present_cash_val_set和TB0007表的duration_val_set字段信息

            // 查询负债久期汇总表数据（TB0007）
            List<LiabilityDurationSummaryEntity> durationSummaryEntities = liabilityDurationSummaryMapper.selectLiabilityDurationSummaryEntityListByCondition(
                    accountPeriod, null, null, "01", null, null);

            if (durationSummaryEntities == null || durationSummaryEntities.isEmpty()) {
                log.warn("未找到账期{}的修正久期汇总数据", accountPeriod);
                return durationEntities;
            }

            log.info("查询到{}条修正久期汇总数据", durationSummaryEntities.size());

            // 查询负债现金流汇总表数据（TB0005）
            List<LiabilityCashFlowSummaryEntity> cashFlowSummaryEntities = liabilityCashFlowSummaryMapper.selectLiabilityCashFlowSummaryEntityListByCondition(
                    accountPeriod, null, null, "01", null, null);

            if (cashFlowSummaryEntities == null || cashFlowSummaryEntities.isEmpty()) {
                log.warn("未找到账期{}的负债现金流汇总数据", accountPeriod);
                return durationEntities;
            }

            log.info("查询到{}条负债现金流汇总数据", cashFlowSummaryEntities.size());

            // 步骤2: 将数据按is_short_term类型分别存储到2个Map中
            // Y类型放到shortTermMap(key为cash_flow_type|duration_type,value为数据记录)
            // N类型放到noneShortTermMap(key为cash_flow_type|duration_type,value为数据记录)

            // 将数据按照cash_flow_type|duration_type|design_type分组，并按is_short_term区分
            Map<String, Map<String, LiabilityDurationSummaryEntity>> durationMap = new HashMap<>();
            Map<String, Map<String, LiabilityCashFlowSummaryEntity>> cashFlowMap = new HashMap<>();

            for (LiabilityDurationSummaryEntity entity : durationSummaryEntities) {
                String key = entity.getCashFlowType() + "|" + entity.getDurationType() + "|" + entity.getDesignType();
                durationMap.computeIfAbsent(key, k -> new HashMap<>()).put(entity.getIsShortTerm(), entity);
            }

            for (LiabilityCashFlowSummaryEntity entity : cashFlowSummaryEntities) {
                String key = entity.getCashFlowType() + "|" + entity.getDurationType() + "|" + entity.getDesignType();
                cashFlowMap.computeIfAbsent(key, k -> new HashMap<>()).put(entity.getIsShortTerm(), entity);
            }

            // 步骤3: 遍历分组后的数据，计算修正久期汇总值
            for (String key : durationMap.keySet()) {
                Map<String, LiabilityDurationSummaryEntity> durationByShortTerm = durationMap.get(key);
                Map<String, LiabilityCashFlowSummaryEntity> cashFlowByShortTerm = cashFlowMap.get(key);

                if (durationByShortTerm == null || cashFlowByShortTerm == null) {
                    log.warn("数据不完整，跳过计算：{}", key);
                    continue;
                }

                // 获取中短期和非中短期数据
                LiabilityDurationSummaryEntity shortTermDuration = durationByShortTerm.get("Y");
                LiabilityDurationSummaryEntity nonShortTermDuration = durationByShortTerm.get("N");
                LiabilityCashFlowSummaryEntity shortTermCashFlow = cashFlowByShortTerm.get("Y");
                LiabilityCashFlowSummaryEntity nonShortTermCashFlow = cashFlowByShortTerm.get("N");

                if (shortTermDuration == null || nonShortTermDuration == null ||
                    shortTermCashFlow == null || nonShortTermCashFlow == null) {
                    log.warn("中短期或非中短期数据缺失，跳过计算：{}", key);
                    continue;
                }

                // 解析数据
                Map<Integer, BigDecimal> shortTermDurationValueMap = ValueSetUtil.parseValueMap(shortTermDuration.getDurationValSet());
                Map<Integer, BigDecimal> nonShortTermDurationValueMap = ValueSetUtil.parseValueMap(nonShortTermDuration.getDurationValSet());
                Map<Integer, BigDecimal> shortTermPresentValueMap = ValueSetUtil.parseValueMap(shortTermCashFlow.getPresentCashValSet());
                Map<Integer, BigDecimal> nonShortTermPresentValueMap = ValueSetUtil.parseValueMap(nonShortTermCashFlow.getPresentCashValSet());
                Map<Integer, String> dateMap = ValueSetUtil.parseDateMap(shortTermDuration.getDurationValSet());

                // 计算汇总久期值
                Map<Integer, BigDecimal> durationSummaryValueMap = new HashMap<>();

                // 按照公式计算修正久期汇总值
                // 公式：duration_summary_value[i] = ((A.present_cash_val_set[i].value * A.duration_val_set[i].value) +
                //                                      (B.present_cash_val_set[i].value * B.duration_val_set[i].value)) /
                //                                      (A.present_cash_val_set[i].value + B.present_cash_val_set[i].value)
                for (int i = 0; i <= 1272; i++) {
                    BigDecimal durationSummaryValue = BigDecimal.ZERO;

                    // 获取短期和非短期的现值和久期值
                    BigDecimal shortTermPresentValue = shortTermPresentValueMap.getOrDefault(i, BigDecimal.ZERO);
                    BigDecimal nonShortTermPresentValue = nonShortTermPresentValueMap.getOrDefault(i, BigDecimal.ZERO);
                    BigDecimal shortTermDurationValue = shortTermDurationValueMap.getOrDefault(i, BigDecimal.ZERO);
                    BigDecimal nonShortTermDurationValue = nonShortTermDurationValueMap.getOrDefault(i, BigDecimal.ZERO);

                    // 计算分母：短期现值 + 非短期现值
                    BigDecimal denominator = shortTermPresentValue.add(nonShortTermPresentValue);

                    if (denominator.compareTo(BigDecimal.ZERO) != 0) {
                        // 计算分子：(短期现值 * 短期久期) + (非短期现值 * 非短期久期)
                        BigDecimal numerator = shortTermPresentValue.multiply(shortTermDurationValue)
                                .add(nonShortTermPresentValue.multiply(nonShortTermDurationValue));

                        // 计算汇总久期值
                        durationSummaryValue = numerator.divide(denominator, CALCULATION_SCALE, RoundingMode.HALF_UP)
                                .setScale(SCALE, RoundingMode.HALF_UP);
                    }

                    durationSummaryValueMap.put(i, durationSummaryValue);
                }

                // 创建分账户负债久期汇总实体
                SubAccountLiabilityDurationEntity durationEntity = new SubAccountLiabilityDurationEntity();
                durationEntity.setAccountPeriod(accountPeriod);
                durationEntity.setCashFlowType(shortTermDuration.getCashFlowType());
                durationEntity.setBpType("03"); // 0bp
                durationEntity.setDurationType(shortTermDuration.getDurationType());
                durationEntity.setDesignType(shortTermDuration.getDesignType());
                durationEntity.setDurationValSet(ValueSetUtil.buildValueSet(durationSummaryValueMap, dateMap));

                durationEntities.add(durationEntity);

                log.info("计算完成一条修正久期汇总数据，key={}", key);
            }

            log.info("修正久期汇总计算完成，共生成{}条数据", durationEntities.size());
            return durationEntities;

        } catch (Exception e) {
            log.error("执行修正久期汇总失败", e);
            throw e;
        }
    }

    /**
     * 有效久期汇总
     *
     * @param accountPeriod 账期
     * @return 有效久期汇总实体列表
     */
    private List<SubAccountLiabilityDurationEntity> aggregateEffectiveDuration(String accountPeriod) {
        List<SubAccountLiabilityDurationEntity> durationEntities = new ArrayList<>();

        log.info("开始执行有效久期汇总，账期：{}", accountPeriod);

        try {
            // 步骤1: 按账期读取TB0008表duration_type为有效久期的数据
            // 查询分账户负债现金流现值汇总表数据（TB0008）
            List<SubAccountLiabilityPresentValueEntity> presentValueEntities = subAccountLiabilityPresentValueMapper
                    .selectSubAccountLiabilityPresentValueEntityListByCondition(accountPeriod, null, null, "02", null);

            if (presentValueEntities == null || presentValueEntities.isEmpty()) {
                log.warn("未找到账期{}的分账户负债现金流现值汇总数据", accountPeriod);
                return durationEntities;
            }

            log.info("查询到{}条分账户负债现金流现值汇总数据", presentValueEntities.size());

            // 步骤2: 将数据按cash_flow_type|design_type分组，并按bp_type区分
            Map<String, Map<String, SubAccountLiabilityPresentValueEntity>> presentValueMap = new HashMap<>();

            for (SubAccountLiabilityPresentValueEntity entity : presentValueEntities) {
                String key = entity.getCashFlowType() + "|" + entity.getDesignType();
                presentValueMap.computeIfAbsent(key, k -> new HashMap<>()).put(entity.getBpType(), entity);
            }

            // 遍历分组后的数据
            for (String key : presentValueMap.keySet()) {
                Map<String, SubAccountLiabilityPresentValueEntity> bpTypeMap = presentValueMap.get(key);

                if (bpTypeMap == null || bpTypeMap.size() < 3) {
                    log.warn("数据不完整，跳过计算：{}", key);
                    continue;
                }

                // 找到基点类型为"+50bp","-50bp","0bp"的记录，分别标记为A,B,C
                SubAccountLiabilityPresentValueEntity entityA = bpTypeMap.get("01"); // +50bp
                SubAccountLiabilityPresentValueEntity entityB = bpTypeMap.get("02"); // -50bp
                SubAccountLiabilityPresentValueEntity entityC = bpTypeMap.get("03"); // 0bp

                if (entityA == null || entityB == null || entityC == null) {
                    log.warn("基点类型数据不完整，跳过计算：{}", key);
                    continue;
                }

                // 解析现金流现值值集
                Map<Integer, BigDecimal> presentValueMapA = ValueSetUtil.parseValueMap(entityA.getPresentCashValSet());
                Map<Integer, BigDecimal> presentValueMapB = ValueSetUtil.parseValueMap(entityB.getPresentCashValSet());
                Map<Integer, BigDecimal> presentValueMapC = ValueSetUtil.parseValueMap(entityC.getPresentCashValSet());
                Map<Integer, String> dateMap = ValueSetUtil.parseDateMap(entityA.getPresentCashValSet());

                // 计算有效久期汇总值
                Map<Integer, BigDecimal> durationSummaryValueMap = new HashMap<>();

                // 按照公式计算有效久期汇总值
                // 公式: duration_summary_value[i] = (A.present_cash_val_set[i].value - B.present_cash_val_set[i].value)/0.01/C.present_cash_val_set[i].value
                for (int i = 0; i <= 1272; i++) {
                    BigDecimal durationSummaryValue = BigDecimal.ZERO;

                    // 获取各基点类型的现值
                    BigDecimal presentValueA = presentValueMapA.getOrDefault(i, BigDecimal.ZERO);
                    BigDecimal presentValueB = presentValueMapB.getOrDefault(i, BigDecimal.ZERO);
                    BigDecimal presentValueC = presentValueMapC.getOrDefault(i, BigDecimal.ZERO);

                    // 如果0bp的现值不为0，计算有效久期
                    if (presentValueC.compareTo(BigDecimal.ZERO) != 0) {
                        // 计算分子：+50bp的现值 - -50bp的现值
                        BigDecimal numerator = presentValueA.subtract(presentValueB);

                        // 计算有效久期值：分子 / 0.01 / 0bp的现值
                        durationSummaryValue = numerator.divide(BP_DIFF, CALCULATION_SCALE, RoundingMode.HALF_UP)
                                .divide(presentValueC, CALCULATION_SCALE, RoundingMode.HALF_UP)
                                .setScale(SCALE, RoundingMode.HALF_UP);
                    }

                    durationSummaryValueMap.put(i, durationSummaryValue);
                }

                // 创建分账户负债久期汇总实体
                SubAccountLiabilityDurationEntity durationEntity = new SubAccountLiabilityDurationEntity();
                durationEntity.setAccountPeriod(accountPeriod);
                durationEntity.setCashFlowType(entityA.getCashFlowType());
                durationEntity.setBpType("03"); // 0bp
                durationEntity.setDurationType("02"); // 有效久期
                durationEntity.setDesignType(entityA.getDesignType());
                durationEntity.setDurationValSet(ValueSetUtil.buildValueSet(durationSummaryValueMap, dateMap));

                durationEntities.add(durationEntity);

                log.info("计算完成一条有效久期汇总数据，key={}", key);
            }

            log.info("有效久期汇总计算完成，共生成{}条数据", durationEntities.size());
            return durationEntities;

        } catch (Exception e) {
            log.error("执行有效久期汇总失败", e);
            throw e;
        }
    }
}
