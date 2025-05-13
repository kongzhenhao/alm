package com.xl.alm.app.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 万能平均结算利率表DTO
 *
 * @author AI Assistant
 */
@Data
public class UniversalAvgSettlementRateDTO {
    /** 主键ID */
    @ExcelIgnore
    private Long id;

    /** 账期 */
    @ExcelProperty(value = "账期", index = 0)
    private String accountingPeriod;

    /** 精算代码 */
    @ExcelProperty(value = "精算代码", index = 1)
    private String actuarialCode;

    /** 业务代码 */
    @ExcelProperty(value = "业务代码", index = 2)
    private String businessCode;

    /** 产品名称 */
    @ExcelProperty(value = "产品名称", index = 3)
    private String productName;

    /** 是否中短：Y-是，N-否 */
    @ExcelProperty(value = "是否中短", index = 4)
    private String shortTermFlag;

    /** 定价保证成本率 */
    @ExcelProperty(value = "定价保证成本率", index = 5)
    private BigDecimal guaranteedCostRate;

    /** 平均结息利率T0 */
    @ExcelProperty(value = "平均结息利率T0", index = 6)
    private BigDecimal avgRateT0;

    /** 平均结息利率T1 */
    @ExcelProperty(value = "平均结息利率T1", index = 7)
    private BigDecimal avgRateT1;

    /** 平均结息利率T2 */
    @ExcelProperty(value = "平均结息利率T2", index = 8)
    private BigDecimal avgRateT2;

    /** 平均结息利率T3 */
    @ExcelProperty(value = "平均结息利率T3", index = 9)
    private BigDecimal avgRateT3;

    /** 创建者 */
    @ExcelIgnore
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelIgnore
    private Date createTime;

    /** 更新者 */
    @ExcelIgnore
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelIgnore
    private Date updateTime;

    /** 备注 */
    @ExcelProperty(value = "备注", index = 10)
    private String remark;
    
    /** 是否删除，0:否，1:是 */
    @ExcelIgnore
    private Integer isDel;
}
