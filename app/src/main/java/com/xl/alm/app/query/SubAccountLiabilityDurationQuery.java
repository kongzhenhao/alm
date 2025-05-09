package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分账户负债久期汇总表 查询对象
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SubAccountLiabilityDurationQuery extends BaseQuery {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 账期，格式YYYYMM */
    private String accountPeriod;

    /** 现金流类型,01:流入,02:流出 */
    private String cashFlowType;

    /** 基点类型,01:+50bp,02:-50bp */
    private String bpType;

    /** 久期类型,01:修正久期,02:有效久期 */
    private String durationType;

    /** 设计类型 */
    private String designType;

    /** 是否删除，0：否，1：是 */
    private Integer isDel = 0;
}
