package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 分红方案表实体类
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DividendFundCostRateEntity extends BaseEntity {
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

    /** 是否中短：Y-是，N-否 */
    private String shortTermFlag;

    /** 定价保证成本率 */
    private BigDecimal guaranteedCostRate;

    /** 投资收益率假设 */
    private BigDecimal investmentReturnRate;

    /** 分红比例 */
    private BigDecimal dividendRatio;

    /** 资金成本率T0 */
    private BigDecimal fundCostRateT0;

    /** 资金成本率T1 */
    private BigDecimal fundCostRateT1;

    /** 资金成本率T2 */
    private BigDecimal fundCostRateT2;

    /** 资金成本率T3 */
    private BigDecimal fundCostRateT3;

    /** 是否删除，0:否，1:是 */
    private Integer isDel;
}
