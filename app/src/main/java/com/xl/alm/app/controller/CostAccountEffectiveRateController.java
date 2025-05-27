package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.utils.SecurityUtils;
import com.xl.alm.app.entity.CostAccountEffectiveRateEntity;
import com.xl.alm.app.query.CostAccountEffectiveRateQuery;
import com.xl.alm.app.service.ICostAccountEffectiveRateService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分账户有效成本率表 控制器
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/cost/account/effective/rate")
public class CostAccountEffectiveRateController extends BaseController {
    @Autowired
    private ICostAccountEffectiveRateService costAccountEffectiveRateService;

    /**
     * 查询分账户有效成本率列表
     */
    @PreAuthorize("@ss.hasPermi('cost:account:effective:rate:list')")
    @GetMapping("/list")
    public TableDataInfo list(CostAccountEffectiveRateQuery query) {
        startPage();
        List<CostAccountEffectiveRateEntity> list = costAccountEffectiveRateService.selectCostAccountEffectiveRateList(query);
        return getDataTable(list);
    }

    /**
     * 导出分账户有效成本率列表
     */
    @PreAuthorize("@ss.hasPermi('cost:account:effective:rate:export')")
    @Log(title = "分账户有效成本率", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CostAccountEffectiveRateQuery query) {
        List<CostAccountEffectiveRateEntity> list = costAccountEffectiveRateService.selectCostAccountEffectiveRateList(query);
        ExcelUtil<CostAccountEffectiveRateEntity> util = new ExcelUtil<>(CostAccountEffectiveRateEntity.class);
        util.exportExcel(list, "分账户有效成本率数据", response);
    }

    /**
     * 获取分账户有效成本率详细信息
     */
    @PreAuthorize("@ss.hasPermi('cost:account:effective:rate:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(costAccountEffectiveRateService.selectCostAccountEffectiveRateById(id));
    }

    /**
     * 新增分账户有效成本率
     */
    @PreAuthorize("@ss.hasPermi('cost:account:effective:rate:add')")
    @Log(title = "分账户有效成本率", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody CostAccountEffectiveRateEntity costAccountEffectiveRate) {
        costAccountEffectiveRate.setCreateBy(SecurityUtils.getUsername());
        return toAjax(costAccountEffectiveRateService.insertCostAccountEffectiveRate(costAccountEffectiveRate));
    }

    /**
     * 修改分账户有效成本率
     */
    @PreAuthorize("@ss.hasPermi('cost:account:effective:rate:edit')")
    @Log(title = "分账户有效成本率", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody CostAccountEffectiveRateEntity costAccountEffectiveRate) {
        costAccountEffectiveRate.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(costAccountEffectiveRateService.updateCostAccountEffectiveRate(costAccountEffectiveRate));
    }

    /**
     * 删除分账户有效成本率
     */
    @PreAuthorize("@ss.hasPermi('cost:account:effective:rate:remove')")
    @Log(title = "分账户有效成本率", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(costAccountEffectiveRateService.deleteCostAccountEffectiveRateByIds(ids));
    }

    /**
     * 导入分账户有效成本率数据
     */
    @PreAuthorize("@ss.hasPermi('cost:account:effective:rate:import')")
    @Log(title = "分账户有效成本率", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        String message = costAccountEffectiveRateService.importCostAccountEffectiveRate(file, updateSupport, SecurityUtils.getUsername());
        return Result.success(message);
    }

    /**
     * 下载分账户有效成本率模板
     */
    @PreAuthorize("@ss.hasPermi('cost:account:effective:rate:import')")
    @GetMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        costAccountEffectiveRateService.importTemplateCostAccountEffectiveRate(response);
    }
}
