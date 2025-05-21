package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 续保率评估月份配置DTO
 *
 * @author AI Assistant
 */
@Data
public class RenewalEvalMonthCfgDTO extends BaseDTO {
    private Long id;

    /**
     * 账期,格式YYYYMM
     */
    @NotBlank(message = "账期不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "账期格式必须为YYYYMM")
    @Excel(name = "账期")
    @ExcelProperty("账期")
    private String accountingPeriod;

    /**
     * 当季第几月
     */
    @NotNull(message = "当季第几月不能为空")
    @Min(value = 1, message = "当季第几月最小值为1")
    @Max(value = 3, message = "当季第几月最大值为3")
    @Excel(name = "当季第几月")
    @ExcelProperty("当季第几月")
    private Integer monthSeqInCurrQuarter;

    /**
     * 评估时点月份,格式YYYYMM
     */
    @NotBlank(message = "评估时点月份不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "评估时点月份格式必须为YYYYMM")
    @Excel(name = "评估时点月份")
    @ExcelProperty("评估时点月份")
    private String evalPointMonth;

    /**
     * 上年同比月份,格式YYYYMM
     */
    @NotBlank(message = "上年同比月份不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "上年同比月份格式必须为YYYYMM")
    @Excel(name = "上年同比月份")
    @ExcelProperty("上年同比月份")
    private String monthOfLastYear;

    /**
     * 当季月份,格式YYYYMM
     */
    @NotBlank(message = "当季月份不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "当季月份格式必须为YYYYMM")
    @Excel(name = "当季月份")
    @ExcelProperty("当季月份")
    private String monthInCurrQuarter;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel = 0;
}
