package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.utils.SecurityUtils;
import com.xl.alm.app.entity.DividendFundCostRateEntity;
import com.xl.alm.app.query.DividendFundCostRateQuery;
import com.xl.alm.app.service.IDividendFundCostRateService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分红方案表 控制器
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/base/dividend/fund")
public class DividendFundCostRateController extends BaseController {
    @Autowired
    private IDividendFundCostRateService dividendFundCostRateService;

    /**
     * 查询分红方案列表
     */
    @PreAuthorize("@ss.hasPermi('base:dividend:fund:list')")
    @GetMapping("/list")
    public TableDataInfo list(DividendFundCostRateQuery query) {
        startPage();
        List<DividendFundCostRateEntity> list = dividendFundCostRateService.selectDividendFundCostRateList(query);
        return getDataTable(list);
    }

    /**
     * 导出分红方案列表
     */
    @PreAuthorize("@ss.hasPermi('base:dividend:fund:export')")
    @Log(title = "分红方案", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DividendFundCostRateQuery query) {
        List<DividendFundCostRateEntity> list = dividendFundCostRateService.selectDividendFundCostRateList(query);
        ExcelUtil<DividendFundCostRateEntity> util = new ExcelUtil<>(DividendFundCostRateEntity.class);
        util.exportExcel(list, "分红方案数据", response);
    }

    /**
     * 获取分红方案详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:dividend:fund:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return success(dividendFundCostRateService.selectDividendFundCostRateById(id));
    }

    /**
     * 新增分红方案
     */
    @PreAuthorize("@ss.hasPermi('base:dividend:fund:add')")
    @Log(title = "分红方案", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody DividendFundCostRateEntity dividendFundCostRate) {
        dividendFundCostRate.setCreateBy(SecurityUtils.getUsername());
        return toAjax(dividendFundCostRateService.insertDividendFundCostRate(dividendFundCostRate));
    }

    /**
     * 修改分红方案
     */
    @PreAuthorize("@ss.hasPermi('base:dividend:fund:edit')")
    @Log(title = "分红方案", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody DividendFundCostRateEntity dividendFundCostRate) {
        dividendFundCostRate.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(dividendFundCostRateService.updateDividendFundCostRate(dividendFundCostRate));
    }

    /**
     * 删除分红方案
     */
    @PreAuthorize("@ss.hasPermi('base:dividend:fund:remove')")
    @Log(title = "分红方案", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(dividendFundCostRateService.deleteDividendFundCostRateByIds(ids));
    }

    /**
     * 导入分红方案数据
     */
    @PreAuthorize("@ss.hasPermi('base:dividend:fund:import')")
    @Log(title = "分红方案", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        String operName = SecurityUtils.getUsername();
        String message = dividendFundCostRateService.importDividendFundCostRate(file, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载分红方案导入模板
     */
    @PreAuthorize("@ss.hasPermi('base:dividend:fund:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        dividendFundCostRateService.importTemplateDividendFundCostRate(response);
    }
}
