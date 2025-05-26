package com.xl.alm.job.minc.task;

import com.xl.alm.job.minc.service.MinCapitalSummaryCalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 最低资本明细汇总数据计算任务
 * 对应UC0006：计算最低资本明细汇总数据
 *
 * @author AI Assistant
 */
@Slf4j
@Component
public class MinCapitalSummaryCalculationTask {

    @Autowired
    private MinCapitalSummaryCalculationService minCapitalSummaryCalculationService;

    /**
     * 执行最低资本明细汇总数据计算
     *
     * @param accountPeriod 账期
     * @return 处理结果
     */
    public boolean execute(String accountPeriod) {
        log.info("开始执行最低资本明细汇总数据计算任务，账期：{}", accountPeriod);
        long startTime = System.currentTimeMillis();
        
        try {
            boolean result = minCapitalSummaryCalculationService.calculateMinCapitalSummary(accountPeriod);
            
            long endTime = System.currentTimeMillis();
            log.info("最低资本明细汇总数据计算任务执行完成，账期：{}，耗时：{}ms，结果：{}", 
                    accountPeriod, (endTime - startTime), result);
            
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("最低资本明细汇总数据计算任务执行失败，账期：{}，耗时：{}ms", 
                    accountPeriod, (endTime - startTime), e);
            return false;
        }
    }
}
