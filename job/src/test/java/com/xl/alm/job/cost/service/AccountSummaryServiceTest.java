package com.xl.alm.job.cost.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 分账户汇总服务测试类
 *
 * @author AI Assistant
 */
@Slf4j
@SpringBootTest
public class AccountSummaryServiceTest {

    @Autowired
    private AccountSummaryService accountSummaryService;

    @Test
    public void testCalculateAccountSummary() {
        String accountingPeriod = "202412"; // 测试账期
        boolean result = accountSummaryService.calculateAccountSummary(accountingPeriod);
        log.info("分账户汇总计算结果：{}", result);
    }
}
