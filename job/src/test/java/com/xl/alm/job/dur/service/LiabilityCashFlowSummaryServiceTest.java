package com.xl.alm.job.dur.service;

import com.xl.alm.job.dur.service.impl.LiabilityCashFlowSummaryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 负债现金流汇总及现值计算服务测试类
 *
 * @author AI Assistant
 */
@Slf4j
@SpringBootTest
public class LiabilityCashFlowSummaryServiceTest {

    @Autowired
    private LiabilityCashFlowSummaryService liabilityCashFlowSummaryService;

    @Test
    public void testCalculateLiabilityCashFlowSummary() {
        String accountPeriod = "202501"; // 测试账期
        boolean result = liabilityCashFlowSummaryService.calculateLiabilityCashFlowSummary(accountPeriod);
        log.info("负债现金流汇总及现值计算结果：{}", result);
    }
}
