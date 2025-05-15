package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jd.lightning.common.annotation.Excel;
import com.xl.alm.app.util.BigDecimalConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 法定准备金预测表DTO
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatutoryReserveForecastDTO extends BaseExcelDTO {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @ExcelIgnore
    private Long id;

    /** 账期 */
    @Excel(name = "账期")
    @ExcelProperty(value = "账期", index = 0)
    private String accountingPeriod;

    /** 业务类型 */
    @Excel(name = "业务类型", dictType = "cost_business_type")
    @ExcelProperty(value = "业务类型", index = 1)
    private String businessType;

    /** 精算代码 */
    @Excel(name = "精算代码")
    @ExcelProperty(value = "精算代码", index = 2)
    private String actuarialCode;

    /** 业务代码 */
    @Excel(name = "业务代码")
    @ExcelProperty(value = "业务代码", index = 3)
    private String businessCode;

    /** 产品名称 */
    @Excel(name = "产品名称")
    @ExcelProperty(value = "产品名称", index = 4)
    private String productName;

    /** 设计类型 */
    @Excel(name = "设计类型", dictType = "cost_design_type")
    @ExcelProperty(value = "设计类型", index = 5)
    private String designType;

    /** 长短期标识：L-长期，S-短期 */
    @ExcelIgnore
    private String termType;

    /** 是否中短：Y-是，N-否 */
    @Excel(name = "是否中短", readConverterExp = "Y=是,N=否", combo = {"Y", "N"}, dictType = "cost_short_term_flag")
    @ExcelProperty(value = "是否中短", index = 6)
    private String shortTermFlag;

    /** 法定准备金T1 */
    @Excel(name = "法定准备金T1")
    @ExcelProperty(value = "法定准备金T1", index = 7, converter = BigDecimalConverter.class)
    private BigDecimal statutoryReserveT1;

    /** 法定准备金T2 */
    @Excel(name = "法定准备金T2")
    @ExcelProperty(value = "法定准备金T2", index = 8, converter = BigDecimalConverter.class)
    private BigDecimal statutoryReserveT2;

    /** 法定准备金T3 */
    @Excel(name = "法定准备金T3")
    @ExcelProperty(value = "法定准备金T3", index = 9, converter = BigDecimalConverter.class)
    private BigDecimal statutoryReserveT3;

    /** 是否删除，0:否，1:是 */
    @ExcelIgnore
    private Integer isDel;
}
