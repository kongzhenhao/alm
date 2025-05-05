package com.xl.alm.job.dur.task;

import com.xl.alm.job.dur.service.SubAccountLiabilityPresentValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分账户负债现金流现值汇总任务
 *
 * @author AI Assistant
 */
@Slf4j
@Component
public class SubAccountLiabilityPresentValueTask {

    @Autowired
    private SubAccountLiabilityPresentValueService subAccountLiabilityPresentValueService;

    /**
     * 执行分账户负债现金流现值汇总
     *
     * @param accountPeriod 账期
     * @return 处理结果
     */
    public boolean execute(String accountPeriod) {
        log.info("开始执行分账户负债现金流现值汇总任务，账期：{}", accountPeriod);
        long startTime = System.currentTimeMillis();
        
        try {
            boolean result = subAccountLiabilityPresentValueService.calculateSubAccountLiabilityPresentValue(accountPeriod);
            
            long endTime = System.currentTimeMillis();
            log.info("分账户负债现金流现值汇总任务执行完成，账期：{}，耗时：{}ms，结果：{}", 
                    accountPeriod, (endTime - startTime), result);
            
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("分账户负债现金流现值汇总任务执行失败，账期：{}，耗时：{}ms", 
                    accountPeriod, (endTime - startTime), e);
            return false;
        }
    }
}
