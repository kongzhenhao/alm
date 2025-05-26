package com.xl.alm.job.minc.service.impl;

import com.xl.alm.job.minc.entity.MarketCreditCapitalEntity;
import com.xl.alm.job.minc.mapper.MarketCreditCapitalMapper;
import com.xl.alm.job.minc.service.MarketCreditCapitalService;
import com.xl.alm.job.minc.util.AviatorFormulaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 市场及信用最低资本计算服务实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class MarketCreditCapitalServiceImpl implements MarketCreditCapitalService {

    @Autowired
    private MarketCreditCapitalMapper marketCreditCapitalMapper;

    @Autowired
    private AviatorFormulaUtil aviatorFormulaUtil;

    /**
     * 执行市场及信用最低资本计算
     *
     * @param accountingPeriod 账期，格式：YYYYMM（如202306）
     * @return 处理结果，true表示成功，false表示失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateMarketCreditCapital(String accountingPeriod) {
        log.info("开始执行市场及信用最低资本计算，账期：{}", accountingPeriod);

        // 参数校验
        if (!StringUtils.hasText(accountingPeriod)) {
            log.error("账期参数不能为空");
            return false;
        }

        if (accountingPeriod.length() != 6) {
            log.error("账期格式错误，应为YYYYMM格式，当前值：{}", accountingPeriod);
            return false;
        }

        try {
            // 步骤1：清理历史数据
            log.info("步骤1：清理账期{}的历史数据", accountingPeriod);
            int deletedCount = marketCreditCapitalMapper.deleteByAccountingPeriod(accountingPeriod);
            log.info("清理历史数据完成，删除{}条记录", deletedCount);

            // 步骤2：读取项目定义表中有最低资本计算公式的项目
            log.info("步骤2：读取项目定义表中有最低资本计算公式的项目");
            List<Map<String, Object>> itemDefinitions = marketCreditCapitalMapper.selectItemDefinitionsWithCapitalFormula();

            if (itemDefinitions == null || itemDefinitions.isEmpty()) {
                log.warn("未找到有最低资本计算公式的项目定义，跳过计算");
                return true;
            }

            log.info("找到{}个有最低资本计算公式的项目", itemDefinitions.size());

            // 步骤3：读取所有账户编码
            log.info("步骤3：读取所有账户编码");
            List<String> accountCodes = marketCreditCapitalMapper.selectAllAccountCodes();

            if (accountCodes == null || accountCodes.isEmpty()) {
                log.warn("未找到账户编码配置，跳过计算");
                return true;
            }

            log.info("找到{}个账户编码：{}", accountCodes.size(), accountCodes);

            // 步骤4：执行计算并按风险类型汇总
            log.info("步骤4：开始执行市场及信用最低资本计算");

            // 用于存储按风险类型和账户汇总的结果
            Map<String, Map<String, BigDecimal>> riskAccountSummary = new HashMap<>();
            // 初始化汇总结构
            riskAccountSummary.put("市场风险", new HashMap<>());
            riskAccountSummary.put("信用风险", new HashMap<>());

            int calculatedCount = 0;

            for (Map<String, Object> itemDef : itemDefinitions) {
                String itemCode = (String) itemDef.get("item_code");
                String capitalItem = (String) itemDef.get("capital_item");
                String riskType = (String) itemDef.get("risk_type");
                String formula = (String) itemDef.get("capital_calculation_formula");

                log.info("计算项目：{} - {}，风险类型：{}，公式：{}", itemCode, capitalItem, riskType, formula);

                // 为每个账户计算该项目的最低资本
                for (String accountCode : accountCodes) {
                    log.info("计算项目{}在账户{}下的最低资本", itemCode, accountCode);

                    // 使用Aviator执行公式计算
                    BigDecimal calculatedAmount = aviatorFormulaUtil.executeCapitalCalculationFormula(formula, accountCode, accountingPeriod);

                    if (calculatedAmount != null && calculatedAmount.compareTo(BigDecimal.ZERO) != 0) {
                        // 累加到对应风险类型和账户的汇总中
                        Map<String, BigDecimal> accountSummary = riskAccountSummary.get(riskType);
                        accountSummary.merge(accountCode, calculatedAmount, BigDecimal::add);

                        calculatedCount++;
                        log.info("项目{}在账户{}下的最低资本：{}，累加到{}汇总", itemCode, accountCode, calculatedAmount, riskType);
                    } else {
                        log.warn("项目{}在账户{}下的最低资本计算结果为空或零，跳过", itemCode, accountCode);
                    }
                }
            }

            // 步骤5：生成最终汇总结果
            log.info("步骤5：生成最终汇总结果");
            log.info("汇总数据详情：{}", riskAccountSummary);
            List<MarketCreditCapitalEntity> resultList = new ArrayList<>();

            for (Map.Entry<String, Map<String, BigDecimal>> riskEntry : riskAccountSummary.entrySet()) {
                String riskType = riskEntry.getKey();
                Map<String, BigDecimal> accountAmounts = riskEntry.getValue();

                log.info("处理风险类型：{}，账户金额：{}", riskType, accountAmounts);

                // 计算普通账户汇总（传统+分红+万能）
                BigDecimal generalAccountAmount = BigDecimal.ZERO;
                for (String accountCode : accountCodes) {
                    BigDecimal amount = accountAmounts.getOrDefault(accountCode, BigDecimal.ZERO);
                    generalAccountAmount = generalAccountAmount.add(amount);
                    log.info("累加账户{}的金额{}到普通账户，当前普通账户总额：{}", accountCode, amount, generalAccountAmount);
                }

                // 为每个账户创建记录
                for (String accountCode : accountCodes) {
                    BigDecimal amount = accountAmounts.getOrDefault(accountCode, BigDecimal.ZERO);
                    if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        MarketCreditCapitalEntity entity = createEntity(accountingPeriod, getRiskTypeItemCode(riskType), accountCode, amount);
                        resultList.add(entity);
                        log.info("创建{}在{}账户的记录，金额：{}", riskType, accountCode, amount);
                    } else {
                        log.info("跳过{}在{}账户的记录，金额为零", riskType, accountCode);
                    }
                }

                // 创建普通账户汇总记录
                if (generalAccountAmount.compareTo(BigDecimal.ZERO) > 0) {
                    MarketCreditCapitalEntity generalEntity = createEntity(accountingPeriod, getRiskTypeItemCode(riskType), "AC006", generalAccountAmount);
                    resultList.add(generalEntity);
                    log.info("创建{}在普通账户(AC006)的汇总记录，金额：{}", riskType, generalAccountAmount);
                } else {
                    log.warn("{}的普通账户汇总金额为零，跳过创建记录", riskType);
                }
            }

            // 步骤6：批量插入计算结果
            if (!resultList.isEmpty()) {
                log.info("步骤6：批量插入{}条汇总结果", resultList.size());
                int insertedCount = marketCreditCapitalMapper.batchInsertMarketCreditCapital(resultList);
                log.info("批量插入完成，实际插入{}条记录", insertedCount);
            } else {
                log.warn("没有计算结果需要插入");
            }

            log.info("市场及信用最低资本计算完成，账期：{}，成功计算{}个项目×账户组合", accountingPeriod, calculatedCount);
            return true;

        } catch (Exception e) {
            log.error("市场及信用最低资本计算失败，账期：{}", accountingPeriod, e);
            throw e; // 重新抛出异常，触发事务回滚
        }
    }

    /**
     * 创建市场及信用最低资本实体
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @param amount 金额
     * @return 实体对象
     */
    private MarketCreditCapitalEntity createEntity(String accountingPeriod, String itemCode, String accountCode, BigDecimal amount) {
        MarketCreditCapitalEntity entity = new MarketCreditCapitalEntity();
        entity.setAccountingPeriod(accountingPeriod);
        entity.setItemCode(itemCode);
        entity.setAccountCode(accountCode);
        entity.setAmount(amount);
        entity.setCreateBy("system");
        entity.setCreateTime(new Date());
        entity.setUpdateBy("system");
        entity.setUpdateTime(new Date());
        return entity;
    }

    /**
     * 根据风险类型获取对应的项目编码
     *
     * @param riskType 风险类型
     * @return 项目编码
     */
    private String getRiskTypeItemCode(String riskType) {
        switch (riskType) {
            case "市场风险":
                return "市场风险最低资本";
            case "信用风险":
                return "信用风险最低资本";
            default:
                return "UNKNOWN";
        }
    }
}
