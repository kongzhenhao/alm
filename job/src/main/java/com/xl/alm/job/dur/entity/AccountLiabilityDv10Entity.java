package com.xl.alm.job.dur.entity;

import com.xl.alm.job.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 分账户负债基点价值DV10表实体类
 *
 * @author AI
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountLiabilityDv10Entity extends BaseEntity {
    private Long id;

    /**
     * 账期，格式YYYYMM
     */
    private String accountPeriod;

    /**
     * 现金流类型,01:流入,02:流出
     */
    private String cashFlowType;

    /**
     * 设计类型
     */
    private String designType;

    /**
     * 现值类型,01:上升,02:下降,03:净值
     */
    private String valueType;

    /**
     * 0年期限点的DV10现值
     */
    private BigDecimal term0;

    /**
     * 0.5年期限点的DV10现值
     */
    private BigDecimal term05;

    /**
     * 1年期限点的DV10现值
     */
    private BigDecimal term1;

    /**
     * 2年期限点的DV10现值
     */
    private BigDecimal term2;

    /**
     * 3年期限点的DV10现值
     */
    private BigDecimal term3;

    /**
     * 4年期限点的DV10现值
     */
    private BigDecimal term4;

    /**
     * 5年期限点的DV10现值
     */
    private BigDecimal term5;

    /**
     * 6年期限点的DV10现值
     */
    private BigDecimal term6;

    /**
     * 7年期限点的DV10现值
     */
    private BigDecimal term7;

    /**
     * 8年期限点的DV10现值
     */
    private BigDecimal term8;

    /**
     * 10年期限点的DV10现值
     */
    private BigDecimal term10;

    /**
     * 12年期限点的DV10现值
     */
    private BigDecimal term12;

    /**
     * 15年期限点的DV10现值
     */
    private BigDecimal term15;

    /**
     * 20年期限点的DV10现值
     */
    private BigDecimal term20;

    /**
     * 25年期限点的DV10现值
     */
    private BigDecimal term25;

    /**
     * 30年期限点的DV10现值
     */
    private BigDecimal term30;

    /**
     * 35年期限点的DV10现值
     */
    private BigDecimal term35;

    /**
     * 40年期限点的DV10现值
     */
    private BigDecimal term40;

    /**
     * 45年期限点的DV10现值
     */
    private BigDecimal term45;

    /**
     * 50年期限点的DV10现值
     */
    private BigDecimal term50;

    /**
     * 是否删除，0：否，1：是
     */
    private Integer isDel = 0;
}
