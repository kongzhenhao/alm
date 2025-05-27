package com.xl.alm.app.service;

import com.xl.alm.app.entity.ProductAttributeEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 产品属性表 服务接口
 *
 * @author AI Assistant
 */
public interface IProductAttributeService {
    /**
     * 查询产品属性列表
     *
     * @param productAttribute 产品属性查询条件
     * @return 产品属性列表
     */
    List<ProductAttributeEntity> selectProductAttributeList(ProductAttributeEntity productAttribute);

    /**
     * 根据ID查询产品属性
     *
     * @param id 产品属性ID
     * @return 产品属性
     */
    ProductAttributeEntity selectProductAttributeById(Long id);

    /**
     * 新增产品属性
     *
     * @param productAttribute 产品属性
     * @return 结果
     */
    int insertProductAttribute(ProductAttributeEntity productAttribute);

    /**
     * 修改产品属性
     *
     * @param productAttribute 产品属性
     * @return 结果
     */
    int updateProductAttribute(ProductAttributeEntity productAttribute);

    /**
     * 批量删除产品属性
     *
     * @param ids 需要删除的产品属性ID
     * @return 结果
     */
    int deleteProductAttributeByIds(Long[] ids);

    /**
     * 删除产品属性信息
     *
     * @param id 产品属性ID
     * @return 结果
     */
    int deleteProductAttributeById(Long id);

    /**
     * 导入产品属性数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    String importProductAttribute(MultipartFile file, boolean updateSupport, String operName);

    /**
     * 导出产品属性数据模板
     *
     * @param response HTTP响应对象
     */
    void importTemplateProductAttribute(HttpServletResponse response);
}
