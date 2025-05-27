package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 分产品有效成本率表实体类
 *
 * @author AI Assistant
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CostProductEffectiveRateEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 账期 */
    private String accountingPeriod;

    /** 精算代码 */
    private String actuarialCode;

    /** 设计类型 */
    private String designType;

    /** 是否中短：Y-是，N-否 */
    private String shortTermFlag;

    /** 业务代码 */
    private String businessCode;

    /** 产品名称 */
    private String productName;

    /** 产品有效成本率 */
    private BigDecimal effectiveCostRate;

    /** 现金流值集，JSON格式存储现金流数据 */
    private String cashFlowSet;

    /** 是否删除，0:否，1:是 */
    private Integer isDel;
}
