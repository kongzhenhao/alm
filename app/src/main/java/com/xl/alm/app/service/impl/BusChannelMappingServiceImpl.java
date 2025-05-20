package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.BusChannelMappingDTO;
import com.xl.alm.app.entity.BusChannelMappingEntity;
import com.xl.alm.app.mapper.BusChannelMappingMapper;
import com.xl.alm.app.query.BusChannelMappingQuery;
import com.xl.alm.app.service.BusChannelMappingService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 渠道码映射配置表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class BusChannelMappingServiceImpl implements BusChannelMappingService {

    @Autowired
    private BusChannelMappingMapper busChannelMappingMapper;

    /**
     * 查询渠道码映射配置列表
     *
     * @param query 渠道码映射配置查询条件
     * @return 渠道码映射配置列表
     */
    @Override
    public List<BusChannelMappingDTO> selectBusChannelMappingDtoList(BusChannelMappingQuery query) {
        List<BusChannelMappingEntity> entityList = busChannelMappingMapper.selectBusChannelMappingEntityList(query);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, BusChannelMappingDTO.class);
    }

    /**
     * 用id查询渠道码映射配置
     *
     * @param id id
     * @return 渠道码映射配置
     */
    @Override
    public BusChannelMappingDTO selectBusChannelMappingDtoById(Long id) {
        BusChannelMappingEntity entity = busChannelMappingMapper.selectBusChannelMappingEntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, BusChannelMappingDTO.class);
    }

    /**
     * 根据业务类型编码和渠道类型编码查询渠道码映射配置
     *
     * @param busTypeCode 业务类型编码
     * @param channelTypeCode 渠道类型编码
     * @return 渠道码映射配置
     */
    @Override
    public BusChannelMappingDTO selectBusChannelMappingDtoByCondition(
            String busTypeCode,
            String channelTypeCode) {
        BusChannelMappingEntity entity = busChannelMappingMapper.selectBusChannelMappingEntityByCondition(
                busTypeCode,
                channelTypeCode);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, BusChannelMappingDTO.class);
    }

    /**
     * 新增渠道码映射配置
     *
     * @param dto 渠道码映射配置
     * @return 结果
     */
    @Override
    public int insertBusChannelMappingDto(BusChannelMappingDTO dto) {
        BusChannelMappingEntity entity = EntityDtoConvertUtil.convertToEntity(dto, BusChannelMappingEntity.class);
        return busChannelMappingMapper.insertBusChannelMappingEntity(entity);
    }

    /**
     * 批量新增渠道码映射配置
     *
     * @param dtoList 渠道码映射配置列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertBusChannelMappingDto(List<BusChannelMappingDTO> dtoList) {
        List<BusChannelMappingEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, BusChannelMappingEntity.class);
        return busChannelMappingMapper.batchInsertBusChannelMappingEntity(entityList);
    }

    /**
     * 修改渠道码映射配置
     *
     * @param dto 渠道码映射配置
     * @return 结果
     */
    @Override
    public int updateBusChannelMappingDto(BusChannelMappingDTO dto) {
        BusChannelMappingEntity entity = EntityDtoConvertUtil.convertToEntity(dto, BusChannelMappingEntity.class);
        return busChannelMappingMapper.updateBusChannelMappingEntity(entity);
    }

    /**
     * 批量删除渠道码映射配置
     *
     * @param ids 需要删除的渠道码映射配置主键集合
     * @return 结果
     */
    @Override
    public int deleteBusChannelMappingDtoByIds(Long[] ids) {
        return busChannelMappingMapper.deleteBusChannelMappingEntityByIds(ids);
    }

    /**
     * 删除渠道码映射配置信息
     *
     * @param id 渠道码映射配置主键
     * @return 结果
     */
    @Override
    public int deleteBusChannelMappingDtoById(Long id) {
        return busChannelMappingMapper.deleteBusChannelMappingEntityById(id);
    }

    /**
     * 导入渠道码映射配置
     *
     * @param dtoList 渠道码映射配置数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importBusChannelMappingDto(List<BusChannelMappingDTO> dtoList, Boolean updateSupport, String username) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入渠道码映射配置数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (BusChannelMappingDTO dto : dtoList) {
            try {
                // 验证是否存在这个渠道码映射配置
                BusChannelMappingDTO existDto = selectBusChannelMappingDtoByCondition(
                        dto.getBusTypeCode(),
                        dto.getChannelTypeCode());

                if (StringUtils.isNull(existDto)) {
                    dto.setCreateBy(username);
                    dto.setUpdateBy(username);
                    insertBusChannelMappingDto(dto);
                    successNum++;
                } else if (updateSupport) {
                    dto.setId(existDto.getId());
                    dto.setUpdateBy(username);
                    updateBusChannelMappingDto(dto);
                    successNum++;
                } else {
                    failureNum++;
                    if (failureNum <= 10) {
                        failureMsg.append("<br/>").append(failureNum).append("、业务类型编码 ").append(dto.getBusTypeCode())
                                .append(" 渠道类型编码 ").append(dto.getChannelTypeCode()).append(" 已存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、业务类型编码 " + dto.getBusTypeCode() + " 渠道类型编码 " + dto.getChannelTypeCode() + " 导入失败：";
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
