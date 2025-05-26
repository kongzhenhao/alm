package com.xl.alm.app.query;

import com.jd.lightning.common.core.domain.BaseQuery;
import lombok.Data;

/**
 * 项目定义表Query
 *
 * @author AI Assistant
 */
@Data
public class ItemDefinitionQuery extends BaseQuery {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 项目编码，如：MR001
     */
    private String itemCode;

    /**
     * 风险类型，如：市场风险、信用风险
     */
    private String riskType;

    /**
     * 边际最低资本贡献率表中的项目名称，如：寿险业务保险风险最低资本合计
     */
    private String capitalItem;

    /**
     * 相关系数表中的项目名称，如：寿险
     */
    private String correlationItem;

    /**
     * 指定该项目对应的上层级项目编码
     */
    private String parentItemCode;

    /**
     * 状态：1-有效，0-无效
     */
    private String status;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel = 0;
}
