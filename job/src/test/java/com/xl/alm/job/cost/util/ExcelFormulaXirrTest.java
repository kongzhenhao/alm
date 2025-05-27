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
 * Excel公式XIRR计算测试类
 * 测试IFERROR(XIRR($I45:$AWG45, $I$10:$AWG$10, 0), 0)公式的实现
 */
@Slf4j
public class ExcelFormulaXirrTest {

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
            
            log.info("现金流 {}: 日期={}, 值={}", entry.getKey(), dateStr, valueStr);
        }
        
        // 使用AccurateXirrCalculator计算XIRR
        BigDecimal xirrResult = AccurateXirrCalculator.calculateXirr(cashflows, dates);
        
        log.info("AccurateXirrCalculator计算结果: {}", xirrResult);
        log.info("Excel计算结果: 0.021028726253");
        
        // 验证结果是否接近Excel计算结果
        BigDecimal excelResult = new BigDecimal("0.021028726253");
        BigDecimal diff = xirrResult.subtract(excelResult).abs();
        
        log.info("差异: {}", diff);
        
        // 差异应该小于0.001 (0.1%)
        boolean isClose = diff.compareTo(new BigDecimal("0.001")) < 0;
        
        log.info("结果是否接近Excel计算结果: {}", isClose);
    }
    
    /**
     * 测试错误处理
     */
    @Test
    public void testErrorHandling() {
        // 测试空数据
        List<BigDecimal> emptyCashflows = new ArrayList<>();
        List<String> emptyDates = new ArrayList<>();
        
        BigDecimal emptyResult = AccurateXirrCalculator.calculateXirr(emptyCashflows, emptyDates);
        log.info("空数据XIRR计算结果: {}", emptyResult);
        
        // 测试只有正值的数据
        List<BigDecimal> positiveCashflows = new ArrayList<>();
        List<String> positiveDates = new ArrayList<>();
        
        positiveCashflows.add(new BigDecimal("100"));
        positiveDates.add("2023-01-01");
        
        positiveCashflows.add(new BigDecimal("200"));
        positiveDates.add("2023-02-01");
        
        BigDecimal positiveResult = AccurateXirrCalculator.calculateXirr(positiveCashflows, positiveDates);
        log.info("只有正值的XIRR计算结果: {}", positiveResult);
        
        // 测试只有负值的数据
        List<BigDecimal> negativeCashflows = new ArrayList<>();
        List<String> negativeDates = new ArrayList<>();
        
        negativeCashflows.add(new BigDecimal("-100"));
        negativeDates.add("2023-01-01");
        
        negativeCashflows.add(new BigDecimal("-200"));
        negativeDates.add("2023-02-01");
        
        BigDecimal negativeResult = AccurateXirrCalculator.calculateXirr(negativeCashflows, negativeDates);
        log.info("只有负值的XIRR计算结果: {}", negativeResult);
    }
    
    /**
     * 测试Excel示例
     */
    @Test
    public void testExcelExample() {
        // Excel示例数据
        List<BigDecimal> cashflows = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        
        // 初始投资 -10000
        cashflows.add(new BigDecimal("-10000"));
        dates.add("2008-01-01");
        
        // 后续收益
        cashflows.add(new BigDecimal("2750"));
        dates.add("2008-03-01");
        
        cashflows.add(new BigDecimal("4250"));
        dates.add("2008-10-30");
        
        cashflows.add(new BigDecimal("3250"));
        dates.add("2009-02-15");
        
        cashflows.add(new BigDecimal("2750"));
        dates.add("2009-04-01");
        
        // 计算XIRR
        BigDecimal xirr = AccurateXirrCalculator.calculateXirr(cashflows, dates);
        log.info("Excel示例XIRR计算结果: {}", xirr);
        
        // Excel XIRR函数计算结果约为0.373 (37.3%)
        log.info("Excel计算结果: 0.373");
    }
}
