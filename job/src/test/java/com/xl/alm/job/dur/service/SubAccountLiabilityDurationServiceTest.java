package com.xl.alm.job.dur.service;

import com.xl.alm.job.dur.service.impl.SubAccountLiabilityDurationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 分账户负债久期汇总服务测试类
 *
 * @author AI Assistant
 */
@Slf4j
@SpringBootTest
public class SubAccountLiabilityDurationServiceTest {

    @Autowired
    private SubAccountLiabilityDurationService subAccountLiabilityDurationService;

    @Test
    public void testCalculateSubAccountLiabilityDuration() {
        String accountPeriod = "202501"; // 测试账期
        boolean result = subAccountLiabilityDurationService.calculateSubAccountLiabilityDuration(accountPeriod);
        log.info("分账户负债久期汇总结果：{}", result);
    }
}
