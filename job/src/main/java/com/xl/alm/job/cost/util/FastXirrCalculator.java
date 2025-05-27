package com.xl.alm.job.cost.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 高性能内部收益率(IRR/XIRR)计算工具类
 * 使用优化算法实现类似Excel的计算性能
 *
 * @author AI Assistant
 */
@Slf4j
public class FastXirrCalculator {

    /** 最大迭代次数 */
    private static final int MAX_ITERATIONS = 100;

    /** 精度 */
    private static final double PRECISION = 1e-10;

    /** 结果精度 */
    private static final int RESULT_SCALE = 6;

    /** 缓存，用于存储已计算过的IRR结果 */
    private static final ConcurrentMap<String, Double> IRR_CACHE = new ConcurrentHashMap<>();

    /**
     * 计算内部收益率(IRR)，使用优化算法提高性能
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

        // 转换为double数组以提高性能
        double[] cashflowArray = new double[cashflows.size()];
        for (int i = 0; i < cashflows.size(); i++) {
            cashflowArray[i] = cashflows.get(i).doubleValue();
        }

        // 生成缓存键
        String cacheKey = generateCacheKey(cashflowArray);
        
        // 检查缓存
        Double cachedResult = IRR_CACHE.get(cacheKey);
        if (cachedResult != null) {
            return BigDecimal.valueOf(cachedResult).setScale(RESULT_SCALE, RoundingMode.HALF_UP);
        }

        // 使用组合算法计算IRR
        Double result = combinedMethod(cashflowArray);
        
        // 存入缓存
        if (result != null) {
            IRR_CACHE.put(cacheKey, result);
        }

        return result != null ? 
               BigDecimal.valueOf(result).setScale(RESULT_SCALE, RoundingMode.HALF_UP) : 
               null;
    }

    /**
     * 使用组合算法计算IRR（结合二分法和牛顿法的优点）
     *
     * @param cashflows 现金流数组
     * @return 内部收益率，如果无法计算则返回null
     */
    private static Double combinedMethod(double[] cashflows) {
        // 初始搜索范围
        double lowerBound = -0.999; // 避免除以零
        double upperBound = 1.0;    // 初始上界
        
        // 计算初始NPV值
        double lowerNpv = calculateNPV(cashflows, lowerBound);
        double upperNpv = calculateNPV(cashflows, upperBound);
        
        // 如果上界NPV为正，则扩大上界
        while (upperNpv > 0) {
            upperBound *= 2;
            if (upperBound > 100) {
                log.warn("IRR计算超出合理范围");
                return null; // 防止无限循环
            }
            upperNpv = calculateNPV(cashflows, upperBound);
        }
        
        // 使用二分法找到一个好的初始猜测值
        double guess = (lowerBound + upperBound) / 2;
        for (int i = 0; i < 10; i++) { // 只进行少量二分迭代
            double npv = calculateNPV(cashflows, guess);
            if (Math.abs(npv) < PRECISION) {
                return guess;
            }
            
            if (npv > 0) {
                lowerBound = guess;
            } else {
                upperBound = guess;
            }
            guess = (lowerBound + upperBound) / 2;
        }
        
        // 使用牛顿法进行精确计算
        return newtonRaphsonMethod(cashflows, guess);
    }

    /**
     * 使用牛顿迭代法计算IRR
     *
     * @param cashflows 现金流数组
     * @param initialGuess 初始猜测值
     * @return 内部收益率，如果无法计算则返回null
     */
    private static Double newtonRaphsonMethod(double[] cashflows, double initialGuess) {
        double rate = initialGuess;
        
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            double npv = calculateNPV(cashflows, rate);
            
            // 如果NPV已经足够接近零，则认为已经找到解
            if (Math.abs(npv) < PRECISION) {
                return rate;
            }
            
            double derivative = calculateDerivative(cashflows, rate);
            
            // 如果导数接近于0，则无法继续迭代
            if (Math.abs(derivative) < PRECISION) {
                log.warn("导数接近于0，无法继续迭代");
                return null;
            }
            
            // 计算新的猜测值
            double newRate = rate - npv / derivative;
            
            // 如果新旧猜测值的差小于精度要求，则认为已经收敛
            if (Math.abs(newRate - rate) < PRECISION) {
                return newRate;
            }
            
            // 防止发散
            if (Double.isNaN(newRate) || Double.isInfinite(newRate) || newRate < -1) {
                log.warn("IRR计算发散");
                return null;
            }
            
            rate = newRate;
        }
        
        log.warn("超过最大迭代次数，无法收敛");
        return null;
    }

    /**
     * 计算净现值(NPV)
     *
     * @param cashflows 现金流数组
     * @param rate 折现率
     * @return 净现值
     */
    private static double calculateNPV(double[] cashflows, double rate) {
        double npv = 0.0;
        
        for (int i = 0; i < cashflows.length; i++) {
            npv += cashflows[i] / Math.pow(1.0 + rate, i);
        }
        
        return npv;
    }

    /**
     * 计算净现值对折现率的导数
     *
     * @param cashflows 现金流数组
     * @param rate 折现率
     * @return 导数值
     */
    private static double calculateDerivative(double[] cashflows, double rate) {
        double derivative = 0.0;
        
        for (int i = 1; i < cashflows.length; i++) {
            derivative -= i * cashflows[i] / Math.pow(1.0 + rate, i + 1);
        }
        
        return derivative;
    }

    /**
     * 生成缓存键
     *
     * @param cashflows 现金流数组
     * @return 缓存键
     */
    private static String generateCacheKey(double[] cashflows) {
        StringBuilder sb = new StringBuilder();
        for (double cf : cashflows) {
            sb.append(cf).append('|');
        }
        return sb.toString();
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
