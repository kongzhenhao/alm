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
 * 修复后的XIRR计算测试类
 */
@Slf4j
public class FixedXirrTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 测试Excel示例
     */
    @Test
    public void testExcelExample() {
        // Excel示例数据
        List<BigDecimal> cashflows = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        
        // 初始投资 -1000
        cashflows.add(new BigDecimal("-1000"));
        dates.add("2023-01-01");
        
        // 后续收益
        cashflows.add(new BigDecimal("200"));
        dates.add("2023-04-01");
        
        cashflows.add(new BigDecimal("800"));
        dates.add("2023-10-01");
        
        cashflows.add(new BigDecimal("200"));
        dates.add("2024-02-01");
        
        // 计算XIRR
        BigDecimal xirr = FastXirrWithDatesCalculator.calculateXirr(cashflows, dates);
        log.info("Excel示例XIRR计算结果: {}", xirr);
        
        // Excel XIRR函数计算结果约为0.1806 (18.06%)
        log.info("Excel计算结果: 0.1806");
    }
    
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
        
        // 计算XIRR
        BigDecimal xirr = FastXirrWithDatesCalculator.calculateXirr(cashflows, dates);
        log.info("用户数据XIRR计算结果: {}", xirr);
        
        // Excel计算结果
        log.info("Excel计算结果: 0.021028726253");
    }
    
    /**
     * 测试用户完整数据
     */
    @Test
    public void testUserFullData() {
        // 用户完整数据
        String jsonStr = loadUserFullData();
        JSONObject cashflowJson = JSON.parseObject(jsonStr);
        
        // 计算XIRR
        BigDecimal xirr = FastXirrWithDatesCalculator.calculateXirrFromJson(cashflowJson);
        log.info("用户完整数据XIRR计算结果: {}", xirr);
        
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
     * 加载用户完整数据
     */
    private String loadUserFullData() {
        // 这里应该是从文件加载JSON数据，但为了简化，我们直接返回一个字符串
        // 实际使用时，可以从文件或其他来源加载完整的JSON数据
        return "{\"0\":{\"date\":\"2024-12-31\",\"value\":\"11126.6974100000\"},\"1\":{\"date\":\"2025-01-31\",\"value\":\"1378.61557618\"},\"2\":{\"date\":\"2025-02-28\",\"value\":\"1358.54167768\"},\"3\":{\"date\":\"2025-03-31\",\"value\":\"1336.26759018\"},\"4\":{\"date\":\"2025-04-30\",\"value\":\"1314.89585230\"},\"5\":{\"date\":\"2025-05-31\",\"value\":\"4969.27102563\"}}";
    }
}
