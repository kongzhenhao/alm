package com.xl.alm.app.service;

import com.xl.alm.app.dto.MinCapitalByAccountDTO;
import com.xl.alm.app.query.MinCapitalByAccountQuery;

import java.util.List;

/**
 * 市场及信用最低资本表Service接口
 *
 * @author alm
 * @date 2024-01-01
 */
public interface MinCapitalByAccountService {

    /**
     * 查询市场及信用最低资本表列表
     *
     * @param query 查询条件
     * @return 市场及信用最低资本表集合
     */
    List<MinCapitalByAccountDTO> selectMinCapitalByAccountDtoList(MinCapitalByAccountQuery query);

    /**
     * 查询市场及信用最低资本表详情
     *
     * @param id 市场及信用最低资本表主键
     * @return 市场及信用最低资本表
     */
    MinCapitalByAccountDTO selectMinCapitalByAccountDtoById(Long id);

    /**
     * 根据账期、项目编码和账户编码查询市场及信用最低资本表详情
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @return 市场及信用最低资本表
     */
    MinCapitalByAccountDTO selectMinCapitalByAccountDtoByUniqueKey(String accountingPeriod, String itemCode, String accountCode);

    /**
     * 根据账期、项目编码和账户编码查询有效的市场及信用最低资本表详情（用于检查）
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @return 市场及信用最低资本表
     */
    MinCapitalByAccountDTO selectValidMinCapitalByAccountDtoByUniqueKey(String accountingPeriod, String itemCode, String accountCode);

    /**
     * 新增市场及信用最低资本表
     *
     * @param dto 市场及信用最低资本表
     * @return 结果
     */
    int insertMinCapitalByAccountDto(MinCapitalByAccountDTO dto);

    /**
     * 批量新增市场及信用最低资本表
     *
     * @param dtoList 市场及信用最低资本表列表
     * @return 结果
     */
    int batchInsertMinCapitalByAccountDto(List<MinCapitalByAccountDTO> dtoList);

    /**
     * 修改市场及信用最低资本表
     *
     * @param dto 市场及信用最低资本表
     * @return 结果
     */
    int updateMinCapitalByAccountDto(MinCapitalByAccountDTO dto);

    /**
     * 批量删除市场及信用最低资本表
     *
     * @param ids 需要删除的市场及信用最低资本表主键集合
     * @return 结果
     */
    int deleteMinCapitalByAccountDtoByIds(Long[] ids);

    /**
     * 删除市场及信用最低资本表信息
     *
     * @param id 市场及信用最低资本表主键
     * @return 结果
     */
    int deleteMinCapitalByAccountDtoById(Long id);
}
