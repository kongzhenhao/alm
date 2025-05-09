package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.SubAccountLiabilityDurationDTO;
import com.xl.alm.app.entity.SubAccountLiabilityDurationEntity;
import com.xl.alm.app.mapper.SubAccountLiabilityDurationMapper;
import com.xl.alm.app.query.SubAccountLiabilityDurationQuery;
import com.xl.alm.app.service.SubAccountLiabilityDurationService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 分账户负债久期汇总表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class SubAccountLiabilityDurationServiceImpl implements SubAccountLiabilityDurationService {

    @Autowired
    private SubAccountLiabilityDurationMapper subAccountLiabilityDurationMapper;

    /**
     * 查询分账户负债久期汇总列表
     *
     * @param query 分账户负债久期汇总查询条件
     * @return 分账户负债久期汇总列表
     */
    @Override
    public List<SubAccountLiabilityDurationDTO> selectSubAccountLiabilityDurationDtoList(SubAccountLiabilityDurationQuery query) {
        List<SubAccountLiabilityDurationEntity> entityList = subAccountLiabilityDurationMapper.selectSubAccountLiabilityDurationEntityList(query);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, SubAccountLiabilityDurationDTO.class);
    }

    /**
     * 用id查询分账户负债久期汇总
     *
     * @param id id
     * @return 分账户负债久期汇总
     */
    @Override
    public SubAccountLiabilityDurationDTO selectSubAccountLiabilityDurationDtoById(Long id) {
        SubAccountLiabilityDurationEntity entity = subAccountLiabilityDurationMapper.selectSubAccountLiabilityDurationEntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, SubAccountLiabilityDurationDTO.class);
    }

    /**
     * 根据条件查询分账户负债久期汇总
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @return 分账户负债久期汇总
     */
    @Override
    public SubAccountLiabilityDurationDTO selectSubAccountLiabilityDurationDtoByCondition(
            String accountPeriod,
            String cashFlowType,
            String durationType,
            String designType) {
        SubAccountLiabilityDurationEntity entity = subAccountLiabilityDurationMapper.selectSubAccountLiabilityDurationEntityByCondition(
                accountPeriod, cashFlowType, durationType, designType);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, SubAccountLiabilityDurationDTO.class);
    }

    /**
     * 新增分账户负债久期汇总
     *
     * @param dto 分账户负债久期汇总
     * @return 结果
     */
    @Override
    public int insertSubAccountLiabilityDurationDto(SubAccountLiabilityDurationDTO dto) {
        SubAccountLiabilityDurationEntity entity = EntityDtoConvertUtil.convertToEntity(dto, SubAccountLiabilityDurationEntity.class);
        return subAccountLiabilityDurationMapper.insertSubAccountLiabilityDurationEntity(entity);
    }

    /**
     * 批量新增分账户负债久期汇总
     *
     * @param dtoList 分账户负债久期汇总列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertSubAccountLiabilityDurationDto(List<SubAccountLiabilityDurationDTO> dtoList) {
        List<SubAccountLiabilityDurationEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, SubAccountLiabilityDurationEntity.class);
        return subAccountLiabilityDurationMapper.batchInsertSubAccountLiabilityDurationEntity(entityList);
    }

    /**
     * 修改分账户负债久期汇总
     *
     * @param dto 分账户负债久期汇总
     * @return 结果
     */
    @Override
    public int updateSubAccountLiabilityDurationDto(SubAccountLiabilityDurationDTO dto) {
        SubAccountLiabilityDurationEntity entity = EntityDtoConvertUtil.convertToEntity(dto, SubAccountLiabilityDurationEntity.class);
        return subAccountLiabilityDurationMapper.updateSubAccountLiabilityDurationEntity(entity);
    }

    /**
     * 批量删除分账户负债久期汇总
     *
     * @param ids 需要删除的分账户负债久期汇总主键集合
     * @return 结果
     */
    @Override
    public int deleteSubAccountLiabilityDurationDtoByIds(Long[] ids) {
        return subAccountLiabilityDurationMapper.deleteSubAccountLiabilityDurationEntityByIds(ids);
    }

    /**
     * 删除分账户负债久期汇总信息
     *
     * @param id 分账户负债久期汇总主键
     * @return 结果
     */
    @Override
    public int deleteSubAccountLiabilityDurationDtoById(Long id) {
        return subAccountLiabilityDurationMapper.deleteSubAccountLiabilityDurationEntityById(id);
    }

    /**
     * 删除指定账期的分账户负债久期汇总
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    @Override
    public int deleteSubAccountLiabilityDurationDtoByPeriod(String accountPeriod) {
        return subAccountLiabilityDurationMapper.deleteSubAccountLiabilityDurationEntityByPeriod(accountPeriod);
    }

    /**
     * 导入分账户负债久期汇总数据
     *
     * @param dtoList 分账户负债久期汇总数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importSubAccountLiabilityDurationDto(List<SubAccountLiabilityDurationDTO> dtoList, boolean updateSupport, String operName) throws Exception {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new Exception("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (SubAccountLiabilityDurationDTO dto : dtoList) {
            try {
                // 验证是否存在这个用户
                SubAccountLiabilityDurationDTO existDto = selectSubAccountLiabilityDurationDtoByCondition(
                        dto.getAccountPeriod(), dto.getCashFlowType(), dto.getDurationType(), dto.getDesignType());
                if (StringUtils.isNull(existDto)) {
                    dto.setCreateBy(operName);
                    dto.setUpdateBy(operName);
                    insertSubAccountLiabilityDurationDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod()).append(" 导入成功");
                } else if (updateSupport) {
                    dto.setId(existDto.getId());
                    dto.setUpdateBy(operName);
                    updateSubAccountLiabilityDurationDto(dto);
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
            throw new Exception(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
