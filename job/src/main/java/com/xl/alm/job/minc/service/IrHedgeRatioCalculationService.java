package com.xl.alm.job.minc.service;

/**
 * 利率风险对冲率计算服务接口
 * 对应UC0009：计算利率风险对冲率数据
 *
 * @author AI Assistant
 */
public interface IrHedgeRatioCalculationService {

    /**
     * 数据准备 - 读取TB0001表数据并按项目编码和账户编码分组汇总
     *
     * @param accountingPeriod 账期
     * @return 执行结果
     */
    boolean prepareData(String accountingPeriod);

    /**
     * 计算利率风险敏感度和对冲率
     *
     * @param accountingPeriod 账期
     * @return 执行结果
     */
    boolean calculateHedgeRatios(String accountingPeriod);
}
