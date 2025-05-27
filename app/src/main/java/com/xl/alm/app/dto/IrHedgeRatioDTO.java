package com.xl.alm.app.dto;

import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 利率风险对冲率表DTO
 *
 * @author AI Assistant
 */
@Data
public class IrHedgeRatioDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 账期，格式：YYYYMM（如202306）
     */
    @Excel(name = "账期")
    @NotBlank(message = "账期不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "账期格式不正确，应为YYYYMM格式")
    @Size(max = 6, message = "账期长度不能超过6个字符")
    private String accountingPeriod;

    /**
     * 项目名称，如：利率风险资产敏感度、利率风险负债敏感度、利率风险对冲率
     */
    @Excel(name = "项目名称")
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 100, message = "项目名称长度不能超过100个字符")
    private String itemName;

    /**
     * 账户编码，对应sys_dict_data表中dict_type='minc_account'的字典项
     */
    @Excel(name = "账户编码", dictType = "minc_account")
    @NotBlank(message = "账户编码不能为空")
    @Size(max = 10, message = "账户编码长度不能超过10个字符")
    private String accountCode;

    /**
     * 敏感度或对冲率，百分比格式，如：0.0793表示7.93%
     */
    @Excel(name = "敏感度/对冲率")
    @DecimalMin(value = "-999999.9999", message = "敏感度/对冲率不能小于-999999.9999")
    @DecimalMax(value = "999999.9999", message = "敏感度/对冲率不能大于999999.9999")
    private BigDecimal sensitivityRate;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel;
}
