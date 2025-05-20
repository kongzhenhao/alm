package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.RenewalEvalMonthCfgDTO;
import com.xl.alm.app.query.RenewalEvalMonthCfgQuery;
import com.xl.alm.app.service.RenewalEvalMonthCfgService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 续保率评估月份配置 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/base/renewal/eval/month/cfg")
public class RenewalEvalMonthCfgController extends BaseController {

    @Autowired
    private RenewalEvalMonthCfgService renewalEvalMonthCfgService;

    /**
     * 查询续保率评估月份配置列表
     */
    @PreAuthorize("@ss.hasPermi('base:renewal:eval:month:cfg:list')")
    @GetMapping("/list")
    public TableDataInfo list(RenewalEvalMonthCfgQuery query) {
        startPage();
        List<RenewalEvalMonthCfgDTO> list = renewalEvalMonthCfgService.selectRenewalEvalMonthCfgDtoList(query);
        return getDataTable(list);
    }

    /**
     * 导出续保率评估月份配置列表
     */
    @PreAuthorize("@ss.hasPermi('base:renewal:eval:month:cfg:export')")
    @Log(title = "续保率评估月份配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RenewalEvalMonthCfgQuery query) {
        List<RenewalEvalMonthCfgDTO> list = renewalEvalMonthCfgService.selectRenewalEvalMonthCfgDtoList(query);
        ExcelUtil<RenewalEvalMonthCfgDTO> util = new ExcelUtil<>(RenewalEvalMonthCfgDTO.class);
        util.exportExcel(list, "续保率评估月份配置数据", response);
    }

    /**
     * 获取续保率评估月份配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:renewal:eval:month:cfg:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(renewalEvalMonthCfgService.selectRenewalEvalMonthCfgDtoById(id));
    }

    /**
     * 新增续保率评估月份配置
     */
    @PreAuthorize("@ss.hasPermi('base:renewal:eval:month:cfg:add')")
    @Log(title = "续保率评估月份配置", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@Valid @RequestBody RenewalEvalMonthCfgDTO dto) {
        return toAjax(renewalEvalMonthCfgService.insertRenewalEvalMonthCfgDto(dto));
    }

    /**
     * 修改续保率评估月份配置
     */
    @PreAuthorize("@ss.hasPermi('base:renewal:eval:month:cfg:edit')")
    @Log(title = "续保率评估月份配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@Valid @RequestBody RenewalEvalMonthCfgDTO dto) {
        return toAjax(renewalEvalMonthCfgService.updateRenewalEvalMonthCfgDto(dto));
    }

    /**
     * 删除续保率评估月份配置
     */
    @PreAuthorize("@ss.hasPermi('base:renewal:eval:month:cfg:remove')")
    @Log(title = "续保率评估月份配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(renewalEvalMonthCfgService.deleteRenewalEvalMonthCfgDtoByIds(ids));
    }

    /**
     * 导入续保率评估月份配置
     */
    @PreAuthorize("@ss.hasPermi('base:renewal:eval:month:cfg:import')")
    @Log(title = "续保率评估月份配置", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<RenewalEvalMonthCfgDTO> util = new ExcelUtil<>(RenewalEvalMonthCfgDTO.class);
        List<RenewalEvalMonthCfgDTO> dtoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = renewalEvalMonthCfgService.importRenewalEvalMonthCfgDto(dtoList, updateSupport, operName);
        return Result.success(message);
    }

    /**
     * 获取续保率评估月份配置导入模板
     */
    @PreAuthorize("@ss.hasPermi('base:renewal:eval:month:cfg:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<RenewalEvalMonthCfgDTO> util = new ExcelUtil<>(RenewalEvalMonthCfgDTO.class);
        util.exportTemplateExcel(response, "续保率评估月份配置");
    }
}
