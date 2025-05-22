package com.xl.alm.job.cost.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xl.alm.job.cost.entity.AccountEffectiveRateEntity;
import com.xl.alm.job.cost.entity.AccountSummaryEntity;
import com.xl.alm.job.cost.entity.ProductEffectiveRateEntity;
import com.xl.alm.job.cost.mapper.AccountEffectiveRateMapper;
import com.xl.alm.job.cost.mapper.AccountSummaryMapper;
import com.xl.alm.job.cost.mapper.ProductEffectiveRateMapper;
import com.xl.alm.job.cost.service.AccountEffectiveRateService;
import com.xl.alm.job.cost.util.IRRCalculator;
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
import java.util.stream.Collectors;

/**
 * 分账户有效成本率表(UC0012) 服务实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class AccountEffectiveRateServiceImpl implements AccountEffectiveRateService {

    @Autowired
    private AccountEffectiveRateMapper accountEffectiveRateMapper;

    @Autowired
    private AccountSummaryMapper accountSummaryMapper;

    @Autowired
    private ProductEffectiveRateMapper productEffectiveRateMapper;

    // 设计类型常量
    private static final String DESIGN_TYPE_TRADITIONAL = "01"; // 传统险
    private static final String DESIGN_TYPE_DIVIDEND = "02";    // 分红险
    private static final String DESIGN_TYPE_UNIVERSAL = "03";   // 万能险
    private static final String DESIGN_TYPE_INVESTMENT = "04";  // 投连险
    private static final String DESIGN_TYPE_NORMAL_ACCOUNT = "05"; // 普通账户

    /**
     * 计算分账户有效成本率数据
     *
     * @param accountingPeriod 账期
     * @return 处理结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateAccountEffectiveRate(String accountingPeriod) {
        log.info("开始执行分账户有效成本率数据计算，账期：{}", accountingPeriod);
        try {
            // 步骤1：加载分账户汇总数据
            List<AccountSummaryEntity> accountSummaryList = loadAccountSummaryList(accountingPeriod);
            log.info("加载分账户汇总数据完成，共加载{}条记录", accountSummaryList.size());

            // 步骤2：加载分产品有效成本率数据
            List<ProductEffectiveRateEntity> productEffectiveRateList = loadProductEffectiveRateList(accountingPeriod);
            log.info("加载分产品有效成本率数据完成，共加载{}条记录", productEffectiveRateList.size());

            // 步骤3：计算分账户有效成本率数据
            List<AccountEffectiveRateEntity> accountEffectiveRateList = calculateAccountEffectiveRateData(
                    accountingPeriod, accountSummaryList, productEffectiveRateList);
            log.info("计算分账户有效成本率数据完成，共计算{}条记录", accountEffectiveRateList.size());

            // 步骤4：数据入表
            // 先删除已有数据
            accountEffectiveRateMapper.deleteAccountEffectiveRateByPeriod(accountingPeriod);
            // 批量插入新数据
            if (!accountEffectiveRateList.isEmpty()) {
                accountEffectiveRateMapper.batchInsertAccountEffectiveRate(accountEffectiveRateList);
            }
            log.info("分账户有效成本率数据计算任务执行完成，账期：{}", accountingPeriod);
            return true;
        } catch (Exception e) {
            log.error("分账户有效成本率数据计算任务执行异常，账期：{}", accountingPeriod, e);
            throw e;
        }
    }

    /**
     * 加载分账户汇总数据
     *
     * @param accountingPeriod 账期
     * @return 分账户汇总列表
     */
    private List<AccountSummaryEntity> loadAccountSummaryList(String accountingPeriod) {
        return accountSummaryMapper.selectAccountSummaryListByPeriod(accountingPeriod);
    }

    /**
     * 加载分产品有效成本率数据
     *
     * @param accountingPeriod 账期
     * @return 分产品有效成本率列表
     */
    private List<ProductEffectiveRateEntity> loadProductEffectiveRateList(String accountingPeriod) {
        return productEffectiveRateMapper.selectProductEffectiveRateListByPeriod(accountingPeriod);
    }

    /**
     * 计算分账户有效成本率数据
     *
     * @param accountingPeriod 账期
     * @param accountSummaryList 分账户汇总列表
     * @param productEffectiveRateList 分产品有效成本率列表
     * @return 分账户有效成本率列表
     */
    private List<AccountEffectiveRateEntity> calculateAccountEffectiveRateData(
            String accountingPeriod,
            List<AccountSummaryEntity> accountSummaryList,
            List<ProductEffectiveRateEntity> productEffectiveRateList) {

        List<AccountEffectiveRateEntity> result = new ArrayList<>();

        // 按设计类型分组处理
        Map<String, List<AccountSummaryEntity>> accountSummaryMap = accountSummaryList.stream()
                .collect(Collectors.groupingBy(AccountSummaryEntity::getDesignType));

        // 按设计类型分组处理产品有效成本率数据
        Map<String, List<ProductEffectiveRateEntity>> productEffectiveRateMap = productEffectiveRateList.stream()
                .collect(Collectors.groupingBy(ProductEffectiveRateEntity::getDesignType));

        // 处理传统险账户
        processAccountType(result, accountingPeriod, DESIGN_TYPE_TRADITIONAL,
                accountSummaryMap.getOrDefault(DESIGN_TYPE_TRADITIONAL, new ArrayList<>()),
                productEffectiveRateMap.getOrDefault(DESIGN_TYPE_TRADITIONAL, new ArrayList<>()));

        // 处理分红险账户
        processAccountType(result, accountingPeriod, DESIGN_TYPE_DIVIDEND,
                accountSummaryMap.getOrDefault(DESIGN_TYPE_DIVIDEND, new ArrayList<>()),
                productEffectiveRateMap.getOrDefault(DESIGN_TYPE_DIVIDEND, new ArrayList<>()));

        // 处理万能险账户
        processAccountType(result, accountingPeriod, DESIGN_TYPE_UNIVERSAL,
                accountSummaryMap.getOrDefault(DESIGN_TYPE_UNIVERSAL, new ArrayList<>()),
                productEffectiveRateMap.getOrDefault(DESIGN_TYPE_UNIVERSAL, new ArrayList<>()));

        // 处理投连险账户
        processAccountType(result, accountingPeriod, DESIGN_TYPE_INVESTMENT,
                accountSummaryMap.getOrDefault(DESIGN_TYPE_INVESTMENT, new ArrayList<>()),
                productEffectiveRateMap.getOrDefault(DESIGN_TYPE_INVESTMENT, new ArrayList<>()));

        // 处理普通账户
        processNormalAccount(result, accountingPeriod,
                accountSummaryMap.getOrDefault(DESIGN_TYPE_NORMAL_ACCOUNT, new ArrayList<>()));

        return result;
    }

    /**
     * 处理特定设计类型的账户
     *
     * @param result 结果列表
     * @param accountingPeriod 账期
     * @param designType 设计类型
     * @param accountSummaryList 分账户汇总列表
     * @param productEffectiveRateList 分产品有效成本率列表
     */
    private void processAccountType(
            List<AccountEffectiveRateEntity> result,
            String accountingPeriod,
            String designType,
            List<AccountSummaryEntity> accountSummaryList,
            List<ProductEffectiveRateEntity> productEffectiveRateList) {

        if (accountSummaryList.isEmpty() || productEffectiveRateList.isEmpty()) {
            log.info("设计类型[{}]没有账户汇总数据或产品有效成本率数据，跳过处理", designType);
            return;
        }

        try {
            // 获取账户汇总数据
            AccountSummaryEntity accountSummary = accountSummaryList.get(0);
            BigDecimal accountValue = accountSummary.getStatutoryReserveT0();

            if (accountValue == null || accountValue.compareTo(BigDecimal.ZERO) <= 0) {
                log.warn("设计类型[{}]账面价值为空或小于等于0，跳过计算", designType);
                return;
            }

            // 汇总产品现金流
            Map<Integer, BigDecimal> aggregatedCashflows = aggregateProductCashflows(productEffectiveRateList, accountValue);

            // 构建现金流列表，第一个元素为账面价值的负值（初始投资）
            List<BigDecimal> irrCashflows = new ArrayList<>();
            irrCashflows.add(accountValue.negate()); // 初始投资为账面价值的负值

            // 添加后续现金流
            int maxPeriod = aggregatedCashflows.keySet().stream().max(Integer::compareTo).orElse(0);
            for (int i = 1; i <= maxPeriod; i++) {
                BigDecimal cashflow = aggregatedCashflows.getOrDefault(i, BigDecimal.ZERO);
                irrCashflows.add(cashflow);
            }

            // 计算IRR
            // 相当于Excel的IFERROR(XIRR(...), 0)函数
            BigDecimal effectiveCostRate = IRRCalculator.calculateIRRWithDefault(irrCashflows);
            log.info("设计类型[{}]IRR计算结果: {}", designType, effectiveCostRate);

            // 创建分账户有效成本率实体
            AccountEffectiveRateEntity entity = new AccountEffectiveRateEntity();
            entity.setAccountingPeriod(accountingPeriod);
            entity.setDesignType(designType);
            entity.setEffectiveCostRate(effectiveCostRate);

            // 将现金流集合转换为JSON字符串
            JSONObject cashflowJson = new JSONObject();
            cashflowJson.put("0", new JSONObject()
                    .fluentPut("日期", accountingPeriod)
                    .fluentPut("值", formatBigDecimal(accountValue.negate())));

            // 确保至少1272期
            maxPeriod = Math.max(maxPeriod, 1272);

            for (int i = 1; i <= maxPeriod; i++) {
                BigDecimal cashflow = aggregatedCashflows.getOrDefault(i, BigDecimal.ZERO);
                // 计算日期，每期增加一个月
                String date = calculateDate(accountingPeriod, i);
                cashflowJson.put(String.valueOf(i), new JSONObject()
                        .fluentPut("日期", date)
                        .fluentPut("值", formatBigDecimal(cashflow)));
            }
            entity.setCashFlowSet(cashflowJson.toString());

            result.add(entity);
            log.info("设计类型[{}]有效成本率计算完成，账面价值：{}，有效成本率：{}", designType, accountValue, effectiveCostRate);
        } catch (Exception e) {
            log.error("设计类型[{}]有效成本率计算异常", designType, e);
        }
    }

    /**
     * 格式化BigDecimal，避免使用科学计数法
     *
     * @param value BigDecimal值
     * @return 格式化后的字符串
     */
    private String formatBigDecimal(BigDecimal value) {
        if (value == null) {
            return "0";
        }
        // 使用纯文本格式，避免科学计数法
        return value.toPlainString();
    }

    /**
     * 处理普通账户
     *
     * @param result 结果列表
     * @param accountingPeriod 账期
     * @param accountSummaryList 分账户汇总列表
     */
    private void processNormalAccount(
            List<AccountEffectiveRateEntity> result,
            String accountingPeriod,
            List<AccountSummaryEntity> accountSummaryList) {

        if (accountSummaryList.isEmpty()) {
            log.info("普通账户没有账户汇总数据，跳过处理");
            return;
        }

        try {
            // 获取账户汇总数据
            AccountSummaryEntity accountSummary = accountSummaryList.get(0);

            // 普通账户的有效成本率直接取资金成本率T0
            BigDecimal effectiveCostRate = accountSummary.getFundCostRateT0();

            if (effectiveCostRate == null) {
                log.warn("普通账户有效成本率为空，跳过");
                return;
            }

            // 创建分账户有效成本率实体
            AccountEffectiveRateEntity entity = new AccountEffectiveRateEntity();
            entity.setAccountingPeriod(accountingPeriod);
            entity.setDesignType(DESIGN_TYPE_NORMAL_ACCOUNT);
            entity.setEffectiveCostRate(effectiveCostRate);

            // 普通账户没有现金流数据，但仍然需要生成包含所有期间的JSON
            JSONObject cashflowJson = new JSONObject();
            cashflowJson.put("0", new JSONObject()
                    .fluentPut("日期", accountingPeriod)
                    .fluentPut("值", "0"));

            // 生成至1272期的数据，所有值都为0
            for (int i = 1; i <= 1272; i++) {
                String date = calculateDate(accountingPeriod, i);
                cashflowJson.put(String.valueOf(i), new JSONObject()
                        .fluentPut("日期", date)
                        .fluentPut("值", "0"));
            }
            entity.setCashFlowSet(cashflowJson.toString());

            result.add(entity);
            log.info("普通账户有效成本率计算完成，有效成本率：{}", effectiveCostRate);
        } catch (Exception e) {
            log.error("普通账户有效成本率计算异常", e);
        }
    }

    /**
     * 汇总产品现金流
     *
     * @param productEffectiveRateList 分产品有效成本率列表
     * @param totalAccountValue 总账面价值
     * @return 汇总后的现金流映射表，键为期数，值为现金流
     */
    private Map<Integer, BigDecimal> aggregateProductCashflows(
            List<ProductEffectiveRateEntity> productEffectiveRateList,
            BigDecimal totalAccountValue) {

        Map<Integer, BigDecimal> aggregatedCashflows = new HashMap<>();

        // 如果总账面价值为0，则返回空映射
        if (totalAccountValue.compareTo(BigDecimal.ZERO) <= 0) {
            return aggregatedCashflows;
        }

        // 处理每个产品的现金流
        for (ProductEffectiveRateEntity product : productEffectiveRateList) {
            // 由于实际表结构中没有bookValue列，我们假设每个产品的权重相同
            BigDecimal weight = BigDecimal.ONE.divide(new BigDecimal(productEffectiveRateList.size()), 10, RoundingMode.HALF_UP);

            // 解析产品现金流
            String cashflowSet = product.getCashflowSet();
            if (cashflowSet == null || cashflowSet.isEmpty()) {
                continue;
            }

            try {
                JSONObject cashflowJson = JSON.parseObject(cashflowSet);

                // 跳过第0期（初始投资）
                for (String periodStr : cashflowJson.keySet()) {
                    if ("0".equals(periodStr)) {
                        continue;
                    }

                    Integer period = Integer.parseInt(periodStr);
                    BigDecimal cashflow = new BigDecimal(cashflowJson.getString(periodStr));

                    // 按权重调整现金流
                    BigDecimal adjustedCashflow = cashflow.multiply(weight);

                    // 汇总到总现金流
                    aggregatedCashflows.put(period, aggregatedCashflows.getOrDefault(period, BigDecimal.ZERO)
                            .add(adjustedCashflow));
                }
            } catch (Exception e) {
                log.error("解析产品[{}]现金流异常", product.getActuarialCode(), e);
            }
        }

        return aggregatedCashflows;
    }

    /**
     * 计算日期
     *
     * @param accountingPeriod 账期，格式为YYYYMM
     * @param addMonths 增加的月数
     * @return 计算后的日期，格式为YYYY/MM/DD
     */
    private String calculateDate(String accountingPeriod, int addMonths) {
        if (accountingPeriod == null || accountingPeriod.length() != 6) {
            return "";
        }

        try {
            int year = Integer.parseInt(accountingPeriod.substring(0, 4));
            int month = Integer.parseInt(accountingPeriod.substring(4, 6));

            // 增加月数
            month += addMonths;
            while (month > 12) {
                year++;
                month -= 12;
            }

            // 获取月末日期
            int day = getLastDayOfMonth(year, month);

            return String.format("%04d/%02d/%02d", year, month, day);
        } catch (Exception e) {
            log.error("计算日期异常，账期：{}，增加月数：{}", accountingPeriod, addMonths, e);
            return "";
        }
    }

    /**
     * 获取月末日期
     *
     * @param year 年
     * @param month 月
     * @return 月末日期
     */
    private int getLastDayOfMonth(int year, int month) {
        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) ? 29 : 28;
            default:
                return 31;
        }
    }
}
