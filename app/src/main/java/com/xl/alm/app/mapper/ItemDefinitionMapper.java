package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.ItemDefinitionEntity;
import com.xl.alm.app.query.ItemDefinitionQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目定义表 Mapper接口
 *
 * @author AI Assistant
 */
public interface ItemDefinitionMapper {
    /**
     * 查询项目定义表列表
     *
     * @param query 查询条件
     * @return 项目定义表集合
     */
    List<ItemDefinitionEntity> selectItemDefinitionEntityList(ItemDefinitionQuery query);

    /**
     * 查询项目定义表详情
     *
     * @param id 主键ID
     * @return 项目定义表
     */
    ItemDefinitionEntity selectItemDefinitionEntityById(Long id);

    /**
     * 查询项目定义表详情
     *
     * @param itemCode 项目编码
     * @return 项目定义表
     */
    ItemDefinitionEntity selectItemDefinitionEntityByItemCode(@Param("itemCode") String itemCode);

    /**
     * 根据项目编码查询有效的项目定义表详情（用于导入检查）
     *
     * @param itemCode 项目编码
     * @return 项目定义表
     */
    ItemDefinitionEntity selectValidItemDefinitionEntityByItemCode(@Param("itemCode") String itemCode);

    /**
     * 新增项目定义表
     *
     * @param entity 项目定义表
     * @return 结果
     */
    int insertItemDefinitionEntity(ItemDefinitionEntity entity);

    /**
     * 批量新增项目定义表
     *
     * @param entityList 项目定义表列表
     * @return 结果
     */
    int batchInsertItemDefinitionEntity(List<ItemDefinitionEntity> entityList);

    /**
     * 修改项目定义表
     *
     * @param entity 项目定义表
     * @return 结果
     */
    int updateItemDefinitionEntity(ItemDefinitionEntity entity);

    /**
     * 删除项目定义表
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteItemDefinitionEntityById(Long id);

    /**
     * 批量删除项目定义表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteItemDefinitionEntityByIds(Long[] ids);

    /**
     * 物理删除项目定义表
     *
     * @param itemCode 项目编码
     * @return 结果
     */
    int physicalDeleteByItemCode(@Param("itemCode") String itemCode);
}
