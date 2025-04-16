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
            String fileName = URLEncoder.encode(sheetName, "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), clazz).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            excelWriter.write(list, writeSheet);
            excelWriter.finish();
        } catch (IOException e) {
            log.error("导出Excel异常", e);
            throw new ServiceException("导出Excel失败");
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
            ExcelWriter excelWriter = EasyExcel.write(filePath, clazz).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            excelWriter.write(list, writeSheet);
            excelWriter.finish();
        } catch (Exception e) {
            log.error("导出Excel异常", e);
            throw new ServiceException("导出Excel失败");
        }
    }

    /**
     * 导出模板
     *
     * @param response 响应对象
     * @param str 模板名称
     */
    public void exportTemplateExcel(HttpServletResponse response, String str) {
        exportExcel(new ArrayList<>(), str, response);
    }
}