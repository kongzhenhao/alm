package com.xl.alm.app.entity;

import com.jd.lightning.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 项目定义表实体类
 *
 * @author AI Assistant
 */
@Data
public class ItemDefinitionEntity extends BaseEntity {
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
     * 子风险最低资本因子计算公式
     */
    private String subRiskFactorFormula;

    /**
     * 公司整体最低资本因子计算公式
     */
    private String companyFactorFormula;

    /**
     * 最低资本计算公式
     */
    private String capitalCalculationFormula;

    /**
     * 状态：1-有效，0-无效
     */
    private String status;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel;
}
