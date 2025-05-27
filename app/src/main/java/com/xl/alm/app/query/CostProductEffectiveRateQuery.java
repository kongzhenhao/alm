package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分产品有效成本率表查询对象
 *
 * @author AI Assistant
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CostProductEffectiveRateQuery extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 账期 */
    private String accountingPeriod;

    /** 精算代码 */
    private String actuarialCode;

    /** 设计类型 */
    private String designType;

    /** 是否中短：Y-是，N-否 */
    private String shortTermFlag;

    /** 业务代码 */
    private String businessCode;

    /** 产品名称 */
    private String productName;
}
