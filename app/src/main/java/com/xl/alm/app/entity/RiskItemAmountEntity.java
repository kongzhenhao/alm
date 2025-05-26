package com.xl.alm.app.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 风险项目金额表实体类
 *
 * @author AI Assistant
 */
@Data
@TableName("t_minc_risk_item_amount")
public class RiskItemAmountEntity {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 账期，格式：YYYYMM（如202306）
     */
    @TableField("accounting_period")
    private String accountingPeriod;

    /**
     * 项目编码，对应sys_dict_data表中dict_type='minc_risk_item'的字典项
     */
    @TableField("item_code")
    private String itemCode;

    /**
     * S05-最低资本表的期末金额
     */
    @TableField("s05_amount")
    private BigDecimal s05Amount;

    /**
     * IR05-寿险业务保险风险表的期末金额
     */
    @TableField("ir05_amount")
    private BigDecimal ir05Amount;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 更新者
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 是否删除，0:否，1:是
     */
    @TableField("is_del")
    private Integer isDel;
}
