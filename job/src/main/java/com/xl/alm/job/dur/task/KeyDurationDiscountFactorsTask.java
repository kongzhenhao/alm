package com.xl.alm.job.dur.task;

import com.xl.alm.job.dur.service.KeyDurationDiscountFactorsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 关键久期折现因子计算任务
 *
 * @author AI
 */
@Slf4j
@Component
public class KeyDurationDiscountFactorsTask {

    @Autowired
    private KeyDurationDiscountFactorsService keyDurationDiscountFactorsService;
    
    /**
     * 计算关键久期折现因子
     * 
     * @param accountPeriod 账期
     * @return 执行结果
     */
    public String calculateKeyDurationDiscountFactors(String accountPeriod) {
        log.info("开始执行计算关键久期折现因子任务，账期：{}", accountPeriod);
        
        try {
            boolean result = keyDurationDiscountFactorsService.calculateKeyDurationDiscountFactors(accountPeriod);
            if (result) {
                log.info("计算关键久期折现因子任务执行成功，账期：{}", accountPeriod);
                return "success";
            } else {
                log.warn("计算关键久期折现因子任务执行失败，账期：{}", accountPeriod);
                return "fail";
            }
        } catch (Exception e) {
            log.error("计算关键久期折现因子任务执行异常，账期：{}", accountPeriod, e);
            return "error: " + e.getMessage();
        }
    }
}
