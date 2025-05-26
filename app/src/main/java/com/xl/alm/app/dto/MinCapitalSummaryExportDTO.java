package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.jd.lightning.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 最低资本明细汇总表Excel操作DTO
 * 用于Excel导出，包含中文表头
 *
 * @author AI Assistant
 */
@Data
public class MinCapitalSummaryExportDTO {

    /**
     * 账期，格式：YYYYMM（如202306）
     */
    @Excel(name = "账期")
    @ExcelProperty("账期")
    private String accountingPeriod;

    /**
     * 项目名称，用于Excel导出
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
    private BigDecimal companyTotalAmount;
}
