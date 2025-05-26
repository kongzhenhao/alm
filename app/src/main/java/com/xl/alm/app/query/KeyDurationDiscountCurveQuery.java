package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseQuery;
import lombok.Data;

/**
 * 关键久期折现曲线表查询对象
 *
 * @author AI Assistant
 */
@Data
public class KeyDurationDiscountCurveQuery extends BaseQuery {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 账期,格式YYYYMM
     */
    private String accountPeriod;

    /**
     * 曲线类型,01:中档,02:低档
     */
    private String curveType;

    /**
     * 关键期限点
     */
    private String keyDuration;

    /**
     * 压力方向,01:上升,02:下降
     */
    private String stressDirection;

    /**
     * 久期类型,01:修正久期,02:有效久期,03:关键久期
     */
    private String durationType;
}
