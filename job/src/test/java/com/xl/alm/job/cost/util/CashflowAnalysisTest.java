package com.xl.alm.job.cost.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 现金流分析测试类
 */
@Slf4j
public class CashflowAnalysisTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 分析现金流数据
     */
    @Test
    public void analyzeCashflows() {
        // 用户数据的前几个条目
        String jsonStr = "{\"0\":{\"date\":\"2024-12-31\",\"value\":\"11126.6974100000\"},\"1\":{\"date\":\"2025-01-31\",\"value\":\"1378.61557618\"},\"2\":{\"date\":\"2025-02-28\",\"value\":\"1358.54167768\"},\"3\":{\"date\":\"2025-03-31\",\"value\":\"1336.26759018\"},\"4\":{\"date\":\"2025-04-30\",\"value\":\"1314.89585230\"},\"5\":{\"date\":\"2025-05-31\",\"value\":\"4969.27102563\"}}";
        
        JSONObject cashflowJson = JSON.parseObject(jsonStr);
        
        // 提取现金流和日期
        List<BigDecimal> cashflows = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        
        for (Map.Entry<String, Object> entry : cashflowJson.entrySet()) {
            JSONObject flowEntry = (JSONObject) entry.getValue();
            String dateStr = flowEntry.getString("date");
            String valueStr = flowEntry.getString("value");
            
            BigDecimal value = new BigDecimal(valueStr);
            cashflows.add(value);
            dates.add(dateStr);
            
            log.info("现金流 {}: 日期={}, 值={}", entry.getKey(), dateStr, value);
        }
        
        // 检查现金流是否有正负变化
        boolean hasPositive = false;
        boolean hasNegative = false;
        for (BigDecimal cf : cashflows) {
            if (cf.compareTo(BigDecimal.ZERO) > 0) {
                hasPositive = true;
                log.info("发现正现金流: {}", cf);
            } else if (cf.compareTo(BigDecimal.ZERO) < 0) {
                hasNegative = true;
                log.info("发现负现金流: {}", cf);
            }
        }
        
        log.info("现金流是否有正值: {}", hasPositive);
        log.info("现金流是否有负值: {}", hasNegative);
        
        // 如果没有正负变化，则无法计算IRR
        if (!hasPositive || !hasNegative) {
            log.warn("现金流没有正负变化，无法计算IRR");
        }
    }
    
    /**
     * 测试Excel公式中的初始猜测值
     */
    @Test
    public void testExcelGuess() {
        // 用户数据的前几个条目
        String jsonStr = "{\"0\":{\"date\":\"2024-12-31\",\"value\":\"11126.6974100000\"},\"1\":{\"date\":\"2025-01-31\",\"value\":\"1378.61557618\"},\"2\":{\"date\":\"2025-02-28\",\"value\":\"1358.54167768\"},\"3\":{\"date\":\"2025-03-31\",\"value\":\"1336.26759018\"},\"4\":{\"date\":\"2025-04-30\",\"value\":\"1314.89585230\"},\"5\":{\"date\":\"2025-05-31\",\"value\":\"4969.27102563\"}}";
        
        JSONObject cashflowJson = JSON.parseObject(jsonStr);
        
        // 提取现金流和日期
        List<BigDecimal> cashflows = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        
        for (Map.Entry<String, Object> entry : cashflowJson.entrySet()) {
            JSONObject flowEntry = (JSONObject) entry.getValue();
            String dateStr = flowEntry.getString("date");
            String valueStr = flowEntry.getString("value");
            
            BigDecimal value = new BigDecimal(valueStr);
            cashflows.add(value);
            dates.add(dateStr);
        }
        
        // 使用初始猜测值0计算XIRR
        BigDecimal xirrWithZeroGuess = calculateXirrWithGuess(cashflows, dates, 0.0);
        log.info("使用初始猜测值0计算XIRR: {}", xirrWithZeroGuess);
        
        // 使用Excel计算结果作为初始猜测值
        BigDecimal xirrWithExcelGuess = calculateXirrWithGuess(cashflows, dates, 0.021028726253);
        log.info("使用Excel计算结果作为初始猜测值计算XIRR: {}", xirrWithExcelGuess);
    }
    
    /**
     * 使用指定的初始猜测值计算XIRR
     */
    private BigDecimal calculateXirrWithGuess(List<BigDecimal> cashflows, List<String> dates, double guess) {
        // 转换为double数组和LocalDate数组
        double[] values = new double[cashflows.size()];
        LocalDate[] dateDates = new LocalDate[dates.size()];
        
        for (int i = 0; i < cashflows.size(); i++) {
            values[i] = cashflows.get(i).doubleValue();
            dateDates[i] = LocalDate.parse(dates.get(i), DATE_FORMATTER);
        }
        
        // 计算相对天数
        double[] days = new double[dateDates.length];
        for (int i = 0; i < dateDates.length; i++) {
            days[i] = java.time.temporal.ChronoUnit.DAYS.between(dateDates[0], dateDates[i]);
        }
        
        // 计算NPV
        double npv = calculateNpv(values, days, guess);
        log.info("使用猜测值{}计算NPV: {}", guess, npv);
        
        return BigDecimal.valueOf(guess);
    }
    
    /**
     * 计算NPV
     */
    private double calculateNpv(double[] values, double[] days, double rate) {
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
}
