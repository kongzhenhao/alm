package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.utils.SecurityUtils;
import com.xl.alm.app.entity.CostProductEffectiveRateEntity;
import com.xl.alm.app.query.CostProductEffectiveRateQuery;
import com.xl.alm.app.service.ICostProductEffectiveRateService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分产品有效成本率表 控制器
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/cost/product/effective/rate")
public class CostProductEffectiveRateController extends BaseController {
    @Autowired
    private ICostProductEffectiveRateService costProductEffectiveRateService;

    /**
     * 查询分产品有效成本率列表
     */
    @PreAuthorize("@ss.hasPermi('cost:product:effective:rate:list')")
    @GetMapping("/list")
    public TableDataInfo list(CostProductEffectiveRateQuery query) {
        startPage();
        List<CostProductEffectiveRateEntity> list = costProductEffectiveRateService.selectCostProductEffectiveRateList(query);
        return getDataTable(list);
    }

    /**
     * 导出分产品有效成本率列表
     */
    @PreAuthorize("@ss.hasPermi('cost:product:effective:rate:export')")
    @Log(title = "分产品有效成本率", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CostProductEffectiveRateQuery query) {
        List<CostProductEffectiveRateEntity> list = costProductEffectiveRateService.selectCostProductEffectiveRateList(query);
        ExcelUtil<CostProductEffectiveRateEntity> util = new ExcelUtil<>(CostProductEffectiveRateEntity.class);
        util.exportExcel(list, "分产品有效成本率数据", response);
    }

    /**
     * 获取分产品有效成本率详细信息
     */
    @PreAuthorize("@ss.hasPermi('cost:product:effective:rate:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(costProductEffectiveRateService.selectCostProductEffectiveRateById(id));
    }

    /**
     * 新增分产品有效成本率
     */
    @PreAuthorize("@ss.hasPermi('cost:product:effective:rate:add')")
    @Log(title = "分产品有效成本率", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody CostProductEffectiveRateEntity costProductEffectiveRate) {
        costProductEffectiveRate.setCreateBy(SecurityUtils.getUsername());
        return toAjax(costProductEffectiveRateService.insertCostProductEffectiveRate(costProductEffectiveRate));
    }

    /**
     * 修改分产品有效成本率
     */
    @PreAuthorize("@ss.hasPermi('cost:product:effective:rate:edit')")
    @Log(title = "分产品有效成本率", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody CostProductEffectiveRateEntity costProductEffectiveRate) {
        costProductEffectiveRate.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(costProductEffectiveRateService.updateCostProductEffectiveRate(costProductEffectiveRate));
    }

    /**
     * 删除分产品有效成本率
     */
    @PreAuthorize("@ss.hasPermi('cost:product:effective:rate:remove')")
    @Log(title = "分产品有效成本率", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(costProductEffectiveRateService.deleteCostProductEffectiveRateByIds(ids));
    }

    /**
     * 导入分产品有效成本率数据
     */
    @PreAuthorize("@ss.hasPermi('cost:product:effective:rate:import')")
    @Log(title = "分产品有效成本率", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        String message = costProductEffectiveRateService.importCostProductEffectiveRate(file, updateSupport, SecurityUtils.getUsername());
        return Result.success(message);
    }

    /**
     * 下载分产品有效成本率模板
     */
    @PreAuthorize("@ss.hasPermi('cost:product:effective:rate:import')")
    @GetMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        costProductEffectiveRateService.importTemplateCostProductEffectiveRate(response);
    }
}
