package com.xl.alm.job.dur.service.impl;

import com.xl.alm.job.dur.entity.AccountLiabilityDv10Entity;
import com.xl.alm.job.dur.entity.LiabilityDv10ByDurationEntity;
import com.xl.alm.job.dur.mapper.AccountLiabilityDv10Mapper;
import com.xl.alm.job.dur.mapper.LiabilityDv10ByDurationMapper;
import com.xl.alm.job.dur.service.AccountLiabilityDv10Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * 分账户负债基点价值DV10服务实现类
 *
 * @author AI
 */
@Slf4j
@Service
public class AccountLiabilityDv10ServiceImpl implements AccountLiabilityDv10Service {

    @Autowired
    private LiabilityDv10ByDurationMapper liabilityDv10ByDurationMapper;

    @Autowired
    private AccountLiabilityDv10Mapper accountLiabilityDv10Mapper;

    /**
     * 计算分账户负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @return 执行结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateAccountLiabilityDv10(String accountPeriod) {
        log.info("【分账户DV10计算】开始计算分账户负债基点价值DV10，账期：{}", accountPeriod);
        long startTime = System.currentTimeMillis();

        try {
            // 1. 删除已有数据
            log.info("【分账户DV10计算-步骤1】开始删除已有数据，账期：{}", accountPeriod);
            int deleteCount = accountLiabilityDv10Mapper.deleteByAccountPeriod(accountPeriod);
            log.info("【分账户DV10计算-步骤1】删除已有数据完成，账期：{}，删除记录数：{}", accountPeriod, deleteCount);

            // 2. 查询分中短负债基点价值DV10数据
            log.info("【分账户DV10计算-步骤2】开始查询分中短负债基点价值DV10数据，账期：{}", accountPeriod);
            // 使用Mapper中定义的方法查询数据
            List<LiabilityDv10ByDurationEntity> liabilityDv10List = liabilityDv10ByDurationMapper.selectByAccountPeriod(accountPeriod);
            if (liabilityDv10List.isEmpty()) {
                log.warn("【分账户DV10计算-步骤2】未找到分中短负债基点价值DV10数据，账期：{}", accountPeriod);
                return false;
            }
            log.info("【分账户DV10计算-步骤2】查询分中短负债基点价值DV10数据完成，账期：{}，记录数：{}", accountPeriod, liabilityDv10List.size());

            // 3. 按设计类型汇总
            log.info("【分账户DV10计算-步骤3】开始按设计类型汇总，账期：{}", accountPeriod);
            List<AccountLiabilityDv10Entity> resultList = aggregateByDesignType(accountPeriod, liabilityDv10List);
            if (resultList.isEmpty()) {
                log.warn("【分账户DV10计算-步骤3】汇总结果为空，账期：{}", accountPeriod);
                return true;
            }
            log.info("【分账户DV10计算-步骤3】按设计类型汇总完成，账期：{}，记录数：{}", accountPeriod, resultList.size());

            // 4. 批量插入数据
            log.info("【分账户DV10计算-步骤4】开始批量插入数据，账期：{}", accountPeriod);
            int insertCount = accountLiabilityDv10Mapper.batchInsert(resultList);
            log.info("【分账户DV10计算-步骤4】批量插入数据完成，账期：{}，插入记录数：{}", accountPeriod, insertCount);

            log.info("【分账户DV10计算】计算分账户负债基点价值DV10完成，账期：{}，总耗时：{}ms",
                    accountPeriod, System.currentTimeMillis() - startTime);
            return true;
        } catch (Exception e) {
            log.error("【分账户DV10计算】计算分账户负债基点价值DV10失败，账期：{}", accountPeriod, e);
            throw e;
        }
    }

    /**
     * 按设计类型汇总
     *
     * @param accountPeriod 账期
     * @param liabilityDv10List 分中短负债基点价值DV10列表
     * @return 分账户负债基点价值DV10列表
     */
    private List<AccountLiabilityDv10Entity> aggregateByDesignType(String accountPeriod, List<LiabilityDv10ByDurationEntity> liabilityDv10List) {
        // 按设计类型、现金流类型和值类型分组
        Map<String, List<LiabilityDv10ByDurationEntity>> groupedData = new HashMap<>();

        for (LiabilityDv10ByDurationEntity entity : liabilityDv10List) {
            String key = entity.getDesignType() + "|" + entity.getCashFlowType() + "|" + entity.getValueType();
            if (!groupedData.containsKey(key)) {
                groupedData.put(key, new ArrayList<>());
            }
            groupedData.get(key).add(entity);
        }

        // 汇总结果列表
        List<AccountLiabilityDv10Entity> resultList = new ArrayList<>();

        // 处理每个分组
        for (Map.Entry<String, List<LiabilityDv10ByDurationEntity>> entry : groupedData.entrySet()) {
            String[] keyParts = entry.getKey().split("\\|");
            String designType = keyParts[0];
            String cashFlowType = keyParts[1];
            String valueType = keyParts[2];

            List<LiabilityDv10ByDurationEntity> groupEntities = entry.getValue();

            // 创建汇总实体
            AccountLiabilityDv10Entity resultEntity = new AccountLiabilityDv10Entity();
            resultEntity.setAccountPeriod(accountPeriod);
            resultEntity.setDesignType(designType);
            resultEntity.setCashFlowType(cashFlowType);
            resultEntity.setValueType(valueType);

            // 汇总各期限点的值
            aggregatePeriodValues(resultEntity, groupEntities);

            // 设置创建和更新信息
            Date now = new Date();
            resultEntity.setCreateTime(now);
            resultEntity.setUpdateTime(now);
            resultEntity.setCreateBy("system");
            resultEntity.setUpdateBy("system");
            resultEntity.setIsDel(0);

            resultList.add(resultEntity);
        }

        return resultList;
    }

    /**
     * 汇总各期限点的值
     *
     * @param resultEntity 结果实体
     * @param groupEntities 分组实体列表
     */
    private void aggregatePeriodValues(AccountLiabilityDv10Entity resultEntity, List<LiabilityDv10ByDurationEntity> groupEntities) {
        // 初始化各期限点的值为0
        resultEntity.setTerm0(BigDecimal.ZERO);
        resultEntity.setTerm05(BigDecimal.ZERO);
        resultEntity.setTerm1(BigDecimal.ZERO);
        resultEntity.setTerm2(BigDecimal.ZERO);
        resultEntity.setTerm3(BigDecimal.ZERO);
        resultEntity.setTerm4(BigDecimal.ZERO);
        resultEntity.setTerm5(BigDecimal.ZERO);
        resultEntity.setTerm6(BigDecimal.ZERO);
        resultEntity.setTerm7(BigDecimal.ZERO);
        resultEntity.setTerm8(BigDecimal.ZERO);
        resultEntity.setTerm10(BigDecimal.ZERO);
        resultEntity.setTerm12(BigDecimal.ZERO);
        resultEntity.setTerm15(BigDecimal.ZERO);
        resultEntity.setTerm20(BigDecimal.ZERO);
        resultEntity.setTerm25(BigDecimal.ZERO);
        resultEntity.setTerm30(BigDecimal.ZERO);
        resultEntity.setTerm35(BigDecimal.ZERO);
        resultEntity.setTerm40(BigDecimal.ZERO);
        resultEntity.setTerm45(BigDecimal.ZERO);
        resultEntity.setTerm50(BigDecimal.ZERO);

        // 汇总各期限点的值
        for (LiabilityDv10ByDurationEntity entity : groupEntities) {
            resultEntity.setTerm0(resultEntity.getTerm0().add(entity.getTerm0() != null ? entity.getTerm0() : BigDecimal.ZERO));
            resultEntity.setTerm05(resultEntity.getTerm05().add(entity.getTerm05() != null ? entity.getTerm05() : BigDecimal.ZERO));
            resultEntity.setTerm1(resultEntity.getTerm1().add(entity.getTerm1() != null ? entity.getTerm1() : BigDecimal.ZERO));
            resultEntity.setTerm2(resultEntity.getTerm2().add(entity.getTerm2() != null ? entity.getTerm2() : BigDecimal.ZERO));
            resultEntity.setTerm3(resultEntity.getTerm3().add(entity.getTerm3() != null ? entity.getTerm3() : BigDecimal.ZERO));
            resultEntity.setTerm4(resultEntity.getTerm4().add(entity.getTerm4() != null ? entity.getTerm4() : BigDecimal.ZERO));
            resultEntity.setTerm5(resultEntity.getTerm5().add(entity.getTerm5() != null ? entity.getTerm5() : BigDecimal.ZERO));
            resultEntity.setTerm6(resultEntity.getTerm6().add(entity.getTerm6() != null ? entity.getTerm6() : BigDecimal.ZERO));
            resultEntity.setTerm7(resultEntity.getTerm7().add(entity.getTerm7() != null ? entity.getTerm7() : BigDecimal.ZERO));
            resultEntity.setTerm8(resultEntity.getTerm8().add(entity.getTerm8() != null ? entity.getTerm8() : BigDecimal.ZERO));
            resultEntity.setTerm10(resultEntity.getTerm10().add(entity.getTerm10() != null ? entity.getTerm10() : BigDecimal.ZERO));
            resultEntity.setTerm12(resultEntity.getTerm12().add(entity.getTerm12() != null ? entity.getTerm12() : BigDecimal.ZERO));
            resultEntity.setTerm15(resultEntity.getTerm15().add(entity.getTerm15() != null ? entity.getTerm15() : BigDecimal.ZERO));
            resultEntity.setTerm20(resultEntity.getTerm20().add(entity.getTerm20() != null ? entity.getTerm20() : BigDecimal.ZERO));
            resultEntity.setTerm25(resultEntity.getTerm25().add(entity.getTerm25() != null ? entity.getTerm25() : BigDecimal.ZERO));
            resultEntity.setTerm30(resultEntity.getTerm30().add(entity.getTerm30() != null ? entity.getTerm30() : BigDecimal.ZERO));
            resultEntity.setTerm35(resultEntity.getTerm35().add(entity.getTerm35() != null ? entity.getTerm35() : BigDecimal.ZERO));
            resultEntity.setTerm40(resultEntity.getTerm40().add(entity.getTerm40() != null ? entity.getTerm40() : BigDecimal.ZERO));
            resultEntity.setTerm45(resultEntity.getTerm45().add(entity.getTerm45() != null ? entity.getTerm45() : BigDecimal.ZERO));
            resultEntity.setTerm50(resultEntity.getTerm50().add(entity.getTerm50() != null ? entity.getTerm50() : BigDecimal.ZERO));
        }
    }
}
