package com.xl.alm.job.minc.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xl.alm.job.common.util.ProcessResultUtil;
import com.xl.alm.job.minc.task.MarginalCapitalCalculationTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

import javax.annotation.PostConstruct;

/**
 * 边际最低资本贡献率计算处理器
 * 对应UC0007：计算边际最低资本贡献率数据
 *
 * 处理流程：
 * 1. 从TB0002表(风险项目金额表)读取数据，获取再保后金额
 * 2. 从TB0004表(相关系数表)读取相关系数数据
 * 3. 从TB0005表(项目定义表)读取项目定义和公式
 * 4. 使用Aviator表达式引擎计算子风险层面边际最低资本贡献因子
 * 5. 将计算结果写入TB0006表(边际最低资本贡献率表)
 *
 * @author AI Assistant
 */
@Slf4j
@Component
public class MarginalCapitalCalculationProcessor implements BasicProcessor {

    @Autowired
    private MarginalCapitalCalculationTask marginalCapitalCalculationTask;

    @Override
    public ProcessResult process(TaskContext taskContext) {
        log.info("边际最低资本贡献率计算处理器开始执行，任务参数：{}", taskContext.getJobParams());

        try {
            // 解析任务参数
            JSONObject params = JSON.parseObject(taskContext.getJobParams());
            String accountPeriod = params.getString("accountPeriod");

            if (accountPeriod == null || accountPeriod.isEmpty()) {
                log.error("任务参数错误，缺少accountPeriod参数");
                return ProcessResultUtil.fail("任务参数错误，缺少accountPeriod参数");
            }

            // 步骤1：执行子风险层面边际最低资本贡献因子计算
            log.info("步骤1：开始执行子风险层面边际最低资本贡献因子计算，账期：{}", accountPeriod);
            boolean subRiskResult = marginalCapitalCalculationTask.executeSubRiskCalculation(accountPeriod);

            if (!subRiskResult) {
                log.error("步骤1：子风险层面边际最低资本贡献因子计算失败，账期：{}", accountPeriod);
                return ProcessResultUtil.fail("子风险层面边际最低资本贡献因子计算失败");
            }
            log.info("步骤1：子风险层面边际最低资本贡献因子计算成功");

            // 步骤2：执行公司层面边际最低资本贡献因子计算
            log.info("步骤2：开始执行公司层面边际最低资本贡献因子计算，账期：{}", accountPeriod);
            boolean companyResult = marginalCapitalCalculationTask.executeCompanyCalculation(accountPeriod);

            if (!companyResult) {
                log.error("步骤2：公司层面边际最低资本贡献因子计算失败，账期：{}", accountPeriod);
                return ProcessResultUtil.fail("公司层面边际最低资本贡献因子计算失败");
            }
            log.info("步骤2：公司层面边际最低资本贡献因子计算成功");

            log.info("边际最低资本贡献率计算处理器执行成功，账期：{}", accountPeriod);
            return ProcessResultUtil.success("边际最低资本贡献率计算成功");

        } catch (Exception e) {
            log.error("边际最低资本贡献率计算处理器执行异常", e);
            return ProcessResultUtil.fail("边际最低资本贡献率计算处理器执行异常：" + e.getMessage());
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
