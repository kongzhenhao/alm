package com.xl.alm.job.dur.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xl.alm.job.dur.task.LiabilityCashFlowSummaryTask;
import com.xl.alm.job.dur.task.LiabilityDurationSummaryTask;
import com.xl.alm.job.dur.task.SubAccountLiabilityPresentValueTask;
import com.xl.alm.job.dur.task.SubAccountLiabilityDurationTask;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

/**
 * 负债计算处理器
 * 合并了以下处理器的功能：
 * 1. 负债现金流汇总及现值计算处理器 (LiabilityCashFlowSummaryProcessor)
 * 2. 负债久期计算处理器 (LiabilityDurationSummaryProcessor)
 * 3. 分账户负债现金流现值汇总处理器 (SubAccountLiabilityPresentValueProcessor)
 * 4. 分账户负债久期汇总处理器 (SubAccountLiabilityDurationProcessor)
 *
 * @author AI Assistant
 */
@Slf4j
@Component
public class LiabilityCalculationProcessor implements BasicProcessor {

    @Autowired
    private LiabilityCashFlowSummaryTask liabilityCashFlowSummaryTask;

    @Autowired
    private LiabilityDurationSummaryTask liabilityDurationSummaryTask;

    @Autowired
    private SubAccountLiabilityPresentValueTask subAccountLiabilityPresentValueTask;

    @Autowired
    private SubAccountLiabilityDurationTask subAccountLiabilityDurationTask;

    @Override
    public ProcessResult process(TaskContext taskContext) {
        log.info("负债计算处理器开始执行，任务参数：{}", taskContext.getJobParams());
        
        try {
            // 解析任务参数
            JSONObject params = JSON.parseObject(taskContext.getJobParams());
            String accountPeriod = params.getString("accountPeriod");
            
            if (accountPeriod == null || accountPeriod.isEmpty()) {
                log.error("任务参数错误，缺少accountPeriod参数");
                return new ProcessResult(false, "任务参数错误，缺少accountPeriod参数");
            }
            
            // 按顺序执行四个任务
            // 1. 执行负债现金流汇总及现值计算任务
//            log.info("开始执行负债现金流汇总及现值计算任务");
//            boolean cashFlowResult = liabilityCashFlowSummaryTask.execute(accountPeriod);
//            if (!cashFlowResult) {
//                log.error("负债现金流汇总及现值计算失败");
//                return new ProcessResult(false, "负债现金流汇总及现值计算失败");
//            }
//            log.info("负债现金流汇总及现值计算成功");
            
            // 2. 执行负债久期计算任务
//            log.info("开始执行负债久期计算任务");
//            boolean durationResult = liabilityDurationSummaryTask.execute(accountPeriod);
//            if (!durationResult) {
//                log.error("负债久期计算失败");
//                return new ProcessResult(false, "负债久期计算失败");
//            }
//            log.info("负债久期计算成功");
            
//            // 3. 执行分账户负债现金流现值汇总任务
//            log.info("开始执行分账户负债现金流现值汇总任务");
//            boolean subAccountPresentValueResult = subAccountLiabilityPresentValueTask.execute(accountPeriod);
//            if (!subAccountPresentValueResult) {
//                log.error("分账户负债现金流现值汇总失败");
//                return new ProcessResult(false, "分账户负债现金流现值汇总失败");
//            }
//            log.info("分账户负债现金流现值汇总成功");
            
            // 4. 执行分账户负债久期汇总任务
            log.info("开始执行分账户负债久期汇总任务");
            boolean subAccountDurationResult = subAccountLiabilityDurationTask.execute(accountPeriod);
            if (!subAccountDurationResult) {
                log.error("分账户负债久期汇总失败");
                return new ProcessResult(false, "分账户负债久期汇总失败");
            }
            log.info("分账户负债久期汇总成功");
            
            // 所有任务执行成功
            return new ProcessResult(true, "负债计算处理器执行成功");
        } catch (Exception e) {
            log.error("负债计算处理器执行异常", e);
            return new ProcessResult(false, "负债计算处理器执行异常：" + e.getMessage());
        }
    }

//    @PostConstruct
//    public void init() {
//        TaskContext tc = new TaskContext();
//        tc.setJobParams("{\"accountPeriod\":\"202412\"}");
//        try {
//            process(tc);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
