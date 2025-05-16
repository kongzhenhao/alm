package com.xl.alm.job.dur.service;

/**
 * 关键久期折现因子服务接口
 *
 * @author AI
 */
public interface KeyDurationDiscountFactorsService {
    
    /**
     * 计算关键久期折现因子
     * 
     * @param accountPeriod 账期
     * @return 执行结果
     */
    boolean calculateKeyDurationDiscountFactors(String accountPeriod);
}
