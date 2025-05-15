package com.xl.alm.app.util;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.jd.lightning.common.utils.StringUtils;

import java.math.BigDecimal;

/**
 * BigDecimal类型转换器，用于EasyExcel导入导出
 * 将字符串转换为BigDecimal，处理空值和非数字字符
 *
 * @author AI Assistant
 */
public class BigDecimalConverter implements Converter<BigDecimal> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return BigDecimal.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public BigDecimal convertToJavaData(ReadConverterContext<?> context) {
        ReadCellData<?> cellData = context.getReadCellData();

        // 处理不同类型的单元格数据
        switch (cellData.getType()) {
            case STRING:
                String stringValue = cellData.getStringValue();
                // 如果为空，返回null
                if (StringUtils.isEmpty(stringValue)) {
                    return null;
                }

                try {
                    // 处理可能包含的特殊字符
                    stringValue = stringValue.trim()
                            .replace(",", "")
                            .replace("%", "")
                            .replace("\u00A0", ""); // 处理不间断空格

                    if (StringUtils.isEmpty(stringValue)) {
                        return null;
                    }

                    // 尝试转换为BigDecimal
                    return new BigDecimal(stringValue);
                } catch (NumberFormatException e) {
                    // 如果转换失败，返回null
                    return null;
                }
            case NUMBER:
                // 直接获取数字值
                Object numberValue = cellData.getNumberValue();
                if (numberValue == null) {
                    return null;
                }

                // 处理不同类型的数字值
                if (numberValue instanceof BigDecimal) {
                    return (BigDecimal) numberValue;
                } else if (numberValue instanceof Double) {
                    return BigDecimal.valueOf((Double) numberValue);
                } else {
                    // 其他类型转换为字符串再转换为BigDecimal
                    return new BigDecimal(numberValue.toString());
                }
            case BOOLEAN:
                // 将布尔值转换为0或1
                Boolean boolValue = cellData.getBooleanValue();
                return boolValue ? BigDecimal.ONE : BigDecimal.ZERO;
            case EMPTY:
                return null;
            default:
                return null;
        }
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<BigDecimal> context) {
        BigDecimal value = context.getValue();
        if (value == null) {
            return new WriteCellData<>("");
        }
        return new WriteCellData<>(value.toString());
    }
}
