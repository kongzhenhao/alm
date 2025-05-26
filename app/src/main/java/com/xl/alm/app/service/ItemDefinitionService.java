package com.xl.alm.app.service;

import com.xl.alm.app.dto.ItemDefinitionDTO;
import com.xl.alm.app.query.ItemDefinitionQuery;

import java.util.List;

/**
 * 项目定义表 Service接口
 *
 * @author AI Assistant
 */
public interface ItemDefinitionService {
    /**
     * 查询项目定义表列表
     *
     * @param query 查询条件
     * @return 项目定义表集合
     */
    List<ItemDefinitionDTO> selectItemDefinitionDtoList(ItemDefinitionQuery query);

    /**
     * 查询项目定义表详情
     *
     * @param id 主键ID
     * @return 项目定义表
     */
    ItemDefinitionDTO selectItemDefinitionDtoById(Long id);

    /**
     * 查询项目定义表详情
     *
     * @param itemCode 项目编码
     * @return 项目定义表
     */
    ItemDefinitionDTO selectItemDefinitionDtoByItemCode(String itemCode);

    /**
     * 根据项目编码查询有效的项目定义表详情（用于导入检查）
     *
     * @param itemCode 项目编码
     * @return 项目定义表
     */
    ItemDefinitionDTO selectValidItemDefinitionDtoByItemCode(String itemCode);

    /**
     * 新增项目定义表
     *
     * @param dto 项目定义表
     * @return 结果
     */
    int insertItemDefinitionDto(ItemDefinitionDTO dto);

    /**
     * 批量新增项目定义表
     *
     * @param dtoList 项目定义表列表
     * @return 结果
     */
    int batchInsertItemDefinitionDto(List<ItemDefinitionDTO> dtoList);

    /**
     * 修改项目定义表
     *
     * @param dto 项目定义表
     * @return 结果
     */
    int updateItemDefinitionDto(ItemDefinitionDTO dto);

    /**
     * 删除项目定义表
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteItemDefinitionDtoById(Long id);

    /**
     * 批量删除项目定义表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteItemDefinitionDtoByIds(Long[] ids);

    /**
     * 导入项目定义表
     *
     * @param dtoList 项目定义表数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    String importItemDefinitionDto(List<ItemDefinitionDTO> dtoList, Boolean updateSupport, String username);
}
