package com.xl.alm.job.cost.service.impl;

import com.xl.alm.job.cost.entity.AccountSummaryEntity;
import com.xl.alm.job.cost.entity.BusinessTypeSummaryEntity;
import com.xl.alm.job.cost.mapper.AccountSummaryMapper;
import com.xl.alm.job.cost.mapper.BusinessTypeSummaryMapper;
import com.xl.alm.job.cost.service.AccountSummaryService;
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
 * 分账户汇总表(TB0009) 服务实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class AccountSummaryServiceImpl implements AccountSummaryService {

    @Autowired
    private AccountSummaryMapper accountSummaryMapper;

    @Autowired
    private BusinessTypeSummaryMapper businessTypeSummaryMapper;

    /**
     * 计算分账户汇总数据
     *
     * @param accountingPeriod 账期
     * @return 处理结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateAccountSummary(String accountingPeriod) {
        log.info("开始执行分账户汇总数据计算，账期：{}", accountingPeriod);
        try {
            // 步骤1：加载分业务类型汇总数据
            List<BusinessTypeSummaryEntity> businessTypeSummaryList = businessTypeSummaryMapper.selectBusinessTypeSummaryListByPeriod(accountingPeriod);
            log.info("加载分业务类型汇总数据完成，共加载{}条记录", businessTypeSummaryList.size());

            // 步骤2：分账户汇总
            List<AccountSummaryEntity> accountSummaryEntities = aggregateByAccount(businessTypeSummaryList);
            log.info("分账户汇总完成，共汇总{}条记录", accountSummaryEntities.size());

            // 步骤3：计算普通账户及公司整体数据
            List<AccountSummaryEntity> normalAccountEntities = calculateNormalAccount(accountSummaryEntities);
            log.info("计算普通账户及公司整体数据完成，共计算{}条记录", normalAccountEntities.size());

            // 步骤4：数据入表
            // 先删除已有数据
            accountSummaryMapper.deleteAccountSummaryByPeriod(accountingPeriod);
            // 批量插入新数据
            List<AccountSummaryEntity> allEntities = new ArrayList<>(accountSummaryEntities);
            allEntities.addAll(normalAccountEntities);
            if (!allEntities.isEmpty()) {
                accountSummaryMapper.batchInsertAccountSummary(allEntities);
            }
            log.info("分账户汇总数据计算任务执行完成，账期：{}", accountingPeriod);
            return true;
        } catch (Exception e) {
            log.error("分账户汇总数据计算任务执行异常，账期：{}", accountingPeriod, e);
            throw e;
        }
    }

    /**
     * 按情景名称、账期、设计类型维度对数据进行分组汇总
     *
     * @param businessTypeSummaryList 分业务类型汇总列表
     * @return 分账户汇总列表
     */
    private List<AccountSummaryEntity> aggregateByAccount(List<BusinessTypeSummaryEntity> businessTypeSummaryList) {
        List<AccountSummaryEntity> result = new ArrayList<>();

        // 按情景名称、账期、设计类型分组
        Map<String, List<BusinessTypeSummaryEntity>> groupedData = businessTypeSummaryList.stream()
                .collect(Collectors.groupingBy(item ->
                        item.getScenarioName() + "|" +
                        item.getAccountingPeriod() + "|" +
                        item.getDesignType()));

        // 对每个分组执行汇总计算
        for (Map.Entry<String, List<BusinessTypeSummaryEntity>> entry : groupedData.entrySet()) {
            String[] keys = entry.getKey().split("\\|");
            String scenarioName = keys[0];
            String accountingPeriod = keys[1];
            String designType = keys[2];
            List<BusinessTypeSummaryEntity> groupItems = entry.getValue();

            // 创建汇总实体
            AccountSummaryEntity summaryEntity = new AccountSummaryEntity();
            summaryEntity.setScenarioName(scenarioName);
            summaryEntity.setAccountingPeriod(accountingPeriod);
            summaryEntity.setDesignType(designType);
            summaryEntity.setIsDel(0);

            // 计算法定准备金汇总值
            BigDecimal statutoryReserveT0Sum = groupItems.stream()
                    .map(BusinessTypeSummaryEntity::getStatutoryReserveT0)
                    .filter(val -> val != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            summaryEntity.setStatutoryReserveT0(statutoryReserveT0Sum);

            BigDecimal statutoryReserveT1Sum = groupItems.stream()
                    .map(BusinessTypeSummaryEntity::getStatutoryReserveT1)
                    .filter(val -> val != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            summaryEntity.setStatutoryReserveT1(statutoryReserveT1Sum);

            BigDecimal statutoryReserveT2Sum = groupItems.stream()
                    .map(BusinessTypeSummaryEntity::getStatutoryReserveT2)
                    .filter(val -> val != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            summaryEntity.setStatutoryReserveT2(statutoryReserveT2Sum);

            BigDecimal statutoryReserveT3Sum = groupItems.stream()
                    .map(BusinessTypeSummaryEntity::getStatutoryReserveT3)
                    .filter(val -> val != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            summaryEntity.setStatutoryReserveT3(statutoryReserveT3Sum);

            // 计算资金成本率加权平均值
            summaryEntity.setFundCostRateT0(calculateWeightedAverage(
                    groupItems,
                    BusinessTypeSummaryEntity::getStatutoryReserveT0,
                    BusinessTypeSummaryEntity::getFundCostRateT0));

            summaryEntity.setFundCostRateT1(calculateWeightedAverage(
                    groupItems,
                    BusinessTypeSummaryEntity::getStatutoryReserveT1,
                    BusinessTypeSummaryEntity::getFundCostRateT1));

            summaryEntity.setFundCostRateT2(calculateWeightedAverage(
                    groupItems,
                    BusinessTypeSummaryEntity::getStatutoryReserveT2,
                    BusinessTypeSummaryEntity::getFundCostRateT2));

            summaryEntity.setFundCostRateT3(calculateWeightedAverage(
                    groupItems,
                    BusinessTypeSummaryEntity::getStatutoryReserveT3,
                    BusinessTypeSummaryEntity::getFundCostRateT3));

            // 计算保证成本率加权平均值
            summaryEntity.setGuaranteedCostRateT0(calculateWeightedAverage(
                    groupItems,
                    BusinessTypeSummaryEntity::getStatutoryReserveT0,
                    BusinessTypeSummaryEntity::getGuaranteedCostRateT0));

            summaryEntity.setGuaranteedCostRateT1(calculateWeightedAverage(
                    groupItems,
                    BusinessTypeSummaryEntity::getStatutoryReserveT1,
                    BusinessTypeSummaryEntity::getGuaranteedCostRateT1));

            summaryEntity.setGuaranteedCostRateT2(calculateWeightedAverage(
                    groupItems,
                    BusinessTypeSummaryEntity::getStatutoryReserveT2,
                    BusinessTypeSummaryEntity::getGuaranteedCostRateT2));

            summaryEntity.setGuaranteedCostRateT3(calculateWeightedAverage(
                    groupItems,
                    BusinessTypeSummaryEntity::getStatutoryReserveT3,
                    BusinessTypeSummaryEntity::getGuaranteedCostRateT3));

            result.add(summaryEntity);
        }

        return result;
    }

    /**
     * 计算普通账户及公司整体数据
     *
     * @param accountSummaryEntities 分账户汇总列表
     * @return 普通账户及公司整体数据列表
     */
    private List<AccountSummaryEntity> calculateNormalAccount(List<AccountSummaryEntity> accountSummaryEntities) {
        List<AccountSummaryEntity> result = new ArrayList<>();

        // 按情景名称、账期分组
        Map<String, List<AccountSummaryEntity>> groupedData = accountSummaryEntities.stream()
                .collect(Collectors.groupingBy(item ->
                        item.getScenarioName() + "|" +
                        item.getAccountingPeriod()));

        // 对每个分组执行汇总计算
        for (Map.Entry<String, List<AccountSummaryEntity>> entry : groupedData.entrySet()) {
            String[] keys = entry.getKey().split("\\|");
            String scenarioName = keys[0];
            String accountingPeriod = keys[1];
            List<AccountSummaryEntity> groupItems = entry.getValue();

            // 创建普通账户汇总实体
            AccountSummaryEntity normalAccountEntity = new AccountSummaryEntity();
            normalAccountEntity.setScenarioName(scenarioName);
            normalAccountEntity.setAccountingPeriod(accountingPeriod);
            normalAccountEntity.setDesignType("05"); // 普通账户
            normalAccountEntity.setIsDel(0);

            // 计算法定准备金汇总值
            BigDecimal statutoryReserveT0Sum = groupItems.stream()
                    .map(AccountSummaryEntity::getStatutoryReserveT0)
                    .filter(val -> val != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            normalAccountEntity.setStatutoryReserveT0(statutoryReserveT0Sum);

            // 计算资金成本率加权平均值
            normalAccountEntity.setFundCostRateT0(calculateWeightedAverage(
                    groupItems,
                    AccountSummaryEntity::getStatutoryReserveT0,
                    AccountSummaryEntity::getFundCostRateT0));

            // 计算保证成本率加权平均值
            normalAccountEntity.setGuaranteedCostRateT0(calculateWeightedAverage(
                    groupItems,
                    AccountSummaryEntity::getStatutoryReserveT0,
                    AccountSummaryEntity::getGuaranteedCostRateT0));

            // 其他T1、T2、T3值默认为0
            normalAccountEntity.setStatutoryReserveT1(BigDecimal.ZERO);
            normalAccountEntity.setStatutoryReserveT2(BigDecimal.ZERO);
            normalAccountEntity.setStatutoryReserveT3(BigDecimal.ZERO);
            normalAccountEntity.setFundCostRateT1(BigDecimal.ZERO);
            normalAccountEntity.setFundCostRateT2(BigDecimal.ZERO);
            normalAccountEntity.setFundCostRateT3(BigDecimal.ZERO);
            normalAccountEntity.setGuaranteedCostRateT1(BigDecimal.ZERO);
            normalAccountEntity.setGuaranteedCostRateT2(BigDecimal.ZERO);
            normalAccountEntity.setGuaranteedCostRateT3(BigDecimal.ZERO);

            result.add(normalAccountEntity);
        }

        return result;
    }

    /**
     * 计算加权平均值
     *
     * @param items 数据项列表
     * @param weightGetter 权重获取函数
     * @param valueGetter 值获取函数
     * @return 加权平均值
     */
    private <T> BigDecimal calculateWeightedAverage(
            List<T> items,
            java.util.function.Function<T, BigDecimal> weightGetter,
            java.util.function.Function<T, BigDecimal> valueGetter) {

        BigDecimal weightSum = BigDecimal.ZERO;
        BigDecimal weightedValueSum = BigDecimal.ZERO;

        for (T item : items) {
            BigDecimal weight = weightGetter.apply(item);
            BigDecimal value = valueGetter.apply(item);

            if (weight != null && value != null && weight.compareTo(BigDecimal.ZERO) > 0) {
                weightSum = weightSum.add(weight);
                weightedValueSum = weightedValueSum.add(weight.multiply(value));
            }
        }

        if (weightSum.compareTo(BigDecimal.ZERO) > 0) {
            return weightedValueSum.divide(weightSum, 10, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }
}
