package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.PolicyDetailDTO;
import com.xl.alm.app.query.PolicyDetailQuery;
import com.xl.alm.app.service.PolicyDetailService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 保单数据明细 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/insu/policy/detail")
public class PolicyDetailController extends BaseController {

    @Autowired
    private PolicyDetailService policyDetailService;

    /**
     * 查询保单数据明细列表
     */
    @PreAuthorize("@ss.hasPermi('insu:policy:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(PolicyDetailQuery query) {
        startPage();
        List<PolicyDetailDTO> list = policyDetailService.selectPolicyDetailDtoList(query);
        return getDataTable(list);
    }

    /**
     * 导出保单数据明细列表
     */
    @PreAuthorize("@ss.hasPermi('insu:policy:detail:export')")
    @Log(title = "保单数据明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PolicyDetailQuery query) {
        List<PolicyDetailDTO> list = policyDetailService.selectPolicyDetailDtoList(query);
        ExcelUtil<PolicyDetailDTO> util = new ExcelUtil<>(PolicyDetailDTO.class);
        util.exportExcel(list, "保单数据明细数据", response);
    }

    /**
     * 获取保单数据明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('insu:policy:detail:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(policyDetailService.selectPolicyDetailDtoById(id));
    }

    /**
     * 新增保单数据明细
     */
    @PreAuthorize("@ss.hasPermi('insu:policy:detail:add')")
    @Log(title = "保单数据明细", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@Valid @RequestBody PolicyDetailDTO dto) {
        return toAjax(policyDetailService.insertPolicyDetailDto(dto));
    }

    /**
     * 修改保单数据明细
     */
    @PreAuthorize("@ss.hasPermi('insu:policy:detail:edit')")
    @Log(title = "保单数据明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@Valid @RequestBody PolicyDetailDTO dto) {
        return toAjax(policyDetailService.updatePolicyDetailDto(dto));
    }

    /**
     * 删除保单数据明细
     */
    @PreAuthorize("@ss.hasPermi('insu:policy:detail:remove')")
    @Log(title = "保单数据明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(policyDetailService.deletePolicyDetailDtoByIds(ids));
    }

    /**
     * 导入保单数据明细
     */
    @PreAuthorize("@ss.hasPermi('insu:policy:detail:import')")
    @Log(title = "保单数据明细", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<PolicyDetailDTO> util = new ExcelUtil<>(PolicyDetailDTO.class);
        List<PolicyDetailDTO> dtoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = policyDetailService.importPolicyDetailDto(dtoList, updateSupport, operName);
        return Result.success(message);
    }

    /**
     * 获取保单数据明细导入模板
     */
    @PreAuthorize("@ss.hasPermi('insu:policy:detail:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<PolicyDetailDTO> util = new ExcelUtil<>(PolicyDetailDTO.class);
        util.exportTemplateExcel(response, "保单数据明细");
    }
}
