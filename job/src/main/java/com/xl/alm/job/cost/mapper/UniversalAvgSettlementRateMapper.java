package com.xl.alm.job.cost.mapper;

import com.xl.alm.job.cost.entity.UniversalAvgSettlementRateEntity;
import com.xl.alm.job.cost.query.UniversalAvgSettlementRateQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 万能平均结算利率表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface UniversalAvgSettlementRateMapper {

    /**
     * 查询万能平均结算利率列表
     *
     * @param query 查询条件
     * @return 万能平均结算利率集合
     */
    List<UniversalAvgSettlementRateEntity> selectUniversalAvgSettlementRateList(UniversalAvgSettlementRateQuery query);
}
