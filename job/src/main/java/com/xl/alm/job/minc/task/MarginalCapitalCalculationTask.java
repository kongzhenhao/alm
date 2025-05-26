package com.xl.alm.job.minc.task;

import com.xl.alm.job.minc.service.MarginalCapitalCalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 边际最低资本贡献率计算任务
 * 对应UC0007：计算边际最低资本贡献率数据
 *
 * @author AI Assistant
 */
@Slf4j
@Component
public class MarginalCapitalCalculationTask {

    @Autowired
    private MarginalCapitalCalculationService marginalCapitalCalculationService;

    /**
     * 执行边际最低资本贡献率计算（子风险层面）
     *
     * @param accountPeriod 账期
     * @return 处理结果
     */
    public boolean executeSubRiskCalculation(String accountPeriod) {
        log.info("开始执行边际最低资本贡献率计算任务（子风险层面），账期：{}", accountPeriod);
        long startTime = System.currentTimeMillis();

        try {
            boolean result = marginalCapitalCalculationService.calculateSubRiskMarginalFactor(accountPeriod);

            long endTime = System.currentTimeMillis();
            log.info("边际最低资本贡献率计算任务（子风险层面）执行完成，账期：{}，耗时：{}ms，结果：{}",
                    accountPeriod, (endTime - startTime), result);

            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("边际最低资本贡献率计算任务（子风险层面）执行失败，账期：{}，耗时：{}ms",
                    accountPeriod, (endTime - startTime), e);
            return false;
        }
    }

    /**
     * 执行边际最低资本贡献率计算（公司层面）
     *
     * @param accountPeriod 账期
     * @return 处理结果
     */
    public boolean executeCompanyCalculation(String accountPeriod) {
        log.info("开始执行边际最低资本贡献率计算任务（公司层面），账期：{}", accountPeriod);
        long startTime = System.currentTimeMillis();

        try {
            boolean result = marginalCapitalCalculationService.calculateCompanyMarginalFactor(accountPeriod);

            long endTime = System.currentTimeMillis();
            log.info("边际最低资本贡献率计算任务（公司层面）执行完成，账期：{}，耗时：{}ms，结果：{}",
                    accountPeriod, (endTime - startTime), result);

            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("边际最低资本贡献率计算任务（公司层面）执行失败，账期：{}，耗时：{}ms",
                    accountPeriod, (endTime - startTime), e);
            return false;
        }
    }
}
