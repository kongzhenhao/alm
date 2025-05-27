package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 基础Excel导出DTO，包含通用的Excel导出注解
 * 继承自BaseDTO，添加了Excel导出相关的注解
 *
 * @author AI Assistant
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseExcelDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    /** 创建者 */
    @ExcelIgnore
    private String createBy;

    /** 创建时间 */
    @ExcelIgnore
    private Date createTime;

    /** 更新者 */
    @ExcelIgnore
    private String updateBy;

    /** 更新时间 */
    @ExcelIgnore
    private Date updateTime;
}
