package com.xl.alm.job.cost.service;

/**
 * 分账户汇总表(TB0009) 服务接口
 *
 * @author AI Assistant
 */
public interface AccountSummaryService {

    /**
     * 计算分账户汇总数据
     *
     * @param accountingPeriod 账期
     * @return 处理结果
     */
    boolean calculateAccountSummary(String accountingPeriod);
}
