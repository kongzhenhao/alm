package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.utils.SecurityUtils;
import com.xl.alm.app.entity.CostAccountSummaryEntity;
import com.xl.alm.app.query.CostAccountSummaryQuery;
import com.xl.alm.app.service.ICostAccountSummaryService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分账户汇总表 控制器
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/cost/account/summary")
public class CostAccountSummaryController extends BaseController {
    @Autowired
    private ICostAccountSummaryService costAccountSummaryService;

    /**
     * 查询分账户汇总列表
     */
    @PreAuthorize("@ss.hasPermi('cost:account:summary:list')")
    @GetMapping("/list")
    public TableDataInfo list(CostAccountSummaryQuery query) {
        startPage();
        List<CostAccountSummaryEntity> list = costAccountSummaryService.selectCostAccountSummaryList(query);
        return getDataTable(list);
    }

    /**
     * 导出分账户汇总列表
     */
    @PreAuthorize("@ss.hasPermi('cost:account:summary:export')")
    @Log(title = "分账户汇总", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CostAccountSummaryQuery query) {
        List<CostAccountSummaryEntity> list = costAccountSummaryService.selectCostAccountSummaryList(query);
        ExcelUtil<CostAccountSummaryEntity> util = new ExcelUtil<>(CostAccountSummaryEntity.class);
        util.exportExcel(list, "分账户汇总数据", response);
    }

    /**
     * 获取分账户汇总详细信息
     */
    @PreAuthorize("@ss.hasPermi('cost:account:summary:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(costAccountSummaryService.selectCostAccountSummaryById(id));
    }

    /**
     * 新增分账户汇总
     */
    @PreAuthorize("@ss.hasPermi('cost:account:summary:add')")
    @Log(title = "分账户汇总", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody CostAccountSummaryEntity costAccountSummary) {
        costAccountSummary.setCreateBy(SecurityUtils.getUsername());
        return toAjax(costAccountSummaryService.insertCostAccountSummary(costAccountSummary));
    }

    /**
     * 修改分账户汇总
     */
    @PreAuthorize("@ss.hasPermi('cost:account:summary:edit')")
    @Log(title = "分账户汇总", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody CostAccountSummaryEntity costAccountSummary) {
        costAccountSummary.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(costAccountSummaryService.updateCostAccountSummary(costAccountSummary));
    }

    /**
     * 删除分账户汇总
     */
    @PreAuthorize("@ss.hasPermi('cost:account:summary:remove')")
    @Log(title = "分账户汇总", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(costAccountSummaryService.deleteCostAccountSummaryByIds(ids));
    }

    /**
     * 导入分账户汇总数据
     */
    @PreAuthorize("@ss.hasPermi('cost:account:summary:import')")
    @Log(title = "分账户汇总", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        String message = costAccountSummaryService.importCostAccountSummary(file, updateSupport, SecurityUtils.getUsername());
        return Result.success(message);
    }

    /**
     * 下载分账户汇总模板
     */
    @PreAuthorize("@ss.hasPermi('cost:account:summary:import')")
    @GetMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        costAccountSummaryService.importTemplateCostAccountSummary(response);
    }
}
