package com.xl.alm.job.dur.service;

/**
 * 负债久期计算服务接口
 *
 * @author AI Assistant
 */
public interface LiabilityDurationSummaryService {

    /**
     * 执行负债久期计算
     *
     * @param accountPeriod 账期
     * @return 处理结果
     */
    boolean calculateLiabilityDurationSummary(String accountPeriod);
}
