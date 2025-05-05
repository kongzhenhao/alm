package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 负债现金流表实体类
 *
 * @author AI Assistant
 */
@Data
public class LiabilityCashFlowEntity extends BaseEntity {
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
     * 基点类型,01:+50bp,02:-50bp
     */
    private String bpType;

    /**
     * 久期类型,01:修正久期,02:有效久期
     */
    private String durationType;

    /**
     * 设计类型
     */
    private String designType;

    /**
     * 是否为中短期险种
     */
    private String isShortTerm;

    /**
     * 精算代码
     */
    private String actuarialCode;

    /**
     * 业务代码
     */
    private String businessCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 险种主类
     */
    private String insuranceMainType;

    /**
     * 险种细类
     */
    private String insuranceSubType;

    /**
     * 现金流值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}
     */
    private String cashValSet;

    /**
     * 是否删除，0：否，1：是
     */
    private int isDel = 0;
}
