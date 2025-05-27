package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.DateUtils;
import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.entity.CostProductStatisticsEntity;
import com.xl.alm.app.mapper.CostProductStatisticsMapper;
import com.xl.alm.app.query.CostProductStatisticsQuery;
import com.xl.alm.app.service.ICostProductStatisticsService;
import com.xl.alm.app.util.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 分产品统计表 服务实现类
 *
 * @author AI Assistant
 */
@Service
public class CostProductStatisticsServiceImpl implements ICostProductStatisticsService {
    private static final Logger log = LoggerFactory.getLogger(CostProductStatisticsServiceImpl.class);

    @Autowired
    private CostProductStatisticsMapper costProductStatisticsMapper;

    /**
     * 查询分产品统计列表
     *
     * @param query 分产品统计查询条件
     * @return 分产品统计列表
     */
    @Override
    public List<CostProductStatisticsEntity> selectCostProductStatisticsList(CostProductStatisticsQuery query) {
        return costProductStatisticsMapper.selectCostProductStatisticsList(query);
    }

    /**
     * 根据ID查询分产品统计
     *
     * @param id 分产品统计ID
     * @return 分产品统计
     */
    @Override
    public CostProductStatisticsEntity selectCostProductStatisticsById(Long id) {
        return costProductStatisticsMapper.selectCostProductStatisticsById(id);
    }

    /**
     * 新增分产品统计
     *
     * @param costProductStatistics 分产品统计
     * @return 结果
     */
    @Override
    public int insertCostProductStatistics(CostProductStatisticsEntity costProductStatistics) {
        costProductStatistics.setCreateTime(DateUtils.getNowDate());
        return costProductStatisticsMapper.insertCostProductStatistics(costProductStatistics);
    }

    /**
     * 修改分产品统计
     *
     * @param costProductStatistics 分产品统计
     * @return 结果
     */
    @Override
    public int updateCostProductStatistics(CostProductStatisticsEntity costProductStatistics) {
        costProductStatistics.setUpdateTime(DateUtils.getNowDate());
        return costProductStatisticsMapper.updateCostProductStatistics(costProductStatistics);
    }

    /**
     * 批量删除分产品统计
     *
     * @param ids 需要删除的分产品统计ID
     * @return 结果
     */
    @Override
    public int deleteCostProductStatisticsByIds(Long[] ids) {
        return costProductStatisticsMapper.deleteCostProductStatisticsByIds(ids);
    }

    /**
     * 删除分产品统计信息
     *
     * @param id 分产品统计ID
     * @return 结果
     */
    @Override
    public int deleteCostProductStatisticsById(Long id) {
        return costProductStatisticsMapper.deleteCostProductStatisticsById(id);
    }

    /**
     * 导入分产品统计数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    @Override
    public String importCostProductStatistics(MultipartFile file, boolean updateSupport, String operName) {
        try {
            ExcelUtil<CostProductStatisticsEntity> util = new ExcelUtil<>(CostProductStatisticsEntity.class);
            List<CostProductStatisticsEntity> statsList = util.importExcel(file.getInputStream());
            int successNum = 0;
            int failureNum = 0;
            StringBuilder successMsg = new StringBuilder();
            StringBuilder failureMsg = new StringBuilder();

            for (CostProductStatisticsEntity stats : statsList) {
                try {
                    // 验证必填字段
                    if (StringUtils.isBlank(stats.getScenarioName()) ||
                        StringUtils.isBlank(stats.getBusinessType()) ||
                        StringUtils.isBlank(stats.getAccountingPeriod()) ||
                        StringUtils.isBlank(stats.getActuarialCode())) {
                        failureNum++;
                        failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据情景名称、业务类型、账期或精算代码为空");
                        continue;
                    }

                    // 设置默认值
                    if (StringUtils.isBlank(stats.getTermType())) {
                        stats.setTermType("L");
                    }
                    if (StringUtils.isBlank(stats.getShortTermFlag())) {
                        stats.setShortTermFlag("N");
                    }
                    if (stats.getStatutoryReserveT0() == null) {
                        stats.setStatutoryReserveT0(BigDecimal.ZERO);
                    }
                    if (stats.getStatutoryReserveT1() == null) {
                        stats.setStatutoryReserveT1(BigDecimal.ZERO);
                    }
                    if (stats.getStatutoryReserveT2() == null) {
                        stats.setStatutoryReserveT2(BigDecimal.ZERO);
                    }
                    if (stats.getStatutoryReserveT3() == null) {
                        stats.setStatutoryReserveT3(BigDecimal.ZERO);
                    }
                    if (stats.getFundCostRateT0() == null) {
                        stats.setFundCostRateT0(BigDecimal.ZERO);
                    }
                    if (stats.getFundCostRateT1() == null) {
                        stats.setFundCostRateT1(BigDecimal.ZERO);
                    }
                    if (stats.getFundCostRateT2() == null) {
                        stats.setFundCostRateT2(BigDecimal.ZERO);
                    }
                    if (stats.getFundCostRateT3() == null) {
                        stats.setFundCostRateT3(BigDecimal.ZERO);
                    }
                    if (stats.getGuaranteedCostRateT0() == null) {
                        stats.setGuaranteedCostRateT0(BigDecimal.ZERO);
                    }
                    if (stats.getGuaranteedCostRateT1() == null) {
                        stats.setGuaranteedCostRateT1(BigDecimal.ZERO);
                    }
                    if (stats.getGuaranteedCostRateT2() == null) {
                        stats.setGuaranteedCostRateT2(BigDecimal.ZERO);
                    }
                    if (stats.getGuaranteedCostRateT3() == null) {
                        stats.setGuaranteedCostRateT3(BigDecimal.ZERO);
                    }

                    // 查询是否已存在
                    CostProductStatisticsEntity existingStats = costProductStatisticsMapper.selectCostProductStatisticsByCode(
                            stats.getActuarialCode(), stats.getAccountingPeriod(), stats.getScenarioName(), stats.getBusinessType());

                    if (existingStats != null) {
                        if (updateSupport) {
                            stats.setId(existingStats.getId());
                            stats.setUpdateBy(operName);
                            costProductStatisticsMapper.updateCostProductStatistics(stats);
                            successNum++;
                            successMsg.append("<br/>第 ").append(successNum).append(" 条数据更新成功");
                        } else {
                            failureNum++;
                            failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据已存在");
                        }
                    } else {
                        stats.setCreateBy(operName);
                        costProductStatisticsMapper.insertCostProductStatistics(stats);
                        successNum++;
                        successMsg.append("<br/>第 ").append(successNum).append(" 条数据导入成功");
                    }
                } catch (Exception e) {
                    failureNum++;
                    String msg = "<br/>第 " + failureNum + " 条数据导入失败：";
                    failureMsg.append(msg).append(e.getMessage());
                    log.error(msg, e);
                }
            }

            if (failureNum > 0) {
                failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
                throw new RuntimeException(failureMsg.toString());
            } else {
                successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
            }
            return successMsg.toString();
        } catch (Exception e) {
            log.error("导入分产品统计数据异常", e);
            return "导入分产品统计数据失败，请联系管理员";
        }
    }

    /**
     * 导出分产品统计数据模板
     *
     * @param response HTTP响应对象
     */
    @Override
    public void importTemplateCostProductStatistics(HttpServletResponse response) {
        ExcelUtil<CostProductStatisticsEntity> util = new ExcelUtil<>(CostProductStatisticsEntity.class);
        util.exportTemplateExcel(response, "分产品统计数据");
    }
}
