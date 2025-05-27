package com.xl.alm.job.cost.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 分账户有效成本率服务测试类
 *
 * @author AI Assistant
 */
@Slf4j
@SpringBootTest
public class AccountEffectiveRateServiceTest {

    @Autowired
    private AccountEffectiveRateService accountEffectiveRateService;

    @Test
    public void testCalculateAccountEffectiveRate() {
        String accountingPeriod = "202412"; // 测试账期
        boolean result = accountEffectiveRateService.calculateAccountEffectiveRate(accountingPeriod);
        log.info("分账户有效成本率计算结果：{}", result);
    }
}
