package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 相关系数表Excel操作DTO
 * 用于Excel导入导出，包含中文表头
 *
 * @author AI Assistant
 */
@Data
public class CorrelationCoefExportDTO {

    /**
     * 账期，格式：YYYYMM（如202306）
     */
    @NotBlank(message = "账期不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "账期格式不正确，应为YYYYMM格式")
    @ExcelProperty("账期")
    private String accountingPeriod;

    /**
     * 项目X名称，用于Excel导入导出
     */
    @NotBlank(message = "项目X不能为空")
    @ExcelProperty("项目X")
    private String itemNameX;

    /**
     * 项目Y名称，用于Excel导入导出
     */
    @NotBlank(message = "项目Y不能为空")
    @ExcelProperty("项目Y")
    private String itemNameY;

    /**
     * 存储相关系数，范围[-1,1]
     */
    @NotNull(message = "相关系数不能为空")
    @DecimalMin(value = "-1.00", message = "相关系数不能小于-1")
    @DecimalMax(value = "1.00", message = "相关系数不能大于1")
    @Digits(integer = 1, fraction = 2, message = "相关系数格式不正确，整数部分最多1位，小数部分最多2位")
    @ExcelProperty("相关系数")
    private BigDecimal correlationValue;

    /**
     * 状态：1-有效，0-无效
     */
    @NotBlank(message = "状态不能为空")
    @Size(max = 1, message = "状态长度不能超过1个字符")
    @ExcelProperty("状态")
    private String status;
}
