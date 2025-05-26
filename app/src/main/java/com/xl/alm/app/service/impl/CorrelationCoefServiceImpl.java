package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.CorrelationCoefDTO;
import com.xl.alm.app.entity.CorrelationCoefEntity;
import com.xl.alm.app.mapper.CorrelationCoefMapper;
import com.xl.alm.app.query.CorrelationCoefQuery;
import com.xl.alm.app.service.CorrelationCoefService;
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
 * 相关系数表 Service实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class CorrelationCoefServiceImpl implements CorrelationCoefService {

    @Autowired
    private CorrelationCoefMapper correlationCoefMapper;

    /**
     * 查询相关系数表列表
     *
     * @param query 查询条件
     * @return 相关系数表集合
     */
    @Override
    public List<CorrelationCoefDTO> selectCorrelationCoefDtoList(CorrelationCoefQuery query) {
        // 直接使用关联查询，返回包含项目信息的DTO
        return correlationCoefMapper.selectCorrelationCoefDtoList(query);
    }

    /**
     * 查询相关系数表详情
     *
     * @param id 主键ID
     * @return 相关系数表
     */
    @Override
    public CorrelationCoefDTO selectCorrelationCoefDtoById(Long id) {
        CorrelationCoefEntity entity = correlationCoefMapper.selectCorrelationCoefEntityById(id);
        return EntityDtoConvertUtil.convertToDTO(entity, CorrelationCoefDTO.class);
    }

    /**
     * 根据账期和项目编码查询相关系数表详情
     *
     * @param accountingPeriod 账期
     * @param itemCodeX 第一个风险项目编码
     * @param itemCodeY 第二个风险项目编码
     * @return 相关系数表
     */
    @Override
    public CorrelationCoefDTO selectCorrelationCoefDtoByUniqueKey(String accountingPeriod, String itemCodeX, String itemCodeY) {
        CorrelationCoefEntity entity = correlationCoefMapper.selectCorrelationCoefEntityByUniqueKey(accountingPeriod, itemCodeX, itemCodeY);
        return EntityDtoConvertUtil.convertToDTO(entity, CorrelationCoefDTO.class);
    }

    /**
     * 根据账期和项目编码查询有效的相关系数表详情（用于导入检查）
     *
     * @param accountingPeriod 账期
     * @param itemCodeX 第一个风险项目编码
     * @param itemCodeY 第二个风险项目编码
     * @return 相关系数表
     */
    @Override
    public CorrelationCoefDTO selectValidCorrelationCoefDtoByUniqueKey(String accountingPeriod, String itemCodeX, String itemCodeY) {
        CorrelationCoefEntity entity = correlationCoefMapper.selectValidCorrelationCoefEntityByUniqueKey(accountingPeriod, itemCodeX, itemCodeY);
        return EntityDtoConvertUtil.convertToDTO(entity, CorrelationCoefDTO.class);
    }

    /**
     * 新增相关系数表
     *
     * @param dto 相关系数表
     * @return 结果
     */
    @Override
    public int insertCorrelationCoefDto(CorrelationCoefDTO dto) {
        // 设置默认值
        if (dto.getIsDel() == null) {
            dto.setIsDel(0); // 默认未删除
        }
        CorrelationCoefEntity entity = EntityDtoConvertUtil.convertToEntity(dto, CorrelationCoefEntity.class);
        return correlationCoefMapper.insertCorrelationCoefEntity(entity);
    }

    /**
     * 批量新增相关系数表
     *
     * @param dtoList 相关系数表列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertCorrelationCoefDto(List<CorrelationCoefDTO> dtoList) {
        // 设置默认值
        for (CorrelationCoefDTO dto : dtoList) {
            if (dto.getIsDel() == null) {
                dto.setIsDel(0); // 默认未删除
            }
        }
        List<CorrelationCoefEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, CorrelationCoefEntity.class);
        return correlationCoefMapper.batchInsertCorrelationCoefEntity(entityList);
    }

    /**
     * 修改相关系数表
     *
     * @param dto 相关系数表
     * @return 结果
     */
    @Override
    public int updateCorrelationCoefDto(CorrelationCoefDTO dto) {
        CorrelationCoefEntity entity = EntityDtoConvertUtil.convertToEntity(dto, CorrelationCoefEntity.class);
        return correlationCoefMapper.updateCorrelationCoefEntity(entity);
    }

    /**
     * 删除相关系数表
     *
     * @param id 主键ID
     * @return 结果
     */
    @Override
    public int deleteCorrelationCoefDtoById(Long id) {
        return correlationCoefMapper.deleteCorrelationCoefEntityById(id);
    }

    /**
     * 批量删除相关系数表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    @Override
    public int deleteCorrelationCoefDtoByIds(Long[] ids) {
        return correlationCoefMapper.deleteCorrelationCoefEntityByIds(ids);
    }

    /**
     * 导入相关系数表
     *
     * @param dtoList 相关系数表数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importCorrelationCoefDto(List<CorrelationCoefDTO> dtoList, Boolean updateSupport, String username) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        // 第一步：检查同一批次内是否有重复的唯一键
        Set<String> uniqueKeySet = new HashSet<>();
        for (CorrelationCoefDTO dto : dtoList) {
            String uniqueKey = dto.getAccountingPeriod() + "_" + dto.getItemCodeX() + "_" + dto.getItemCodeY();
            if (uniqueKeySet.contains(uniqueKey)) {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、账期 ").append(dto.getAccountingPeriod())
                        .append(" 项目编码 ").append(dto.getItemCodeX()).append("-").append(dto.getItemCodeY())
                        .append(" 在导入数据中重复出现");
            } else {
                uniqueKeySet.add(uniqueKey);
            }
        }

        // 如果有重复的唯一键，直接返回错误
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

        for (CorrelationCoefDTO dto : dtoList) {
            try {
                // 设置操作人
                dto.setCreateBy(username);
                dto.setUpdateBy(username);

                // 不需要设置status字段，因为数据库表中没有这个字段
                if (dto.getIsDel() == null) {
                    dto.setIsDel(0); // 默认未删除
                }

                // 查询是否存在有效数据
                CorrelationCoefDTO existDto = selectValidCorrelationCoefDtoByUniqueKey(
                        dto.getAccountingPeriod(), dto.getItemCodeX(), dto.getItemCodeY());
                log.info("查询账期 {} 项目编码 {}-{} 的结果: {}",
                        dto.getAccountingPeriod(), dto.getItemCodeX(), dto.getItemCodeY(),
                        existDto != null ? "存在" : "不存在");

                if (StringUtils.isNull(existDto)) {
                    // 不存在，执行新增
                    insertCorrelationCoefDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountingPeriod())
                            .append(" 项目编码 ").append(dto.getItemCodeX()).append("-").append(dto.getItemCodeY())
                            .append(" 导入成功");
                } else if (updateSupport) {
                    // 存在且允许更新，执行更新
                    dto.setId(existDto.getId());
                    updateCorrelationCoefDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountingPeriod())
                            .append(" 项目编码 ").append(dto.getItemCodeX()).append("-").append(dto.getItemCodeY())
                            .append(" 更新成功");
                } else {
                    // 存在但不允许更新
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、账期 ").append(dto.getAccountingPeriod())
                            .append(" 项目编码 ").append(dto.getItemCodeX()).append("-").append(dto.getItemCodeY())
                            .append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账期 " + dto.getAccountingPeriod()
                        + " 项目编码 " + dto.getItemCodeX() + "-" + dto.getItemCodeY() + " 导入失败：";
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
