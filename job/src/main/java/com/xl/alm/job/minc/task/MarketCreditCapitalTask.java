package com.xl.alm.job.minc.task;

import com.xl.alm.job.minc.service.MarketCreditCapitalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 市场及信用最低资本计算任务类
 *
 * @author AI Assistant
 */
@Slf4j
@Component
public class MarketCreditCapitalTask {

    @Autowired
    private MarketCreditCapitalService marketCreditCapitalService;

    /**
     * 执行市场及信用最低资本计算
     *
     * @param accountPeriod 账期
     * @return 处理结果
     */
    public boolean executeCalculation(String accountPeriod) {
        log.info("开始执行市场及信用最低资本计算任务，账期：{}", accountPeriod);
        long startTime = System.currentTimeMillis();
        
        try {
            boolean result = marketCreditCapitalService.calculateMarketCreditCapital(accountPeriod);
            
            long endTime = System.currentTimeMillis();
            log.info("市场及信用最低资本计算任务执行完成，账期：{}，耗时：{}ms，结果：{}", 
                    accountPeriod, (endTime - startTime), result);
            
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("市场及信用最低资本计算任务执行失败，账期：{}，耗时：{}ms", 
                    accountPeriod, (endTime - startTime), e);
            return false;
        }
    }
}
