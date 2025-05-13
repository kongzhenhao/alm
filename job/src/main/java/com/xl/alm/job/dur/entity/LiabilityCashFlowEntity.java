package com.xl.alm.job.dur.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xl.alm.job.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 负债现金流表实体类
 *
 * @author AI Assistant
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_dur_liability_cash_flow")
public class LiabilityCashFlowEntity extends BaseEntity {
    @TableField("id")
    private Long id;

    /**
     * 账期，格式YYYYMM
     */
    @TableField("account_period")
    private String accountPeriod;

    /**
     * 现金流类型,01:流入,02:流出
     */
    @TableField("cash_flow_type")
    private String cashFlowType;

    /**
     * 基点类型,01:+50bp,02:-50bp,03:0bp
     */
    @TableField("bp_type")
    private String bpType;

    /**
     * 久期类型,01:修正久期,02:有效久期
     */
    @TableField("duration_type")
    private String durationType;

    /**
     * 设计类型
     */
    @TableField("design_type")
    private String designType;

    /**
     * 是否为中短期险种
     */
    @TableField("is_short_term")
    private String isShortTerm;

    /**
     * 精算代码
     */
    @TableField("actuarial_code")
    private String actuarialCode;

    /**
     * 业务代码
     */
    @TableField("business_code")
    private String businessCode;

    /**
     * 产品名称
     */
    @TableField("product_name")
    private String productName;

    /**
     * 险种主类
     */
    @TableField("insurance_main_type")
    private String insuranceMainType;

    /**
     * 险种细类
     */
    @TableField("insurance_sub_type")
    private String insuranceSubType;

    /**
     * 现金流值集,分为1273项,格式{"0":{"date":"2025-01-01","value":0.25},"1":{"date":"2025-01-02","value":0.35},...,"1272":{"date":"2025-12-01","value":0.15}}
     */
    @TableField("cash_flow_val_set")
    private String cashFlowValSet;

    /**
     * 是否删除，0：否，1：是
     */
    @TableField("is_del")
    private int isDel = 0;
}
