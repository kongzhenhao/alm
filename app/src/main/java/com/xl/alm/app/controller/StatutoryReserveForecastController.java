package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.utils.SecurityUtils;
import com.xl.alm.app.entity.StatutoryReserveForecastEntity;
import com.xl.alm.app.query.StatutoryReserveForecastQuery;
import com.xl.alm.app.service.IStatutoryReserveForecastService;
import com.xl.alm.app.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 法定准备金预测表 控制器
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/base/statutory/forecast")
public class StatutoryReserveForecastController extends BaseController {
    @Autowired
    private IStatutoryReserveForecastService statutoryReserveForecastService;

    /**
     * 查询法定准备金预测列表
     */
    @PreAuthorize("@ss.hasPermi('base:statutory:forecast:list')")
    @GetMapping("/list")
    public TableDataInfo list(StatutoryReserveForecastQuery query) {
        startPage();
        List<StatutoryReserveForecastEntity> list = statutoryReserveForecastService.selectStatutoryReserveForecastList(query);
        return getDataTable(list);
    }

    /**
     * 导出法定准备金预测列表
     */
    @PreAuthorize("@ss.hasPermi('base:statutory:forecast:export')")
    @Log(title = "法定准备金预测", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StatutoryReserveForecastQuery query) {
        List<StatutoryReserveForecastEntity> list = statutoryReserveForecastService.selectStatutoryReserveForecastList(query);
        ExcelUtil<StatutoryReserveForecastEntity> util = new ExcelUtil<>(StatutoryReserveForecastEntity.class);
        util.exportExcel(list, "法定准备金预测数据", response);
    }

    /**
     * 获取法定准备金预测详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:statutory:forecast:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return success(statutoryReserveForecastService.selectStatutoryReserveForecastById(id));
    }

    /**
     * 新增法定准备金预测
     */
    @PreAuthorize("@ss.hasPermi('base:statutory:forecast:add')")
    @Log(title = "法定准备金预测", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody StatutoryReserveForecastEntity statutoryReserveForecast) {
        statutoryReserveForecast.setCreateBy(SecurityUtils.getUsername());
        return toAjax(statutoryReserveForecastService.insertStatutoryReserveForecast(statutoryReserveForecast));
    }

    /**
     * 修改法定准备金预测
     */
    @PreAuthorize("@ss.hasPermi('base:statutory:forecast:edit')")
    @Log(title = "法定准备金预测", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody StatutoryReserveForecastEntity statutoryReserveForecast) {
        statutoryReserveForecast.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(statutoryReserveForecastService.updateStatutoryReserveForecast(statutoryReserveForecast));
    }

    /**
     * 删除法定准备金预测
     */
    @PreAuthorize("@ss.hasPermi('base:statutory:forecast:remove')")
    @Log(title = "法定准备金预测", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(statutoryReserveForecastService.deleteStatutoryReserveForecastByIds(ids));
    }

    /**
     * 导入法定准备金预测数据
     */
    @PreAuthorize("@ss.hasPermi('base:statutory:forecast:import')")
    @Log(title = "法定准备金预测", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        String operName = SecurityUtils.getUsername();
        String message = statutoryReserveForecastService.importStatutoryReserveForecast(file, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载法定准备金预测导入模板
     */
    @PreAuthorize("@ss.hasPermi('base:statutory:forecast:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        statutoryReserveForecastService.importTemplateStatutoryReserveForecast(response);
    }
}
