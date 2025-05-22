package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.CostProductEffectiveRateEntity;
import com.xl.alm.app.query.CostProductEffectiveRateQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分产品有效成本率表 数据层
 *
 * @author AI Assistant
 */
public interface CostProductEffectiveRateMapper {
    /**
     * 查询分产品有效成本率列表
     *
     * @param query 分产品有效成本率查询条件
     * @return 分产品有效成本率列表
     */
    List<CostProductEffectiveRateEntity> selectCostProductEffectiveRateList(CostProductEffectiveRateQuery query);

    /**
     * 根据ID查询分产品有效成本率
     *
     * @param id 分产品有效成本率ID
     * @return 分产品有效成本率
     */
    CostProductEffectiveRateEntity selectCostProductEffectiveRateById(Long id);

    /**
     * 根据账期、精算代码、设计类型和是否中短查询分产品有效成本率
     *
     * @param accountingPeriod 账期
     * @param actuarialCode 精算代码
     * @param designType 设计类型
     * @param shortTermFlag 是否中短
     * @return 分产品有效成本率
     */
    CostProductEffectiveRateEntity selectCostProductEffectiveRateByKey(@Param("accountingPeriod") String accountingPeriod, 
                                                                      @Param("actuarialCode") String actuarialCode,
                                                                      @Param("designType") String designType,
                                                                      @Param("shortTermFlag") String shortTermFlag);

    /**
     * 新增分产品有效成本率
     *
     * @param costProductEffectiveRate 分产品有效成本率
     * @return 结果
     */
    int insertCostProductEffectiveRate(CostProductEffectiveRateEntity costProductEffectiveRate);

    /**
     * 批量新增分产品有效成本率
     *
     * @param list 分产品有效成本率列表
     * @return 结果
     */
    int batchInsertCostProductEffectiveRate(List<CostProductEffectiveRateEntity> list);

    /**
     * 修改分产品有效成本率
     *
     * @param costProductEffectiveRate 分产品有效成本率
     * @return 结果
     */
    int updateCostProductEffectiveRate(CostProductEffectiveRateEntity costProductEffectiveRate);

    /**
     * 删除分产品有效成本率
     *
     * @param id 分产品有效成本率ID
     * @return 结果
     */
    int deleteCostProductEffectiveRateById(Long id);

    /**
     * 批量删除分产品有效成本率
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCostProductEffectiveRateByIds(Long[] ids);
}
