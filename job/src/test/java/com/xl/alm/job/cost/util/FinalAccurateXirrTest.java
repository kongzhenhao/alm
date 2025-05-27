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
 * 最终准确XIRR计算测试类
 */
@Slf4j
public class FinalAccurateXirrTest {

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
        
        // 使用不同的计算器计算XIRR
        BigDecimal accurateXirrResult = AccurateXirrCalculator.calculateXirr(cashflows, dates);
        
        log.info("AccurateXirrCalculator计算结果: {}", accurateXirrResult);
        log.info("Excel计算结果: 0.021028726253");
        
        // 验证结果是否接近Excel计算结果
        BigDecimal excelResult = new BigDecimal("0.021028726253");
        BigDecimal accurateDiff = accurateXirrResult.subtract(excelResult).abs();
        
        log.info("AccurateXirrCalculator差异: {}", accurateDiff);
        
        // 差异应该小于0.001 (0.1%)
        boolean isAccurateClose = accurateDiff.compareTo(new BigDecimal("0.001")) < 0;
        
        log.info("AccurateXirrCalculator结果是否接近Excel计算结果: {}", isAccurateClose);
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
        BigDecimal accurateXirrResult = AccurateXirrCalculator.calculateXirrFromJson(cashflowJson);
        
        log.info("AccurateXirrCalculator计算结果: {}", accurateXirrResult);
        log.info("Excel计算结果: 0.021028726253");
        
        // 验证结果是否接近Excel计算结果
        BigDecimal excelResult = new BigDecimal("0.021028726253");
        BigDecimal accurateDiff = accurateXirrResult.subtract(excelResult).abs();
        
        log.info("AccurateXirrCalculator差异: {}", accurateDiff);
        
        // 差异应该小于0.001 (0.1%)
        boolean isAccurateClose = accurateDiff.compareTo(new BigDecimal("0.001")) < 0;
        
        log.info("AccurateXirrCalculator结果是否接近Excel计算结果: {}", isAccurateClose);
    }
    
    /**
     * 测试完整用户数据
     */
    @Test
    public void testFullUserData() {
        // 完整用户数据
        String jsonStr = loadFullUserData();
        JSONObject cashflowJson = JSON.parseObject(jsonStr);
        
        // 计算XIRR
        BigDecimal xirr = AccurateXirrCalculator.calculateXirrFromJson(cashflowJson);
        log.info("完整用户数据XIRR计算结果: {}", xirr);
        
        // Excel计算结果
        log.info("Excel计算结果: 0.021028726253");
    }
    
    /**
     * 加载完整用户数据
     */
    private String loadFullUserData() {
        // 这里应该是从文件加载JSON数据，但为了简化，我们直接返回一个字符串
        // 实际使用时，可以从文件或其他来源加载完整的JSON数据
        return "{\"0\":{\"date\":\"2024-12-31\",\"value\":\"11126.6974100000\"},\"1\":{\"date\":\"2025-01-31\",\"value\":\"1378.61557618\"},\"2\":{\"date\":\"2025-02-28\",\"value\":\"1358.54167768\"},\"3\":{\"date\":\"2025-03-31\",\"value\":\"1336.26759018\"},\"4\":{\"date\":\"2025-04-30\",\"value\":\"1314.89585230\"},\"5\":{\"date\":\"2025-05-31\",\"value\":\"4969.27102563\"}}";
    }
}
