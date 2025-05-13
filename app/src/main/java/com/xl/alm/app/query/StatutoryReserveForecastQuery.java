package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 法定准备金预测查询条件
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatutoryReserveForecastQuery extends BaseQuery {
    /** 主键ID */
    private Long id;

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

    /** 设计类型 */
    private String designType;

    /** 长短期标识：L-长期，S-短期 */
    private String termType;

    /** 是否中短：Y-是，N-否 */
    private String shortTermFlag;

    /** 是否删除，0:否，1:是 */
    private Integer isDel = 0;
}
