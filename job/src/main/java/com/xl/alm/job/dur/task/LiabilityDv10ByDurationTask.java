package com.xl.alm.job.dur.task;

import com.xl.alm.job.dur.service.LiabilityDv10ByDurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分中短负债基点价值DV10计算任务
 *
 * @author AI
 */
@Slf4j
@Component
public class LiabilityDv10ByDurationTask {

    @Autowired
    private LiabilityDv10ByDurationService liabilityDv10ByDurationService;
    
    /**
     * 计算分中短负债基点价值DV10
     * 
     * @param accountPeriod 账期
     * @return 执行结果
     */
    public String calculateLiabilityDv10ByDuration(String accountPeriod) {
        log.info("开始执行计算分中短负债基点价值DV10任务，账期：{}", accountPeriod);
        
        try {
            boolean result = liabilityDv10ByDurationService.calculateLiabilityDv10ByDuration(accountPeriod);
            if (result) {
                log.info("计算分中短负债基点价值DV10任务执行成功，账期：{}", accountPeriod);
                return "success";
            } else {
                log.warn("计算分中短负债基点价值DV10任务执行失败，账期：{}", accountPeriod);
                return "fail";
            }
        } catch (Exception e) {
            log.error("计算分中短负债基点价值DV10任务执行异常，账期：{}", accountPeriod, e);
            return "error: " + e.getMessage();
        }
    }
}
