package com.xl.alm.job.cost.mapper;

import com.xl.alm.job.cost.entity.ProductStatisticsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分产品统计表(TB0007) Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface ProductStatisticsMapper {

    /**
     * 批量插入分产品统计数据
     *
     * @param productStatisticsEntityList 分产品统计列表
     * @return 影响行数
     */
    int batchInsertProductStatistics(List<ProductStatisticsEntity> productStatisticsEntityList);

    /**
     * 删除指定账期的分产品统计数据
     *
     * @param accountingPeriod 账期
     * @return 影响行数
     */
    int deleteProductStatisticsByPeriod(@Param("accountingPeriod") String accountingPeriod);

    /**
     * 根据账期查询分产品统计列表
     *
     * @param accountingPeriod 账期
     * @return 分产品统计列表
     */
    List<ProductStatisticsEntity> selectProductStatisticsListByPeriod(@Param("accountingPeriod") String accountingPeriod);
}
