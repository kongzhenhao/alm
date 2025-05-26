package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 风险项目金额表查询对象
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RiskItemAmountQuery extends BaseEntity {
    /**
     * 账期，格式：YYYYMM（如202306）
     */
    @Pattern(regexp = "^\\d{6}$", message = "账期格式必须为YYYYMM")
    private String accountingPeriod;

    /**
     * 项目编码，对应sys_dict_data表中dict_type='minc_risk_item'的字典项
     */
    @Size(max = 20, message = "项目编码长度不能超过20个字符")
    private String itemCode;

    /**
     * 项目名称（用于模糊搜索）
     */
    @Size(max = 100, message = "项目名称长度不能超过100个字符")
    private String itemName;
}
