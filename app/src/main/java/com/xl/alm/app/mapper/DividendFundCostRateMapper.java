package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.DividendFundCostRateEntity;
import com.xl.alm.app.query.DividendFundCostRateQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分红方案表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface DividendFundCostRateMapper {
    /**
     * 查询分红方案列表
     *
     * @param query 分红方案查询条件
     * @return 分红方案列表
     */
    List<DividendFundCostRateEntity> selectDividendFundCostRateList(DividendFundCostRateQuery query);

    /**
     * 根据ID查询分红方案
     *
     * @param id 分红方案ID
     * @return 分红方案
     */
    DividendFundCostRateEntity selectDividendFundCostRateById(@Param("id") Long id);

    /**
     * 根据精算代码和账期查询分红方案
     *
     * @param actuarialCode 精算代码
     * @param accountingPeriod 账期
     * @return 分红方案
     */
    DividendFundCostRateEntity selectDividendFundCostRateByCode(@Param("actuarialCode") String actuarialCode, 
                                                       @Param("accountingPeriod") String accountingPeriod);

    /**
     * 新增分红方案
     *
     * @param dividendFundCostRate 分红方案
     * @return 结果
     */
    int insertDividendFundCostRate(DividendFundCostRateEntity dividendFundCostRate);

    /**
     * 修改分红方案
     *
     * @param dividendFundCostRate 分红方案
     * @return 结果
     */
    int updateDividendFundCostRate(DividendFundCostRateEntity dividendFundCostRate);

    /**
     * 删除分红方案
     *
     * @param id 分红方案ID
     * @return 结果
     */
    int deleteDividendFundCostRateById(Long id);

    /**
     * 批量删除分红方案
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteDividendFundCostRateByIds(Long[] ids);

    /**
     * 批量插入分红方案数据
     *
     * @param dividendFundCostRateList 分红方案列表
     * @return 影响行数
     */
    int batchInsertDividendFundCostRate(List<DividendFundCostRateEntity> dividendFundCostRateList);
}
