package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.LiabilityCashFlowSummaryDTO;
import com.xl.alm.app.query.LiabilityCashFlowSummaryQuery;
import com.xl.alm.app.service.LiabilityCashFlowSummaryService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 负债现金流汇总表 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/dur/liability/cash/flow/summary")
public class LiabilityCashFlowSummaryController extends BaseController {

    @Autowired
    private LiabilityCashFlowSummaryService liabilityCashFlowSummaryService;

    /**
     * 查询负债现金流汇总列表
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:summary:list')")
    @GetMapping("/list")
    public TableDataInfo list(LiabilityCashFlowSummaryQuery query) {
        startPage();
        List<LiabilityCashFlowSummaryDTO> list = liabilityCashFlowSummaryService.selectLiabilityCashFlowSummaryDtoList(query);
        return getDataTable(list);
    }

    /**
     * 获取负债现金流汇总详细信息
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:summary:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(liabilityCashFlowSummaryService.selectLiabilityCashFlowSummaryDtoById(id));
    }

    /**
     * 根据条件查询负债现金流汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:summary:query')")
    @GetMapping("/condition")
    public Result getByCondition(
            @RequestParam("accountPeriod") String accountPeriod,
            @RequestParam("cashFlowType") String cashFlowType,
            @RequestParam("durationType") String durationType,
            @RequestParam(value = "designType", required = false) String designType,
            @RequestParam(value = "isShortTerm", required = false) String isShortTerm) {
        return Result.success(liabilityCashFlowSummaryService.selectLiabilityCashFlowSummaryDtoByCondition(
                accountPeriod, cashFlowType, durationType, designType, isShortTerm));
    }

    /**
     * 新增负债现金流汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:summary:add')")
    @Log(title = "负债现金流汇总", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody @Valid LiabilityCashFlowSummaryDTO dto) {
        return toAjax(liabilityCashFlowSummaryService.insertLiabilityCashFlowSummaryDto(dto));
    }

    /**
     * 批量新增负债现金流汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:summary:add')")
    @Log(title = "负债现金流汇总", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<LiabilityCashFlowSummaryDTO> dtoList) {
        return toAjax(liabilityCashFlowSummaryService.batchInsertLiabilityCashFlowSummaryDto(dtoList));
    }

    /**
     * 修改负债现金流汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:summary:edit')")
    @Log(title = "负债现金流汇总", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody @Valid LiabilityCashFlowSummaryDTO dto) {
        return toAjax(liabilityCashFlowSummaryService.updateLiabilityCashFlowSummaryDto(dto));
    }

    /**
     * 删除负债现金流汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:summary:remove')")
    @Log(title = "负债现金流汇总", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(liabilityCashFlowSummaryService.deleteLiabilityCashFlowSummaryDtoByIds(ids));
    }

    /**
     * 删除指定账期的负债现金流汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:summary:remove')")
    @Log(title = "负债现金流汇总", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountPeriod}")
    public Result removeByPeriod(@PathVariable String accountPeriod) {
        return toAjax(liabilityCashFlowSummaryService.deleteLiabilityCashFlowSummaryDtoByPeriod(accountPeriod));
    }

    /**
     * 导入负债现金流汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:summary:import')")
    @Log(title = "负债现金流汇总", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<LiabilityCashFlowSummaryDTO> util = new ExcelUtil<>(LiabilityCashFlowSummaryDTO.class);
        List<LiabilityCashFlowSummaryDTO> dtoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        try {
            String message = liabilityCashFlowSummaryService.importLiabilityCashFlowSummaryDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 导出负债现金流汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:summary:export')")
    @Log(title = "负债现金流汇总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LiabilityCashFlowSummaryQuery query) {
        ExcelUtil<LiabilityCashFlowSummaryDTO> util = new ExcelUtil<>(LiabilityCashFlowSummaryDTO.class);
        List<LiabilityCashFlowSummaryDTO> list = liabilityCashFlowSummaryService.selectLiabilityCashFlowSummaryDtoList(query);
        util.exportExcel(list, "负债现金流汇总数据", response);
    }

    /**
     * 获取负债现金流汇总模板Excel
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:cash:flow:summary:export')")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelUtil<LiabilityCashFlowSummaryDTO> util = new ExcelUtil<>(LiabilityCashFlowSummaryDTO.class);
        util.exportTemplateExcel(response, "负债现金流汇总");
    }
}
