package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.LiabilityCashFlowSummaryDTO;
import com.xl.alm.app.entity.LiabilityCashFlowSummaryEntity;
import com.xl.alm.app.mapper.LiabilityCashFlowSummaryMapper;
import com.xl.alm.app.query.LiabilityCashFlowSummaryQuery;
import com.xl.alm.app.service.LiabilityCashFlowSummaryService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 负债现金流汇总表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class LiabilityCashFlowSummaryServiceImpl implements LiabilityCashFlowSummaryService {

    @Autowired
    private LiabilityCashFlowSummaryMapper liabilityCashFlowSummaryMapper;

    /**
     * 查询负债现金流汇总列表
     *
     * @param query 负债现金流汇总查询条件
     * @return 负债现金流汇总列表
     */
    @Override
    public List<LiabilityCashFlowSummaryDTO> selectLiabilityCashFlowSummaryDtoList(LiabilityCashFlowSummaryQuery query) {
        List<LiabilityCashFlowSummaryEntity> entityList = liabilityCashFlowSummaryMapper.selectLiabilityCashFlowSummaryEntityList(query);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, LiabilityCashFlowSummaryDTO.class);
    }

    /**
     * 用id查询负债现金流汇总
     *
     * @param id id
     * @return 负债现金流汇总
     */
    @Override
    public LiabilityCashFlowSummaryDTO selectLiabilityCashFlowSummaryDtoById(Long id) {
        LiabilityCashFlowSummaryEntity entity = liabilityCashFlowSummaryMapper.selectLiabilityCashFlowSummaryEntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, LiabilityCashFlowSummaryDTO.class);
    }

    /**
     * 根据条件查询负债现金流汇总
     *
     * @param accountPeriod 账期
     * @param cashFlowType  现金流类型
     * @param durationType  久期类型
     * @param designType    设计类型
     * @param isShortTerm   是否中短期险种
     * @return 负债现金流汇总
     */
    @Override
    public LiabilityCashFlowSummaryDTO selectLiabilityCashFlowSummaryDtoByCondition(
            String accountPeriod,
            String cashFlowType,
            String durationType,
            String designType,
            String isShortTerm) {
        LiabilityCashFlowSummaryEntity entity = liabilityCashFlowSummaryMapper.selectLiabilityCashFlowSummaryEntityByCondition(
                accountPeriod,
                cashFlowType,
                durationType,
                designType,
                isShortTerm);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, LiabilityCashFlowSummaryDTO.class);
    }

    /**
     * 新增负债现金流汇总
     *
     * @param dto 负债现金流汇总
     * @return 结果
     */
    @Override
    public int insertLiabilityCashFlowSummaryDto(LiabilityCashFlowSummaryDTO dto) {
        LiabilityCashFlowSummaryEntity entity = EntityDtoConvertUtil.convertToEntity(dto, LiabilityCashFlowSummaryEntity.class);
        return liabilityCashFlowSummaryMapper.insertLiabilityCashFlowSummaryEntity(entity);
    }

    /**
     * 批量新增负债现金流汇总
     *
     * @param dtoList 负债现金流汇总列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertLiabilityCashFlowSummaryDto(List<LiabilityCashFlowSummaryDTO> dtoList) {
        List<LiabilityCashFlowSummaryEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, LiabilityCashFlowSummaryEntity.class);
        return liabilityCashFlowSummaryMapper.batchInsertLiabilityCashFlowSummaryEntity(entityList);
    }

    /**
     * 修改负债现金流汇总
     *
     * @param dto 负债现金流汇总
     * @return 结果
     */
    @Override
    public int updateLiabilityCashFlowSummaryDto(LiabilityCashFlowSummaryDTO dto) {
        LiabilityCashFlowSummaryEntity entity = EntityDtoConvertUtil.convertToEntity(dto, LiabilityCashFlowSummaryEntity.class);
        return liabilityCashFlowSummaryMapper.updateLiabilityCashFlowSummaryEntity(entity);
    }

    /**
     * 批量删除负债现金流汇总
     *
     * @param ids 需要删除的负债现金流汇总主键集合
     * @return 结果
     */
    @Override
    public int deleteLiabilityCashFlowSummaryDtoByIds(Long[] ids) {
        return liabilityCashFlowSummaryMapper.deleteLiabilityCashFlowSummaryEntityByIds(ids);
    }

    /**
     * 删除负债现金流汇总信息
     *
     * @param id 负债现金流汇总主键
     * @return 结果
     */
    @Override
    public int deleteLiabilityCashFlowSummaryDtoById(Long id) {
        return liabilityCashFlowSummaryMapper.deleteLiabilityCashFlowSummaryEntityById(id);
    }

    /**
     * 根据账期删除负债现金流汇总
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    @Override
    public int deleteLiabilityCashFlowSummaryDtoByPeriod(String accountPeriod) {
        return liabilityCashFlowSummaryMapper.deleteLiabilityCashFlowSummaryEntityByPeriod(accountPeriod);
    }

    /**
     * 导入负债现金流汇总数据
     *
     * @param dtoList       负债现金流汇总数据列表
     * @param updateSupport 是否支持更新，如果已存在，则进行更新
     * @param operName      操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importLiabilityCashFlowSummaryDto(List<LiabilityCashFlowSummaryDTO> dtoList, boolean updateSupport, String operName) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (LiabilityCashFlowSummaryDTO dto : dtoList) {
            try {
                // 设置操作人
                dto.setCreateBy(operName);
                dto.setUpdateBy(operName);

                // 验证是否存在相同的记录
                LiabilityCashFlowSummaryDTO existDto = selectLiabilityCashFlowSummaryDtoByCondition(
                        dto.getAccountPeriod(),
                        dto.getCashFlowType(),
                        dto.getDurationType(),
                        dto.getDesignType(),
                        dto.getIsShortTerm());

                if (StringUtils.isNull(existDto)) {
                    // 不存在，执行新增
                    insertLiabilityCashFlowSummaryDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod()).append(" 导入成功");
                } else if (updateSupport) {
                    // 存在且允许更新，执行更新
                    dto.setId(existDto.getId());
                    updateLiabilityCashFlowSummaryDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod()).append(" 更新成功");
                } else {
                    // 存在但不允许更新
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
