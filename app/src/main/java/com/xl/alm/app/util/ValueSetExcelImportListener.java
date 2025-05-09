package com.xl.alm.app.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 值集Excel导入监听器
 * 用于处理包含值集的Excel导入，将多列数据转换为JSON值集
 *
 * @author AI Assistant
 */
@Slf4j
public class ValueSetExcelImportListener<T> extends AnalysisEventListener<Map<Integer, Object>> {

    /**
     * 表头行数
     */
    private static final int HEAD_ROW_COUNT = 2;

    /**
     * 值集开始列索引
     */
    private int valueSetStartIndex;

    /**
     * 值集字段名
     */
    private final String valueSetFieldName;

    /**
     * 目标类型
     */
    private final Class<T> targetClass;

    /**
     * 表头日期映射，key为列索引，value为日期
     */
    private final Map<Integer, String> headerDateMap = new HashMap<>();

    /**
     * 结果列表
     * -- GETTER --
     *  获取结果列表

     */
    @Getter
    private final List<T> resultList = new ArrayList<>();

    /**
     * 普通字段映射，key为列索引，value为字段名
     */
    private final Map<Integer, String> normalFieldMap = new HashMap<>();

    /**
     * 构造函数
     *
     * @param targetClass      目标类型
     * @param valueSetFieldName 值集字段名
     */
    public ValueSetExcelImportListener(Class<T> targetClass, String valueSetFieldName) {
        this.targetClass = targetClass;
        this.valueSetFieldName = valueSetFieldName;
        initValueSetStartIndex();
        initNormalFieldMap();
    }

    /**
     * 初始化值集开始列索引
     */
    private void initValueSetStartIndex() {
        String className = targetClass.getSimpleName();
        log.info("初始化值集开始列索引，目标类型: {}", className);

        // 根据不同的DTO类型设置不同的值集开始列索引
        switch (className) {
            case "LiabilityCashFlowDTO":
                // 负债现金流的值集开始列索引
                valueSetStartIndex = 12;
                log.info("负债现金流的值集开始列索引设置为: {}", valueSetStartIndex);
                break;
            case "DiscountFactorDTO":
                // 折现因子的值集开始列索引
                valueSetStartIndex = 5;
                log.info("折现因子的值集开始列索引设置为: {}", valueSetStartIndex);
                break;
            case "DiscountCurveDTO":
                // 折现曲线的值集开始列索引
                valueSetStartIndex = 5;
                log.info("折现曲线的值集开始列索引设置为: {}", valueSetStartIndex);
                break;
            default:
                // 其他类型的默认值集开始列索引
                valueSetStartIndex = 12;
                log.warn("未找到目标类型[{}]的值集开始列索引配置，使用默认值: {}", className, valueSetStartIndex);
                break;
        }
    }

    /**
     * 初始化普通字段映射
     */
    private void initNormalFieldMap() {
        // 根据目标类型初始化不同的字段映射
        String className = targetClass.getSimpleName();
        log.info("初始化字段映射，目标类型: {}", className);

        // 通用字段映射
        normalFieldMap.put(0, "id");
        normalFieldMap.put(1, "accountPeriod");

        // 根据不同的DTO类型设置不同的字段映射
        switch (className) {
            case "LiabilityCashFlowDTO":
                // 负债现金流的字段映射
                normalFieldMap.put(2, "cashFlowType");
                normalFieldMap.put(3, "bpType");
                normalFieldMap.put(4, "durationType");
                normalFieldMap.put(5, "designType");
                normalFieldMap.put(6, "isShortTerm");
                normalFieldMap.put(7, "actuarialCode");
                normalFieldMap.put(8, "businessCode");
                normalFieldMap.put(9, "productName");
                normalFieldMap.put(10, "insuranceMainType");
                normalFieldMap.put(11, "insuranceSubType");
                log.info("初始化负债现金流字段映射完成");
                break;
            case "DiscountFactorDTO":
                // 折现因子的字段映射
                normalFieldMap.put(2, "factorType");
                normalFieldMap.put(3, "bpType");
                normalFieldMap.put(4, "durationType");
                log.info("初始化折现因子字段映射完成");
                break;
            case "DiscountCurveDTO":
                // 折现曲线的字段映射
                normalFieldMap.put(2, "curveType");
                normalFieldMap.put(3, "bpType");
                normalFieldMap.put(4, "durationType");
                log.info("初始化折现曲线字段映射完成");
                break;
            default:
                // 其他类型的默认字段映射
                log.warn("未找到目标类型[{}]的字段映射配置，使用默认映射", className);
                normalFieldMap.put(2, "cashFlowType");
                normalFieldMap.put(3, "bpType");
                normalFieldMap.put(4, "durationType");
                normalFieldMap.put(5, "designType");
                normalFieldMap.put(6, "isShortTerm");
                normalFieldMap.put(7, "actuarialCode");
                normalFieldMap.put(8, "businessCode");
                normalFieldMap.put(9, "productName");
                normalFieldMap.put(10, "insuranceMainType");
                normalFieldMap.put(11, "insuranceSubType");
                normalFieldMap.put(12, "factorType");
                normalFieldMap.put(13, "curveType");
                break;
        }
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        // 处理表头信息
        ReadRowHolder readRowHolder = context.readRowHolder();
        int rowIndex = readRowHolder.getRowIndex();

        // 如果是第二行表头（日期行），处理日期信息
        if (rowIndex == 1) {
            for (Map.Entry<Integer, String> entry : headMap.entrySet()) {
                int columnIndex = entry.getKey();
                String dateStr = entry.getValue();

                // 只处理值集部分的列（从值集开始列索引开始）
                if (columnIndex >= valueSetStartIndex && dateStr != null) {
                    // 计算值集索引
                    int valueSetIndex = columnIndex - valueSetStartIndex;
                    // 存储值集索引和日期的映射关系
                    headerDateMap.put(valueSetIndex, dateStr);
                    log.debug("读取到日期信息: 索引={}, 日期={}", valueSetIndex, dateStr);
                }
            }

            // 日志输出所有读取到的日期信息，用于调试
            log.info("共读取到{}个日期信息", headerDateMap.size());
            if (headerDateMap.isEmpty()) {
                log.warn("未读取到任何日期信息，请检查Excel模板格式是否正确");
            }
        }

        // 打印所有表头信息，用于调试
        if (log.isDebugEnabled()) {
            log.debug("表头行 {}: ", rowIndex);
            for (Map.Entry<Integer, String> entry : headMap.entrySet()) {
                log.debug("列 {}: {}", entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public void invoke(Map<Integer, Object> data, AnalysisContext context) {
        ReadRowHolder readRowHolder = context.readRowHolder();
        /**
         * 当前处理的行号
         */
        int currentRowIndex = readRowHolder.getRowIndex();

        // 跳过表头行
        if (currentRowIndex < HEAD_ROW_COUNT) {
            return;
        }

        // 处理数据行
        processDataRow(data);
    }

    /**
     * 处理数据行
     *
     * @param data    数据行
     */
    private void processDataRow(Map<Integer, Object> data) {
        try {
            // 创建目标对象实例
            T targetObject = targetClass.newInstance();

            // 处理普通字段
            for (Map.Entry<Integer, String> entry : normalFieldMap.entrySet()) {
                int columnIndex = entry.getKey();
                String fieldName = entry.getValue();
                Object value = data.get(columnIndex);

                if (value != null) {
                    try {
                        Field field = targetClass.getDeclaredField(fieldName);
                        field.setAccessible(true);

                        // 根据字段类型进行转换
                        if (field.getType() == String.class) {
                            field.set(targetObject, value.toString());
                        } else if (field.getType() == Integer.class || field.getType() == int.class) {
                            field.set(targetObject, Integer.parseInt(value.toString()));
                        } else if (field.getType() == Long.class || field.getType() == long.class) {
                            field.set(targetObject, Long.parseLong(value.toString()));
                        } else if (field.getType() == BigDecimal.class) {
                            field.set(targetObject, new BigDecimal(value.toString()));
                        } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
                            field.set(targetObject, Boolean.parseBoolean(value.toString()));
                        }
                        // 可以根据需要添加更多类型的处理
                    } catch (Exception e) {
                        log.warn("设置字段{}失败: {}", fieldName, e.getMessage());
                    }
                }
            }

            // 处理值集字段
            JSONObject valueSetJson = new JSONObject();

            // 记录已处理的列数，用于调试
            int processedColumns = 0;

            // 处理从0到1272的所有索引
            for (int i = 0; i < 1273; i++) {
                int columnIndex = valueSetStartIndex + i;
                Object value = data.get(columnIndex);

                // 创建值集项
                JSONObject valueItem = new JSONObject();

                // 获取日期信息
                String date = headerDateMap.get(i);
                if (date == null || date.trim().isEmpty()) {
                    // 如果没有日期信息，使用默认日期
                    date = "2000-01-01";
                    if (i < 10 || i % 100 == 0) { // 只记录部分索引的警告，避免日志过多
                        log.warn("未找到索引{}对应的日期信息，将使用默认日期", i);
                    }
                }

                // 调试日志，输出日期信息
                if (i < 5 || i > 1268) {
                    log.debug("索引 {} 的日期信息: {}", i, date);
                }
                valueItem.put("date", date);

                // 设置值
                if (value != null) {
                    processedColumns++;
                    try {
                        if (value instanceof Number) {
                            BigDecimal bdValue = new BigDecimal(value.toString());
                            valueItem.put("value", bdValue);
                        } else {
                            String strValue = value.toString().trim();
                            if (!strValue.isEmpty()) {
                                BigDecimal bdValue = new BigDecimal(strValue);
                                valueItem.put("value", bdValue);
                            } else {
                                // 如果值为空字符串，设置为0
                                valueItem.put("value", BigDecimal.ZERO);
                            }
                        }
                    } catch (NumberFormatException e) {
                        log.warn("无法解析值: {} - {}", value, e.getMessage());
                        // 如果解析失败，设置为0
                        valueItem.put("value", BigDecimal.ZERO);
                    }
                } else {
                    // 如果值为null，设置为0
                    valueItem.put("value", BigDecimal.ZERO);
                }

                // 添加到值集JSON，使用原始索引作为key
                valueSetJson.put(String.valueOf(i), valueItem);
            }

            log.info("共处理了{}个非空值集列，生成了{}个值集项", processedColumns, valueSetJson.size());

            // 将值集JSON设置到目标对象
            if (!valueSetJson.isEmpty()) {
                try {
                    Field field = targetClass.getDeclaredField(valueSetFieldName);
                    field.setAccessible(true);
                    field.set(targetObject, valueSetJson.toString());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    log.error("设置值集字段{}失败: {}", valueSetFieldName, e.getMessage());
                }
            }

            // 添加到结果列表
            resultList.add(targetObject);
        } catch (Exception e) {
            log.error("处理数据行失败: {}", e.getMessage());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("所有数据解析完成，共解析{}条数据", resultList.size());

        // 输出日期映射信息，用于调试
        if (log.isDebugEnabled()) {
            log.debug("日期映射信息：");
            for (Map.Entry<Integer, String> entry : headerDateMap.entrySet()) {
                log.debug("索引: {}, 日期: {}", entry.getKey(), entry.getValue());
            }
        }

        // 检查日期映射信息是否完整
        checkDateMapping();
    }

    /**
     * 检查日期映射信息是否完整
     */
    private void checkDateMapping() {
        // 检查是否有日期信息
        if (headerDateMap.isEmpty()) {
            log.warn("未读取到任何日期信息，请检查Excel模板格式是否正确");
            return;
        }

        // 检查日期信息是否完整
        int missingCount = 0;
        for (int i = 0; i < 1273; i++) {
            if (!headerDateMap.containsKey(i)) {
                missingCount++;
                if (missingCount <= 10) { // 只记录前10个缺失的索引
                    log.warn("缺失索引 {} 的日期信息", i);
                }
            }
        }

        if (missingCount > 0) {
            log.warn("共有 {} 个索引缺失日期信息", missingCount);
        } else {
            log.info("日期映射信息完整，共有 {} 个索引", headerDateMap.size());
        }
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        // 处理额外的单元格信息，如合并单元格等
    }
}
