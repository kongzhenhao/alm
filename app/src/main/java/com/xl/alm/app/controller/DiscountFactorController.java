package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.dto.DiscountFactorDTO;
import com.xl.alm.app.query.DiscountFactorQuery;
import com.xl.alm.app.service.DiscountFactorService;
import com.xl.alm.app.util.ExcelUtil;
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
        ExcelUtil<DiscountFactorDTO> util = new ExcelUtil<>(DiscountFactorDTO.class);
        List<DiscountFactorDTO> dtoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        try {
            String message = discountFactorService.importDiscountFactorDto(dtoList, updateSupport, operName);
            return Result.success(message);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 导出折现因子
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:factor:export')")
    @Log(title = "折现因子", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DiscountFactorQuery query) {
        ExcelUtil<DiscountFactorDTO> util = new ExcelUtil<>(DiscountFactorDTO.class);
        List<DiscountFactorDTO> list = discountFactorService.selectDiscountFactorDtoList(query);
        util.exportExcel(list, "折现因子数据", response);
    }

    /**
     * 获取折现因子模板Excel
     */
    @PreAuthorize("@ss.hasPermi('dur:discount:factor:import')")
    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) {
        ExcelUtil<DiscountFactorDTO> util = new ExcelUtil<>(DiscountFactorDTO.class);
        util.exportTemplateExcel(response, "折现因子");
    }
}
