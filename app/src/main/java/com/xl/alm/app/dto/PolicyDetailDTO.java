package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 保单数据明细DTO
 *
 * @author AI Assistant
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PolicyDetailDTO extends BaseDTO {
    private Long id;

    /**
     * 险种号
     */
    @NotBlank(message = "险种号不能为空")
    @Size(max = 20, message = "险种号长度不能超过20个字符")
    @Excel(name = "险种号")
    @ExcelProperty("险种号")
    private String polNo;

    /**
     * 保单号
     */
    @NotBlank(message = "保单号不能为空")
    @Size(max = 20, message = "保单号长度不能超过20个字符")
    @Excel(name = "保单号")
    @ExcelProperty("保单号")
    private String contNo;

    /**
     * 险种生效日
     */
    @NotNull(message = "险种生效日不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "险种生效日", dateFormat = "yyyy-MM-dd")
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "险种生效日")
    private Date effectiveDate;

    /**
     * 险种编码
     */
    @NotBlank(message = "险种编码不能为空")
    @Size(max = 20, message = "险种编码长度不能超过20个字符")
    @Excel(name = "险种编码")
    @ExcelProperty("险种编码")
    private String riskCode;

    /**
     * 险种状态描述
     */
    @NotBlank(message = "险种状态描述不能为空")
    @Size(max = 20, message = "险种状态描述长度不能超过20个字符")
    @Excel(name = "险种状态描述")
    @ExcelProperty("险种状态描述")
    private String polStatusDesc;

    /**
     * 险种状态变化时间
     */
    @NotNull(message = "险种状态变化时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "险种状态变化时间", dateFormat = "yyyy-MM-dd")
    @DateTimeFormat("yyyy-MM-dd")
    @ExcelProperty(value = "险种状态变化时间")
    private Date polStatusChangedTime;

    /**
     * 设计类型
     */
    @NotBlank(message = "设计类型不能为空")
    @Size(max = 50, message = "设计类型长度不能超过50个字符")
    @Excel(name = "设计类型")
    @ExcelProperty("设计类型")
    private String designType;

    /**
     * 业务类型编码
     */
    @NotBlank(message = "业务类型编码不能为空")
    @Size(max = 20, message = "业务类型编码长度不能超过20个字符")
    @Excel(name = "业务类型编码")
    @ExcelProperty("业务类型编码")
    private String busTypeCode;

    /**
     * 是否删除，0:否，1:是
     */
    private Integer isDel = 0;
}
