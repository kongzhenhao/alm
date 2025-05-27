package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分产品统计表查询对象
 *
 * @author AI Assistant
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CostProductStatisticsQuery extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 情景名称 */
    private String scenarioName;

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

    /** 长短期标识：L-长期，S-短期 */
    private String termType;

    /** 设计类型 */
    private String designType;

    /** 是否中短：Y-是，N-否 */
    private String shortTermFlag;
}
