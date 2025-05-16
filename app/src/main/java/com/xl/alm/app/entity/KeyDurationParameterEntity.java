package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 关键久期参数表实体类
 *
 * @author AI Assistant
 */
@Data
public class KeyDurationParameterEntity extends BaseEntity {
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
     * 关键期限点
     */
    private String keyDuration;

    /**
     * 关键久期参数值集,分为1273项,格式{"0":{"date":"2025-01-01","value":0.25},"1":{"date":"2025-01-02","value":0.35},...,"1272":{"date":"2025-12-01","value":0.15}}
     */
    private String parameterValSet;

    /**
     * 是否删除，0：否，1：是
     */
    private Integer isDel;
}
