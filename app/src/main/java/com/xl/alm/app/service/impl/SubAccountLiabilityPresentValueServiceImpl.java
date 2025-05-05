package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.SubAccountLiabilityPresentValueDTO;
import com.xl.alm.app.entity.SubAccountLiabilityPresentValueEntity;
import com.xl.alm.app.mapper.SubAccountLiabilityPresentValueMapper;
import com.xl.alm.app.query.SubAccountLiabilityPresentValueQuery;
import com.xl.alm.app.service.SubAccountLiabilityPresentValueService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 分账户负债现金流现值汇总表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class SubAccountLiabilityPresentValueServiceImpl implements SubAccountLiabilityPresentValueService {

    @Autowired
    private SubAccountLiabilityPresentValueMapper subAccountLiabilityPresentValueMapper;

    /**
     * 查询分账户负债现金流现值汇总列表
     *
     * @param query 分账户负债现金流现值汇总查询条件
     * @return 分账户负债现金流现值汇总列表
     */
    @Override
    public List<SubAccountLiabilityPresentValueDTO> selectSubAccountLiabilityPresentValueDtoList(SubAccountLiabilityPresentValueQuery query) {
        List<SubAccountLiabilityPresentValueEntity> entityList = subAccountLiabilityPresentValueMapper.selectSubAccountLiabilityPresentValueEntityList(query);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, SubAccountLiabilityPresentValueDTO.class);
    }

    /**
     * 用id查询分账户负债现金流现值汇总
     *
     * @param id id
     * @return 分账户负债现金流现值汇总
     */
    @Override
    public SubAccountLiabilityPresentValueDTO selectSubAccountLiabilityPresentValueDtoById(Long id) {
        SubAccountLiabilityPresentValueEntity entity = subAccountLiabilityPresentValueMapper.selectSubAccountLiabilityPresentValueEntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, SubAccountLiabilityPresentValueDTO.class);
    }

    /**
     * 根据条件查询分账户负债现金流现值汇总
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @return 分账户负债现金流现值汇总
     */
    @Override
    public SubAccountLiabilityPresentValueDTO selectSubAccountLiabilityPresentValueDtoByCondition(
            String accountPeriod,
            String cashFlowType,
            String bpType,
            String durationType,
            String designType) {
        SubAccountLiabilityPresentValueEntity entity = subAccountLiabilityPresentValueMapper.selectSubAccountLiabilityPresentValueEntityByCondition(
                accountPeriod, cashFlowType, bpType, durationType, designType);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, SubAccountLiabilityPresentValueDTO.class);
    }

    /**
     * 新增分账户负债现金流现值汇总
     *
     * @param dto 分账户负债现金流现值汇总
     * @return 结果
     */
    @Override
    public int insertSubAccountLiabilityPresentValueDto(SubAccountLiabilityPresentValueDTO dto) {
        SubAccountLiabilityPresentValueEntity entity = EntityDtoConvertUtil.convertToEntity(dto, SubAccountLiabilityPresentValueEntity.class);
        return subAccountLiabilityPresentValueMapper.insertSubAccountLiabilityPresentValueEntity(entity);
    }

    /**
     * 批量新增分账户负债现金流现值汇总
     *
     * @param dtoList 分账户负债现金流现值汇总列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertSubAccountLiabilityPresentValueDto(List<SubAccountLiabilityPresentValueDTO> dtoList) {
        List<SubAccountLiabilityPresentValueEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, SubAccountLiabilityPresentValueEntity.class);
        return subAccountLiabilityPresentValueMapper.batchInsertSubAccountLiabilityPresentValueEntity(entityList);
    }

    /**
     * 修改分账户负债现金流现值汇总
     *
     * @param dto 分账户负债现金流现值汇总
     * @return 结果
     */
    @Override
    public int updateSubAccountLiabilityPresentValueDto(SubAccountLiabilityPresentValueDTO dto) {
        SubAccountLiabilityPresentValueEntity entity = EntityDtoConvertUtil.convertToEntity(dto, SubAccountLiabilityPresentValueEntity.class);
        return subAccountLiabilityPresentValueMapper.updateSubAccountLiabilityPresentValueEntity(entity);
    }

    /**
     * 批量删除分账户负债现金流现值汇总
     *
     * @param ids 需要删除的分账户负债现金流现值汇总主键集合
     * @return 结果
     */
    @Override
    public int deleteSubAccountLiabilityPresentValueDtoByIds(Long[] ids) {
        return subAccountLiabilityPresentValueMapper.deleteSubAccountLiabilityPresentValueEntityByIds(ids);
    }

    /**
     * 删除指定账期的分账户负债现金流现值汇总数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    @Override
    public int deleteSubAccountLiabilityPresentValueDtoByPeriod(String accountPeriod) {
        return subAccountLiabilityPresentValueMapper.deleteSubAccountLiabilityPresentValueEntityByPeriod(accountPeriod);
    }

    /**
     * 导入分账户负债现金流现值汇总数据
     *
     * @param dtoList 分账户负债现金流现值汇总数据列表
     * @param updateSupport 是否支持更新，如果已存在，则进行更新
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importSubAccountLiabilityPresentValueDto(List<SubAccountLiabilityPresentValueDTO> dtoList, boolean updateSupport, String operName) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (SubAccountLiabilityPresentValueDTO dto : dtoList) {
            try {
                // 设置操作人
                dto.setCreateBy(operName);
                dto.setUpdateBy(operName);

                // 查询是否已存在
                SubAccountLiabilityPresentValueDTO existDto = selectSubAccountLiabilityPresentValueDtoByCondition(
                        dto.getAccountPeriod(), dto.getCashFlowType(), dto.getBpType(), dto.getDurationType(), dto.getDesignType());

                if (StringUtils.isNull(existDto)) {
                    // 不存在，执行新增
                    insertSubAccountLiabilityPresentValueDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期为 ").append(dto.getAccountPeriod()).append(" 的数据导入成功");
                } else if (updateSupport) {
                    // 存在且允许更新，执行更新
                    dto.setId(existDto.getId());
                    updateSubAccountLiabilityPresentValueDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期为 ").append(dto.getAccountPeriod()).append(" 的数据更新成功");
                } else {
                    // 存在但不允许更新
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、账期为 ").append(dto.getAccountPeriod()).append(" 的数据已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账期为 " + dto.getAccountPeriod() + " 的数据导入失败：";
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
