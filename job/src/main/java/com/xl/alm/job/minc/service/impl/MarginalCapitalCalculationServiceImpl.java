package com.xl.alm.job.minc.service.impl;

import com.xl.alm.job.minc.entity.MarginalCapitalCalculationEntity;
import com.xl.alm.job.minc.mapper.MarginalCapitalCalculationMapper;
import com.xl.alm.job.minc.service.MarginalCapitalCalculationService;
import com.xl.alm.job.minc.util.AviatorFormulaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 边际最低资本贡献率计算服务实现类
 * 对应UC0007：计算边际最低资本贡献率数据
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class MarginalCapitalCalculationServiceImpl implements MarginalCapitalCalculationService {

    @Autowired
    private MarginalCapitalCalculationMapper marginalCapitalCalculationMapper;

    @Autowired
    private AviatorFormulaUtil aviatorFormulaUtil;

    /**
     * 执行边际最低资本贡献率计算（只计算子风险层面）
     *
     * @param accountingPeriod 账期，格式：YYYYMM（如202306）
     * @return 处理结果，true表示成功，false表示失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateSubRiskMarginalFactor(String accountingPeriod) {
        log.info("开始执行边际最低资本贡献率计算（子风险层面），账期：{}", accountingPeriod);

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
            // 步骤1：数据准备 - 读取风险项目金额表数据
            log.info("步骤1：读取风险项目金额表(TB0002)数据，账期：{}", accountingPeriod);
            List<Map<String, Object>> riskItemAmounts = marginalCapitalCalculationMapper.selectRiskItemAmounts(accountingPeriod);

            if (riskItemAmounts == null || riskItemAmounts.isEmpty()) {
                log.warn("未找到账期{}的风险项目金额数据，跳过计算", accountingPeriod);
                return true; // 没有数据不算失败
            }

            log.info("从TB0002表读取到{}条风险项目金额记录", riskItemAmounts.size());

            // 步骤2：清理目标账期的TB0006表数据
            log.info("步骤2：清理目标账期的边际最低资本贡献率表(TB0006)数据，账期：{}", accountingPeriod);
            int deletedCount = marginalCapitalCalculationMapper.deleteMarginalCapitalByPeriod(accountingPeriod);
            log.info("删除了{}条历史记录", deletedCount);

            // 步骤3：读取项目定义表数据，用于过滤
            log.info("步骤3：读取项目定义表数据，用于过滤计算范围");
            List<Map<String, Object>> allItemDefinitions = marginalCapitalCalculationMapper.selectAllItemDefinitions();

            if (allItemDefinitions == null || allItemDefinitions.isEmpty()) {
                log.warn("未找到项目定义表数据，跳过计算");
                return true;
            }

            // 创建项目定义表中项目编码的Set，用于快速查找
            Set<String> definedItemCodes = new HashSet<>();
            for (Map<String, Object> itemDef : allItemDefinitions) {
                String itemCode = (String) itemDef.get("item_code");
                if (itemCode != null) {
                    definedItemCodes.add(itemCode);
                }
            }
            log.info("项目定义表中共有{}个项目编码", definedItemCodes.size());

            // 步骤4：过滤TB0002中的数据，只保留在TB0005中存在的项目
            log.info("步骤4：过滤风险项目金额数据，只保留在项目定义表中存在的项目");
            List<MarginalCapitalCalculationEntity> initialDataList = new ArrayList<>();
            int filteredCount = 0;

            for (Map<String, Object> riskItem : riskItemAmounts) {
                String itemCode = (String) riskItem.get("item_code");
                BigDecimal reinsuAfterAmount = (BigDecimal) riskItem.get("reinsu_after_amount");

                // 只有在项目定义表中存在的项目才进行处理
                if (definedItemCodes.contains(itemCode)) {
                    MarginalCapitalCalculationEntity entity = new MarginalCapitalCalculationEntity();
                    entity.setAccountingPeriod(accountingPeriod);
                    entity.setItemCode(itemCode);
                    entity.setReinsuAfterAmount(reinsuAfterAmount);
                    entity.setSubRiskMarginalFactor(null); // 初始为空，后续计算
                    entity.setCompanyMarginalFactor(null); // 初始为空，后续计算
                    entity.setIsDel(0);
                    entity.setCreateBy("system");
                    entity.setCreateTime(new Date());
                    entity.setUpdateBy("system");
                    entity.setUpdateTime(new Date());

                    initialDataList.add(entity);
                } else {
                    filteredCount++;
                    log.debug("项目编码{}不在项目定义表中，跳过处理", itemCode);
                }
            }

            log.info("过滤完成：TB0002表中{}条记录，项目定义表中存在{}条，过滤掉{}条",
                    riskItemAmounts.size(), initialDataList.size(), filteredCount);

            if (initialDataList.isEmpty()) {
                log.warn("过滤后没有需要处理的数据，跳过计算");
                return true;
            }

            // 步骤5：初始化TB0006表数据 - 写入再保后金额
            log.info("步骤5：初始化边际最低资本贡献率表数据");
            int insertedCount = marginalCapitalCalculationMapper.batchInsertMarginalCapital(initialDataList);
            log.info("成功初始化{}条边际最低资本贡献率记录", insertedCount);

            // 步骤6：计算子风险层面边际最低资本贡献因子
            log.info("步骤6：计算子风险层面边际最低资本贡献因子");
            List<Map<String, Object>> itemDefinitions = marginalCapitalCalculationMapper.selectItemDefinitionsWithSubRiskFormula();

            if (itemDefinitions == null || itemDefinitions.isEmpty()) {
                log.warn("未找到有子风险公式的项目定义，跳过公式计算");
                return true;
            }

            log.info("找到{}个有子风险公式的项目，开始计算", itemDefinitions.size());

            int calculatedCount = 0;
            for (Map<String, Object> itemDef : itemDefinitions) {
                String itemCode = (String) itemDef.get("item_code");
                String formula = (String) itemDef.get("sub_risk_factor_formula");

                log.info("计算项目{}的子风险层面边际最低资本贡献因子，公式：{}", itemCode, formula);

                // 使用Aviator执行公式计算
                BigDecimal subRiskFactor = aviatorFormulaUtil.executeSubRiskFactorFormula(formula, accountingPeriod);

                if (subRiskFactor != null) {
                    // 更新计算结果
                    int updateCount = marginalCapitalCalculationMapper.updateSubRiskMarginalFactor(itemCode, accountingPeriod, subRiskFactor);
                    if (updateCount > 0) {
                        calculatedCount++;
                        log.info("项目{}计算完成，子风险层面边际最低资本贡献因子：{}", itemCode, subRiskFactor);
                    } else {
                        log.warn("项目{}更新失败，可能不存在对应的记录", itemCode);
                    }
                } else {
                    log.error("项目{}计算失败，公式：{}", itemCode, formula);
                }
            }

            log.info("边际最低资本贡献率计算完成，账期：{}，成功计算{}个项目", accountingPeriod, calculatedCount);
            return true;

        } catch (Exception e) {
            log.error("边际最低资本贡献率计算失败，账期：{}", accountingPeriod, e);
            throw e; // 重新抛出异常，触发事务回滚
        }
    }

    /**
     * 执行边际最低资本贡献率计算（计算公司层面）
     *
     * @param accountingPeriod 账期，格式：YYYYMM（如202306）
     * @return 处理结果，true表示成功，false表示失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateCompanyMarginalFactor(String accountingPeriod) {
        log.info("开始执行边际最低资本贡献率计算（公司层面），账期：{}", accountingPeriod);

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
            // 步骤1：读取项目定义表中有公司层面公式的项目
            log.info("步骤1：读取项目定义表中有公司层面公式的项目");
            List<Map<String, Object>> companyItemDefinitions = marginalCapitalCalculationMapper.selectItemDefinitionsWithCompanyFormula();

            if (companyItemDefinitions == null || companyItemDefinitions.isEmpty()) {
                log.warn("未找到有公司层面公式的项目定义，跳过计算");
                return true;
            }

            log.info("找到{}个有公司层面公式的项目，开始计算", companyItemDefinitions.size());

            int calculatedCount = 0;
            for (Map<String, Object> itemDef : companyItemDefinitions) {
                String itemCode = (String) itemDef.get("item_code");
                String formula = (String) itemDef.get("company_factor_formula");

                log.info("计算项目{}的公司层面边际最低资本贡献因子，公式：{}", itemCode, formula);

                // 使用Aviator执行公式计算
                BigDecimal companyFactor = aviatorFormulaUtil.executeCompanyFactorFormula(formula, accountingPeriod);

                if (companyFactor != null) {
                    // 更新计算结果
                    int updateCount = marginalCapitalCalculationMapper.updateCompanyMarginalFactor(itemCode, accountingPeriod, companyFactor);
                    if (updateCount > 0) {
                        calculatedCount++;
                        log.info("项目{}计算完成，公司层面边际最低资本贡献因子：{}", itemCode, companyFactor);
                    } else {
                        log.warn("项目{}更新失败，可能不存在对应的记录", itemCode);
                    }
                } else {
                    log.error("项目{}计算失败，公式：{}", itemCode, formula);
                }
            }

            log.info("公司层面边际最低资本贡献率计算完成，账期：{}，成功计算{}个项目", accountingPeriod, calculatedCount);
            return true;

        } catch (Exception e) {
            log.error("公司层面边际最低资本贡献率计算失败，账期：{}", accountingPeriod, e);
            throw e; // 重新抛出异常，触发事务回滚
        }
    }
}
