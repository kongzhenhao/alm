package com.xl.alm.job.dur.mapper;

import com.xl.alm.job.dur.entity.DiscountFactorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 折现因子表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface DiscountFactorMapper {

    /**
     * 查询折现因子列表
     *
     * @param accountPeriod 账期
     * @return 折现因子列表
     */
    List<DiscountFactorEntity> selectDiscountFactorEntityListByPeriod(@Param("accountPeriod") String accountPeriod);

    /**
     * 根据账期、因子类型、基点类型和久期类型查询折现因子
     *
     * @param accountPeriod 账期
     * @param factorType 因子类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @return 折现因子
     */
    DiscountFactorEntity selectDiscountFactorEntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("factorType") String factorType,
            @Param("bpType") String bpType,
            @Param("durationType") String durationType);
}
