package com.xl.alm.job.cost.query;

import lombok.Data;

/**
 * 法定准备金预测表查询条件
 *
 * @author AI Assistant
 */
@Data
public class StatutoryReserveForecastQuery {
    /** 账期 */
    private String accountingPeriod;
}
