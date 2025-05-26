package com.xl.alm.app.dto;

import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 关键久期参数表DTO
 *
 * @author AI Assistant
 */
@Data
public class KeyDurationParameterDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 账期,格式YYYYMM
     */
    @NotBlank(message = "账期不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "账期格式必须为YYYYMM")
    @Excel(name = "账期")
    private String accountPeriod;

    /**
     * 关键期限点
     */
    @NotBlank(message = "关键期限点不能为空")
    @Excel(name = "关键期限点", prompt = "可以是整数、小数或其他数值")
    private String keyDuration;

    /**
     * 关键久期参数值集,分为1273项,格式{"0":{"date":"2025-01-01","value":0.25},"1":{"date":"2025-01-02","value":0.35},...,"1272":{"date":"2025-12-01","value":0.15}}
     */
    @NotBlank(message = "关键久期参数值集不能为空")
    @Excel(name = "关键久期参数值集")
    private String parameterValSet;
}
