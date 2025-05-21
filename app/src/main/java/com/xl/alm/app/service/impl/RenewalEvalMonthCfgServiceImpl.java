package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.RenewalEvalMonthCfgDTO;
import com.xl.alm.app.entity.RenewalEvalMonthCfgEntity;
import com.xl.alm.app.mapper.RenewalEvalMonthCfgMapper;
import com.xl.alm.app.query.RenewalEvalMonthCfgQuery;
import com.xl.alm.app.service.RenewalEvalMonthCfgService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 续保率评估月份配置表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class RenewalEvalMonthCfgServiceImpl implements RenewalEvalMonthCfgService {

    @Autowired
    private RenewalEvalMonthCfgMapper renewalEvalMonthCfgMapper;

    /**
     * 查询续保率评估月份配置列表
     *
     * @param query 续保率评估月份配置查询条件
     * @return 续保率评估月份配置列表
     */
    @Override
    public List<RenewalEvalMonthCfgDTO> selectRenewalEvalMonthCfgDtoList(RenewalEvalMonthCfgQuery query) {
        List<RenewalEvalMonthCfgEntity> entityList = renewalEvalMonthCfgMapper.selectRenewalEvalMonthCfgEntityList(query);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, RenewalEvalMonthCfgDTO.class);
    }

    /**
     * 用id查询续保率评估月份配置
     *
     * @param id id
     * @return 续保率评估月份配置
     */
    @Override
    public RenewalEvalMonthCfgDTO selectRenewalEvalMonthCfgDtoById(Long id) {
        RenewalEvalMonthCfgEntity entity = renewalEvalMonthCfgMapper.selectRenewalEvalMonthCfgEntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, RenewalEvalMonthCfgDTO.class);
    }

    /**
     * 根据账期和当季第几月查询续保率评估月份配置
     *
     * @param accountingPeriod 账期
     * @param monthSeqInCurrQuarter 当季第几月
     * @return 续保率评估月份配置
     */
    @Override
    public RenewalEvalMonthCfgDTO selectRenewalEvalMonthCfgDtoByCondition(
            String accountingPeriod,
            Integer monthSeqInCurrQuarter) {
        RenewalEvalMonthCfgEntity entity = renewalEvalMonthCfgMapper.selectRenewalEvalMonthCfgEntityByCondition(
                accountingPeriod,
                monthSeqInCurrQuarter);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, RenewalEvalMonthCfgDTO.class);
    }

    /**
     * 新增续保率评估月份配置
     *
     * @param dto 续保率评估月份配置
     * @return 结果
     */
    @Override
    public int insertRenewalEvalMonthCfgDto(RenewalEvalMonthCfgDTO dto) {
        RenewalEvalMonthCfgEntity entity = EntityDtoConvertUtil.convertToEntity(dto, RenewalEvalMonthCfgEntity.class);
        return renewalEvalMonthCfgMapper.insertRenewalEvalMonthCfgEntity(entity);
    }

    /**
     * 批量新增续保率评估月份配置
     *
     * @param dtoList 续保率评估月份配置列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertRenewalEvalMonthCfgDto(List<RenewalEvalMonthCfgDTO> dtoList) {
        List<RenewalEvalMonthCfgEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, RenewalEvalMonthCfgEntity.class);
        return renewalEvalMonthCfgMapper.batchInsertRenewalEvalMonthCfgEntity(entityList);
    }

    /**
     * 修改续保率评估月份配置
     *
     * @param dto 续保率评估月份配置
     * @return 结果
     */
    @Override
    public int updateRenewalEvalMonthCfgDto(RenewalEvalMonthCfgDTO dto) {
        RenewalEvalMonthCfgEntity entity = EntityDtoConvertUtil.convertToEntity(dto, RenewalEvalMonthCfgEntity.class);
        return renewalEvalMonthCfgMapper.updateRenewalEvalMonthCfgEntity(entity);
    }

    /**
     * 批量删除续保率评估月份配置
     *
     * @param ids 需要删除的续保率评估月份配置主键集合
     * @return 结果
     */
    @Override
    public int deleteRenewalEvalMonthCfgDtoByIds(Long[] ids) {
        return renewalEvalMonthCfgMapper.deleteRenewalEvalMonthCfgEntityByIds(ids);
    }

    /**
     * 删除续保率评估月份配置信息
     *
     * @param id 续保率评估月份配置主键
     * @return 结果
     */
    @Override
    public int deleteRenewalEvalMonthCfgDtoById(Long id) {
        return renewalEvalMonthCfgMapper.deleteRenewalEvalMonthCfgEntityById(id);
    }

    /**
     * 导入续保率评估月份配置
     *
     * @param dtoList 续保率评估月份配置数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importRenewalEvalMonthCfgDto(List<RenewalEvalMonthCfgDTO> dtoList, Boolean updateSupport, String username) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入续保率评估月份配置数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (RenewalEvalMonthCfgDTO dto : dtoList) {
            try {
                // 验证是否存在这个续保率评估月份配置
                RenewalEvalMonthCfgDTO existDto = selectRenewalEvalMonthCfgDtoByCondition(
                        dto.getAccountingPeriod(),
                        dto.getMonthSeqInCurrQuarter());

                if (StringUtils.isNull(existDto)) {
                    dto.setCreateBy(username);
                    dto.setUpdateBy(username);
                    insertRenewalEvalMonthCfgDto(dto);
                    successNum++;
                } else if (updateSupport) {
                    dto.setId(existDto.getId());
                    dto.setUpdateBy(username);
                    updateRenewalEvalMonthCfgDto(dto);
                    successNum++;
                } else {
                    failureNum++;
                    if (failureNum <= 10) {
                        failureMsg.append("<br/>").append(failureNum).append("、账期 ").append(dto.getAccountingPeriod())
                                .append(" 当季第几月 ").append(dto.getMonthSeqInCurrQuarter()).append(" 已存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账期 " + dto.getAccountingPeriod() + " 当季第几月 " + dto.getMonthSeqInCurrQuarter() + " 导入失败：";
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
