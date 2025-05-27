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
 * 完整用户数据XIRR计算测试类
 */
@Slf4j
public class CompleteUserDataTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int MAX_ITERATIONS = 100;
    private static final double PRECISION = 1e-10;

    /**
     * 测试完整用户数据
     */
    @Test
    public void testCompleteUserData() {
        // 用户完整数据文件路径
        String jsonFilePath = "user_data.json";
        
        // 从文件加载JSON数据
        String jsonStr = loadUserDataJson();
        JSONObject cashflowJson = JSON.parseObject(jsonStr);
        
        // 计算XIRR
        BigDecimal xirr = FastXirrWithDatesCalculator.calculateXirrFromJson(cashflowJson);
        log.info("完整用户数据XIRR计算结果: {}", xirr);
        
        // Excel计算结果
        log.info("Excel计算结果: 0.021028726253");
    }

    /**
     * 手动实现XIRR计算，用于验证
     */
    @Test
    public void manualXirrImplementation() {
        // 从文件加载JSON数据
        String jsonStr = loadUserDataJson();
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
        double[] dayArray = new double[dates.size()];
        for (int i = 0; i < dates.size(); i++) {
            dayArray[i] = ChronoUnit.DAYS.between(firstDate, dates.get(i));
        }
        
        // 手动计算XIRR
        double result = calculateXirr(cashflows, dayArray);
        log.info("手动计算XIRR结果: {}", result);
        
        // Excel计算结果
        log.info("Excel计算结果: 0.021028726253");
    }
    
    /**
     * 手动计算XIRR
     */
    private double calculateXirr(List<Double> cashflows, double[] days) {
        // 初始猜测值为Excel结果
        double guess = 0.021028726253;
        
        // 使用二分法找到一个好的初始猜测值
        double lowerBound = -0.999; // 避免除以零
        double upperBound = 1.0;    // 初始上界
        
        // 计算初始NPV值
        double lowerNpv = calculateXirrNPV(cashflows, days, lowerBound);
        double upperNpv = calculateXirrNPV(cashflows, days, upperBound);
        
        // 如果上界NPV为正，则扩大上界
        while (upperNpv > 0) {
            upperBound *= 2;
            if (upperBound > 100) {
                log.warn("XIRR计算超出合理范围");
                return 0; // 防止无限循环
            }
            upperNpv = calculateXirrNPV(cashflows, days, upperBound);
        }
        
        // 使用二分法找到一个好的初始猜测值
        guess = (lowerBound + upperBound) / 2;
        for (int i = 0; i < 10; i++) { // 只进行少量二分迭代
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
     */
    private double newtonRaphsonMethod(List<Double> cashflows, double[] days, double initialGuess) {
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
                return 0;
            }
            
            // 计算新的猜测值
            double newRate = rate - npv / derivative;
            
            // 如果新旧猜测值的差小于精度要求，则认为已经收敛
            if (Math.abs(newRate - rate) < PRECISION) {
                return newRate;
            }
            
            // 防止发散
            if (Double.isNaN(newRate) || Double.isInfinite(newRate) || newRate < -1) {
                log.warn("XIRR计算发散");
                return 0;
            }
            
            rate = newRate;
        }
        
        log.warn("超过最大迭代次数，无法收敛");
        return rate; // 返回最后的猜测值
    }
    
    /**
     * 计算XIRR的净现值(NPV)
     */
    private double calculateXirrNPV(List<Double> cashflows, double[] days, double rate) {
        double npv = 0.0;
        
        for (int i = 0; i < cashflows.size(); i++) {
            npv += cashflows.get(i) / Math.pow(1.0 + rate, days[i] / 365.0);
        }
        
        return npv;
    }
    
    /**
     * 计算XIRR的净现值对折现率的导数
     */
    private double calculateXirrDerivative(List<Double> cashflows, double[] days, double rate) {
        double derivative = 0.0;
        
        for (int i = 0; i < cashflows.size(); i++) {
            double dayFactor = days[i] / 365.0;
            derivative -= dayFactor * cashflows.get(i) / Math.pow(1.0 + rate, dayFactor + 1.0);
        }
        
        return derivative;
    }
    
    /**
     * 加载用户数据JSON
     */
    private String loadUserDataJson() {
        // 这里应该是从文件加载JSON数据，但为了简化，我们直接返回一个字符串
        // 实际使用时，可以从文件或其他来源加载完整的JSON数据
        return "{\"0\":{\"date\":\"2024-12-31\",\"value\":\"11126.6974100000\"},\"1\":{\"date\":\"2025-01-31\",\"value\":\"1378.61557618\"},\"2\":{\"date\":\"2025-02-28\",\"value\":\"1358.54167768\"},\"3\":{\"date\":\"2025-03-31\",\"value\":\"1336.26759018\"},\"4\":{\"date\":\"2025-04-30\",\"value\":\"1314.89585230\"},\"5\":{\"date\":\"2025-05-31\",\"value\":\"4969.27102563\"}}";
    }
}
