package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.jd.lightning.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 风险项目金额表导出DTO
 * 专门用于导出，包含中文表头
 *
 * @author AI Assistant
 */
@Data
public class RiskItemAmountExportDTO {

    /**
     * 账期，格式：YYYYMM（如202306）
     */
    @Excel(name = "账期")
    @ExcelProperty("账期")
    private String accountingPeriod;



    /**
     * 项目名称
     * 可选值包括：
     * 基础风险项目：
     * - 寿险业务保险风险最低资本
     * - 非寿险业务保险风险最低资本
     * - 市场风险最低资本
     * - 信用风险最低资本
     * - 风险分散效应
     * - 控制风险最低资本
     *
     * 量化风险相关：
     * - 量化风险最低资本
     * - 量化风险最低资本（未考虑特征系数前）
     * - 量化风险分散效应
     *
     * 寿险业务保险风险相关：
     * - 寿险业务保险风险最低资本合计
     * - 寿险业务保险风险-损失发生风险最低资本
     * - 寿险业务保险风险-退保风险最低资本
     * - 寿险业务保险风险-费用风险最低资本
     * - 寿险业务保险风险-风险分散效应
     *
     * 非寿险业务保险风险相关：
     * - 非寿险业务保险风险最低资本合计
     * - 非寿险业务保险风险-保费及准备金风险最低资本
     * - 非寿险业务保险风险-巨灾风险最低资本
     * - 非寿险业务保险风险-风险分散效应
     *
     * 市场风险相关：
     * - 市场风险-最低资本合计
     * - 市场风险-利率风险最低资本
     * - 市场风险-权益价格风险最低资本
     * - 市场风险-房地产价格风险最低资本
     * - 市场风险-境外固定收益类资产价格风险最低资本
     * - 市场风险-境外权益类资产价格风险最低资本
     * - 市场风险-汇率风险最低资本
     * - 市场风险-风险分散效应
     *
     * 信用风险相关：
     * - 信用风险-最低资本合计
     * - 信用风险-利差风险最低资本
     * - 信用风险-交易对手违约风险最低资本
     * - 信用风险-风险分散效应
     *
     * 精算假设和现值情景相关：
     * - AA基础情景
     * - PV基础情景
     * - AA利率下降情景
     * - PV利率下降情景
     */
    @Excel(name = "项目名称")
    @ExcelProperty("项目名称")
    private String itemName;

    /**
     * S05-最低资本表的期末金额
     */
    @Excel(name = "S05-最低资本表的期末金额", cellType = Excel.ColumnType.NUMERIC, scale = 10)
    @ExcelProperty("S05-最低资本表的期末金额")
    private BigDecimal s05Amount;

    /**
     * IR05-寿险业务保险风险表的期末金额
     */
    @Excel(name = "IR05-寿险业务保险风险表的期末金额", cellType = Excel.ColumnType.NUMERIC, scale = 10)
    @ExcelProperty("IR05-寿险业务保险风险表的期末金额")
    private BigDecimal ir05Amount;
}
