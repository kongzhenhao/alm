package com.xl.alm.job.dur.task;

import com.xl.alm.job.dur.service.LiabilityCashFlowSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 负债现金流汇总及现值计算任务
 *
 * @author AI Assistant
 */
@Slf4j
@Component
public class LiabilityCashFlowSummaryTask {

    @Autowired
    private LiabilityCashFlowSummaryService liabilityCashFlowSummaryService;

    /**
     * 执行负债现金流汇总及现值计算
     *
     * @param accountPeriod 账期
     * @return 处理结果
     */
    public boolean execute(String accountPeriod) {
        log.info("开始执行负债现金流汇总及现值计算任务，账期：{}", accountPeriod);
        long startTime = System.currentTimeMillis();
        
        try {
            boolean result = liabilityCashFlowSummaryService.calculateLiabilityCashFlowSummary(accountPeriod);
            
            long endTime = System.currentTimeMillis();
            log.info("负债现金流汇总及现值计算任务执行完成，账期：{}，耗时：{}ms，结果：{}", 
                    accountPeriod, (endTime - startTime), result);
            
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("负债现金流汇总及现值计算任务执行失败，账期：{}，耗时：{}ms", 
                    accountPeriod, (endTime - startTime), e);
            return false;
        }
    }
}
