package com.xl.alm.app.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.exception.ServiceException;
import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.LiabilityDv10ByDurationDTO;
import com.xl.alm.app.dto.LiabilityDv10ByDurationExportDTO;
import com.xl.alm.app.query.LiabilityDv10ByDurationQuery;
import com.xl.alm.app.service.LiabilityDv10ByDurationService;
import com.xl.alm.app.util.CustomExcelExporter;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 分中短负债基点价值DV10表 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/dur/liability/dv10/by/duration")
public class LiabilityDv10ByDurationController extends BaseController {

    @Autowired
    private LiabilityDv10ByDurationService liabilityDv10ByDurationService;

    /**
     * 查询分中短负债基点价值DV10列表
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:dv10:by:duration:list')")
    @GetMapping("/list")
    public TableDataInfo list(LiabilityDv10ByDurationQuery query) {
        startPage();
        List<LiabilityDv10ByDurationDTO> list = liabilityDv10ByDurationService.selectLiabilityDv10ByDurationDtoList(query);
        return getDataTable(list);
    }

    /**
     * 导出分中短负债基点价值DV10列表
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:dv10:by:duration:export')")
    @Log(title = "分中短负债基点价值DV10", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LiabilityDv10ByDurationQuery query) {
        try {
            logger.info("开始导出分中短负债基点价值DV10数据，查询条件：{}", query);

            // 查询数据
            List<LiabilityDv10ByDurationDTO> list = liabilityDv10ByDurationService.selectLiabilityDv10ByDurationDtoList(query);
            logger.info("查询到{}条数据", list != null ? list.size() : 0);

            if (list == null || list.isEmpty()) {
                logger.warn("没有数据可导出");
                return;
            }

            // 转换为导出DTO
            List<LiabilityDv10ByDurationExportDTO> exportList = new ArrayList<>();
            for (LiabilityDv10ByDurationDTO dto : list) {
                LiabilityDv10ByDurationExportDTO exportDTO = new LiabilityDv10ByDurationExportDTO();

                // 设置基本字段
                exportDTO.setAccountPeriod(dto.getAccountPeriod());

                // 转换现金流类型
                if ("01".equals(dto.getCashFlowType())) {
                    exportDTO.setCashFlowTypeName("流入");
                } else if ("02".equals(dto.getCashFlowType())) {
                    exportDTO.setCashFlowTypeName("流出");
                } else {
                    exportDTO.setCashFlowTypeName(dto.getCashFlowType());
                }

                // 转换设计类型
                if ("01".equals(dto.getDesignType())) {
                    exportDTO.setDesignTypeName("传统险");
                } else if ("02".equals(dto.getDesignType())) {
                    exportDTO.setDesignTypeName("分红险");
                } else if ("03".equals(dto.getDesignType())) {
                    exportDTO.setDesignTypeName("万能险");
                } else if ("04".equals(dto.getDesignType())) {
                    exportDTO.setDesignTypeName("投连险");
                } else {
                    exportDTO.setDesignTypeName(dto.getDesignType());
                }

                // 转换是否为中短期险种
                if ("Y".equals(dto.getShortTermFlag())) {
                    exportDTO.setShortTermFlagName("是");
                } else if ("N".equals(dto.getShortTermFlag())) {
                    exportDTO.setShortTermFlagName("否");
                } else {
                    exportDTO.setShortTermFlagName(dto.getShortTermFlag());
                }

                // 转换现值类型
                if ("01".equals(dto.getValueType())) {
                    exportDTO.setValueTypeName("上升");
                } else if ("02".equals(dto.getValueType())) {
                    exportDTO.setValueTypeName("下降");
                } else if ("03".equals(dto.getValueType())) {
                    exportDTO.setValueTypeName("净值");
                } else {
                    exportDTO.setValueTypeName(dto.getValueType());
                }

                // 设置期限点值
                exportDTO.setTerm0(dto.getTerm0());
                exportDTO.setTerm05(dto.getTerm05());
                exportDTO.setTerm1(dto.getTerm1());
                exportDTO.setTerm2(dto.getTerm2());
                exportDTO.setTerm3(dto.getTerm3());
                exportDTO.setTerm4(dto.getTerm4());
                exportDTO.setTerm5(dto.getTerm5());
                exportDTO.setTerm6(dto.getTerm6());
                exportDTO.setTerm7(dto.getTerm7());
                exportDTO.setTerm8(dto.getTerm8());
                exportDTO.setTerm10(dto.getTerm10());
                exportDTO.setTerm12(dto.getTerm12());
                exportDTO.setTerm15(dto.getTerm15());
                exportDTO.setTerm20(dto.getTerm20());
                exportDTO.setTerm25(dto.getTerm25());
                exportDTO.setTerm30(dto.getTerm30());
                exportDTO.setTerm35(dto.getTerm35());
                exportDTO.setTerm40(dto.getTerm40());
                exportDTO.setTerm45(dto.getTerm45());
                exportDTO.setTerm50(dto.getTerm50());

                exportList.add(exportDTO);
            }

            // 使用EasyExcel直接导出，确保使用@ExcelProperty注解中的中文名称
            logger.info("开始导出Excel文件，共{}条数据", exportList.size());
            try {
                // 设置下载文件的名称和类型
                String fileName = URLEncoder.encode("分中短负债基点价值DV10数据", "UTF-8").replaceAll("\\+", "%20");
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

                // 创建表头样式
                WriteCellStyle headWriteCellStyle = new WriteCellStyle();
                headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                WriteFont headWriteFont = new WriteFont();
                headWriteFont.setFontHeightInPoints((short) 12);
                headWriteFont.setBold(true);
                headWriteCellStyle.setWriteFont(headWriteFont);
                headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
                headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
                headWriteCellStyle.setBorderRight(BorderStyle.THIN);
                headWriteCellStyle.setBorderTop(BorderStyle.THIN);

                // 创建内容样式
                WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
                WriteFont contentWriteFont = new WriteFont();
                contentWriteFont.setFontHeightInPoints((short) 11);
                contentWriteCellStyle.setWriteFont(contentWriteFont);
                contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
                contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
                contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
                contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
                contentWriteCellStyle.setBorderTop(BorderStyle.THIN);

                // 组合表头和内容样式
                HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                    new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

                // 使用EasyExcel导出，自动使用@ExcelProperty注解中的中文名称作为表头
                EasyExcel.write(response.getOutputStream(), LiabilityDv10ByDurationExportDTO.class)
                        .registerWriteHandler(horizontalCellStyleStrategy) // 应用样式
                        .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 自动调整列宽
                        .sheet("分中短负债基点价值DV10数据")
                        .doWrite(exportList);

                logger.info("导出Excel文件完成");
            } catch (Exception e) {
                logger.error("导出Excel文件失败", e);
                throw new ServiceException("导出Excel文件失败: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("导出分中短负债基点价值DV10数据失败", e);
            throw e;
        }
    }

    /**
     * 获取分中短负债基点价值DV10详细信息
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:dv10:by:duration:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(liabilityDv10ByDurationService.selectLiabilityDv10ByDurationDtoById(id));
    }

    /**
     * 根据条件查询分中短负债基点价值DV10
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:dv10:by:duration:query')")
    @GetMapping("/condition")
    public Result getByCondition(
            @RequestParam("accountPeriod") String accountPeriod,
            @RequestParam("cashFlowType") String cashFlowType,
            @RequestParam("designType") String designType,
            @RequestParam("shortTermFlag") String shortTermFlag,
            @RequestParam("valueType") String valueType) {
        return Result.success(liabilityDv10ByDurationService.selectLiabilityDv10ByDurationDtoByCondition(
                accountPeriod, cashFlowType, designType, shortTermFlag, valueType));
    }

    /**
     * 新增分中短负债基点价值DV10
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:dv10:by:duration:add')")
    @Log(title = "分中短负债基点价值DV10", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody @Valid LiabilityDv10ByDurationDTO dto) {
        return toAjax(liabilityDv10ByDurationService.insertLiabilityDv10ByDurationDto(dto));
    }

    /**
     * 修改分中短负债基点价值DV10
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:dv10:by:duration:edit')")
    @Log(title = "分中短负债基点价值DV10", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody @Valid LiabilityDv10ByDurationDTO dto) {
        return toAjax(liabilityDv10ByDurationService.updateLiabilityDv10ByDurationDto(dto));
    }

    /**
     * 删除分中短负债基点价值DV10
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:dv10:by:duration:remove')")
    @Log(title = "分中短负债基点价值DV10", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(liabilityDv10ByDurationService.deleteLiabilityDv10ByDurationDtoByIds(ids));
    }

    /**
     * 删除指定账期的分中短负债基点价值DV10
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:dv10:by:duration:remove')")
    @Log(title = "分中短负债基点价值DV10", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountPeriod}")
    public Result removeByPeriod(@PathVariable String accountPeriod) {
        return toAjax(liabilityDv10ByDurationService.deleteLiabilityDv10ByDurationDtoByPeriod(accountPeriod));
    }

    /**
     * 下载分中短负债基点价值DV10导入模板
     */
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelUtil<LiabilityDv10ByDurationDTO> util = new ExcelUtil<>(LiabilityDv10ByDurationDTO.class);
        util.exportTemplateExcel(response, "分中短负债基点价值DV10");
    }

    /**
     * 导入分中短负债基点价值DV10
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:dv10:by:duration:import')")
    @Log(title = "分中短负债基点价值DV10", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<LiabilityDv10ByDurationDTO> util = new ExcelUtil<>(LiabilityDv10ByDurationDTO.class);
        List<LiabilityDv10ByDurationDTO> dtoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        try {
            String message = liabilityDv10ByDurationService.importLiabilityDv10ByDurationDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
