package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 项目定义表Excel操作DTO
 * 用于Excel导入导出，包含中文表头
 *
 * @author AI Assistant
 */
@Data
public class ItemDefinitionExportDTO {

    /**
     * 项目编码，如：MR001
     */
    @NotBlank(message = "项目编码不能为空")
    @Size(max = 20, message = "项目编码长度不能超过20个字符")
    @ExcelProperty("项目编码")
    private String itemCode;

    /**
     * 风险类型，如：市场风险、信用风险
     */
    @Size(max = 50, message = "风险类型长度不能超过50个字符")
    @ExcelProperty("风险类型")
    private String riskType;

    /**
     * 边际最低资本贡献率表中的项目名称，如：寿险业务保险风险最低资本合计
     */
    @Size(max = 100, message = "边际最低资本贡献率表中的项目名称长度不能超过100个字符")
    @ExcelProperty("边际最低资本贡献率表中的项目名称")
    private String capitalItem;

    /**
     * 相关系数表中的项目名称，如：寿险
     */
    @Size(max = 50, message = "相关系数表中的项目名称长度不能超过50个字符")
    @ExcelProperty("相关系数表中的项目名称")
    private String correlationItem;

    /**
     * 指定该项目对应的上层级项目编码
     */
    @Size(max = 20, message = "上层级项目编码长度不能超过20个字符")
    @ExcelProperty("上层级项目编码")
    private String parentItemCode;

    /**
     * 子风险最低资本因子计算公式
     */
    @Size(max = 500, message = "子风险最低资本因子计算公式长度不能超过500个字符")
    @ExcelProperty("子风险最低资本因子计算公式")
    private String subRiskFactorFormula;

    /**
     * 公司整体最低资本因子计算公式
     */
    @Size(max = 500, message = "公司整体最低资本因子计算公式长度不能超过500个字符")
    @ExcelProperty("公司整体最低资本因子计算公式")
    private String companyFactorFormula;

    /**
     * 最低资本计算公式
     */
    @Size(max = 500, message = "最低资本计算公式长度不能超过500个字符")
    @ExcelProperty("最低资本计算公式")
    private String capitalCalculationFormula;

    /**
     * 状态：1-有效，0-无效
     */
    @NotBlank(message = "状态不能为空")
    @Size(max = 1, message = "状态长度不能超过1个字符")
    @ExcelProperty("状态")
    private String status;
}
