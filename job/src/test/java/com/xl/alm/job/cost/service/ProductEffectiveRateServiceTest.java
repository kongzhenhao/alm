package com.xl.alm.job.cost.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 分产品有效成本率服务测试类
 *
 * @author AI Assistant
 */
@Slf4j
@SpringBootTest
public class ProductEffectiveRateServiceTest {

    @Autowired
    private ProductEffectiveRateService productEffectiveRateService;

    /**
     * 测试计算分产品有效成本率
     * 使用真实数据库数据进行计算
     */
    @Test
    public void testCalculateProductEffectiveRate() {
        // 设置测试账期
        String accountingPeriod = "202412";

        try {
            // 执行计算
            long startTime = System.currentTimeMillis();
            boolean result = productEffectiveRateService.calculateProductEffectiveRate(accountingPeriod);
            long endTime = System.currentTimeMillis();

            // 验证结果
            Assertions.assertTrue(result, "计算分产品有效成本率应该成功");

            // 输出执行时间和结果
            log.info("分产品有效成本率计算结果：{}，耗时：{}毫秒", result, (endTime - startTime));
        } catch (Exception e) {
            log.error("分产品有效成本率计算异常", e);
            Assertions.fail("计算过程发生异常：" + e.getMessage());
        }
    }
}
