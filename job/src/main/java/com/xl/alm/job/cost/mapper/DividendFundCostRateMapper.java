package com.xl.alm.job.cost.mapper;

import com.xl.alm.job.cost.entity.DividendFundCostRateEntity;
import com.xl.alm.job.cost.query.DividendFundCostRateQuery;
import org.apache.ibatis.annotations.Mapper;

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
     * @param query 查询条件
     * @return 分红方案集合
     */
    List<DividendFundCostRateEntity> selectDividendFundCostRateList(DividendFundCostRateQuery query);
}
