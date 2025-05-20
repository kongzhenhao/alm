package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.BusChannelMappingEntity;
import com.xl.alm.app.query.BusChannelMappingQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 渠道码映射配置表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface BusChannelMappingMapper {

    /**
     * 查询渠道码映射配置列表
     *
     * @param query 渠道码映射配置查询条件
     * @return 渠道码映射配置列表
     */
    List<BusChannelMappingEntity> selectBusChannelMappingEntityList(BusChannelMappingQuery query);

    /**
     * 用id查询渠道码映射配置
     *
     * @param id id
     * @return 渠道码映射配置
     */
    BusChannelMappingEntity selectBusChannelMappingEntityById(Long id);

    /**
     * 根据业务类型编码和渠道类型编码查询渠道码映射配置
     *
     * @param busTypeCode 业务类型编码
     * @param channelTypeCode 渠道类型编码
     * @return 渠道码映射配置
     */
    BusChannelMappingEntity selectBusChannelMappingEntityByCondition(
            @Param("busTypeCode") String busTypeCode,
            @Param("channelTypeCode") String channelTypeCode);

    /**
     * 新增渠道码映射配置
     *
     * @param entity 渠道码映射配置
     * @return 结果
     */
    int insertBusChannelMappingEntity(BusChannelMappingEntity entity);

    /**
     * 批量新增渠道码映射配置
     *
     * @param entityList 渠道码映射配置列表
     * @return 结果
     */
    int batchInsertBusChannelMappingEntity(List<BusChannelMappingEntity> entityList);

    /**
     * 修改渠道码映射配置
     *
     * @param entity 渠道码映射配置
     * @return 结果
     */
    int updateBusChannelMappingEntity(BusChannelMappingEntity entity);

    /**
     * 删除渠道码映射配置
     *
     * @param id 渠道码映射配置主键
     * @return 结果
     */
    int deleteBusChannelMappingEntityById(Long id);

    /**
     * 批量删除渠道码映射配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBusChannelMappingEntityByIds(Long[] ids);
}
