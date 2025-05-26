package com.xl.alm.app.dto;

import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.annotation.Excels;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 分部门最低资本明细表DTO
 *
 * @author AI Assistant
 */
@Data
public class DeptMincapDetailDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 账期，格式：YYYYMM（如202306）
     */
    @NotBlank(message = "账期不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "账期格式必须为YYYYMM")
    private String accountingPeriod;

    /**
     * 统计部门名称
     */
    @NotBlank(message = "统计部门名称不能为空")
    @Size(max = 100, message = "统计部门名称长度不能超过100个字符")
    @Excel(name = "部门名称")
    private String department;

    /**
     * 项目编码，关联项目定义表(t_minc_item_definition)的item_code
     */
    @NotBlank(message = "项目编码不能为空")
    @Size(max = 20, message = "项目编码长度不能超过20个字符")
    private String itemCode;

    /**
     * 项目名称（用于导入导出）
     */
    @Excel(name = "项目")
    private String itemName;

    /**
     * 账户编码，对应sys_dict_data表中dict_type='minc_account'的字典项
     */
    @Size(max = 10, message = "账户编码长度不能超过10个字符")
    private String accountCode;

    /**
     * 账户金额
     */
    @DecimalMin(value = "-999999999999999999.9999999999", message = "账户金额不能小于-999999999999999999.9999999999")
    @DecimalMax(value = "999999999999999999.9999999999", message = "账户金额不能大于999999999999999999.9999999999")
    private BigDecimal amount;

    /**
     * 传统保险账户金额（用于导入导出）
     */
    @Excel(name = "传统保险账户", cellType = Excel.ColumnType.NUMERIC, scale = 2)
    private BigDecimal traditionalAmount;

    /**
     * 分红保险账户金额（用于导入导出）
     */
    @Excel(name = "分红保险账户", cellType = Excel.ColumnType.NUMERIC, scale = 2)
    private BigDecimal dividendAmount;

    /**
     * 万能保险账户金额（用于导入导出）
     */
    @Excel(name = "万能保险账户", cellType = Excel.ColumnType.NUMERIC, scale = 2)
    private BigDecimal universalAmount;

    /**
     * 独立账户金额（用于导入导出）
     */
    @Excel(name = "独立账户", cellType = Excel.ColumnType.NUMERIC, scale = 2)
    private BigDecimal independentAmount;

    /**
     * 资本补充债账户金额（用于导入导出）
     */
    @Excel(name = "资本补充债账户", cellType = Excel.ColumnType.NUMERIC, scale = 2)
    private BigDecimal companyTotalAmount;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel;
}
