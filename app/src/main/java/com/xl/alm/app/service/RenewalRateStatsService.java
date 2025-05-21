package com.xl.alm.app.service;

import com.xl.alm.app.dto.RenewalRateStatsDTO;
import com.xl.alm.app.query.RenewalRateStatsQuery;

import java.util.List;

/**
 * 保单续保率统计表 Service 接口
 *
 * @author AI Assistant
 */
public interface RenewalRateStatsService {

    /**
     * 查询保单续保率统计列表
     *
     * @param query 保单续保率统计查询条件
     * @return 保单续保率统计列表
     */
    List<RenewalRateStatsDTO> selectRenewalRateStatsDtoList(RenewalRateStatsQuery query);

    /**
     * 查询保单续保率统计详细
     *
     * @param id 保单续保率统计ID
     * @return 保单续保率统计
     */
    RenewalRateStatsDTO selectRenewalRateStatsDtoById(Long id);

    /**
     * 新增保单续保率统计
     *
     * @param dto 保单续保率统计
     * @return 结果
     */
    int insertRenewalRateStatsDto(RenewalRateStatsDTO dto);

    /**
     * 批量新增保单续保率统计
     *
     * @param dtoList 保单续保率统计列表
     * @return 结果
     */
    int batchInsertRenewalRateStatsDto(List<RenewalRateStatsDTO> dtoList);

    /**
     * 修改保单续保率统计
     *
     * @param dto 保单续保率统计
     * @return 结果
     */
    int updateRenewalRateStatsDto(RenewalRateStatsDTO dto);

    /**
     * 删除保单续保率统计信息
     *
     * @param id 保单续保率统计ID
     * @return 结果
     */
    int deleteRenewalRateStatsDtoById(Long id);

    /**
     * 批量删除保单续保率统计
     *
     * @param ids 需要删除的保单续保率统计ID
     * @return 结果
     */
    int deleteRenewalRateStatsDtoByIds(Long[] ids);

    /**
     * 根据账期删除保单续保率统计
     *
     * @param accountingPeriod 账期
     * @return 结果
     */
    int deleteRenewalRateStatsDtoByPeriod(String accountingPeriod);

    /**
     * 导入保单续保率统计数据
     *
     * @param dtoList       保单续保率统计数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username      操作用户
     * @return 结果
     */
    String importRenewalRateStatsDto(List<RenewalRateStatsDTO> dtoList, Boolean updateSupport, String username);

    /**
     * 计算保单续保率统计
     *
     * @param accountingPeriod 账期
     * @return 结果
     */
    int calcRenewalRateStats(String accountingPeriod);
}
