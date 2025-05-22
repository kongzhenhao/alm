package com.xl.alm.job.cost.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xl.alm.job.cost.entity.AccountingReserveDetailEntity;
import com.xl.alm.job.cost.entity.ProductAttributeEntity;
import com.xl.alm.job.cost.entity.ProductEffectiveRateEntity;
import com.xl.alm.job.cost.mapper.AccountingReserveDetailMapper;
import com.xl.alm.job.cost.mapper.ProductAttributeMapper;
import com.xl.alm.job.cost.mapper.ProductEffectiveRateMapper;
import com.xl.alm.job.cost.query.AccountingReserveDetailQuery;
import com.xl.alm.job.cost.service.ProductEffectiveRateService;
import com.xl.alm.job.cost.util.IRRCalculator;
import com.xl.alm.job.dur.entity.LiabilityCashFlowEntity;
import com.xl.alm.job.dur.mapper.LiabilityCashFlowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分产品有效成本率表(UC0011) 服务实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class ProductEffectiveRateServiceImpl implements ProductEffectiveRateService {

    @Autowired
    private ProductEffectiveRateMapper productEffectiveRateMapper;

    @Autowired
    private AccountingReserveDetailMapper accountingReserveDetailMapper;

    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    @Autowired
    private LiabilityCashFlowMapper liabilityCashFlowMapper;

    // 设计类型常量
    private static final String DESIGN_TYPE_TRADITIONAL = "01"; // 传统险
    private static final String DESIGN_TYPE_DIVIDEND = "02";    // 分红险
    private static final String DESIGN_TYPE_UNIVERSAL = "03";   // 万能险

    // 长短期标识常量
    private static final String TERM_TYPE_LONG = "L"; // 长期
    private static final String TERM_TYPE_SHORT = "S"; // 短期

    // 是否中短常量
    private static final String SHORT_TERM_FLAG_YES = "Y"; // 是
    private static final String SHORT_TERM_FLAG_NO = "N";  // 否

    // 基点类型常量
    private static final String BP_TYPE_ZERO = "03"; // 0bp

    // 现金流类型常量
    private static final String CASHFLOW_TYPE_IN = "01";  // 流入
    private static final String CASHFLOW_TYPE_OUT = "02"; // 流出

    /**
     * 计算分产品有效成本率数据
     *
     * @param accountingPeriod 账期
     * @return 处理结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateProductEffectiveRate(String accountingPeriod) {
        log.info("开始执行分产品有效成本率数据计算，账期：{}", accountingPeriod);
        try {
            // 步骤1：加载产品属性数据
            List<ProductAttributeEntity> productAttributeList = loadProductAttributes(accountingPeriod);
            log.info("加载产品属性数据完成，共加载{}条记录", productAttributeList.size());

            // 步骤2：加载会计准备金明细数据
            List<AccountingReserveDetailEntity> accountingReserveDetailList = loadAccountingReserveDetails(accountingPeriod);
            log.info("加载会计准备金明细数据完成，共加载{}条记录", accountingReserveDetailList.size());

            // 步骤3：计算分产品有效成本率数据
            List<ProductEffectiveRateEntity> productEffectiveRateList = calculateProductEffectiveRateData(accountingPeriod, productAttributeList, accountingReserveDetailList);
            log.info("计算分产品有效成本率数据完成，共计算{}条记录", productEffectiveRateList.size());

            // 步骤3：数据入表
            // 先删除已有数据
            productEffectiveRateMapper.deleteProductEffectiveRateByPeriod(accountingPeriod);
            // 批量插入新数据
            if (!productEffectiveRateList.isEmpty()) {
                productEffectiveRateMapper.batchInsertProductEffectiveRate(productEffectiveRateList);
            }
            log.info("分产品有效成本率数据计算任务执行完成，账期：{}", accountingPeriod);
            return true;
        } catch (Exception e) {
            log.error("分产品有效成本率数据计算任务执行异常，账期：{}", accountingPeriod, e);
            throw e;
        }
    }

    /**
     * 加载产品属性数据
     *
     * @param accountingPeriod 账期
     * @return 产品属性列表
     */
    private List<ProductAttributeEntity> loadProductAttributes(String accountingPeriod) {
        ProductAttributeEntity query = new ProductAttributeEntity();
        query.setAccountingPeriod(accountingPeriod);
        query.setTermType(TERM_TYPE_LONG); // 只查询长期产品

        // 获取所有产品属性数据
        List<ProductAttributeEntity> allProductAttributes = productAttributeMapper.selectProductAttributeList(query);

        // 筛选出传统险、分红险、万能险的产品
        return allProductAttributes.stream()
                .filter(product -> DESIGN_TYPE_TRADITIONAL.equals(product.getDesignType()) ||
                                  DESIGN_TYPE_DIVIDEND.equals(product.getDesignType()) ||
                                  DESIGN_TYPE_UNIVERSAL.equals(product.getDesignType()))
                .collect(Collectors.toList());
    }

    /**
     * 加载会计准备金明细数据
     *
     * @param accountingPeriod 账期
     * @return 会计准备金明细列表
     */
    private List<AccountingReserveDetailEntity> loadAccountingReserveDetails(String accountingPeriod) {
        AccountingReserveDetailQuery query = new AccountingReserveDetailQuery();
        query.setAccountingPeriod(accountingPeriod);
        return accountingReserveDetailMapper.selectAccountingReserveDetailList(query);
    }

    /**
     * 计算分产品有效成本率数据
     *
     * @param accountingPeriod 账期
     * @param productAttributeList 产品属性列表
     * @param accountingReserveDetailList 会计准备金明细列表
     * @return 分产品有效成本率列表
     */
    private List<ProductEffectiveRateEntity> calculateProductEffectiveRateData(String accountingPeriod,
            List<ProductAttributeEntity> productAttributeList,
            List<AccountingReserveDetailEntity> accountingReserveDetailList) {
        List<ProductEffectiveRateEntity> result = new ArrayList<>();

        // 构建会计准备金明细HashMap，以精算代码为键
        Map<String, AccountingReserveDetailEntity> accountingReserveDetailMap = accountingReserveDetailList.stream()
                .collect(Collectors.toMap(AccountingReserveDetailEntity::getActuarialCode, detail -> detail, (v1, v2) -> v1));

        // 加载负债现金流数据
        List<LiabilityCashFlowEntity> cashflowInList = loadLiabilityCashflows(accountingPeriod, null, BP_TYPE_ZERO, CASHFLOW_TYPE_IN);
        List<LiabilityCashFlowEntity> cashflowOutList = loadLiabilityCashflows(accountingPeriod, null, BP_TYPE_ZERO, CASHFLOW_TYPE_OUT);

        // 构建负债现金流HashMap，以精算代码为键
        Map<String, List<LiabilityCashFlowEntity>> cashflowInMap = cashflowInList.stream()
                .collect(Collectors.groupingBy(LiabilityCashFlowEntity::getActuarialCode));
        Map<String, List<LiabilityCashFlowEntity>> cashflowOutMap = cashflowOutList.stream()
                .collect(Collectors.groupingBy(LiabilityCashFlowEntity::getActuarialCode));

        // 遍历产品属性表中的产品记录
        for (ProductAttributeEntity product : productAttributeList) {
            // 获取产品基本信息
            String actuarialCode = product.getActuarialCode();
            String businessCode = product.getBusinessCode();
            String productName = product.getProductName();
            String designType = product.getDesignType();
            String shortTermFlag = product.getShortTermFlag();

            try {

                // 获取会计准备金明细数据
                AccountingReserveDetailEntity detail = accountingReserveDetailMap.get(actuarialCode);
                if (detail == null) {
                    log.warn("产品[{}]没有对应的会计准备金明细数据，跳过计算", actuarialCode);
                    continue;
                }

                // 计算账面价值 = 账户价值 + 最优估计 + 风险边际 + 剩余边际 + 持续奖准备金 + 零头月红利
                BigDecimal accountValue = detail.getAccountValue() != null ? detail.getAccountValue() : BigDecimal.ZERO;
                BigDecimal bestEstimate = detail.getBestEstimate() != null ? detail.getBestEstimate() : BigDecimal.ZERO;
                BigDecimal riskMargin = detail.getRiskMargin() != null ? detail.getRiskMargin() : BigDecimal.ZERO;
                BigDecimal residualMargin = detail.getResidualMargin() != null ? detail.getResidualMargin() : BigDecimal.ZERO;
                BigDecimal persistenceBonusReserve = detail.getPersistenceBonusReserve() != null ? detail.getPersistenceBonusReserve() : BigDecimal.ZERO;
                BigDecimal fractionalMonthDividend = detail.getFractionalMonthDividend() != null ? detail.getFractionalMonthDividend() : BigDecimal.ZERO;

                BigDecimal bookValue = accountValue
                        .add(bestEstimate)
                        .add(riskMargin)
                        .add(residualMargin)
                        .add(persistenceBonusReserve)
                        .add(fractionalMonthDividend);

               /* if (bookValue.compareTo(BigDecimal.ZERO) <= 0) {
                    log.warn("产品[{}]账面价值为空或小于等于0，跳过计算", actuarialCode);
                    continue;
                }*/

                // 获取产品的负债现金流数据
                List<LiabilityCashFlowEntity> productCashflowInList = cashflowInMap.getOrDefault(actuarialCode, new ArrayList<>());
                List<LiabilityCashFlowEntity> productCashflowOutList = cashflowOutMap.getOrDefault(actuarialCode, new ArrayList<>());

               /* if (productCashflowInList.isEmpty() && productCashflowOutList.isEmpty()) {
                    log.warn("产品[{}]没有负债现金流数据，跳过计算", actuarialCode);
                    continue;
                }*/

                // 构建现金流值集
                Map<Integer, BigDecimal> netCashflows = calculateNetCashflows(productCashflowInList, productCashflowOutList);

                // 检查未来期间现金流之和是否为0
                BigDecimal futureCashflowSum = netCashflows.values().stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal effectiveRate;
                if (futureCashflowSum.compareTo(BigDecimal.ZERO) == 0) {
                    // 如果未来期间现金流之和为0，有效成本率 = 0%
                    effectiveRate = BigDecimal.ZERO;
                    log.info("产品[{}]未来期间现金流之和为0，有效成本率设置为0%", actuarialCode);
                } else {
                    // 构建现金流列表，第一个元素为账面价值的负值（初始投资）
                    List<BigDecimal> irrCashflows = new ArrayList<>();
                    irrCashflows.add(bookValue.negate()); // 初始投资为账面价值的负值

                    // 添加后续现金流
                    int maxPeriod = netCashflows.keySet().stream().max(Integer::compareTo).orElse(0);
                    for (int i = 1; i <= maxPeriod; i++) {
                        BigDecimal cashflow = netCashflows.getOrDefault(i, BigDecimal.ZERO);
                        irrCashflows.add(cashflow);
                    }

                    // 使用内部收益率(IRR)方法计算有效成本率
                    // 相当于Excel的IFERROR(XIRR(...), 0)函数
                    effectiveRate = IRRCalculator.calculateIRRWithDefault(irrCashflows);
                    log.info("产品[{}]IRR计算结果: {}", actuarialCode, effectiveRate);
                }

                // 创建分产品有效成本率实体
                ProductEffectiveRateEntity entity = new ProductEffectiveRateEntity();
                entity.setAccountingPeriod(accountingPeriod);
                entity.setActuarialCode(actuarialCode);
                entity.setBusinessCode(businessCode);
                entity.setProductName(productName);
                entity.setDesignType(designType);
                entity.setShortTermFlag(shortTermFlag);
                entity.setBookValue(bookValue);
                entity.setEffectiveRate(effectiveRate);

                // 将现金流集合转换为JSON字符串
                JSONObject cashflowJson = new JSONObject();
                cashflowJson.put("0", formatBigDecimal(bookValue)); // 初始投资

                // 添加后续现金流
                int maxPeriod = netCashflows.keySet().stream().max(Integer::compareTo).orElse(0);
                // 确保至少1272期
                maxPeriod = Math.max(maxPeriod, 1272);

                for (int i = 1; i <= maxPeriod; i++) {
                    BigDecimal cashflow = netCashflows.getOrDefault(i, BigDecimal.ZERO);
                    cashflowJson.put(String.valueOf(i), formatBigDecimal(cashflow));
                }
                entity.setCashflowSet(cashflowJson.toString());

                result.add(entity);
                log.info("产品[{}]有效成本率计算完成，账面价值：{}，有效成本率：{}", actuarialCode, bookValue, effectiveRate);
            } catch (Exception e) {
                log.error("产品[{}]有效成本率计算异常", actuarialCode, e);
            }
        }

        return result;
    }

    /**
     * 加载负债现金流数据
     *
     * @param accountingPeriod 账期
     * @param actuarialCode 精算代码（可为null，表示加载所有产品的现金流）
     * @param bpType 基点类型
     * @param cashflowType 现金流类型
     * @return 负债现金流列表
     */
    private List<LiabilityCashFlowEntity> loadLiabilityCashflows(String accountingPeriod, String actuarialCode, String bpType, String cashflowType) {
        // 调用dur模块的Mapper查询负债现金流数据
        List<LiabilityCashFlowEntity> cashflowList = liabilityCashFlowMapper.selectLiabilityCashFlowEntityListByCondition(
                accountingPeriod, cashflowType, bpType, null);

        // 如果指定了精算代码，则过滤出符合精算代码的数据
        if (actuarialCode != null && !actuarialCode.isEmpty()) {
            return cashflowList.stream()
                    .filter(cf -> actuarialCode.equals(cf.getActuarialCode()))
                    .collect(Collectors.toList());
        }

        return cashflowList;
    }

    /**
     * 计算净现金流
     *
     * @param cashflowInList 现金流入列表
     * @param cashflowOutList 现金流出列表
     * @return 净现金流映射表，键为期数，值为净现金流
     */
    private Map<Integer, BigDecimal> calculateNetCashflows(List<LiabilityCashFlowEntity> cashflowInList, List<LiabilityCashFlowEntity> cashflowOutList) {
        Map<Integer, BigDecimal> netCashflows = new HashMap<>();

        // 处理现金流入
        for (LiabilityCashFlowEntity cashflow : cashflowInList) {
            String cashValSet = cashflow.getCashValSet();
            if (cashValSet != null && !cashValSet.isEmpty()) {
                try {
                    JSONObject cashValJson = JSON.parseObject(cashValSet);
                    for (String periodStr : cashValJson.keySet()) {
                        // 跳过第0期，因为第0期是账面价值
                        if ("0".equals(periodStr)) {
                            continue;
                        }

                        Integer period = Integer.parseInt(periodStr);
                        JSONObject periodData = cashValJson.getJSONObject(periodStr);
                        BigDecimal amount = new BigDecimal(periodData.getString("value"));
                        netCashflows.put(period, netCashflows.getOrDefault(period, BigDecimal.ZERO).add(amount));
                    }
                } catch (Exception e) {
                    log.error("解析现金流入值集异常", e);
                }
            }
        }

        // 处理现金流出（流出现金流*(-1)）
        for (LiabilityCashFlowEntity cashflow : cashflowOutList) {
            String cashValSet = cashflow.getCashValSet();
            if (cashValSet != null && !cashValSet.isEmpty()) {
                try {
                    JSONObject cashValJson = JSON.parseObject(cashValSet);
                    for (String periodStr : cashValJson.keySet()) {
                        // 跳过第0期，因为第0期是账面价值
                        if ("0".equals(periodStr)) {
                            continue;
                        }

                        Integer period = Integer.parseInt(periodStr);
                        JSONObject periodData = cashValJson.getJSONObject(periodStr);
                        BigDecimal amount = new BigDecimal(periodData.getString("value"));
                        // 流出现金流取负值
                        netCashflows.put(period, netCashflows.getOrDefault(period, BigDecimal.ZERO).subtract(amount));
                    }
                } catch (Exception e) {
                    log.error("解析现金流出值集异常", e);
                }
            }
        }

        return netCashflows;
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
}
