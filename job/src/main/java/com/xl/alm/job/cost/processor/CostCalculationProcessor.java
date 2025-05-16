package com.xl.alm.job.cost.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xl.alm.job.cost.task.AccountSummaryTask;
import com.xl.alm.job.cost.task.BusinessTypeSummaryTask;
import com.xl.alm.job.cost.task.ProductStatisticsTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

/**
 * 成本计算处理器
 * 合并了以下处理器的功能：
 * 1. 分产品统计处理器 (ProductStatisticsProcessor)
 * 2. 分业务类型汇总处理器 (BusinessTypeSummaryProcessor)
 * 3. 分账户汇总处理器 (AccountSummaryProcessor)
 *
 * @author AI Assistant
 */
@Slf4j
@Component
public class CostCalculationProcessor implements BasicProcessor {

    @Autowired
    private ProductStatisticsTask productStatisticsTask;

    @Autowired
    private BusinessTypeSummaryTask businessTypeSummaryTask;

    @Autowired
    private AccountSummaryTask accountSummaryTask;

    @Override
    public ProcessResult process(TaskContext taskContext) {
        log.info("成本计算处理器开始执行，任务参数：{}", taskContext.getJobParams());

        try {
            // 解析任务参数
            JSONObject params = JSON.parseObject(taskContext.getJobParams());
            String accountingPeriod = params.getString("accountingPeriod");

            if (accountingPeriod == null || accountingPeriod.isEmpty()) {
                log.error("任务参数错误，缺少accountingPeriod参数");
                return new ProcessResult(false, "任务参数错误，缺少accountingPeriod参数");
            }

            // 按顺序执行三个任务
            // 1. 执行分产品统计计算任务
            log.info("开始执行分产品统计计算任务");
            boolean productStatisticsResult = productStatisticsTask.execute(accountingPeriod);
            if (!productStatisticsResult) {
                log.error("分产品统计计算失败");
                return new ProcessResult(false, "分产品统计计算失败");
            }
            log.info("分产品统计计算成功");

            // 2. 执行分业务类型汇总计算任务
            log.info("开始执行分业务类型汇总计算任务");
            boolean businessTypeResult = businessTypeSummaryTask.execute(accountingPeriod);
            if (!businessTypeResult) {
                log.error("分业务类型汇总计算失败");
                return new ProcessResult(false, "分业务类型汇总计算失败");
            }
            log.info("分业务类型汇总计算成功");

            // 3. 执行分账户汇总计算任务
            log.info("开始执行分账户汇总计算任务");
            boolean accountResult = accountSummaryTask.execute(accountingPeriod);
            if (!accountResult) {
                log.error("分账户汇总计算失败");
                return new ProcessResult(false, "分账户汇总计算失败");
            }
            log.info("分账户汇总计算成功");

            // 所有任务执行成功
            return new ProcessResult(true, "成本计算处理器执行成功");
        } catch (Exception e) {
            log.error("成本计算处理器执行异常", e);
            return new ProcessResult(false, "成本计算处理器执行异常：" + e.getMessage());
        }
    }
}
