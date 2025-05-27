package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 分产品统计表实体类
 *
 * @author AI Assistant
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CostProductStatisticsEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 情景名称 */
    private String scenarioName;

    /** 业务类型 */
    private String businessType;

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

    /** 设计类型 */
    private String designType;

    /** 是否中短：Y-是，N-否 */
    private String shortTermFlag;

    /** 法定准备金T0 */
    private BigDecimal statutoryReserveT0;

    /** 法定准备金T1 */
    private BigDecimal statutoryReserveT1;

    /** 法定准备金T2 */
    private BigDecimal statutoryReserveT2;

    /** 法定准备金T3 */
    private BigDecimal statutoryReserveT3;

    /** 资金成本率T0 */
    private BigDecimal fundCostRateT0;

    /** 资金成本率T1 */
    private BigDecimal fundCostRateT1;

    /** 资金成本率T2 */
    private BigDecimal fundCostRateT2;

    /** 资金成本率T3 */
    private BigDecimal fundCostRateT3;

    /** 保证成本率T0 */
    private BigDecimal guaranteedCostRateT0;

    /** 保证成本率T1 */
    private BigDecimal guaranteedCostRateT1;

    /** 保证成本率T2 */
    private BigDecimal guaranteedCostRateT2;

    /** 保证成本率T3 */
    private BigDecimal guaranteedCostRateT3;

    /** 是否删除，0:否，1:是 */
    private Integer isDel;
}
