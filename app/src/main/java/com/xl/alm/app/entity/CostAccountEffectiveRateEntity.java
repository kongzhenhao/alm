package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 分账户有效成本率表实体类
 *
 * @author AI Assistant
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CostAccountEffectiveRateEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 账期 */
    private String accountingPeriod;

    /** 设计类型 */
    private String designType;

    /** 账户有效成本率 */
    private BigDecimal effectiveCostRate;

    /** 现金流值集，JSON格式存储现金流数据 */
    private String cashFlowSet;

    /** 是否删除，0:否，1:是 */
    private Integer isDel;
}
