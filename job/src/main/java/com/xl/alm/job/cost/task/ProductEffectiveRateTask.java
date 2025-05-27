package com.xl.alm.job.cost.task;

import com.xl.alm.job.cost.service.ProductEffectiveRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分产品有效成本率任务
 *
 * @author AI Assistant
 */
@Slf4j
@Component
public class ProductEffectiveRateTask {

    @Autowired
    private ProductEffectiveRateService productEffectiveRateService;

    /**
     * 执行分产品有效成本率计算
     *
     * @param accountingPeriod 账期
     * @return 处理结果
     */
    public boolean execute(String accountingPeriod) {
        log.info("开始执行分产品有效成本率计算任务，账期：{}", accountingPeriod);
        long startTime = System.currentTimeMillis();
        
        try {
            boolean result = productEffectiveRateService.calculateProductEffectiveRate(accountingPeriod);
            
            long endTime = System.currentTimeMillis();
            log.info("分产品有效成本率计算任务执行完成，账期：{}，耗时：{}ms，结果：{}", 
                    accountingPeriod, (endTime - startTime), result);
            
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("分产品有效成本率计算任务执行失败，账期：{}，耗时：{}ms", 
                    accountingPeriod, (endTime - startTime), e);
            return false;
        }
    }
}
