package com.xl.alm.job.dur.mapper;

import com.xl.alm.job.dur.entity.LiabilityCashFlowEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 负债现金流表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface LiabilityCashFlowMapper {

    /**
     * 查询负债现金流列表
     *
     * @param accountPeriod 账期
     * @return 负债现金流列表
     */
    List<LiabilityCashFlowEntity> selectLiabilityCashFlowEntityListByPeriod(@Param("accountPeriod") String accountPeriod);

    /**
     * 根据账期、现金流类型、基点类型、久期类型查询负债现金流列表
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @return 负债现金流列表
     */
    List<LiabilityCashFlowEntity> selectLiabilityCashFlowEntityListByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("cashFlowType") String cashFlowType,
            @Param("bpType") String bpType,
            @Param("durationType") String durationType);
}
