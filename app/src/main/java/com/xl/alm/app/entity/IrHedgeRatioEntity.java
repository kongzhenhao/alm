package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 利率风险对冲率表实体类
 *
 * @author AI Assistant
 */
@Data
public class IrHedgeRatioEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 账期，格式：YYYYMM（如202306）
     */
    private String accountingPeriod;

    /**
     * 项目名称，如：利率风险资产敏感度、利率风险负债敏感度、利率风险对冲率
     */
    private String itemName;

    /**
     * 账户编码，对应sys_dict_data表中dict_type='minc_account'的字典项
     */
    private String accountCode;

    /**
     * 敏感度或对冲率，百分比格式，如：0.0793表示7.93%
     */
    private BigDecimal sensitivityRate;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel;
}
