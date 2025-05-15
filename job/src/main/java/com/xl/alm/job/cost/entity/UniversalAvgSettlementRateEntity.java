package com.xl.alm.job.cost.entity;

import com.xl.alm.job.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 万能平均结算利率表实体类
 *
 * @author AI Assistant
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UniversalAvgSettlementRateEntity extends BaseEntity {
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
    
    /** 平均结算利率T0 */
    private BigDecimal avgSettlementRateT0;
    
    /** 平均结算利率T1 */
    private BigDecimal avgSettlementRateT1;
    
    /** 平均结算利率T2 */
    private BigDecimal avgSettlementRateT2;
    
    /** 平均结算利率T3 */
    private BigDecimal avgSettlementRateT3;
    
    /** 是否删除，0:否，1:是 */
    private Integer isDel;
}
