package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 会计准备金明细表实体类
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountingReserveDetailEntity extends BaseEntity {
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

    /** 红利预提 */
    private BigDecimal dividendProvision;

    /** 最优估计 */
    private BigDecimal bestEstimate;

    /** 风险边际 */
    private BigDecimal riskMargin;

    /** 剩余边际 */
    private BigDecimal residualMargin;

    /** 未建模准备金 */
    private BigDecimal unmodeledReserve;

    /** 豪免准备金 */
    private BigDecimal waiverReserve;

    /** 持续奖准备金 */
    private BigDecimal persistenceBonusReserve;

    /** 长期险未到期 */
    private BigDecimal longTermUnearned;

    /** 短险未到期 */
    private BigDecimal shortTermUnearned;

    /** 未到期责任准备金 */
    private BigDecimal unearnedPremiumReserve;

    /** 未决赔款准备金 */
    private BigDecimal outstandingClaimReserve;

    /** 会计准备金合计 */
    private BigDecimal totalAccountingReserve;

    /** 是否删除，0:否，1:是 */
    private Integer isDel;
}
