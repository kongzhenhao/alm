package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseQuery;
import lombok.Data;

/**
 * 负债久期汇总表 查询对象
 *
 * @author AI Assistant
 */
@Data
public class LiabilityDurationSummaryQuery extends BaseQuery {
    private static final long serialVersionUID = 1L;

    /** 账期，格式YYYYMM */
    private String accountPeriod;

    /** 现金流类型,01:流入,02:流出 */
    private String cashFlowType;

    /** 久期类型,01:修正久期,02:有效久期 */
    private String durationType;

    /** 设计类型 */
    private String designType;

    /** 是否为中短期险种 */
    private String isShortTerm;
}
