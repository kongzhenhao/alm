package com.xl.alm.app.service;

import com.xl.alm.app.dto.MinCapitalSummaryDTO;
import com.xl.alm.app.query.MinCapitalSummaryQuery;

import java.util.List;

/**
 * 最低资本明细汇总表 Service接口
 *
 * @author AI Assistant
 */
public interface MinCapitalSummaryService {
    /**
     * 查询最低资本明细汇总表列表
     *
     * @param query 查询条件
     * @return 最低资本明细汇总表集合
     */
    List<MinCapitalSummaryDTO> selectMinCapitalSummaryDtoList(MinCapitalSummaryQuery query);

    /**
     * 查询最低资本明细汇总表详情
     *
     * @param id 主键ID
     * @return 最低资本明细汇总表
     */
    MinCapitalSummaryDTO selectMinCapitalSummaryDtoById(Long id);

    /**
     * 根据账期、项目编码和账户编码查询最低资本明细汇总表详情
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @return 最低资本明细汇总表
     */
    MinCapitalSummaryDTO selectMinCapitalSummaryDtoByUniqueKey(String accountingPeriod, String itemCode, String accountCode);

    /**
     * 根据账期、项目编码和账户编码查询有效的最低资本明细汇总表详情（用于检查）
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @return 最低资本明细汇总表
     */
    MinCapitalSummaryDTO selectValidMinCapitalSummaryDtoByUniqueKey(String accountingPeriod, String itemCode, String accountCode);

    /**
     * 新增最低资本明细汇总表
     *
     * @param dto 最低资本明细汇总表
     * @return 结果
     */
    int insertMinCapitalSummaryDto(MinCapitalSummaryDTO dto);

    /**
     * 批量新增最低资本明细汇总表
     *
     * @param dtoList 最低资本明细汇总表列表
     * @return 结果
     */
    int batchInsertMinCapitalSummaryDto(List<MinCapitalSummaryDTO> dtoList);

    /**
     * 修改最低资本明细汇总表
     *
     * @param dto 最低资本明细汇总表
     * @return 结果
     */
    int updateMinCapitalSummaryDto(MinCapitalSummaryDTO dto);

    /**
     * 删除最低资本明细汇总表
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteMinCapitalSummaryDtoById(Long id);

    /**
     * 批量删除最低资本明细汇总表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteMinCapitalSummaryDtoByIds(Long[] ids);
}
