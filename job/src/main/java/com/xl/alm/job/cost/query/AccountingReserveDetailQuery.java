package com.xl.alm.job.cost.query;

import lombok.Data;

/**
 * 会计准备金明细查询条件
 *
 * @author AI Assistant
 */
@Data
public class AccountingReserveDetailQuery {
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
