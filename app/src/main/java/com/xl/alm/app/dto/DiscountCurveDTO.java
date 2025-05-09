package com.xl.alm.app.dto;

import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 折现曲线DTO
 *
 * @author AI Assistant
 */
@Data
public class DiscountCurveDTO extends BaseDTO {
    private Long id;
    
    /**
     * 账期，格式YYYYMM
     */
    @NotBlank(message = "账期不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "账期格式必须为YYYYMM")
    @Excel(name = "账期")
    private String accountPeriod;

    /**
     * 曲线类型,01:中档,02:低档
     */
    @NotBlank(message = "曲线类型不能为空")
    @Size(min = 2, max = 2, message = "曲线类型长度必须为2")
    @Excel(name = "曲线类型", readConverterExp = "01=中档,02=低档", combo = {"01", "02"}, dictType = "dur_curve_type")
    private String curveType;

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
     * 曲线值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}
     */
    @NotBlank(message = "曲线值集不能为空")
    @Excel(name = "曲线值集", width = 50)
    private String curveValSet;

    /**
     * 是否删除，0：否，1：是
     */
    private int isDel = 0;
}
