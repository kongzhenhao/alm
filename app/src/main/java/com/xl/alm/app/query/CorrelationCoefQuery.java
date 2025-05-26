package com.xl.alm.app.query;

import lombok.Data;

/**
 * 相关系数表查询对象
 *
 * @author AI Assistant
 */
@Data
public class CorrelationCoefQuery {

    /**
     * 主键
     */
    private Long id;

    /**
     * 账期，格式：YYYYMM（如202306）
     */
    private String accountingPeriod;

    /**
     * 第一个风险项目编码，关联项目定义表(t_minc_item_definition)的item_code
     */
    private String itemCodeX;

    /**
     * 第二个风险项目编码，关联项目定义表(t_minc_item_definition)的item_code
     */
    private String itemCodeY;

    /**
     * 状态：1-有效，0-无效
     */
    private String status;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel;
}
