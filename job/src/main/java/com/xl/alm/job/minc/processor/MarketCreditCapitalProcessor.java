package com.xl.alm.job.minc.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xl.alm.job.common.util.ProcessResultUtil;
import com.xl.alm.job.minc.task.MarketCreditCapitalTask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

import javax.annotation.PostConstruct;

/**
 * 市场及信用最低资本计算处理器
 * 对应UC0008：计算市场及信用最低资本数据
 *
 * 处理流程：
 * 1. 从TB0006表(边际最低资本贡献率表)读取公司层面边际最低资本贡献因子
 * 2. 从TB0007表(最低资本明细汇总表)读取各账户下的金额数据
 * 3. 从TB0005表(项目定义表)读取最低资本计算公式
 * 4. 使用Aviator表达式引擎计算各账户下的市场风险和信用风险最低资本
 * 5. 将计算结果写入TB0008表(市场及信用最低资本表)
 *
 * @author AI Assistant
 */
@Slf4j
@Component
public class MarketCreditCapitalProcessor implements BasicProcessor {

    @Autowired
    private MarketCreditCapitalTask marketCreditCapitalTask;

    @Override
    public ProcessResult process(TaskContext taskContext) {
        log.info("市场及信用最低资本计算处理器开始执行，任务参数：{}", taskContext.getJobParams());

        try {
            // 解析任务参数
            JSONObject params = JSON.parseObject(taskContext.getJobParams());
            String accountPeriod = params.getString("accountPeriod");

            if (accountPeriod == null || accountPeriod.isEmpty()) {
                log.error("任务参数错误，缺少accountPeriod参数");
                return ProcessResultUtil.fail("任务参数错误，缺少accountPeriod参数");
            }

            // 步骤1：执行市场及信用最低资本计算任务
            log.info("步骤1：开始执行市场及信用最低资本计算任务，账期：{}", accountPeriod);
            boolean result = marketCreditCapitalTask.executeCalculation(accountPeriod);

            if (!result) {
                log.error("步骤1：市场及信用最低资本计算失败，账期：{}", accountPeriod);
                return ProcessResultUtil.fail("市场及信用最低资本计算失败");
            }
            log.info("步骤1：市场及信用最低资本计算成功");

            log.info("市场及信用最低资本计算处理器执行成功，账期：{}", accountPeriod);
            return ProcessResultUtil.success("市场及信用最低资本计算成功");

        } catch (Exception e) {
            log.error("市场及信用最低资本计算处理器执行异常", e);
            return ProcessResultUtil.fail("市场及信用最低资本计算处理器执行异常：" + e.getMessage());
        }
    }

    /**
     * 用于本地测试的初始化方法
     */
    /*@PostConstruct
    public void init() {
        // 本地测试时取消注释
        TaskContext tc = new TaskContext();
        tc.setJobParams("{\"accountPeriod\":\"202412\"}");
        try {
            process(tc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
