package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 相关系数表实体类
 * 对应数据库表：t_minc_correlation_coef
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CorrelationCoefEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 账期，格式：YYYYMM（如202306）
     */
    private String accountingPeriod;

    /**
     * 第一个风险项目编码，关联项目定义表(t_minc_item_definition)的item_code
     */
    private String itemCodeX;

    /**
     * 第二个风险项目编码，关联项目定义表(t_minc_item_definition)的item_code
     */
    private String itemCodeY;

    /**
     * 存储相关系数，范围[-1,1]
     */
    private BigDecimal correlationValue;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel;
}
