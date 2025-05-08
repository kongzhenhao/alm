package com.xl.alm.job.dur.service;

/**
 * 负债现金流汇总及现值计算服务接口
 *
 * @author AI Assistant
 */
public interface LiabilityCashFlowSummaryService {

    /**
     * 执行负债现金流汇总及现值计算
     *
     * @param accountPeriod 账期
     * @return 处理结果
     */
    boolean calculateLiabilityCashFlowSummary(String accountPeriod);
}
