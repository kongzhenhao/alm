package com.xl.alm.job.cost.service.impl;

import com.xl.alm.job.cost.entity.DividendFundCostRateEntity;
import com.xl.alm.job.cost.entity.ProductAttributeEntity;
import com.xl.alm.job.cost.entity.ProductStatisticsEntity;
import com.xl.alm.job.cost.entity.StatutoryReserveDetailEntity;
import com.xl.alm.job.cost.entity.StatutoryReserveForecastEntity;
import com.xl.alm.job.cost.entity.UniversalAvgSettlementRateEntity;
import com.xl.alm.job.cost.mapper.DividendFundCostRateMapper;
import com.xl.alm.job.cost.mapper.ProductAttributeMapper;
import com.xl.alm.job.cost.mapper.ProductStatisticsMapper;
import com.xl.alm.job.cost.mapper.StatutoryReserveDetailMapper;
import com.xl.alm.job.cost.mapper.StatutoryReserveForecastMapper;
import com.xl.alm.job.cost.mapper.UniversalAvgSettlementRateMapper;
import com.xl.alm.job.cost.query.DividendFundCostRateQuery;
import com.xl.alm.job.cost.query.StatutoryReserveDetailQuery;
import com.xl.alm.job.cost.query.StatutoryReserveForecastQuery;
import com.xl.alm.job.cost.query.UniversalAvgSettlementRateQuery;
import com.xl.alm.job.cost.service.ProductStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分产品统计表(TB0007) 服务实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class ProductStatisticsServiceImpl implements ProductStatisticsService {

    // 设计类型常量
    private static final String DESIGN_TYPE_TRADITIONAL = "01"; // 传统险
    private static final String DESIGN_TYPE_DIVIDEND = "02";    // 分红险
    private static final String DESIGN_TYPE_UNIVERSAL = "03";   // 万能险

    // 长短期标识常量
    private static final String TERM_TYPE_LONG = "L"; // 长期

    // 新业务标识常量
    private static final String NEW_BUSINESS_FLAG_YES = "Y"; // 是

    // 业务类型常量
    private static final String BUSINESS_TYPE_VALID = "01"; // 有效业务
    private static final String BUSINESS_TYPE_NEW = "02";   // 新业务

    // 情景名称常量
    private static final String SCENARIO_NAME_BASIC = "01";  // 基本情景
    private static final String SCENARIO_NAME_STRESS = "03"; // 压力情景三

    // 是否中短常量
    private static final String SHORT_TERM_FLAG_YES = "Y"; // 是
    private static final String SHORT_TERM_FLAG_NO = "N";  // 否

    @Autowired
    private ProductStatisticsMapper productStatisticsMapper;

    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    @Autowired
    private UniversalAvgSettlementRateMapper universalAvgSettlementRateMapper;

    @Autowired
    private DividendFundCostRateMapper dividendFundCostRateMapper;

    @Autowired
    private StatutoryReserveDetailMapper statutoryReserveDetailMapper;

    @Autowired
    private StatutoryReserveForecastMapper statutoryReserveForecastMapper;

    /**
     * 计算分产品统计数据
     *
     * @param accountingPeriod 账期
     * @return 处理结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateProductStatistics(String accountingPeriod) {
        log.info("开始执行分产品统计数据计算，账期：{}", accountingPeriod);
        try {
            // 步骤1：加载基础数据
            Map<String, ProductAttributeEntity> productAttributeMap = loadProductAttributes(accountingPeriod);
            Map<String, UniversalAvgSettlementRateEntity> universalAvgSettlementRateMap = loadUniversalAvgSettlementRates(accountingPeriod);
            Map<String, DividendFundCostRateEntity> dividendFundCostRateMap = loadDividendFundCostRates(accountingPeriod);
            Map<String, StatutoryReserveDetailEntity> statutoryReserveDetailMap = loadStatutoryReserveDetails(accountingPeriod);
            Map<String, Map<String, StatutoryReserveForecastEntity>> statutoryReserveForecastMap = loadStatutoryReserveForecasts(accountingPeriod);

            log.info("加载基础数据完成，产品属性表共加载{}条记录，万能平均结算利率表共加载{}条记录，分红方案表共加载{}条记录，法定准备金明细表共加载{}条记录，法定准备金预测表共加载{}条记录",
                    productAttributeMap.size(), universalAvgSettlementRateMap.size(), dividendFundCostRateMap.size(),
                    statutoryReserveDetailMap.size(), statutoryReserveForecastMap.size());

            // 步骤2：计算分产品统计数据
            List<ProductStatisticsEntity> productStatisticsEntities = calculateProductStatisticsData(
                    accountingPeriod, productAttributeMap, universalAvgSettlementRateMap,
                    dividendFundCostRateMap, statutoryReserveDetailMap, statutoryReserveForecastMap);

            log.info("计算分产品统计数据完成，共计算{}条记录", productStatisticsEntities.size());

            // 步骤3：数据入表
            // 先删除已有数据
            productStatisticsMapper.deleteProductStatisticsByPeriod(accountingPeriod);
            // 批量插入新数据
            if (!productStatisticsEntities.isEmpty()) {
                productStatisticsMapper.batchInsertProductStatistics(productStatisticsEntities);
            }

            log.info("分产品统计数据计算任务执行完成，账期：{}", accountingPeriod);
            return true;
        } catch (Exception e) {
            log.error("分产品统计数据计算任务执行异常，账期：{}", accountingPeriod, e);
            throw e;
        }
    }

    /**
     * 加载产品属性表数据
     *
     * @param accountingPeriod 账期
     * @return 产品属性表数据Map
     */
    private Map<String, ProductAttributeEntity> loadProductAttributes(String accountingPeriod) {
        ProductAttributeEntity query = new ProductAttributeEntity();
        query.setAccountingPeriod(accountingPeriod);
        List<ProductAttributeEntity> productAttributeList = productAttributeMapper.selectProductAttributeList(query);

        Map<String, ProductAttributeEntity> productAttributeMap = new HashMap<>();
        for (ProductAttributeEntity productAttribute : productAttributeList) {
            productAttributeMap.put(productAttribute.getActuarialCode(), productAttribute);
        }

        return productAttributeMap;
    }

    /**
     * 加载万能平均结算利率表数据
     *
     * @param accountingPeriod 账期
     * @return 万能平均结算利率表数据Map
     */
    private Map<String, UniversalAvgSettlementRateEntity> loadUniversalAvgSettlementRates(String accountingPeriod) {
        Map<String, UniversalAvgSettlementRateEntity> universalAvgSettlementRateMap = new HashMap<>();
        try {
            UniversalAvgSettlementRateQuery query = new UniversalAvgSettlementRateQuery();
            query.setAccountingPeriod(accountingPeriod);
            List<UniversalAvgSettlementRateEntity> universalAvgSettlementRateList = universalAvgSettlementRateMapper.selectUniversalAvgSettlementRateList(query);

            for (UniversalAvgSettlementRateEntity universalAvgSettlementRate : universalAvgSettlementRateList) {
                universalAvgSettlementRateMap.put(universalAvgSettlementRate.getActuarialCode(), universalAvgSettlementRate);
            }
        } catch (Exception e) {
            log.error("加载万能平均结算利率表数据失败，账期：{}", accountingPeriod, e);
        }
        return universalAvgSettlementRateMap;
    }

    /**
     * 加载分红方案表数据
     *
     * @param accountingPeriod 账期
     * @return 分红方案表数据Map
     */
    private Map<String, DividendFundCostRateEntity> loadDividendFundCostRates(String accountingPeriod) {
        Map<String, DividendFundCostRateEntity> dividendFundCostRateMap = new HashMap<>();
        try {
            DividendFundCostRateQuery query = new DividendFundCostRateQuery();
            query.setAccountingPeriod(accountingPeriod);
            List<DividendFundCostRateEntity> dividendFundCostRateList = dividendFundCostRateMapper.selectDividendFundCostRateList(query);

            for (DividendFundCostRateEntity dividendFundCostRate : dividendFundCostRateList) {
                dividendFundCostRateMap.put(dividendFundCostRate.getActuarialCode(), dividendFundCostRate);
            }
        } catch (Exception e) {
            log.error("加载分红方案表数据失败，账期：{}", accountingPeriod, e);
        }
        return dividendFundCostRateMap;
    }

    /**
     * 加载法定准备金明细表数据
     *
     * @param accountingPeriod 账期
     * @return 法定准备金明细表数据Map
     */
    private Map<String, StatutoryReserveDetailEntity> loadStatutoryReserveDetails(String accountingPeriod) {
        StatutoryReserveDetailQuery query = new StatutoryReserveDetailQuery();
        query.setAccountingPeriod(accountingPeriod);
        List<StatutoryReserveDetailEntity> statutoryReserveDetailList = statutoryReserveDetailMapper.selectStatutoryReserveDetailList(query);

        Map<String, StatutoryReserveDetailEntity> statutoryReserveDetailMap = new HashMap<>();
        for (StatutoryReserveDetailEntity statutoryReserveDetail : statutoryReserveDetailList) {
            statutoryReserveDetailMap.put(statutoryReserveDetail.getActuarialCode(), statutoryReserveDetail);
        }

        return statutoryReserveDetailMap;
    }

    /**
     * 加载法定准备金预测表数据
     *
     * @param accountingPeriod 账期
     * @return 法定准备金预测表数据Map
     */
    private Map<String, Map<String, StatutoryReserveForecastEntity>> loadStatutoryReserveForecasts(String accountingPeriod) {
        Map<String, Map<String, StatutoryReserveForecastEntity>> statutoryReserveForecastMap = new HashMap<>();
        try {
            StatutoryReserveForecastQuery query = new StatutoryReserveForecastQuery();
            query.setAccountingPeriod(accountingPeriod);
            List<StatutoryReserveForecastEntity> statutoryReserveForecastList = statutoryReserveForecastMapper.selectStatutoryReserveForecastList(query);

            for (StatutoryReserveForecastEntity statutoryReserveForecast : statutoryReserveForecastList) {
                // 设置情景名称为基本情景
                statutoryReserveForecast.setScenarioName(SCENARIO_NAME_BASIC);
                // 设置长短期标识为长期
                statutoryReserveForecast.setTermType(TERM_TYPE_LONG);

                String businessType = statutoryReserveForecast.getBusinessType();
                String actuarialCode = statutoryReserveForecast.getActuarialCode();

                statutoryReserveForecastMap.computeIfAbsent(businessType, k -> new HashMap<>())
                        .put(actuarialCode, statutoryReserveForecast);
            }
        } catch (Exception e) {
            log.error("加载法定准备金预测表数据失败，账期：{}", accountingPeriod, e);
        }
        return statutoryReserveForecastMap;
    }

    /**
     * 计算分产品统计数据
     *
     * @param accountingPeriod 账期
     * @param productAttributeMap 产品属性表数据Map
     * @param universalAvgSettlementRateMap 万能平均结算利率表数据Map
     * @param dividendFundCostRateMap 分红方案表数据Map
     * @param statutoryReserveDetailMap 法定准备金明细表数据Map
     * @param statutoryReserveForecastMap 法定准备金预测表数据Map
     * @return 分产品统计列表
     */
    private List<ProductStatisticsEntity> calculateProductStatisticsData(
            String accountingPeriod,
            Map<String, ProductAttributeEntity> productAttributeMap,
            Map<String, UniversalAvgSettlementRateEntity> universalAvgSettlementRateMap,
            Map<String, DividendFundCostRateEntity> dividendFundCostRateMap,
            Map<String, StatutoryReserveDetailEntity> statutoryReserveDetailMap,
            Map<String, Map<String, StatutoryReserveForecastEntity>> statutoryReserveForecastMap) {

        List<ProductStatisticsEntity> result = new ArrayList<>();

        // 步骤1：筛选产品数据
        for (ProductAttributeEntity productAttribute : productAttributeMap.values()) {
            String designType = productAttribute.getDesignType();

            // 只处理传统险、分红险、万能险，不包含投连险
            if (DESIGN_TYPE_TRADITIONAL.equals(designType) || DESIGN_TYPE_DIVIDEND.equals(designType) || DESIGN_TYPE_UNIVERSAL.equals(designType)) {
                // 步骤2：确定业务类型
                // 有效业务：设计类型为传统险、分红险、万能险，且长短期标识为长期的产品
                boolean isValidBusiness = TERM_TYPE_LONG.equals(productAttribute.getTermType());

                // 新业务：设计类型为传统险、分红险、万能险，长短期标识为长期，且新业务标识为是的产品
                boolean isNewBusiness = isValidBusiness && NEW_BUSINESS_FLAG_YES.equals(productAttribute.getNewBusinessFlag());

                // 处理有效业务
                if (isValidBusiness) {
                    processBusinessType(result, BUSINESS_TYPE_VALID, SCENARIO_NAME_BASIC, accountingPeriod, productAttribute, universalAvgSettlementRateMap, dividendFundCostRateMap, statutoryReserveDetailMap, statutoryReserveForecastMap);
                    processBusinessType(result, BUSINESS_TYPE_VALID, SCENARIO_NAME_STRESS, accountingPeriod, productAttribute, universalAvgSettlementRateMap, dividendFundCostRateMap, statutoryReserveDetailMap, statutoryReserveForecastMap);
                }

                // 处理新业务
                if (isNewBusiness) {
                    processBusinessType(result, BUSINESS_TYPE_NEW, SCENARIO_NAME_BASIC, accountingPeriod, productAttribute, universalAvgSettlementRateMap, dividendFundCostRateMap, statutoryReserveDetailMap, statutoryReserveForecastMap);
                    processBusinessType(result, BUSINESS_TYPE_NEW, SCENARIO_NAME_STRESS, accountingPeriod, productAttribute, universalAvgSettlementRateMap, dividendFundCostRateMap, statutoryReserveDetailMap, statutoryReserveForecastMap);
                }
            }
        }

        return result;
    }

    /**
     * 处理特定业务类型和情景的产品数据
     *
     * @param result 结果列表
     * @param businessType 业务类型
     * @param scenarioName 情景名称
     * @param accountingPeriod 账期
     * @param productAttribute 产品属性
     * @param universalAvgSettlementRateMap 万能平均结算利率表数据Map
     * @param dividendFundCostRateMap 分红方案表数据Map
     * @param statutoryReserveDetailMap 法定准备金明细表数据Map
     * @param statutoryReserveForecastMap 法定准备金预测表数据Map
     */
    private void processBusinessType(
            List<ProductStatisticsEntity> result,
            String businessType,
            String scenarioName,
            String accountingPeriod,
            ProductAttributeEntity productAttribute,
            Map<String, UniversalAvgSettlementRateEntity> universalAvgSettlementRateMap,
            Map<String, DividendFundCostRateEntity> dividendFundCostRateMap,
            Map<String, StatutoryReserveDetailEntity> statutoryReserveDetailMap,
            Map<String, Map<String, StatutoryReserveForecastEntity>> statutoryReserveForecastMap) {

        String actuarialCode = productAttribute.getActuarialCode();
        String designType = productAttribute.getDesignType();
        String shortTermFlag = productAttribute.getShortTermFlag();

        // 创建产品统计实体
        ProductStatisticsEntity entity = new ProductStatisticsEntity();
        entity.setScenarioName(scenarioName);
        entity.setBusinessType(businessType);
        entity.setAccountingPeriod(accountingPeriod);
        entity.setActuarialCode(actuarialCode);
        entity.setBusinessCode(productAttribute.getBusinessCode());
        entity.setProductName(productAttribute.getProductName());
        entity.setTermType(productAttribute.getTermType());
        entity.setDesignType(designType);
        entity.setShortTermFlag(shortTermFlag);
        entity.setIsDel(0);

        // 步骤3：计算法定准备金
        calculateStatutoryReserves(entity, businessType, actuarialCode, statutoryReserveDetailMap, statutoryReserveForecastMap);

        // 步骤4-7：计算资金成本率和保证成本率
        calculateCostRates(entity, scenarioName, designType, shortTermFlag, productAttribute, universalAvgSettlementRateMap, dividendFundCostRateMap);

        result.add(entity);
    }

    /**
     * 计算法定准备金
     *
     * @param entity 产品统计实体
     * @param businessType 业务类型
     * @param actuarialCode 精算代码
     * @param statutoryReserveDetailMap 法定准备金明细表数据Map
     * @param statutoryReserveForecastMap 法定准备金预测表数据Map
     */
    private void calculateStatutoryReserves(
            ProductStatisticsEntity entity,
            String businessType,
            String actuarialCode,
            Map<String, StatutoryReserveDetailEntity> statutoryReserveDetailMap,
            Map<String, Map<String, StatutoryReserveForecastEntity>> statutoryReserveForecastMap) {

        // 法定准备金T0计算规则
        if (BUSINESS_TYPE_VALID.equals(businessType)) {
            StatutoryReserveDetailEntity detail = statutoryReserveDetailMap.get(actuarialCode);
            if (detail != null) {
                BigDecimal accountValue = detail.getAccountValue() != null ? detail.getAccountValue() : BigDecimal.ZERO;
                BigDecimal totalStatutoryReserve = detail.getTotalStatutoryReserve() != null ? detail.getTotalStatutoryReserve() : BigDecimal.ZERO;
                entity.setStatutoryReserveT0(accountValue.add(totalStatutoryReserve));
            } else {
                entity.setStatutoryReserveT0(BigDecimal.ZERO);
            }
        } else {
            // 新业务强制赋值为0
            entity.setStatutoryReserveT0(BigDecimal.ZERO);
        }

        // 法定准备金T1、T2、T3计算规则
        Map<String, StatutoryReserveForecastEntity> forecastMap = statutoryReserveForecastMap.get(businessType);
        if (forecastMap != null) {
            StatutoryReserveForecastEntity forecast = forecastMap.get(actuarialCode);
            if (forecast != null) {
                entity.setStatutoryReserveT1(forecast.getStatutoryReserveT1() != null ? forecast.getStatutoryReserveT1() : BigDecimal.ZERO);
                entity.setStatutoryReserveT2(forecast.getStatutoryReserveT2() != null ? forecast.getStatutoryReserveT2() : BigDecimal.ZERO);
                entity.setStatutoryReserveT3(forecast.getStatutoryReserveT3() != null ? forecast.getStatutoryReserveT3() : BigDecimal.ZERO);
            } else {
                entity.setStatutoryReserveT1(BigDecimal.ZERO);
                entity.setStatutoryReserveT2(BigDecimal.ZERO);
                entity.setStatutoryReserveT3(BigDecimal.ZERO);
            }
        } else {
            entity.setStatutoryReserveT1(BigDecimal.ZERO);
            entity.setStatutoryReserveT2(BigDecimal.ZERO);
            entity.setStatutoryReserveT3(BigDecimal.ZERO);
        }
    }

    /**
     * 计算资金成本率和保证成本率
     *
     * @param entity 产品统计实体
     * @param scenarioName 情景名称
     * @param designType 设计类型
     * @param shortTermFlag 是否中短
     * @param productAttribute 产品属性
     * @param universalAvgSettlementRateMap 万能平均结算利率表数据Map
     * @param dividendFundCostRateMap 分红方案表数据Map
     */
    private void calculateCostRates(
            ProductStatisticsEntity entity,
            String scenarioName,
            String designType,
            String shortTermFlag,
            ProductAttributeEntity productAttribute,
            Map<String, UniversalAvgSettlementRateEntity> universalAvgSettlementRateMap,
            Map<String, DividendFundCostRateEntity> dividendFundCostRateMap) {

        String actuarialCode = productAttribute.getActuarialCode();
        BigDecimal guaranteedCostRate = productAttribute.getGuaranteedCostRate() != null ? productAttribute.getGuaranteedCostRate() : BigDecimal.ZERO;

        // 计算资金成本率T0和资金成本率T1（适用于基本情景和压力情景三）
        if (DESIGN_TYPE_TRADITIONAL.equals(designType)) {
            // 传统险：取自产品属性表的定价保证成本率字段
            entity.setFundCostRateT0(guaranteedCostRate);
            entity.setFundCostRateT1(guaranteedCostRate);
        } else if (DESIGN_TYPE_DIVIDEND.equals(designType)) {
            // 分红险：取自分红方案表的资金成本率T0和资金成本率T1字段
            DividendFundCostRateEntity dividendFundCostRate = dividendFundCostRateMap.get(actuarialCode);
            if (dividendFundCostRate != null) {
                entity.setFundCostRateT0(dividendFundCostRate.getFundCostRateT0());
                entity.setFundCostRateT1(dividendFundCostRate.getFundCostRateT1());
            } else {
                entity.setFundCostRateT0(BigDecimal.ZERO);
                entity.setFundCostRateT1(BigDecimal.ZERO);
            }
        } else if (DESIGN_TYPE_UNIVERSAL.equals(designType)) {
            // 万能险：取自万能平均结算利率表的平均结算利率T0和平均结算利率T1字段
            UniversalAvgSettlementRateEntity universalAvgSettlementRate = universalAvgSettlementRateMap.get(actuarialCode);
            if (universalAvgSettlementRate != null) {
                entity.setFundCostRateT0(universalAvgSettlementRate.getAvgSettlementRateT0());
                entity.setFundCostRateT1(universalAvgSettlementRate.getAvgSettlementRateT1());
            } else {
                entity.setFundCostRateT0(BigDecimal.ZERO);
                entity.setFundCostRateT1(BigDecimal.ZERO);
            }
        }

        // 计算资金成本率T2（适用于基本情景和压力情景三）
        if (DESIGN_TYPE_TRADITIONAL.equals(designType)) {
            entity.setFundCostRateT2(guaranteedCostRate);
        } else if (DESIGN_TYPE_DIVIDEND.equals(designType)) {
            DividendFundCostRateEntity dividendFundCostRate = dividendFundCostRateMap.get(actuarialCode);
            if (dividendFundCostRate != null) {
                entity.setFundCostRateT2(dividendFundCostRate.getFundCostRateT2());
            } else {
                entity.setFundCostRateT2(BigDecimal.ZERO);
            }
        } else if (DESIGN_TYPE_UNIVERSAL.equals(designType)) {
            UniversalAvgSettlementRateEntity universalAvgSettlementRate = universalAvgSettlementRateMap.get(actuarialCode);
            if (universalAvgSettlementRate != null) {
                entity.setFundCostRateT2(universalAvgSettlementRate.getAvgSettlementRateT2());
            } else {
                entity.setFundCostRateT2(BigDecimal.ZERO);
            }
        }

        // 计算资金成本率T3
        if (SCENARIO_NAME_BASIC.equals(scenarioName)) {
            // 基本情景
            if (DESIGN_TYPE_TRADITIONAL.equals(designType)) {
                entity.setFundCostRateT3(guaranteedCostRate);
            } else if (DESIGN_TYPE_DIVIDEND.equals(designType)) {
                DividendFundCostRateEntity dividendFundCostRate = dividendFundCostRateMap.get(actuarialCode);
                if (dividendFundCostRate != null) {
                    entity.setFundCostRateT3(dividendFundCostRate.getFundCostRateT3());
                } else {
                    entity.setFundCostRateT3(BigDecimal.ZERO);
                }
            } else if (DESIGN_TYPE_UNIVERSAL.equals(designType)) {
                UniversalAvgSettlementRateEntity universalAvgSettlementRate = universalAvgSettlementRateMap.get(actuarialCode);
                if (universalAvgSettlementRate != null) {
                    entity.setFundCostRateT3(universalAvgSettlementRate.getAvgSettlementRateT3());
                } else {
                    entity.setFundCostRateT3(BigDecimal.ZERO);
                }
            }
        } else {
            // 压力情景三
            if (DESIGN_TYPE_TRADITIONAL.equals(designType)) {
                if (SHORT_TERM_FLAG_NO.equals(shortTermFlag)) {
                    // 非中短
                    entity.setFundCostRateT3(guaranteedCostRate);
                } else {
                    // 中短
                    entity.setFundCostRateT3(guaranteedCostRate.add(new BigDecimal("0.02")));
                }
            } else if (DESIGN_TYPE_DIVIDEND.equals(designType)) {
                DividendFundCostRateEntity dividendFundCostRate = dividendFundCostRateMap.get(actuarialCode);
                if (dividendFundCostRate != null) {
                    BigDecimal fundCostRateT3 = dividendFundCostRate.getFundCostRateT3() != null ? dividendFundCostRate.getFundCostRateT3() : BigDecimal.ZERO;
                    BigDecimal dividendRatio = dividendFundCostRate.getDividendRatio() != null ? dividendFundCostRate.getDividendRatio() : BigDecimal.ZERO;
                    entity.setFundCostRateT3(fundCostRateT3.add(new BigDecimal("0.02").multiply(dividendRatio)));
                } else {
                    entity.setFundCostRateT3(BigDecimal.ZERO);
                }
            } else if (DESIGN_TYPE_UNIVERSAL.equals(designType)) {
                UniversalAvgSettlementRateEntity universalAvgSettlementRate = universalAvgSettlementRateMap.get(actuarialCode);
                if (universalAvgSettlementRate != null) {
                    BigDecimal avgSettlementRateT3 = universalAvgSettlementRate.getAvgSettlementRateT3() != null ? universalAvgSettlementRate.getAvgSettlementRateT3() : BigDecimal.ZERO;
                    entity.setFundCostRateT3(avgSettlementRateT3.add(new BigDecimal("0.02")));
                } else {
                    entity.setFundCostRateT3(BigDecimal.ZERO);
                }
            }
        }

        // 计算保证成本率T0、保证成本率T1、保证成本率T2、保证成本率T3（适用于基本情景和压力情景三）
        if (SHORT_TERM_FLAG_NO.equals(shortTermFlag)) {
            // 非中短
            entity.setGuaranteedCostRateT0(guaranteedCostRate);
            entity.setGuaranteedCostRateT1(guaranteedCostRate);
            entity.setGuaranteedCostRateT2(guaranteedCostRate);
            entity.setGuaranteedCostRateT3(guaranteedCostRate);
        } else {
            // 中短
            entity.setGuaranteedCostRateT0(entity.getFundCostRateT0());
            entity.setGuaranteedCostRateT1(entity.getFundCostRateT1());
            entity.setGuaranteedCostRateT2(entity.getFundCostRateT2());
            entity.setGuaranteedCostRateT3(entity.getFundCostRateT3());
        }
    }
}
