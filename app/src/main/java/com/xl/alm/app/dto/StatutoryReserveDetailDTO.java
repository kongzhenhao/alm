package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jd.lightning.common.annotation.Excel;
import com.xl.alm.app.dto.BaseExcelDTO;
import com.xl.alm.app.util.BigDecimalConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 法定准备金明细表DTO
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatutoryReserveDetailDTO extends BaseExcelDTO {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @ExcelIgnore
    private Long id;

    /** 账期 */
    @Excel(name = "账期")
    @ExcelProperty(value = "账期", index = 0)
    private String accountingPeriod;

    /** 精算代码 */
    @Excel(name = "精算代码")
    @ExcelProperty(value = "精算代码", index = 1)
    private String actuarialCode;

    /** 业务代码 */
    @Excel(name = "业务代码")
    @ExcelProperty(value = "业务代码", index = 2)
    private String businessCode;

    /** 产品名称 */
    @Excel(name = "产品名称")
    @ExcelProperty(value = "产品名称", index = 3)
    private String productName;

    /** 长短期标识：L-长期，S-短期 */
    @Excel(name = "长短期标识", readConverterExp = "L=长期,S=短期", combo = {"L", "S"})
    @ExcelProperty(value = "长短期标识", index = 4)
    private String termType;

    /** 设计类型 */
    @Excel(name = "设计类型")
    @ExcelProperty(value = "设计类型", index = 5)
    private String designType;

    /** 是否中短：Y-是，N-否 */
    @Excel(name = "是否中短", readConverterExp = "Y=是,N=否", combo = {"Y", "N"})
    @ExcelProperty(value = "是否中短", index = 6)
    private String shortTermFlag;

    /** 有效保单件数 */
    @Excel(name = "有效保单件数")
    @ExcelProperty(value = "有效保单件数", index = 7)
    private Integer validPolicyCount;

    /** 存量累计规模保费 */
    @Excel(name = "存量累计规模保费")
    @ExcelProperty(value = "存量累计规模保费", index = 8, converter = BigDecimalConverter.class)
    private BigDecimal accumulatedPremium;

    /** 账户价值 */
    @Excel(name = "账户价值")
    @ExcelProperty(value = "账户价值", index = 9, converter = BigDecimalConverter.class)
    private BigDecimal accountValue;

    /** 法定/非单位准备金 */
    @Excel(name = "法定/非单位准备金")
    @ExcelProperty(value = "法定/非单位准备金", index = 10, converter = BigDecimalConverter.class)
    private BigDecimal statutoryReserve;

    /** 保证利率准备金 */
    @Excel(name = "保证利率准备金")
    @ExcelProperty(value = "保证利率准备金", index = 11, converter = BigDecimalConverter.class)
    private BigDecimal guaranteedRateReserve;

    /** 失效单现价 */
    @Excel(name = "失效单现价")
    @ExcelProperty(value = "失效单现价", index = 12, converter = BigDecimalConverter.class)
    private BigDecimal lapsedPolicyValue;

    /** 豁免责任准备金 */
    @Excel(name = "豁免责任准备金")
    @ExcelProperty(value = "豁免责任准备金", index = 13, converter = BigDecimalConverter.class)
    private BigDecimal waiverReserve;

    /** 未建模准备金 */
    @Excel(name = "未建模准备金")
    @ExcelProperty(value = "未建模准备金", index = 14, converter = BigDecimalConverter.class)
    private BigDecimal unmodeledReserve;

    /** 持续奖准备金 */
    @Excel(name = "持续奖准备金")
    @ExcelProperty(value = "持续奖准备金", index = 15, converter = BigDecimalConverter.class)
    private BigDecimal persistenceBonusReserve;

    /** 长期未到期准备金 */
    @Excel(name = "长期未到期准备金")
    @ExcelProperty(value = "长期未到期准备金", index = 16, converter = BigDecimalConverter.class)
    private BigDecimal longTermUnearned;

    /** 短险未到期准备金 */
    @Excel(name = "短险未到期准备金")
    @ExcelProperty(value = "短险未到期准备金", index = 17, converter = BigDecimalConverter.class)
    private BigDecimal shortTermUnearned;

    /** 未到期责任准备金 */
    @Excel(name = "未到期责任准备金")
    @ExcelProperty(value = "未到期责任准备金", index = 18, converter = BigDecimalConverter.class)
    private BigDecimal unearnedPremiumReserve;

    /** 已报未决赔款 */
    @Excel(name = "已报未决赔款")
    @ExcelProperty(value = "已报未决赔款", index = 19, converter = BigDecimalConverter.class)
    private BigDecimal reportedUnpaid;

    /** 未报未决赔款 */
    @Excel(name = "未报未决赔款")
    @ExcelProperty(value = "未报未决赔款", index = 20, converter = BigDecimalConverter.class)
    private BigDecimal incurredUnreported;

    /** 理赔费用准备金 */
    @Excel(name = "理赔费用准备金")
    @ExcelProperty(value = "理赔费用准备金", index = 21, converter = BigDecimalConverter.class)
    private BigDecimal claimExpenseReserve;

    /** 未决赔款准备金 */
    @Excel(name = "未决赔款准备金")
    @ExcelProperty(value = "未决赔款准备金", index = 22, converter = BigDecimalConverter.class)
    private BigDecimal outstandingClaimReserve;

    /** 法定准备金合计 */
    @Excel(name = "法定准备金合计")
    @ExcelProperty(value = "法定准备金合计", index = 23, converter = BigDecimalConverter.class)
    private BigDecimal totalStatutoryReserve;

    /** 备注 */
    @ExcelIgnore
    private String remark;

    /** 是否删除，0:否，1:是 */
    @ExcelIgnore
    private Integer isDel;
}
