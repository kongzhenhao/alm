package com.xl.alm.job.cost.mapper;

import com.xl.alm.job.cost.entity.ProductAttributeEntity;
import org.apache.ibatis.annotations.Mapper;

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
     * @param productAttribute 产品属性信息
     * @return 产品属性集合
     */
    List<ProductAttributeEntity> selectProductAttributeList(ProductAttributeEntity productAttribute);
}
