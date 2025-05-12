package com.xl.alm.app.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.entity.SysDictData;
import com.jd.lightning.common.exception.ServiceException;
import com.jd.lightning.common.utils.StringUtils;
import com.jd.lightning.common.utils.spring.SpringUtils;
import com.jd.lightning.system.service.ISysDictTypeService;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 值集Excel导出工具类
 * 用于处理包含JSON值集字段的Excel导出，将JSON值集转换为多列数据
 */
public class ValueSetExcelExporter {
    private static final Logger log = LoggerFactory.getLogger(ValueSetExcelExporter.class);

    /**
     * 导出带有多个值集的Excel
     *
     * @param list           数据列表
     * @param sheetName      工作表名称
     * @param response       响应对象
     * @param valueSetFields 值集字段名称数组
     * @param <T>            数据类型
     */
    public static <T> void exportExcel(List<T> list, String sheetName, HttpServletResponse response, String... valueSetFields) {
        if (valueSetFields == null || valueSetFields.length == 0) {
            throw new ServiceException("至少需要指定一个值集字段");
        }
//
//        // 如果只有一个值集字段，使用原来的方法
//        if (valueSetFields.length == 1) {
//            exportExcel(list, sheetName, response, valueSetFields[0]);
//            return;
//        }

        try {
            if (list == null || list.isEmpty()) {
                throw new ServiceException("没有数据可导出");
            }

            // 获取第一个对象的类型
            Class<?> clazz = list.get(0).getClass();

            // 检查所有值集字段是否存在
            Map<String, Field> valueSetFieldMap = new HashMap<>();
            for (String fieldName : valueSetFields) {
                try {
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    valueSetFieldMap.put(fieldName, field);
                } catch (NoSuchFieldException e) {
                    throw new ServiceException("值集字段 " + fieldName + " 不存在");
                }
            }

            // 获取所有字段（排除值集字段、isDel字段、serialVersionUID和id字段，以及静态字段）
            List<Field> normalFields = new ArrayList<>();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                int modifiers = field.getModifiers();

                // 排除值集字段、isDel字段、serialVersionUID和静态字段，但保留id字段
                if (!valueSetFieldMap.containsKey(fieldName) &&
                    !"isDel".equals(fieldName) &&
                    !"serialVersionUID".equals(fieldName) &&
                    !java.lang.reflect.Modifier.isStatic(modifiers)) {
                    normalFields.add(field);
                }
            }

            // 设置响应头
            String fileName = URLEncoder.encode(sheetName, "UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 创建表头
            List<List<String>> headList = new ArrayList<>();

            // 添加普通字段表头（占两行）
            for (Field field : normalFields) {
                List<String> head = new ArrayList<>();
                String fieldName = getFieldDisplayName(field);
                head.add(fieldName);
                head.add(fieldName);
                headList.add(head);
            }

            // 为每个值集字段创建日期映射
            Map<String, Map<Integer, String>> fieldDateMaps = new HashMap<>();
            for (String fieldName : valueSetFields) {
                Map<Integer, String> dateMap = new TreeMap<>();
                Field field = valueSetFieldMap.get(fieldName);

                // 解析值集数据，获取所有序号和日期
                for (T item : list) {
                    String valueSetJson = (String) field.get(item);
                    if (valueSetJson != null && !valueSetJson.isEmpty()) {
                        try {
                            JSONObject jsonObject = JSON.parseObject(valueSetJson);
                            for (String key : jsonObject.keySet()) {
                                try {
                                    int index = Integer.parseInt(key);
                                    JSONObject itemData = jsonObject.getJSONObject(key);
                                    if (itemData != null && itemData.containsKey("date")) {
                                        String date = itemData.getString("date");
                                        dateMap.put(index, date);
                                    }
                                } catch (NumberFormatException e) {
                                    log.warn("无法解析索引: " + key, e);
                                }
                            }
                        } catch (Exception e) {
                            log.error("解析值集JSON失败", e);
                        }
                    }
                }

                fieldDateMaps.put(fieldName, dateMap);
            }

            // 为每个值集添加表头
            for (String fieldName : valueSetFields) {
                Field field = valueSetFieldMap.get(fieldName);
                String fieldDisplayName = getFieldDisplayName(field);
                Map<Integer, String> dateMap = fieldDateMaps.get(fieldName);

                for (Map.Entry<Integer, String> entry : dateMap.entrySet()) {
                    List<String> head = new ArrayList<>();
                    head.add(fieldDisplayName + "-" + entry.getKey()); // 第一行是字段名-序号
                    head.add(entry.getValue()); // 第二行是日期
                    headList.add(head);
                }
            }

            // 准备数据
            List<List<Object>> dataList = new ArrayList<>();
            for (T item : list) {
                List<Object> rowData = new ArrayList<>();

                // 添加普通字段数据
                for (Field field : normalFields) {
                    Object value = field.get(item);
                    // 处理字典类型
                    Excel excel = field.getAnnotation(Excel.class);
                    if (excel != null && StringUtils.isNotEmpty(excel.dictType()) && value != null) {
                        value = convertDictValue(excel.dictType(), String.valueOf(value));
                    } else if (excel != null && StringUtils.isNotEmpty(excel.readConverterExp()) && value != null) {
                        value = convertReadConverterExp(excel.readConverterExp(), String.valueOf(value));
                    }
                    rowData.add(value);
                }

                // 为每个值集添加数据
                for (String fieldName : valueSetFields) {
                    Field field = valueSetFieldMap.get(fieldName);
                    String valueSetJson = (String) field.get(item);
                    Map<Integer, BigDecimal> valueMap = parseValueSetJson(valueSetJson);
                    Map<Integer, String> dateMap = fieldDateMaps.get(fieldName);

                    // 按序号顺序添加值
                    for (Integer index : dateMap.keySet()) {
                        BigDecimal value = valueMap.getOrDefault(index, null);
                        rowData.add(value);
                    }
                }

                dataList.add(rowData);
            }

            // 设置样式
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            WriteFont headWriteFont = new WriteFont();
            headWriteFont.setFontHeightInPoints((short) 12);
            headWriteFont.setBold(true);
            headWriteCellStyle.setWriteFont(headWriteFont);
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
            headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
            headWriteCellStyle.setBorderRight(BorderStyle.THIN);
            headWriteCellStyle.setBorderTop(BorderStyle.THIN);

            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
            contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
            contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
            contentWriteCellStyle.setBorderTop(BorderStyle.THIN);

            HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

            // 导出Excel
            EasyExcel.write(response.getOutputStream())
                    .head(headList)
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet(sheetName)
                    .doWrite(dataList);

        } catch (Exception e) {
            log.error("导出Excel异常", e);
            throw new ServiceException("导出Excel失败: " + e.getMessage());
        }
    }

    /**
     * 获取字段显示名称
     *
     * @param field 字段
     * @return 显示名称
     */
    private static String getFieldDisplayName(Field field) {
        // 从 Excel 注解中获取显示名称
        try {
            Excel excel = field.getAnnotation(Excel.class);
            if (excel != null && excel.name() != null && !excel.name().isEmpty()) {
                return excel.name();
            }
        } catch (Exception e) {
            log.warn("获取字段注解失败: " + field.getName(), e);
        }

        // 如果没有注解或获取失败，返回字段名
        return field.getName();
    }

    /**
     * 解析值集JSON字符串，提取值
     *
     * @param valueSetJson 值集JSON字符串
     * @return 序号-值映射
     */
    private static Map<Integer, BigDecimal> parseValueSetJson(String valueSetJson) {
        Map<Integer, BigDecimal> valueMap = new HashMap<>();
        if (valueSetJson == null || valueSetJson.isEmpty()) {
            return valueMap;
        }

        try {
            JSONObject jsonObject = JSON.parseObject(valueSetJson);
            for (String key : jsonObject.keySet()) {
                try {
                    int index = Integer.parseInt(key);
                    JSONObject itemData = jsonObject.getJSONObject(key);
                    if (itemData != null) {
                        // 支持两种可能的值字段名：val 和 value
                        BigDecimal value = null;
                        if (itemData.containsKey("value")) {
                            value = itemData.getBigDecimal("value");
                        }

                        if (value != null) {
                            valueMap.put(index, value);
                        }
                    }
                } catch (NumberFormatException e) {
                    log.warn("无法解析索引: " + key, e);
                }
            }
        } catch (Exception e) {
            log.error("解析值集JSON失败", e);
        }

        return valueMap;
    }

    /**
     * 导出带有值集的模板Excel
     *
     * @param clazz         DTO类型
     * @param sheetName     工作表名称
     * @param response      响应对象
     * @param valueSetField 值集字段名称
     * @param <T>           数据类型
     */
    public static <T> void exportTemplateExcel(Class<T> clazz, String sheetName, HttpServletResponse response, String valueSetField) {
        try {
            try {
                Field valueSetFieldObj = clazz.getDeclaredField(valueSetField);
                valueSetFieldObj.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new ServiceException("值集字段 " + valueSetField + " 不存在");
            }

            // 获取所有字段（排除值集字段、isDel字段、serialVersionUID和id字段，以及静态字段）
            List<Field> normalFields = new ArrayList<>();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                int modifiers = field.getModifiers();

                // 排除值集字段、isDel字段、serialVersionUID和静态字段，但保留id字段
                if (!fieldName.equals(valueSetField) &&
                    !"isDel".equals(fieldName) &&
                    !"serialVersionUID".equals(fieldName) &&
                    !java.lang.reflect.Modifier.isStatic(modifiers)) {
                    normalFields.add(field);
                }
            }

            // 设置响应头
            String fileName = URLEncoder.encode(sheetName, "UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 创建表头
            List<List<String>> headList = new ArrayList<>();

            // 添加普通字段表头（占两行）
            for (Field field : normalFields) {
                List<String> head = new ArrayList<>();
                String fieldName = getFieldDisplayName(field);
                head.add(fieldName);
                head.add(fieldName);
                headList.add(head);
            }

            // 生成日期映射，从0开始，共1273项
            Map<Integer, String> dateMap = new TreeMap<>();

            // 获取上个月最后一天的日期作为起始日期
            LocalDate now = LocalDate.now();
            LocalDate lastDayOfPreviousMonth = now.withDayOfMonth(1).minusDays(1);

            log.info("模板导出起始日期: {}", lastDayOfPreviousMonth.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            // 添加值集序号和日期表头，从0开始，共1273项
            for (int i = 0; i < 1273; i++) {
                List<String> head = new ArrayList<>();
                head.add(String.valueOf(i)); // 第一行是序号

                // 序号为0的列日期显示为上一月最后一天的日期
                // 随序号递增加一个自然月显示日期，保持每个月的最后一天
                LocalDate date;
                if (i == 0) {
                    date = lastDayOfPreviousMonth;
                } else {
                    // 获取上一个日期所在月份的下一个月的最后一天
                    LocalDate previousDate = lastDayOfPreviousMonth.plusMonths(i - 1);
                    date = previousDate.plusMonths(1).withDayOfMonth(
                            previousDate.plusMonths(1).lengthOfMonth()
                    );
                }

                String dateStr = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                dateMap.put(i, dateStr);

                if (i < 5 || i > 1268) {
                    log.debug("序号 {} 的日期: {}", i, dateStr);
                }

                head.add(dateStr); // 第二行是日期
                headList.add(head);
            }

            // 准备数据
            List<List<Object>> dataList = new ArrayList<>();
            List<Object> rowData = new ArrayList<>();

            // 添加普通字段数据（空值）
            for (Field field : normalFields) {
                rowData.add(null);
            }

            // 添加值集数据（空值）
            for (int i = 0; i < 1273; i++) {
                rowData.add(null);
            }

            dataList.add(rowData);

            // 设置样式
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            WriteFont headWriteFont = new WriteFont();
            headWriteFont.setFontHeightInPoints((short) 12);
            headWriteFont.setBold(true);
            headWriteCellStyle.setWriteFont(headWriteFont);
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
            headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
            headWriteCellStyle.setBorderRight(BorderStyle.THIN);
            headWriteCellStyle.setBorderTop(BorderStyle.THIN);

            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
            contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
            contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
            contentWriteCellStyle.setBorderTop(BorderStyle.THIN);

            HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

            // 导出Excel
            EasyExcel.write(response.getOutputStream())
                    .head(headList)
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet(sheetName)
                    .doWrite(dataList);

        } catch (Exception e) {
            log.error("导出模板Excel异常", e);
            throw new ServiceException("导出模板Excel失败: " + e.getMessage());
        }
    }

    /**
     * 将字典数据值转换为字典标签
     *
     * @param dictType 字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    private static String convertDictValue(String dictType, String dictValue) {
        try {
            ISysDictTypeService dictTypeService = SpringUtils.getBean(ISysDictTypeService.class);
            return dictTypeService.selectDictDataByType(dictType).stream()
                    .filter(data -> data.getDictValue().equals(dictValue))
                    .map(SysDictData::getDictLabel)
                    .findFirst().orElse("");
        } catch (Exception e) {
            log.error("获取字典标签失败", e);
            return dictValue;
        }
    }

    /**
     * 解析读取转换表达式
     *
     * @param readConverterExp 读取转换表达式
     * @param value 值
     * @return 转换后的值
     */
    private static String convertReadConverterExp(String readConverterExp, String value) {
        try {
            String[] params = readConverterExp.split(",");
            for (String param : params) {
                String[] kv = param.split("=");
                if (kv.length == 2 && kv[0].equals(value)) {
                    return kv[1];
                }
            }
        } catch (Exception e) {
            log.error("解析转换表达式失败", e);
        }
        return value;
    }


}
