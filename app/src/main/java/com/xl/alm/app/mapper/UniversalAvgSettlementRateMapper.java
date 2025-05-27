package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.UniversalAvgSettlementRateEntity;
import com.xl.alm.app.query.UniversalAvgSettlementRateQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
     * @param query 万能平均结算利率查询条件
     * @return 万能平均结算利率列表
     */
    List<UniversalAvgSettlementRateEntity> selectUniversalAvgSettlementRateList(UniversalAvgSettlementRateQuery query);

    /**
     * 根据ID查询万能平均结算利率
     *
     * @param id 万能平均结算利率ID
     * @return 万能平均结算利率
     */
    UniversalAvgSettlementRateEntity selectUniversalAvgSettlementRateById(@Param("id") Long id);

    /**
     * 根据精算代码和账期查询万能平均结算利率
     *
     * @param actuarialCode 精算代码
     * @param accountingPeriod 账期
     * @return 万能平均结算利率
     */
    UniversalAvgSettlementRateEntity selectUniversalAvgSettlementRateByCode(@Param("actuarialCode") String actuarialCode, 
                                                       @Param("accountingPeriod") String accountingPeriod);

    /**
     * 新增万能平均结算利率
     *
     * @param universalAvgSettlementRate 万能平均结算利率
     * @return 结果
     */
    int insertUniversalAvgSettlementRate(UniversalAvgSettlementRateEntity universalAvgSettlementRate);

    /**
     * 修改万能平均结算利率
     *
     * @param universalAvgSettlementRate 万能平均结算利率
     * @return 结果
     */
    int updateUniversalAvgSettlementRate(UniversalAvgSettlementRateEntity universalAvgSettlementRate);

    /**
     * 删除万能平均结算利率
     *
     * @param id 万能平均结算利率ID
     * @return 结果
     */
    int deleteUniversalAvgSettlementRateById(Long id);

    /**
     * 批量删除万能平均结算利率
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteUniversalAvgSettlementRateByIds(Long[] ids);

    /**
     * 批量插入万能平均结算利率数据
     *
     * @param universalAvgSettlementRateList 万能平均结算利率列表
     * @return 影响行数
     */
    int batchInsertUniversalAvgSettlementRate(List<UniversalAvgSettlementRateEntity> universalAvgSettlementRateList);
}
