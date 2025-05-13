package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 法定准备金明细表实体类
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = true)
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

    /** 有效保单件数 */
    private Integer validPolicyCount;

    /** 存量累计规模保费 */
    private BigDecimal accumulatedPremium;

    /** 账户价值 */
    private BigDecimal accountValue;

    /** 法定/非单位准备金 */
    private BigDecimal statutoryReserve;

    /** 未到期责任准备金 */
    private BigDecimal unearnedPremiumReserve;

    /** 未决赔款准备金 */
    private BigDecimal outstandingClaimReserve;

    /** 法定准备金合计 */
    private BigDecimal totalStatutoryReserve;

    /** 是否删除，0:否，1:是 */
    private Integer isDel;
}
