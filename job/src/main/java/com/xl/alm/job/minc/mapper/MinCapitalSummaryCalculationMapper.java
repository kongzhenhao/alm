package com.xl.alm.job.minc.mapper;

import com.xl.alm.job.minc.entity.MinCapitalSummaryCalculationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 最低资本明细汇总数据计算 Mapper接口
 * 对应UC0006：计算最低资本明细汇总数据
 *
 * @author AI Assistant
 */
@Mapper
public interface MinCapitalSummaryCalculationMapper {

    /**
     * 从分部门最低资本明细表汇总数据
     * 根据项目定义表中的风险类型过滤，只计算风险类型为"市场风险"和"信用风险"的项目
     * 按账期、项目编码、账户编码分组，汇总amount值
     *
     * @param accountingPeriod 账期，格式：YYYYMM（如202306）
     * @return 汇总后的数据列表
     */
    List<MinCapitalSummaryCalculationEntity> selectSummaryFromDeptMincapDetail(@Param("accountingPeriod") String accountingPeriod);

    /**
     * 删除指定账期的最低资本明细汇总表数据
     * 用于清理目标账期的数据，避免重复插入
     *
     * @param accountingPeriod 账期，格式：YYYYMM（如202306）
     * @return 删除的记录数
     */
    int deleteMinCapitalSummaryByPeriod(@Param("accountingPeriod") String accountingPeriod);

    /**
     * 批量插入最低资本明细汇总表数据
     *
     * @param entityList 汇总数据列表
     * @return 插入的记录数
     */
    int batchInsertMinCapitalSummary(List<MinCapitalSummaryCalculationEntity> entityList);
}
