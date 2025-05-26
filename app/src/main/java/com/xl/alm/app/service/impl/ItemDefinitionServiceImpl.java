package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.ItemDefinitionDTO;
import com.xl.alm.app.entity.ItemDefinitionEntity;
import com.xl.alm.app.mapper.ItemDefinitionMapper;
import com.xl.alm.app.query.ItemDefinitionQuery;
import com.xl.alm.app.service.ItemDefinitionService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 项目定义表 Service实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class ItemDefinitionServiceImpl implements ItemDefinitionService {

    @Autowired
    private ItemDefinitionMapper itemDefinitionMapper;

    /**
     * 查询项目定义表列表
     *
     * @param query 查询条件
     * @return 项目定义表集合
     */
    @Override
    public List<ItemDefinitionDTO> selectItemDefinitionDtoList(ItemDefinitionQuery query) {
        List<ItemDefinitionEntity> entityList = itemDefinitionMapper.selectItemDefinitionEntityList(query);
        return EntityDtoConvertUtil.convertToDTOList(entityList, ItemDefinitionDTO.class);
    }

    /**
     * 查询项目定义表详情
     *
     * @param id 主键ID
     * @return 项目定义表
     */
    @Override
    public ItemDefinitionDTO selectItemDefinitionDtoById(Long id) {
        ItemDefinitionEntity entity = itemDefinitionMapper.selectItemDefinitionEntityById(id);
        return EntityDtoConvertUtil.convertToDTO(entity, ItemDefinitionDTO.class);
    }

    /**
     * 查询项目定义表详情
     *
     * @param itemCode 项目编码
     * @return 项目定义表
     */
    @Override
    public ItemDefinitionDTO selectItemDefinitionDtoByItemCode(String itemCode) {
        ItemDefinitionEntity entity = itemDefinitionMapper.selectItemDefinitionEntityByItemCode(itemCode);
        return EntityDtoConvertUtil.convertToDTO(entity, ItemDefinitionDTO.class);
    }

    /**
     * 根据项目编码查询有效的项目定义表详情（用于导入检查）
     *
     * @param itemCode 项目编码
     * @return 项目定义表
     */
    @Override
    public ItemDefinitionDTO selectValidItemDefinitionDtoByItemCode(String itemCode) {
        ItemDefinitionEntity entity = itemDefinitionMapper.selectValidItemDefinitionEntityByItemCode(itemCode);
        return EntityDtoConvertUtil.convertToDTO(entity, ItemDefinitionDTO.class);
    }

    /**
     * 新增项目定义表
     *
     * @param dto 项目定义表
     * @return 结果
     */
    @Override
    public int insertItemDefinitionDto(ItemDefinitionDTO dto) {
        // 设置默认值
        if (dto.getIsDel() == null) {
            dto.setIsDel(0); // 默认未删除
        }
        ItemDefinitionEntity entity = EntityDtoConvertUtil.convertToEntity(dto, ItemDefinitionEntity.class);
        return itemDefinitionMapper.insertItemDefinitionEntity(entity);
    }

    /**
     * 批量新增项目定义表
     *
     * @param dtoList 项目定义表列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertItemDefinitionDto(List<ItemDefinitionDTO> dtoList) {
        // 设置默认值
        for (ItemDefinitionDTO dto : dtoList) {
            if (dto.getIsDel() == null) {
                dto.setIsDel(0); // 默认未删除
            }
        }
        List<ItemDefinitionEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, ItemDefinitionEntity.class);
        return itemDefinitionMapper.batchInsertItemDefinitionEntity(entityList);
    }

    /**
     * 修改项目定义表
     *
     * @param dto 项目定义表
     * @return 结果
     */
    @Override
    public int updateItemDefinitionDto(ItemDefinitionDTO dto) {
        ItemDefinitionEntity entity = EntityDtoConvertUtil.convertToEntity(dto, ItemDefinitionEntity.class);
        return itemDefinitionMapper.updateItemDefinitionEntity(entity);
    }

    /**
     * 删除项目定义表
     *
     * @param id 主键ID
     * @return 结果
     */
    @Override
    public int deleteItemDefinitionDtoById(Long id) {
        return itemDefinitionMapper.deleteItemDefinitionEntityById(id);
    }

    /**
     * 批量删除项目定义表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    @Override
    public int deleteItemDefinitionDtoByIds(Long[] ids) {
        return itemDefinitionMapper.deleteItemDefinitionEntityByIds(ids);
    }

    /**
     * 导入项目定义表
     *
     * @param dtoList 项目定义表数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importItemDefinitionDto(List<ItemDefinitionDTO> dtoList, Boolean updateSupport, String username) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        // 第一步：检查同一批次内是否有重复的项目编码
        Set<String> itemCodeSet = new HashSet<>();
        for (ItemDefinitionDTO dto : dtoList) {
            if (itemCodeSet.contains(dto.getItemCode())) {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、项目编码 ").append(dto.getItemCode()).append(" 在导入数据中重复出现");
            } else {
                itemCodeSet.add(dto.getItemCode());
            }
        }

        // 如果有重复的项目编码，直接返回错误
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new RuntimeException(failureMsg.toString());
        }

        // 第二步：处理数据导入
        // 重置计数器
        successNum = 0;
        failureNum = 0;
        successMsg = new StringBuilder();
        failureMsg = new StringBuilder();

        for (ItemDefinitionDTO dto : dtoList) {
            try {
                // 设置操作人
                dto.setCreateBy(username);
                dto.setUpdateBy(username);

                // 设置默认值
                if (StringUtils.isEmpty(dto.getStatus())) {
                    dto.setStatus("1"); // 默认有效
                }
                if (dto.getIsDel() == null) {
                    dto.setIsDel(0); // 默认未删除
                }

                // 查询是否存在有效数据
                ItemDefinitionDTO existDto = selectValidItemDefinitionDtoByItemCode(dto.getItemCode());
                log.info("查询项目编码 {} 的结果: {}", dto.getItemCode(), existDto != null ? "存在" : "不存在");

                if (StringUtils.isNull(existDto)) {
                    // 不存在，执行新增
                    insertItemDefinitionDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、项目编码 ").append(dto.getItemCode()).append(" 导入成功");
                } else if (updateSupport) {
                    // 存在且允许更新，执行更新
                    dto.setId(existDto.getId());
                    updateItemDefinitionDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、项目编码 ").append(dto.getItemCode()).append(" 更新成功");
                } else {
                    // 存在但不允许更新
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、项目编码 ").append(dto.getItemCode()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、项目编码 " + dto.getItemCode() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new RuntimeException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
