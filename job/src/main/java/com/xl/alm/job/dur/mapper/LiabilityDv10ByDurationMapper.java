package com.xl.alm.job.dur.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xl.alm.job.dur.entity.LiabilityDv10ByDurationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分中短负债基点价值DV10表 Mapper 接口
 *
 * @author AI
 */
@Mapper
public interface LiabilityDv10ByDurationMapper extends BaseMapper<LiabilityDv10ByDurationEntity> {

    /**
     * 根据账期删除分中短负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    int deleteByAccountPeriod(@Param("accountPeriod") String accountPeriod);

    /**
     * 批量插入分中短负债基点价值DV10
     *
     * @param entities 分中短负债基点价值DV10实体列表
     * @return 影响行数
     */
    int batchInsert(@Param("entities") List<LiabilityDv10ByDurationEntity> entities);

    /**
     * 根据账期查询分中短负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @return 分中短负债基点价值DV10列表
     */
    List<LiabilityDv10ByDurationEntity> selectByAccountPeriod(@Param("accountPeriod") String accountPeriod);
}
