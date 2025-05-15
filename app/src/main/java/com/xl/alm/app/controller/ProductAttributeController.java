package com.xl.alm.app.controller;

import com.jd.lightning.common.annotation.Log;
import com.jd.lightning.common.core.controller.BaseController;
import com.jd.lightning.common.core.domain.Result;
import com.jd.lightning.common.core.page.TableDataInfo;
import com.jd.lightning.common.enums.BusinessType;
import com.xl.alm.app.util.ExcelUtil;
import com.jd.lightning.common.utils.SecurityUtils;
import com.xl.alm.app.entity.ProductAttributeEntity;
import com.xl.alm.app.dto.ProductAttributeDTO;
import com.xl.alm.app.service.IProductAttributeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品属性表 控制器
 *
 * @author AI Assistant
 */
@RestController
@RequestMapping("/base/product/attribute")
@Slf4j
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

        // 将Entity转换为DTO
        List<ProductAttributeDTO> dtoList = new ArrayList<>();
        for (ProductAttributeEntity entity : list) {
            ProductAttributeDTO dto = new ProductAttributeDTO();
            dto.setId(entity.getId());
            dto.setAccountingPeriod(entity.getAccountingPeriod());
            dto.setActuarialCode(entity.getActuarialCode());
            dto.setBusinessCode(entity.getBusinessCode());
            dto.setProductName(entity.getProductName());
            dto.setTermType(entity.getTermType());
            dto.setInsuranceMainType(entity.getInsuranceMainType());
            dto.setInsuranceSubType(entity.getInsuranceSubType());
            dto.setDesignType(entity.getDesignType());
            dto.setShortTermFlag(entity.getShortTermFlag());
            dto.setRegMidId(entity.getRegMidId());
            dto.setGuaranteedCostRate(entity.getGuaranteedCostRate());
            dto.setSubAccount(entity.getSubAccount());
            dto.setNewBusinessFlag(entity.getNewBusinessFlag());
            dto.setRemark(entity.getRemark());
            dto.setIsDel(entity.getIsDel());
            dto.setCreateBy(entity.getCreateBy());
            dto.setCreateTime(entity.getCreateTime());
            dto.setUpdateBy(entity.getUpdateBy());
            dto.setUpdateTime(entity.getUpdateTime());
            dtoList.add(dto);
        }

        ExcelUtil<ProductAttributeDTO> util = new ExcelUtil<>(ProductAttributeDTO.class);
        util.exportExcel(dtoList, "产品属性数据", response);
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
    public Result importData(MultipartFile file, boolean updateSupport) {
        try {
            String operName = SecurityUtils.getUsername();
            String message = productAttributeService.importProductAttribute(file, updateSupport, operName);
            return success(message);
        } catch (Exception e) {
            log.error("导入产品属性数据失败", e);
            return error(e.getMessage());
        }
    }

    /**
     * 下载产品属性导入模板
     */
    @GetMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        productAttributeService.importTemplateProductAttribute(response);
    }
}
