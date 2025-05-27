package com.xl.alm.job.cost.mapper;

import com.xl.alm.job.cost.entity.AccountSummaryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分账户汇总表(TB0009) Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface AccountSummaryMapper {

    /**
     * 批量插入分账户汇总数据
     *
     * @param accountSummaryEntityList 分账户汇总列表
     * @return 影响行数
     */
    int batchInsertAccountSummary(List<AccountSummaryEntity> accountSummaryEntityList);

    /**
     * 删除指定账期的分账户汇总数据
     *
     * @param accountingPeriod 账期
     * @return 影响行数
     */
    int deleteAccountSummaryByPeriod(@Param("accountingPeriod") String accountingPeriod);

    /**
     * 根据账期查询分账户汇总列表
     *
     * @param accountingPeriod 账期
     * @return 分账户汇总列表
     */
    List<AccountSummaryEntity> selectAccountSummaryListByPeriod(@Param("accountingPeriod") String accountingPeriod);
}
