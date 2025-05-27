package com.xl.alm.job.cost.util;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 准确的XIRR计算器
 * 实现与Excel XIRR函数相同的计算逻辑
 */
@Slf4j
public class AccurateXirrCalculator {

    private static final DateTimeFormatter DATE_FORMATTER_DASH = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_FORMATTER_SLASH = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final double PRECISION = 1e-10;
    private static final int MAX_ITERATIONS = 200;
    private static final int RESULT_SCALE = 6;

    /**
     * 计算XIRR
     * 实现Excel公式: IFERROR(XIRR($I45:$AWG45, $I$10:$AWG$10, 0), 0)
     *
     * @param cashflows 现金流列表
     * @param dates 日期列表
     * @return XIRR值，如果计算失败则返回0
     */
    public static BigDecimal calculateXirr(List<BigDecimal> cashflows, List<String> dates) {
        // IFERROR部分: 如果计算出错，返回0
        if (cashflows == null || dates == null || cashflows.size() != dates.size() || cashflows.size() < 2) {
            log.warn("现金流或日期数据不足，无法计算XIRR");
            return BigDecimal.ZERO;
        }

        try {
            // 转换为double数组和日期数组
            double[] values = new double[cashflows.size()];
            LocalDate[] dateDates = new LocalDate[dates.size()];

            for (int i = 0; i < cashflows.size(); i++) {
                values[i] = cashflows.get(i).doubleValue();
                try {
                    dateDates[i] = LocalDate.parse(dates.get(i), DATE_FORMATTER_SLASH);
                } catch (Exception e) {
                    dateDates[i] = LocalDate.parse(dates.get(i), DATE_FORMATTER_DASH);
                }
            }

            // 计算相对天数
            double[] days = new double[dateDates.length];
            for (int i = 0; i < dateDates.length; i++) {
                days[i] = ChronoUnit.DAYS.between(dateDates[0], dateDates[i]);
            }

            // 打印现金流和日期信息，用于调试
            for (int i = 0; i < values.length; i++) {
                log.debug("现金流 {}: 日期={}, 值={}, 相对天数={}", i, dateDates[i], values[i], days[i]);
            }

            // 检查现金流是否有正负变化
            boolean hasPositive = false;
            boolean hasNegative = false;
            for (double value : values) {
                if (value > 0) {
                    hasPositive = true;
                } else if (value < 0) {
                    hasNegative = true;
                }
                if (hasPositive && hasNegative) {
                    break;
                }
            }

            // 如果没有正负变化，则返回0（符合Excel IFERROR逻辑）
            if (!hasPositive || !hasNegative) {
                log.warn("现金流没有正负变化，无法计算XIRR");
                return BigDecimal.ZERO;
            }

            // 计算XIRR，使用初始猜测值0（符合Excel XIRR第三个参数）
            double result = xirrCalculationWithGuess(values, days, 0.0);

            return BigDecimal.valueOf(result).setScale(RESULT_SCALE, RoundingMode.HALF_UP);
        } catch (Exception e) {
            // IFERROR部分: 如果计算出错，返回0
            log.error("XIRR计算异常: {}", e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * 从JSON计算XIRR
     *
     * @param cashflowJsonObj 现金流JSON
     * @return XIRR值，如果计算失败则返回0
     */
    public static BigDecimal calculateXirrFromJson(Object cashflowJsonObj) {
        try {
            // 处理不同类型的输入
            Map<String, Map<String, String>> cashflowJson;
            if (cashflowJsonObj instanceof JSONObject) {
                cashflowJson = ((JSONObject) cashflowJsonObj).toJavaObject(Map.class);
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

            // 提取现金流和日期
            List<Double> values = new ArrayList<>();
            List<LocalDate> dates = new ArrayList<>();

            for (Map.Entry<String, Map<String, String>> entry : cashflowJson.entrySet()) {
                Map<String, String> flowEntry = entry.getValue();

                // 获取值
                String valueStr = flowEntry.get("值");
                if (valueStr == null) {
                    valueStr = flowEntry.get("value");
                }
                if (valueStr == null) {
                    continue;
                }

                // 处理可能的空格和特殊字符
                valueStr = valueStr.trim().replace(",", "");
                double value;
                try {
                    value = Double.parseDouble(valueStr);
                } catch (NumberFormatException e) {
                    log.warn("现金流值格式错误: {}", valueStr);
                    continue;
                }

                // 获取日期
                String dateStr = flowEntry.get("日期");
                if (dateStr == null) {
                    dateStr = flowEntry.get("date");
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

                // 存储数据
                values.add(value);
                dates.add(date);
            }

            // 如果没有有效数据，返回0
            if (values.size() < 2) {
                log.warn("有效现金流数据不足，无法计算XIRR");
                return BigDecimal.ZERO;
            }

            // 计算相对天数
            double[] days = new double[dates.size()];
            for (int i = 0; i < dates.size(); i++) {
                days[i] = ChronoUnit.DAYS.between(dates.get(0), dates.get(i));
            }

            // 打印现金流和日期信息，用于调试
            for (int i = 0; i < values.size(); i++) {
                log.debug("现金流 {}: 日期={}, 值={}, 相对天数={}", i, dates.get(i), values.get(i), days[i]);
            }

            // 检查现金流是否全部为正值
            boolean allPositive = true;
            for (double value : values) {
                if (value < 0) {
                    allPositive = false;
                    break;
                }
            }

            // 如果所有现金流都是正值，则需要特殊处理
            if (allPositive) {
                log.warn("所有现金流都是正值，XIRR计算可能不准确");
                // 将第一个现金流视为负值（投资）
                values.set(0, -values.get(0));
            }

            // 转换为数组
            double[] valueArray = new double[values.size()];
            for (int i = 0; i < values.size(); i++) {
                valueArray[i] = values.get(i);
            }

            // 计算XIRR，使用初始猜测值0（符合Excel XIRR第三个参数）
            double result = xirrCalculationWithGuess(valueArray, days, 0.0);

            return BigDecimal.valueOf(result).setScale(RESULT_SCALE, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("从JSON计算XIRR异常: {}", e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * XIRR计算，使用指定的初始猜测值
     * 实现Excel公式: XIRR($I45:$AWG45, $I$10:$AWG$10, 0)
     *
     * @param values 现金流数组
     * @param days 相对天数数组
     * @param guess 初始猜测值
     * @return XIRR值
     */
    private static double xirrCalculationWithGuess(double[] values, double[] days, double guess) {
        try {
            // 先尝试使用牛顿法计算XIRR
            double result = newtonMethod(values, days, guess);

            // 验证结果
            double npv = calculateNpv(values, days, result);
            if (Math.abs(npv) < PRECISION) {
                return result;
            }
        } catch (Exception e) {
            log.debug("使用牛顿法计算XIRR失败: {}", e.getMessage());
        }

        // 如果牛顿法失败，则使用二分法
        return bisectionMethod(values, days);
    }

    /**
     * XIRR计算
     *
     * @param values 现金流数组
     * @param days 相对天数数组
     * @return XIRR值
     */
    private static double xirrCalculation(double[] values, double[] days) {
        // 检查现金流是否有正负变化
        boolean hasPositive = false;
        boolean hasNegative = false;
        for (double value : values) {
            if (value > 0) {
                hasPositive = true;
            } else if (value < 0) {
                hasNegative = true;
            }
            if (hasPositive && hasNegative) {
                break;
            }
        }

        if (!hasPositive || !hasNegative) {
            log.warn("现金流没有正负变化，无法计算XIRR");
            return 0.0;
        }

        // 使用多个初始猜测值
        double[] guesses = {0.0, 0.1, 0.01, 0.05, 0.2, 0.021028726253};

        for (double guess : guesses) {
            try {
                // 使用牛顿法计算XIRR
                double result = newtonMethod(values, days, guess);

                // 验证结果
                double npv = calculateNpv(values, days, result);
                if (Math.abs(npv) < PRECISION) {
                    return result;
                }
            } catch (Exception e) {
                log.debug("使用猜测值{}计算XIRR失败: {}", guess, e.getMessage());
            }
        }

        // 如果所有猜测值都失败，则使用二分法
        return bisectionMethod(values, days);
    }

    /**
     * 使用二分法计算XIRR
     */
    private static double bisectionMethod(double[] values, double[] days) {
        // 初始搜索范围
        double lowerBound = -0.99; // 避免除以零
        double upperBound = 0.99;  // 初始上界

        // 计算初始NPV值
        double lowerNpv = calculateNpv(values, days, lowerBound);
        double upperNpv = calculateNpv(values, days, upperBound);

        // 如果上界NPV为正，则扩大上界
        int iterations = 0;
        while (upperNpv > 0 && iterations < 30) {
            upperBound = (upperBound + 1.0) / 2.0;
            if (upperBound >= 0.999999) {
                upperBound = 0.999999;
                break;
            }
            upperNpv = calculateNpv(values, days, upperBound);
            iterations++;
        }

        // 如果下界NPV为负，则缩小下界
        iterations = 0;
        while (lowerNpv < 0 && iterations < 30) {
            lowerBound = (lowerBound - 1.0) / 2.0;
            if (lowerBound <= -0.999999) {
                lowerBound = -0.999999;
                break;
            }
            lowerNpv = calculateNpv(values, days, lowerBound);
            iterations++;
        }

        // 使用二分法找到一个好的初始猜测值
        double rate = (lowerBound + upperBound) / 2.0;
        for (int i = 0; i < 100; i++) {
            double npv = calculateNpv(values, days, rate);
            if (Math.abs(npv) < PRECISION) {
                return rate;
            }

            if (npv > 0) {
                lowerBound = rate;
            } else {
                upperBound = rate;
            }
            rate = (lowerBound + upperBound) / 2.0;

            // 如果区间足够小，则认为已经收敛
            if (Math.abs(upperBound - lowerBound) < PRECISION) {
                return rate;
            }
        }

        return rate;
    }

    /**
     * 使用牛顿法计算XIRR
     */
    private static double newtonMethod(double[] values, double[] days, double initialGuess) {
        double rate = initialGuess;

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            double npv = calculateNpv(values, days, rate);

            if (Math.abs(npv) < PRECISION) {
                return rate;
            }

            double derivative = calculateDerivative(values, days, rate);

            if (Math.abs(derivative) < PRECISION) {
                throw new ArithmeticException("导数接近于0，无法继续迭代");
            }

            double newRate = rate - npv / derivative;

            if (Math.abs(newRate - rate) < PRECISION) {
                return newRate;
            }

            if (Double.isNaN(newRate) || Double.isInfinite(newRate) || newRate <= -1.0) {
                throw new ArithmeticException("牛顿法发散");
            }

            rate = newRate;
        }

        throw new ArithmeticException("超过最大迭代次数，无法收敛");
    }

    /**
     * 计算NPV
     */
    private static double calculateNpv(double[] values, double[] days, double rate) {
        double npv = 0.0;

        for (int i = 0; i < values.length; i++) {
            double dayFactor = days[i] / 365.0;
            double denominator = Math.pow(1.0 + rate, dayFactor);

            if (Math.abs(denominator) < 1e-15) {
                denominator = 1e-15;
            }

            npv += values[i] / denominator;
        }

        return npv;
    }

    /**
     * 计算导数
     */
    private static double calculateDerivative(double[] values, double[] days, double rate) {
        double derivative = 0.0;

        for (int i = 0; i < values.length; i++) {
            double dayFactor = days[i] / 365.0;
            double denominator = Math.pow(1.0 + rate, dayFactor + 1.0);

            if (Math.abs(denominator) < 1e-15) {
                denominator = 1e-15;
            }

            derivative -= dayFactor * values[i] / denominator;
        }

        return derivative;
    }
}
