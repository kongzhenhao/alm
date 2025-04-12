package com.xl.alm.app.dto;

import lombok.Data;

import java.util.Date;

/**
 * 折现因子DTO
 *
 * @author AI Assistant
 */
@Data
public class SampleDTO {

    private Long id;

    /**
     * 账期，格式YYYYMM
     */
    private String accountPeriod;

    /**
     * 因子类型
     */
    private String factorType;

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
     * 因子值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}
     */
    private String factorValSet;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 是否删除，0：否，1：是
     */
    private int isDel = 0;
}