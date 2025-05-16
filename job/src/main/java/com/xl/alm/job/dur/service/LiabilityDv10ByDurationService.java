package com.xl.alm.job.dur.service;

/**
 * 分中短负债基点价值DV10服务接口
 *
 * @author AI
 */
public interface LiabilityDv10ByDurationService {
    
    /**
     * 计算分中短负债基点价值DV10
     * 
     * @param accountPeriod 账期
     * @return 执行结果
     */
    boolean calculateLiabilityDv10ByDuration(String accountPeriod);
}
