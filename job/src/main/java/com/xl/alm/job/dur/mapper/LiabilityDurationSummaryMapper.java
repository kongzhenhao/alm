package com.xl.alm.job.dur.mapper;

import com.xl.alm.job.dur.entity.LiabilityDurationSummaryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 负债久期汇总表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface LiabilityDurationSummaryMapper {

    /**
     * 批量插入负债久期汇总数据
     *
     * @param liabilityDurationSummaryEntityList 负债久期汇总列表
     * @return 影响行数
     */
    int batchInsertLiabilityDurationSummaryEntity(List<LiabilityDurationSummaryEntity> liabilityDurationSummaryEntityList);

    /**
     * 删除指定账期的负债久期汇总数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    int deleteLiabilityDurationSummaryEntityByPeriod(@Param("accountPeriod") String accountPeriod);

    /**
     * 根据条件查询负债久期汇总
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @param isShortTerm 是否为中短期险种
     * @return 负债久期汇总
     */
    LiabilityDurationSummaryEntity selectLiabilityDurationSummaryEntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("cashFlowType") String cashFlowType,
            @Param("bpType") String bpType,
            @Param("durationType") String durationType,
            @Param("designType") String designType,
            @Param("isShortTerm") String isShortTerm);

    /**
     * 根据条件查询负债久期汇总列表
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @param isShortTerm 是否为中短期险种
     * @return 负债久期汇总列表
     */
    List<LiabilityDurationSummaryEntity> selectLiabilityDurationSummaryEntityListByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("cashFlowType") String cashFlowType,
            @Param("bpType") String bpType,
            @Param("durationType") String durationType,
            @Param("designType") String designType,
            @Param("isShortTerm") String isShortTerm);
}
