package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.CostProductStatisticsEntity;
import com.xl.alm.app.query.CostProductStatisticsQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分产品统计表 数据层
 *
 * @author AI Assistant
 */
public interface CostProductStatisticsMapper {
    /**
     * 查询分产品统计列表
     *
     * @param query 分产品统计查询条件
     * @return 分产品统计列表
     */
    List<CostProductStatisticsEntity> selectCostProductStatisticsList(CostProductStatisticsQuery query);

    /**
     * 根据ID查询分产品统计
     *
     * @param id 分产品统计ID
     * @return 分产品统计
     */
    CostProductStatisticsEntity selectCostProductStatisticsById(Long id);

    /**
     * 根据精算代码和账期查询分产品统计
     *
     * @param actuarialCode 精算代码
     * @param accountingPeriod 账期
     * @param scenarioName 情景名称
     * @param businessType 业务类型
     * @return 分产品统计
     */
    CostProductStatisticsEntity selectCostProductStatisticsByCode(@Param("actuarialCode") String actuarialCode, 
                                                                 @Param("accountingPeriod") String accountingPeriod,
                                                                 @Param("scenarioName") String scenarioName,
                                                                 @Param("businessType") String businessType);

    /**
     * 新增分产品统计
     *
     * @param costProductStatistics 分产品统计
     * @return 结果
     */
    int insertCostProductStatistics(CostProductStatisticsEntity costProductStatistics);

    /**
     * 批量新增分产品统计
     *
     * @param list 分产品统计列表
     * @return 结果
     */
    int batchInsertCostProductStatistics(List<CostProductStatisticsEntity> list);

    /**
     * 修改分产品统计
     *
     * @param costProductStatistics 分产品统计
     * @return 结果
     */
    int updateCostProductStatistics(CostProductStatisticsEntity costProductStatistics);

    /**
     * 删除分产品统计
     *
     * @param id 分产品统计ID
     * @return 结果
     */
    int deleteCostProductStatisticsById(Long id);

    /**
     * 批量删除分产品统计
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCostProductStatisticsByIds(Long[] ids);
}
