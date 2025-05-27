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
 * 最终XIRR计算测试类
 */
@Slf4j
public class FinalXirrTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 测试用户数据
     */
    @Test
    public void testUserData() {
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
            
            cashflows.add(new BigDecimal(valueStr));
            dates.add(dateStr);
        }
        
        // 使用不同的计算器计算XIRR
        BigDecimal fastXirrResult = FastXirrWithDatesCalculator.calculateXirr(cashflows, dates);
        BigDecimal excelXirrResult = ExcelXirrCalculator.calculateXirr(cashflows, dates);
        
        log.info("FastXirrWithDatesCalculator计算结果: {}", fastXirrResult);
        log.info("ExcelXirrCalculator计算结果: {}", excelXirrResult);
        log.info("Excel计算结果: 0.021028726253");
        
        // 验证结果是否接近Excel计算结果
        BigDecimal excelResult = new BigDecimal("0.021028726253");
        BigDecimal fastDiff = fastXirrResult.subtract(excelResult).abs();
        BigDecimal excelDiff = excelXirrResult.subtract(excelResult).abs();
        
        log.info("FastXirrWithDatesCalculator差异: {}", fastDiff);
        log.info("ExcelXirrCalculator差异: {}", excelDiff);
        
        // 差异应该小于0.001 (0.1%)
        boolean isFastClose = fastDiff.compareTo(new BigDecimal("0.001")) < 0;
        boolean isExcelClose = excelDiff.compareTo(new BigDecimal("0.001")) < 0;
        
        log.info("FastXirrWithDatesCalculator结果是否接近Excel计算结果: {}", isFastClose);
        log.info("ExcelXirrCalculator结果是否接近Excel计算结果: {}", isExcelClose);
    }
    
    /**
     * 测试从JSON计算XIRR
     */
    @Test
    public void testJsonXirr() {
        // 用户数据的前几个条目
        String jsonStr = "{\"0\":{\"date\":\"2024-12-31\",\"value\":\"11126.6974100000\"},\"1\":{\"date\":\"2025-01-31\",\"value\":\"1378.61557618\"},\"2\":{\"date\":\"2025-02-28\",\"value\":\"1358.54167768\"},\"3\":{\"date\":\"2025-03-31\",\"value\":\"1336.26759018\"},\"4\":{\"date\":\"2025-04-30\",\"value\":\"1314.89585230\"},\"5\":{\"date\":\"2025-05-31\",\"value\":\"4969.27102563\"}}";
        
        JSONObject cashflowJson = JSON.parseObject(jsonStr);
        
        // 使用不同的计算器计算XIRR
        BigDecimal fastXirrResult = FastXirrWithDatesCalculator.calculateXirrFromJson(cashflowJson);
        BigDecimal excelXirrResult = ExcelXirrCalculator.calculateXirrFromJson(cashflowJson);
        
        log.info("FastXirrWithDatesCalculator计算结果: {}", fastXirrResult);
        log.info("ExcelXirrCalculator计算结果: {}", excelXirrResult);
        log.info("Excel计算结果: 0.021028726253");
        
        // 验证结果是否接近Excel计算结果
        BigDecimal excelResult = new BigDecimal("0.021028726253");
        BigDecimal fastDiff = fastXirrResult.subtract(excelResult).abs();
        BigDecimal excelDiff = excelXirrResult.subtract(excelResult).abs();
        
        log.info("FastXirrWithDatesCalculator差异: {}", fastDiff);
        log.info("ExcelXirrCalculator差异: {}", excelDiff);
        
        // 差异应该小于0.001 (0.1%)
        boolean isFastClose = fastDiff.compareTo(new BigDecimal("0.001")) < 0;
        boolean isExcelClose = excelDiff.compareTo(new BigDecimal("0.001")) < 0;
        
        log.info("FastXirrWithDatesCalculator结果是否接近Excel计算结果: {}", isFastClose);
        log.info("ExcelXirrCalculator结果是否接近Excel计算结果: {}", isExcelClose);
    }
}
