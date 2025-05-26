package com.xl.alm.app.controller;

import com.alibaba.excel.EasyExcel;
import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.KeyDurationDiscountFactorsDTO;
import com.xl.alm.app.query.KeyDurationDiscountFactorsQuery;
import com.xl.alm.app.service.KeyDurationDiscountFactorsService;

import com.xl.alm.app.util.ValueSetExcelExporter;
import com.xl.alm.app.util.ValueSetExcelImportListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 关键久期折现因子表 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/dur/key/duration/discount/factors")
public class KeyDurationDiscountFactorsController extends BaseController {

    @Autowired
    private KeyDurationDiscountFactorsService keyDurationDiscountFactorsService;

    /**
     * 查询关键久期折现因子列表
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:factors:list')")
    @GetMapping("/list")
    public TableDataInfo list(KeyDurationDiscountFactorsQuery query) {
        startPage();
        List<KeyDurationDiscountFactorsDTO> list = keyDurationDiscountFactorsService.selectKeyDurationDiscountFactorsDtoList(query);
        return getDataTable(list);
    }

    /**
     * 获取关键久期折现因子详细信息
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:factors:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(keyDurationDiscountFactorsService.selectKeyDurationDiscountFactorsDtoById(id));
    }

    /**
     * 根据条件查询关键久期折现因子
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:factors:query')")
    @GetMapping("/condition")
    public Result getByCondition(
            @RequestParam("accountPeriod") String accountPeriod,
            @RequestParam("curveType") String curveType,
            @RequestParam("keyDuration") String keyDuration,
            @RequestParam("stressDirection") String stressDirection,
            @RequestParam("durationType") String durationType) {
        return Result.success(keyDurationDiscountFactorsService.selectKeyDurationDiscountFactorsDtoByCondition(
                accountPeriod, curveType, keyDuration, stressDirection, durationType));
    }

    /**
     * 新增关键久期折现因子
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:factors:add')")
    @Log(title = "关键久期折现因子", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody @Valid KeyDurationDiscountFactorsDTO dto) {
        return toAjax(keyDurationDiscountFactorsService.insertKeyDurationDiscountFactorsDto(dto));
    }

    /**
     * 修改关键久期折现因子
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:factors:edit')")
    @Log(title = "关键久期折现因子", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody @Valid KeyDurationDiscountFactorsDTO dto) {
        return toAjax(keyDurationDiscountFactorsService.updateKeyDurationDiscountFactorsDto(dto));
    }

    /**
     * 删除关键久期折现因子
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:factors:remove')")
    @Log(title = "关键久期折现因子", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(keyDurationDiscountFactorsService.deleteKeyDurationDiscountFactorsDtoByIds(ids));
    }

    /**
     * 删除指定账期的关键久期折现因子
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:factors:remove')")
    @Log(title = "关键久期折现因子", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountPeriod}")
    public Result removeByPeriod(@PathVariable String accountPeriod) {
        return toAjax(keyDurationDiscountFactorsService.deleteKeyDurationDiscountFactorsDtoByPeriod(accountPeriod));
    }

    /**
     * 导出关键久期折现因子
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:factors:export')")
    @Log(title = "关键久期折现因子", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KeyDurationDiscountFactorsQuery query) {
        List<KeyDurationDiscountFactorsDTO> list = keyDurationDiscountFactorsService.selectKeyDurationDiscountFactorsDtoList(query);
        // 使用值集导出工具类导出
        ValueSetExcelExporter.exportExcel(list, "关键久期折现因子数据", response, "factorValSet");
    }

    /**
     * 获取关键久期折现因子模板Excel
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:factors:import')")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        // 使用自定义的ValueSetExcelExporter导出模板，处理factorValSet字段
        ValueSetExcelExporter.exportTemplateExcel(KeyDurationDiscountFactorsDTO.class, "关键久期折现因子模板", response, "factorValSet");
    }

    /**
     * 导入关键久期折现因子
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:factors:import')")
    @Log(title = "关键久期折现因子", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        try {
            // 使用自定义的ValueSetExcelImportListener处理Excel导入
            // 将表头列名从0开始至1273的列转成JSON串赋值到factorValSet字段中
            ValueSetExcelImportListener<KeyDurationDiscountFactorsDTO> listener = new ValueSetExcelImportListener<>(KeyDurationDiscountFactorsDTO.class, "factorValSet");

            // 读取Excel文件
            EasyExcel.read(file.getInputStream())
                    .registerReadListener(listener)
                    .sheet()
                    .headRowNumber(2) // 设置表头行数为2，确保不跳过表头行
                    .doRead();

            // 获取解析后的数据
            List<KeyDurationDiscountFactorsDTO> dtoList = listener.getResultList();

            // 导入数据
            String operName = getUsername();
            String message = keyDurationDiscountFactorsService.importKeyDurationDiscountFactorsDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            return Result.error("导入关键久期折现因子失败：" + e.getMessage());
        }
    }
}
