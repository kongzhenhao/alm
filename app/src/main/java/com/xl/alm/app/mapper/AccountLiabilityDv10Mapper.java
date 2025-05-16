package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.AccountLiabilityDv10Entity;
import com.xl.alm.app.query.AccountLiabilityDv10Query;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分账户负债基点价值DV10表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface AccountLiabilityDv10Mapper {

    /**
     * 查询分账户负债基点价值DV10列表
     *
     * @param query 分账户负债基点价值DV10查询条件
     * @return 分账户负债基点价值DV10列表
     */
    List<AccountLiabilityDv10Entity> selectAccountLiabilityDv10EntityList(AccountLiabilityDv10Query query);

    /**
     * 根据ID查询分账户负债基点价值DV10
     *
     * @param id ID
     * @return 分账户负债基点价值DV10
     */
    AccountLiabilityDv10Entity selectAccountLiabilityDv10EntityById(Long id);

    /**
     * 根据条件查询分账户负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param designType 设计类型
     * @param valueType 现值类型
     * @return 分账户负债基点价值DV10
     */
    AccountLiabilityDv10Entity selectAccountLiabilityDv10EntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("cashFlowType") String cashFlowType,
            @Param("designType") String designType,
            @Param("valueType") String valueType);

    /**
     * 新增分账户负债基点价值DV10
     *
     * @param entity 分账户负债基点价值DV10
     * @return 结果
     */
    int insertAccountLiabilityDv10Entity(AccountLiabilityDv10Entity entity);

    /**
     * 批量新增分账户负债基点价值DV10
     *
     * @param entityList 分账户负债基点价值DV10列表
     * @return 结果
     */
    int batchInsertAccountLiabilityDv10Entity(List<AccountLiabilityDv10Entity> entityList);

    /**
     * 修改分账户负债基点价值DV10
     *
     * @param entity 分账户负债基点价值DV10
     * @return 结果
     */
    int updateAccountLiabilityDv10Entity(AccountLiabilityDv10Entity entity);

    /**
     * 删除分账户负债基点价值DV10
     *
     * @param id 分账户负债基点价值DV10ID
     * @return 结果
     */
    int deleteAccountLiabilityDv10EntityById(Long id);

    /**
     * 批量删除分账户负债基点价值DV10
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteAccountLiabilityDv10EntityByIds(Long[] ids);

    /**
     * 删除指定账期的分账户负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteAccountLiabilityDv10EntityByPeriod(String accountPeriod);
}
