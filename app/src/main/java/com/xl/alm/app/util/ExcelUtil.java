package com.xl.alm.app.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.jd.lightning.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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
        try {
            EasyExcel.read(inputStream, clazz, new AnalysisEventListener<T>() {
                @Override
                public void invoke(T data, AnalysisContext context) {
                    list.add(data);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            }).sheet().doRead();
            return list;
        } catch (Exception e) {
            log.error("导入Excel异常", e);
            throw new Exception("导入Excel数据失败");
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

            // 使用标准的EasyExcel写入方式，添加BigDecimal格式处理
            EasyExcel.write(response.getOutputStream(), clazz)
                    .sheet(sheetName)
                    .doWrite(processedList);
        } catch (Exception e) {
            log.error("导出Excel异常", e);
            throw new ServiceException("导出Excel失败: " + e.getMessage());
        }
    }

    /**
     * 导出Excel到指定路径
     *
     * @param list      数据列表
     * @param sheetName 工作表名称
     * @param filePath  文件路径
     */
    public void exportExcel(List<T> list, String sheetName, String filePath) {
        try {
            // 处理数据，确保每个单元格的内容不超过32767字符
            List<T> processedList = new ArrayList<>();
            for (T item : list) {
                processedList.add(processLongFields(item));
            }

            // 使用直接写入方式，添加BigDecimal格式处理
            EasyExcel.write(filePath, clazz)
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

            // 导出模板
            exportExcel(templateList, str + "模板", response);
        } catch (Exception e) {
            log.error("导出模板异常", e);
            throw new ServiceException("导出模板失败: " + e.getMessage());
        }
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
                Object value = field.get(item);

                // 如果是字符串类型且不为空
                if (value instanceof String && value != null) {
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