package com.xl.alm.app.dto;

import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 渠道码映射配置DTO
 *
 * @author AI Assistant
 */
@Data
public class BusChannelMappingDTO extends BaseDTO {
    private Long id;
    
    /**
     * 业务类型编码
     */
    @NotBlank(message = "业务类型编码不能为空")
    @Size(max = 20, message = "业务类型编码长度不能超过20个字符")
    @Excel(name = "业务类型编码")
    private String busTypeCode;
    
    /**
     * 渠道类型编码
     */
    @NotBlank(message = "渠道类型编码不能为空")
    @Size(max = 20, message = "渠道类型编码长度不能超过20个字符")
    @Excel(name = "渠道类型编码")
    private String channelTypeCode;
    
    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel = 0;
}
