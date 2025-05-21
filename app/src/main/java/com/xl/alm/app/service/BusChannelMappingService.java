package com.xl.alm.app.service;

import com.xl.alm.app.dto.BusChannelMappingDTO;
import com.xl.alm.app.query.BusChannelMappingQuery;

import java.util.List;

/**
 * 渠道码映射配置表 Service 接口
 *
 * @author AI Assistant
 */
public interface BusChannelMappingService {

    /**
     * 查询渠道码映射配置列表
     *
     * @param query 渠道码映射配置查询条件
     * @return 渠道码映射配置列表
     */
    List<BusChannelMappingDTO> selectBusChannelMappingDtoList(BusChannelMappingQuery query);

    /**
     * 用id查询渠道码映射配置
     *
     * @param id id
     * @return 渠道码映射配置
     */
    BusChannelMappingDTO selectBusChannelMappingDtoById(Long id);

    /**
     * 根据业务类型编码和渠道类型编码查询渠道码映射配置
     *
     * @param busTypeCode 业务类型编码
     * @param channelTypeCode 渠道类型编码
     * @return 渠道码映射配置
     */
    BusChannelMappingDTO selectBusChannelMappingDtoByCondition(
            String busTypeCode,
            String channelTypeCode);

    /**
     * 新增渠道码映射配置
     *
     * @param dto 渠道码映射配置
     * @return 结果
     */
    int insertBusChannelMappingDto(BusChannelMappingDTO dto);

    /**
     * 批量新增渠道码映射配置
     *
     * @param dtoList 渠道码映射配置列表
     * @return 结果
     */
    int batchInsertBusChannelMappingDto(List<BusChannelMappingDTO> dtoList);

    /**
     * 修改渠道码映射配置
     *
     * @param dto 渠道码映射配置
     * @return 结果
     */
    int updateBusChannelMappingDto(BusChannelMappingDTO dto);

    /**
     * 批量删除渠道码映射配置
     *
     * @param ids 需要删除的渠道码映射配置主键集合
     * @return 结果
     */
    int deleteBusChannelMappingDtoByIds(Long[] ids);

    /**
     * 删除渠道码映射配置信息
     *
     * @param id 渠道码映射配置主键
     * @return 结果
     */
    int deleteBusChannelMappingDtoById(Long id);

    /**
     * 导入渠道码映射配置
     *
     * @param dtoList 渠道码映射配置数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    String importBusChannelMappingDto(List<BusChannelMappingDTO> dtoList, Boolean updateSupport, String username);
}
