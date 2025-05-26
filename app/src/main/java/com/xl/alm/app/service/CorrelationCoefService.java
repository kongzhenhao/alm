package com.xl.alm.app.service;

import com.xl.alm.app.dto.CorrelationCoefDTO;
import com.xl.alm.app.query.CorrelationCoefQuery;

import java.util.List;

/**
 * 相关系数表 Service接口
 *
 * @author AI Assistant
 */
public interface CorrelationCoefService {
    /**
     * 查询相关系数表列表
     *
     * @param query 查询条件
     * @return 相关系数表集合
     */
    List<CorrelationCoefDTO> selectCorrelationCoefDtoList(CorrelationCoefQuery query);

    /**
     * 查询相关系数表详情
     *
     * @param id 主键ID
     * @return 相关系数表
     */
    CorrelationCoefDTO selectCorrelationCoefDtoById(Long id);

    /**
     * 根据账期和项目编码查询相关系数表详情
     *
     * @param accountingPeriod 账期
     * @param itemCodeX 第一个风险项目编码
     * @param itemCodeY 第二个风险项目编码
     * @return 相关系数表
     */
    CorrelationCoefDTO selectCorrelationCoefDtoByUniqueKey(String accountingPeriod, String itemCodeX, String itemCodeY);

    /**
     * 根据账期和项目编码查询有效的相关系数表详情（用于导入检查）
     *
     * @param accountingPeriod 账期
     * @param itemCodeX 第一个风险项目编码
     * @param itemCodeY 第二个风险项目编码
     * @return 相关系数表
     */
    CorrelationCoefDTO selectValidCorrelationCoefDtoByUniqueKey(String accountingPeriod, String itemCodeX, String itemCodeY);

    /**
     * 新增相关系数表
     *
     * @param dto 相关系数表
     * @return 结果
     */
    int insertCorrelationCoefDto(CorrelationCoefDTO dto);

    /**
     * 批量新增相关系数表
     *
     * @param dtoList 相关系数表列表
     * @return 结果
     */
    int batchInsertCorrelationCoefDto(List<CorrelationCoefDTO> dtoList);

    /**
     * 修改相关系数表
     *
     * @param dto 相关系数表
     * @return 结果
     */
    int updateCorrelationCoefDto(CorrelationCoefDTO dto);

    /**
     * 删除相关系数表
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteCorrelationCoefDtoById(Long id);

    /**
     * 批量删除相关系数表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteCorrelationCoefDtoByIds(Long[] ids);

    /**
     * 导入相关系数表
     *
     * @param dtoList 相关系数表数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    String importCorrelationCoefDto(List<CorrelationCoefDTO> dtoList, Boolean updateSupport, String username);
}
