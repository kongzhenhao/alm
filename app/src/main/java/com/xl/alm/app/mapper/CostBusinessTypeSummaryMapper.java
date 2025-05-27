package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.CostBusinessTypeSummaryEntity;
import com.xl.alm.app.query.CostBusinessTypeSummaryQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分业务类型汇总表 数据层
 *
 * @author AI Assistant
 */
public interface CostBusinessTypeSummaryMapper {
    /**
     * 查询分业务类型汇总列表
     *
     * @param query 分业务类型汇总查询条件
     * @return 分业务类型汇总列表
     */
    List<CostBusinessTypeSummaryEntity> selectCostBusinessTypeSummaryList(CostBusinessTypeSummaryQuery query);

    /**
     * 根据ID查询分业务类型汇总
     *
     * @param id 分业务类型汇总ID
     * @return 分业务类型汇总
     */
    CostBusinessTypeSummaryEntity selectCostBusinessTypeSummaryById(Long id);

    /**
     * 根据情景名称、业务类型、账期和设计类型查询分业务类型汇总
     *
     * @param scenarioName 情景名称
     * @param businessType 业务类型
     * @param accountingPeriod 账期
     * @param designType 设计类型
     * @return 分业务类型汇总
     */
    CostBusinessTypeSummaryEntity selectCostBusinessTypeSummaryByKey(@Param("scenarioName") String scenarioName, 
                                                                    @Param("businessType") String businessType,
                                                                    @Param("accountingPeriod") String accountingPeriod,
                                                                    @Param("designType") String designType);

    /**
     * 新增分业务类型汇总
     *
     * @param costBusinessTypeSummary 分业务类型汇总
     * @return 结果
     */
    int insertCostBusinessTypeSummary(CostBusinessTypeSummaryEntity costBusinessTypeSummary);

    /**
     * 批量新增分业务类型汇总
     *
     * @param list 分业务类型汇总列表
     * @return 结果
     */
    int batchInsertCostBusinessTypeSummary(List<CostBusinessTypeSummaryEntity> list);

    /**
     * 修改分业务类型汇总
     *
     * @param costBusinessTypeSummary 分业务类型汇总
     * @return 结果
     */
    int updateCostBusinessTypeSummary(CostBusinessTypeSummaryEntity costBusinessTypeSummary);

    /**
     * 删除分业务类型汇总
     *
     * @param id 分业务类型汇总ID
     * @return 结果
     */
    int deleteCostBusinessTypeSummaryById(Long id);

    /**
     * 批量删除分业务类型汇总
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCostBusinessTypeSummaryByIds(Long[] ids);
}
