package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.CostAccountEffectiveRateEntity;
import com.xl.alm.app.query.CostAccountEffectiveRateQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分账户有效成本率表 数据层
 *
 * @author AI Assistant
 */
public interface CostAccountEffectiveRateMapper {
    /**
     * 查询分账户有效成本率列表
     *
     * @param query 分账户有效成本率查询条件
     * @return 分账户有效成本率列表
     */
    List<CostAccountEffectiveRateEntity> selectCostAccountEffectiveRateList(CostAccountEffectiveRateQuery query);

    /**
     * 根据ID查询分账户有效成本率
     *
     * @param id 分账户有效成本率ID
     * @return 分账户有效成本率
     */
    CostAccountEffectiveRateEntity selectCostAccountEffectiveRateById(Long id);

    /**
     * 根据账期和设计类型查询分账户有效成本率
     *
     * @param accountingPeriod 账期
     * @param designType 设计类型
     * @return 分账户有效成本率
     */
    CostAccountEffectiveRateEntity selectCostAccountEffectiveRateByKey(@Param("accountingPeriod") String accountingPeriod, 
                                                                      @Param("designType") String designType);

    /**
     * 新增分账户有效成本率
     *
     * @param costAccountEffectiveRate 分账户有效成本率
     * @return 结果
     */
    int insertCostAccountEffectiveRate(CostAccountEffectiveRateEntity costAccountEffectiveRate);

    /**
     * 批量新增分账户有效成本率
     *
     * @param list 分账户有效成本率列表
     * @return 结果
     */
    int batchInsertCostAccountEffectiveRate(List<CostAccountEffectiveRateEntity> list);

    /**
     * 修改分账户有效成本率
     *
     * @param costAccountEffectiveRate 分账户有效成本率
     * @return 结果
     */
    int updateCostAccountEffectiveRate(CostAccountEffectiveRateEntity costAccountEffectiveRate);

    /**
     * 删除分账户有效成本率
     *
     * @param id 分账户有效成本率ID
     * @return 结果
     */
    int deleteCostAccountEffectiveRateById(Long id);

    /**
     * 批量删除分账户有效成本率
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCostAccountEffectiveRateByIds(Long[] ids);
}
