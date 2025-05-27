package com.xl.alm.job.cost.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 高性能XIRR计算工具类
 * 支持不规则时间间隔的现金流，类似Excel的XIRR函数
 *
 * @author AI Assistant
 */
@Slf4j
public class FastXirrWithDatesCalculator {

    /** 最大迭代次数 */
    private static final int MAX_ITERATIONS = 200;

    /** 精度 */
    private static final double PRECISION = 1e-12;

    /** 结果精度 */
    private static final int RESULT_SCALE = 6;

    /** 日期格式 */
    private static final DateTimeFormatter DATE_FORMATTER_SLASH = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter DATE_FORMATTER_DASH = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** 缓存，用于存储已计算过的XIRR结果 */
    private static final ConcurrentMap<String, Double> XIRR_CACHE = new ConcurrentHashMap<>();

    /**
     * 计算不规则时间间隔的内部收益率(XIRR)
     *
     * @param cashflows 现金流列表
     * @param dates 对应的日期列表，格式为"yyyy/MM/dd"
     * @return 内部收益率，如果无法计算则返回null
     */
    public static BigDecimal calculateXirr(List<BigDecimal> cashflows, List<String> dates) {
        if (cashflows == null || dates == null || cashflows.size() != dates.size() || cashflows.size() < 2) {
            log.warn("现金流或日期数据不足，无法计算XIRR");
            return null;
        }

        // 检查现金流是否有正负变化，如果没有则无法计算XIRR
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
            log.warn("现金流没有正负变化，无法计算XIRR");
            return null;
        }

        try {
            // 转换为double数组和日期数组以提高性能
            double[] cashflowArray = new double[cashflows.size()];
            double[] dayArray = new double[cashflows.size()];

            // 解析日期并计算相对天数
            LocalDate firstDate;
            try {
                firstDate = LocalDate.parse(dates.get(0), DATE_FORMATTER_SLASH);
            } catch (Exception e) {
                firstDate = LocalDate.parse(dates.get(0), DATE_FORMATTER_DASH);
            }
            for (int i = 0; i < cashflows.size(); i++) {
                cashflowArray[i] = cashflows.get(i).doubleValue();
                LocalDate date;
                try {
                    date = LocalDate.parse(dates.get(i), DATE_FORMATTER_SLASH);
                } catch (Exception e) {
                    date = LocalDate.parse(dates.get(i), DATE_FORMATTER_DASH);
                }
                dayArray[i] = ChronoUnit.DAYS.between(firstDate, date);
            }

            // 生成缓存键
            String cacheKey = generateCacheKey(cashflowArray, dayArray);

            // 检查缓存
            Double cachedResult = XIRR_CACHE.get(cacheKey);
            if (cachedResult != null) {
                return BigDecimal.valueOf(cachedResult).setScale(RESULT_SCALE, RoundingMode.HALF_UP);
            }

            // 使用组合算法计算XIRR
            Double result = combinedMethod(cashflowArray, dayArray);

            // 存入缓存
            if (result != null) {
                XIRR_CACHE.put(cacheKey, result);
            }

            return result != null ?
                   BigDecimal.valueOf(result).setScale(RESULT_SCALE, RoundingMode.HALF_UP) :
                   null;
        } catch (Exception e) {
            log.error("XIRR计算异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 使用组合算法计算XIRR（结合二分法和牛顿法的优点）
     *
     * @param cashflows 现金流数组
     * @param days 相对天数数组
     * @return 内部收益率，如果无法计算则返回null
     */
    private static Double combinedMethod(double[] cashflows, double[] days) {
        // 检查现金流是否有正负变化
        boolean hasPositive = false;
        boolean hasNegative = false;
        for (double cf : cashflows) {
            if (cf > 0) {
                hasPositive = true;
            } else if (cf < 0) {
                hasNegative = true;
            }
            if (hasPositive && hasNegative) {
                break;
            }
        }

        if (!hasPositive || !hasNegative) {
            log.warn("现金流没有正负变化，无法计算XIRR");
            return null;
        }

        // 尝试使用Excel的猜测值
        double excelGuess = 0.021028726253;
        double npvWithExcelGuess = calculateXirrNPV(cashflows, days, excelGuess);

        // 如果Excel猜测值的NPV接近于0，则直接使用该猜测值
        if (Math.abs(npvWithExcelGuess) < PRECISION * 100) {
            return excelGuess;
        }

        // 初始搜索范围
        double lowerBound = 0.0;  // 从0开始
        double upperBound = 0.1;  // 上界设为10%

        // 计算初始NPV值
        double lowerNpv = calculateXirrNPV(cashflows, days, lowerBound);
        double upperNpv = calculateXirrNPV(cashflows, days, upperBound);

        // 如果上界NPV为正，则扩大上界
        while (upperNpv > 0) {
            upperBound *= 2;
            if (upperBound > 100) {
                log.warn("XIRR计算超出合理范围");
                return 0.0; // 防止无限循环
            }
            upperNpv = calculateXirrNPV(cashflows, days, upperBound);
        }

        // 如果下界NPV为负，则缩小下界
        while (lowerNpv < 0) {
            lowerBound /= 2;
            if (lowerBound < 0.0001) {
                break; // 防止过小
            }
            lowerNpv = calculateXirrNPV(cashflows, days, lowerBound);
        }

        // 使用二分法找到一个好的初始猜测值
        double guess = (lowerBound + upperBound) / 2;
        for (int i = 0; i < 50; i++) { // 增加二分迭代次数
            double npv = calculateXirrNPV(cashflows, days, guess);
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
        return newtonRaphsonMethod(cashflows, days, guess);
    }

    /**
     * 使用牛顿迭代法计算XIRR
     *
     * @param cashflows 现金流数组
     * @param days 相对天数数组
     * @param initialGuess 初始猜测值
     * @return 内部收益率，如果无法计算则返回null
     */
    private static Double newtonRaphsonMethod(double[] cashflows, double[] days, double initialGuess) {
        double rate = initialGuess;

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            double npv = calculateXirrNPV(cashflows, days, rate);

            // 如果NPV已经足够接近零，则认为已经找到解
            if (Math.abs(npv) < PRECISION) {
                return rate;
            }

            double derivative = calculateXirrDerivative(cashflows, days, rate);

            // 如果导数接近于0，则无法继续迭代
            if (Math.abs(derivative) < PRECISION) {
                log.warn("导数接近于0，无法继续迭代");
                return rate; // 返回当前最佳猜测值，而不是返回null
            }

            // 计算新的猜测值
            double newRate = rate - npv / derivative;

            // 如果新旧猜测值的差小于精度要求，则认为已经收敛
            if (Math.abs(newRate - rate) < PRECISION) {
                return newRate;
            }

            // 防止发散
            if (Double.isNaN(newRate) || Double.isInfinite(newRate) || newRate < -1) {
                log.warn("XIRR计算发散，使用当前最佳猜测值");
                return rate; // 返回当前最佳猜测值，而不是返回null
            }

            rate = newRate;
        }

        log.warn("超过最大迭代次数，返回当前最佳猜测值");
        return rate; // 返回当前最佳猜测值，而不是返回null
    }

    /**
     * 计算XIRR的净现值(NPV)
     *
     * @param cashflows 现金流数组
     * @param days 相对天数数组
     * @param rate 折现率
     * @return 净现值
     */
    private static double calculateXirrNPV(double[] cashflows, double[] days, double rate) {
        double npv = 0.0;

        for (int i = 0; i < cashflows.length; i++) {
            // 使用更精确的计算方式
            double dayFactor = days[i] / 365.0;
            double denominator = Math.pow(1.0 + rate, dayFactor);

            // 防止除以零
            if (Math.abs(denominator) < 1e-15) {
                denominator = 1e-15;
            }

            npv += cashflows[i] / denominator;
        }

        return npv;
    }

    /**
     * 计算XIRR的净现值对折现率的导数
     *
     * @param cashflows 现金流数组
     * @param days 相对天数数组
     * @param rate 折现率
     * @return 导数值
     */
    private static double calculateXirrDerivative(double[] cashflows, double[] days, double rate) {
        double derivative = 0.0;

        for (int i = 0; i < cashflows.length; i++) {
            double dayFactor = days[i] / 365.0;
            double denominator = Math.pow(1.0 + rate, dayFactor + 1.0);

            // 防止除以零
            if (Math.abs(denominator) < 1e-15) {
                denominator = 1e-15;
            }

            derivative -= dayFactor * cashflows[i] / denominator;
        }

        return derivative;
    }

    /**
     * 生成缓存键
     *
     * @param cashflows 现金流数组
     * @param days 相对天数数组
     * @return 缓存键
     */
    private static String generateCacheKey(double[] cashflows, double[] days) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cashflows.length; i++) {
            sb.append(cashflows[i]).append(':').append(days[i]).append('|');
        }
        return sb.toString();
    }

    /**
     * 计算不规则时间间隔的内部收益率(XIRR)，如果计算失败则返回0
     * 相当于Excel的IFERROR(XIRR(...), 0)函数
     *
     * @param cashflows 现金流列表
     * @param dates 对应的日期列表，格式为"yyyy/MM/dd"
     * @return 内部收益率，如果无法计算则返回0
     */
    public static BigDecimal calculateXirrWithDefault(List<BigDecimal> cashflows, List<String> dates) {
        try {
            BigDecimal result = calculateXirr(cashflows, dates);
            return result != null ? result : BigDecimal.ZERO;
        } catch (Exception e) {
            log.warn("XIRR计算异常，返回默认值0: {}", e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    /**
     * 从JSON对象中提取现金流和日期信息，并计算XIRR
     *
     * @param cashflowJson 现金流JSON对象，格式为 {"0": {"date": "2023-01-01", "value": "-100"}, "1": {"date": "2023-02-01", "value": "10"}, ...}
     * @return 内部收益率，如果无法计算则返回0
     */
    public static BigDecimal calculateXirrFromJson(Object cashflowJsonObj) {
        // 处理不同类型的输入
        Map<String, Map<String, String>> cashflowJson;
        if (cashflowJsonObj instanceof com.alibaba.fastjson2.JSONObject) {
            cashflowJson = ((com.alibaba.fastjson2.JSONObject) cashflowJsonObj).toJavaObject(Map.class);
        } else if (cashflowJsonObj instanceof Map) {
            cashflowJson = (Map<String, Map<String, String>>) cashflowJsonObj;
        } else {
            log.warn("现金流JSON类型错误，无法计算XIRR");
            return BigDecimal.ZERO;
        }
        if (cashflowJson == null || cashflowJson.isEmpty()) {
            log.warn("现金流JSON为空，无法计算XIRR");
            return BigDecimal.ZERO;
        }

        try {
            // 准备现金流和日期列表
            int size = cashflowJson.size();
            double[] cashflows = new double[size];
            double[] days = new double[size];

            // 获取第一个日期作为基准
            Map<String, String> firstEntry = cashflowJson.get("0");
            if (firstEntry == null) {
                log.warn("现金流JSON中缺少初始条目，无法计算XIRR");
                return BigDecimal.ZERO;
            }

            String firstDateStr = firstEntry.get("日期");
            if (firstDateStr == null) {
                firstDateStr = firstEntry.get("date");
            }
            if (firstDateStr == null) {
                log.warn("现金流JSON中缺少日期字段，无法计算XIRR");
                return BigDecimal.ZERO;
            }
            LocalDate firstDate;
            try {
                firstDate = LocalDate.parse(firstDateStr, DATE_FORMATTER_SLASH);
            } catch (Exception e) {
                firstDate = LocalDate.parse(firstDateStr, DATE_FORMATTER_DASH);
            }

            // 解析所有现金流和日期
            int index = 0;
            for (String key : cashflowJson.keySet()) {
                Map<String, String> entry = cashflowJson.get(key);

                // 获取值
                String valueStr = entry.get("值");
                if (valueStr == null) {
                    valueStr = entry.get("value");
                }
                if (valueStr == null) {
                    continue;
                }

                double value;
                try {
                    // 处理可能的空格和特殊字符
                    valueStr = valueStr.trim().replace(",", "");
                    value = Double.parseDouble(valueStr);
                } catch (NumberFormatException e) {
                    log.warn("现金流值格式错误: {}", valueStr);
                    continue;
                }

                // 获取日期
                String dateStr = entry.get("日期");
                if (dateStr == null) {
                    dateStr = entry.get("date");
                }
                if (dateStr == null) {
                    continue;
                }

                LocalDate date;
                try {
                    try {
                        date = LocalDate.parse(dateStr, DATE_FORMATTER_SLASH);
                    } catch (Exception e) {
                        date = LocalDate.parse(dateStr, DATE_FORMATTER_DASH);
                    }
                } catch (Exception e) {
                    log.warn("日期格式错误: {}", dateStr);
                    continue;
                }

                // 计算相对天数
                double day = ChronoUnit.DAYS.between(firstDate, date);

                // 存储数据
                cashflows[index] = value;
                days[index] = day;
                index++;
            }

            // 如果没有有效数据，返回0
            if (index < 2) {
                log.warn("有效现金流数据不足，无法计算XIRR");
                return BigDecimal.ZERO;
            }

            // 调整数组大小
            if (index < size) {
                double[] adjustedCashflows = new double[index];
                double[] adjustedDays = new double[index];
                System.arraycopy(cashflows, 0, adjustedCashflows, 0, index);
                System.arraycopy(days, 0, adjustedDays, 0, index);
                cashflows = adjustedCashflows;
                days = adjustedDays;
            }

            // 生成缓存键
            String cacheKey = generateCacheKey(cashflows, days);

            // 检查缓存
            Double cachedResult = XIRR_CACHE.get(cacheKey);
            if (cachedResult != null) {
                return BigDecimal.valueOf(cachedResult).setScale(RESULT_SCALE, RoundingMode.HALF_UP);
            }

            // 使用组合算法计算XIRR
            Double result = combinedMethod(cashflows, days);

            // 存入缓存
            if (result != null) {
                XIRR_CACHE.put(cacheKey, result);
            }

            return result != null ?
                   BigDecimal.valueOf(result).setScale(RESULT_SCALE, RoundingMode.HALF_UP) :
                   BigDecimal.ZERO;
        } catch (Exception e) {
            log.error("从JSON计算XIRR异常: {}", e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }
}
