package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.jd.lightning.common.utils.poi.ExcelUtil;
import com.jd.lightning.common.utils.SecurityUtils;
import com.xl.alm.app.entity.ProductAttributeEntity;
import com.xl.alm.app.service.IProductAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 产品属性表 控制器
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/base/product/attribute")
public class ProductAttributeController extends BaseController {
    @Autowired
    private IProductAttributeService productAttributeService;

    /**
     * 查询产品属性列表
     */
    @PreAuthorize("@ss.hasPermi('base:product:attribute:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductAttributeEntity productAttribute) {
        startPage();
        List<ProductAttributeEntity> list = productAttributeService.selectProductAttributeList(productAttribute);
        return getDataTable(list);
    }

    /**
     * 导出产品属性列表
     */
    @PreAuthorize("@ss.hasPermi('base:product:attribute:export')")
    @Log(title = "产品属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProductAttributeEntity productAttribute) {
        List<ProductAttributeEntity> list = productAttributeService.selectProductAttributeList(productAttribute);
        ExcelUtil<ProductAttributeEntity> util = new ExcelUtil<>(ProductAttributeEntity.class);
        util.exportExcel(response, list, "产品属性数据");
    }

    /**
     * 获取产品属性详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:product:attribute:query')")
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id) {
        return success(productAttributeService.selectProductAttributeById(id));
    }

    /**
     * 新增产品属性
     */
    @PreAuthorize("@ss.hasPermi('base:product:attribute:add')")
    @Log(title = "产品属性", businessType = BusinessType.INSERT)
    @PostMapping
    public Result add(@RequestBody ProductAttributeEntity productAttribute) {
        productAttribute.setCreateBy(SecurityUtils.getUsername());
        return toAjax(productAttributeService.insertProductAttribute(productAttribute));
    }

    /**
     * 修改产品属性
     */
    @PreAuthorize("@ss.hasPermi('base:product:attribute:edit')")
    @Log(title = "产品属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result edit(@RequestBody ProductAttributeEntity productAttribute) {
        productAttribute.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(productAttributeService.updateProductAttribute(productAttribute));
    }

    /**
     * 删除产品属性
     */
    @PreAuthorize("@ss.hasPermi('base:product:attribute:remove')")
    @Log(title = "产品属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids) {
        return toAjax(productAttributeService.deleteProductAttributeByIds(ids));
    }

    /**
     * 导入产品属性数据
     */
    @PreAuthorize("@ss.hasPermi('base:product:attribute:import')")
    @Log(title = "产品属性", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public Result importData(MultipartFile file, boolean updateSupport) throws Exception {
        String operName = SecurityUtils.getUsername();
        String message = productAttributeService.importProductAttribute(file, updateSupport, operName);
        return success(message);
    }

    /**
     * 下载产品属性导入模板
     */
    @PreAuthorize("@ss.hasPermi('base:product:attribute:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        productAttributeService.importTemplateProductAttribute(response);
    }
}
