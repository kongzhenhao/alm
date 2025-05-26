package com.xl.alm.job.minc.service;

/**
 * 市场及信用最低资本计算服务接口
 *
 * @author AI Assistant
 */
public interface MarketCreditCapitalService {

    /**
     * 执行市场及信用最低资本计算
     * 
     * 功能描述：
     * 1. 从TB0006表(边际最低资本贡献率表)读取公司层面边际最低资本贡献因子
     * 2. 从TB0007表(最低资本明细汇总表)读取各账户下的金额数据
     * 3. 从TB0005表(项目定义表)读取最低资本计算公式
     * 4. 使用Aviator表达式引擎计算各账户下的市场风险和信用风险最低资本
     * 5. 将计算结果写入TB0008表(市场及信用最低资本表)
     *
     * @param accountingPeriod 账期，格式：YYYYMM（如202306）
     * @return 处理结果，true表示成功，false表示失败
     */
    boolean calculateMarketCreditCapital(String accountingPeriod);
}
