package com.xl.alm.app.service;

import com.xl.alm.app.dto.AccountLiabilityDv10DTO;
import com.xl.alm.app.query.AccountLiabilityDv10Query;

import java.util.List;

/**
 * 分账户负债基点价值DV10表 Service 接口
 *
 * @author AI Assistant
 */
public interface AccountLiabilityDv10Service {

    /**
     * 查询分账户负债基点价值DV10列表
     *
     * @param query 分账户负债基点价值DV10查询条件
     * @return 分账户负债基点价值DV10列表
     */
    List<AccountLiabilityDv10DTO> selectAccountLiabilityDv10DtoList(AccountLiabilityDv10Query query);

    /**
     * 根据ID查询分账户负债基点价值DV10
     *
     * @param id ID
     * @return 分账户负债基点价值DV10
     */
    AccountLiabilityDv10DTO selectAccountLiabilityDv10DtoById(Long id);

    /**
     * 根据条件查询分账户负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param designType 设计类型
     * @param valueType 现值类型
     * @return 分账户负债基点价值DV10
     */
    AccountLiabilityDv10DTO selectAccountLiabilityDv10DtoByCondition(
            String accountPeriod,
            String cashFlowType,
            String designType,
            String valueType);

    /**
     * 新增分账户负债基点价值DV10
     *
     * @param dto 分账户负债基点价值DV10
     * @return 结果
     */
    int insertAccountLiabilityDv10Dto(AccountLiabilityDv10DTO dto);

    /**
     * 批量新增分账户负债基点价值DV10
     *
     * @param dtoList 分账户负债基点价值DV10列表
     * @return 结果
     */
    int batchInsertAccountLiabilityDv10Dto(List<AccountLiabilityDv10DTO> dtoList);

    /**
     * 修改分账户负债基点价值DV10
     *
     * @param dto 分账户负债基点价值DV10
     * @return 结果
     */
    int updateAccountLiabilityDv10Dto(AccountLiabilityDv10DTO dto);

    /**
     * 删除分账户负债基点价值DV10
     *
     * @param id 分账户负债基点价值DV10ID
     * @return 结果
     */
    int deleteAccountLiabilityDv10DtoById(Long id);

    /**
     * 批量删除分账户负债基点价值DV10
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteAccountLiabilityDv10DtoByIds(Long[] ids);

    /**
     * 删除指定账期的分账户负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteAccountLiabilityDv10DtoByPeriod(String accountPeriod);

    /**
     * 导入分账户负债基点价值DV10
     *
     * @param dtoList 分账户负债基点价值DV10数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    String importAccountLiabilityDv10Dto(List<AccountLiabilityDv10DTO> dtoList, Boolean updateSupport, String username);
}
