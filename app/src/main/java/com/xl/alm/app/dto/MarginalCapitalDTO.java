package com.xl.alm.app.dto;

import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 边际最低资本贡献率表DTO
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MarginalCapitalDTO extends BaseDTO {
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
     * 项目编码，关联项目定义表(t_minc_item_definition)的item_code
     */
    @NotBlank(message = "项目编码不能为空")
    @Size(max = 20, message = "项目编码长度不能超过20个字符")
    @Excel(name = "项目编码")
    private String itemCode;

    /**
     * 再保后金额，数值类型，保留2位小数
     */
    @NotNull(message = "再保后金额不能为空")
    @DecimalMin(value = "0", message = "再保后金额不能小于0")
    @Digits(integer = 16, fraction = 2, message = "再保后金额格式不正确，整数部分最多16位，小数部分最多2位")
    @Excel(name = "再保后金额")
    private BigDecimal reinsuAfterAmount;

    /**
     * 存储子风险层面边际最低资本贡献因子，百分比格式，如：0.583表示58.3%
     */
    @DecimalMin(value = "0", message = "子风险边际因子不能小于0")
    @DecimalMax(value = "1", message = "子风险边际因子不能大于1")
    @Digits(integer = 1, fraction = 4, message = "子风险边际因子格式不正确，整数部分最多1位，小数部分最多4位")
    @Excel(name = "子风险边际因子")
    private BigDecimal subRiskMarginalFactor;

    /**
     * 存储公司层面边际最低资本贡献因子，百分比格式，如：0.583表示58.3%
     */
    @DecimalMin(value = "0", message = "公司边际因子不能小于0")
    @DecimalMax(value = "1", message = "公司边际因子不能大于1")
    @Digits(integer = 1, fraction = 4, message = "公司边际因子格式不正确，整数部分最多1位，小数部分最多4位")
    @Excel(name = "公司边际因子")
    private BigDecimal companyMarginalFactor;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel;

    // ========== 关联项目定义表的字段 ==========
    
    /**
     * 项目名称（从项目定义表关联获取）
     */
    private String itemName;

    /**
     * 风险类型（从项目定义表关联获取）
     */
    private String riskType;
}
