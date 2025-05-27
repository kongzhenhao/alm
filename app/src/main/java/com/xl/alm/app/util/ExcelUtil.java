package com.xl.alm.app.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.jd.lightning.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Excel相关处理
 */
public class ExcelUtil<T> {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * Excel实体类型
     */
    private final Class<T> clazz;

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 导入Excel数据
     *
     * @param inputStream 输入流
     * @return 数据列表
     */
    public List<T> importExcel(InputStream inputStream) throws Exception {
        List<T> list = new ArrayList<>();
        final StringBuilder errorMsg = new StringBuilder();

        try {
            // 创建一个自定义的分析监听器，处理异常情况
            AnalysisEventListener<T> listener = new AnalysisEventListener<T>() {
                @Override
                public void invoke(T data, AnalysisContext context) {
                    list.add(data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    log.info("导入Excel完成，共读取{}\u6761数据", list.size());
                }

                @Override
                public void onException(Exception exception, AnalysisContext context) throws Exception {
                    log.error("导入Excel异常", exception);
                    // 记录异常信息
                    if (exception instanceof ExcelDataConvertException) {
                        ExcelDataConvertException excelException = (ExcelDataConvertException) exception;
                        errorMsg.append("第").append(excelException.getRowIndex() + 1)
                               .append("行，第").append(excelException.getColumnIndex() + 1)
                               .append("列数据格式错误\n");
                    } else {
                        errorMsg.append(exception.getMessage()).append("\n");
                    }
                    // 继续处理下一行数据
                    throw exception;
                }
            };

            // 注册自定义转换器
            EasyExcel.read(inputStream, clazz, listener)
                .registerConverter(new BigDecimalConverter())
                .registerConverter(new MapConverter())
                .sheet().doRead();

            // 如果有错误信息，抛出异常
            if (errorMsg.length() > 0) {
                throw new Exception("导入Excel数据失败\n" + errorMsg.toString());
            }

            return list;
        } catch (Exception e) {
            log.error("导入Excel异常", e);
            if (errorMsg.length() > 0) {
                throw new Exception("导入Excel数据失败\n" + errorMsg.toString());
            } else {
                throw new Exception("导入Excel数据失败: " + e.getMessage());
            }
        }
    }

    /**
     * 导出Excel
     *
     * @param list      数据列表
     * @param sheetName 工作表名称
     * @param response  响应对象
     */
    public void exportExcel(List<T> list, String sheetName, HttpServletResponse response) {
        try {
            // 处理数据，确保每个单元格的内容不超过32767字符
            List<T> processedList = new ArrayList<>();
            for (T item : list) {
                processedList.add(processLongFields(item));
            }

            String fileName = URLEncoder.encode(sheetName, "UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 使用标准的EasyExcel写入方式，添加自定义转换器
            // 使用@ExcelProperty注解作为表头
            // 排除空字段
            EasyExcel.write(response.getOutputStream(), clazz)
                    .registerConverter(new MapConverter())
                    .registerConverter(new BigDecimalConverter())
                    .excludeColumnFiledNames(getExcludeColumnFiledNames())
                    .sheet(sheetName)
                    .doWrite(processedList);
        } catch (Exception e) {
            log.error("导出Excel异常", e);
            throw new ServiceException("导出Excel失败: " + e.getMessage());
        }
    }

    /**
     * 导出模板
     *
     * @param response 响应对象
     * @param str 模板名称
     */
    public void exportTemplateExcel(HttpServletResponse response, String str) {
        try {
            // 创建一个空实例作为模板
            T instance = clazz.newInstance();
            List<T> templateList = new ArrayList<>();
            templateList.add(instance);

            // 处理数据，确保每个单元格的内容不超过32767字符
            List<T> processedList = new ArrayList<>();
            for (T item : templateList) {
                processedList.add(processLongFields(item));
            }

            String fileName = URLEncoder.encode(str + "模板", "UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            // 使用标准的EasyExcel写入方式，添加Map类型转换器
            EasyExcel.write(response.getOutputStream(), clazz)
                    .registerConverter(new MapConverter())
                    .registerConverter(new BigDecimalConverter())
                    .excludeColumnFiledNames(getExcludeColumnFiledNames())
                    .sheet(str + "模板")
                    .doWrite(processedList);
        } catch (Exception e) {
            log.error("导出模板异常", e);
            throw new ServiceException("导出模板失败: " + e.getMessage());
        }
    }

    /**
     * 获取需要排除的字段名
     *
     * @return 需要排除的字段名列表
     */
    private Set<String> getExcludeColumnFiledNames() {
        Set<String> excludeColumnFiledNames = new HashSet<>();
        excludeColumnFiledNames.add("createBy");
        excludeColumnFiledNames.add("createTime");
        excludeColumnFiledNames.add("updateBy");
        excludeColumnFiledNames.add("updateTime");
        excludeColumnFiledNames.add("remark");
        excludeColumnFiledNames.add("isDel");
        return excludeColumnFiledNames;
    }

    /**
     * 处理长文本字段，确保不超过Excel单元格最大长度限制
     *
     * @param item 原始数据项
     * @return 处理后的数据项
     */
    @SuppressWarnings("unchecked")
    private T processLongFields(T item) {
        if (item == null) {
            return null;
        }

        try {
            // 创建一个新实例
            T newItem = (T) item.getClass().newInstance();

            // 获取所有字段
            Field[] fields = item.getClass().getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                // 跳过static和final字段
                int modifiers = field.getModifiers();
                if (java.lang.reflect.Modifier.isStatic(modifiers) || java.lang.reflect.Modifier.isFinal(modifiers)) {
                    continue;
                }

                Object value = field.get(item);

                // 如果是字符串类型且不为空
                if (value instanceof String) {
                    String strValue = (String) value;

                    // 如果超过32767字符（Excel单元格限制）
                    if (strValue.length() > 32767) {
                        // 截断并添加提示
                        String truncated = strValue.substring(0, 32700) + "... (内容过长已截断)";
                        field.set(newItem, truncated);
                    } else {
                        field.set(newItem, value);
                    }
                } else {
                    // 非字符串类型直接复制
                    field.set(newItem, value);
                }
            }

            return newItem;
        } catch (Exception e) {
            log.error("处理长文本字段异常", e);
            // 如果处理失败，返回原始项
            return item;
        }
    }
}