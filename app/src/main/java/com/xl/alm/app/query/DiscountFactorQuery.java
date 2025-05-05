package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseQuery;
import lombok.Data;

/**
 * 折现因子Query
 *
 * @author AI Assistant
 */
@Data
public class DiscountFactorQuery extends BaseQuery {
    private Long id;
    
    /**
     * 账期，格式YYYYMM
     */
    private String accountPeriod;

    /**
     * 因子类型,01:中档,02:低档
     */
    private String factorType;

    /**
     * 基点类型,01:+50bp,02:-50bp
     */
    private String bpType;

    /**
     * 久期类型,01:修正久期,02:有效久期
     */
    private String durationType;

    /**
     * 是否删除，0：否，1：是
     */
    private Integer isDel = 0;
}
