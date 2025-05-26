package com.xl.alm.job.minc.entity;

import com.xl.alm.job.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 最低资本明细汇总数据计算实体类
 * 对应数据库表：t_minc_min_capital_summary
 * 用于UC0006：计算最低资本明细汇总数据
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MinCapitalSummaryCalculationEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 账期，格式：YYYYMM（如202306）
     */
    private String accountingPeriod;

    /**
     * 项目编码，关联项目定义表(t_minc_item_definition)的item_code
     */
    private String itemCode;

    /**
     * 账户编码，对应sys_dict_data表中dict_type='minc_account'的字典项
     */
    private String accountCode;

    /**
     * 账户金额（汇总后的金额）
     */
    private BigDecimal amount;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel;
}
