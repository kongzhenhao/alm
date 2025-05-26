package com.xl.alm.job.dur.task;

import com.xl.alm.job.dur.service.AccountLiabilityDv10Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分账户负债基点价值DV10计算任务
 *
 * @author AI
 */
@Slf4j
@Component
public class AccountLiabilityDv10Task {

    @Autowired
    private AccountLiabilityDv10Service accountLiabilityDv10Service;

    /**
     * 计算分账户负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @return 执行结果
     */
    public boolean calculateAccountLiabilityDv10(String accountPeriod) {
        log.info("【分账户DV10计算任务】开始执行分账户负债基点价值DV10计算任务，账期：{}", accountPeriod);
        boolean result = accountLiabilityDv10Service.calculateAccountLiabilityDv10(accountPeriod);
        log.info("【分账户DV10计算任务】分账户负债基点价值DV10计算任务执行完成，账期：{}，结果：{}", accountPeriod, result ? "成功" : "失败");
        return result;
    }
}
