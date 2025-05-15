package com.xl.alm.job.cost.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 分业务类型汇总服务测试类
 *
 * @author AI Assistant
 */
@Slf4j
@SpringBootTest
public class BusinessTypeSummaryServiceTest {

    @Autowired
    private BusinessTypeSummaryService businessTypeSummaryService;

    @Test
    public void testCalculateBusinessTypeSummary() {
        String accountingPeriod = "202501"; // 测试账期
        boolean result = businessTypeSummaryService.calculateBusinessTypeSummary(accountingPeriod);
        log.info("分业务类型汇总计算结果：{}", result);
    }
}
