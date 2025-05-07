package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.LiabilityCashFlowDTO;
import com.xl.alm.app.entity.LiabilityCashFlowEntity;
import com.xl.alm.app.mapper.LiabilityCashFlowMapper;
import com.xl.alm.app.query.LiabilityCashFlowQuery;
import com.xl.alm.app.service.LiabilityCashFlowService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 负债现金流表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class LiabilityCashFlowServiceImpl implements LiabilityCashFlowService {

    @Autowired
    private LiabilityCashFlowMapper liabilityCashFlowMapper;

    /**
     * 查询负债现金流列表
     *
     * @param query 负债现金流查询条件
     * @return 负债现金流列表
     */
    @Override
    public List<LiabilityCashFlowDTO> selectLiabilityCashFlowDtoList(LiabilityCashFlowQuery query) {
        List<LiabilityCashFlowEntity> entityList = liabilityCashFlowMapper.selectLiabilityCashFlowEntityList(query);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, LiabilityCashFlowDTO.class);
    }

    /**
     * 用id查询负债现金流
     *
     * @param id id
     * @return 负债现金流
     */
    @Override
    public LiabilityCashFlowDTO selectLiabilityCashFlowDtoById(Long id) {
        LiabilityCashFlowEntity entity = liabilityCashFlowMapper.selectLiabilityCashFlowEntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, LiabilityCashFlowDTO.class);
    }

    /**
     * 根据条件查询负债现金流
     *
     * @param accountPeriod 账期
     * @param cashFlowType  现金流类型
     * @param bpType        基点类型
     * @param durationType  久期类型
     * @param designType    设计类型
     * @param isShortTerm   是否中短期险种
     * @param actuarialCode 精算代码
     * @return 负债现金流
     */
    @Override
    public LiabilityCashFlowDTO selectLiabilityCashFlowDtoByCondition(
            String accountPeriod,
            String cashFlowType,
            String bpType,
            String durationType,
            String designType,
            String isShortTerm,
            String actuarialCode) {
        LiabilityCashFlowEntity entity = liabilityCashFlowMapper.selectLiabilityCashFlowEntityByCondition(
                accountPeriod,
                cashFlowType,
                bpType,
                durationType,
                designType,
                isShortTerm,
                actuarialCode);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, LiabilityCashFlowDTO.class);
    }

    /**
     * 批量插入负债现金流数据
     *
     * @param dtoList 负债现金流列表
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertLiabilityCashFlowDto(List<LiabilityCashFlowDTO> dtoList) {
        List<LiabilityCashFlowEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, LiabilityCashFlowEntity.class);
        return liabilityCashFlowMapper.batchInsertLiabilityCashFlowEntity(entityList);
    }

    /**
     * 更新负债现金流数据
     *
     * @param dto 负债现金流
     * @return 结果
     */
    @Override
    public int updateLiabilityCashFlowDto(LiabilityCashFlowDTO dto) {
        LiabilityCashFlowEntity entity = EntityDtoConvertUtil.convertToEntity(dto, LiabilityCashFlowEntity.class);
        return liabilityCashFlowMapper.updateLiabilityCashFlowEntity(entity);
    }

    /**
     * 删除指定账期的负债现金流数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteLiabilityCashFlowDtoByPeriod(String accountPeriod) {
        return liabilityCashFlowMapper.deleteLiabilityCashFlowEntityByPeriod(accountPeriod);
    }

    /**
     * 删除指定id的负债现金流数据
     *
     * @param id id
     * @return 影响行数
     */
    @Override
    public int deleteLiabilityCashFlowDtoById(Long id) {
        return liabilityCashFlowMapper.deleteLiabilityCashFlowEntityById(id);
    }
    
    /**
     * 批量删除负债现金流
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteLiabilityCashFlowDtoByIds(Long[] ids) {
        return liabilityCashFlowMapper.deleteLiabilityCashFlowEntityByIds(ids);
    }

    /**
     * 导入负债现金流
     *
     * @param dtoList       负债现金流数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username      操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importLiabilityCashFlowDto(List<LiabilityCashFlowDTO> dtoList, Boolean updateSupport, String username) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入负债现金流不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (LiabilityCashFlowDTO dto : dtoList) {
            try {
                // 验证是否存在这个负债现金流
                LiabilityCashFlowDTO existDto = selectLiabilityCashFlowDtoByCondition(
                        dto.getAccountPeriod(),
                        dto.getCashFlowType(),
                        dto.getBpType(),
                        dto.getDurationType(),
                        dto.getDesignType(),
                        dto.getIsShortTerm(),
                        dto.getActuarialCode());
                
                if (StringUtils.isNull(existDto)) {
                    dto.setCreateBy(username);
                    dto.setUpdateBy(username);
                    List<LiabilityCashFlowDTO> list = new ArrayList<>();
                    list.add(dto);
                    batchInsertLiabilityCashFlowDto(list);
                    successNum++;
//                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod()).append(" 导入成功");
                } else if (updateSupport) {
                    dto.setId(existDto.getId());
                    dto.setUpdateBy(username);
                    updateLiabilityCashFlowDto(dto);
                    successNum++;
//                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod()).append(" 更新成功");
                } else {
                    failureNum++;
                    if (failureNum <= 10) {
                        failureMsg.append("<br/>").append(failureNum).append("、账期 ").append(dto.getAccountPeriod()).append(" 已存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                if (failureNum <= 10) {
                    String msg = "<br/>" + failureNum + "、账期 " + dto.getAccountPeriod() + " 导入失败：";
                    failureMsg.append(msg).append(e.getMessage());
                    log.error(msg, e);
                }

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
