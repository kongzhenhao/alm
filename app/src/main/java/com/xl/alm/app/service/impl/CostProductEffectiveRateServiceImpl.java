package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.DateUtils;
import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.entity.CostProductEffectiveRateEntity;
import com.xl.alm.app.mapper.CostProductEffectiveRateMapper;
import com.xl.alm.app.query.CostProductEffectiveRateQuery;
import com.xl.alm.app.service.ICostProductEffectiveRateService;
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
 * 分产品有效成本率表 服务实现类
 *
 * @author AI Assistant
 */
@Service
public class CostProductEffectiveRateServiceImpl implements ICostProductEffectiveRateService {
    private static final Logger log = LoggerFactory.getLogger(CostProductEffectiveRateServiceImpl.class);

    @Autowired
    private CostProductEffectiveRateMapper costProductEffectiveRateMapper;

    /**
     * 查询分产品有效成本率列表
     *
     * @param query 分产品有效成本率查询条件
     * @return 分产品有效成本率列表
     */
    @Override
    public List<CostProductEffectiveRateEntity> selectCostProductEffectiveRateList(CostProductEffectiveRateQuery query) {
        return costProductEffectiveRateMapper.selectCostProductEffectiveRateList(query);
    }

    /**
     * 根据ID查询分产品有效成本率
     *
     * @param id 分产品有效成本率ID
     * @return 分产品有效成本率
     */
    @Override
    public CostProductEffectiveRateEntity selectCostProductEffectiveRateById(Long id) {
        return costProductEffectiveRateMapper.selectCostProductEffectiveRateById(id);
    }

    /**
     * 新增分产品有效成本率
     *
     * @param costProductEffectiveRate 分产品有效成本率
     * @return 结果
     */
    @Override
    public int insertCostProductEffectiveRate(CostProductEffectiveRateEntity costProductEffectiveRate) {
        costProductEffectiveRate.setCreateTime(DateUtils.getNowDate());
        return costProductEffectiveRateMapper.insertCostProductEffectiveRate(costProductEffectiveRate);
    }

    /**
     * 修改分产品有效成本率
     *
     * @param costProductEffectiveRate 分产品有效成本率
     * @return 结果
     */
    @Override
    public int updateCostProductEffectiveRate(CostProductEffectiveRateEntity costProductEffectiveRate) {
        costProductEffectiveRate.setUpdateTime(DateUtils.getNowDate());
        return costProductEffectiveRateMapper.updateCostProductEffectiveRate(costProductEffectiveRate);
    }

    /**
     * 批量删除分产品有效成本率
     *
     * @param ids 需要删除的分产品有效成本率ID
     * @return 结果
     */
    @Override
    public int deleteCostProductEffectiveRateByIds(Long[] ids) {
        return costProductEffectiveRateMapper.deleteCostProductEffectiveRateByIds(ids);
    }

    /**
     * 删除分产品有效成本率信息
     *
     * @param id 分产品有效成本率ID
     * @return 结果
     */
    @Override
    public int deleteCostProductEffectiveRateById(Long id) {
        return costProductEffectiveRateMapper.deleteCostProductEffectiveRateById(id);
    }

    /**
     * 导入分产品有效成本率数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    @Override
    public String importCostProductEffectiveRate(MultipartFile file, boolean updateSupport, String operName) {
        try {
            ExcelUtil<CostProductEffectiveRateEntity> util = new ExcelUtil<>(CostProductEffectiveRateEntity.class);
            List<CostProductEffectiveRateEntity> rateList = util.importExcel(file.getInputStream());
            int successNum = 0;
            int failureNum = 0;
            StringBuilder successMsg = new StringBuilder();
            StringBuilder failureMsg = new StringBuilder();

            for (CostProductEffectiveRateEntity rate : rateList) {
                try {
                    // 验证必填字段
                    if (StringUtils.isBlank(rate.getAccountingPeriod()) ||
                        StringUtils.isBlank(rate.getActuarialCode()) ||
                        StringUtils.isBlank(rate.getDesignType()) ||
                        StringUtils.isBlank(rate.getShortTermFlag())) {
                        failureNum++;
                        failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据账期、精算代码、设计类型或是否中短为空");
                        continue;
                    }

                    // 设置默认值
                    if (rate.getEffectiveCostRate() == null) {
                        rate.setEffectiveCostRate(BigDecimal.ZERO);
                    }
                    if (StringUtils.isBlank(rate.getCashFlowSet())) {
                        rate.setCashFlowSet("{}");
                    }

                    // 查询是否已存在
                    CostProductEffectiveRateEntity existingRate = costProductEffectiveRateMapper.selectCostProductEffectiveRateByKey(
                            rate.getAccountingPeriod(), rate.getActuarialCode(), rate.getDesignType(), rate.getShortTermFlag());

                    if (existingRate != null) {
                        if (updateSupport) {
                            rate.setId(existingRate.getId());
                            rate.setUpdateBy(operName);
                            costProductEffectiveRateMapper.updateCostProductEffectiveRate(rate);
                            successNum++;
                            successMsg.append("<br/>第 ").append(successNum).append(" 条数据更新成功");
                        } else {
                            failureNum++;
                            failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据已存在");
                        }
                    } else {
                        rate.setCreateBy(operName);
                        costProductEffectiveRateMapper.insertCostProductEffectiveRate(rate);
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
            log.error("导入分产品有效成本率数据异常", e);
            return "导入分产品有效成本率数据失败，请联系管理员";
        }
    }

    /**
     * 导出分产品有效成本率数据模板
     *
     * @param response HTTP响应对象
     */
    @Override
    public void importTemplateCostProductEffectiveRate(HttpServletResponse response) {
        ExcelUtil<CostProductEffectiveRateEntity> util = new ExcelUtil<>(CostProductEffectiveRateEntity.class);
        util.exportTemplateExcel(response, "分产品有效成本率数据");
    }
}
