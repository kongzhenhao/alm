package com.xl.alm.job.dur.mapper;

import com.xl.alm.job.dur.entity.AccountLiabilityDv10Entity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分账户负债基点价值DV10表数据访问层
 *
 * @author AI
 */
@Mapper
public interface AccountLiabilityDv10Mapper {
    
    /**
     * 根据账期删除数据
     *
     * @param accountPeriod 账期
     * @return 删除记录数
     */
    int deleteByAccountPeriod(@Param("accountPeriod") String accountPeriod);
    
    /**
     * 批量插入数据
     *
     * @param entities 实体列表
     * @return 插入记录数
     */
    int batchInsert(@Param("entities") List<AccountLiabilityDv10Entity> entities);
}
