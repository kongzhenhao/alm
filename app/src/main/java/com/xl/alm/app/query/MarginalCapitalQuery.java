package com.xl.alm.app.query;

import lombok.Data;

/**
 * 边际最低资本贡献率表查询对象
 *
 * @author AI Assistant
 */
@Data
public class MarginalCapitalQuery {

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
     * 是否删除，0:否，1:是
     */
    private Integer isDel;
}
