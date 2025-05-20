package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseQuery;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 保单续保率统计Query
 *
 * @author AI Assistant
 */
@Data
public class RenewalRateStatsQuery extends BaseQuery {
    private Long id;
    
    /**
     * 账期，格式YYYYMM
     */
    private String accountingPeriod;

    /**
     * 当季第几月
     */
    private Integer monthSeqInCurrQuarter;

    /**
     * 渠道类型编码
     */
    private String channelTypeCode;

    /**
     * 上年同比月份,格式YYYYMM
     */
    private String monthOfLastYear;

    /**
     * 上年度有效保单数量
     */
    private Integer validPolicyCntLastYear;

    /**
     * 本年度有效保单数量
     */
    private Integer validPolicyCntCurrYear;

    /**
     * 保单续保率
     */
    private BigDecimal policyRenewalRate;
    
    /**
     * 是否删除，0：否，1：是
     */
    private Integer isDel;
}
