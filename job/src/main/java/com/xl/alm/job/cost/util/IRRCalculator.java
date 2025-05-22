package com.xl.alm.job.cost.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 内部收益率(IRR)计算工具类
 *
 * @author AI Assistant
 */
@Slf4j
public class IRRCalculator {

    /** 最大迭代次数 */
    private static final int MAX_ITERATIONS = 1000;

    /** 精度 */
    private static final BigDecimal PRECISION = new BigDecimal("0.0000000001");

    /** 计算精度 */
    private static final int CALCULATION_SCALE = 10;

    /** 结果精度 */
    private static final int RESULT_SCALE = 6;

    /**
     * 计算内部收益率(IRR)
     *
     * @param cashflows 现金流列表，第一个元素为初始投资（通常为负值）
     * @return 内部收益率，如果无法计算则返回null
     */
    public static BigDecimal calculateIRR(List<BigDecimal> cashflows) {
        if (cashflows == null || cashflows.size() < 2) {
            log.warn("现金流数据不足，无法计算IRR");
            return null;
        }

        // 检查现金流是否有正负变化，如果没有则无法计算IRR
        boolean hasPositive = false;
        boolean hasNegative = false;
        for (BigDecimal cf : cashflows) {
            if (cf.compareTo(BigDecimal.ZERO) > 0) {
                hasPositive = true;
            } else if (cf.compareTo(BigDecimal.ZERO) < 0) {
                hasNegative = true;
            }
            if (hasPositive && hasNegative) {
                break;
            }
        }

        if (!hasPositive || !hasNegative) {
            log.warn("现金流没有正负变化，无法计算IRR");
            return null;
        }

        // 使用牛顿迭代法计算IRR
        return newtonRaphsonMethod(cashflows);
    }

    /**
     * 使用牛顿迭代法计算IRR
     *
     * @param cashflows 现金流列表
     * @return 内部收益率，如果无法计算则返回null
     */
    private static BigDecimal newtonRaphsonMethod(List<BigDecimal> cashflows) {
        // 初始猜测值为10%
        BigDecimal rate = new BigDecimal("0.1");

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            BigDecimal npv = calculateNPV(cashflows, rate);
            BigDecimal derivative = calculateDerivative(cashflows, rate);

            // 如果导数接近于0，则无法继续迭代
            if (derivative.abs().compareTo(PRECISION) <= 0) {
                log.warn("导数接近于0，无法继续迭代");
                return null;
            }

            // 计算新的猜测值
            BigDecimal newRate = rate.subtract(npv.divide(derivative, CALCULATION_SCALE, RoundingMode.HALF_UP));

            // 如果新旧猜测值的差小于精度要求，则认为已经收敛
            if (newRate.subtract(rate).abs().compareTo(PRECISION) <= 0) {
                return newRate.setScale(RESULT_SCALE, RoundingMode.HALF_UP);
            }

            rate = newRate;
        }

        log.warn("超过最大迭代次数，无法收敛");
        return null;
    }

    /**
     * 计算净现值(NPV)
     *
     * @param cashflows 现金流列表
     * @param rate 折现率
     * @return 净现值
     */
    private static BigDecimal calculateNPV(List<BigDecimal> cashflows, BigDecimal rate) {
        BigDecimal npv = BigDecimal.ZERO;
        BigDecimal one = BigDecimal.ONE;

        for (int i = 0; i < cashflows.size(); i++) {
            BigDecimal cashflow = cashflows.get(i);
            BigDecimal denominator = one.add(rate).pow(i);
            BigDecimal discountedCashflow = cashflow.divide(denominator, CALCULATION_SCALE, RoundingMode.HALF_UP);
            npv = npv.add(discountedCashflow);
        }

        return npv;
    }

    /**
     * 计算净现值对折现率的导数
     *
     * @param cashflows 现金流列表
     * @param rate 折现率
     * @return 导数值
     */
    private static BigDecimal calculateDerivative(List<BigDecimal> cashflows, BigDecimal rate) {
        BigDecimal derivative = BigDecimal.ZERO;
        BigDecimal one = BigDecimal.ONE;

        for (int i = 1; i < cashflows.size(); i++) {
            BigDecimal cashflow = cashflows.get(i);
            BigDecimal denominator = one.add(rate).pow(i + 1);
            BigDecimal term = cashflow.multiply(new BigDecimal(i)).divide(denominator, CALCULATION_SCALE, RoundingMode.HALF_UP);
            derivative = derivative.subtract(term);
        }

        return derivative;
    }

    /**
     * 计算内部收益率(IRR)，如果计算失败则返回0
     * 相当于Excel的IFERROR(XIRR(...), 0)函数
     *
     * @param cashflows 现金流列表，第一个元素为初始投资（通常为负值）
     * @return 内部收益率，如果无法计算则返回0
     */
    public static BigDecimal calculateIRRWithDefault(List<BigDecimal> cashflows) {
        try {
            BigDecimal result = calculateIRR(cashflows);
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception e) {
            log.warn("IRR计算异常，返回默认值0: {}", e.getMessage());
            return BigDecimal.ZERO;
        }
    }
}
