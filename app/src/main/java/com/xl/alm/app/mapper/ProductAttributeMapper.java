package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.ProductAttributeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品属性表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface ProductAttributeMapper {
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
    ProductAttributeEntity selectProductAttributeById(@Param("id") Long id);

    /**
     * 根据精算代码和账期查询产品属性
     *
     * @param actuarialCode 精算代码
     * @param accountingPeriod 账期
     * @return 产品属性
     */
    ProductAttributeEntity selectProductAttributeByCode(@Param("actuarialCode") String actuarialCode, 
                                                       @Param("accountingPeriod") String accountingPeriod);

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
     * 删除产品属性
     *
     * @param id 产品属性ID
     * @return 结果
     */
    int deleteProductAttributeById(Long id);

    /**
     * 批量删除产品属性
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteProductAttributeByIds(Long[] ids);

    /**
     * 批量插入产品属性数据
     *
     * @param productAttributeList 产品属性列表
     * @return 影响行数
     */
    int batchInsertProductAttribute(List<ProductAttributeEntity> productAttributeList);
}
