package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseQuery;
import lombok.Data;

import java.util.Date;

/**
 * 保单数据明细Query
 *
 * @author AI Assistant
 */
@Data
public class PolicyDetailQuery extends BaseQuery {
    private Long id;
    
    /**
     * 险种号
     */
    private String polNo;
    
    /**
     * 保单号
     */
    private String contNo;
    
    /**
     * 险种生效日
     */
    private Date effectiveDate;
    
    /**
     * 险种编码
     */
    private String riskCode;
    
    /**
     * 险种状态描述
     */
    private String polStatusDesc;
    
    /**
     * 险种状态变化时间
     */
    private Date polStatusChangedTime;
    
    /**
     * 设计类型
     */
    private String designType;

    /**
     * 业务类型编码
     */
    private String busTypeCode;
    
    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel = 0;
    
    /** 开始时间 */
    private String beginTime;

    /** 结束时间 */
    private String endTime;
}
