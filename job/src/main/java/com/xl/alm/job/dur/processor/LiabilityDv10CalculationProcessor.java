package com.xl.alm.job.dur.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xl.alm.job.dur.task.KeyDurationDiscountCurveTask;
import com.xl.alm.job.dur.task.KeyDurationDiscountFactorsTask;
import com.xl.alm.job.dur.task.LiabilityDv10ByDurationTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

import javax.annotation.PostConstruct;

/**
 * 关键久期计算处理器
 * 按照设计文档中的步骤顺序执行计算：
 * 1. 计算关键久期折现曲线
 * 2. 计算关键久期折现因子
 * 3. 计算分中短负债基点价值DV10
 *
 * @author AI
 */
@Slf4j
@Component
public class LiabilityDv10CalculationProcessor implements BasicProcessor {

    @Autowired
    private KeyDurationDiscountCurveTask keyDurationDiscountCurveTask;

    @Autowired
    private KeyDurationDiscountFactorsTask keyDurationDiscountFactorsTask;

    @Autowired
    private LiabilityDv10ByDurationTask liabilityDv10ByDurationTask;

    @Override
    public ProcessResult process(TaskContext taskContext) {
        log.info("关键久期计算处理器开始执行，任务参数：{}", taskContext.getJobParams());

        try {
            // 解析任务参数
            JSONObject params = JSON.parseObject(taskContext.getJobParams());
            String accountPeriod = params.getString("accountPeriod");

            if (accountPeriod == null || accountPeriod.isEmpty()) {
                log.error("任务参数错误，缺少accountPeriod参数");
                return new ProcessResult(false, "任务参数错误，缺少accountPeriod参数");
            }

            // 步骤1：计算关键久期折现曲线
            log.info("步骤1：开始计算关键久期折现曲线");
            String curveResult = keyDurationDiscountCurveTask.calculateKeyDurationDiscountCurve(accountPeriod);
            if (!"success".equals(curveResult)) {
                log.error("计算关键久期折现曲线失败：{}", curveResult);
                return new ProcessResult(false, "计算关键久期折现曲线失败：" + curveResult);
            }
            log.info("步骤1：计算关键久期折现曲线成功");

            // 步骤2：计算关键久期折现因子
            log.info("步骤2：开始计算关键久期折现因子");
            String factorsResult = keyDurationDiscountFactorsTask.calculateKeyDurationDiscountFactors(accountPeriod);
            if (!"success".equals(factorsResult)) {
                log.error("计算关键久期折现因子失败：{}", factorsResult);
                return new ProcessResult(false, "计算关键久期折现因子失败：" + factorsResult);
            }
            log.info("步骤2：计算关键久期折现因子成功");

            //步骤3：计算分中短负债基点价值DV10
            log.info("步骤3：开始计算分中短负债基点价值DV10");
            String dv10Result = liabilityDv10ByDurationTask.calculateLiabilityDv10ByDuration(accountPeriod);
            if (!"success".equals(dv10Result)) {
                log.error("计算分中短负债基点价值DV10失败：{}", dv10Result);
                return new ProcessResult(false, "计算分中短负债基点价值DV10失败：" + dv10Result);
            }
            log.info("步骤3：计算分中短负债基点价值DV10成功");

            log.info("关键久期计算处理器执行成功");
            return new ProcessResult(true, "关键久期计算处理器执行成功");
        } catch (Exception e) {
            log.error("关键久期计算处理器执行异常", e);
            return new ProcessResult(false, "关键久期计算处理器执行异常：" + e.getMessage());
        }
    }

    /*@PostConstruct
    public void init() {
        TaskContext tc = new TaskContext();
        tc.setJobParams("{\"accountPeriod\":\"202412\"}");
        try {
            process(tc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
