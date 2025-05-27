package com.xl.alm.job.cost.query;

import lombok.Data;

/**
 * 万能平均结算利率表查询条件
 *
 * @author AI Assistant
 */
@Data
public class UniversalAvgSettlementRateQuery {
    /** 账期 */
    private String accountingPeriod;
}
