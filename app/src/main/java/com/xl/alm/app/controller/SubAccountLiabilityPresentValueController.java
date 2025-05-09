package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.SubAccountLiabilityPresentValueDTO;
import com.xl.alm.app.query.SubAccountLiabilityPresentValueQuery;
import com.xl.alm.app.service.SubAccountLiabilityPresentValueService;
import com.xl.alm.app.util.ExcelUtil;
import com.xl.alm.app.util.ValueSetExcelExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 分账户负债现金流现值汇总 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/dur/sub/account/liability/present/value")
public class SubAccountLiabilityPresentValueController extends BaseController {
    @Autowired
    private SubAccountLiabilityPresentValueService subAccountLiabilityPresentValueService;

    /**
     * 查询分账户负债现金流现值汇总列表
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:present:value:list')")
    @GetMapping("/list")
    public TableDataInfo list(SubAccountLiabilityPresentValueQuery query) {
        startPage();
        List<SubAccountLiabilityPresentValueDTO> list = subAccountLiabilityPresentValueService.selectSubAccountLiabilityPresentValueDtoList(query);
        return getDataTable(list);
    }

    /**
     * 导出分账户负债现金流现值汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:present:value:export')")
    @Log(title = "分账户负债现金流现值汇总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SubAccountLiabilityPresentValueQuery query) {
        List<SubAccountLiabilityPresentValueDTO> list = subAccountLiabilityPresentValueService.selectSubAccountLiabilityPresentValueDtoList(query);
        // 使用自定义的ValueSetExcelExporter导出，处理presentCashValSet字段
        ValueSetExcelExporter.exportExcel(list, "分账户负债现金流现值汇总数据", response, "presentCashValSet");
    }

    /**
     * 获取分账户负债现金流现值汇总模板Excel
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:present:value:import')")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelUtil<SubAccountLiabilityPresentValueDTO> util = new ExcelUtil<>(SubAccountLiabilityPresentValueDTO.class);
        util.exportTemplateExcel(response, "分账户负债现金流现值汇总");
    }

    /**
     * 导入分账户负债现金流现值汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:present:value:import')")
    @Log(title = "分账户负债现金流现值汇总", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SubAccountLiabilityPresentValueDTO> util = new ExcelUtil<>(SubAccountLiabilityPresentValueDTO.class);
        List<SubAccountLiabilityPresentValueDTO> dtoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        try {
            String message = subAccountLiabilityPresentValueService.importSubAccountLiabilityPresentValueDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取分账户负债现金流现值汇总详细信息
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:present:value:query')")
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(subAccountLiabilityPresentValueService.selectSubAccountLiabilityPresentValueDtoById(id));
    }

    /**
     * 根据条件查询分账户负债现金流现值汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:present:value:query')")
    @GetMapping("/condition")
    public Result getInfoByCondition(
            @RequestParam("accountPeriod") String accountPeriod,
            @RequestParam("cashFlowType") String cashFlowType,
            @RequestParam("bpType") String bpType,
            @RequestParam("durationType") String durationType,
            @RequestParam("designType") String designType) {
        return Result.success(subAccountLiabilityPresentValueService.selectSubAccountLiabilityPresentValueDtoByCondition(
                accountPeriod, cashFlowType, bpType, durationType, designType));
    }

    /**
     * 新增分账户负债现金流现值汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:present:value:add')")
    @Log(title = "分账户负债现金流现值汇总", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody @Valid SubAccountLiabilityPresentValueDTO dto) {
        return toAjax(subAccountLiabilityPresentValueService.insertSubAccountLiabilityPresentValueDto(dto));
    }

    /**
     * 批量新增分账户负债现金流现值汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:present:value:add')")
    @Log(title = "分账户负债现金流现值汇总", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<SubAccountLiabilityPresentValueDTO> dtoList) {
        return toAjax(subAccountLiabilityPresentValueService.batchInsertSubAccountLiabilityPresentValueDto(dtoList));
    }

    /**
     * 修改分账户负债现金流现值汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:present:value:edit')")
    @Log(title = "分账户负债现金流现值汇总", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody @Valid SubAccountLiabilityPresentValueDTO dto) {
        return toAjax(subAccountLiabilityPresentValueService.updateSubAccountLiabilityPresentValueDto(dto));
    }

    /**
     * 删除分账户负债现金流现值汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:present:value:remove')")
    @Log(title = "分账户负债现金流现值汇总", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(subAccountLiabilityPresentValueService.deleteSubAccountLiabilityPresentValueDtoByIds(ids));
    }

    /**
     * 删除指定账期的分账户负债现金流现值汇总
     */
    @PreAuthorize("@ss.hasPermi('dur:sub:account:liability:present:value:remove')")
    @Log(title = "分账户负债现金流现值汇总", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountPeriod}")
    public Result removeByPeriod(@PathVariable("accountPeriod") String accountPeriod) {
        return toAjax(subAccountLiabilityPresentValueService.deleteSubAccountLiabilityPresentValueDtoByPeriod(accountPeriod));
    }
}
