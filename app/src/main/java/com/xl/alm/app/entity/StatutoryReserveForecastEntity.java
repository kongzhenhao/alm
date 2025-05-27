package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 法定准备金预测表实体类
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatutoryReserveForecastEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 业务类型 */
    private String businessType;

    /** 账期 */
    private String accountingPeriod;

    /** 精算代码 */
    private String actuarialCode;

    /** 业务代码 */
    private String businessCode;

    /** 产品名称 */
    private String productName;

    /** 设计类型 */
    private String designType;

    /** 长短期标识：L-长期，S-短期 */
    private String termType;

    /** 是否中短：Y-是，N-否 */
    private String shortTermFlag;

    /** 法定准备金T1 */
    private BigDecimal statutoryReserveT1;

    /** 法定准备金T2 */
    private BigDecimal statutoryReserveT2;

    /** 法定准备金T3 */
    private BigDecimal statutoryReserveT3;

    /** 是否删除，0:否，1:是 */
    private Integer isDel;
}
