package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.DateUtils;
import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.entity.CostBusinessTypeSummaryEntity;
import com.xl.alm.app.mapper.CostBusinessTypeSummaryMapper;
import com.xl.alm.app.query.CostBusinessTypeSummaryQuery;
import com.xl.alm.app.service.ICostBusinessTypeSummaryService;
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
 * 分业务类型汇总表 服务实现类
 *
 * @author AI Assistant
 */
@Service
public class CostBusinessTypeSummaryServiceImpl implements ICostBusinessTypeSummaryService {
    private static final Logger log = LoggerFactory.getLogger(CostBusinessTypeSummaryServiceImpl.class);

    @Autowired
    private CostBusinessTypeSummaryMapper costBusinessTypeSummaryMapper;

    /**
     * 查询分业务类型汇总列表
     *
     * @param query 分业务类型汇总查询条件
     * @return 分业务类型汇总列表
     */
    @Override
    public List<CostBusinessTypeSummaryEntity> selectCostBusinessTypeSummaryList(CostBusinessTypeSummaryQuery query) {
        return costBusinessTypeSummaryMapper.selectCostBusinessTypeSummaryList(query);
    }

    /**
     * 根据ID查询分业务类型汇总
     *
     * @param id 分业务类型汇总ID
     * @return 分业务类型汇总
     */
    @Override
    public CostBusinessTypeSummaryEntity selectCostBusinessTypeSummaryById(Long id) {
        return costBusinessTypeSummaryMapper.selectCostBusinessTypeSummaryById(id);
    }

    /**
     * 新增分业务类型汇总
     *
     * @param costBusinessTypeSummary 分业务类型汇总
     * @return 结果
     */
    @Override
    public int insertCostBusinessTypeSummary(CostBusinessTypeSummaryEntity costBusinessTypeSummary) {
        costBusinessTypeSummary.setCreateTime(DateUtils.getNowDate());
        return costBusinessTypeSummaryMapper.insertCostBusinessTypeSummary(costBusinessTypeSummary);
    }

    /**
     * 修改分业务类型汇总
     *
     * @param costBusinessTypeSummary 分业务类型汇总
     * @return 结果
     */
    @Override
    public int updateCostBusinessTypeSummary(CostBusinessTypeSummaryEntity costBusinessTypeSummary) {
        costBusinessTypeSummary.setUpdateTime(DateUtils.getNowDate());
        return costBusinessTypeSummaryMapper.updateCostBusinessTypeSummary(costBusinessTypeSummary);
    }

    /**
     * 批量删除分业务类型汇总
     *
     * @param ids 需要删除的分业务类型汇总ID
     * @return 结果
     */
    @Override
    public int deleteCostBusinessTypeSummaryByIds(Long[] ids) {
        return costBusinessTypeSummaryMapper.deleteCostBusinessTypeSummaryByIds(ids);
    }

    /**
     * 删除分业务类型汇总信息
     *
     * @param id 分业务类型汇总ID
     * @return 结果
     */
    @Override
    public int deleteCostBusinessTypeSummaryById(Long id) {
        return costBusinessTypeSummaryMapper.deleteCostBusinessTypeSummaryById(id);
    }

    /**
     * 导入分业务类型汇总数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    @Override
    public String importCostBusinessTypeSummary(MultipartFile file, boolean updateSupport, String operName) {
        try {
            ExcelUtil<CostBusinessTypeSummaryEntity> util = new ExcelUtil<>(CostBusinessTypeSummaryEntity.class);
            List<CostBusinessTypeSummaryEntity> summaryList = util.importExcel(file.getInputStream());
            int successNum = 0;
            int failureNum = 0;
            StringBuilder successMsg = new StringBuilder();
            StringBuilder failureMsg = new StringBuilder();

            for (CostBusinessTypeSummaryEntity summary : summaryList) {
                try {
                    // 验证必填字段
                    if (StringUtils.isBlank(summary.getScenarioName()) ||
                        StringUtils.isBlank(summary.getBusinessType()) ||
                        StringUtils.isBlank(summary.getAccountingPeriod()) ||
                        StringUtils.isBlank(summary.getDesignType())) {
                        failureNum++;
                        failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据情景名称、业务类型、账期或设计类型为空");
                        continue;
                    }

                    // 设置默认值
                    if (summary.getStatutoryReserveT0() == null) {
                        summary.setStatutoryReserveT0(BigDecimal.ZERO);
                    }
                    if (summary.getStatutoryReserveT1() == null) {
                        summary.setStatutoryReserveT1(BigDecimal.ZERO);
                    }
                    if (summary.getStatutoryReserveT2() == null) {
                        summary.setStatutoryReserveT2(BigDecimal.ZERO);
                    }
                    if (summary.getStatutoryReserveT3() == null) {
                        summary.setStatutoryReserveT3(BigDecimal.ZERO);
                    }
                    if (summary.getFundCostRateT0() == null) {
                        summary.setFundCostRateT0(BigDecimal.ZERO);
                    }
                    if (summary.getFundCostRateT1() == null) {
                        summary.setFundCostRateT1(BigDecimal.ZERO);
                    }
                    if (summary.getFundCostRateT2() == null) {
                        summary.setFundCostRateT2(BigDecimal.ZERO);
                    }
                    if (summary.getFundCostRateT3() == null) {
                        summary.setFundCostRateT3(BigDecimal.ZERO);
                    }
                    if (summary.getGuaranteedCostRateT0() == null) {
                        summary.setGuaranteedCostRateT0(BigDecimal.ZERO);
                    }
                    if (summary.getGuaranteedCostRateT1() == null) {
                        summary.setGuaranteedCostRateT1(BigDecimal.ZERO);
                    }
                    if (summary.getGuaranteedCostRateT2() == null) {
                        summary.setGuaranteedCostRateT2(BigDecimal.ZERO);
                    }
                    if (summary.getGuaranteedCostRateT3() == null) {
                        summary.setGuaranteedCostRateT3(BigDecimal.ZERO);
                    }

                    // 查询是否已存在
                    CostBusinessTypeSummaryEntity existingSummary = costBusinessTypeSummaryMapper.selectCostBusinessTypeSummaryByKey(
                            summary.getScenarioName(), summary.getBusinessType(), summary.getAccountingPeriod(), summary.getDesignType());

                    if (existingSummary != null) {
                        if (updateSupport) {
                            summary.setId(existingSummary.getId());
                            summary.setUpdateBy(operName);
                            costBusinessTypeSummaryMapper.updateCostBusinessTypeSummary(summary);
                            successNum++;
                            successMsg.append("<br/>第 ").append(successNum).append(" 条数据更新成功");
                        } else {
                            failureNum++;
                            failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据已存在");
                        }
                    } else {
                        summary.setCreateBy(operName);
                        costBusinessTypeSummaryMapper.insertCostBusinessTypeSummary(summary);
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
            log.error("导入分业务类型汇总数据异常", e);
            return "导入分业务类型汇总数据失败，请联系管理员";
        }
    }

    /**
     * 导出分业务类型汇总数据模板
     *
     * @param response HTTP响应对象
     */
    @Override
    public void importTemplateCostBusinessTypeSummary(HttpServletResponse response) {
        ExcelUtil<CostBusinessTypeSummaryEntity> util = new ExcelUtil<>(CostBusinessTypeSummaryEntity.class);
        util.exportTemplateExcel(response, "分业务类型汇总数据");
    }
}
