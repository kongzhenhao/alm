package com.xl.alm.job.minc.service.impl;

import com.xl.alm.job.minc.entity.MinCapitalSummaryCalculationEntity;
import com.xl.alm.job.minc.mapper.MinCapitalSummaryCalculationMapper;
import com.xl.alm.job.minc.service.MinCapitalSummaryCalculationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 最低资本明细汇总数据计算服务实现类
 * 对应UC0006：计算最低资本明细汇总数据
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class MinCapitalSummaryCalculationServiceImpl implements MinCapitalSummaryCalculationService {

    @Autowired
    private MinCapitalSummaryCalculationMapper minCapitalSummaryCalculationMapper;

    /**
     * 执行最低资本明细汇总数据计算
     *
     * @param accountPeriod 账期，格式：YYYYMM（如202306）
     * @return 处理结果，true表示成功，false表示失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean calculateMinCapitalSummary(String accountPeriod) {
        log.info("开始执行最低资本明细汇总数据计算，账期：{}", accountPeriod);

        // 参数校验
        if (!StringUtils.hasText(accountPeriod)) {
            log.error("账期参数不能为空");
            return false;
        }

        if (accountPeriod.length() != 6) {
            log.error("账期格式错误，应为YYYYMM格式，当前值：{}", accountPeriod);
            return false;
        }

        try {
            // 步骤1：数据准备 - 从TB0001表汇总数据，只计算市场风险和信用风险项目
            log.info("步骤1：从分部门最低资本明细表(TB0001)汇总数据，账期：{}，只计算风险类型为'市场风险'和'信用风险'的项目", accountPeriod);
            List<MinCapitalSummaryCalculationEntity> summaryDataList =
                minCapitalSummaryCalculationMapper.selectSummaryFromDeptMincapDetail(accountPeriod);

            if (summaryDataList == null || summaryDataList.isEmpty()) {
                log.warn("未找到账期{}的市场风险和信用风险相关的分部门最低资本明细数据，跳过汇总计算", accountPeriod);
                return true; // 没有数据不算失败
            }

            log.info("从TB0001表汇总得到{}条市场风险和信用风险相关记录", summaryDataList.size());

            // 步骤2：清理目标账期的TB0007表数据
            log.info("步骤2：清理目标账期的最低资本明细汇总表(TB0007)数据，账期：{}", accountPeriod);
            int deletedCount = minCapitalSummaryCalculationMapper.deleteMinCapitalSummaryByPeriod(accountPeriod);
            log.info("删除了{}条历史汇总记录", deletedCount);

            // 步骤3：数据入表 - 将汇总结果写入TB0007表
            log.info("步骤3：将汇总结果写入最低资本明细汇总表(TB0007)");

            // 设置默认值
            for (MinCapitalSummaryCalculationEntity entity : summaryDataList) {
                entity.setIsDel(0); // 默认未删除
            }

            int insertedCount = minCapitalSummaryCalculationMapper.batchInsertMinCapitalSummary(summaryDataList);
            log.info("成功插入{}条汇总记录到TB0007表", insertedCount);

            if (insertedCount != summaryDataList.size()) {
                log.error("插入记录数({})与汇总记录数({})不一致", insertedCount, summaryDataList.size());
                return false;
            }

            log.info("最低资本明细汇总数据计算完成，账期：{}，处理记录数：{}", accountPeriod, insertedCount);
            return true;

        } catch (Exception e) {
            log.error("最低资本明细汇总数据计算失败，账期：{}", accountPeriod, e);
            throw e; // 重新抛出异常，触发事务回滚
        }
    }
}
