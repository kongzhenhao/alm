package com.xl.alm.job.minc.service;

/**
 * 最低资本明细汇总数据计算服务接口
 * 对应UC0006：计算最低资本明细汇总数据
 *
 * @author AI Assistant
 */
public interface MinCapitalSummaryCalculationService {

    /**
     * 执行最低资本明细汇总数据计算
     *
     * 功能描述：
     * 1. 从TB0001表(分部门最低资本明细表)读取指定账期的数据
     * 2. 根据项目定义表中的风险类型过滤，只计算风险类型为"市场风险"和"信用风险"的项目
     * 3. 按账期、项目编码、账户编码分组汇总amount值
     * 4. 将汇总结果写入TB0007表(最低资本明细汇总表)
     *
     * @param accountPeriod 账期，格式：YYYYMM（如202306）
     * @return 处理结果，true表示成功，false表示失败
     */
    boolean calculateMinCapitalSummary(String accountPeriod);
}
