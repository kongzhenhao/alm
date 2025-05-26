package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.jd.lightning.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 分账户负债基点价值DV10表导出DTO
 * 专门用于导出，包含中文表头和转换后的字段值
 *
 * @author AI Assistant
 */
@Data
public class AccountLiabilityDv10ExportDTO {

    /** 账期 */
    @Excel(name = "账期")
    @ExcelProperty("账期")
    private String accountPeriod;

    /** 现金流类型 */
    @Excel(name = "现金流类型")
    @ExcelProperty("现金流类型")
    private String cashFlowTypeName;

    /** 设计类型 */
    @Excel(name = "设计类型")
    @ExcelProperty("设计类型")
    private String designTypeName;

    /** 现值类型 */
    @Excel(name = "现值类型")
    @ExcelProperty("现值类型")
    private String valueTypeName;

    /** 0年期限点的DV10现值 */
    @Excel(name = "0年期限点")
    @ExcelProperty("0年期限点")
    private BigDecimal term0;

    /** 0.5年期限点的DV10现值 */
    @Excel(name = "0.5年期限点")
    @ExcelProperty("0.5年期限点")
    private BigDecimal term05;

    /** 1年期限点的DV10现值 */
    @Excel(name = "1年期限点")
    @ExcelProperty("1年期限点")
    private BigDecimal term1;

    /** 2年期限点的DV10现值 */
    @Excel(name = "2年期限点")
    @ExcelProperty("2年期限点")
    private BigDecimal term2;

    /** 3年期限点的DV10现值 */
    @Excel(name = "3年期限点")
    @ExcelProperty("3年期限点")
    private BigDecimal term3;

    /** 4年期限点的DV10现值 */
    @Excel(name = "4年期限点")
    @ExcelProperty("4年期限点")
    private BigDecimal term4;

    /** 5年期限点的DV10现值 */
    @Excel(name = "5年期限点")
    @ExcelProperty("5年期限点")
    private BigDecimal term5;

    /** 6年期限点的DV10现值 */
    @Excel(name = "6年期限点")
    @ExcelProperty("6年期限点")
    private BigDecimal term6;

    /** 7年期限点的DV10现值 */
    @Excel(name = "7年期限点")
    @ExcelProperty("7年期限点")
    private BigDecimal term7;

    /** 8年期限点的DV10现值 */
    @Excel(name = "8年期限点")
    @ExcelProperty("8年期限点")
    private BigDecimal term8;

    /** 10年期限点的DV10现值 */
    @Excel(name = "10年期限点")
    @ExcelProperty("10年期限点")
    private BigDecimal term10;

    /** 12年期限点的DV10现值 */
    @Excel(name = "12年期限点")
    @ExcelProperty("12年期限点")
    private BigDecimal term12;

    /** 15年期限点的DV10现值 */
    @Excel(name = "15年期限点")
    @ExcelProperty("15年期限点")
    private BigDecimal term15;

    /** 20年期限点的DV10现值 */
    @Excel(name = "20年期限点")
    @ExcelProperty("20年期限点")
    private BigDecimal term20;

    /** 25年期限点的DV10现值 */
    @Excel(name = "25年期限点")
    @ExcelProperty("25年期限点")
    private BigDecimal term25;

    /** 30年期限点的DV10现值 */
    @Excel(name = "30年期限点")
    @ExcelProperty("30年期限点")
    private BigDecimal term30;

    /** 35年期限点的DV10现值 */
    @Excel(name = "35年期限点")
    @ExcelProperty("35年期限点")
    private BigDecimal term35;

    /** 40年期限点的DV10现值 */
    @Excel(name = "40年期限点")
    @ExcelProperty("40年期限点")
    private BigDecimal term40;

    /** 45年期限点的DV10现值 */
    @Excel(name = "45年期限点")
    @ExcelProperty("45年期限点")
    private BigDecimal term45;

    /** 50年期限点的DV10现值 */
    @Excel(name = "50年期限点")
    @ExcelProperty("50年期限点")
    private BigDecimal term50;
}
