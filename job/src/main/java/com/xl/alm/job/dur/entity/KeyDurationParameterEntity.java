package com.xl.alm.job.dur.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xl.alm.job.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 关键久期参数表实体类
 *
 * @author AI
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("t_dur_key_duration_parameter")
public class KeyDurationParameterEntity extends BaseEntity {
    @TableField("id")
    private Long id;

    /**
     * 账期，格式YYYYMM
     */
    @TableField("account_period")
    private String accountPeriod;

    /**
     * 关键期限点
     */
    @TableField("key_duration")
    private String keyDuration;

    /**
     * 关键久期参数值集,分为1273项,格式{"0":{"date":"2025-01-01","value":0.25},"1":{"date":"2025-01-02","value":0.35},...,"1272":{"date":"2025-12-01","value":0.15}}
     */
    @TableField("parameter_val_set")
    private String parameterValSet;

    /**
     * 是否删除，0：否，1：是
     */
    @TableField("is_del")
    private int isDel = 0;
}
