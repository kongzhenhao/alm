package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 分中短负债基点价值DV10表实体类
 *
 * @author AI Assistant
 */
@Data
public class LiabilityDv10ByDurationEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 账期,格式YYYYMM */
    private String accountPeriod;

    /** 现金流类型,01:流入,02:流出 */
    private String cashFlowType;

    /** 设计类型 */
    private String designType;

    /** 是否为中短期险种,Y:是,N:否 */
    private String shortTermFlag;

    /** 现值类型,01:上升,02:下降,03:净值 */
    private String valueType;

    /** 0年期限点的DV10现值 */
    private BigDecimal term0;

    /** 0.5年期限点的DV10现值 */
    private BigDecimal term05;

    /** 1年期限点的DV10现值 */
    private BigDecimal term1;

    /** 2年期限点的DV10现值 */
    private BigDecimal term2;

    /** 3年期限点的DV10现值 */
    private BigDecimal term3;

    /** 4年期限点的DV10现值 */
    private BigDecimal term4;

    /** 5年期限点的DV10现值 */
    private BigDecimal term5;

    /** 6年期限点的DV10现值 */
    private BigDecimal term6;

    /** 7年期限点的DV10现值 */
    private BigDecimal term7;

    /** 8年期限点的DV10现值 */
    private BigDecimal term8;

    /** 10年期限点的DV10现值 */
    private BigDecimal term10;

    /** 12年期限点的DV10现值 */
    private BigDecimal term12;

    /** 15年期限点的DV10现值 */
    private BigDecimal term15;

    /** 20年期限点的DV10现值 */
    private BigDecimal term20;

    /** 25年期限点的DV10现值 */
    private BigDecimal term25;

    /** 30年期限点的DV10现值 */
    private BigDecimal term30;

    /** 35年期限点的DV10现值 */
    private BigDecimal term35;

    /** 40年期限点的DV10现值 */
    private BigDecimal term40;

    /** 45年期限点的DV10现值 */
    private BigDecimal term45;

    /** 50年期限点的DV10现值 */
    private BigDecimal term50;

    /** 是否删除，0：否，1：是 */
    private Integer isDel;
}
