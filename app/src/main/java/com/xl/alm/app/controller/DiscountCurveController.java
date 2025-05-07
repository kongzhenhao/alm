package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.DiscountCurveDTO;
import com.xl.alm.app.query.DiscountCurveQuery;
import com.xl.alm.app.service.DiscountCurveService;
import com.xl.alm.app.util.ExcelUtil;
import com.xl.alm.app.util.ValueSetExcelExporter;
import com.xl.alm.app.util.ValueSetExcelImportListener;
import com.alibaba.excel.EasyExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 折现曲线表 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/dur/discount/curve")
public class DiscountCurveController extends BaseController {

    @Autowired
    private DiscountCurveService discountCurveService;

    /**
     * 查询折现曲线列表
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:curve:list')")
    @GetMapping("/list")
    public TableDataInfo list(DiscountCurveQuery discountCurveQuery) {
        startPage();
        List<DiscountCurveDTO> list = discountCurveService.selectDiscountCurveDtoList(discountCurveQuery);
        return getDataTable(list);
    }

    /**
     * 获取折现曲线详细信息
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:curve:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(discountCurveService.selectDiscountCurveDtoById(id));
    }

    /**
     * 根据条件查询折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:curve:query')")
    @GetMapping(value = "/condition")
    public Result getInfoByCondition(
            @RequestParam("accountPeriod") String accountPeriod,
            @RequestParam("curveType") String curveType,
            @RequestParam("bpType") String bpType,
            @RequestParam("durationType") String durationType) {
        return Result.success(discountCurveService.selectDiscountCurveDtoByCondition(
                accountPeriod, curveType, bpType, durationType));
    }

    /**
     * 新增折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:curve:add')")
    @Log(title = "折现曲线", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody DiscountCurveDTO discountCurveDTO) {
        return toAjax(discountCurveService.insertDiscountCurveDto(discountCurveDTO));
    }

    /**
     * 批量新增折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:curve:add')")
    @Log(title = "折现曲线", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<DiscountCurveDTO> discountCurveDTOList) {
        return toAjax(discountCurveService.batchInsertDiscountCurveDto(discountCurveDTOList));
    }

    /**
     * 修改折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:curve:edit')")
    @Log(title = "折现曲线", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody DiscountCurveDTO discountCurveDTO) {
        return toAjax(discountCurveService.updateDiscountCurveDto(discountCurveDTO));
    }

    /**
     * 删除折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:curve:remove')")
    @Log(title = "折现曲线", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public Result remove(@PathVariable Long id) {
        return toAjax(discountCurveService.deleteDiscountCurveDtoById(id));
    }

    /**
     * 批量删除折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:curve:remove')")
    @Log(title = "折现曲线", businessType = BusinessType.DELETE)
    @DeleteMapping("/batch/{ids}")
    public Result batchRemove(@PathVariable Long[] ids) {
        return toAjax(discountCurveService.deleteDiscountCurveDtoByIds(ids));
    }

    /**
     * 删除指定账期的折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:curve:remove')")
    @Log(title = "折现曲线", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountPeriod}")
    public Result removeByPeriod(@PathVariable String accountPeriod) {
        return toAjax(discountCurveService.deleteDiscountCurveDtoByPeriod(accountPeriod));
    }

    /**
     * 导入折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:curve:import')")
    @Log(title = "折现曲线", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        try {
            // 使用自定义的ValueSetExcelImportListener处理Excel导入
            // 将表头列名从0开始至1272的列转成JSON串赋值到curveValSet字段中
            ValueSetExcelImportListener<DiscountCurveDTO> listener = new ValueSetExcelImportListener<>(DiscountCurveDTO.class, "curveValSet");

            // 读取Excel文件
            EasyExcel.read(file.getInputStream())
                    .registerReadListener(listener)
                    .sheet()
                    .headRowNumber(2) // 设置表头行数为2，确保不跳过表头行
                    .doRead();

            // 获取处理后的数据列表
            List<DiscountCurveDTO> dtoList = listener.getResultList();

            // 导入数据
            String operName = getUsername();
            String message = discountCurveService.importDiscountCurveDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            logger.error("导入折现曲线失败", e);
            return Result.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出折现曲线
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:curve:export')")
    @Log(title = "折现曲线", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DiscountCurveQuery query) {
        List<DiscountCurveDTO> list = discountCurveService.selectDiscountCurveDtoList(query);
        // 使用自定义的ValueSetExcelExporter导出，处理curveValSet字段
        // 表头全部用中文，编码值通过字典转为中文
        ValueSetExcelExporter.exportExcel(list, "折现曲线数据", response, "curveValSet");
    }

    /**
     * 获取折现曲线模板Excel
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:curve:export')")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        // 使用自定义的ValueSetExcelExporter导出模板，处理curveValSet字段
        // 序号从0开始，共有1273项，序号为0的列日期显示为上一月最后一天的日期，随序号递增加一个自然月显示日期
        ValueSetExcelExporter.exportTemplateExcel(DiscountCurveDTO.class, "折现曲线模板", response, "curveValSet");
    }
}
