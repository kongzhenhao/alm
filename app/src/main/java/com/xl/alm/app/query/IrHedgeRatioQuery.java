package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseQuery;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 利率风险对冲率表查询参数
 *
 * @author AI Assistant
 */
@Data
public class IrHedgeRatioQuery extends BaseQuery {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 账期，格式：YYYYMM（如202306）
     */
    @Pattern(regexp = "^\\d{6}$", message = "账期格式不正确，应为YYYYMM格式")
    @Size(max = 6, message = "账期长度不能超过6个字符")
    private String accountingPeriod;

    /**
     * 项目名称，如：利率风险资产敏感度、利率风险负债敏感度、利率风险对冲率
     */
    @Size(max = 100, message = "项目名称长度不能超过100个字符")
    private String itemName;

    /**
     * 账户编码，对应sys_dict_data表中dict_type='minc_account'的字典项
     */
    @Size(max = 10, message = "账户编码长度不能超过10个字符")
    private String accountCode;

    /**
     * 敏感度或对冲率，百分比格式，如：0.0793表示7.93%
     */
    private BigDecimal sensitivityRate;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel = 0;
}
