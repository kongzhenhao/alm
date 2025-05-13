package com.xl.alm.job.dur.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xl.alm.job.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 折现曲线表实体类
 *
 * @author AI Assistant
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_dur_discount_curve")
public class DiscountCurveEntity extends BaseEntity {
    @TableField("id")
    private Long id;

    /**
     * 账期，格式YYYYMM
     */
    @TableField("account_period")
    private String accountPeriod;

    /**
     * 曲线类型,01:低档,02:中档
     */
    @TableField("curve_type")
    private String curveType;

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
     * 曲线值集,分为1273项,格式{"0":{"date":"2025-01-01","value":0.25},"1":{"date":"2025-01-02","value":0.35},...,"1272":{"date":"2025-12-01","value":0.15}}
     */
    @TableField("curve_val_set")
    private String curveValSet;

    /**
     * 是否删除，0：否，1：是
     */
    @TableField("is_del")
    private int isDel = 0;
}
