package com.xl.alm.job.dur.service;

import com.xl.alm.job.dur.service.impl.LiabilityDurationSummaryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 负债久期计算服务测试类
 *
 * @author AI Assistant
 */
@Slf4j
@SpringBootTest
public class LiabilityDurationSummaryServiceTest {

    @Autowired
    private LiabilityDurationSummaryService liabilityDurationSummaryService;

    @Test
    public void testCalculateLiabilityDurationSummary() {
        String accountPeriod = "202501"; // 测试账期
        boolean result = liabilityDurationSummaryService.calculateLiabilityDurationSummary(accountPeriod);
        log.info("负债久期计算结果：{}", result);
    }
}
