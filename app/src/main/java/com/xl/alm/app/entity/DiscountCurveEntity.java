package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 折现曲线表实体类
 *
 * @author AI Assistant
 */
@Data
public class DiscountCurveEntity extends BaseEntity {
    private Long id;
    
    /**
     * 账期，格式YYYYMM
     */
    private String accountPeriod;

    /**
     * 曲线类型,01:中档,02:低档
     */
    private String curveType;

    /**
     * 基点类型,01:+50bp,02:-50bp
     */
    private String bpType;

    /**
     * 久期类型,01:修正久期,02:有效久期
     */
    private String durationType;

    /**
     * 曲线值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}
     */
    private String curveValSet;
    
    /**
     * 是否删除，0：否，1：是
     */
    private int isDel = 0;
}
