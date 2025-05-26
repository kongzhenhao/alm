package com.xl.alm.app.controller;

import com.alibaba.excel.EasyExcel;
import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.KeyDurationDiscountCurveDTO;
import com.xl.alm.app.query.KeyDurationDiscountCurveQuery;
import com.xl.alm.app.service.KeyDurationDiscountCurveService;
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
 * 关键久期折现曲线表 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/dur/key/duration/discount/curve")
public class KeyDurationDiscountCurveController extends BaseController {

    @Autowired
    private KeyDurationDiscountCurveService keyDurationDiscountCurveService;

    /**
     * 查询关键久期折现曲线列表
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:curve:list')")
    @GetMapping("/list")
    public TableDataInfo list(KeyDurationDiscountCurveQuery query) {
        startPage();
        List<KeyDurationDiscountCurveDTO> list = keyDurationDiscountCurveService.selectKeyDurationDiscountCurveDtoList(query);
        return getDataTable(list);
    }

    /**
     * 获取关键久期折现曲线详细信息
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:curve:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(keyDurationDiscountCurveService.selectKeyDurationDiscountCurveDtoById(id));
    }

    /**
     * 根据条件查询关键久期折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:curve:query')")
    @GetMapping("/condition")
    public Result getByCondition(
            @RequestParam("accountPeriod") String accountPeriod,
            @RequestParam("curveType") String curveType,
            @RequestParam("keyDuration") String keyDuration,
            @RequestParam("stressDirection") String stressDirection,
            @RequestParam("durationType") String durationType) {
        return Result.success(keyDurationDiscountCurveService.selectKeyDurationDiscountCurveDtoByCondition(
                accountPeriod, curveType, keyDuration, stressDirection, durationType));
    }

    /**
     * 新增关键久期折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:curve:add')")
    @Log(title = "关键久期折现曲线", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody @Valid KeyDurationDiscountCurveDTO dto) {
        return toAjax(keyDurationDiscountCurveService.insertKeyDurationDiscountCurveDto(dto));
    }

    /**
     * 批量新增关键久期折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:curve:add')")
    @Log(title = "关键久期折现曲线", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<KeyDurationDiscountCurveDTO> dtoList) {
        return toAjax(keyDurationDiscountCurveService.batchInsertKeyDurationDiscountCurveDto(dtoList));
    }

    /**
     * 修改关键久期折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:curve:edit')")
    @Log(title = "关键久期折现曲线", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody @Valid KeyDurationDiscountCurveDTO dto) {
        return toAjax(keyDurationDiscountCurveService.updateKeyDurationDiscountCurveDto(dto));
    }

    /**
     * 删除关键久期折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:curve:remove')")
    @Log(title = "关键久期折现曲线", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(keyDurationDiscountCurveService.deleteKeyDurationDiscountCurveDtoByIds(ids));
    }

    /**
     * 删除指定账期的关键久期折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:curve:remove')")
    @Log(title = "关键久期折现曲线", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountPeriod}")
    public Result removeByPeriod(@PathVariable String accountPeriod) {
        return toAjax(keyDurationDiscountCurveService.deleteKeyDurationDiscountCurveDtoByAccountPeriod(accountPeriod));
    }

    /**
     * 导入关键久期折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:curve:import')")
    @Log(title = "关键久期折现曲线", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        try {
            // 使用自定义的ValueSetExcelImportListener处理Excel导入
            // 将表头列名从0开始至1273的列转成JSON串赋值到curveValSet字段中
            ValueSetExcelImportListener<KeyDurationDiscountCurveDTO> listener = new ValueSetExcelImportListener<>(KeyDurationDiscountCurveDTO.class, "curveValSet");

            // 读取Excel文件
            EasyExcel.read(file.getInputStream())
                    .registerReadListener(listener)
                    .sheet()
                    .headRowNumber(2) // 设置表头行数为2，确保不跳过表头行
                    .doRead();

            // 获取处理后的数据列表
            List<KeyDurationDiscountCurveDTO> dtoList = listener.getResultList();

            // 导入数据
            String operName = getUsername();
            String message = keyDurationDiscountCurveService.importKeyDurationDiscountCurveDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            logger.error("导入关键久期折现曲线失败", e);
            return Result.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出关键久期折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:curve:export')")
    @Log(title = "关键久期折现曲线", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KeyDurationDiscountCurveQuery query) {
        List<KeyDurationDiscountCurveDTO> list = keyDurationDiscountCurveService.selectKeyDurationDiscountCurveDtoList(query);
        // 使用自定义的ValueSetExcelExporter导出，处理curveValSet字段
        // 表头全部用中文，编码值通过字典转为中文
        ValueSetExcelExporter.exportExcel(list, "关键久期折现曲线数据", response, "curveValSet");
    }

    /**
     * 获取关键久期折现曲线模板Excel
     */
    @PreAuthorize("@ss.hasPermi('dur:key:duration:discount:curve:export')")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        // 使用自定义的ValueSetExcelExporter导出模板，处理curveValSet字段
        // 序号从0开始，共有1273项，序号为0的列日期显示为上一月最后一天的日期，随序号递增加一个自然月显示日期
        ValueSetExcelExporter.exportTemplateExcel(KeyDurationDiscountCurveDTO.class, "关键久期折现曲线模板", response, "curveValSet");
    }
}
