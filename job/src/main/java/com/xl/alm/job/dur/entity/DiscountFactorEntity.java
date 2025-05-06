package com.xl.alm.job.dur.entity;

import com.xl.alm.job.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 折现因子表实体类
 *
 * @author AI Assistant
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DiscountFactorEntity extends BaseEntity {
    private Long id;
    
    /**
     * 账期，格式YYYYMM
     */
    private String accountPeriod;

    /**
     * 因子类型,01:低档,02:中档
     */
    private String factorType;

    /**
     * 基点类型,01:+50bp,02:-50bp,03:0bp
     */
    private String bpType;

    /**
     * 久期类型,01:修正久期,02:有效久期
     */
    private String durationType;

    /**
     * 因子值集,分为1273项,格式{"0":{"date":"2025-01-01","value":0.25},"1":{"date":"2025-01-02","value":0.35},...,"1272":{"date":"2025-12-01","value":0.15}}
     */
    private String factorValSet;
    
    /**
     * 是否删除，0：否，1：是
     */
    private int isDel = 0;
}
