package com.xl.alm.app.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
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
import java.net.URLEncoder;
import java.util.*;

/**
 * 自定义Excel导出工具类
 * 用于处理特殊格式的Excel导出，如分中短负债基点价值DV10
 */
public class CustomExcelExporter {
    private static final Logger log = LoggerFactory.getLogger(CustomExcelExporter.class);

    /**
     * 导出分中短负债基点价值DV10数据
     *
     * @param list      数据列表
     * @param sheetName 工作表名称
     * @param response  响应对象
     * @param <T>       数据类型
     */
    public static <T> void exportLiabilityDv10ByDuration(List<T> list, String sheetName, HttpServletResponse response) {
        try {
            log.info("开始导出分中短负债基点价值DV10数据");

            // 设置下载文件的名称和类型
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(sheetName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 检查数据列表是否为空
            if (list == null || list.isEmpty()) {
                log.warn("导出数据列表为空");

                // 创建一个简单的Excel文件，只有一行提示信息
                List<Map<String, String>> emptyData = new ArrayList<>();
                Map<String, String> emptyRow = new HashMap<>();
                emptyRow.put("提示", "没有符合条件的数据");
                emptyData.add(emptyRow);

                EasyExcel.write(response.getOutputStream())
                        .sheet(sheetName)
                        .doWrite(emptyData);

                log.info("导出空数据Excel文件完成");
                return;
            }

            log.info("开始处理导出数据，共{}条记录", list.size());

            // 使用ExcelUtil导出，确保使用@Excel注解中的中文名称
            ExcelUtil<T> util = new ExcelUtil<>((Class<T>) list.get(0).getClass());
            // 第二个参数是导出文件的标题，第三个参数是响应对象
            // 这里使用sheetName作为标题，确保导出的Excel文件使用中文表头
            util.exportExcel(list, sheetName, response);

            log.info("导出Excel文件完成");
        } catch (Exception e) {
            log.error("导出Excel异常", e);
            throw new ServiceException("导出Excel失败");
        }
    }

    /**
     * 获取字段显示名称
     *
     * @param fieldName 字段名称
     * @return 显示名称
     */
    private static String getFieldDisplayName(String fieldName) {
        Map<String, String> fieldNameMap = new HashMap<>();
        fieldNameMap.put("id", "ID");
        fieldNameMap.put("accountPeriod", "账期");
        fieldNameMap.put("cashFlowType", "现金流类型");
        fieldNameMap.put("designType", "设计类型");
        fieldNameMap.put("shortTermFlag", "是否为中短期险种");
        fieldNameMap.put("valueType", "现值类型");
        fieldNameMap.put("term0", "0年期限点");
        fieldNameMap.put("term05", "0.5年期限点");
        fieldNameMap.put("term1", "1年期限点");
        fieldNameMap.put("term2", "2年期限点");
        fieldNameMap.put("term3", "3年期限点");
        fieldNameMap.put("term4", "4年期限点");
        fieldNameMap.put("term5", "5年期限点");
        fieldNameMap.put("term6", "6年期限点");
        fieldNameMap.put("term7", "7年期限点");
        fieldNameMap.put("term8", "8年期限点");
        fieldNameMap.put("term10", "10年期限点");
        fieldNameMap.put("term12", "12年期限点");
        fieldNameMap.put("term15", "15年期限点");
        fieldNameMap.put("term20", "20年期限点");
        fieldNameMap.put("term25", "25年期限点");
        fieldNameMap.put("term30", "30年期限点");
        fieldNameMap.put("term35", "35年期限点");
        fieldNameMap.put("term40", "40年期限点");
        fieldNameMap.put("term45", "45年期限点");
        fieldNameMap.put("term50", "50年期限点");
        fieldNameMap.put("createTime", "创建时间");
        fieldNameMap.put("createBy", "创建者");
        fieldNameMap.put("updateTime", "更新时间");
        fieldNameMap.put("updateBy", "更新者");

        return fieldNameMap.getOrDefault(fieldName, fieldName);
    }

    /**
     * 将字典数据值转换为字典标签
     *
     * @param dictType 字典类型
     * @param dictValue 字典值
     * @return 字典标签
     */
    private static String convertDictValue(String dictType, String dictValue) {
        // 如果字典类型或字典值为空，直接返回原值
        if (StringUtils.isEmpty(dictType) || StringUtils.isEmpty(dictValue)) {
            return dictValue;
        }

        try {
            // 使用更安全的方式获取字典服务
            ISysDictTypeService dictTypeService = null;
            try {
                dictTypeService = SpringUtils.getBean(ISysDictTypeService.class);
            } catch (Exception e) {
                log.error("获取字典服务失败", e);
                return dictValue;
            }

            // 如果字典服务为空，直接返回原值
            if (dictTypeService == null) {
                return dictValue;
            }

            // 获取字典数据列表
            List<SysDictData> dictDataList = dictTypeService.selectDictDataByType(dictType);

            // 如果字典数据列表为空，直接返回原值
            if (dictDataList == null || dictDataList.isEmpty()) {
                return dictValue;
            }

            // 查找匹配的字典标签
            for (SysDictData dictData : dictDataList) {
                if (dictValue.equals(dictData.getDictValue())) {
                    return dictData.getDictLabel();
                }
            }

            // 如果没有找到匹配的字典标签，返回原值
            return dictValue;
        } catch (Exception e) {
            log.error("获取字典标签失败", e);
            return dictValue;
        }
    }


}
