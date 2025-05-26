package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 边际最低资本贡献率表Excel操作DTO
 * 用于Excel导入导出，包含中文表头
 *
 * @author AI Assistant
 */
@Data
public class MarginalCapitalExportDTO {

    /**
     * 账期，格式：YYYYMM（如202306）
     */
    @NotBlank(message = "账期不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "账期格式不正确，应为YYYYMM格式")
    @ExcelProperty("账期")
    private String accountingPeriod;

    /**
     * 项目名称，用于Excel导入导出
     */
    @NotBlank(message = "项目不能为空")
    @ExcelProperty("项目")
    private String itemName;

    /**
     * 再保后金额，数值类型，保留2位小数
     */
    @NotNull(message = "再保后金额不能为空")
    @DecimalMin(value = "0", message = "再保后金额不能小于0")
    @Digits(integer = 16, fraction = 2, message = "再保后金额格式不正确，整数部分最多16位，小数部分最多2位")
    @ExcelProperty("再保后金额")
    private BigDecimal reinsuAfterAmount;

    /**
     * 存储子风险层面边际最低资本贡献因子，百分比格式，如：0.583表示58.3%
     */
    @DecimalMin(value = "0", message = "子风险边际因子不能小于0")
    @DecimalMax(value = "1", message = "子风险边际因子不能大于1")
    @Digits(integer = 1, fraction = 4, message = "子风险边际因子格式不正确，整数部分最多1位，小数部分最多4位")
    @ExcelProperty("子风险边际因子")
    private BigDecimal subRiskMarginalFactor;

    /**
     * 存储公司层面边际最低资本贡献因子，百分比格式，如：0.583表示58.3%
     */
    @DecimalMin(value = "0", message = "公司边际因子不能小于0")
    @DecimalMax(value = "1", message = "公司边际因子不能大于1")
    @Digits(integer = 1, fraction = 4, message = "公司边际因子格式不正确，整数部分最多1位，小数部分最多4位")
    @ExcelProperty("公司边际因子")
    private BigDecimal companyMarginalFactor;
}
