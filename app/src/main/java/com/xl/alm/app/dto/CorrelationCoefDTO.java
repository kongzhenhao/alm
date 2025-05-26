package com.xl.alm.app.dto;

import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 相关系数表DTO
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CorrelationCoefDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 账期，格式：YYYYMM（如202306）
     */
    @NotBlank(message = "账期不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "账期格式不正确，应为YYYYMM格式")
    @Excel(name = "账期", readConverterExp = "格式：YYYYMM")
    private String accountingPeriod;

    /**
     * 第一个风险项目编码，关联项目定义表(t_minc_item_definition)的item_code
     */
    @NotBlank(message = "第一个风险项目编码不能为空")
    @Size(max = 20, message = "第一个风险项目编码长度不能超过20个字符")
    @Excel(name = "第一个风险项目编码")
    private String itemCodeX;

    /**
     * 第二个风险项目编码，关联项目定义表(t_minc_item_definition)的item_code
     */
    @NotBlank(message = "第二个风险项目编码不能为空")
    @Size(max = 20, message = "第二个风险项目编码长度不能超过20个字符")
    @Excel(name = "第二个风险项目编码")
    private String itemCodeY;

    /**
     * 存储相关系数，范围[-1,1]
     */
    @NotNull(message = "相关系数不能为空")
    @DecimalMin(value = "-1.00", message = "相关系数不能小于-1")
    @DecimalMax(value = "1.00", message = "相关系数不能大于1")
    @Digits(integer = 1, fraction = 2, message = "相关系数格式不正确，整数部分最多1位，小数部分最多2位")
    @Excel(name = "相关系数")
    private BigDecimal correlationValue;



    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel;

    // ========== 关联项目定义表的字段 ==========

    /**
     * 第一个风险项目名称（从项目定义表关联获取）
     */
    private String itemNameX;

    /**
     * 第二个风险项目名称（从项目定义表关联获取）
     */
    private String itemNameY;

    /**
     * 第一个风险项目的风险类型（从项目定义表关联获取）
     */
    private String riskTypeX;

    /**
     * 第二个风险项目的风险类型（从项目定义表关联获取）
     */
    private String riskTypeY;
}
