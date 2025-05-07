package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.LiabilityDurationSummaryDTO;
import com.xl.alm.app.entity.LiabilityDurationSummaryEntity;
import com.xl.alm.app.mapper.LiabilityDurationSummaryMapper;
import com.xl.alm.app.query.LiabilityDurationSummaryQuery;
import com.xl.alm.app.service.LiabilityDurationSummaryService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 负债久期汇总表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class LiabilityDurationSummaryServiceImpl implements LiabilityDurationSummaryService {

    @Autowired
    private LiabilityDurationSummaryMapper liabilityDurationSummaryMapper;

    /**
     * 查询负债久期汇总列表
     *
     * @param query 负债久期汇总查询条件
     * @return 负债久期汇总列表
     */
    @Override
    public List<LiabilityDurationSummaryDTO> selectLiabilityDurationSummaryDtoList(LiabilityDurationSummaryQuery query) {
        List<LiabilityDurationSummaryEntity> entityList = liabilityDurationSummaryMapper.selectLiabilityDurationSummaryEntityList(query);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, LiabilityDurationSummaryDTO.class);
    }

    /**
     * 用id查询负债久期汇总
     *
     * @param id id
     * @return 负债久期汇总
     */
    @Override
    public LiabilityDurationSummaryDTO selectLiabilityDurationSummaryDtoById(Long id) {
        LiabilityDurationSummaryEntity entity = liabilityDurationSummaryMapper.selectLiabilityDurationSummaryEntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, LiabilityDurationSummaryDTO.class);
    }

    /**
     * 根据条件查询负债久期汇总
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @param isShortTerm 是否为中短期险种
     * @return 负债久期汇总
     */
    @Override
    public LiabilityDurationSummaryDTO selectLiabilityDurationSummaryDtoByCondition(
            String accountPeriod,
            String cashFlowType,
            String durationType,
            String designType,
            String isShortTerm) {
        LiabilityDurationSummaryEntity entity = liabilityDurationSummaryMapper.selectLiabilityDurationSummaryEntityByCondition(
                accountPeriod, cashFlowType, durationType, designType, isShortTerm);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, LiabilityDurationSummaryDTO.class);
    }

    /**
     * 新增负债久期汇总
     *
     * @param dto 负债久期汇总
     * @return 结果
     */
    @Override
    public int insertLiabilityDurationSummaryDto(LiabilityDurationSummaryDTO dto) {
        LiabilityDurationSummaryEntity entity = EntityDtoConvertUtil.convertToEntity(dto, LiabilityDurationSummaryEntity.class);
        return liabilityDurationSummaryMapper.insertLiabilityDurationSummaryEntity(entity);
    }

    /**
     * 批量新增负债久期汇总
     *
     * @param dtoList 负债久期汇总列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertLiabilityDurationSummaryDto(List<LiabilityDurationSummaryDTO> dtoList) {
        List<LiabilityDurationSummaryEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, LiabilityDurationSummaryEntity.class);
        return liabilityDurationSummaryMapper.batchInsertLiabilityDurationSummaryEntity(entityList);
    }

    /**
     * 修改负债久期汇总
     *
     * @param dto 负债久期汇总
     * @return 结果
     */
    @Override
    public int updateLiabilityDurationSummaryDto(LiabilityDurationSummaryDTO dto) {
        LiabilityDurationSummaryEntity entity = EntityDtoConvertUtil.convertToEntity(dto, LiabilityDurationSummaryEntity.class);
        return liabilityDurationSummaryMapper.updateLiabilityDurationSummaryEntity(entity);
    }

    /**
     * 批量删除负债久期汇总
     *
     * @param ids 需要删除的负债久期汇总主键集合
     * @return 结果
     */
    @Override
    public int deleteLiabilityDurationSummaryDtoByIds(Long[] ids) {
        return liabilityDurationSummaryMapper.deleteLiabilityDurationSummaryEntityByIds(ids);
    }

    /**
     * 删除负债久期汇总信息
     *
     * @param id 负债久期汇总主键
     * @return 结果
     */
    @Override
    public int deleteLiabilityDurationSummaryDtoById(Long id) {
        return liabilityDurationSummaryMapper.deleteLiabilityDurationSummaryEntityById(id);
    }

    /**
     * 根据账期删除负债久期汇总
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    @Override
    public int deleteLiabilityDurationSummaryDtoByPeriod(String accountPeriod) {
        return liabilityDurationSummaryMapper.deleteLiabilityDurationSummaryEntityByPeriod(accountPeriod);
    }

    /**
     * 导入负债久期汇总数据
     *
     * @param dtoList 负债久期汇总数据列表
     * @param updateSupport 是否支持更新，如果已存在，则进行更新
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importLiabilityDurationSummaryDto(List<LiabilityDurationSummaryDTO> dtoList, boolean updateSupport, String operName) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (LiabilityDurationSummaryDTO dto : dtoList) {
            try {
                // 设置操作人
                dto.setCreateBy(operName);
                dto.setUpdateBy(operName);

                // 验证是否存在相同记录
                LiabilityDurationSummaryDTO existDto = selectLiabilityDurationSummaryDtoByCondition(
                        dto.getAccountPeriod(), dto.getCashFlowType(),
                        dto.getDurationType(), dto.getDesignType(), dto.getIsShortTerm());

                if (StringUtils.isNull(existDto)) {
                    // 不存在，执行新增
                    insertLiabilityDurationSummaryDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期为 ").append(dto.getAccountPeriod()).append(" 的数据导入成功");
                } else if (updateSupport) {
                    // 存在且允许更新，执行更新
                    dto.setId(existDto.getId());
                    updateLiabilityDurationSummaryDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期为 ").append(dto.getAccountPeriod()).append(" 的数据更新成功");
                } else {
                    // 存在但不允许更新，记录失败
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
