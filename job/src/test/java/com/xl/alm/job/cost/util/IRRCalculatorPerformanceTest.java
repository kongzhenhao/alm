package com.xl.alm.job.cost.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * IRR计算器性能测试类
 */
@Slf4j
public class IRRCalculatorPerformanceTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 测试IRR计算性能
     */
    @Test
    public void testIRRPerformance() {
        // 准备测试数据
        List<BigDecimal> cashflows = generateTestCashflows(1272);

        // 测试原始IRR计算器性能
        long startTime = System.nanoTime();
        BigDecimal originalResult = IRRCalculator.calculateIRR(cashflows);
        long originalTime = System.nanoTime() - startTime;

        // 测试优化后的IRR计算器性能
        startTime = System.nanoTime();
        BigDecimal fastResult = FastXirrCalculator.calculateIRR(cashflows);
        long fastTime = System.nanoTime() - startTime;

        // 输出结果
        log.info("原始IRR计算器结果: {}, 耗时: {}毫秒", originalResult, TimeUnit.NANOSECONDS.toMillis(originalTime));
        log.info("优化IRR计算器结果: {}, 耗时: {}毫秒", fastResult, TimeUnit.NANOSECONDS.toMillis(fastTime));
        log.info("性能提升: {}倍", (double) originalTime / fastTime);

        // 测试缓存性能
        startTime = System.nanoTime();
        FastXirrCalculator.calculateIRR(cashflows);
        long cachedTime = System.nanoTime() - startTime;
        log.info("缓存后计算耗时: {}毫秒", TimeUnit.NANOSECONDS.toMillis(cachedTime));
        log.info("缓存性能提升: {}倍", (double) fastTime / cachedTime);
    }

    /**
     * 测试XIRR计算性能
     */
    @Test
    public void testXIRRPerformance() {
        // 准备测试数据
        List<BigDecimal> cashflows = generateTestCashflows(1272);
        List<String> dates = generateTestDates("202301", 1272);

        // 测试XIRR计算器性能
        long startTime = System.nanoTime();
        BigDecimal xirrResult = FastXirrWithDatesCalculator.calculateXirr(cashflows, dates);
        long xirrTime = System.nanoTime() - startTime;

        // 输出结果
        log.info("XIRR计算器结果: {}, 耗时: {}毫秒", xirrResult, TimeUnit.NANOSECONDS.toMillis(xirrTime));

        // 测试缓存性能
        startTime = System.nanoTime();
        FastXirrWithDatesCalculator.calculateXirr(cashflows, dates);
        long cachedTime = System.nanoTime() - startTime;
        log.info("缓存后计算耗时: {}毫秒", TimeUnit.NANOSECONDS.toMillis(cachedTime));
        log.info("缓存性能提升: {}倍", (double) xirrTime / cachedTime);
    }

    /**
     * 测试JSON XIRR计算性能
     */
    @Test
    public void testJsonXIRRPerformance() {
        // 准备测试数据
        Map<String, Map<String, String>> cashflowJson = generateTestCashflowJson("202301", 1272);

        // 测试JSON XIRR计算器性能
        long startTime = System.nanoTime();
        BigDecimal xirrResult = FastXirrWithDatesCalculator.calculateXirrFromJson(cashflowJson);
        long xirrTime = System.nanoTime() - startTime;

        // 输出结果
        log.info("JSON XIRR计算器结果: {}, 耗时: {}毫秒", xirrResult, TimeUnit.NANOSECONDS.toMillis(xirrTime));

        // 测试缓存性能
        startTime = System.nanoTime();
        FastXirrWithDatesCalculator.calculateXirrFromJson(cashflowJson);
        long cachedTime = System.nanoTime() - startTime;
        log.info("缓存后计算耗时: {}毫秒", TimeUnit.NANOSECONDS.toMillis(cachedTime));
        log.info("缓存性能提升: {}倍", (double) xirrTime / cachedTime);
    }

    /**
     * 生成测试现金流
     */
    private List<BigDecimal> generateTestCashflows(int size) {
        List<BigDecimal> cashflows = new ArrayList<>(size);

        // 添加初始投资（负值）
        cashflows.add(new BigDecimal("-10000"));

        // 添加后续现金流
        for (int i = 1; i < size; i++) {
            // 模拟一些现金流模式
            BigDecimal value;
            if (i % 12 == 0) {
                // 每年有一个较大的正现金流
                value = new BigDecimal("1200");
            } else {
                // 其他月份有小额现金流
                value = new BigDecimal("100");
            }
            cashflows.add(value);
        }

        return cashflows;
    }

    /**
     * 生成测试日期
     */
    private List<String> generateTestDates(String startPeriod, int size) {
        List<String> dates = new ArrayList<>(size);

        int year = Integer.parseInt(startPeriod.substring(0, 4));
        int month = Integer.parseInt(startPeriod.substring(4, 6));

        // 添加初始日期
        LocalDate date = LocalDate.of(year, month, getLastDayOfMonth(year, month));
        dates.add(date.format(DATE_FORMATTER));

        // 添加后续日期
        for (int i = 1; i < size; i++) {
            // 增加一个月
            month++;
            if (month > 12) {
                year++;
                month = 1;
            }

            date = LocalDate.of(year, month, getLastDayOfMonth(year, month));
            dates.add(date.format(DATE_FORMATTER));
        }

        return dates;
    }

    /**
     * 生成测试现金流JSON
     */
    private Map<String, Map<String, String>> generateTestCashflowJson(String startPeriod, int size) {
        Map<String, Map<String, String>> cashflowJson = new HashMap<>(size);

        int year = Integer.parseInt(startPeriod.substring(0, 4));
        int month = Integer.parseInt(startPeriod.substring(4, 6));

        // 添加初始投资
        LocalDate date = LocalDate.of(year, month, getLastDayOfMonth(year, month));
        Map<String, String> initialEntry = new HashMap<>();
        initialEntry.put("date", date.format(DATE_FORMATTER));
        initialEntry.put("value", "-10000");
        cashflowJson.put("0", initialEntry);

        // 添加后续现金流
        for (int i = 1; i < size; i++) {
            // 增加一个月
            month++;
            if (month > 12) {
                year++;
                month = 1;
            }

            date = LocalDate.of(year, month, getLastDayOfMonth(year, month));

            Map<String, String> entry = new HashMap<>();
            entry.put("date", date.format(DATE_FORMATTER));

            // 模拟一些现金流模式
            String value;
            if (i % 12 == 0) {
                // 每年有一个较大的正现金流
                value = "1200";
            } else {
                // 其他月份有小额现金流
                value = "100";
            }
            entry.put("value", value);

            cashflowJson.put(String.valueOf(i), entry);
        }

        return cashflowJson;
    }

    /**
     * 获取月末日期
     */
    private int getLastDayOfMonth(int year, int month) {
        switch (month) {
            case 2:
                return (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
    }
}
