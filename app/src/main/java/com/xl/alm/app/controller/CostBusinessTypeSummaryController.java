package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.utils.SecurityUtils;
import com.xl.alm.app.entity.CostBusinessTypeSummaryEntity;
import com.xl.alm.app.query.CostBusinessTypeSummaryQuery;
import com.xl.alm.app.service.ICostBusinessTypeSummaryService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分业务类型汇总表 控制器
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/cost/business/type/summary")
public class CostBusinessTypeSummaryController extends BaseController {
    @Autowired
    private ICostBusinessTypeSummaryService costBusinessTypeSummaryService;

    /**
     * 查询分业务类型汇总列表
     */
    @PreAuthorize("@ss.hasPermi('cost:business:type:summary:list')")
    @GetMapping("/list")
    public TableDataInfo list(CostBusinessTypeSummaryQuery query) {
        startPage();
        List<CostBusinessTypeSummaryEntity> list = costBusinessTypeSummaryService.selectCostBusinessTypeSummaryList(query);
        return getDataTable(list);
    }

    /**
     * 导出分业务类型汇总列表
     */
    @PreAuthorize("@ss.hasPermi('cost:business:type:summary:export')")
    @Log(title = "分业务类型汇总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CostBusinessTypeSummaryQuery query) {
        List<CostBusinessTypeSummaryEntity> list = costBusinessTypeSummaryService.selectCostBusinessTypeSummaryList(query);
        ExcelUtil<CostBusinessTypeSummaryEntity> util = new ExcelUtil<>(CostBusinessTypeSummaryEntity.class);
        util.exportExcel(list, "分业务类型汇总数据", response);
    }

    /**
     * 获取分业务类型汇总详细信息
     */
    @PreAuthorize("@ss.hasPermi('cost:business:type:summary:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(costBusinessTypeSummaryService.selectCostBusinessTypeSummaryById(id));
    }

    /**
     * 新增分业务类型汇总
     */
    @PreAuthorize("@ss.hasPermi('cost:business:type:summary:add')")
    @Log(title = "分业务类型汇总", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody CostBusinessTypeSummaryEntity costBusinessTypeSummary) {
        costBusinessTypeSummary.setCreateBy(SecurityUtils.getUsername());
        return toAjax(costBusinessTypeSummaryService.insertCostBusinessTypeSummary(costBusinessTypeSummary));
    }

    /**
     * 修改分业务类型汇总
     */
    @PreAuthorize("@ss.hasPermi('cost:business:type:summary:edit')")
    @Log(title = "分业务类型汇总", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody CostBusinessTypeSummaryEntity costBusinessTypeSummary) {
        costBusinessTypeSummary.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(costBusinessTypeSummaryService.updateCostBusinessTypeSummary(costBusinessTypeSummary));
    }

    /**
     * 删除分业务类型汇总
     */
    @PreAuthorize("@ss.hasPermi('cost:business:type:summary:remove')")
    @Log(title = "分业务类型汇总", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(costBusinessTypeSummaryService.deleteCostBusinessTypeSummaryByIds(ids));
    }

    /**
     * 导入分业务类型汇总数据
     */
    @PreAuthorize("@ss.hasPermi('cost:business:type:summary:import')")
    @Log(title = "分业务类型汇总", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        String message = costBusinessTypeSummaryService.importCostBusinessTypeSummary(file, updateSupport, SecurityUtils.getUsername());
        return Result.success(message);
    }

    /**
     * 下载分业务类型汇总模板
     */
    @PreAuthorize("@ss.hasPermi('cost:business:type:summary:import')")
    @GetMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        costBusinessTypeSummaryService.importTemplateCostBusinessTypeSummary(response);
    }
}
