package com.xl.alm.job.cost.service;

/**
 * 分产品有效成本率表 服务接口
 *
 * @author AI Assistant
 */
public interface ProductEffectiveRateService {

    /**
     * 计算分产品有效成本率数据
     *
     * @param accountingPeriod 账期
     * @return 处理结果
     */
    boolean calculateProductEffectiveRate(String accountingPeriod);
}
