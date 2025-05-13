package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 产品属性表实体类
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductAttributeEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 账期 */
    private String accountingPeriod;

    /** 精算代码 */
    private String actuarialCode;

    /** 业务代码 */
    private String businessCode;

    /** 产品名称 */
    private String productName;

    /** 长短期标识：L-长期，S-短期 */
    private String termType;

    /** 险种主类 */
    private String insuranceMainType;

    /** 险种细类 */
    private String insuranceSubType;

    /** 设计类型 */
    private String designType;

    /** 是否中短：Y-是，N-否 */
    private String shortTermFlag;

    /** 报监管中短标识：Y-是，N-否 */
    private String regMidId;

    /** 定价保证成本率 */
    private BigDecimal guaranteedCostRate;

    /** 子账户 */
    private String subAccount;

    /** 新业务标识：Y-是，N-否 */
    private String newBusinessFlag;

    /** 备注 */
    private String remark;

    /** 是否删除，0:否，1:是 */
    private Integer isDel;
}
