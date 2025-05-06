package com.xl.alm.job.dur.util;

import com.xl.alm.job.dur.constant.CalculationConstant;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimal工具类
 *
 * @author AI Assistant
 */
public class BigDecimalUtil {
    /**
     * 加法
     *
     * @param a 加数
     * @param b 被加数
     * @return 和
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        if (a == null) {
            return b == null ? BigDecimal.ZERO : b;
        }
        if (b == null) {
            return a;
        }
        return a.add(b);
    }

    /**
     * 减法
     *
     * @param a 被减数
     * @param b 减数
     * @return 差
     */
    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        if (a == null) {
            return b == null ? BigDecimal.ZERO : b.negate();
        }
        if (b == null) {
            return a;
        }
        return a.subtract(b);
    }

    /**
     * 乘法
     *
     * @param a 乘数
     * @param b 被乘数
     * @return 积
     */
    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return BigDecimal.ZERO;
        }
        return a.multiply(b);
    }

    /**
     * 除法
     *
     * @param a 被除数
     * @param b 除数
     * @return 商
     */
    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return divide(a, b, CalculationConstant.CALCULATION_RESULT_SCALE);
    }

    /**
     * 除法
     *
     * @param a 被除数
     * @param b 除数
     * @param scale 精度
     * @return 商
     */
    public static BigDecimal divide(BigDecimal a, BigDecimal b, int scale) {
        if (a == null) {
            return BigDecimal.ZERO;
        }
        if (b == null || BigDecimal.ZERO.compareTo(b) == 0) {
            return BigDecimal.ZERO;
        }
        return a.divide(b, scale, RoundingMode.HALF_UP);
    }

    /**
     * 设置精度
     *
     * @param value 值
     * @return 设置精度后的值
     */
    public static BigDecimal setScale(BigDecimal value) {
        return setScale(value, CalculationConstant.CALCULATION_RESULT_SCALE);
    }

    /**
     * 设置精度
     *
     * @param value 值
     * @param scale 精度
     * @return 设置精度后的值
     */
    public static BigDecimal setScale(BigDecimal value, int scale) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value.setScale(scale, RoundingMode.HALF_UP);
    }
}
