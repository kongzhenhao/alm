package com.xl.alm.app.dto;

import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 负债现金流DTO
 *
 * @author AI Assistant
 */
@Data
public class LiabilityCashFlowDTO extends BaseDTO {
    private Long id;
    
    /**
     * 账期，格式YYYYMM
     */
    @NotBlank(message = "账期不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "账期格式必须为YYYYMM")
    @Excel(name = "账期")
    private String accountPeriod;

    /**
     * 现金流类型,01:流入,02:流出
     */
    @NotBlank(message = "现金流类型不能为空")
    @Size(min = 2, max = 2, message = "现金流类型长度必须为2")
    @Excel(name = "现金流类型", readConverterExp = "01=流入,02=流出", combo = {"01", "02"}, dictType = "dur_cash_flow_type")
    private String cashFlowType;

    /**
     * 基点类型,01:+50bp,02:-50bp
     */
    @NotBlank(message = "基点类型不能为空")
    @Size(min = 2, max = 2, message = "基点类型长度必须为2")
    @Excel(name = "基点类型", readConverterExp = "01=+50bp,02=-50bp", combo = {"01", "02"})
    private String bpType;

    /**
     * 久期类型,01:修正久期,02:有效久期
     */
    @NotBlank(message = "久期类型不能为空")
    @Size(min = 2, max = 2, message = "久期类型长度必须为2")
    @Excel(name = "久期类型", readConverterExp = "01=修正久期,02=有效久期", combo = {"01", "02"}, dictType = "dur_duration_type")
    private String durationType;

    /**
     * 设计类型
     */
    @NotBlank(message = "设计类型不能为空")
    @Size(max = 50, message = "设计类型长度不能超过50个字符")
    @Excel(name = "设计类型")
    private String designType;

    /**
     * 是否为中短期险种
     */
    @NotBlank(message = "是否为中短期险种不能为空")
    @Size(min = 1, max = 1, message = "是否为中短期险种长度必须为1")
    @Excel(name = "是否为中短期险种", readConverterExp = "Y=是,N=否", combo = {"Y", "N"})
    private String isShortTerm;

    /**
     * 精算代码
     */
    @NotBlank(message = "精算代码不能为空")
    @Size(max = 20, message = "精算代码长度不能超过20个字符")
    @Excel(name = "精算代码")
    private String actuarialCode;

    /**
     * 业务代码
     */
    @NotBlank(message = "业务代码不能为空")
    @Size(max = 20, message = "业务代码长度不能超过20个字符")
    @Excel(name = "业务代码")
    private String businessCode;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空")
    @Size(max = 50, message = "产品名称长度不能超过50个字符")
    @Excel(name = "产品名称")
    private String productName;

    /**
     * 险种主类
     */
    @NotBlank(message = "险种主类不能为空")
    @Size(max = 50, message = "险种主类长度不能超过50个字符")
    @Excel(name = "险种主类")
    private String insuranceMainType;

    /**
     * 险种细类
     */
    @NotBlank(message = "险种细类不能为空")
    @Size(max = 50, message = "险种细类长度不能超过50个字符")
    @Excel(name = "险种细类")
    private String insuranceSubType;

    /**
     * 现金流值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}
     */
    @NotBlank(message = "现金流值集不能为空")
    @Excel(name = "现金流值集")
    private String cashValSet;
}
