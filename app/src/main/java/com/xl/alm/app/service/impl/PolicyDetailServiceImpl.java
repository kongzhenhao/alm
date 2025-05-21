package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.PolicyDetailDTO;
import com.xl.alm.app.entity.PolicyDetailEntity;
import com.xl.alm.app.mapper.PolicyDetailMapper;
import com.xl.alm.app.query.PolicyDetailQuery;
import com.xl.alm.app.service.PolicyDetailService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 保单数据明细表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class PolicyDetailServiceImpl implements PolicyDetailService {

    @Autowired
    private PolicyDetailMapper policyDetailMapper;

    /**
     * 查询保单数据明细列表
     *
     * @param query 保单数据明细查询条件
     * @return 保单数据明细列表
     */
    @Override
    public List<PolicyDetailDTO> selectPolicyDetailDtoList(PolicyDetailQuery query) {
        List<PolicyDetailEntity> entityList = policyDetailMapper.selectPolicyDetailEntityList(query);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, PolicyDetailDTO.class);
    }

    /**
     * 用id查询保单数据明细
     *
     * @param id id
     * @return 保单数据明细
     */
    @Override
    public PolicyDetailDTO selectPolicyDetailDtoById(Long id) {
        PolicyDetailEntity entity = policyDetailMapper.selectPolicyDetailEntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, PolicyDetailDTO.class);
    }

    /**
     * 根据险种号查询保单数据明细
     *
     * @param polNo 险种号
     * @return 保单数据明细
     */
    @Override
    public PolicyDetailDTO selectPolicyDetailDtoByPolNo(String polNo) {
        PolicyDetailEntity entity = policyDetailMapper.selectPolicyDetailEntityByPolNo(polNo);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, PolicyDetailDTO.class);
    }

    /**
     * 新增保单数据明细
     *
     * @param dto 保单数据明细
     * @return 结果
     */
    @Override
    public int insertPolicyDetailDto(PolicyDetailDTO dto) {
        PolicyDetailEntity entity = EntityDtoConvertUtil.convertToEntity(dto, PolicyDetailEntity.class);
        return policyDetailMapper.insertPolicyDetailEntity(entity);
    }

    /**
     * 批量新增保单数据明细
     *
     * @param dtoList 保单数据明细列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertPolicyDetailDto(List<PolicyDetailDTO> dtoList) {
        List<PolicyDetailEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, PolicyDetailEntity.class);
        return policyDetailMapper.batchInsertPolicyDetailEntity(entityList);
    }

    /**
     * 修改保单数据明细
     *
     * @param dto 保单数据明细
     * @return 结果
     */
    @Override
    public int updatePolicyDetailDto(PolicyDetailDTO dto) {
        PolicyDetailEntity entity = EntityDtoConvertUtil.convertToEntity(dto, PolicyDetailEntity.class);
        return policyDetailMapper.updatePolicyDetailEntity(entity);
    }

    /**
     * 批量删除保单数据明细
     *
     * @param ids 需要删除的保单数据明细主键集合
     * @return 结果
     */
    @Override
    public int deletePolicyDetailDtoByIds(Long[] ids) {
        return policyDetailMapper.deletePolicyDetailEntityByIds(ids);
    }

    /**
     * 删除保单数据明细信息
     *
     * @param id 保单数据明细主键
     * @return 结果
     */
    @Override
    public int deletePolicyDetailDtoById(Long id) {
        return policyDetailMapper.deletePolicyDetailEntityById(id);
    }

    /**
     * 导入保单数据明细
     *
     * @param dtoList 保单数据明细数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importPolicyDetailDto(List<PolicyDetailDTO> dtoList, Boolean updateSupport, String username) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入保单数据明细数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (PolicyDetailDTO dto : dtoList) {
            try {
                // 验证是否存在这个保单数据明细
                PolicyDetailDTO existDto = selectPolicyDetailDtoByPolNo(dto.getPolNo());

                if (StringUtils.isNull(existDto)) {
                    dto.setCreateBy(username);
                    dto.setUpdateBy(username);
                    insertPolicyDetailDto(dto);
                    successNum++;
                } else if (updateSupport) {
                    dto.setId(existDto.getId());
                    dto.setUpdateBy(username);
                    updatePolicyDetailDto(dto);
                    successNum++;
                } else {
                    failureNum++;
                    if (failureNum <= 10) {
                        failureMsg.append("<br/>").append(failureNum).append("、险种号 ").append(dto.getPolNo())
                                .append(" 已存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、险种号 " + dto.getPolNo() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new RuntimeException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条");
        }
        return successMsg.toString();
    }
}
