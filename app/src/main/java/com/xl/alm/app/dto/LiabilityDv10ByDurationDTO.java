package com.xl.alm.app.dto;

import com.jd.lightning.common.annotation.Excel;
import com.jd.lightning.common.core.domain.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 分中短负债基点价值DV10表DTO
 *
 * @author AI Assistant
 */
@Data
public class LiabilityDv10ByDurationDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 账期,格式YYYYMM */
    @NotBlank(message = "账期不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "账期格式必须为YYYYMM")
    @Excel(name = "账期")
    private String accountPeriod;

    /** 现金流类型,01:流入,02:流出 */
    @NotBlank(message = "现金流类型不能为空")
    @Size(min = 2, max = 2, message = "现金流类型长度必须为2")
    @Excel(name = "现金流类型", readConverterExp = "01=流入,02=流出", combo = {"01", "02"}, dictType = "dur_cash_flow_type")
    private String cashFlowType;

    /** 设计类型 */
    @NotBlank(message = "设计类型不能为空")
    @Size(min = 2, max = 2, message = "设计类型长度必须为2")
    @Excel(name = "设计类型")
    private String designType;

    /** 是否为中短期险种,Y:是,N:否 */
    @NotBlank(message = "中短期险种标志不能为空")
    @Size(min = 1, max = 1, message = "中短期险种标志长度必须为1")
    @Pattern(regexp = "^[YN]$", message = "中短期险种标志必须为Y或N")
    @Excel(name = "是否为中短期险种", readConverterExp = "Y=是,N=否", combo = {"Y", "N"})
    private String shortTermFlag;

    /** 现值类型,01:上升,02:下降,03:净值 */
    @NotBlank(message = "现值类型不能为空")
    @Size(min = 2, max = 2, message = "现值类型长度必须为2")
    @Excel(name = "现值类型", readConverterExp = "01=上升,02=下降,03=净值", combo = {"01", "02", "03"}, dictType = "dur_value_type")
    private String valueType;

    /** 0年期限点的DV10现值 */
    @Excel(name = "0年期限点的DV10现值")
    private BigDecimal term0;

    /** 0.5年期限点的DV10现值 */
    @Excel(name = "0.5年期限点的DV10现值")
    private BigDecimal term05;

    /** 1年期限点的DV10现值 */
    @Excel(name = "1年期限点的DV10现值")
    private BigDecimal term1;

    /** 2年期限点的DV10现值 */
    @Excel(name = "2年期限点的DV10现值")
    private BigDecimal term2;

    /** 3年期限点的DV10现值 */
    @Excel(name = "3年期限点的DV10现值")
    private BigDecimal term3;

    /** 4年期限点的DV10现值 */
    @Excel(name = "4年期限点的DV10现值")
    private BigDecimal term4;

    /** 5年期限点的DV10现值 */
    @Excel(name = "5年期限点的DV10现值")
    private BigDecimal term5;

    /** 6年期限点的DV10现值 */
    @Excel(name = "6年期限点的DV10现值")
    private BigDecimal term6;

    /** 7年期限点的DV10现值 */
    @Excel(name = "7年期限点的DV10现值")
    private BigDecimal term7;

    /** 8年期限点的DV10现值 */
    @Excel(name = "8年期限点的DV10现值")
    private BigDecimal term8;

    /** 10年期限点的DV10现值 */
    @Excel(name = "10年期限点的DV10现值")
    private BigDecimal term10;

    /** 12年期限点的DV10现值 */
    @Excel(name = "12年期限点的DV10现值")
    private BigDecimal term12;

    /** 15年期限点的DV10现值 */
    @Excel(name = "15年期限点的DV10现值")
    private BigDecimal term15;

    /** 20年期限点的DV10现值 */
    @Excel(name = "20年期限点的DV10现值")
    private BigDecimal term20;

    /** 25年期限点的DV10现值 */
    @Excel(name = "25年期限点的DV10现值")
    private BigDecimal term25;

    /** 30年期限点的DV10现值 */
    @Excel(name = "30年期限点的DV10现值")
    private BigDecimal term30;

    /** 35年期限点的DV10现值 */
    @Excel(name = "35年期限点的DV10现值")
    private BigDecimal term35;

    /** 40年期限点的DV10现值 */
    @Excel(name = "40年期限点的DV10现值")
    private BigDecimal term40;

    /** 45年期限点的DV10现值 */
    @Excel(name = "45年期限点的DV10现值")
    private BigDecimal term45;

    /** 50年期限点的DV10现值 */
    @Excel(name = "50年期限点的DV10现值")
    private BigDecimal term50;
}
