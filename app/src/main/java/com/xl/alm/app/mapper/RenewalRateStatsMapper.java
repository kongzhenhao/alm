package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.RenewalRateStatsEntity;
import com.xl.alm.app.query.RenewalRateStatsQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 保单续保率统计表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface RenewalRateStatsMapper {

    /**
     * 查询保单续保率统计列表
     *
     * @param query 保单续保率统计查询条件
     * @return 保单续保率统计列表
     */
    List<RenewalRateStatsEntity> selectRenewalRateStatsEntityList(RenewalRateStatsQuery query);

    /**
     * 查询保单续保率统计详细
     *
     * @param id 保单续保率统计ID
     * @return 保单续保率统计
     */
    RenewalRateStatsEntity selectRenewalRateStatsEntityById(Long id);

    /**
     * 新增保单续保率统计
     *
     * @param entity 保单续保率统计
     * @return 结果
     */
    int insertRenewalRateStatsEntity(RenewalRateStatsEntity entity);

    /**
     * 批量新增保单续保率统计
     *
     * @param entityList 保单续保率统计列表
     * @return 结果
     */
    int batchInsertRenewalRateStatsEntity(List<RenewalRateStatsEntity> entityList);

    /**
     * 修改保单续保率统计
     *
     * @param entity 保单续保率统计
     * @return 结果
     */
    int updateRenewalRateStatsEntity(RenewalRateStatsEntity entity);

    /**
     * 删除保单续保率统计
     *
     * @param id 保单续保率统计ID
     * @return 结果
     */
    int deleteRenewalRateStatsEntityById(Long id);

    /**
     * 批量删除保单续保率统计
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteRenewalRateStatsEntityByIds(Long[] ids);

    /**
     * 根据账期删除保单续保率统计
     *
     * @param accountingPeriod 账期
     * @return 结果
     */
    int deleteRenewalRateStatsEntityByPeriod(@Param("accountingPeriod") String accountingPeriod);

    /**
     * 计算续保率
     *
     * @param accountingPeriod 账期
     * @return 结果
     */
    int calcRenewalRateStats(@Param("accountingPeriod") String accountingPeriod);
}
