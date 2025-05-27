package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.CostAccountSummaryEntity;
import com.xl.alm.app.query.CostAccountSummaryQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分账户汇总表 数据层
 *
 * @author AI Assistant
 */
public interface CostAccountSummaryMapper {
    /**
     * 查询分账户汇总列表
     *
     * @param query 分账户汇总查询条件
     * @return 分账户汇总列表
     */
    List<CostAccountSummaryEntity> selectCostAccountSummaryList(CostAccountSummaryQuery query);

    /**
     * 根据ID查询分账户汇总
     *
     * @param id 分账户汇总ID
     * @return 分账户汇总
     */
    CostAccountSummaryEntity selectCostAccountSummaryById(Long id);

    /**
     * 根据情景名称、账期和设计类型查询分账户汇总
     *
     * @param scenarioName 情景名称
     * @param accountingPeriod 账期
     * @param designType 设计类型
     * @return 分账户汇总
     */
    CostAccountSummaryEntity selectCostAccountSummaryByKey(@Param("scenarioName") String scenarioName, 
                                                          @Param("accountingPeriod") String accountingPeriod,
                                                          @Param("designType") String designType);

    /**
     * 新增分账户汇总
     *
     * @param costAccountSummary 分账户汇总
     * @return 结果
     */
    int insertCostAccountSummary(CostAccountSummaryEntity costAccountSummary);

    /**
     * 批量新增分账户汇总
     *
     * @param list 分账户汇总列表
     * @return 结果
     */
    int batchInsertCostAccountSummary(List<CostAccountSummaryEntity> list);

    /**
     * 修改分账户汇总
     *
     * @param costAccountSummary 分账户汇总
     * @return 结果
     */
    int updateCostAccountSummary(CostAccountSummaryEntity costAccountSummary);

    /**
     * 删除分账户汇总
     *
     * @param id 分账户汇总ID
     * @return 结果
     */
    int deleteCostAccountSummaryById(Long id);

    /**
     * 批量删除分账户汇总
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCostAccountSummaryByIds(Long[] ids);
}
