package com.xl.alm.job.cost.service;

import com.xl.alm.job.cost.task.ProductStatisticsTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 分产品统计任务测试类
 *
 * @author AI Assistant
 */
@Slf4j
@SpringBootTest
public class ProductStatisticsTaskTest {

    @Autowired
    private ProductStatisticsTask productStatisticsTask;

    @Test
    public void testExecute() {
        String accountingPeriod = "202412"; // 测试账期
        boolean result = productStatisticsTask.execute(accountingPeriod);
        log.info("分产品统计计算结果：{}", result);
    }
}
