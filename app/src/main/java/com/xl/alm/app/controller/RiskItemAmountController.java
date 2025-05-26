package com.xl.alm.app.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.RiskItemAmountDTO;
import com.xl.alm.app.dto.RiskItemAmountExportDTO;
import com.xl.alm.app.query.RiskItemAmountQuery;
import com.xl.alm.app.service.RiskItemAmountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 风险项目金额表 Controller
 *
 * @author AI Assistant
 */
@Slf4j
@RestController
@RequestMapping("/minc/risk/item/amount")
public class RiskItemAmountController extends BaseController {

    @Autowired
    private RiskItemAmountService riskItemAmountService;

    /**
     * 查询风险项目金额列表
     */
    @PreAuthorize("@ss.hasPermi('minc:risk:item:amount:list')")
    @GetMapping("/list")
    public TableDataInfo list(RiskItemAmountQuery query) {
        startPage();
        List<RiskItemAmountDTO> list = riskItemAmountService.selectRiskItemAmountDtoList(query);
        return getDataTable(list);
    }

    /**
     * 获取风险项目金额详细信息
     */
    @PreAuthorize("@ss.hasPermi('minc:risk:item:amount:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(riskItemAmountService.selectRiskItemAmountDtoById(id));
    }

    /**
     * 新增风险项目金额
     */
    @PreAuthorize("@ss.hasPermi('minc:risk:item:amount:add')")
    @Log(title = "风险项目金额", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody @Valid RiskItemAmountDTO dto) {
        return toAjax(riskItemAmountService.insertRiskItemAmountDto(dto));
    }

    /**
     * 批量新增风险项目金额
     */
    @PreAuthorize("@ss.hasPermi('minc:risk:item:amount:add')")
    @Log(title = "风险项目金额", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<RiskItemAmountDTO> dtoList) {
        return toAjax(riskItemAmountService.batchInsertRiskItemAmountDto(dtoList));
    }

    /**
     * 修改风险项目金额
     */
    @PreAuthorize("@ss.hasPermi('minc:risk:item:amount:edit')")
    @Log(title = "风险项目金额", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody @Valid RiskItemAmountDTO dto) {
        return toAjax(riskItemAmountService.updateRiskItemAmountDto(dto));
    }

    /**
     * 删除风险项目金额
     */
    @PreAuthorize("@ss.hasPermi('minc:risk:item:amount:remove')")
    @Log(title = "风险项目金额", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(riskItemAmountService.deleteRiskItemAmountDtoByIds(ids));
    }

    /**
     * 根据账期删除风险项目金额
     */
    @PreAuthorize("@ss.hasPermi('minc:risk:item:amount:remove')")
    @Log(title = "风险项目金额", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountingPeriod}")
    public Result removeByPeriod(@PathVariable String accountingPeriod) {
        return toAjax(riskItemAmountService.deleteRiskItemAmountDtoByPeriod(accountingPeriod));
    }

    /**
     * 根据条件删除风险项目金额
     */
    @PreAuthorize("@ss.hasPermi('minc:risk:item:amount:remove')")
    @Log(title = "风险项目金额", businessType = BusinessType.DELETE)
    @DeleteMapping("/condition")
    public Result removeByCondition(@RequestParam String accountingPeriod,
                                   @RequestParam String itemCode) {
        try {
            int result = riskItemAmountService.deleteRiskItemAmountDtoByCondition(accountingPeriod, itemCode);
            return toAjax(result);
        } catch (Exception e) {
            log.error("删除风险项目金额失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 导入风险项目金额
     */
    @PreAuthorize("@ss.hasPermi('minc:risk:item:amount:import')")
    @Log(title = "风险项目金额", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, @RequestParam(defaultValue = "false") boolean updateSupport) throws Exception {
        try {
            log.info("开始导入风险项目金额数据，是否更新已有数据：" + updateSupport);

            // 使用EasyExcel读取Excel文件
            List<RiskItemAmountExportDTO> importList = EasyExcel.read(file.getInputStream())
                    .head(RiskItemAmountExportDTO.class)
                    .sheet()
                    .doReadSync();

            // 转换为DTO
            List<RiskItemAmountDTO> dtoList = new ArrayList<>();
            for (RiskItemAmountExportDTO importDTO : importList) {
                // 跳过空行
                if (importDTO.getItemName() == null) {
                    continue;
                }

                RiskItemAmountDTO dto = new RiskItemAmountDTO();
                dto.setAccountingPeriod(importDTO.getAccountingPeriod());
                dto.setItemName(importDTO.getItemName());
                dto.setS05Amount(processAmount(importDTO.getS05Amount()));
                dto.setIr05Amount(processAmount(importDTO.getIr05Amount()));
                dtoList.add(dto);
            }

            String operName = getUsername();
            String message = riskItemAmountService.importRiskItemAmountDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            log.error("导入Excel异常", e);
            return Result.error("导入Excel失败：" + e.getMessage());
        }
    }

    /**
     * 导出风险项目金额
     */
    @PreAuthorize("@ss.hasPermi('minc:risk:item:amount:export')")
    @Log(title = "风险项目金额", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RiskItemAmountQuery query) {
        try {
            // 获取数据
            List<RiskItemAmountDTO> list = riskItemAmountService.selectRiskItemAmountDtoList(query);

            // 转换为导出DTO
            List<RiskItemAmountExportDTO> exportList = new ArrayList<>();
            for (RiskItemAmountDTO dto : list) {
                RiskItemAmountExportDTO exportDTO = new RiskItemAmountExportDTO();
                exportDTO.setAccountingPeriod(dto.getAccountingPeriod());
                exportDTO.setItemName(dto.getItemName());
                exportDTO.setS05Amount(dto.getS05Amount());
                exportDTO.setIr05Amount(dto.getIr05Amount());
                exportList.add(exportDTO);
            }

            // 设置下载文件的名称和类型
            String fileName = URLEncoder.encode("风险项目金额数据", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 使用EasyExcel导出数据，自动调整列宽
            EasyExcel.write(response.getOutputStream(), RiskItemAmountExportDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("风险项目金额")
                    .doWrite(exportList);
        } catch (Exception e) {
            log.error("导出Excel异常", e);
        }
    }

    /**
     * 获取风险项目金额模板Excel
     *
     * 项目编码和名称的映射关系：
     * MR001_01: 寿险业务保险风险最低资本
     * MR001_02: 非寿险业务保险风险最低资本
     * MR002_01: 市场风险最低资本
     * MR002_02: 信用风险最低资本
     * MR003_01: 风险分散效应
     * MR003_02: 控制风险最低资本
     */
    @PreAuthorize("@ss.hasPermi('minc:risk:item:amount:export')")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        try {
            // 设置下载文件的名称和类型
            String fileName = URLEncoder.encode("风险项目金额模板", "UTF-8").replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 创建一个空的模板数据
            List<RiskItemAmountExportDTO> templateList = new ArrayList<>();

            // 添加一个空行作为模板
            RiskItemAmountExportDTO template = new RiskItemAmountExportDTO();
            templateList.add(template);

            // 使用EasyExcel导出模板，自动调整列宽
            EasyExcel.write(response.getOutputStream(), RiskItemAmountExportDTO.class)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("风险项目金额")
                    .doWrite(templateList);
        } catch (Exception e) {
            log.error("导出模板Excel异常", e);
            e.printStackTrace(); // 打印详细错误信息
        }
    }

    /**
     * 处理金额，确保不超出数据库字段范围
     *
     * @param amount 原始金额
     * @return 处理后的金额
     */
    private BigDecimal processAmount(BigDecimal amount) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }

        try {
            // 设置精度为10位小数
            amount = amount.setScale(10, BigDecimal.ROUND_HALF_UP);

            // 检查整数部分是否超过20位（对应DECIMAL(30,10)类型）
            BigDecimal absAmount = amount.abs();
            String amountStr = absAmount.toPlainString();
            int decimalPointIndex = amountStr.indexOf('.');
            String integerPart = decimalPointIndex > 0 ? amountStr.substring(0, decimalPointIndex) : amountStr;

            if (integerPart.length() > 20) {
                log.warn("金额超出范围，将被截断：" + amount);
                // 截断为最大值或最小值（20位整数+10位小数）
                BigDecimal maxValue = new BigDecimal("99999999999999999999.9999999999");
                BigDecimal minValue = new BigDecimal("-99999999999999999999.9999999999");
                return amount.compareTo(BigDecimal.ZERO) > 0 ? maxValue : minValue;
            }

            return amount;
        } catch (Exception e) {
            log.error("处理金额时出错：" + amount, e);
            return BigDecimal.ZERO;
        }
    }
}
