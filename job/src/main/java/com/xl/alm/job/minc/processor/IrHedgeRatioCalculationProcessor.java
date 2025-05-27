package com.xl.alm.job.minc.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xl.alm.job.common.util.ProcessResultUtil;
import com.xl.alm.job.minc.task.IrHedgeRatioCalculationTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

import javax.annotation.PostConstruct;

/**
 * 利率风险对冲率计算处理器
 * 对应UC0009：计算利率风险对冲率数据
 *
 * 处理流程：
 * 1. 从TB0001表(分部门最低资本明细表)读取特定项目编码的数据
 * 2. 按项目编码和账户编码分组汇总金额数据
 * 3. 计算利率风险敏感度和对冲率
 * 4. 将计算结果写入TB0009表(利率风险对冲率表)
 *
 * @author AI Assistant
 */
@Slf4j
@Component
public class IrHedgeRatioCalculationProcessor implements BasicProcessor {

    @Autowired
    private IrHedgeRatioCalculationTask irHedgeRatioCalculationTask;

    @Override
    public ProcessResult process(TaskContext taskContext) {
        log.info("利率风险对冲率计算处理器开始执行，任务参数：{}", taskContext.getJobParams());

        try {
            // 解析任务参数
            JSONObject params = JSON.parseObject(taskContext.getJobParams());
            String accountPeriod = params.getString("accountPeriod");

            if (accountPeriod == null || accountPeriod.isEmpty()) {
                log.error("任务参数错误，缺少accountPeriod参数");
                return ProcessResultUtil.fail("任务参数错误，缺少accountPeriod参数");
            }

            // 步骤1：执行利率风险对冲率计算任务
            log.info("步骤1：开始执行利率风险对冲率计算任务，账期：{}", accountPeriod);
            boolean result = irHedgeRatioCalculationTask.executeCalculation(accountPeriod);

            if (!result) {
                log.error("步骤1：利率风险对冲率计算失败，账期：{}", accountPeriod);
                return ProcessResultUtil.fail("利率风险对冲率计算失败");
            }
            log.info("步骤1：利率风险对冲率计算成功");

            log.info("利率风险对冲率计算处理器执行成功，账期：{}", accountPeriod);
            return ProcessResultUtil.success("利率风险对冲率计算成功");

        } catch (Exception e) {
            log.error("利率风险对冲率计算处理器执行异常", e);
            return ProcessResultUtil.fail("利率风险对冲率计算处理器执行异常：" + e.getMessage());
        }
    }

    /**
     * 用于本地测试的初始化方法
     */
    @PostConstruct
    public void init() {
         //本地测试时取消注释
         TaskContext tc = new TaskContext();
         tc.setJobParams("{\"accountPeriod\":\"202412\"}");
         try {
             process(tc);
         } catch (Exception e) {
             e.printStackTrace();
         }
    }
}
