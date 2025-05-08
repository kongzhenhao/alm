package com.xl.alm.job.dur.mapper;

import com.xl.alm.job.dur.entity.SubAccountLiabilityDurationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分账户负债久期汇总表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface SubAccountLiabilityDurationMapper {

    /**
     * 批量插入分账户负债久期汇总数据
     *
     * @param subAccountLiabilityDurationEntityList 分账户负债久期汇总列表
     * @return 影响行数
     */
    int batchInsertSubAccountLiabilityDurationEntity(List<SubAccountLiabilityDurationEntity> subAccountLiabilityDurationEntityList);

    /**
     * 删除指定账期的分账户负债久期汇总数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    int deleteSubAccountLiabilityDurationEntityByPeriod(@Param("accountPeriod") String accountPeriod);
    
    /**
     * 根据条件查询分账户负债久期汇总
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @return 分账户负债久期汇总
     */
    SubAccountLiabilityDurationEntity selectSubAccountLiabilityDurationEntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("cashFlowType") String cashFlowType,
            @Param("bpType") String bpType,
            @Param("durationType") String durationType,
            @Param("designType") String designType);
}
