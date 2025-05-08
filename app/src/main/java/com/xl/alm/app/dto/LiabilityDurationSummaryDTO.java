package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 负债久期汇总表 DTO
 *
 * @author AI Assistant
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LiabilityDurationSummaryDTO extends BaseDTO {
    /** 主键 */
    @ExcelIgnore
    private Long id;

    /** 账期，格式YYYYMM */
    @NotBlank(message = "账期不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "账期格式必须为YYYYMM")
    @ExcelProperty(value = "账期")
    @Excel(name = "账期")
    private String accountPeriod;

    /** 现金流类型,01:流入,02:流出 */
    @NotBlank(message = "现金流类型不能为空")
    @Size(min = 2, max = 2, message = "现金流类型长度必须为2")
    @ExcelProperty(value = "现金流类型")
    @Excel(name = "现金流类型", readConverterExp = "01=流入,02=流出", combo = { "01", "02" }, dictType = "dur_cash_flow_type")
    private String cashFlowType;

    /** 基点类型,01:+50bp,02:-50bp */
    @NotBlank(message = "基点类型不能为空")
    @Size(min = 2, max = 2, message = "基点类型长度必须为2")
    @ExcelProperty(value = "基点类型")
    @Excel(name = "基点类型", readConverterExp = "01=+50bp,02=-50bp,03=0bp", combo = { "01", "02", "03" }, dictType = "dur_bp_type")
    private String bpType;

    /** 久期类型,01:修正久期,02:有效久期 */
    @NotBlank(message = "久期类型不能为空")
    @Size(min = 2, max = 2, message = "久期类型长度必须为2")
    @ExcelProperty(value = "久期类型")
    @Excel(name = "久期类型", readConverterExp = "01=修正久期,02=有效久期", combo = { "01", "02" }, dictType = "dur_duration_type")
    private String durationType;

    /** 设计类型 */
    @NotBlank(message = "设计类型不能为空")
    @Size(max = 50, message = "设计类型长度不能超过50个字符")
    @ExcelProperty(value = "设计类型")
    @Excel(name = "设计类型")
    private String designType;

    /** 是否为中短期险种 */
    @NotBlank(message = "是否为中短期险种不能为空")
    @Size(min = 1, max = 1, message = "是否为中短期险种长度必须为1")
    @ExcelProperty(value = "是否为中短期险种")
    @Excel(name = "是否为中短期险种", readConverterExp = "Y=是,N=否", combo = { "Y", "N" }, dictType = "dur_is_short_term")
    private String isShortTerm;

    /** 久期值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}} */
    @NotBlank(message = "久期值集不能为空")
    @ExcelProperty(value = "久期值集")
    @Excel(name = "久期值集", width = 50)
    private String durationValSet;

    int isDel = 0;
}
