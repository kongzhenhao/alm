package com.xl.alm.job.dur.service.impl;

import com.xl.alm.job.dur.constant.CalculationConstant;
import com.xl.alm.job.dur.entity.DiscountCurveEntity;
import com.xl.alm.job.dur.entity.DiscountFactorEntity;
import com.xl.alm.job.dur.entity.LiabilityCashFlowSummaryEntity;
import com.xl.alm.job.dur.entity.LiabilityDurationSummaryEntity;
import com.xl.alm.job.dur.mapper.DiscountCurveMapper;
import com.xl.alm.job.dur.mapper.DiscountFactorMapper;
import com.xl.alm.job.dur.mapper.LiabilityCashFlowSummaryMapper;
import com.xl.alm.job.dur.mapper.LiabilityDurationSummaryMapper;
import com.xl.alm.job.dur.service.LiabilityDurationSummaryService;
import com.xl.alm.job.dur.util.ValueSetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.FieldVector;
import org.apache.commons.math3.util.BigReal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 负债久期计算服务实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class LiabilityDurationSummaryServiceImpl implements LiabilityDurationSummaryService {
    /**
     * 基点差值 0.01 (1%)
     */
    private static final BigDecimal BP_DIFF = new BigDecimal("0.01");

    /**
     * 高精度计算上下文
     */
    private static final MathContext HIGH_PRECISION_CONTEXT = new MathContext(16, RoundingMode.HALF_UP);

    @Autowired
    private DiscountFactorMapper discountFactorMapper;

    @Autowired
    private DiscountCurveMapper discountCurveMapper;

    @Autowired
    private LiabilityCashFlowSummaryMapper liabilityCashFlowSummaryMapper;

    @Autowired
    private LiabilityDurationSummaryMapper liabilityDurationSummaryMapper;

    /**
     * 执行负债久期计算
     *
     * @param accountPeriod 账期
     * @return 处理结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateLiabilityDurationSummary(String accountPeriod) {
        log.info("开始执行负债久期计算，账期：{}", accountPeriod);
        try {
            // 步骤1：加载因子表及曲线表
            Map<String, Map<Integer, BigDecimal>> factorMap = loadDiscountFactors(accountPeriod);
            Map<String, Map<Integer, BigDecimal>> curveMap = loadDiscountCurves(accountPeriod);
            log.info("加载因子表及曲线表完成，因子表共加载{}条记录，曲线表共加载{}条记录", factorMap.size(), curveMap.size());

            // 步骤2：计算修正久期
            List<LiabilityDurationSummaryEntity> modifiedDurationEntities = calculateModifiedDuration(accountPeriod, factorMap, curveMap);
            log.info("计算修正久期完成，共计算{}条记录", modifiedDurationEntities.size());

            // 步骤3：计算有效久期
            List<LiabilityDurationSummaryEntity> effectiveDurationEntities = calculateEffectiveDuration(accountPeriod);
            log.info("计算有效久期完成，共计算{}条记录", effectiveDurationEntities.size());

            // 步骤4：数据入表
            saveDurationEntities(accountPeriod, modifiedDurationEntities, effectiveDurationEntities);

            return true;
        } catch (Exception e) {
            log.error("执行负债久期计算失败", e);
            throw e;
        }
    }

    /**
     * 保存久期实体
     *
     * @param accountPeriod 账期
     * @param modifiedDurationEntities 修正久期实体列表
     * @param effectiveDurationEntities 有效久期实体列表
     */
    private void saveDurationEntities(
            String accountPeriod,
            List<LiabilityDurationSummaryEntity> modifiedDurationEntities,
            List<LiabilityDurationSummaryEntity> effectiveDurationEntities) {
        // 先删除已有数据
        liabilityDurationSummaryMapper.deleteLiabilityDurationSummaryEntityByPeriod(accountPeriod);

        // 合并修正久期和有效久期记录
        List<LiabilityDurationSummaryEntity> allDurationEntities = new ArrayList<>(
                modifiedDurationEntities.size() + effectiveDurationEntities.size());
        allDurationEntities.addAll(modifiedDurationEntities);
        allDurationEntities.addAll(effectiveDurationEntities);

        // 批量插入新数据
        if (!allDurationEntities.isEmpty()) {
            liabilityDurationSummaryMapper.batchInsertLiabilityDurationSummaryEntity(allDurationEntities);
        }
        log.info("数据入表完成，共插入{}条记录", allDurationEntities.size());
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
            String key = buildDiscountKey(entity.getAccountPeriod(), entity.getFactorType(),
                                         entity.getBpType(), entity.getDurationType());
            factorMap.put(key, ValueSetUtil.parseValueMap(entity.getFactorValSet()));
        }

        return factorMap;
    }

    /**
     * 加载折现曲线
     *
     * @param accountPeriod 账期
     * @return 曲线Map，key为account_period|curve_type|bp_type|duration_type，value为曲线值Map
     */
    private Map<String, Map<Integer, BigDecimal>> loadDiscountCurves(String accountPeriod) {
        Map<String, Map<Integer, BigDecimal>> curveMap = new HashMap<>();
        List<DiscountCurveEntity> curveEntities = discountCurveMapper.selectDiscountCurveEntityListByPeriod(accountPeriod);

        for (DiscountCurveEntity entity : curveEntities) {
            String key = buildDiscountKey(entity.getAccountPeriod(), entity.getCurveType(),
                                         entity.getBpType(), entity.getDurationType());
            curveMap.put(key, ValueSetUtil.parseValueMap(entity.getCurveValSet()));
        }

        return curveMap;
    }

    /**
     * 构建折现键
     *
     * @param accountPeriod 账期
     * @param typeValue 类型值（因子类型或曲线类型）
     * @param bpType BP类型
     * @param durationType 久期类型
     * @return 键
     */
    private String buildDiscountKey(String accountPeriod, String typeValue, String bpType, String durationType) {
        return accountPeriod + "|" + typeValue + "|" + bpType + "|" + durationType;
    }

    /**
     * 计算修正久期
     *
     * @param accountPeriod 账期
     * @param factorMap 因子Map
     * @param curveMap 曲线Map
     * @return 修正久期实体列表
     */
    private List<LiabilityDurationSummaryEntity> calculateModifiedDuration(
            String accountPeriod,
            Map<String, Map<Integer, BigDecimal>> factorMap,
            Map<String, Map<Integer, BigDecimal>> curveMap) {
        List<LiabilityDurationSummaryEntity> durationEntities = new ArrayList<>();

        // 查询修正久期的现金流汇总数据
        // 查询所有durationType为"01"（修正久期）的记录
        List<LiabilityCashFlowSummaryEntity> cashFlowSummaryEntities = liabilityCashFlowSummaryMapper.selectLiabilityCashFlowSummaryEntityListByCondition(
                accountPeriod, null, null, "01", null, null);

        if (cashFlowSummaryEntities == null || cashFlowSummaryEntities.isEmpty()) {
            log.warn("未找到账期{}的修正久期现金流汇总数据", accountPeriod);
            return durationEntities;
        }

        log.info("查询到{}条修正久期现金流汇总数据", cashFlowSummaryEntities.size());

        // 遍历现金流汇总数据，计算修正久期
        for (LiabilityCashFlowSummaryEntity cashFlowSummary : cashFlowSummaryEntities) {
            // 获取现金流数据
            Map<Integer, BigDecimal> cashFlowValueMap = ValueSetUtil.parseValueMap(cashFlowSummary.getCashValSet());
            Map<Integer, BigDecimal> presentCashValueMap = ValueSetUtil.parseValueMap(cashFlowSummary.getPresentCashValSet());
            Map<Integer, String> dateMap = ValueSetUtil.parseDateMap(cashFlowSummary.getCashValSet());

            // 获取折现因子和折现率
            String factorType = determineFactorType(cashFlowSummary.getDesignType(), cashFlowSummary.getIsShortTerm());

            // 构建因子键和曲线键
            String factorKey = buildDiscountKey(
                cashFlowSummary.getAccountPeriod(), factorType,
                cashFlowSummary.getBpType(), cashFlowSummary.getDurationType());

            Map<Integer, BigDecimal> factorValueMap = factorMap.get(factorKey);
            if (factorValueMap == null) {
                log.warn("未找到对应的折现因子，跳过计算：{}", factorKey);
                continue;
            }

            // 曲线类型与因子类型相同
            String curveKey = buildDiscountKey(
                cashFlowSummary.getAccountPeriod(), factorType,
                cashFlowSummary.getBpType(), cashFlowSummary.getDurationType());

            Map<Integer, BigDecimal> curveValueMap = curveMap.get(curveKey);
            if (curveValueMap == null) {
                log.warn("未找到对应的折现曲线，跳过计算：{}", curveKey);
                continue;
            }

            // 计算修正久期
            Map<Integer, BigDecimal> durationValueMap = calculateDurationValue(
                    cashFlowValueMap, presentCashValueMap, factorValueMap, curveValueMap, cashFlowSummary.getCashFlowType());

            // 创建负债久期汇总实体
            LiabilityDurationSummaryEntity durationEntity = new LiabilityDurationSummaryEntity();
            durationEntity.setAccountPeriod(cashFlowSummary.getAccountPeriod());
            durationEntity.setCashFlowType(cashFlowSummary.getCashFlowType());
            durationEntity.setDurationType(cashFlowSummary.getDurationType());
            durationEntity.setDesignType(cashFlowSummary.getDesignType());
            durationEntity.setIsShortTerm(cashFlowSummary.getIsShortTerm());
            durationEntity.setDurationValSet(ValueSetUtil.buildValueSet(durationValueMap, dateMap));

            durationEntities.add(durationEntity);
        }

        return durationEntities;
    }

    /**
     * 根据设计类型和是否为中短期险种确定因子类型
     *
     * @param designType 设计类型
     * @param isShortTerm 是否为中短期险种
     * @return 因子类型
     */
    private String determineFactorType(String designType, String isShortTerm) {
        // 使用ValueSetUtil中已有的方法
        return ValueSetUtil.getFactorType(designType, isShortTerm);
    }

    /**
     * 计算久期值
     *
     * @param cashFlowValueMap 现金流值Map
     * @param presentCashValueMap 现金流现值值Map
     * @param factorValueMap 折现因子值Map
     * @param curveValueMap 折现曲线值Map
     * @param cashFlowType 现金流类型
     * @return 久期值Map
     */
    private Map<Integer, BigDecimal> calculateDurationValue(
            Map<Integer, BigDecimal> cashFlowValueMap,
            Map<Integer, BigDecimal> presentCashValueMap,
            Map<Integer, BigDecimal> factorValueMap,
            Map<Integer, BigDecimal> curveValueMap,
            String cashFlowType) {
        Map<Integer, BigDecimal> durationValueMap = new HashMap<>();

        // 是否为流入类型
        boolean isInflow = "01".equals(cashFlowType);

        // 计算久期值
        for (int i = 0; i <= 1272; i++) {
            // 如果现金流现值为0，则久期值为0
            BigDecimal presentCashValue = presentCashValueMap.getOrDefault(i, BigDecimal.ZERO);
            if (presentCashValue.compareTo(BigDecimal.ZERO) == 0) {
                durationValueMap.put(i, BigDecimal.ZERO);
                continue;
            }

            // 根据现金流类型确定起始索引和计算参数
            int startIndex = isInflow ? i + 1 : i;
            int size = 1273 - startIndex;

            if (size <= 0) {
                durationValueMap.put(i, BigDecimal.ZERO);
                continue;
            }

            // 使用矩阵计算久期值
            BigDecimal durationValue = calculateDurationWithMatrix(
                cashFlowValueMap, factorValueMap, curveValueMap,
                i, startIndex, size, isInflow, presentCashValue);

            durationValueMap.put(i, durationValue);
        }

        return durationValueMap;
    }

    /**
     * 使用向量计算久期值
     *
     * @param cashFlowValueMap 现金流值Map
     * @param factorValueMap 折现因子值Map
     * @param curveValueMap 折现曲线值Map
     * @param baseIndex 基准索引
     * @param startIndex 起始索引
     * @param size 计算大小
     * @param isInflow 是否为流入
     * @param presentCashValue 现金流现值
     * @return 久期值
     */
    private BigDecimal calculateDurationWithMatrix(
            Map<Integer, BigDecimal> cashFlowValueMap,
            Map<Integer, BigDecimal> factorValueMap,
            Map<Integer, BigDecimal> curveValueMap,
            int baseIndex, int startIndex, int size, boolean isInflow,
            BigDecimal presentCashValue) {

        log.debug("开始计算久期值, baseIndex={}, startIndex={}, size={}, isInflow={}",
                baseIndex, startIndex, size, isInflow);

        // 过滤掉现金流为0的数据点，提高计算效率
        List<Integer> validIndices = new ArrayList<>();
        for (int j = 0; j < size; j++) {
            int index = startIndex + j;
            BigDecimal cashFlow = cashFlowValueMap.getOrDefault(index, BigDecimal.ZERO);
            if (cashFlow.compareTo(BigDecimal.ZERO) > 0) {
                validIndices.add(j);
            }
        }

        // 如果没有有效数据点，直接返回0
        if (validIndices.isEmpty()) {
            log.debug("没有有效数据点，返回0");
            return BigDecimal.ZERO;
        }

        int validSize = validIndices.size();
        log.debug("有效数据点数量: {}", validSize);

        // 创建并填充向量数据
        BigReal[] cashFlowArray = new BigReal[validSize];
        BigReal[] timeWeightArray = new BigReal[validSize];
        BigReal[] factorArray = new BigReal[validSize];
        BigReal[] inverseOnePlusCurveArray = new BigReal[validSize];

        // 填充向量数据
        for (int i = 0; i < validSize; i++) {
            int j = validIndices.get(i);
            int index = startIndex + j;

            // 现金流金额
            BigDecimal cashFlow = cashFlowValueMap.getOrDefault(index, BigDecimal.ZERO);
            cashFlowArray[i] = new BigReal(cashFlow);

            BigDecimal timeWeight;
            // 计算时间权重
            timeWeight = new BigDecimal(j).divide(new BigDecimal("12"), CalculationConstant.PROCESS_SCALE, RoundingMode.HALF_UP);
            timeWeightArray[i] = new BigReal(timeWeight);

            // 计算因子索引
            int factorIndex = isInflow ? index - baseIndex - 1 : index - baseIndex;

            // 折现因子
            BigDecimal factor = factorValueMap.getOrDefault(factorIndex, BigDecimal.ZERO);
            factorArray[i] = new BigReal(factor);

            // 折现率的倒数 (1/(1+折现率))
            BigDecimal curve = curveValueMap.getOrDefault(factorIndex, BigDecimal.ZERO);
            BigDecimal onePlusCurve = BigDecimal.ONE.add(curve);
            inverseOnePlusCurveArray[i] = new BigReal(BigDecimal.ONE).divide(new BigReal(onePlusCurve));
        }

        // 创建向量
        FieldVector<BigReal> cashFlowVector = new ArrayFieldVector<>(cashFlowArray);
        FieldVector<BigReal> timeWeightVector = new ArrayFieldVector<>(timeWeightArray);
        FieldVector<BigReal> factorVector = new ArrayFieldVector<>(factorArray);
        FieldVector<BigReal> inverseOnePlusCurveVector = new ArrayFieldVector<>(inverseOnePlusCurveArray);

        // 计算元素对应相乘 (Hadamard乘积)
        // 现金流 ⊙ 时间权重
        FieldVector<BigReal> cashFlowTimeVector = cashFlowVector.ebeMultiply(timeWeightVector);

        // (现金流 ⊙ 时间权重) ⊙ 折现因子
        FieldVector<BigReal> productVector = cashFlowTimeVector.ebeMultiply(factorVector);

        // (现金流 ⊙ 时间权重 ⊙ 折现因子) ⊙ (1/(1+折现率))
        FieldVector<BigReal> resultVector = productVector.ebeMultiply(inverseOnePlusCurveVector);

        // 计算向量元素和
        BigDecimal sum = calculateVectorSum(resultVector);

        // 计算最终的久期值
        BigDecimal durationValue = sum.divide(presentCashValue, CalculationConstant.RESULT_SCALE, RoundingMode.HALF_UP);
        log.debug("计算得到的久期值: {}", durationValue);

        return durationValue;
    }

    /**
     * 计算向量元素和
     *
     * @param vector 向量
     * @return 元素和
     */
    private BigDecimal calculateVectorSum(FieldVector<BigReal> vector) {
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < vector.getDimension(); i++) {
            sum = sum.add(vector.getEntry(i).bigDecimalValue());
        }
        return sum;
    }

    /**
     * 计算有效久期
     *
     * @param accountPeriod 账期
     * @return 有效久期实体列表
     */
    private List<LiabilityDurationSummaryEntity> calculateEffectiveDuration(String accountPeriod) {
        List<LiabilityDurationSummaryEntity> durationEntities = new ArrayList<>();

        // 查询三个数据集
        List<LiabilityCashFlowSummaryEntity> aDataSet = liabilityCashFlowSummaryMapper.selectLiabilityCashFlowSummaryEntityListByCondition(
                accountPeriod, null, "01", "02", null, null); // bp_type=01(+50bp), durationType=02(有效久期)
        List<LiabilityCashFlowSummaryEntity> bDataSet = liabilityCashFlowSummaryMapper.selectLiabilityCashFlowSummaryEntityListByCondition(
                accountPeriod, null, "02", "02", null, null); // bp_type=02(-50bp), durationType=02(有效久期)
        List<LiabilityCashFlowSummaryEntity> cDataSet = liabilityCashFlowSummaryMapper.selectLiabilityCashFlowSummaryEntityListByCondition(
                accountPeriod, null, "03", "01", null, null); // bp_type=03(0bp), durationType=01(修正久期)

        if (aDataSet.isEmpty() || bDataSet.isEmpty() || cDataSet.isEmpty()) {
            log.warn("计算有效久期所需的数据集不完整，A数据集大小：{}，B数据集大小：{}，C数据集大小：{}",
                    aDataSet.size(), bDataSet.size(), cDataSet.size());
            return durationEntities;
        }

        // 将数据集按照cash_flow_type, design_type, is_short_term分组
        Map<String, LiabilityCashFlowSummaryEntity> aMap = groupCashFlowEntities(aDataSet);
        Map<String, LiabilityCashFlowSummaryEntity> bMap = groupCashFlowEntities(bDataSet);
        Map<String, LiabilityCashFlowSummaryEntity> cMap = groupCashFlowEntities(cDataSet);

        // 计算有效久期
        // 遍历A数据集的key，找到对应的B和C数据集记录
        for (String key : aMap.keySet()) {
            LiabilityCashFlowSummaryEntity aEntity = aMap.get(key);
            LiabilityCashFlowSummaryEntity bEntity = bMap.get(key);
            LiabilityCashFlowSummaryEntity cEntity = cMap.get(key);

            if (bEntity == null || cEntity == null) {
                log.warn("未找到匹配的B或C数据集记录，key：{}", key);
                continue;
            }

            // 解析现金流现值值集
            Map<Integer, BigDecimal> aPresentValueMap = ValueSetUtil.parseValueMap(aEntity.getPresentCashValSet());
            Map<Integer, BigDecimal> bPresentValueMap = ValueSetUtil.parseValueMap(bEntity.getPresentCashValSet());
            Map<Integer, BigDecimal> cPresentValueMap = ValueSetUtil.parseValueMap(cEntity.getPresentCashValSet());
            Map<Integer, String> dateMap = ValueSetUtil.parseDateMap(aEntity.getPresentCashValSet());

            // 计算有效久期
            Map<Integer, BigDecimal> durationValueMap = calculateEffectiveDurationValues(
                    aPresentValueMap, bPresentValueMap, cPresentValueMap);

            // 创建负债久期汇总实体
            LiabilityDurationSummaryEntity durationEntity = new LiabilityDurationSummaryEntity();
            durationEntity.setAccountPeriod(accountPeriod);
            durationEntity.setCashFlowType(aEntity.getCashFlowType());
            durationEntity.setDurationType("02"); // 有效久期
            durationEntity.setDesignType(aEntity.getDesignType());
            durationEntity.setIsShortTerm(aEntity.getIsShortTerm());
            durationEntity.setDurationValSet(ValueSetUtil.buildValueSet(durationValueMap, dateMap));

            durationEntities.add(durationEntity);
        }

        return durationEntities;
    }

    /**
     * 将现金流实体按照现金流类型、设计类型和是否短期分组
     *
     * @param entities 现金流实体列表
     * @return 分组后的Map
     */
    private Map<String, LiabilityCashFlowSummaryEntity> groupCashFlowEntities(List<LiabilityCashFlowSummaryEntity> entities) {
        Map<String, LiabilityCashFlowSummaryEntity> result = new HashMap<>();
        for (LiabilityCashFlowSummaryEntity entity : entities) {
            String key = entity.getCashFlowType() + "|" + entity.getDesignType() + "|" + entity.getIsShortTerm();
            result.put(key, entity);
        }
        return result;
    }

    /**
     * 计算有效久期值
     *
     * @param aPresentValueMap +50bp现值Map
     * @param bPresentValueMap -50bp现值Map
     * @param cPresentValueMap 0bp现值Map
     * @return 有效久期值Map
     */
    private Map<Integer, BigDecimal> calculateEffectiveDurationValues(
            Map<Integer, BigDecimal> aPresentValueMap,
            Map<Integer, BigDecimal> bPresentValueMap,
            Map<Integer, BigDecimal> cPresentValueMap) {

        log.debug("开始计算有效久期值");
        Map<Integer, BigDecimal> durationValueMap = new HashMap<>();

        // 计算有效数据点数量
        List<Integer> validIndices = new ArrayList<>();

        // 第一次遍历，找出所有有效数据点
        for (int i = 0; i <= 1272; i++) {
            BigDecimal cPresentValue = cPresentValueMap.getOrDefault(i, BigDecimal.ZERO);
            if (cPresentValue.compareTo(BigDecimal.ZERO) == 0) {
                durationValueMap.put(i, BigDecimal.ZERO);
                continue;
            }
            validIndices.add(i);
        }

        // 如果没有有效数据点，直接返回
        if (validIndices.isEmpty()) {
            log.debug("没有有效数据点，返回空结果");
            return durationValueMap;
        }

        int validSize = validIndices.size();
        log.debug("有效数据点数量: {}", validSize);

        // 创建并填充向量数据
        BigReal[] aArray = new BigReal[validSize];  // A值数组
        BigReal[] bArray = new BigReal[validSize];  // B值数组
        BigReal[] cArray = new BigReal[validSize];  // C值数组

        // 填充向量数据
        for (int j = 0; j < validSize; j++) {
            int i = validIndices.get(j);

            // 获取A、B和C的现值
            BigDecimal aPresentValue = aPresentValueMap.getOrDefault(i, BigDecimal.ZERO);
            BigDecimal bPresentValue = bPresentValueMap.getOrDefault(i, BigDecimal.ZERO);
            BigDecimal cPresentValue = cPresentValueMap.getOrDefault(i, BigDecimal.ZERO);

            // 填充向量数据
            aArray[j] = new BigReal(aPresentValue);  // A值
            bArray[j] = new BigReal(bPresentValue);  // B值
            cArray[j] = new BigReal(cPresentValue);  // C值
        }

        // 创建向量
        FieldVector<BigReal> aVector = new ArrayFieldVector<>(aArray);
        FieldVector<BigReal> bVector = new ArrayFieldVector<>(bArray);
        FieldVector<BigReal> cVector = new ArrayFieldVector<>(cArray);

        // 计算 B - A (向量减法)
        FieldVector<BigReal> diffVector = bVector.subtract(aVector);

        // 为每个有效索引计算久期值
        for (int j = 0; j < validSize; j++) {
            int i = validIndices.get(j);

            // 获取当前元素
            BigReal diffValue = diffVector.getEntry(j);
            BigReal cValue = cVector.getEntry(j);

            // 计算久期值: (B-A)/(C*0.01)
            BigDecimal durationValue = diffValue.divide(cValue.multiply(new BigReal(BP_DIFF)))
                .bigDecimalValue();

            durationValueMap.put(i, durationValue);
        }

        log.debug("有效久期值计算完成，共计算{}个数据点", durationValueMap.size());
        return durationValueMap;
    }
}
