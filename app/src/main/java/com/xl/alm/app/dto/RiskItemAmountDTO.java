package com.xl.alm.app.dto;

import com.jd.lightning.common.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 风险项目金额表数据传输对象
 *
 * @author AI Assistant
 */
@Data
public class RiskItemAmountDTO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 账期，格式：YYYYMM（如202306）
     */
    @NotBlank(message = "账期不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "账期格式必须为YYYYMM")
    @Excel(name = "账期")
    private String accountingPeriod;

    /**
     * 项目编码，对应sys_dict_data表中dict_type='minc_risk_item'的字典项
     */
    @NotBlank(message = "项目编码不能为空")
    @Size(max = 20, message = "项目编码长度不能超过20个字符")
    @Excel(name = "项目编码")
    private String itemCode;

    /**
     * 项目名称（非数据库字段，用于展示）
     */
    @Excel(name = "项目名称")
    private String itemName;

    /**
     * S05-最低资本表的期末金额
     */
    @Excel(name = "S05-最低资本表的期末金额", cellType = Excel.ColumnType.NUMERIC, scale = 10)
    private BigDecimal s05Amount;

    /**
     * IR05-寿险业务保险风险表的期末金额
     */
    @Excel(name = "IR05-寿险业务保险风险表的期末金额", cellType = Excel.ColumnType.NUMERIC, scale = 10)
    private BigDecimal ir05Amount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新者
     */
    private String updateBy;
}
