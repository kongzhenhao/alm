package com.xl.alm.job.minc.service;

/**
 * 边际最低资本贡献率计算服务接口
 * 对应UC0007：计算边际最低资本贡献率数据
 *
 * @author AI Assistant
 */
public interface MarginalCapitalCalculationService {

    /**
     * 执行边际最低资本贡献率计算（只计算子风险层面）
     *
     * 功能描述：
     * 1. 从TB0002表(风险项目金额表)读取数据，获取再保后金额
     * 2. 从TB0004表(相关系数表)读取相关系数数据
     * 3. 从TB0005表(项目定义表)读取项目定义和公式
     * 4. 使用Aviator表达式引擎计算子风险层面边际最低资本贡献因子
     * 5. 将计算结果写入TB0006表(边际最低资本贡献率表)
     *
     * @param accountingPeriod 账期，格式：YYYYMM（如202306）
     * @return 处理结果，true表示成功，false表示失败
     */
    boolean calculateSubRiskMarginalFactor(String accountingPeriod);

    /**
     * 执行边际最低资本贡献率计算（计算公司层面）
     *
     * 功能描述：
     * 1. 从TB0005表(项目定义表)读取有公司层面公式的项目
     * 2. 从TB0006表(边际最低资本贡献率表)读取已计算的子风险层面因子
     * 3. 使用Aviator表达式引擎计算公司层面边际最低资本贡献因子
     * 4. 将计算结果更新到TB0006表的company_marginal_factor字段
     *
     * @param accountingPeriod 账期，格式：YYYYMM（如202306）
     * @return 处理结果，true表示成功，false表示失败
     */
    boolean calculateCompanyMarginalFactor(String accountingPeriod);
}
