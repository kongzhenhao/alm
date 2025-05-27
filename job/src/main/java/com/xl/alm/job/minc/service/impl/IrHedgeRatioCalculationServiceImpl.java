package com.xl.alm.job.minc.service.impl;

import com.xl.alm.job.minc.mapper.IrHedgeRatioCalculationMapper;
import com.xl.alm.job.minc.service.IrHedgeRatioCalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 利率风险对冲率计算服务实现类
 * 对应UC0009：计算利率风险对冲率数据
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class IrHedgeRatioCalculationServiceImpl implements IrHedgeRatioCalculationService {

    @Autowired
    private IrHedgeRatioCalculationMapper irHedgeRatioCalculationMapper;

    /**
     * 数据准备 - 读取TB0001表数据并按项目编码和账户编码分组汇总
     *
     * @param accountingPeriod 账期
     * @return 执行结果
     */
    @Override
    public boolean prepareData(String accountingPeriod) {
        try {
            log.info("开始数据准备，账期：{}", accountingPeriod);

            // 读取TB0001表中特定项目编码的数据
            List<Map<String, Object>> deptMincapData = irHedgeRatioCalculationMapper.selectDeptMincapDetailData(accountingPeriod);
            
            if (deptMincapData == null || deptMincapData.isEmpty()) {
                log.warn("未找到账期{}的分部门最低资本明细数据", accountingPeriod);
                return false;
            }

            log.info("成功读取{}条分部门最低资本明细数据", deptMincapData.size());
            return true;

        } catch (Exception e) {
            log.error("数据准备异常，账期：{}", accountingPeriod, e);
            return false;
        }
    }

    /**
     * 计算利率风险敏感度和对冲率
     *
     * @param accountingPeriod 账期
     * @return 执行结果
     */
    @Override
    @Transactional
    public boolean calculateHedgeRatios(String accountingPeriod) {
        try {
            log.info("开始计算利率风险敏感度和对冲率，账期：{}", accountingPeriod);

            // 先删除该账期的旧数据
            irHedgeRatioCalculationMapper.deleteByAccountingPeriod(accountingPeriod);

            // 读取汇总数据
            Map<String, Map<String, BigDecimal>> summaryData = getSummaryData(accountingPeriod);
            
            if (summaryData.isEmpty()) {
                log.warn("未找到账期{}的汇总数据", accountingPeriod);
                return false;
            }

            // 计算结果列表
            List<Map<String, Object>> results = new ArrayList<>();

            // 计算传统账户(AC001)、分红账户(AC002)、万能账户(AC003)
            String[] accounts = {"AC001", "AC002", "AC003"};
            for (String accountCode : accounts) {
                calculateAccountHedgeRatio(accountingPeriod, accountCode, summaryData, results);
            }

            // 计算普通账户(AC006) = 传统账户 + 分红账户 + 万能账户
            calculateGeneralAccountHedgeRatio(accountingPeriod, summaryData, results);

            // 批量插入结果
            if (!results.isEmpty()) {
                int insertCount = irHedgeRatioCalculationMapper.batchInsertHedgeRatios(results);
                log.info("成功插入{}条利率风险对冲率数据", insertCount);
            }

            return true;

        } catch (Exception e) {
            log.error("计算利率风险敏感度和对冲率异常，账期：{}", accountingPeriod, e);
            return false;
        }
    }

    /**
     * 获取汇总数据
     */
    private Map<String, Map<String, BigDecimal>> getSummaryData(String accountingPeriod) {
        List<Map<String, Object>> rawData = irHedgeRatioCalculationMapper.selectDeptMincapDetailData(accountingPeriod);
        Map<String, Map<String, BigDecimal>> summaryData = new HashMap<>();

        for (Map<String, Object> row : rawData) {
            String itemCode = (String) row.get("item_code");
            String accountCode = (String) row.get("account_code");
            BigDecimal amount = (BigDecimal) row.get("total_amount");

            summaryData.computeIfAbsent(itemCode, k -> new HashMap<>()).put(accountCode, amount);
        }

        return summaryData;
    }

    /**
     * 计算单个账户的对冲率
     */
    private void calculateAccountHedgeRatio(String accountingPeriod, String accountCode, 
                                          Map<String, Map<String, BigDecimal>> summaryData, 
                                          List<Map<String, Object>> results) {
        
        // 获取各情景下的金额
        BigDecimal aa001Amount = getAmount(summaryData, "AA001", accountCode);
        BigDecimal aa002Amount = getAmount(summaryData, "AA002", accountCode);
        BigDecimal pv001Amount = getAmount(summaryData, "PV001", accountCode);
        BigDecimal pv002Amount = getAmount(summaryData, "PV002", accountCode);

        // 计算资产敏感度：(AA利率下降情景 - AA基础情景) / AA基础情景
        BigDecimal assetSensitivity = calculateSensitivity(aa002Amount, aa001Amount, aa001Amount);
        
        // 计算负债敏感度：(PV利率下降情景 - PV基础情景) / PV基础情景
        BigDecimal liabilitySensitivity = calculateSensitivity(pv002Amount, pv001Amount, pv001Amount);
        
        // 计算对冲率：(AA利率下降情景 - AA基础情景) / (PV利率下降情景 - PV基础情景)
        BigDecimal hedgeRatio = calculateHedgeRatio(aa002Amount, aa001Amount, pv002Amount, pv001Amount);

        // 添加结果
        addResult(results, accountingPeriod, "利率风险资产敏感度", accountCode, assetSensitivity);
        addResult(results, accountingPeriod, "利率风险负债敏感度", accountCode, liabilitySensitivity);
        addResult(results, accountingPeriod, "利率风险对冲率", accountCode, hedgeRatio);
    }

    /**
     * 计算普通账户的对冲率
     */
    private void calculateGeneralAccountHedgeRatio(String accountingPeriod, 
                                                 Map<String, Map<String, BigDecimal>> summaryData, 
                                                 List<Map<String, Object>> results) {
        
        // 普通账户 = 传统账户 + 分红账户 + 万能账户
        String[] subAccounts = {"AC001", "AC002", "AC003"};
        
        BigDecimal aa001Total = BigDecimal.ZERO;
        BigDecimal aa002Total = BigDecimal.ZERO;
        BigDecimal pv001Total = BigDecimal.ZERO;
        BigDecimal pv002Total = BigDecimal.ZERO;

        for (String subAccount : subAccounts) {
            aa001Total = aa001Total.add(getAmount(summaryData, "AA001", subAccount));
            aa002Total = aa002Total.add(getAmount(summaryData, "AA002", subAccount));
            pv001Total = pv001Total.add(getAmount(summaryData, "PV001", subAccount));
            pv002Total = pv002Total.add(getAmount(summaryData, "PV002", subAccount));
        }

        // 计算普通账户的敏感度和对冲率
        BigDecimal assetSensitivity = calculateSensitivity(aa002Total, aa001Total, aa001Total);
        BigDecimal liabilitySensitivity = calculateSensitivity(pv002Total, pv001Total, pv001Total);
        BigDecimal hedgeRatio = calculateHedgeRatio(aa002Total, aa001Total, pv002Total, pv001Total);

        // 添加结果
        addResult(results, accountingPeriod, "利率风险资产敏感度", "AC006", assetSensitivity);
        addResult(results, accountingPeriod, "利率风险负债敏感度", "AC006", liabilitySensitivity);
        addResult(results, accountingPeriod, "利率风险对冲率", "AC006", hedgeRatio);
    }

    /**
     * 获取指定项目编码和账户编码的金额
     */
    private BigDecimal getAmount(Map<String, Map<String, BigDecimal>> summaryData, String itemCode, String accountCode) {
        Map<String, BigDecimal> itemData = summaryData.get(itemCode);
        if (itemData == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal amount = itemData.get(accountCode);
        return amount != null ? amount : BigDecimal.ZERO;
    }

    /**
     * 计算敏感度
     */
    private BigDecimal calculateSensitivity(BigDecimal scenarioAmount, BigDecimal baseAmount, BigDecimal denominator) {
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return scenarioAmount.subtract(baseAmount).divide(denominator, 4, RoundingMode.HALF_UP);
    }

    /**
     * 计算对冲率
     */
    private BigDecimal calculateHedgeRatio(BigDecimal aa002, BigDecimal aa001, BigDecimal pv002, BigDecimal pv001) {
        BigDecimal pvDiff = pv002.subtract(pv001);
        if (pvDiff.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return aa002.subtract(aa001).divide(pvDiff, 4, RoundingMode.HALF_UP);
    }

    /**
     * 添加计算结果
     */
    private void addResult(List<Map<String, Object>> results, String accountingPeriod, 
                          String itemName, String accountCode, BigDecimal value) {
        Map<String, Object> result = new HashMap<>();
        result.put("accounting_period", accountingPeriod);
        result.put("item_name", itemName);
        result.put("account_code", accountCode);
        result.put("sensitivity_rate", value);
        results.add(result);
    }
}
