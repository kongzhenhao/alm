package com.xl.alm.job.minc.entity;

import com.xl.alm.job.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 边际最低资本贡献率计算实体类
 * 对应数据库表：t_minc_marginal_capital
 * 用于UC0007：计算边际最低资本贡献率数据
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MarginalCapitalCalculationEntity extends BaseEntity {
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
     * 再保后金额，数值类型，保留2位小数
     */
    private BigDecimal reinsuAfterAmount;

    /**
     * 子风险层面边际最低资本贡献因子，百分比格式，如：0.583表示58.3%
     */
    private BigDecimal subRiskMarginalFactor;

    /**
     * 公司层面边际最低资本贡献因子，百分比格式，如：0.583表示58.3%
     */
    private BigDecimal companyMarginalFactor;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel;
}
