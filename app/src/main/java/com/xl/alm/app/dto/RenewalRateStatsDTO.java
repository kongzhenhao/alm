package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.date.DateStringConverter;
import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 保单续保率统计DTO
 *
 * @author AI Assistant
 */
@Data
public class RenewalRateStatsDTO extends BaseDTO {
    private Long id;

    /**
     * 账期，格式YYYYMM
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
    @Excel(name = "当季第几月")
    @ExcelProperty("当季第几月")
    private Integer monthSeqInCurrQuarter;

    /**
     * 渠道类型编码
     */
    @NotBlank(message = "渠道类型编码不能为空")
    @Size(max = 20, message = "渠道类型编码长度不能超过20个字符")
    @Excel(name = "渠道类型编码")
    @ExcelProperty("渠道类型编码")
    private String channelTypeCode;

    /**
     * 上年同比月份,格式YYYYMM
     */
    @NotBlank(message = "上年同比月份不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "上年同比月份格式必须为YYYYMM")
    @Excel(name = "上年同比月份")
    @ExcelProperty("上年同比月份")
    private String monthOfLastYear;

    /**
     * 上年度有效保单数量
     */
    @NotNull(message = "上年度有效保单数量不能为空")
    @Excel(name = "上年度有效保单数量")
    @ExcelProperty("上年度有效保单数量")
    private Integer validPolicyCntLastYear;

    /**
     * 本年度有效保单数量
     */
    @NotNull(message = "本年度有效保单数量不能为空")
    @Excel(name = "本年度有效保单数量")
    @ExcelProperty("本年度有效保单数量")
    private Integer validPolicyCntCurrYear;

    /**
     * 保单续保率
     */
    @NotNull(message = "保单续保率不能为空")
    @Excel(name = "保单续保率")
    @ExcelProperty("保单续保率")
    private BigDecimal policyRenewalRate;

    /**
     * 是否删除，0：否，1：是
     */
    private Integer isDel;
}
