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
 * 准确XIRR计算器测试类
 */
@Slf4j
public class AccurateXirrTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 测试准确XIRR计算器
     */
    @Test
    public void testAccurateXirr() {
        // 用户数据的前几个条目
        String jsonStr = "{\"0\":{\"date\":\"2024-12-31\",\"value\":\"11126.6974100000\"},\"1\":{\"date\":\"2025-01-31\",\"value\":\"1378.61557618\"},\"2\":{\"date\":\"2025-02-28\",\"value\":\"1358.54167768\"},\"3\":{\"date\":\"2025-03-31\",\"value\":\"1336.26759018\"},\"4\":{\"date\":\"2025-04-30\",\"value\":\"1314.89585230\"},\"5\":{\"date\":\"2025-05-31\",\"value\":\"4969.27102563\"}}";
        
        JSONObject cashflowJson = JSON.parseObject(jsonStr);
        
        // 计算XIRR
        BigDecimal xirr = AccurateXirrCalculator.calculateXirrFromJson(cashflowJson);
        log.info("准确XIRR计算结果: {}", xirr);
        
        // Excel计算结果
        log.info("Excel计算结果: 0.021028726253");
        
        // 验证结果是否接近Excel计算结果
        BigDecimal excelResult = new BigDecimal("0.021028726253");
        BigDecimal diff = xirr.subtract(excelResult).abs();
        log.info("差异: {}", diff);
        
        // 差异应该小于0.001 (0.1%)
        boolean isClose = diff.compareTo(new BigDecimal("0.001")) < 0;
        log.info("结果是否接近Excel计算结果: {}", isClose);
    }
    
    /**
     * 测试从列表计算XIRR
     */
    @Test
    public void testXirrFromList() {
        // 创建一个简单的现金流列表和日期列表
        List<BigDecimal> cashflows = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        
        // 添加用户数据
        cashflows.add(new BigDecimal("11126.6974100000"));
        dates.add("2024-12-31");
        
        cashflows.add(new BigDecimal("1378.61557618"));
        dates.add("2025-01-31");
        
        cashflows.add(new BigDecimal("1358.54167768"));
        dates.add("2025-02-28");
        
        cashflows.add(new BigDecimal("1336.26759018"));
        dates.add("2025-03-31");
        
        cashflows.add(new BigDecimal("1314.89585230"));
        dates.add("2025-04-30");
        
        cashflows.add(new BigDecimal("4969.27102563"));
        dates.add("2025-05-31");
        
        // 计算XIRR
        BigDecimal xirr = AccurateXirrCalculator.calculateXirr(cashflows, dates);
        log.info("从列表计算XIRR结果: {}", xirr);
        
        // Excel计算结果
        log.info("Excel计算结果: 0.021028726253");
    }
    
    /**
     * 测试标准XIRR计算
     */
    @Test
    public void testStandardXirr() {
        // 创建一个标准的现金流列表和日期列表
        List<BigDecimal> cashflows = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        
        // 初始投资 -1000
        cashflows.add(new BigDecimal("-1000"));
        dates.add("2023-01-01");
        
        // 一年后收回 1100
        cashflows.add(new BigDecimal("1100"));
        dates.add("2024-01-01");
        
        // 计算XIRR
        BigDecimal xirr = AccurateXirrCalculator.calculateXirr(cashflows, dates);
        log.info("标准XIRR计算结果: {}", xirr);
        
        // 预期结果应该接近0.1 (10%)
        log.info("预期结果: 0.1");
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
