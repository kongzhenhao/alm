package com.xl.alm.job.cost.entity;

import com.xl.alm.job.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 法定准备金明细表实体类
 *
 * @author AI Assistant
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StatutoryReserveDetailEntity extends BaseEntity {
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
    
    /** 设计类型 */
    private String designType;
    
    /** 是否中短：Y-是，N-否 */
    private String shortTermFlag;
    
    /** 账户价值 */
    private BigDecimal accountValue;
    
    /** 法定准备金合计 */
    private BigDecimal totalStatutoryReserve;
    
    /** 是否删除，0:否，1:是 */
    private Integer isDel;
}
