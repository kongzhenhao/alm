package com.xl.alm.job.minc.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 利率风险对冲率计算Mapper接口
 * 对应UC0009：计算利率风险对冲率数据
 *
 * @author AI Assistant
 */
@Mapper
public interface IrHedgeRatioCalculationMapper {

    /**
     * 查询分部门最低资本明细表数据，筛选特定项目编码并按项目编码和账户编码分组汇总
     * 筛选项目编码：AA001(AA基础情景)、AA002(AA利率下降情景)、PV001(PV基础情景)、PV002(PV利率下降情景)
     *
     * @param accountingPeriod 账期
     * @return 汇总数据列表
     */
    List<Map<String, Object>> selectDeptMincapDetailData(@Param("accountingPeriod") String accountingPeriod);

    /**
     * 批量插入利率风险对冲率数据
     *
     * @param hedgeRatios 利率风险对冲率数据列表
     * @return 插入记录数
     */
    int batchInsertHedgeRatios(@Param("hedgeRatios") List<Map<String, Object>> hedgeRatios);

    /**
     * 根据账期删除利率风险对冲率数据
     *
     * @param accountingPeriod 账期
     * @return 删除记录数
     */
    int deleteByAccountingPeriod(@Param("accountingPeriod") String accountingPeriod);
}
