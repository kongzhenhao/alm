package com.xl.alm.app.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 久期值集工具类
 *
 * @author AI Assistant
 */
@Slf4j
public class DurationValueSetUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 验证久期值集格式是否正确
     *
     * @param durationValSet 久期值集JSON字符串
     * @return 是否有效
     */
    public static boolean isValidDurationValSet(String durationValSet) {
        try {
            JSONObject jsonObject = JSON.parseObject(durationValSet);
            // 至少要有一个元素
            if (jsonObject.isEmpty()) {
                return false;
            }
            
            // 检查每个元素的格式
            for (String key : jsonObject.keySet()) {
                JSONObject item = jsonObject.getJSONObject(key);
                if (item == null || !item.containsKey("date") || !item.containsKey("val")) {
                    return false;
                }
                
                // 验证日期格式
                try {
                    LocalDate.parse(item.getString("date"), DATE_FORMATTER);
                } catch (Exception e) {
                    return false;
                }
                
                // 验证值是否为数字
                try {
                    new BigDecimal(item.getString("val"));
                } catch (Exception e) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.error("久期值集格式验证失败", e);
            return false;
        }
    }

    /**
     * 创建空的久期值集
     *
     * @return 空的久期值集JSON字符串
     */
    public static String createEmptyDurationValSet() {
        return "{}";
    }

    /**
     * 创建包含指定日期范围的久期值集
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param defaultValue 默认值
     * @return 久期值集JSON字符串
     */
    public static String createDurationValSet(LocalDate startDate, LocalDate endDate, BigDecimal defaultValue) {
        Map<String, Object> result = new HashMap<>();
        int index = 1;
        
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", currentDate.format(DATE_FORMATTER));
            item.put("val", defaultValue);
            
            result.put(String.valueOf(index), item);
            index++;
            currentDate = currentDate.plusDays(1);
        }
        
        return JSON.toJSONString(result);
    }

    /**
     * 从久期值集中获取指定日期的值
     *
     * @param durationValSet 久期值集JSON字符串
     * @param date 日期
     * @return 值，如果不存在则返回null
     */
    public static BigDecimal getValueByDate(String durationValSet, LocalDate date) {
        try {
            String dateStr = date.format(DATE_FORMATTER);
            JSONObject jsonObject = JSON.parseObject(durationValSet);
            
            for (String key : jsonObject.keySet()) {
                JSONObject item = jsonObject.getJSONObject(key);
                if (dateStr.equals(item.getString("date"))) {
                    return new BigDecimal(item.getString("val"));
                }
            }
            return null;
        } catch (Exception e) {
            log.error("从久期值集获取值失败", e);
            return null;
        }
    }

    /**
     * 更新久期值集中指定日期的值
     *
     * @param durationValSet 久期值集JSON字符串
     * @param date 日期
     * @param value 新值
     * @return 更新后的久期值集JSON字符串
     */
    public static String updateValueByDate(String durationValSet, LocalDate date, BigDecimal value) {
        try {
            String dateStr = date.format(DATE_FORMATTER);
            JSONObject jsonObject = JSON.parseObject(durationValSet);
            boolean found = false;
            
            for (String key : jsonObject.keySet()) {
                JSONObject item = jsonObject.getJSONObject(key);
                if (dateStr.equals(item.getString("date"))) {
                    item.put("val", value.toString());
                    found = true;
                    break;
                }
            }
            
            // 如果没找到，添加一个新的
            if (!found) {
                int maxIndex = 0;
                for (String key : jsonObject.keySet()) {
                    try {
                        int index = Integer.parseInt(key);
                        maxIndex = Math.max(maxIndex, index);
                    } catch (NumberFormatException e) {
                        // 忽略非数字键
                    }
                }
                
                JSONObject newItem = new JSONObject();
                newItem.put("date", dateStr);
                newItem.put("val", value.toString());
                jsonObject.put(String.valueOf(maxIndex + 1), newItem);
            }
            
            return jsonObject.toString();
        } catch (Exception e) {
            log.error("更新久期值集失败", e);
            return durationValSet;
        }
    }
}
