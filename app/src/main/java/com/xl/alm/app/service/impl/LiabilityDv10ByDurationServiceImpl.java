package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.LiabilityDv10ByDurationDTO;
import com.xl.alm.app.entity.LiabilityDv10ByDurationEntity;
import com.xl.alm.app.mapper.LiabilityDv10ByDurationMapper;
import com.xl.alm.app.query.LiabilityDv10ByDurationQuery;
import com.xl.alm.app.service.LiabilityDv10ByDurationService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 分中短负债基点价值DV10表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class LiabilityDv10ByDurationServiceImpl implements LiabilityDv10ByDurationService {

    @Autowired
    private LiabilityDv10ByDurationMapper liabilityDv10ByDurationMapper;

    /**
     * 查询分中短负债基点价值DV10列表
     *
     * @param query 分中短负债基点价值DV10查询条件
     * @return 分中短负债基点价值DV10列表
     */
    @Override
    public List<LiabilityDv10ByDurationDTO> selectLiabilityDv10ByDurationDtoList(LiabilityDv10ByDurationQuery query) {
        List<LiabilityDv10ByDurationEntity> entityList = liabilityDv10ByDurationMapper.selectLiabilityDv10ByDurationEntityList(query);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, LiabilityDv10ByDurationDTO.class);
    }

    /**
     * 根据ID查询分中短负债基点价值DV10
     *
     * @param id ID
     * @return 分中短负债基点价值DV10
     */
    @Override
    public LiabilityDv10ByDurationDTO selectLiabilityDv10ByDurationDtoById(Long id) {
        LiabilityDv10ByDurationEntity entity = liabilityDv10ByDurationMapper.selectLiabilityDv10ByDurationEntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, LiabilityDv10ByDurationDTO.class);
    }

    /**
     * 根据条件查询分中短负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @param cashFlowType  现金流类型
     * @param designType    设计类型
     * @param shortTermFlag 是否为中短期险种
     * @param valueType     现值类型
     * @return 分中短负债基点价值DV10
     */
    @Override
    public LiabilityDv10ByDurationDTO selectLiabilityDv10ByDurationDtoByCondition(
            String accountPeriod,
            String cashFlowType,
            String designType,
            String shortTermFlag,
            String valueType) {
        LiabilityDv10ByDurationEntity entity = liabilityDv10ByDurationMapper.selectLiabilityDv10ByDurationEntityByCondition(
                accountPeriod, cashFlowType, designType, shortTermFlag, valueType);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, LiabilityDv10ByDurationDTO.class);
    }

    /**
     * 新增分中短负债基点价值DV10
     *
     * @param dto 分中短负债基点价值DV10
     * @return 结果
     */
    @Override
    public int insertLiabilityDv10ByDurationDto(LiabilityDv10ByDurationDTO dto) {
        LiabilityDv10ByDurationEntity entity = EntityDtoConvertUtil.convertToEntity(dto, LiabilityDv10ByDurationEntity.class);
        return liabilityDv10ByDurationMapper.insertLiabilityDv10ByDurationEntity(entity);
    }

    /**
     * 批量新增分中短负债基点价值DV10
     *
     * @param dtoList 分中短负债基点价值DV10列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertLiabilityDv10ByDurationDto(List<LiabilityDv10ByDurationDTO> dtoList) {
        List<LiabilityDv10ByDurationEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, LiabilityDv10ByDurationEntity.class);
        return liabilityDv10ByDurationMapper.batchInsertLiabilityDv10ByDurationEntity(entityList);
    }

    /**
     * 修改分中短负债基点价值DV10
     *
     * @param dto 分中短负债基点价值DV10
     * @return 结果
     */
    @Override
    public int updateLiabilityDv10ByDurationDto(LiabilityDv10ByDurationDTO dto) {
        LiabilityDv10ByDurationEntity entity = EntityDtoConvertUtil.convertToEntity(dto, LiabilityDv10ByDurationEntity.class);
        return liabilityDv10ByDurationMapper.updateLiabilityDv10ByDurationEntity(entity);
    }

    /**
     * 删除分中短负债基点价值DV10
     *
     * @param id 分中短负债基点价值DV10ID
     * @return 结果
     */
    @Override
    public int deleteLiabilityDv10ByDurationDtoById(Long id) {
        return liabilityDv10ByDurationMapper.deleteLiabilityDv10ByDurationEntityById(id);
    }

    /**
     * 批量删除分中短负债基点价值DV10
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteLiabilityDv10ByDurationDtoByIds(Long[] ids) {
        return liabilityDv10ByDurationMapper.deleteLiabilityDv10ByDurationEntityByIds(ids);
    }

    /**
     * 删除指定账期的分中短负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteLiabilityDv10ByDurationDtoByPeriod(String accountPeriod) {
        return liabilityDv10ByDurationMapper.deleteLiabilityDv10ByDurationEntityByPeriod(accountPeriod);
    }

    /**
     * 导入分中短负债基点价值DV10数据
     *
     * @param dtoList       分中短负债基点价值DV10列表
     * @param updateSupport 是否更新已存在的数据
     * @param operName      操作人
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importLiabilityDv10ByDurationDto(List<LiabilityDv10ByDurationDTO> dtoList, boolean updateSupport, String operName) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (LiabilityDv10ByDurationDTO dto : dtoList) {
            try {
                // 验证是否存在相同的记录
                LiabilityDv10ByDurationDTO existDto = selectLiabilityDv10ByDurationDtoByCondition(
                        dto.getAccountPeriod(), dto.getCashFlowType(), dto.getDesignType(),
                        dto.getShortTermFlag(), dto.getValueType());
                if (StringUtils.isNull(existDto)) {
                    dto.setCreateBy(operName);
                    dto.setUpdateBy(operName);
                    insertLiabilityDv10ByDurationDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod()).append(" 导入成功");
                } else if (updateSupport) {
                    dto.setId(existDto.getId());
                    dto.setUpdateBy(operName);
                    updateLiabilityDv10ByDurationDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、账期 ").append(dto.getAccountPeriod()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账期 " + dto.getAccountPeriod() + " 导入失败：";
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
