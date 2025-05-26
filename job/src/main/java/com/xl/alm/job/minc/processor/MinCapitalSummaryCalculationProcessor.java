package com.xl.alm.job.minc.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xl.alm.job.common.util.ProcessResultUtil;
import com.xl.alm.job.minc.task.MinCapitalSummaryCalculationTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

import javax.annotation.PostConstruct;

/**
 * 最低资本明细汇总数据计算处理器
 * 对应UC0006：计算最低资本明细汇总数据
 *
 * 处理流程：
 * 1. 从TB0001表(分部门最低资本明细表)读取指定账期的数据
 * 2. 根据项目定义表中的风险类型过滤，只计算风险类型为"市场风险"和"信用风险"的项目
 * 3. 按账期、项目编码、账户编码分组汇总amount值
 * 4. 将汇总结果写入TB0007表(最低资本明细汇总表)
 *
 * @author AI Assistant
 */
@Slf4j
@Component
public class MinCapitalSummaryCalculationProcessor implements BasicProcessor {

    @Autowired
    private MinCapitalSummaryCalculationTask minCapitalSummaryCalculationTask;

    @Override
    public ProcessResult process(TaskContext taskContext) {
        log.info("最低资本明细汇总数据计算处理器开始执行，任务参数：{}", taskContext.getJobParams());

        try {
            // 解析任务参数
            JSONObject params = JSON.parseObject(taskContext.getJobParams());
            String accountPeriod = params.getString("accountPeriod");

            if (accountPeriod == null || accountPeriod.isEmpty()) {
                log.error("任务参数错误，缺少accountPeriod参数");
                return ProcessResultUtil.fail("任务参数错误，缺少accountPeriod参数");
            }

            // 步骤1：执行最低资本明细汇总数据计算任务
            log.info("步骤1：开始执行最低资本明细汇总数据计算任务，账期：{}", accountPeriod);
            boolean result = minCapitalSummaryCalculationTask.execute(accountPeriod);

            if (!result) {
                log.error("步骤1：最低资本明细汇总数据计算失败，账期：{}", accountPeriod);
                return ProcessResultUtil.fail("最低资本明细汇总数据计算失败");
            }
            log.info("步骤1：最低资本明细汇总数据计算成功");

            log.info("最低资本明细汇总数据计算处理器执行成功，账期：{}", accountPeriod);
            return ProcessResultUtil.success("最低资本明细汇总数据计算成功");

        } catch (Exception e) {
            log.error("最低资本明细汇总数据计算处理器执行异常", e);
            return ProcessResultUtil.fail("最低资本明细汇总数据计算处理器执行异常：" + e.getMessage());
        }
    }

    /**
     * 用于本地测试的初始化方法
     */
    @PostConstruct
    public void init() {
        // 本地测试时取消注释
        TaskContext tc = new TaskContext();
        tc.setJobParams("{\"accountPeriod\":\"202412\"}");
        try {
            process(tc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
