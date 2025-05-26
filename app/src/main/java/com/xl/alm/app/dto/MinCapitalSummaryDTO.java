package com.xl.alm.app.dto;

import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 最低资本明细汇总表DTO
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MinCapitalSummaryDTO extends BaseDTO {
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
     * 账户编码，对应sys_dict_data表中dict_type='minc_account'的字典项
     */
    @Size(max = 10, message = "账户编码长度不能超过10个字符")
    private String accountCode;

    /**
     * 账户金额
     */
    @Digits(integer = 18, fraction = 10, message = "账户金额格式不正确，整数部分最多18位，小数部分最多10位")
    private BigDecimal amount;

    // ========== 聚合显示字段（用于前端显示多账户） ==========

    /**
     * 传统保险账户金额（用于前端显示）
     */
    @Excel(name = "传统保险账户")
    private BigDecimal traditionalAmount;

    /**
     * 分红保险账户金额（用于前端显示）
     */
    @Excel(name = "分红保险账户")
    private BigDecimal dividendAmount;

    /**
     * 万能保险账户金额（用于前端显示）
     */
    @Excel(name = "万能保险账户")
    private BigDecimal universalAmount;

    /**
     * 独立账户金额（用于前端显示）
     */
    @Excel(name = "独立账户")
    private BigDecimal independentAmount;

    /**
     * 资本补充债账户金额（用于前端显示）
     */
    @Excel(name = "资本补充债账户")
    private BigDecimal companyTotalAmount;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel;

    // ========== 关联字段 ==========

    /**
     * 项目名称（从项目定义表关联获取）
     */
    private String itemName;

    /**
     * 风险类型（从项目定义表关联获取）
     */
    private String riskType;

    /**
     * 账户名称（从字典表关联获取）
     */
    private String accountName;
}
