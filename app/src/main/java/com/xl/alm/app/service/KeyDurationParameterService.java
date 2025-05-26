package com.xl.alm.app.service;

import com.xl.alm.app.dto.KeyDurationParameterDTO;
import com.xl.alm.app.query.KeyDurationParameterQuery;

import java.util.List;

/**
 * 关键久期参数表 Service 接口
 *
 * @author AI Assistant
 */
public interface KeyDurationParameterService {

    /**
     * 查询关键久期参数列表
     *
     * @param query 关键久期参数查询条件
     * @return 关键久期参数列表
     */
    List<KeyDurationParameterDTO> selectKeyDurationParameterDtoList(KeyDurationParameterQuery query);

    /**
     * 根据ID查询关键久期参数
     *
     * @param id ID
     * @return 关键久期参数
     */
    KeyDurationParameterDTO selectKeyDurationParameterDtoById(Long id);

    /**
     * 根据ID查询关键久期参数（包含参数值集）
     *
     * @param id ID
     * @return 关键久期参数
     */
    KeyDurationParameterDTO selectKeyDurationParameterDtoWithValSetById(Long id);

    /**
     * 根据账期和关键期限点查询关键久期参数
     *
     * @param accountPeriod 账期
     * @param keyDuration 关键期限点
     * @return 关键久期参数
     */
    KeyDurationParameterDTO selectKeyDurationParameterDtoByCondition(String accountPeriod, String keyDuration);

    /**
     * 新增关键久期参数
     *
     * @param dto 关键久期参数
     * @return 结果
     */
    int insertKeyDurationParameterDto(KeyDurationParameterDTO dto);

    /**
     * 批量新增关键久期参数
     *
     * @param dtoList 关键久期参数列表
     * @return 结果
     */
    int batchInsertKeyDurationParameterDto(List<KeyDurationParameterDTO> dtoList);

    /**
     * 修改关键久期参数
     *
     * @param dto 关键久期参数
     * @return 结果
     */
    int updateKeyDurationParameterDto(KeyDurationParameterDTO dto);

    /**
     * 删除关键久期参数
     *
     * @param id ID
     * @return 结果
     */
    int deleteKeyDurationParameterDtoById(Long id);

    /**
     * 批量删除关键久期参数
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteKeyDurationParameterDtoByIds(Long[] ids);

    /**
     * 根据账期删除关键久期参数
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteKeyDurationParameterDtoByAccountPeriod(String accountPeriod);

    /**
     * 导入关键久期参数数据
     *
     * @param dtoList 关键久期参数数据列表
     * @param updateSupport 是否支持更新，如果已存在，则进行更新
     * @param operName 操作用户
     * @return 结果
     */
    String importKeyDurationParameterDto(List<KeyDurationParameterDTO> dtoList, boolean updateSupport, String operName);
}
