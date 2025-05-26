package com.xl.alm.job.dur.processor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xl.alm.job.common.util.ProcessResultUtil;
import com.xl.alm.job.dur.entity.LiabilityDv10ByDurationEntity;
import com.xl.alm.job.dur.mapper.LiabilityDv10ByDurationMapper;
import com.xl.alm.job.dur.task.AccountLiabilityDv10Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 分账户负债基点价值DV10计算处理器
 * 按照设计文档中的步骤顺序执行计算：
 * 1. 验证分中短负债基点价值DV10数据是否存在
 * 2. 计算分账户负债基点价值DV10
 *
 * @author AI
 */
@Slf4j
@Component
public class AccountLiabilityDv10Processor implements BasicProcessor {

    @Autowired
    private LiabilityDv10ByDurationMapper liabilityDv10ByDurationMapper;

    @Autowired
    private AccountLiabilityDv10Task accountLiabilityDv10Task;

    @Override
    public ProcessResult process(TaskContext taskContext) {
        log.info("分账户负债基点价值DV10计算处理器开始执行，任务参数：{}", taskContext.getJobParams());

        try {
            // 解析任务参数
            JSONObject params = JSON.parseObject(taskContext.getJobParams());
            String accountPeriod = params.getString("accountPeriod");

            if (accountPeriod == null || accountPeriod.isEmpty()) {
                log.error("任务参数错误，缺少accountPeriod参数");
                return ProcessResultUtil.fail("任务参数错误，缺少accountPeriod参数");
            }

            // 1. 验证分中短负债基点价值DV10数据是否存在
            log.info("步骤1：开始验证分中短负债基点价值DV10数据是否存在，账期：{}", accountPeriod);
            // 这里不再计算分中短负债基点价值DV10，而是直接验证数据是否存在
            // 根据设计文档，我们应该直接读取TB0006表(分中短负债基点价值DV10)数据
            List<LiabilityDv10ByDurationEntity> dataList = liabilityDv10ByDurationMapper.selectByAccountPeriod(accountPeriod);
            if (dataList == null || dataList.isEmpty()) {
                log.error("分中短负债基点价值DV10数据不存在，账期：{}", accountPeriod);
                return ProcessResultUtil.fail("分中短负债基点价值DV10数据不存在，请先计算分中短负债基点价值DV10");
            }
            log.info("步骤1：验证分中短负债基点价值DV10数据存在成功，共找到{}条记录", dataList.size());

            // 2. 计算分账户负债基点价值DV10
            log.info("步骤2：开始计算分账户负债基点价值DV10，账期：{}", accountPeriod);
            boolean accountLiabilityDv10Result = accountLiabilityDv10Task.calculateAccountLiabilityDv10(accountPeriod);
            if (!accountLiabilityDv10Result) {
                log.error("计算分账户负债基点价值DV10失败，账期：{}", accountPeriod);
                return ProcessResultUtil.fail("计算分账户负债基点价值DV10失败");
            }
            log.info("步骤2：计算分账户负债基点价值DV10成功");

            log.info("分账户负债基点价值DV10计算处理器执行成功");
            return ProcessResultUtil.success("分账户负债基点价值DV10计算处理器执行成功");
        } catch (Exception e) {
            log.error("分账户负债基点价值DV10计算处理器执行异常", e);
            return ProcessResultUtil.fail("分账户负债基点价值DV10计算处理器执行异常：" + e.getMessage());
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
