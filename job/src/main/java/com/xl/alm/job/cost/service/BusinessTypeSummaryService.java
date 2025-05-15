package com.xl.alm.job.cost.service;

/**
 * 分业务类型汇总表(TB0008) 服务接口
 *
 * @author AI Assistant
 */
public interface BusinessTypeSummaryService {

    /**
     * 计算分业务类型汇总数据
     *
     * @param accountingPeriod 账期
     * @return 处理结果
     */
    boolean calculateBusinessTypeSummary(String accountingPeriod);
}
