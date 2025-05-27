package com.xl.alm.job.cost.service.impl;

import com.xl.alm.job.cost.entity.BusinessTypeSummaryEntity;
import com.xl.alm.job.cost.entity.ProductStatisticsEntity;
import com.xl.alm.job.cost.mapper.BusinessTypeSummaryMapper;
import com.xl.alm.job.cost.mapper.ProductStatisticsMapper;
import com.xl.alm.job.cost.service.BusinessTypeSummaryService;
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
 * 分业务类型汇总表(TB0008) 服务实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class BusinessTypeSummaryServiceImpl implements BusinessTypeSummaryService {

    @Autowired
    private BusinessTypeSummaryMapper businessTypeSummaryMapper;

    @Autowired
    private ProductStatisticsMapper productStatisticsMapper;

    /**
     * 计算分业务类型汇总数据
     *
     * @param accountingPeriod 账期
     * @return 处理结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateBusinessTypeSummary(String accountingPeriod) {
        log.info("开始执行分业务类型汇总数据计算，账期：{}", accountingPeriod);
        try {
            // 步骤1：加载分产品统计数据
            List<ProductStatisticsEntity> productStatisticsList = productStatisticsMapper.selectProductStatisticsListByPeriod(accountingPeriod);
            log.info("加载分产品统计数据完成，共加载{}条记录", productStatisticsList.size());

            // 步骤2：分业务类型汇总
            List<BusinessTypeSummaryEntity> businessTypeSummaryEntities = aggregateByBusinessType(productStatisticsList);
            log.info("分业务类型汇总完成，共汇总{}条记录", businessTypeSummaryEntities.size());

            // 步骤3：数据入表
            // 先删除已有数据
            businessTypeSummaryMapper.deleteBusinessTypeSummaryByPeriod(accountingPeriod);
            // 批量插入新数据
            if (!businessTypeSummaryEntities.isEmpty()) {
                businessTypeSummaryMapper.batchInsertBusinessTypeSummary(businessTypeSummaryEntities);
            }
            log.info("分业务类型汇总数据计算任务执行完成，账期：{}", accountingPeriod);
            return true;
        } catch (Exception e) {
            log.error("分业务类型汇总数据计算任务执行异常，账期：{}", accountingPeriod, e);
            throw e;
        }
    }

    /**
     * 按业务类型、情景名称、账期、设计类型维度对数据进行分组汇总
     *
     * @param productStatisticsList 分产品统计列表
     * @return 分业务类型汇总列表
     */
    private List<BusinessTypeSummaryEntity> aggregateByBusinessType(List<ProductStatisticsEntity> productStatisticsList) {
        List<BusinessTypeSummaryEntity> result = new ArrayList<>();
        
        // 按业务类型、情景名称、账期、设计类型分组
        Map<String, List<ProductStatisticsEntity>> groupedData = productStatisticsList.stream()
                .collect(Collectors.groupingBy(item -> 
                        item.getBusinessType() + "|" + 
                        item.getScenarioName() + "|" + 
                        item.getAccountingPeriod() + "|" + 
                        item.getDesignType()));
        
        // 对每个分组执行汇总计算
        for (Map.Entry<String, List<ProductStatisticsEntity>> entry : groupedData.entrySet()) {
            String[] keys = entry.getKey().split("\\|");
            String businessType = keys[0];
            String scenarioName = keys[1];
            String accountingPeriod = keys[2];
            String designType = keys[3];
            List<ProductStatisticsEntity> groupItems = entry.getValue();
            
            // 创建汇总实体
            BusinessTypeSummaryEntity summaryEntity = new BusinessTypeSummaryEntity();
            summaryEntity.setBusinessType(businessType);
            summaryEntity.setScenarioName(scenarioName);
            summaryEntity.setAccountingPeriod(accountingPeriod);
            summaryEntity.setDesignType(designType);
            summaryEntity.setIsDel(0);
            
            // 计算法定准备金汇总值
            BigDecimal statutoryReserveT0Sum = groupItems.stream()
                    .map(ProductStatisticsEntity::getStatutoryReserveT0)
                    .filter(val -> val != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            summaryEntity.setStatutoryReserveT0(statutoryReserveT0Sum);
            
            BigDecimal statutoryReserveT1Sum = groupItems.stream()
                    .map(ProductStatisticsEntity::getStatutoryReserveT1)
                    .filter(val -> val != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            summaryEntity.setStatutoryReserveT1(statutoryReserveT1Sum);
            
            BigDecimal statutoryReserveT2Sum = groupItems.stream()
                    .map(ProductStatisticsEntity::getStatutoryReserveT2)
                    .filter(val -> val != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            summaryEntity.setStatutoryReserveT2(statutoryReserveT2Sum);
            
            BigDecimal statutoryReserveT3Sum = groupItems.stream()
                    .map(ProductStatisticsEntity::getStatutoryReserveT3)
                    .filter(val -> val != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            summaryEntity.setStatutoryReserveT3(statutoryReserveT3Sum);
            
            // 计算资金成本率加权平均值
            summaryEntity.setFundCostRateT0(calculateWeightedAverage(
                    groupItems, 
                    ProductStatisticsEntity::getStatutoryReserveT0, 
                    ProductStatisticsEntity::getFundCostRateT0));
            
            summaryEntity.setFundCostRateT1(calculateWeightedAverage(
                    groupItems, 
                    ProductStatisticsEntity::getStatutoryReserveT1, 
                    ProductStatisticsEntity::getFundCostRateT1));
            
            summaryEntity.setFundCostRateT2(calculateWeightedAverage(
                    groupItems, 
                    ProductStatisticsEntity::getStatutoryReserveT2, 
                    ProductStatisticsEntity::getFundCostRateT2));
            
            summaryEntity.setFundCostRateT3(calculateWeightedAverage(
                    groupItems, 
                    ProductStatisticsEntity::getStatutoryReserveT3, 
                    ProductStatisticsEntity::getFundCostRateT3));
            
            // 计算保证成本率加权平均值
            summaryEntity.setGuaranteedCostRateT0(calculateWeightedAverage(
                    groupItems, 
                    ProductStatisticsEntity::getStatutoryReserveT0, 
                    ProductStatisticsEntity::getGuaranteedCostRateT0));
            
            summaryEntity.setGuaranteedCostRateT1(calculateWeightedAverage(
                    groupItems, 
                    ProductStatisticsEntity::getStatutoryReserveT1, 
                    ProductStatisticsEntity::getGuaranteedCostRateT1));
            
            summaryEntity.setGuaranteedCostRateT2(calculateWeightedAverage(
                    groupItems, 
                    ProductStatisticsEntity::getStatutoryReserveT2, 
                    ProductStatisticsEntity::getGuaranteedCostRateT2));
            
            summaryEntity.setGuaranteedCostRateT3(calculateWeightedAverage(
                    groupItems, 
                    ProductStatisticsEntity::getStatutoryReserveT3, 
                    ProductStatisticsEntity::getGuaranteedCostRateT3));
            
            result.add(summaryEntity);
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
    private BigDecimal calculateWeightedAverage(
            List<ProductStatisticsEntity> items,
            java.util.function.Function<ProductStatisticsEntity, BigDecimal> weightGetter,
            java.util.function.Function<ProductStatisticsEntity, BigDecimal> valueGetter) {
        
        BigDecimal weightSum = BigDecimal.ZERO;
        BigDecimal weightedValueSum = BigDecimal.ZERO;
        
        for (ProductStatisticsEntity item : items) {
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
