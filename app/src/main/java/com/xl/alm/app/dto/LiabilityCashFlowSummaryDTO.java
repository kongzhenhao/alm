package com.xl.alm.app.dto;

import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 负债现金流汇总DTO
 *
 * @author AI Assistant
 */
@Data
public class LiabilityCashFlowSummaryDTO extends BaseDTO {
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
    @Excel(name = "基点类型", readConverterExp = "01=+50bp,02=-50bp", combo = {"01", "02"}, dictType = "dur_bp_type")
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
    @Excel(name = "是否为中短期险种", readConverterExp = "Y=是,N=否", combo = {"Y", "N"}, dictType = "dur_is_short_term")
    private String isShortTerm;

    /**
     * 现金流值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}
     */
    @NotBlank(message = "现金流值集不能为空")
    @Excel(name = "现金流值集", width = 50)
    private String cashValSet;

    /**
     * 现金流现值值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}
     */
    @NotBlank(message = "现金流现值值集不能为空")
    @Excel(name = "现金流现值值集", width = 50)
    private String presentCashValSet;

    /**
     * is开头字段为了easyexcel能够识别，所以需要手动添加get和set方法
     */
    public String getIsShortTerm() {
        return isShortTerm;
    }

    public void setIsShortTerm(String isShortTerm) {
        this.isShortTerm = isShortTerm;
    }

    /**
     * 是否删除，0：否，1：是
     */

    private int isDel = 0;
}
