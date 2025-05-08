package com.xl.alm.job.dur.service;

/**
 * 分账户负债久期汇总服务接口
 *
 * @author AI Assistant
 */
public interface SubAccountLiabilityDurationService {

    /**
     * 执行分账户负债久期汇总
     *
     * @param accountPeriod 账期
     * @return 处理结果
     */
    boolean calculateSubAccountLiabilityDuration(String accountPeriod);
}
