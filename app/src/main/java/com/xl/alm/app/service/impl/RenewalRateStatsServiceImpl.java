package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.RenewalRateStatsDTO;
import com.xl.alm.app.entity.RenewalRateStatsEntity;
import com.xl.alm.app.mapper.RenewalRateStatsMapper;
import com.xl.alm.app.query.RenewalRateStatsQuery;
import com.xl.alm.app.service.RenewalRateStatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 保单续保率统计表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class RenewalRateStatsServiceImpl implements RenewalRateStatsService {

    @Autowired
    private RenewalRateStatsMapper renewalRateStatsMapper;

    /**
     * 查询保单续保率统计列表
     *
     * @param query 保单续保率统计查询条件
     * @return 保单续保率统计列表
     */
    @Override
    public List<RenewalRateStatsDTO> selectRenewalRateStatsDtoList(RenewalRateStatsQuery query) {
        List<RenewalRateStatsEntity> entityList = renewalRateStatsMapper.selectRenewalRateStatsEntityList(query);
        return convertToDTOList(entityList);
    }

    /**
     * 查询保单续保率统计详细
     *
     * @param id 保单续保率统计ID
     * @return 保单续保率统计
     */
    @Override
    public RenewalRateStatsDTO selectRenewalRateStatsDtoById(Long id) {
        RenewalRateStatsEntity entity = renewalRateStatsMapper.selectRenewalRateStatsEntityById(id);
        return convertToDTO(entity);
    }

    /**
     * 新增保单续保率统计
     *
     * @param dto 保单续保率统计
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRenewalRateStatsDto(RenewalRateStatsDTO dto) {
        RenewalRateStatsEntity entity = convertToEntity(dto);
        return renewalRateStatsMapper.insertRenewalRateStatsEntity(entity);
    }

    /**
     * 批量新增保单续保率统计
     *
     * @param dtoList 保单续保率统计列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertRenewalRateStatsDto(List<RenewalRateStatsDTO> dtoList) {
        List<RenewalRateStatsEntity> entityList = convertToEntityList(dtoList);
        return renewalRateStatsMapper.batchInsertRenewalRateStatsEntity(entityList);
    }

    /**
     * 修改保单续保率统计
     *
     * @param dto 保单续保率统计
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRenewalRateStatsDto(RenewalRateStatsDTO dto) {
        RenewalRateStatsEntity entity = convertToEntity(dto);
        return renewalRateStatsMapper.updateRenewalRateStatsEntity(entity);
    }

    /**
     * 删除保单续保率统计信息
     *
     * @param id 保单续保率统计ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRenewalRateStatsDtoById(Long id) {
        return renewalRateStatsMapper.deleteRenewalRateStatsEntityById(id);
    }

    /**
     * 批量删除保单续保率统计
     *
     * @param ids 需要删除的保单续保率统计ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRenewalRateStatsDtoByIds(Long[] ids) {
        return renewalRateStatsMapper.deleteRenewalRateStatsEntityByIds(ids);
    }

    /**
     * 根据账期删除保单续保率统计
     *
     * @param accountingPeriod 账期
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRenewalRateStatsDtoByPeriod(String accountingPeriod) {
        return renewalRateStatsMapper.deleteRenewalRateStatsEntityByPeriod(accountingPeriod);
    }

    /**
     * 导入保单续保率统计数据
     *
     * @param dtoList       保单续保率统计数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username      操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importRenewalRateStatsDto(List<RenewalRateStatsDTO> dtoList, Boolean updateSupport, String username) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入保单续保率统计数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (RenewalRateStatsDTO dto : dtoList) {
            try {
                // 设置创建者和更新者
                dto.setCreateBy(username);
                dto.setUpdateBy(username);

                // 查询是否存在相同账期、当季第几月、渠道类型编码、上年同比月份的数据
                RenewalRateStatsQuery query = new RenewalRateStatsQuery();
                query.setAccountingPeriod(dto.getAccountingPeriod());
                query.setMonthSeqInCurrQuarter(dto.getMonthSeqInCurrQuarter());
                query.setChannelTypeCode(dto.getChannelTypeCode());
                query.setMonthOfLastYear(dto.getMonthOfLastYear());
                List<RenewalRateStatsDTO> existList = selectRenewalRateStatsDtoList(query);

                if (existList == null || existList.isEmpty()) {
                    insertRenewalRateStatsDto(dto);
                    successNum++;
                } else if (updateSupport) {
                    dto.setId(existList.get(0).getId());
                    updateRenewalRateStatsDto(dto);
                    successNum++;
                } else {
                    failureNum++;
                    if (failureNum <= 10) {
                        failureMsg.append("<br/>").append(failureNum).append("、账期 ").append(dto.getAccountingPeriod())
                                .append(" 当季第几月 ").append(dto.getMonthSeqInCurrQuarter())
                                .append(" 渠道类型编码 ").append(dto.getChannelTypeCode())
                                .append(" 上年同比月份 ").append(dto.getMonthOfLastYear()).append(" 已存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账期 " + dto.getAccountingPeriod() + " 导入失败：";
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

    /**
     * 将实体对象转换为DTO对象
     *
     * @param entity 实体对象
     * @return DTO对象
     */
    private RenewalRateStatsDTO convertToDTO(RenewalRateStatsEntity entity) {
        if (entity == null) {
            return null;
        }
        RenewalRateStatsDTO dto = new RenewalRateStatsDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    /**
     * 将实体对象列表转换为DTO对象列表
     *
     * @param entityList 实体对象列表
     * @return DTO对象列表
     */
    private List<RenewalRateStatsDTO> convertToDTOList(List<RenewalRateStatsEntity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return entityList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 将DTO对象转换为实体对象
     *
     * @param dto DTO对象
     * @return 实体对象
     */
    private RenewalRateStatsEntity convertToEntity(RenewalRateStatsDTO dto) {
        if (dto == null) {
            return null;
        }
        RenewalRateStatsEntity entity = new RenewalRateStatsEntity();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    /**
     * 将DTO对象列表转换为实体对象列表
     *
     * @param dtoList DTO对象列表
     * @return 实体对象列表
     */
    private List<RenewalRateStatsEntity> convertToEntityList(List<RenewalRateStatsDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>();
        }
        return dtoList.stream().map(this::convertToEntity).collect(Collectors.toList());
    }

    /**
     * 计算保单续保率统计
     *
     * @param accountingPeriod 账期
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int calcRenewalRateStats(String accountingPeriod) {
        // 先删除该账期的数据
        renewalRateStatsMapper.deleteRenewalRateStatsEntityByPeriod(accountingPeriod);
        // 调用Mapper的calcRenewalRateStats方法进行统计计算
        return renewalRateStatsMapper.calcRenewalRateStats(accountingPeriod);
    }
}
