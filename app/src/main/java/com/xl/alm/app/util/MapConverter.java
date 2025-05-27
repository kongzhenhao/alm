package com.xl.alm.app.util;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

import java.util.Map;

/**
 * Map类型转换器，用于EasyExcel导出
 * 将Map类型转换为字符串
 *
 * @author AI Assistant
 */
public class MapConverter implements Converter<Map<String, Object>> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return Map.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Map<String, Object>> context) {
        Map<String, Object> value = context.getValue();
        if (value == null) {
            return new WriteCellData<>("");
        }
        // 将Map转换为字符串
        return new WriteCellData<>(value.toString());
    }
}
