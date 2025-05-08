package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseQuery;
import lombok.Data;

/**
 * 折现曲线Query
 *
 * @author AI Assistant
 */
@Data
public class DiscountCurveQuery extends BaseQuery {
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
     * 是否删除，0：否，1：是
     */
    private Integer isDel = 0;
}
