package com.xl.alm.job.cost.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户数据XIRR计算测试类
 */
@Slf4j
public class UserDataXirrTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 测试用户提供的数据
     */
    @Test
    public void testUserData() {
        // 用户数据的前几个和后几个条目
        String jsonStr = "{\"0\":{\"date\":\"2024-12-31\",\"value\":\"11126.6974100000\"},\"1\":{\"date\":\"2025-01-31\",\"value\":\"1378.61557618\"},\"2\":{\"date\":\"2025-02-28\",\"value\":\"1358.54167768\"}," +
                "\"3\":{\"date\":\"2025-03-31\",\"value\":\"1336.26759018\"},\"4\":{\"date\":\"2025-04-30\",\"value\":\"1314.89585230\"},\"5\":{\"date\":\"2025-05-31\",\"value\":\"4969.27102563\"}," +
                "\"1270\":{\"date\":\"2130-10-31\",\"value\":\"0\"},\"1271\":{\"date\":\"2130-11-30\",\"value\":\"0\"},\"1272\":{\"date\":\"2130-12-31\",\"value\":\"0\"}}";
        
        JSONObject cashflowJson = JSON.parseObject(jsonStr);
        
        // 计算XIRR
        BigDecimal xirr = FastXirrWithDatesCalculator.calculateXirrFromJson(cashflowJson);
        log.info("用户数据XIRR计算结果: {}", xirr);
        
        // Excel计算结果
        log.info("Excel计算结果: 0.021028726253");
    }

    /**
     * 测试用户数据的日期格式
     */
    @Test
    public void testDateFormat() {
        try {
            LocalDate date = LocalDate.parse("2024-12-31", DATE_FORMATTER);
            log.info("日期解析成功: {}", date);
        } catch (Exception e) {
            log.error("日期解析失败", e);
        }
    }

    /**
     * 手动计算XIRR，检查计算过程
     */
    @Test
    public void manualXirrCalculation() {
        // 加载用户数据
        String jsonStr = "{\"0\":{\"date\":\"2024-12-31\",\"value\":\"11126.6974100000\"},\"1\":{\"date\":\"2025-01-31\",\"value\":\"1378.61557618\"}}";
        JSONObject cashflowJson = JSON.parseObject(jsonStr);
        
        // 提取现金流和日期
        List<BigDecimal> cashflows = new ArrayList<>();
        List<LocalDate> dates = new ArrayList<>();
        
        for (Map.Entry<String, Object> entry : cashflowJson.entrySet()) {
            JSONObject flowEntry = (JSONObject) entry.getValue();
            String dateStr = flowEntry.getString("date");
            String valueStr = flowEntry.getString("value");
            
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
            BigDecimal value = new BigDecimal(valueStr);
            
            cashflows.add(value);
            dates.add(date);
        }
        
        // 打印提取的数据
        for (int i = 0; i < cashflows.size(); i++) {
            log.info("现金流 {}: 日期={}, 值={}", i, dates.get(i), cashflows.get(i));
        }
        
        // 计算相对天数
        LocalDate firstDate = dates.get(0);
        double[] dayArray = new double[dates.size()];
        for (int i = 0; i < dates.size(); i++) {
            dayArray[i] = ChronoUnit.DAYS.between(firstDate, dates.get(i));
            log.info("现金流 {}: 相对天数={}", i, dayArray[i]);
        }
    }
}
