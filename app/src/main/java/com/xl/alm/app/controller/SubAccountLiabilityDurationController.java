package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.SubAccountLiabilityDurationDTO;
import com.xl.alm.app.query.SubAccountLiabilityDurationQuery;
import com.xl.alm.app.service.SubAccountLiabilityDurationService;
import com.xl.alm.app.util.ExcelUtil;
import com.xl.alm.app.util.ValueSetExcelExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分账户负债久期汇总表 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/dur/sub/account/liability/duration")
public class SubAccountLiabilityDurationController extends BaseController {

    @Autowired
    private SubAccountLiabilityDurationService subAccountLiabilityDurationService;

    /**
     * 查询分账户负债久期汇总列表
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:duration:list')")
    @GetMapping("/list")
    public TableDataInfo list(SubAccountLiabilityDurationQuery query) {
        startPage();
        List<SubAccountLiabilityDurationDTO> list = subAccountLiabilityDurationService.selectSubAccountLiabilityDurationDtoList(query);
        return getDataTable(list);
    }

    /**
     * 获取分账户负债久期汇总详细信息
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:duration:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(subAccountLiabilityDurationService.selectSubAccountLiabilityDurationDtoById(id));
    }

    /**
     * 根据条件查询分账户负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:duration:query')")
    @GetMapping("/condition")
    public Result getInfoByCondition(
            @RequestParam("accountPeriod") String accountPeriod,
            @RequestParam("cashFlowType") String cashFlowType,
            @RequestParam("durationType") String durationType,
            @RequestParam("designType") String designType) {
        return Result.success(subAccountLiabilityDurationService.selectSubAccountLiabilityDurationDtoByCondition(
                accountPeriod, cashFlowType, durationType, designType));
    }

    /**
     * 新增分账户负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:duration:add')")
    @Log(title = "分账户负债久期汇总", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody SubAccountLiabilityDurationDTO dto) {
        return toAjax(subAccountLiabilityDurationService.insertSubAccountLiabilityDurationDto(dto));
    }

    /**
     * 批量新增分账户负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:duration:add')")
    @Log(title = "分账户负债久期汇总", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<SubAccountLiabilityDurationDTO> dtoList) {
        return toAjax(subAccountLiabilityDurationService.batchInsertSubAccountLiabilityDurationDto(dtoList));
    }

    /**
     * 修改分账户负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:duration:edit')")
    @Log(title = "分账户负债久期汇总", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody SubAccountLiabilityDurationDTO dto) {
        return toAjax(subAccountLiabilityDurationService.updateSubAccountLiabilityDurationDto(dto));
    }

    /**
     * 删除分账户负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:duration:remove')")
    @Log(title = "分账户负债久期汇总", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(subAccountLiabilityDurationService.deleteSubAccountLiabilityDurationDtoByIds(ids));
    }

    /**
     * 删除指定账期的分账户负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:duration:remove')")
    @Log(title = "分账户负债久期汇总", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountPeriod}")
    public Result removeByPeriod(@PathVariable String accountPeriod) {
        return toAjax(subAccountLiabilityDurationService.deleteSubAccountLiabilityDurationDtoByPeriod(accountPeriod));
    }

    /**
     * 导出分账户负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:duration:export')")
    @Log(title = "分账户负债久期汇总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SubAccountLiabilityDurationQuery query) {
        List<SubAccountLiabilityDurationDTO> list = subAccountLiabilityDurationService.selectSubAccountLiabilityDurationDtoList(query);
        // 使用自定义的ValueSetExcelExporter导出，处理durationValSet字段
        ValueSetExcelExporter.exportExcel(list, "分账户负债久期汇总数据", response, "durationValSet");
    }

    /**
     * 获取分账户负债久期汇总模板Excel
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:duration:import')")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelUtil<SubAccountLiabilityDurationDTO> util = new ExcelUtil<>(SubAccountLiabilityDurationDTO.class);
        util.exportTemplateExcel(response, "分账户负债久期汇总");
    }

    /**
     * 导入分账户负债久期汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:duration:import')")
    @Log(title = "分账户负债久期汇总", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SubAccountLiabilityDurationDTO> util = new ExcelUtil<>(SubAccountLiabilityDurationDTO.class);
        List<SubAccountLiabilityDurationDTO> dtoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        try {
            String message = subAccountLiabilityDurationService.importSubAccountLiabilityDurationDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
