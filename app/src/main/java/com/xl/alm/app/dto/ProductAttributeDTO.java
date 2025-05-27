package com.xl.alm.app.dto;

import com.jd.lightning.common.annotation.Excel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.xl.alm.app.util.BigDecimalConverter;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 产品属性表DTO
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductAttributeDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @ExcelIgnore
    private Long id;

    /** 账期 */
    @NotBlank(message = "账期不能为空")
    @Excel(name = "账期")
    @ExcelProperty("账期")
    private String accountingPeriod;

    /** 精算代码 */
    @NotBlank(message = "精算代码不能为空")
    @Excel(name = "精算代码")
    @ExcelProperty("精算代码")
    private String actuarialCode;

    /** 业务代码 */
    @NotBlank(message = "业务代码不能为空")
    @Excel(name = "业务代码")
    @ExcelProperty("业务代码")
    private String businessCode;

    /** 产品名称 */
    @NotBlank(message = "产品名称不能为空")
    @Excel(name = "产品名称")
    @ExcelProperty("产品名称")
    private String productName;

    /** 长短期标识：L-长期，S-短期 */
    @NotBlank(message = "长短期标识不能为空")
    @Excel(name = "长短期标识", readConverterExp = "L=长期,S=短期", combo = {"L", "S"}, dictType = "cost_term_type")
    @ExcelProperty("长短期标识")
    private String termType;

    /** 险种主类 */
    @Excel(name = "险种主类")
    @ExcelProperty("险种主类")
    private String insuranceMainType;

    /** 险种细类 */
    @Excel(name = "险种细类")
    @ExcelProperty("险种细类")
    private String insuranceSubType;

    /** 设计类型 */
    @NotBlank(message = "设计类型不能为空")
    @Excel(name = "设计类型", dictType = "cost_design_type")
    @ExcelProperty("设计类型")
    private String designType;

    /** 是否中短：Y-是，N-否 */
    @NotBlank(message = "是否中短不能为空")
    @Excel(name = "是否中短", readConverterExp = "Y=是,N=否", combo = {"Y", "N"}, dictType = "cost_short_term_flag")
    @ExcelProperty("是否中短")
    private String shortTermFlag;

    /** 报监管中短标识：Y-是，N-否 */
    @Excel(name = "报监管中短标识", readConverterExp = "Y=是,N=否", combo = {"Y", "N"})
    @ExcelProperty("报监管中短标识")
    private String regMidId;

    /** 定价保证成本率 */
    @Excel(name = "定价保证成本率")
    @ExcelProperty(value = "定价保证成本率", converter = BigDecimalConverter.class)
    private BigDecimal guaranteedCostRate;

    /** 子账户 */
    @Excel(name = "子账户")
    @ExcelProperty("子账户")
    private String subAccount;

    /** 新业务标识：Y-是，N-否 */
    @Excel(name = "新业务标识", readConverterExp = "Y=是,N=否", combo = {"Y", "N"})
    @ExcelProperty("新业务标识")
    private String newBusinessFlag;

    /** 备注 */
    @ExcelIgnore
    private String remark;

    /** 是否删除，0:否，1:是 */
    @ExcelIgnore
    private Integer isDel;
}
