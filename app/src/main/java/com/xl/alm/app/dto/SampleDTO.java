package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 折现因子DTO
 *
 * @author AI Assistant
 */
@Data
public class SampleDTO extends BaseDTO {
    private Long id;
    /**
     * 账期，格式YYYYMM
     */
    @NotBlank(message = "账期不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "账期格式必须为YYYYMM")
    @Excel(name = "账期")
    @ExcelProperty("账期")
    private String accountPeriod;

    /**
     * 因子类型01:中档,02:低档
     */
    @NotBlank(message = "因子类型不能为空")
    @Size(min = 2, max = 2, message = "因子类型长度必须为2")
    @Excel(name = "因子类型", readConverterExp = "01=中档,02=低档", combo = {"01", "02"})
    @ExcelProperty("因子类型")
    private String factorType;

    /**
     * 久期类型,01:修正久期,02:有效久期
     */
    @NotBlank(message = "久期类型不能为空")
    @Size(min = 2, max = 2, message = "久期类型长度必须为2")
    @Excel(name = "久期类型", readConverterExp = "01=修正久期,02=有效久期", combo = {"01", "02"})
    @ExcelProperty("久期类型")
    private String durationType;

    /**
     * 设计类型
     */
    @NotBlank(message = "久设计类型不能为空")
    @Size(min = 1, max = 50, message = "设计类型长度必须小于等于50")
    @Excel(name = "设计类型")
    @ExcelProperty("设计类型")
    private String designType;

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
     * 是否为中短期险种
     */
    @NotBlank(message = "是否为中短期险种不能为空")
    @Size(max = 1, message = "是否为中短期险种长度不能超过1个字符")
    @Excel(name = "是否为中短期险种", readConverterExp = "Y=是,N=否", width = 15)
    @ExcelProperty("是否为中短期险种")
    private String isShortTerm;

    /**
     * 因子值集,分为1272项,格式{"1":{"date":"2025-01-01","val":0.25},"2":{"date":"2025-01-02","val":0.35},...,"1272":{"date":"2025-12-01","val":0.15}}
     */
    @NotBlank(message = "因子值集不能为空")
    @Excel(name = "因子值集", width = 50)
    @ExcelProperty("因子值集")
    private String factorValSet;

    /**
     * 是否删除，0：否，1：是
     */
    private int isDel = 0;
}