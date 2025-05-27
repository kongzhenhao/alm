package com.xl.alm.job.cost.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * XIRR计算调试测试类
 */
@Slf4j
public class XirrDebugTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final double PRECISION = 1e-12;
    private static final int MAX_ITERATIONS = 200;

    /**
     * 使用Excel的初始猜测值计算XIRR
     */
    @Test
    public void testWithExcelGuess() {
        // 用户数据的前几个条目
        String jsonStr = "{\"0\":{\"date\":\"2024-12-31\",\"value\":\"11126.6974100000\"},\"1\":{\"date\":\"2025-01-31\",\"value\":\"1378.61557618\"},\"2\":{\"date\":\"2025-02-28\",\"value\":\"1358.54167768\"},\"3\":{\"date\":\"2025-03-31\",\"value\":\"1336.26759018\"},\"4\":{\"date\":\"2025-04-30\",\"value\":\"1314.89585230\"},\"5\":{\"date\":\"2025-05-31\",\"value\":\"4969.27102563\"}}";
        
        JSONObject cashflowJson = JSON.parseObject(jsonStr);
        
        // 提取现金流和日期
        List<Double> cashflows = new ArrayList<>();
        List<LocalDate> dates = new ArrayList<>();
        
        for (Map.Entry<String, Object> entry : cashflowJson.entrySet()) {
            JSONObject flowEntry = (JSONObject) entry.getValue();
            String dateStr = flowEntry.getString("date");
            String valueStr = flowEntry.getString("value");
            
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
            Double value = Double.parseDouble(valueStr);
            
            cashflows.add(value);
            dates.add(date);
        }
        
        // 计算相对天数
        LocalDate firstDate = dates.get(0);
        double[] days = new double[dates.size()];
        for (int i = 0; i < dates.size(); i++) {
            days[i] = ChronoUnit.DAYS.between(firstDate, dates.get(i));
            log.info("现金流 {}: 日期={}, 值={}, 相对天数={}", i, dates.get(i), cashflows.get(i), days[i]);
        }
        
        // 使用Excel的初始猜测值
        double excelGuess = 0.021028726253;
        
        // 计算NPV
        double npv = calculateNPV(cashflows, days, excelGuess);
        log.info("使用Excel猜测值计算NPV: {}", npv);
        
        // 使用牛顿法迭代
        double result = newtonMethod(cashflows, days, excelGuess);
        log.info("使用Excel猜测值计算XIRR: {}", result);
        
        // Excel计算结果
        log.info("Excel计算结果: 0.021028726253");
    }
    
    /**
     * 使用二分法计算XIRR
     */
    @Test
    public void testWithBisection() {
        // 用户数据的前几个条目
        String jsonStr = "{\"0\":{\"date\":\"2024-12-31\",\"value\":\"11126.6974100000\"},\"1\":{\"date\":\"2025-01-31\",\"value\":\"1378.61557618\"},\"2\":{\"date\":\"2025-02-28\",\"value\":\"1358.54167768\"},\"3\":{\"date\":\"2025-03-31\",\"value\":\"1336.26759018\"},\"4\":{\"date\":\"2025-04-30\",\"value\":\"1314.89585230\"},\"5\":{\"date\":\"2025-05-31\",\"value\":\"4969.27102563\"}}";
        
        JSONObject cashflowJson = JSON.parseObject(jsonStr);
        
        // 提取现金流和日期
        List<Double> cashflows = new ArrayList<>();
        List<LocalDate> dates = new ArrayList<>();
        
        for (Map.Entry<String, Object> entry : cashflowJson.entrySet()) {
            JSONObject flowEntry = (JSONObject) entry.getValue();
            String dateStr = flowEntry.getString("date");
            String valueStr = flowEntry.getString("value");
            
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
            Double value = Double.parseDouble(valueStr);
            
            cashflows.add(value);
            dates.add(date);
        }
        
        // 计算相对天数
        LocalDate firstDate = dates.get(0);
        double[] days = new double[dates.size()];
        for (int i = 0; i < dates.size(); i++) {
            days[i] = ChronoUnit.DAYS.between(firstDate, dates.get(i));
        }
        
        // 使用二分法计算XIRR
        double result = bisectionMethod(cashflows, days);
        log.info("使用二分法计算XIRR: {}", result);
        
        // Excel计算结果
        log.info("Excel计算结果: 0.021028726253");
    }
    
    /**
     * 使用二分法计算XIRR
     */
    private double bisectionMethod(List<Double> cashflows, double[] days) {
        // 初始搜索范围
        double lowerBound = -0.999; // 避免除以零
        double upperBound = 10.0;   // 扩大上界
        
        // 计算初始NPV值
        double lowerNpv = calculateNPV(cashflows, days, lowerBound);
        double upperNpv = calculateNPV(cashflows, days, upperBound);
        
        log.info("初始下界: {}, NPV: {}", lowerBound, lowerNpv);
        log.info("初始上界: {}, NPV: {}", upperBound, upperNpv);
        
        // 确保NPV有正负变化
        if (lowerNpv * upperNpv >= 0) {
            log.warn("NPV没有正负变化，无法使用二分法");
            return 0;
        }
        
        // 使用二分法查找XIRR
        double mid = 0;
        double midNpv = 0;
        
        for (int i = 0; i < 100; i++) {
            mid = (lowerBound + upperBound) / 2;
            midNpv = calculateNPV(cashflows, days, mid);
            
            log.info("迭代 {}: 中点={}, NPV={}", i, mid, midNpv);
            
            if (Math.abs(midNpv) < PRECISION) {
                log.info("找到解: {}", mid);
                return mid;
            }
            
            if (midNpv * lowerNpv < 0) {
                upperBound = mid;
                upperNpv = midNpv;
            } else {
                lowerBound = mid;
                lowerNpv = midNpv;
            }
            
            if (upperBound - lowerBound < PRECISION) {
                log.info("区间收敛: {}", mid);
                return mid;
            }
        }
        
        log.info("二分法最终结果: {}", mid);
        return mid;
    }
    
    /**
     * 使用牛顿法计算XIRR
     */
    private double newtonMethod(List<Double> cashflows, double[] days, double initialGuess) {
        double rate = initialGuess;
        
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            double npv = calculateNPV(cashflows, days, rate);
            
            if (Math.abs(npv) < PRECISION) {
                log.info("牛顿法找到解: {}", rate);
                return rate;
            }
            
            double derivative = calculateDerivative(cashflows, days, rate);
            
            if (Math.abs(derivative) < PRECISION) {
                log.warn("导数接近于0，无法继续迭代");
                return rate;
            }
            
            double newRate = rate - npv / derivative;
            
            log.info("迭代 {}: 当前率={}, NPV={}, 导数={}, 新率={}", i, rate, npv, derivative, newRate);
            
            if (Math.abs(newRate - rate) < PRECISION) {
                log.info("牛顿法收敛: {}", newRate);
                return newRate;
            }
            
            if (Double.isNaN(newRate) || Double.isInfinite(newRate) || newRate < -1) {
                log.warn("牛顿法发散");
                return rate;
            }
            
            rate = newRate;
        }
        
        log.warn("超过最大迭代次数");
        return rate;
    }
    
    /**
     * 计算NPV
     */
    private double calculateNPV(List<Double> cashflows, double[] days, double rate) {
        double npv = 0.0;
        
        for (int i = 0; i < cashflows.size(); i++) {
            double dayFactor = days[i] / 365.0;
            double denominator = Math.pow(1.0 + rate, dayFactor);
            
            if (Math.abs(denominator) < 1e-15) {
                denominator = 1e-15;
            }
            
            npv += cashflows.get(i) / denominator;
        }
        
        return npv;
    }
    
    /**
     * 计算导数
     */
    private double calculateDerivative(List<Double> cashflows, double[] days, double rate) {
        double derivative = 0.0;
        
        for (int i = 0; i < cashflows.size(); i++) {
            double dayFactor = days[i] / 365.0;
            double denominator = Math.pow(1.0 + rate, dayFactor + 1.0);
            
            if (Math.abs(denominator) < 1e-15) {
                denominator = 1e-15;
            }
            
            derivative -= dayFactor * cashflows.get(i) / denominator;
        }
        
        return derivative;
    }
}
