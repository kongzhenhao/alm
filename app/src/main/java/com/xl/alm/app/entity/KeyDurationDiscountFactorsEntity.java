package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 关键久期折现因子表实体类
 *
 * @author AI Assistant
 */
@Data
public class KeyDurationDiscountFactorsEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 账期,格式YYYYMM */
    private String accountPeriod;

    /** 曲线类型,01:中档,02:低档 */
    private String curveType;

    /** 关键期限点 */
    private String keyDuration;

    /** 压力方向,01:上升,02:下降 */
    private String stressDirection;

    /** 久期类型,01:修正久期,02:有效久期,03:关键久期 */
    private String durationType;

    /** 因子值集,分为1273项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1273":{"date":"2025-12-01","val":0.15}} */
    private String factorValSet;

    /** 是否删除，0：否，1：是 */
    private Integer isDel = 0;
}
