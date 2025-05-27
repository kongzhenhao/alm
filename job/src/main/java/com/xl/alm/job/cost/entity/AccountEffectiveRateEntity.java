package com.xl.alm.job.cost.entity;

import com.xl.alm.job.common.entity.BaseEntity;
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
public class AccountEffectiveRateEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 账期 */
    private String accountingPeriod;
    
    /** 设计类型 */
    private String designType;
    
    /** 有效成本率 */
    private BigDecimal effectiveCostRate;
    
    /** 现金流值集，格式：{"0":{"日期":"2023/06/30","值":"2100"},"1":{"日期":"2023/07/31","值":"2100"}} */
    private String cashFlowSet;
    
    /** 是否删除，0:否，1:是 */
    private Integer isDel;
}
