package com.xl.alm.job.dur.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xl.alm.job.dur.task.SubAccountLiabilityDurationTask;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

/**
 * 分账户负债久期汇总处理器
 *
 * @author AI Assistant
 */
@Slf4j
@Component
public class SubAccountLiabilityDurationProcessor implements BasicProcessor {

    @Autowired
    private SubAccountLiabilityDurationTask subAccountLiabilityDurationTask;

    @Override
    public ProcessResult process(TaskContext taskContext) throws Exception {
        log.info("分账户负债久期汇总处理器开始执行，任务参数：{}", taskContext.getJobParams());
        
        try {
            // 解析任务参数
            JSONObject params = JSON.parseObject(taskContext.getJobParams());
            String accountPeriod = params.getString("accountPeriod");
            
            if (accountPeriod == null || accountPeriod.isEmpty()) {
                log.error("任务参数错误，缺少accountPeriod参数");
                return new ProcessResult(false, "任务参数错误，缺少accountPeriod参数");
            }
            
            // 执行任务
            boolean result = subAccountLiabilityDurationTask.execute(accountPeriod);
            
            if (result) {
                return new ProcessResult(true, "分账户负债久期汇总成功");
            } else {
                return new ProcessResult(false, "分账户负债久期汇总失败");
            }
        } catch (Exception e) {
            log.error("分账户负债久期汇总处理器执行异常", e);
            return new ProcessResult(false, "分账户负债久期汇总处理器执行异常：" + e.getMessage());
        }
    }

    @PostConstruct
    public void init() {
        TaskContext tc = new TaskContext();
        tc.setJobParams("{\"accountPeriod\":\"202412\"}");
        try {
            process(tc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
