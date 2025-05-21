package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 保单数据明细表实体类
 *
 * @author AI Assistant
 */
@Data
public class PolicyDetailEntity extends BaseEntity {
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
}
