package com.xl.alm.job.dur.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xl.alm.job.dur.constant.DurationConstant;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * 久期计算工具类
 * 
 * @author AI
 */
public class DurationUtil {
    
    /**
     * 计算折现因子
     * 
     * @param rate 利率
     * @param timeInMonths 时间（月）
     * @return 折现因子
     */
    public static BigDecimal calculateDiscountFactor(BigDecimal rate, int timeInMonths) {
        if (timeInMonths == 0) {
            return BigDecimal.ONE;
        }
        
        // 计算(1+r)^(t/12)
        BigDecimal timeInYears = BigDecimal.valueOf(timeInMonths).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);
        BigDecimal base = BigDecimal.ONE.add(rate);
        
        // 使用幂运算计算折现因子
        return BigDecimal.ONE.divide(pow(base, timeInYears), 10, RoundingMode.HALF_UP);
    }
    
    /**
     * 计算幂运算
     * 
     * @param base 底数
     * @param exponent 指数
     * @return 幂运算结果
     */
    public static BigDecimal pow(BigDecimal base, BigDecimal exponent) {
        // 使用自然对数和指数函数计算幂运算
        double result = Math.pow(base.doubleValue(), exponent.doubleValue());
        return new BigDecimal(result);
    }
    
    /**
     * 将对象转换为JSON字符串
     * 
     * @param obj 对象
     * @return JSON字符串
     */
    public static String toJsonString(Object obj) {
        return JSON.toJSONString(obj);
    }
    
    /**
     * 解析值集
     * 
     * @param valSet 值集
     * @return 值Map
     */
    public static Map<Integer, BigDecimal> parseValSet(String valSet) {
        Map<Integer, BigDecimal> valueMap = new HashMap<>();
        
        try {
            JSONObject jsonObject = JSON.parseObject(valSet);
            for (String key : jsonObject.keySet()) {
                int index = Integer.parseInt(key);
                JSONObject item = jsonObject.getJSONObject(key);
                BigDecimal value = item.getBigDecimal("value");
                valueMap.put(index, value);
            }
        } catch (Exception e) {
            // 解析失败，返回空Map
        }
        
        return valueMap;
    }
    
    /**
     * 获取期限字段名
     * 
     * @param keyDuration 关键期限点
     * @return 字段名
     */
    public static String getTermFieldName(String keyDuration) {
        // 将关键期限点转换为字段名
        // 例如：0 -> term_0, 0.5 -> term_0_5, 1 -> term_1, ...
        if (keyDuration == null || keyDuration.isEmpty()) {
            return null;
        }
        
        // 处理特殊情况
        if ("0.5".equals(keyDuration)) {
            return "term_0_5";
        }
        
        try {
            // 尝试解析为数字
            int duration = Integer.parseInt(keyDuration);
            return "term_" + duration;
        } catch (NumberFormatException e) {
            // 解析失败，返回null
            return null;
        }
    }
}
