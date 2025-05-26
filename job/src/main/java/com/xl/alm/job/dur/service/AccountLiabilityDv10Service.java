package com.xl.alm.job.dur.service;

/**
 * 分账户负债基点价值DV10服务接口
 *
 * @author AI
 */
public interface AccountLiabilityDv10Service {
    
    /**
     * 计算分账户负债基点价值DV10
     * 
     * @param accountPeriod 账期
     * @return 执行结果
     */
    boolean calculateAccountLiabilityDv10(String accountPeriod);
}
