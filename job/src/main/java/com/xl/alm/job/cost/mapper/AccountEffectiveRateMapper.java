package com.xl.alm.job.cost.mapper;

import com.xl.alm.job.cost.entity.AccountEffectiveRateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分账户有效成本率表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface AccountEffectiveRateMapper {

    /**
     * 批量插入分账户有效成本率数据
     *
     * @param accountEffectiveRateList 分账户有效成本率列表
     * @return 影响行数
     */
    int batchInsertAccountEffectiveRate(List<AccountEffectiveRateEntity> accountEffectiveRateList);

    /**
     * 删除指定账期的分账户有效成本率数据
     *
     * @param accountingPeriod 账期
     * @return 影响行数
     */
    int deleteAccountEffectiveRateByPeriod(@Param("accountingPeriod") String accountingPeriod);

    /**
     * 根据账期查询分账户有效成本率列表
     *
     * @param accountingPeriod 账期
     * @return 分账户有效成本率列表
     */
    List<AccountEffectiveRateEntity> selectAccountEffectiveRateListByPeriod(@Param("accountingPeriod") String accountingPeriod);
}
