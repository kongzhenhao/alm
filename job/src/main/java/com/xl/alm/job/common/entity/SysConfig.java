package com.xl.alm.job.common.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;



/**
 * 参数配置表 sys_config
 *
 * * @author lightning
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 参数主键 */
    private Long configId;

    /** 参数名称 */
    private String configName;

    /** 参数键名 */
    private String configKey;

    /** 参数键值 */
    private String configValue;

    /** 系统内置（Y是 N否） */
    private String configType;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("configId", getConfigId())
            .append("configName", getConfigName())
            .append("configKey", getConfigKey())
            .append("configValue", getConfigValue())
            .append("configType", getConfigType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
