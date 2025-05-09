package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 分账户负债现金流现值汇总表实体类
 *
 * @author AI Assistant
 */
@Data
public class SubAccountLiabilityPresentValueEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 账期，格式YYYYMM */
    private String accountPeriod;

    /** 现金流类型,01:流入,02:流出 */
    private String cashFlowType;

    /** 基点类型,01:+50bp,02:-50bp */
    private String bpType;

    /** 久期类型,01:修正久期,02:有效久期 */
    private String durationType;

    /** 设计类型 */
    private String designType;

    /**
     * 现金流现值值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}
     */
    private String presentCashValSet;

    /** 是否删除，0：否，1：是 */
    private Integer isDel = 0;
}
