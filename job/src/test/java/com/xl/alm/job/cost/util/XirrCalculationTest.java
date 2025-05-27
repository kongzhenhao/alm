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
 * XIRR计算测试类
 */
@Slf4j
public class XirrCalculationTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 测试简单的XIRR计算
     */
    @Test
    public void testSimpleXirr() {
        // 创建一个简单的现金流列表和日期列表
        List<BigDecimal> cashflows = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        // 初始投资 -1000
        cashflows.add(new BigDecimal("-1000"));
        dates.add("2023-01-01");

        // 一年后收回 1100
        cashflows.add(new BigDecimal("1100"));
        dates.add("2024-01-01");

        // 计算XIRR
        BigDecimal xirr = FastXirrWithDatesCalculator.calculateXirr(cashflows, dates);
        log.info("简单XIRR计算结果: {}", xirr);
        
        // 预期结果应该接近0.1 (10%)
        log.info("预期结果: 0.1");
    }

    /**
     * 测试从JSON计算XIRR
     */
    @Test
    public void testXirrFromJson() {
        // 创建一个简单的JSON对象
        JSONObject cashflowJson = new JSONObject();
        
        // 初始投资 -1000
        JSONObject initialEntry = new JSONObject();
        initialEntry.put("date", "2023-01-01");
        initialEntry.put("value", "-1000");
        cashflowJson.put("0", initialEntry);
        
        // 一年后收回 1100
        JSONObject secondEntry = new JSONObject();
        secondEntry.put("date", "2024-01-01");
        secondEntry.put("value", "1100");
        cashflowJson.put("1", secondEntry);
        
        // 计算XIRR
        BigDecimal xirr = FastXirrWithDatesCalculator.calculateXirrFromJson(cashflowJson);
        log.info("从JSON计算的XIRR结果: {}", xirr);
        
        // 预期结果应该接近0.1 (10%)
        log.info("预期结果: 0.1");
    }

    /**
     * 测试Excel示例数据
     */
    @Test
    public void testExcelExample() {
        // Excel示例数据
        String jsonStr = "{\"0\":{\"date\":\"2023-01-01\",\"value\":\"-1000\"},\"1\":{\"date\":\"2023-04-01\",\"value\":\"200\"},\"2\":{\"date\":\"2023-10-01\",\"value\":\"800\"},\"3\":{\"date\":\"2024-02-01\",\"value\":\"200\"}}";
        JSONObject cashflowJson = JSON.parseObject(jsonStr);
        
        // 计算XIRR
        BigDecimal xirr = FastXirrWithDatesCalculator.calculateXirrFromJson(cashflowJson);
        log.info("Excel示例XIRR计算结果: {}", xirr);
        
        // Excel XIRR函数计算结果约为0.1806 (18.06%)
        log.info("Excel计算结果: 0.1806");
    }
}
