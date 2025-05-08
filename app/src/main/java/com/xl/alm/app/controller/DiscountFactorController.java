package com.xl.alm.app.controller;

import com.alibaba.excel.EasyExcel;
import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.DiscountFactorDTO;
import com.xl.alm.app.query.DiscountFactorQuery;
import com.xl.alm.app.service.DiscountFactorService;
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
 * 折现因子表 Controller
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/dur/discount/factor")
public class DiscountFactorController extends BaseController {

    @Autowired
    private DiscountFactorService discountFactorService;

    /**
     * 查询折现因子列表
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:factor:list')")
    @GetMapping("/list")
    public TableDataInfo list(DiscountFactorQuery discountFactorQuery) {
        startPage();
        List<DiscountFactorDTO> list = discountFactorService.selectDiscountFactorDtoList(discountFactorQuery);
        return getDataTable(list);
    }

    /**
     * 获取折现因子详细信息
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:factor:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return Result.success(discountFactorService.selectDiscountFactorDtoById(id));
    }

    /**
     * 新增折现因子
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:factor:add')")
    @Log(title = "折现因子", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody @Valid DiscountFactorDTO discountFactorDto) {
        discountFactorDto.setCreateBy(getUsername());
        discountFactorDto.setUpdateBy(getUsername());
        return toAjax(discountFactorService.insertDiscountFactorDto(discountFactorDto));
    }

    /**
     * 批量新增折现因子
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:factor:add')")
    @Log(title = "折现因子", businessType = BusinessType.INSERT)
    @PostMapping("/batchAdd")
    public Result batchAdd(@RequestBody List<DiscountFactorDTO> discountFactorDtoList) {
        for (DiscountFactorDTO dto : discountFactorDtoList) {
            dto.setCreateBy(getUsername());
            dto.setUpdateBy(getUsername());
        }
        return toAjax(discountFactorService.batchInsertDiscountFactorDto(discountFactorDtoList));
    }

    /**
     * 修改折现因子
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:factor:edit')")
    @Log(title = "折现因子", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody @Valid DiscountFactorDTO discountFactorDto) {
        discountFactorDto.setUpdateBy(getUsername());
        return toAjax(discountFactorService.updateDiscountFactorDto(discountFactorDto));
    }

    /**
     * 删除折现因子
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:factor:remove')")
    @Log(title = "折现因子", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(discountFactorService.deleteDiscountFactorDtoByIds(ids));
    }

    /**
     * 删除指定账期的折现因子
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:factor:remove')")
    @Log(title = "折现因子", businessType = BusinessType.DELETE)
    @DeleteMapping("/period/{accountPeriod}")
    public Result removeByPeriod(@PathVariable String accountPeriod) {
        return toAjax(discountFactorService.deleteDiscountFactorDtoByPeriod(accountPeriod));
    }

    /**
     * 导入折现因子
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:factor:import')")
    @Log(title = "折现因子", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        try {
            // 使用自定义的ValueSetExcelImportListener处理Excel导入
            // 将表头列名从0开始至1272的列转成JSON串赋值到factorValSet字段中
            ValueSetExcelImportListener<DiscountFactorDTO> listener = new ValueSetExcelImportListener<>(DiscountFactorDTO.class, "factorValSet");

            // 读取Excel文件
            EasyExcel.read(file.getInputStream())
                    .registerReadListener(listener)
                    .sheet()
                    .headRowNumber(2) // 设置表头行数为2，确保不跳过表头行
                    .doRead();

            // 获取处理后的数据列表
            List<DiscountFactorDTO> dtoList = listener.getResultList();

            // 导入数据
            String operName = getUsername();
            String message = discountFactorService.importDiscountFactorDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            logger.error("导入折现因子失败", e);
            return Result.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出折现因子
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:factor:export')")
    @Log(title = "折现因子", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DiscountFactorQuery query) {
        List<DiscountFactorDTO> list = discountFactorService.selectDiscountFactorDtoList(query);
        // 使用自定义的ValueSetExcelExporter导出，处理factorValSet字段
        // 表头全部用中文，编码值通过字典转为中文
        ValueSetExcelExporter.exportExcel(list, "折现因子数据", response, "factorValSet");
    }

    /**
     * 获取折现因子模板Excel
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:factor:import')")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        // 使用自定义的ValueSetExcelExporter导出模板，处理factorValSet字段
        // 序号从0开始，共有1273项，序号为0的列日期显示为上一月最后一天的日期，随序号递增加一个自然月显示日期
        ValueSetExcelExporter.exportTemplateExcel(DiscountFactorDTO.class, "折现因子模板", response, "factorValSet");
    }
}
