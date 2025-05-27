package com.xl.alm.job.cost.service;

/**
 * 分账户有效成本率表 服务接口
 *
 * @author AI Assistant
 */
public interface AccountEffectiveRateService {

    /**
     * 计算分账户有效成本率数据
     *
     * @param accountingPeriod 账期
     * @return 处理结果
     */
    boolean calculateAccountEffectiveRate(String accountingPeriod);
}
