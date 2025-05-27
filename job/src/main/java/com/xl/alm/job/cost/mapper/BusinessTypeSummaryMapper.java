package com.xl.alm.job.cost.mapper;

import com.xl.alm.job.cost.entity.BusinessTypeSummaryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分业务类型汇总表(TB0008) Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface BusinessTypeSummaryMapper {

    /**
     * 批量插入分业务类型汇总数据
     *
     * @param businessTypeSummaryEntityList 分业务类型汇总列表
     * @return 影响行数
     */
    int batchInsertBusinessTypeSummary(List<BusinessTypeSummaryEntity> businessTypeSummaryEntityList);

    /**
     * 删除指定账期的分业务类型汇总数据
     *
     * @param accountingPeriod 账期
     * @return 影响行数
     */
    int deleteBusinessTypeSummaryByPeriod(@Param("accountingPeriod") String accountingPeriod);

    /**
     * 根据账期查询分业务类型汇总列表
     *
     * @param accountingPeriod 账期
     * @return 分业务类型汇总列表
     */
    List<BusinessTypeSummaryEntity> selectBusinessTypeSummaryListByPeriod(@Param("accountingPeriod") String accountingPeriod);
}
