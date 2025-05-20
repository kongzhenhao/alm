package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.utils.SecurityUtils;
import com.xl.alm.app.dto.RenewalRateStatsDTO;
import com.xl.alm.app.query.RenewalRateStatsQuery;
import com.xl.alm.app.service.RenewalRateStatsService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 保单续保率统计表 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/insu/renewal/rate/stats")
public class RenewalRateStatsController extends BaseController {

    @Autowired
    private RenewalRateStatsService renewalRateStatsService;

    /**
     * 查询保单续保率统计列表
     */
    @PreAuthorize("@ss.hasPermi('insu:renewal:rate:stats:list')")
    @GetMapping("/list")
    public TableDataInfo list(RenewalRateStatsQuery query) {
        startPage();
        List<RenewalRateStatsDTO> list = renewalRateStatsService.selectRenewalRateStatsDtoList(query);
        return getDataTable(list);
    }

    /**
     * 导出保单续保率统计列表
     */
    @PreAuthorize("@ss.hasPermi('insu:renewal:rate:stats:export')")
    @Log(title = "保单续保率统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RenewalRateStatsQuery query) {
        List<RenewalRateStatsDTO> list = renewalRateStatsService.selectRenewalRateStatsDtoList(query);
        ExcelUtil<RenewalRateStatsDTO> util = new ExcelUtil<>(RenewalRateStatsDTO.class);
        util.exportExcel(list, "保单续保率统计数据", response);
    }

    /**
     * 获取保单续保率统计详细信息
     */
    @PreAuthorize("@ss.hasPermi('insu:renewal:rate:stats:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(renewalRateStatsService.selectRenewalRateStatsDtoById(id));
    }

    /**
     * 新增保单续保率统计
     */
    @PreAuthorize("@ss.hasPermi('insu:renewal:rate:stats:add')")
    @Log(title = "保单续保率统计", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody @Valid RenewalRateStatsDTO dto) {
        dto.setCreateBy(SecurityUtils.getUsername());
        dto.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(renewalRateStatsService.insertRenewalRateStatsDto(dto));
    }

    /**
     * 修改保单续保率统计
     */
    @PreAuthorize("@ss.hasPermi('insu:renewal:rate:stats:edit')")
    @Log(title = "保单续保率统计", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody @Valid RenewalRateStatsDTO dto) {
        dto.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(renewalRateStatsService.updateRenewalRateStatsDto(dto));
    }

    /**
     * 删除保单续保率统计
     */
    @PreAuthorize("@ss.hasPermi('insu:renewal:rate:stats:remove')")
    @Log(title = "保单续保率统计", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(renewalRateStatsService.deleteRenewalRateStatsDtoByIds(ids));
    }

    /**
     * 删除指定账期的保单续保率统计
     */
    @PreAuthorize("@ss.hasPermi('insu:renewal:rate:stats:remove')")
    @Log(title = "保单续保率统计", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountingPeriod}")
    public Result removeByPeriod(@PathVariable String accountingPeriod) {
        return toAjax(renewalRateStatsService.deleteRenewalRateStatsDtoByPeriod(accountingPeriod));
    }

    /**
     * 获取保单续保率统计导入模板
     */
    @PreAuthorize("@ss.hasPermi('insu:renewal:rate:stats:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<RenewalRateStatsDTO> util = new ExcelUtil<>(RenewalRateStatsDTO.class);
        util.exportTemplateExcel(response, "保单续保率统计");
    }

    /**
     * 导入保单续保率统计数据
     */
    @PreAuthorize("@ss.hasPermi('insu:renewal:rate:stats:import')")
    @Log(title = "保单续保率统计", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<RenewalRateStatsDTO> util = new ExcelUtil<>(RenewalRateStatsDTO.class);
        List<RenewalRateStatsDTO> dtoList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = renewalRateStatsService.importRenewalRateStatsDto(dtoList, updateSupport, operName);
        return Result.success(message);
    }

    /**
     * 计算保单续保率统计
     */
    @PreAuthorize("@ss.hasPermi('insu:renewal:rate:stats:calc')")
    @Log(title = "保单续保率统计", businessType = BusinessType.OTHER)
    @PostMapping("/calc/{accountingPeriod}")
    public Result calcRenewalRateStats(@PathVariable String accountingPeriod) {
        return toAjax(renewalRateStatsService.calcRenewalRateStats(accountingPeriod));
    }
}
