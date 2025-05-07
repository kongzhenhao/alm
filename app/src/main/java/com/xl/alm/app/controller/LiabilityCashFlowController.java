package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.LiabilityCashFlowDTO;
import com.xl.alm.app.query.LiabilityCashFlowQuery;
import com.xl.alm.app.service.LiabilityCashFlowService;
import com.xl.alm.app.util.ExcelUtil;
import com.xl.alm.app.util.ValueSetExcelExporter;
import com.xl.alm.app.util.ValueSetExcelImportListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 负债现金流表 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/dur/liability/cash/flow")
public class LiabilityCashFlowController extends BaseController {

    @Autowired
    private LiabilityCashFlowService liabilityCashFlowService;

    /**
     * 查询负债现金流列表
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:list')")
    @GetMapping("/list")
    public TableDataInfo list(LiabilityCashFlowQuery query) {
        startPage();
        List<LiabilityCashFlowDTO> list = liabilityCashFlowService.selectLiabilityCashFlowDtoList(query);
        return getDataTable(list);
    }

    /**
     * 获取负债现金流详细信息
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(liabilityCashFlowService.selectLiabilityCashFlowDtoById(id));
    }

    /**
     * 根据条件查询负债现金流
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:query')")
    @GetMapping("/condition")
    public Result getByCondition(
            @RequestParam("accountPeriod") String accountPeriod,
            @RequestParam("cashFlowType") String cashFlowType,
            @RequestParam("bpType") String bpType,
            @RequestParam("durationType") String durationType,
            @RequestParam(value = "designType", required = false) String designType,
            @RequestParam(value = "isShortTerm", required = false) String isShortTerm,
            @RequestParam(value = "actuarialCode", required = false) String actuarialCode) {
        return Result.success(liabilityCashFlowService.selectLiabilityCashFlowDtoByCondition(
                accountPeriod, cashFlowType, bpType, durationType, designType, isShortTerm, actuarialCode));
    }

    /**
     * 新增负债现金流
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:add')")
    @Log(title = "负债现金流", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody LiabilityCashFlowDTO dto) {
        List<LiabilityCashFlowDTO> list = new java.util.ArrayList<>();
        list.add(dto);
        return toAjax(liabilityCashFlowService.batchInsertLiabilityCashFlowDto(list));
    }

    /**
     * 批量新增负债现金流
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:add')")
    @Log(title = "负债现金流", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<LiabilityCashFlowDTO> dtoList) {
        return toAjax(liabilityCashFlowService.batchInsertLiabilityCashFlowDto(dtoList));
    }

    /**
     * 修改负债现金流
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:edit')")
    @Log(title = "负债现金流", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody LiabilityCashFlowDTO dto) {
        return toAjax(liabilityCashFlowService.updateLiabilityCashFlowDto(dto));
    }

    /**
     * 删除指定账期的负债现金流
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:remove')")
    @Log(title = "负债现金流", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountPeriod}")
    public Result removeByPeriod(@PathVariable String accountPeriod) {
        return toAjax(liabilityCashFlowService.deleteLiabilityCashFlowDtoByPeriod(accountPeriod));
    }

    /**
     * 删除负债现金流
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:remove')")
    @Log(title = "负债现金流", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result remove(@PathVariable Long id) {
        return toAjax(liabilityCashFlowService.deleteLiabilityCashFlowDtoById(id));
    }

    /**
     * 批量删除负债现金流
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:remove')")
    @Log(title = "负债现金流", businessType = BusinessType.DELETE)
    @DeleteMapping("/batch/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(liabilityCashFlowService.deleteLiabilityCashFlowDtoByIds(ids));
    }

    /**
     * 导入负债现金流
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:import')")
    @Log(title = "负债现金流", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        try {
            // 使用自定义的ValueSetExcelImportListener处理Excel导入
            // 将表头列名从0开始至1272的列转成JSON串赋值到cashValSet字段中
            ValueSetExcelImportListener<LiabilityCashFlowDTO> listener = new ValueSetExcelImportListener<>(LiabilityCashFlowDTO.class, "cashValSet");

            // 读取Excel文件
            com.alibaba.excel.EasyExcel.read(file.getInputStream())
                    .registerReadListener(listener)
                    .sheet()
                    .headRowNumber(2) // 设置表头行数为2，确保不跳过表头行
                    .doRead();

            // 获取处理后的数据列表
            List<LiabilityCashFlowDTO> dtoList = listener.getResultList();

            // 导入数据
            String operName = getUsername();
            String message = liabilityCashFlowService.importLiabilityCashFlowDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            logger.error("导入负债现金流失败", e);
            return Result.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出负债现金流
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:export')")
    @Log(title = "负债现金流", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LiabilityCashFlowQuery query) {
        List<LiabilityCashFlowDTO> list = liabilityCashFlowService.selectLiabilityCashFlowDtoList(query);
        // 使用自定义的ValueSetExcelExporter导出，处理cashValSet字段
        // 表头全部用中文，编码值通过字典转为中文
        ValueSetExcelExporter.exportExcel(list, "负债现金流数据", response, "cashValSet");
    }

    /**
     * 获取负债现金流模板Excel
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:export')")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        // 使用自定义的ValueSetExcelExporter导出模板，处理cashValSet字段
        // 序号从0开始，共有1273项，序号为0的列日期显示为上一月最后一天的日期，随序号递增加一个自然月显示日期
        ValueSetExcelExporter.exportTemplateExcel(LiabilityCashFlowDTO.class, "负债现金流模板", response, "cashValSet");
    }
}
