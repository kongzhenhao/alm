package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.MarginalCapitalDTO;
import com.xl.alm.app.entity.MarginalCapitalEntity;
import com.xl.alm.app.mapper.MarginalCapitalMapper;
import com.xl.alm.app.query.MarginalCapitalQuery;
import com.xl.alm.app.service.MarginalCapitalService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 边际最低资本贡献率表 Service实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class MarginalCapitalServiceImpl implements MarginalCapitalService {

    @Autowired
    private MarginalCapitalMapper marginalCapitalMapper;

    /**
     * 查询边际最低资本贡献率表列表
     *
     * @param query 查询条件
     * @return 边际最低资本贡献率表集合
     */
    @Override
    public List<MarginalCapitalDTO> selectMarginalCapitalDtoList(MarginalCapitalQuery query) {
        // 直接使用关联查询，返回包含项目信息的DTO
        return marginalCapitalMapper.selectMarginalCapitalDtoList(query);
    }

    /**
     * 查询边际最低资本贡献率表详情
     *
     * @param id 主键ID
     * @return 边际最低资本贡献率表
     */
    @Override
    public MarginalCapitalDTO selectMarginalCapitalDtoById(Long id) {
        MarginalCapitalEntity entity = marginalCapitalMapper.selectMarginalCapitalEntityById(id);
        return EntityDtoConvertUtil.convertToDTO(entity, MarginalCapitalDTO.class);
    }

    /**
     * 根据账期和项目编码查询边际最低资本贡献率表详情
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @return 边际最低资本贡献率表
     */
    @Override
    public MarginalCapitalDTO selectMarginalCapitalDtoByUniqueKey(String accountingPeriod, String itemCode) {
        MarginalCapitalEntity entity = marginalCapitalMapper.selectMarginalCapitalEntityByUniqueKey(accountingPeriod, itemCode);
        return EntityDtoConvertUtil.convertToDTO(entity, MarginalCapitalDTO.class);
    }

    /**
     * 根据账期和项目编码查询有效的边际最低资本贡献率表详情（用于导入检查）
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @return 边际最低资本贡献率表
     */
    @Override
    public MarginalCapitalDTO selectValidMarginalCapitalDtoByUniqueKey(String accountingPeriod, String itemCode) {
        MarginalCapitalEntity entity = marginalCapitalMapper.selectValidMarginalCapitalEntityByUniqueKey(accountingPeriod, itemCode);
        return EntityDtoConvertUtil.convertToDTO(entity, MarginalCapitalDTO.class);
    }

    /**
     * 新增边际最低资本贡献率表
     *
     * @param dto 边际最低资本贡献率表
     * @return 结果
     */
    @Override
    public int insertMarginalCapitalDto(MarginalCapitalDTO dto) {
        // 设置默认值
        if (dto.getIsDel() == null) {
            dto.setIsDel(0); // 默认未删除
        }
        MarginalCapitalEntity entity = EntityDtoConvertUtil.convertToEntity(dto, MarginalCapitalEntity.class);
        return marginalCapitalMapper.insertMarginalCapitalEntity(entity);
    }

    /**
     * 批量新增边际最低资本贡献率表
     *
     * @param dtoList 边际最低资本贡献率表列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertMarginalCapitalDto(List<MarginalCapitalDTO> dtoList) {
        // 设置默认值
        for (MarginalCapitalDTO dto : dtoList) {
            if (dto.getIsDel() == null) {
                dto.setIsDel(0); // 默认未删除
            }
        }
        List<MarginalCapitalEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, MarginalCapitalEntity.class);
        return marginalCapitalMapper.batchInsertMarginalCapitalEntity(entityList);
    }

    /**
     * 修改边际最低资本贡献率表
     *
     * @param dto 边际最低资本贡献率表
     * @return 结果
     */
    @Override
    public int updateMarginalCapitalDto(MarginalCapitalDTO dto) {
        MarginalCapitalEntity entity = EntityDtoConvertUtil.convertToEntity(dto, MarginalCapitalEntity.class);
        return marginalCapitalMapper.updateMarginalCapitalEntity(entity);
    }

    /**
     * 删除边际最低资本贡献率表
     *
     * @param id 主键ID
     * @return 结果
     */
    @Override
    public int deleteMarginalCapitalDtoById(Long id) {
        return marginalCapitalMapper.deleteMarginalCapitalEntityById(id);
    }

    /**
     * 批量删除边际最低资本贡献率表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    @Override
    public int deleteMarginalCapitalDtoByIds(Long[] ids) {
        return marginalCapitalMapper.deleteMarginalCapitalEntityByIds(ids);
    }

    /**
     * 导入边际最低资本贡献率表
     *
     * @param dtoList 边际最低资本贡献率表数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importMarginalCapitalDto(List<MarginalCapitalDTO> dtoList, Boolean updateSupport, String username) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        // 第一步：检查同一批次内是否有重复的唯一键
        Set<String> uniqueKeySet = new HashSet<>();
        for (MarginalCapitalDTO dto : dtoList) {
            String uniqueKey = dto.getAccountingPeriod() + "_" + dto.getItemCode();
            if (uniqueKeySet.contains(uniqueKey)) {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、账期 ").append(dto.getAccountingPeriod())
                        .append(" 项目编码 ").append(dto.getItemCode())
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

        for (MarginalCapitalDTO dto : dtoList) {
            try {
                // 设置操作人
                dto.setCreateBy(username);
                dto.setUpdateBy(username);

                // 设置默认值
                if (dto.getIsDel() == null) {
                    dto.setIsDel(0); // 默认未删除
                }

                // 查询是否存在有效数据
                MarginalCapitalDTO existDto = selectValidMarginalCapitalDtoByUniqueKey(
                        dto.getAccountingPeriod(), dto.getItemCode());
                log.info("查询账期 {} 项目编码 {} 的结果: {}",
                        dto.getAccountingPeriod(), dto.getItemCode(),
                        existDto != null ? "存在" : "不存在");

                if (StringUtils.isNull(existDto)) {
                    // 不存在，执行新增
                    insertMarginalCapitalDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountingPeriod())
                            .append(" 项目编码 ").append(dto.getItemCode())
                            .append(" 导入成功");
                } else if (updateSupport) {
                    // 存在且允许更新，执行更新
                    dto.setId(existDto.getId());
                    updateMarginalCapitalDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountingPeriod())
                            .append(" 项目编码 ").append(dto.getItemCode())
                            .append(" 更新成功");
                } else {
                    // 存在但不允许更新
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、账期 ").append(dto.getAccountingPeriod())
                            .append(" 项目编码 ").append(dto.getItemCode())
                            .append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账期 " + dto.getAccountingPeriod()
                        + " 项目编码 " + dto.getItemCode() + " 导入失败：";
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
