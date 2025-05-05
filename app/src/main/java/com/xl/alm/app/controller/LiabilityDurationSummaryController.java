package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.LiabilityDurationSummaryDTO;
import com.xl.alm.app.query.LiabilityDurationSummaryQuery;
import com.xl.alm.app.service.LiabilityDurationSummaryService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 负债久期汇总 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/dur/liability/duration/summary")
public class LiabilityDurationSummaryController extends BaseController {
    @Autowired
    private LiabilityDurationSummaryService liabilityDurationSummaryService;

    /**
     * 查询负债久期汇总列表
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:duration:summary:list')")
    @GetMapping("/list")
    public TableDataInfo list(LiabilityDurationSummaryQuery query) {
        startPage();
        List<LiabilityDurationSummaryDTO> list = liabilityDurationSummaryService.selectLiabilityDurationSummaryDtoList(query);
        return getDataTable(list);
    }

    /**
     * 获取负债久期汇总详细信息
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:duration:summary:query')")
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(liabilityDurationSummaryService.selectLiabilityDurationSummaryDtoById(id));
    }

    /**
     * 根据条件查询负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:duration:summary:query')")
    @GetMapping("/condition")
    public Result getInfoByCondition(
            @RequestParam("accountPeriod") String accountPeriod,
            @RequestParam("cashFlowType") String cashFlowType,
            @RequestParam("bpType") String bpType,
            @RequestParam("durationType") String durationType,
            @RequestParam("designType") String designType,
            @RequestParam("isShortTerm") String isShortTerm) {
        return Result.success(liabilityDurationSummaryService.selectLiabilityDurationSummaryDtoByCondition(
                accountPeriod, cashFlowType, bpType, durationType, designType, isShortTerm));
    }

    /**
     * 新增负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:duration:summary:add')")
    @Log(title = "负债久期汇总", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody @Valid LiabilityDurationSummaryDTO dto) {
        return toAjax(liabilityDurationSummaryService.insertLiabilityDurationSummaryDto(dto));
    }

    /**
     * 批量新增负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:duration:summary:add')")
    @Log(title = "负债久期汇总", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<LiabilityDurationSummaryDTO> dtoList) {
        return toAjax(liabilityDurationSummaryService.batchInsertLiabilityDurationSummaryDto(dtoList));
    }

    /**
     * 修改负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:duration:summary:edit')")
    @Log(title = "负债久期汇总", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody @Valid LiabilityDurationSummaryDTO dto) {
        return toAjax(liabilityDurationSummaryService.updateLiabilityDurationSummaryDto(dto));
    }

    /**
     * 删除负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:duration:summary:remove')")
    @Log(title = "负债久期汇总", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(liabilityDurationSummaryService.deleteLiabilityDurationSummaryDtoByIds(ids));
    }

    /**
     * 删除指定账期的负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:duration:summary:remove')")
    @Log(title = "负债久期汇总", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountPeriod}")
    public Result removeByPeriod(@PathVariable String accountPeriod) {
        return toAjax(liabilityDurationSummaryService.deleteLiabilityDurationSummaryDtoByPeriod(accountPeriod));
    }

    /**
     * 导入负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:duration:summary:import')")
    @Log(title = "负债久期汇总", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<LiabilityDurationSummaryDTO> util = new ExcelUtil<>(LiabilityDurationSummaryDTO.class);
        List<LiabilityDurationSummaryDTO> dtoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        try {
            String message = liabilityDurationSummaryService.importLiabilityDurationSummaryDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 导出负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:duration:summary:export')")
    @Log(title = "负债久期汇总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LiabilityDurationSummaryQuery query) {
        ExcelUtil<LiabilityDurationSummaryDTO> util = new ExcelUtil<>(LiabilityDurationSummaryDTO.class);
        List<LiabilityDurationSummaryDTO> list = liabilityDurationSummaryService.selectLiabilityDurationSummaryDtoList(query);
        util.exportExcel(list, "负债久期汇总数据", response);
    }

    /**
     * 获取负债久期汇总模板Excel
     */
    @PreAuthorize("@ss.hasPermi('dur:liability:duration:summary:export')")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelUtil<LiabilityDurationSummaryDTO> util = new ExcelUtil<>(LiabilityDurationSummaryDTO.class);
        util.exportTemplateExcel(response, "负债久期汇总");
    }
}
