package com.xl.alm.job.minc.util;

import com.googlecode.aviator.AviatorEvaluator;
import com.xl.alm.job.minc.mapper.MarginalCapitalCalculationMapper;
import com.xl.alm.job.minc.mapper.MarketCreditCapitalMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Aviator公式计算工具类
 * 用于解析和执行项目定义表中的计算公式
 *
 * @author AI Assistant
 */
@Slf4j
@Component
public class AviatorFormulaUtil {

    @Autowired
    private MarginalCapitalCalculationMapper marginalCapitalCalculationMapper;

    @Autowired
    private MarketCreditCapitalMapper marketCreditCapitalMapper;

    /**
     * 执行子风险层面边际最低资本贡献因子计算公式
     *
     * @param formula 公式字符串
     * @param accountingPeriod 账期
     * @return 计算结果
     */
    public BigDecimal executeSubRiskFactorFormula(String formula, String accountingPeriod) {
        try {
            log.info("开始执行公式计算，公式：{}", formula);

            // 准备环境变量
            Map<String, Object> env = prepareFormulaEnvironment(formula, accountingPeriod);

            // 打印所有环境变量
            log.info("准备执行公式，环境变量如下：");
            for (Map.Entry<String, Object> entry : env.entrySet()) {
                log.info("  {} = {}", entry.getKey(), entry.getValue());
            }

            // 执行公式计算
            Object result = AviatorEvaluator.execute(formula, env);

            if (result instanceof Number) {
                BigDecimal calculatedValue = new BigDecimal(result.toString());
                log.info("公式计算完成，结果：{}", calculatedValue);
                return calculatedValue;
            } else {
                log.error("公式计算结果类型错误，期望数字类型，实际：{}", result.getClass().getSimpleName());
                return null;
            }

        } catch (Exception e) {
            log.error("公式计算失败，公式：{}", formula, e);
            return null;
        }
    }

    /**
     * 准备公式执行环境变量
     *
     * @param formula 公式字符串
     * @param accountingPeriod 账期
     * @return 环境变量Map
     */
    private Map<String, Object> prepareFormulaEnvironment(String formula, String accountingPeriod) {
        Map<String, Object> env = new HashMap<>();

        log.info("开始解析公式变量，公式：{}", formula);

        // 先提取所有相关系数变量
        Pattern corrPattern = Pattern.compile("\\b[A-Z]{2}\\d{3}(?:_\\d{2})*_[A-Z]{2}\\d{3}(?:_\\d{2})*_CORR\\b");
        Matcher corrMatcher = corrPattern.matcher(formula);

        while (corrMatcher.find()) {
            String corrVar = corrMatcher.group();
            log.info("找到相关系数变量：{}", corrVar);

            // 解析相关系数变量，例如：IR001_NR001_CORR
            String corrPart = corrVar.replace("_CORR", ""); // IR001_NR001
            String[] parts = corrPart.split("_");

            if (parts.length >= 2) {
                String itemCodeX, itemCodeY;

                // 简单的解析逻辑：假设是两个基本项目编码
                if (parts.length == 2) {
                    itemCodeX = parts[0]; // IR001
                    itemCodeY = parts[1]; // NR001
                } else {
                    // 处理复杂的多级编码，需要更智能的解析
                    itemCodeX = reconstructItemCode(parts, 0);
                    itemCodeY = reconstructItemCode(parts, getSecondItemStartIndex(parts));
                }

                BigDecimal correlation = marginalCapitalCalculationMapper.selectCorrelationValue(itemCodeX, itemCodeY, accountingPeriod);
                env.put(corrVar, correlation != null ? correlation : BigDecimal.ZERO);
                log.info("添加相关系数变量：{} = {} ({}与{})", corrVar, correlation, itemCodeX, itemCodeY);
            }
        }

        // 直接添加公式中需要的金额变量（硬编码方式，更可靠）
        String[] itemCodes = {"IR001", "NR001", "MR001", "CR001", "QR003"};
        for (String itemCode : itemCodes) {
            if (formula.contains(itemCode) && !env.containsKey(itemCode)) {
                BigDecimal amount = marginalCapitalCalculationMapper.selectAmountByItemCode(itemCode, accountingPeriod);
                env.put(itemCode, amount != null ? amount : BigDecimal.ZERO);
                log.info("添加金额变量：{} = {}", itemCode, amount);
            }
        }

        // 处理多级项目编码（如IR001_01等）
        Pattern multiLevelPattern = Pattern.compile("\\b[A-Z]{2}\\d{3}(?:_\\d{2})+\\b");
        Matcher multiLevelMatcher = multiLevelPattern.matcher(formula);
        while (multiLevelMatcher.find()) {
            String itemCode = multiLevelMatcher.group();
            if (!env.containsKey(itemCode)) {
                BigDecimal amount = marginalCapitalCalculationMapper.selectAmountByItemCode(itemCode, accountingPeriod);
                env.put(itemCode, amount != null ? amount : BigDecimal.ZERO);
                log.info("添加多级金额变量：{} = {}", itemCode, amount);
            }
        }

        log.info("公式变量解析完成，共{}个变量", env.size());
        return env;
    }

    /**
     * 重新组合项目编码（处理多级编码如IR001_01_04）
     */
    private String reconstructItemCode(String[] parts, int startIndex) {
        StringBuilder sb = new StringBuilder();
        sb.append(parts[startIndex]);

        for (int i = startIndex + 1; i < parts.length; i++) {
            if (parts[i].matches("\\d{2}")) {
                sb.append("_").append(parts[i]);
            } else {
                break;
            }
        }

        return sb.toString();
    }

    /**
     * 获取第二个项目编码的起始索引
     */
    private int getSecondItemStartIndex(String[] parts) {
        // 找到第一个项目编码结束的位置
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].matches("[A-Z]{2}\\d{3}")) {
                return i;
            }
        }
        return parts.length / 2; // 默认从中间开始
    }

    /**
     * 执行公司层面边际最低资本贡献因子计算公式
     *
     * @param formula 公式字符串
     * @param accountingPeriod 账期
     * @return 计算结果
     */
    public BigDecimal executeCompanyFactorFormula(String formula, String accountingPeriod) {
        try {
            log.info("开始执行公司层面公式计算，公式：{}", formula);

            // 准备环境变量
            Map<String, Object> env = prepareCompanyFormulaEnvironment(formula, accountingPeriod);

            // 打印所有环境变量
            log.info("准备执行公司层面公式，环境变量如下：");
            for (Map.Entry<String, Object> entry : env.entrySet()) {
                log.info("  {} = {}", entry.getKey(), entry.getValue());
            }

            // 执行公式计算
            Object result = AviatorEvaluator.execute(formula, env);

            if (result instanceof Number) {
                BigDecimal calculatedValue = new BigDecimal(result.toString());
                log.info("公司层面公式计算完成，结果：{}", calculatedValue);
                return calculatedValue;
            } else {
                log.error("公司层面公式计算结果类型错误，期望数字类型，实际：{}", result.getClass().getSimpleName());
                return null;
            }

        } catch (Exception e) {
            log.error("公司层面公式计算失败，公式：{}", formula, e);
            return null;
        }
    }

    /**
     * 准备公司层面公式执行环境变量
     *
     * @param formula 公式字符串
     * @param accountingPeriod 账期
     * @return 环境变量Map
     */
    private Map<String, Object> prepareCompanyFormulaEnvironment(String formula, String accountingPeriod) {
        Map<String, Object> env = new HashMap<>();

        log.info("开始解析公司层面公式变量，公式：{}", formula);

        // 提取公式中的子风险因子变量（格式：项目编码_SUB）
        Pattern subFactorPattern = Pattern.compile("\\b[A-Z]{2}\\d{3}(?:_\\d{2})*_SUB\\b");
        Matcher subFactorMatcher = subFactorPattern.matcher(formula);

        while (subFactorMatcher.find()) {
            String subFactorVar = subFactorMatcher.group();
            // 提取项目编码（去掉_SUB后缀）
            String itemCode = subFactorVar.replace("_SUB", "");

            log.info("找到子风险因子变量：{} -> 项目编码：{}", subFactorVar, itemCode);

            // 从TB0006表查询子风险层面边际最低资本贡献因子
            BigDecimal subRiskFactor = marginalCapitalCalculationMapper.selectSubRiskMarginalFactor(itemCode, accountingPeriod);
            env.put(subFactorVar, subRiskFactor != null ? subRiskFactor : BigDecimal.ZERO);
            log.info("添加子风险因子变量：{} = {}", subFactorVar, subRiskFactor);
        }

        log.info("公司层面公式变量解析完成，共{}个变量", env.size());
        return env;
    }

    /**
     * 执行市场及信用最低资本计算公式
     *
     * @param formula 公式字符串
     * @param accountCode 账户编码
     * @param accountingPeriod 账期
     * @return 计算结果
     */
    public BigDecimal executeCapitalCalculationFormula(String formula, String accountCode, String accountingPeriod) {
        try {
            log.info("开始执行市场及信用最低资本公式计算，公式：{}，账户：{}", formula, accountCode);

            // 准备环境变量
            Map<String, Object> env = prepareCapitalFormulaEnvironment(formula, accountCode, accountingPeriod);

            // 打印所有环境变量
            log.info("准备执行市场及信用最低资本公式，环境变量如下：");
            for (Map.Entry<String, Object> entry : env.entrySet()) {
                log.info("  {} = {}", entry.getKey(), entry.getValue());
            }

            // 执行公式计算
            Object result = AviatorEvaluator.execute(formula, env);

            if (result instanceof Number) {
                BigDecimal calculatedValue = new BigDecimal(result.toString());
                log.info("市场及信用最低资本公式计算完成，结果：{}", calculatedValue);
                return calculatedValue;
            } else {
                log.error("市场及信用最低资本公式计算结果类型错误，期望数字类型，实际：{}", result.getClass().getSimpleName());
                return null;
            }

        } catch (Exception e) {
            log.error("市场及信用最低资本公式计算失败，公式：{}", formula, e);
            return null;
        }
    }

    /**
     * 准备市场及信用最低资本公式执行环境变量
     *
     * @param formula 公式字符串
     * @param accountCode 账户编码
     * @param accountingPeriod 账期
     * @return 环境变量Map
     */
    private Map<String, Object> prepareCapitalFormulaEnvironment(String formula, String accountCode, String accountingPeriod) {
        Map<String, Object> env = new HashMap<>();

        log.info("开始解析市场及信用最低资本公式变量，公式：{}", formula);

        // 提取公式中的金额变量（格式：项目编码）
        Pattern amountPattern = Pattern.compile("\\b[A-Z]{2}\\d{3}(?:_\\d{2})*\\b");
        Matcher amountMatcher = amountPattern.matcher(formula);

        while (amountMatcher.find()) {
            String itemCode = amountMatcher.group();

            // 跳过公司层面贡献因子变量（以_COMPANY结尾）
            if (itemCode.endsWith("_COMPANY")) {
                continue;
            }

            log.info("找到金额变量：{}", itemCode);

            // 从TB0007表查询金额
            BigDecimal amount = marketCreditCapitalMapper.selectAmountFromSummary(itemCode, accountCode, accountingPeriod);
            env.put(itemCode, amount != null ? amount : BigDecimal.ZERO);
            log.info("添加金额变量：{} = {}", itemCode, amount);
        }

        // 提取公式中的公司层面贡献因子变量（格式：项目编码_COMPANY）
        Pattern companyFactorPattern = Pattern.compile("\\b[A-Z]{2}\\d{3}(?:_\\d{2})*_COMPANY\\b");
        Matcher companyFactorMatcher = companyFactorPattern.matcher(formula);

        while (companyFactorMatcher.find()) {
            String companyFactorVar = companyFactorMatcher.group();
            // 提取项目编码（去掉_COMPANY后缀）
            String itemCode = companyFactorVar.replace("_COMPANY", "");

            log.info("找到公司层面贡献因子变量：{} -> 项目编码：{}", companyFactorVar, itemCode);

            // 从TB0006表查询公司层面边际最低资本贡献因子
            BigDecimal companyFactor = marketCreditCapitalMapper.selectCompanyMarginalFactor(itemCode, accountingPeriod);
            env.put(companyFactorVar, companyFactor != null ? companyFactor : BigDecimal.ZERO);
            log.info("添加公司层面贡献因子变量：{} = {}", companyFactorVar, companyFactor);
        }

        log.info("市场及信用最低资本公式变量解析完成，共{}个变量", env.size());
        return env;
    }
}
