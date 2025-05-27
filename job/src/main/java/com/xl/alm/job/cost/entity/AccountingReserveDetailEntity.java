package com.xl.alm.job.cost.entity;

import com.xl.alm.job.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 会计准备金明细表实体类
 *
 * @author AI Assistant
 */
@EqualsAndHashCode(callSuper = true)
@Data
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

    /** 豁免准备金 */
    private BigDecimal waiverReserve;

    /** 持续奖准备金 */
    private BigDecimal persistenceBonusReserve;

    /** 长期未到期责任准备金 */
    private BigDecimal longTermUnearned;

    /** 短期未到期责任准备金 */
    private BigDecimal shortTermUnearned;

    /** 未到期责任准备金 */
    private BigDecimal unearnedPremiumReserve;

    /** 已报未决 */
    private BigDecimal reportedUnpaid;

    /** 未报未决 */
    private BigDecimal incurredUnreported;

    /** 理赔费用准备金 */
    private BigDecimal claimExpenseReserve;

    /** 未决赔款准备金 */
    private BigDecimal outstandingClaimReserve;

    /** 会计准备金合计 */
    private BigDecimal totalAccountingReserve;

    /** 应收分保未到期责任准备金 */
    private BigDecimal reinsuranceUnearned;

    /** 应收分保已报未决 */
    private BigDecimal reinsuranceReported;

    /** 应收分保未报未决 */
    private BigDecimal reinsuranceUnreported;

    /** 应收分保未决合计 */
    private BigDecimal reinsuranceClaimTotal;

    /** 应收分保合计 */
    private BigDecimal reinsuranceTotal;

    /** 失效保单现价 */
    private BigDecimal lapsedPolicyValue;

    /** 零头月红利 */
    private BigDecimal fractionalMonthDividend;

    /** 应付未付红利 */
    private BigDecimal unpaidDividend;

    /** 是否删除，0:否，1:是 */
    private Integer isDel;
}
