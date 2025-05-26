package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.jd.lightning.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 市场及信用最低资本表导出DTO
 *
 * @author alm
 * @date 2024-01-01
 */
@Data
public class MinCapitalByAccountExportDTO {

    /**
     * 账期
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
     * 普通账户金额
     */
    @Excel(name = "普通账户", cellType = Excel.ColumnType.NUMERIC, scale = 2)
    @ExcelProperty("普通账户")
    private BigDecimal generalAmount;
}
