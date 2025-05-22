package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.utils.SecurityUtils;
import com.xl.alm.app.entity.CostProductStatisticsEntity;
import com.xl.alm.app.query.CostProductStatisticsQuery;
import com.xl.alm.app.service.ICostProductStatisticsService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分产品统计表 控制器
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/cost/product/statistics")
public class CostProductStatisticsController extends BaseController {
    @Autowired
    private ICostProductStatisticsService costProductStatisticsService;

    /**
     * 查询分产品统计列表
     */
    @PreAuthorize("@ss.hasPermi('cost:product:statistics:list')")
    @GetMapping("/list")
    public TableDataInfo list(CostProductStatisticsQuery query) {
        startPage();
        List<CostProductStatisticsEntity> list = costProductStatisticsService.selectCostProductStatisticsList(query);
        return getDataTable(list);
    }

    /**
     * 导出分产品统计列表
     */
    @PreAuthorize("@ss.hasPermi('cost:product:statistics:export')")
    @Log(title = "分产品统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CostProductStatisticsQuery query) {
        List<CostProductStatisticsEntity> list = costProductStatisticsService.selectCostProductStatisticsList(query);
        ExcelUtil<CostProductStatisticsEntity> util = new ExcelUtil<>(CostProductStatisticsEntity.class);
        util.exportExcel(list, "分产品统计数据", response);
    }

    /**
     * 获取分产品统计详细信息
     */
    @PreAuthorize("@ss.hasPermi('cost:product:statistics:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(costProductStatisticsService.selectCostProductStatisticsById(id));
    }

    /**
     * 新增分产品统计
     */
    @PreAuthorize("@ss.hasPermi('cost:product:statistics:add')")
    @Log(title = "分产品统计", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody CostProductStatisticsEntity costProductStatistics) {
        costProductStatistics.setCreateBy(SecurityUtils.getUsername());
        return toAjax(costProductStatisticsService.insertCostProductStatistics(costProductStatistics));
    }

    /**
     * 修改分产品统计
     */
    @PreAuthorize("@ss.hasPermi('cost:product:statistics:edit')")
    @Log(title = "分产品统计", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody CostProductStatisticsEntity costProductStatistics) {
        costProductStatistics.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(costProductStatisticsService.updateCostProductStatistics(costProductStatistics));
    }

    /**
     * 删除分产品统计
     */
    @PreAuthorize("@ss.hasPermi('cost:product:statistics:remove')")
    @Log(title = "分产品统计", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(costProductStatisticsService.deleteCostProductStatisticsByIds(ids));
    }

    /**
     * 导入分产品统计数据
     */
    @PreAuthorize("@ss.hasPermi('cost:product:statistics:import')")
    @Log(title = "分产品统计", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        String message = costProductStatisticsService.importCostProductStatistics(file, updateSupport, SecurityUtils.getUsername());
        return Result.success(message);
    }

    /**
     * 下载分产品统计模板
     */
    @PreAuthorize("@ss.hasPermi('cost:product:statistics:import')")
    @GetMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        costProductStatisticsService.importTemplateCostProductStatistics(response);
    }
}
