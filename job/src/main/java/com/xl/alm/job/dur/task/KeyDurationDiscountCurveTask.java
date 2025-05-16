package com.xl.alm.job.dur.task;

import com.xl.alm.job.dur.service.KeyDurationDiscountCurveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 关键久期折现曲线计算任务
 *
 * @author AI
 */
@Slf4j
@Component
public class KeyDurationDiscountCurveTask {

    @Autowired
    private KeyDurationDiscountCurveService keyDurationDiscountCurveService;
    
    /**
     * 计算关键久期折现曲线
     * 
     * @param accountPeriod 账期
     * @return 执行结果
     */
    public String calculateKeyDurationDiscountCurve(String accountPeriod) {
        log.info("开始执行计算关键久期折现曲线任务，账期：{}", accountPeriod);
        
        try {
            boolean result = keyDurationDiscountCurveService.calculateKeyDurationDiscountCurve(accountPeriod);
            if (result) {
                log.info("计算关键久期折现曲线任务执行成功，账期：{}", accountPeriod);
                return "success";
            } else {
                log.warn("计算关键久期折现曲线任务执行失败，账期：{}", accountPeriod);
                return "fail";
            }
        } catch (Exception e) {
            log.error("计算关键久期折现曲线任务执行异常，账期：{}", accountPeriod, e);
            return "error: " + e.getMessage();
        }
    }
}
