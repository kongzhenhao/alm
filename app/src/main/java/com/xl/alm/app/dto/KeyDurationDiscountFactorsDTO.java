package com.xl.alm.app.dto;

import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 关键久期折现因子表DTO
 *
 * @author AI Assistant
 */
@Data
public class KeyDurationDiscountFactorsDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 账期,格式YYYYMM */
    @NotBlank(message = "账期不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "账期格式必须为YYYYMM")
    @Excel(name = "账期")
    private String accountPeriod;

    /** 曲线类型,01:中档,02:低档 */
    @NotBlank(message = "曲线类型不能为空")
    @Size(min = 2, max = 2, message = "曲线类型长度必须为2")
    @Excel(name = "曲线类型", readConverterExp = "01=中档,02=低档", combo = {"01", "02"}, dictType = "dur_curve_type")
    private String curveType;

    /** 关键期限点 */
    @NotBlank(message = "关键期限点不能为空")
    @Excel(name = "关键期限点", prompt = "可以是整数、小数或其他数值")
    private String keyDuration;

    /** 压力方向,01:上升,02:下降 */
    @NotBlank(message = "压力方向不能为空")
    @Size(min = 2, max = 2, message = "压力方向长度必须为2")
    @Excel(name = "压力方向", readConverterExp = "01=上升,02=下降", combo = {"01", "02"}, dictType = "dur_stress_direction")
    private String stressDirection;

    /** 久期类型,01:修正久期,02:有效久期,03:关键久期 */
    @NotBlank(message = "久期类型不能为空")
    @Size(min = 2, max = 2, message = "久期类型长度必须为2")
    @Excel(name = "久期类型", readConverterExp = "01=修正久期,02=有效久期,03=关键久期", combo = {"01", "02", "03"}, dictType = "dur_duration_type")
    private String durationType;

    /** 因子值集,分为1273项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1273":{"date":"2025-12-01","val":0.15}} */
    @NotBlank(message = "因子值集不能为空")
    @Excel(name = "因子值集", width = 50)
    private String factorValSet;

    /** 是否删除，0：否，1：是 */
    private Integer isDel = 0;
}
