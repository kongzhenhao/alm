package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.jd.lightning.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 分部门最低资本明细表导出DTO
 * 专门用于导出，包含中文表头
 *
 * @author AI Assistant
 */
@Data
public class DeptMincapDetailExportDTO {

    /**
     * 账期，格式：YYYYMM（如202306）
     */
    @Excel(name = "账期")
    @ExcelProperty("账期")
    private String accountingPeriod;

    /**
     * 部门名称
     */
    @Excel(name = "部门名称")
    @ExcelProperty("部门名称")
    private String department;

    /**
     * 项目
     */
    @Excel(name = "项目")
    @ExcelProperty("项目")
    private String itemName;

    /**
     * 传统保险账户金额
     */
    @Excel(name = "传统保险账户", cellType = Excel.ColumnType.NUMERIC, scale = 2)
    @ExcelProperty("传统保险账户")
    private BigDecimal traditionalAmount;

    /**
     * 分红保险账户金额
     */
    @Excel(name = "分红保险账户", cellType = Excel.ColumnType.NUMERIC, scale = 2)
    @ExcelProperty("分红保险账户")
    private BigDecimal dividendAmount;

    /**
     * 万能保险账户金额
     */
    @Excel(name = "万能保险账户", cellType = Excel.ColumnType.NUMERIC, scale = 2)
    @ExcelProperty("万能保险账户")
    private BigDecimal universalAmount;

    /**
     * 独立账户金额
     */
    @Excel(name = "独立账户", cellType = Excel.ColumnType.NUMERIC, scale = 2)
    @ExcelProperty("独立账户")
    private BigDecimal independentAmount;

    /**
     * 资本补充债账户金额
     */
    @Excel(name = "资本补充债账户", cellType = Excel.ColumnType.NUMERIC, scale = 2)
    @ExcelProperty("资本补充债账户")
    private BigDecimal capitalSupplementAmount;
}
