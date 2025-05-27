package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import com.xl.alm.app.util.BigDecimalConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 分红方案表DTO
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DividendFundCostRateDTO extends BaseDTO {
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

    /** 是否中短：Y-是，N-否 */
    @Excel(name = "是否中短", readConverterExp = "Y=是,N=否", combo = {"Y", "N"})
    @ExcelProperty(value = "是否中短", index = 4)
    private String shortTermFlag;

    /** 定价保证成本率 */
    @Excel(name = "定价保证成本率")
    @ExcelProperty(value = "定价保证成本率", index = 5, converter = BigDecimalConverter.class)
    private BigDecimal guaranteedCostRate;

    /** 投资收益率假设 */
    @Excel(name = "投资收益率假设")
    @ExcelProperty(value = "投资收益率假设", index = 6, converter = BigDecimalConverter.class)
    private BigDecimal investmentReturnRate;

    /** 分红比例 */
    @Excel(name = "分红比例")
    @ExcelProperty(value = "分红比例", index = 7, converter = BigDecimalConverter.class)
    private BigDecimal dividendRatio;

    /** 资金成本率T0 */
    @Excel(name = "资金成本率T0")
    @ExcelProperty(value = "资金成本率T0", index = 8, converter = BigDecimalConverter.class)
    private BigDecimal fundCostRateT0;

    /** 资金成本率T1 */
    @Excel(name = "资金成本率T1")
    @ExcelProperty(value = "资金成本率T1", index = 9, converter = BigDecimalConverter.class)
    private BigDecimal fundCostRateT1;

    /** 资金成本率T2 */
    @Excel(name = "资金成本率T2")
    @ExcelProperty(value = "资金成本率T2", index = 10, converter = BigDecimalConverter.class)
    private BigDecimal fundCostRateT2;

    /** 资金成本率T3 */
    @Excel(name = "资金成本率T3")
    @ExcelProperty(value = "资金成本率T3", index = 11, converter = BigDecimalConverter.class)
    private BigDecimal fundCostRateT3;

    /** 备注 */
    @Excel(name = "备注")
    @ExcelProperty(value = "备注", index = 12)
    private String remark;

    /** 是否删除，0:否，1:是 */
    @ExcelIgnore
    private Integer isDel;
}
