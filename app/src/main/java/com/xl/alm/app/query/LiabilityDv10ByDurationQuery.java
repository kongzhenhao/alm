package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseQuery;
import lombok.Data;

/**
 * 分中短负债基点价值DV10表查询对象
 *
 * @author AI Assistant
 */
@Data
public class LiabilityDv10ByDurationQuery extends BaseQuery {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 账期,格式YYYYMM */
    private String accountPeriod;

    /** 现金流类型,01:流入,02:流出 */
    private String cashFlowType;

    /** 设计类型 */
    private String designType;

    /** 是否为中短期险种,Y:是,N:否 */
    private String shortTermFlag;

    /** 现值类型,01:上升,02:下降,03:净值 */
    private String valueType;

    /** 是否删除，0：否，1：是 */
    private Integer isDel = 0;
}
