package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseQuery;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 分部门最低资本明细表Query
 *
 * @author AI Assistant
 */
@Data
public class DeptMincapDetailQuery extends BaseQuery {
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
     * 统计部门名称
     */
    private String department;

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
    private Integer isDel = 0;
}
