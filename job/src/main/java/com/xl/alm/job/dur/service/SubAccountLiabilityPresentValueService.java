package com.xl.alm.job.dur.service;

/**
 * 分账户负债现金流现值汇总服务接口
 *
 * @author AI Assistant
 */
public interface SubAccountLiabilityPresentValueService {

    /**
     * 执行分账户负债现金流现值汇总
     *
     * @param accountPeriod 账期
     * @return 处理结果
     */
    boolean calculateSubAccountLiabilityPresentValue(String accountPeriod);
}
