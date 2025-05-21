package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseQuery;
import lombok.Data;

/**
 * 续保率评估月份配置Query
 *
 * @author AI Assistant
 */
@Data
public class RenewalEvalMonthCfgQuery extends BaseQuery {
    private Long id;
    
    /**
     * 账期,格式YYYYMM
     */
    private String accountingPeriod;
    
    /**
     * 当季第几月
     */
    private Integer monthSeqInCurrQuarter;
    
    /**
     * 评估时点月份,格式YYYYMM
     */
    private String evalPointMonth;
    
    /**
     * 上年同比月份,格式YYYYMM
     */
    private String monthOfLastYear;
    
    /**
     * 当季月份,格式YYYYMM
     */
    private String monthInCurrQuarter;
    
    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel = 0;
}
