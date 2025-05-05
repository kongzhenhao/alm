package com.xl.alm.job.dur.service;

import com.xl.alm.job.dur.service.impl.SubAccountLiabilityPresentValueServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 分账户负债现金流现值汇总服务测试类
 *
 * @author AI Assistant
 */
@Slf4j
@SpringBootTest
public class SubAccountLiabilityPresentValueServiceTest {

    @Autowired
    private SubAccountLiabilityPresentValueService subAccountLiabilityPresentValueService;

    @Test
    public void testCalculateSubAccountLiabilityPresentValue() {
        String accountPeriod = "202501"; // 测试账期
        boolean result = subAccountLiabilityPresentValueService.calculateSubAccountLiabilityPresentValue(accountPeriod);
        log.info("分账户负债现金流现值汇总结果：{}", result);
    }
}
