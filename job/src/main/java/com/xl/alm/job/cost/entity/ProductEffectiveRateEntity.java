package com.xl.alm.job.cost.entity;

import com.xl.alm.job.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 分产品有效成本率表实体类
 *
 * @author AI Assistant
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductEffectiveRateEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

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

    /** 是否中短：Y-是，N-否 */
    private String shortTermFlag;

    // 账面价值 - 在实际表结构中不存在，但在代码中需要使用
    private BigDecimal bookValue;

    /** 有效成本率 */
    private BigDecimal effectiveRate;

    /** 现金流值集，格式：{"0":账面价值,"1":第1期现金流,"2":第2期现金流,...,"1272":第1272期现金流} */
    private String cashflowSet;

    /** 是否删除，0:否，1:是 */
    private Integer isDel;
}
