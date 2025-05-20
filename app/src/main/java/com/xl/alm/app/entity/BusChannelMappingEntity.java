package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 渠道码映射配置表实体类
 *
 * @author AI Assistant
 */
@Data
public class BusChannelMappingEntity extends BaseEntity {
    private Long id;
    
    /**
     * 业务类型编码
     */
    private String busTypeCode;
    
    /**
     * 渠道类型编码
     */
    private String channelTypeCode;
    
    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel = 0;
}
