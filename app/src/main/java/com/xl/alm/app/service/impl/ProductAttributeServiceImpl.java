package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.DateUtils;
import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.util.ExcelUtil;
import com.jd.lightning.common.exception.ServiceException;
import com.xl.alm.app.entity.ProductAttributeEntity;
import com.xl.alm.app.mapper.ProductAttributeMapper;
import com.xl.alm.app.service.IProductAttributeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品属性表 服务实现类
 *
 * @author AI Assistant
 */
@Service
public class ProductAttributeServiceImpl implements IProductAttributeService {
    private static final Logger log = LoggerFactory.getLogger(ProductAttributeServiceImpl.class);

    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    /**
     * 查询产品属性列表
     *
     * @param productAttribute 产品属性查询条件
     * @return 产品属性列表
     */
    @Override
    public List<ProductAttributeEntity> selectProductAttributeList(ProductAttributeEntity productAttribute) {
        return productAttributeMapper.selectProductAttributeList(productAttribute);
    }

    /**
     * 根据ID查询产品属性
     *
     * @param id 产品属性ID
     * @return 产品属性
     */
    @Override
    public ProductAttributeEntity selectProductAttributeById(Long id) {
        return productAttributeMapper.selectProductAttributeById(id);
    }

    /**
     * 新增产品属性
     *
     * @param productAttribute 产品属性
     * @return 结果
     */
    @Override
    public int insertProductAttribute(ProductAttributeEntity productAttribute) {
        productAttribute.setCreateTime(DateUtils.getNowDate());
        return productAttributeMapper.insertProductAttribute(productAttribute);
    }

    /**
     * 修改产品属性
     *
     * @param productAttribute 产品属性
     * @return 结果
     */
    @Override
    public int updateProductAttribute(ProductAttributeEntity productAttribute) {
        productAttribute.setUpdateTime(DateUtils.getNowDate());
        return productAttributeMapper.updateProductAttribute(productAttribute);
    }

    /**
     * 批量删除产品属性
     *
     * @param ids 需要删除的产品属性ID
     * @return 结果
     */
    @Override
    public int deleteProductAttributeByIds(Long[] ids) {
        return productAttributeMapper.deleteProductAttributeByIds(ids);
    }

    /**
     * 删除产品属性信息
     *
     * @param id 产品属性ID
     * @return 结果
     */
    @Override
    public int deleteProductAttributeById(Long id) {
        return productAttributeMapper.deleteProductAttributeById(id);
    }

    /**
     * 导入产品属性数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importProductAttribute(MultipartFile file, boolean updateSupport, String operName) {
        try {
            ExcelUtil<ProductAttributeEntity> util = new ExcelUtil<>(ProductAttributeEntity.class);
            List<ProductAttributeEntity> productAttributeList = util.importExcel(file.getInputStream());
            int successNum = 0;
            int failureNum = 0;
            StringBuilder successMsg = new StringBuilder();
            StringBuilder failureMsg = new StringBuilder();

            for (ProductAttributeEntity productAttribute : productAttributeList) {
                try {
                    // 验证必填字段
                    if (StringUtils.isBlank(productAttribute.getAccountingPeriod()) ||
                        StringUtils.isBlank(productAttribute.getActuarialCode())) {
                        failureNum++;
                        failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据账期或精算代码为空");
                        continue;
                    }

                    // 设置默认值
                    if (StringUtils.isBlank(productAttribute.getTermType())) {
                        productAttribute.setTermType("L");
                    }
                    if (StringUtils.isBlank(productAttribute.getShortTermFlag())) {
                        productAttribute.setShortTermFlag("N");
                    }
                    if (StringUtils.isBlank(productAttribute.getRegMidId())) {
                        productAttribute.setRegMidId("N");
                    }
                    if (StringUtils.isBlank(productAttribute.getNewBusinessFlag())) {
                        productAttribute.setNewBusinessFlag("Y");
                    }

                    // 查询是否已存在
                    ProductAttributeEntity existingAttribute = productAttributeMapper.selectProductAttributeByCode(
                            productAttribute.getActuarialCode(), productAttribute.getAccountingPeriod());

                    if (existingAttribute != null) {
                        if (updateSupport) {
                            productAttribute.setId(existingAttribute.getId());
                            productAttribute.setUpdateBy(operName);
                            productAttributeMapper.updateProductAttribute(productAttribute);
                            successNum++;
                            successMsg.append("<br/>第 ").append(successNum).append(" 条数据更新成功");
                        } else {
                            failureNum++;
                            failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据已存在");
                        }
                    } else {
                        productAttribute.setCreateBy(operName);
                        productAttributeMapper.insertProductAttribute(productAttribute);
                        successNum++;
                        successMsg.append("<br/>第 ").append(successNum).append(" 条数据导入成功");
                    }
                } catch (Exception e) {
                    failureNum++;
                    String msg = "<br/>第 " + failureNum + " 条数据导入失败：";
                    failureMsg.append(msg).append(e.getMessage());
                    log.error(msg, e);
                }
            }

            if (failureNum > 0) {
                failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
                throw new ServiceException(failureMsg.toString());
            } else {
                successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
            }
            return successMsg.toString();
        } catch (Exception e) {
            log.error("导入产品属性数据失败", e);
            throw new ServiceException("导入产品属性数据失败：" + e.getMessage());
        }
    }

    /**
     * 导出产品属性数据模板
     *
     * @return 模板文件名
     */
    @Override
    public void importTemplateProductAttribute(HttpServletResponse response) {
        List<ProductAttributeEntity> templateList = new ArrayList<>();
        ProductAttributeEntity template = new ProductAttributeEntity();
        template.setAccountingPeriod("202401");
        template.setActuarialCode("SAMPLE001");
        template.setBusinessCode("BIZ001");
        template.setProductName("示例产品");
        template.setTermType("L");
        template.setInsuranceMainType("长期寿险");
        template.setInsuranceSubType("年金险");
        template.setDesignType("传统险");
        template.setShortTermFlag("N");
        template.setRegMidId("N");
        template.setGuaranteedCostRate(new java.math.BigDecimal("0.025"));
        template.setSubAccount("主账户");
        template.setNewBusinessFlag("Y");
        template.setRemark("示例数据");
        templateList.add(template);

        ExcelUtil<ProductAttributeEntity> util = new ExcelUtil<>(ProductAttributeEntity.class);
        util.exportExcel(templateList, "产品属性数据模板", response);
    }
}
