package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 市场及信用最低资本表对象 t_minc_min_capital_by_account
 *
 * @author alm
 * @date 2024-01-01
 */
@Data
public class MinCapitalByAccountEntity extends BaseEntity {
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
     * 账户金额
     */
    private BigDecimal amount;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel;
}
