package com.xl.alm.job.minc.entity;

import com.xl.alm.job.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 市场及信用最低资本表实体类
 * 对应表：t_minc_min_capital_by_account (TB0008)
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MarketCreditCapitalEntity extends BaseEntity {

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
     * 账户金额
     */
    private BigDecimal amount;
}
