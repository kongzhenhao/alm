package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分账户汇总表查询对象
 *
 * @author AI Assistant
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CostAccountSummaryQuery extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 情景名称 */
    private String scenarioName;

    /** 账期 */
    private String accountingPeriod;

    /** 设计类型 */
    private String designType;
}
