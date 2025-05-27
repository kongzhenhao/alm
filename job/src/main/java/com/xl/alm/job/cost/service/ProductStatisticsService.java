package com.xl.alm.job.cost.service;

/**
 * 分产品统计表(TB0007) 服务接口
 *
 * @author AI Assistant
 */
public interface ProductStatisticsService {

    /**
     * 计算分产品统计数据
     *
     * @param accountingPeriod 账期
     * @return 处理结果
     */
    boolean calculateProductStatistics(String accountingPeriod);
}
