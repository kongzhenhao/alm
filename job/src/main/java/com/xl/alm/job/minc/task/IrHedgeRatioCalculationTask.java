package com.xl.alm.job.minc.task;

import com.xl.alm.job.minc.service.IrHedgeRatioCalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 利率风险对冲率计算任务
 * 对应UC0009：计算利率风险对冲率数据
 *
 * @author AI Assistant
 */
@Slf4j
@Component
public class IrHedgeRatioCalculationTask {

    @Autowired
    private IrHedgeRatioCalculationService irHedgeRatioCalculationService;

    /**
     * 执行利率风险对冲率计算
     *
     * @param accountingPeriod 账期
     * @return 执行结果
     */
    public boolean executeCalculation(String accountingPeriod) {
        try {
            log.info("开始执行利率风险对冲率计算，账期：{}", accountingPeriod);

            // 步骤1：数据准备 - 读取TB0001表数据并按项目编码和账户编码分组汇总
            log.info("步骤1：数据准备 - 读取分部门最低资本明细表数据");
            boolean prepareResult = irHedgeRatioCalculationService.prepareData(accountingPeriod);
            if (!prepareResult) {
                log.error("数据准备失败，账期：{}", accountingPeriod);
                return false;
            }

            // 步骤2：计算利率风险敏感度和对冲率
            log.info("步骤2：计算利率风险敏感度和对冲率");
            boolean calculateResult = irHedgeRatioCalculationService.calculateHedgeRatios(accountingPeriod);
            if (!calculateResult) {
                log.error("利率风险敏感度和对冲率计算失败，账期：{}", accountingPeriod);
                return false;
            }

            log.info("利率风险对冲率计算完成，账期：{}", accountingPeriod);
            return true;

        } catch (Exception e) {
            log.error("利率风险对冲率计算异常，账期：{}", accountingPeriod, e);
            return false;
        }
    }
}
