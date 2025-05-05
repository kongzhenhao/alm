package com.xl.alm.job.dur.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 值集工具类
 *
 * @author AI Assistant
 */
@Slf4j
public class ValueSetUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int SCALE = 8; // 精度：小数位留8位，四舍五入

    /**
     * 解析值集为Map
     *
     * @param valSet 值集JSON字符串
     * @return Map，key为序号，value为值
     */
    public static Map<Integer, BigDecimal> parseValueMap(String valSet) {
        Map<Integer, BigDecimal> valueMap = new HashMap<>();
        if (valSet == null || valSet.trim().isEmpty()) {
            return valueMap;
        }

        try {
            JSONObject jsonObject = JSON.parseObject(valSet);
            for (String key : jsonObject.keySet()) {
                JSONObject item = jsonObject.getJSONObject(key);
                if (item != null) {
                    try {
                        int index = Integer.parseInt(key);
                        BigDecimal value = null;

                        // 尝试从"value"字段获取值
                        if (item.containsKey("value")) {
                            try {
                                value = new BigDecimal(item.getString("value"));
                            } catch (NumberFormatException e) {
                                log.warn("无法解析value字段的值: " + item.getString("value"), e);
                            }
                        }

                        if (value != null) {
                            valueMap.put(index, value);
                        }
                    } catch (NumberFormatException e) {
                        log.warn("无法解析索引: " + key, e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("解析值集失败", e);
        }
        return valueMap;
    }

    /**
     * 解析值集为日期Map
     *
     * @param valSet 值集JSON字符串
     * @return Map，key为序号，value为日期字符串
     */
    public static Map<Integer, String> parseDateMap(String valSet) {
        Map<Integer, String> dateMap = new HashMap<>();
        if (valSet == null || valSet.trim().isEmpty()) {
            return dateMap;
        }

        try {
            // 预处理JSON字符串，处理可能的转义字符问题
            String cleanedValSet = valSet.replace("\\\\", "\\");

            JSONObject jsonObject = JSON.parseObject(cleanedValSet);
            for (String key : jsonObject.keySet()) {
                JSONObject item = jsonObject.getJSONObject(key);
                if (item != null && item.containsKey("date")) {
                    try {
                        int index = Integer.parseInt(key);
                        String date = item.getString("date");
                        // 标准化日期格式，将斜杠替换为短杠
                        if (date != null && date.contains("/")) {
                            date = date.replace("/", "-");
                        }
                        dateMap.put(index, date);
                    } catch (NumberFormatException e) {
                        log.warn("无法解析索引: " + key, e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("解析日期值集失败", e);
        }
        return dateMap;
    }

    /**
     * 构建值集JSON字符串
     *
     * @param valueMap 值Map
     * @param dateMap 日期Map
     * @return 值集JSON字符串
     */
    public static String buildValueSet(Map<Integer, BigDecimal> valueMap, Map<Integer, String> dateMap) {
        Map<String, Object> result = new TreeMap<>();

        for (Map.Entry<Integer, BigDecimal> entry : valueMap.entrySet()) {
            int index = entry.getKey();
            BigDecimal value = entry.getValue();
            String date = dateMap.get(index);

            if (date != null && value != null) {
                Map<String, Object> item = new HashMap<>();

                // 标准化日期格式，确保使用短杠分隔符
                if (date.contains("/")) {
                    date = date.replace("/", "-");
                }
                item.put("date", date);

                // 使用value字段保存值，确保精度
                item.put("value", value.setScale(SCALE, RoundingMode.HALF_UP).doubleValue());

                result.put(String.valueOf(index), item);
            }
        }

        try {
            return JSON.toJSONString(result);
        } catch (Exception e) {
            log.error("构建值集JSON字符串失败", e);
            return "{}";
        }
    }

    /**
     * 合并两个值Map
     *
     * @param map1 值Map1
     * @param map2 值Map2
     * @return 合并后的值Map
     */
    public static Map<Integer, BigDecimal> mergeValueMaps(Map<Integer, BigDecimal> map1, Map<Integer, BigDecimal> map2) {
        Map<Integer, BigDecimal> result = new HashMap<>(map1);

        for (Map.Entry<Integer, BigDecimal> entry : map2.entrySet()) {
            int index = entry.getKey();
            BigDecimal value = entry.getValue();

            if (result.containsKey(index)) {
                result.put(index, result.get(index).add(value));
            } else {
                result.put(index, value);
            }
        }

        return result;
    }

    /**
     * 获取因子类型
     *
     * @param designType 设计类型
     * @param isShortTerm 是否中短期险种
     * @return 因子类型
     */
    public static String getFactorType(String designType, String isShortTerm) {
        // design_type为"传统险","分红险","万能险"同时is_short_term为Y时factor_type为01
        // design_type为"万能险"同时is_short_term为N时factor_type为01
        // 其他情况factor_type为02
        if (("传统险".equals(designType) || "分红险".equals(designType) || "万能险".equals(designType)) && "Y".equals(isShortTerm)) {
            return "01";
        } else if ("万能险".equals(designType) && "N".equals(isShortTerm)) {
            return "01";
        } else {
            return "02";
        }
    }
}
