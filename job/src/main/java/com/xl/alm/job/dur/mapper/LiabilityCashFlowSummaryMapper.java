package com.xl.alm.job.dur.mapper;

import com.xl.alm.job.dur.entity.LiabilityCashFlowSummaryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 负债现金流汇总表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface LiabilityCashFlowSummaryMapper {

    /**
     * 批量插入负债现金流汇总数据
     *
     * @param liabilityCashFlowSummaryEntityList 负债现金流汇总列表
     * @return 影响行数
     */
    int batchInsertLiabilityCashFlowSummaryEntity(List<LiabilityCashFlowSummaryEntity> liabilityCashFlowSummaryEntityList);

    /**
     * 删除指定账期的负债现金流汇总数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    int deleteLiabilityCashFlowSummaryEntityByPeriod(@Param("accountPeriod") String accountPeriod);

    /**
     * 根据账期查询负债现金流汇总列表
     *
     * @param accountPeriod 账期
     * @return 负债现金流汇总列表
     */
    List<LiabilityCashFlowSummaryEntity> selectLiabilityCashFlowSummaryEntityListByPeriod(@Param("accountPeriod") String accountPeriod);

    /**
     * 根据条件查询负债现金流汇总列表
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @param isShortTerm 是否为中短期险种
     * @return 负债现金流汇总列表
     */
    List<LiabilityCashFlowSummaryEntity> selectLiabilityCashFlowSummaryEntityListByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("cashFlowType") String cashFlowType,
            @Param("bpType") String bpType,
            @Param("durationType") String durationType,
            @Param("designType") String designType,
            @Param("isShortTerm") String isShortTerm);
}
