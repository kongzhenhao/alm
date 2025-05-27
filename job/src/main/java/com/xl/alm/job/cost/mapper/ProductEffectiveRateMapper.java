package com.xl.alm.job.cost.mapper;

import com.xl.alm.job.cost.entity.ProductEffectiveRateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分产品有效成本率表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface ProductEffectiveRateMapper {

    /**
     * 批量插入分产品有效成本率数据
     *
     * @param productEffectiveRateList 分产品有效成本率列表
     * @return 影响行数
     */
    int batchInsertProductEffectiveRate(List<ProductEffectiveRateEntity> productEffectiveRateList);

    /**
     * 删除指定账期的分产品有效成本率数据
     *
     * @param accountingPeriod 账期
     * @return 影响行数
     */
    int deleteProductEffectiveRateByPeriod(@Param("accountingPeriod") String accountingPeriod);

    /**
     * 根据账期查询分产品有效成本率列表
     *
     * @param accountingPeriod 账期
     * @return 分产品有效成本率列表
     */
    List<ProductEffectiveRateEntity> selectProductEffectiveRateListByPeriod(@Param("accountingPeriod") String accountingPeriod);
}
