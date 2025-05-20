package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 续保率评估月份配置表实体类
 *
 * @author AI Assistant
 */
@Data
public class RenewalEvalMonthCfgEntity extends BaseEntity {
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
