package com.xl.alm.job.dur.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xl.alm.job.dur.entity.DiscountCurveEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 折现曲线表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface DiscountCurveMapper extends BaseMapper<DiscountCurveEntity> {

    /**
     * 查询折现曲线列表
     *
     * @param accountPeriod 账期
     * @return 折现曲线列表
     */
    List<DiscountCurveEntity> selectDiscountCurveEntityListByPeriod(@Param("accountPeriod") String accountPeriod);

    /**
     * 根据账期、曲线类型、基点类型和久期类型查询折现曲线
     *
     * @param accountPeriod 账期
     * @param curveType 曲线类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @return 折现曲线
     */
    DiscountCurveEntity selectDiscountCurveEntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("curveType") String curveType,
            @Param("bpType") String bpType,
            @Param("durationType") String durationType);

    /**
     * 根据账期删除折现曲线
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteByAccountPeriod(@Param("accountPeriod") String accountPeriod);

    /**
     * 批量插入折现曲线
     *
     * @param list 折现曲线列表
     * @return 结果
     */
    int batchInsert(List<DiscountCurveEntity> list);
}
