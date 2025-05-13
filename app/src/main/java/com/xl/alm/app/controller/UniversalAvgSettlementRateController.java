package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.utils.poi.ExcelUtil;
import com.jd.lightning.common.utils.SecurityUtils;
import com.xl.alm.app.entity.UniversalAvgSettlementRateEntity;
import com.xl.alm.app.query.UniversalAvgSettlementRateQuery;
import com.xl.alm.app.service.IUniversalAvgSettlementRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 万能平均结算利率表 控制器
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/base/universal/settlement")
public class UniversalAvgSettlementRateController extends BaseController {
    @Autowired
    private IUniversalAvgSettlementRateService universalAvgSettlementRateService;

    /**
     * 查询万能平均结算利率列表
     */
    @PreAuthorize("@ss.hasPermi('base:universal:settlement:list')")
    @GetMapping("/list")
    public TableDataInfo list(UniversalAvgSettlementRateQuery query) {
        startPage();
        List<UniversalAvgSettlementRateEntity> list = universalAvgSettlementRateService.selectUniversalAvgSettlementRateList(query);
        return getDataTable(list);
    }

    /**
     * 导出万能平均结算利率列表
     */
    @PreAuthorize("@ss.hasPermi('base:universal:settlement:export')")
    @Log(title = "万能平均结算利率", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UniversalAvgSettlementRateQuery query) {
        List<UniversalAvgSettlementRateEntity> list = universalAvgSettlementRateService.selectUniversalAvgSettlementRateList(query);
        ExcelUtil<UniversalAvgSettlementRateEntity> util = new ExcelUtil<>(UniversalAvgSettlementRateEntity.class);
        util.exportExcel(response, list, "万能平均结算利率数据");
    }

    /**
     * 获取万能平均结算利率详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:universal:settlement:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return success(universalAvgSettlementRateService.selectUniversalAvgSettlementRateById(id));
    }

    /**
     * 新增万能平均结算利率
     */
    @PreAuthorize("@ss.hasPermi('base:universal:settlement:add')")
    @Log(title = "万能平均结算利率", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody UniversalAvgSettlementRateEntity universalAvgSettlementRate) {
        universalAvgSettlementRate.setCreateBy(SecurityUtils.getUsername());
        return toAjax(universalAvgSettlementRateService.insertUniversalAvgSettlementRate(universalAvgSettlementRate));
    }

    /**
     * 修改万能平均结算利率
     */
    @PreAuthorize("@ss.hasPermi('base:universal:settlement:edit')")
    @Log(title = "万能平均结算利率", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody UniversalAvgSettlementRateEntity universalAvgSettlementRate) {
        universalAvgSettlementRate.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(universalAvgSettlementRateService.updateUniversalAvgSettlementRate(universalAvgSettlementRate));
    }

    /**
     * 删除万能平均结算利率
     */
    @PreAuthorize("@ss.hasPermi('base:universal:settlement:remove')")
    @Log(title = "万能平均结算利率", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(universalAvgSettlementRateService.deleteUniversalAvgSettlementRateByIds(ids));
    }

    /**
     * 导入万能平均结算利率数据
     */
    @PreAuthorize("@ss.hasPermi('base:universal:settlement:import')")
    @Log(title = "万能平均结算利率", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        String operName = SecurityUtils.getUsername();
        String message = universalAvgSettlementRateService.importUniversalAvgSettlementRate(file, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载万能平均结算利率导入模板
     */
    @PreAuthorize("@ss.hasPermi('base:universal:settlement:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        universalAvgSettlementRateService.importTemplateUniversalAvgSettlementRate(response);
    }
}
