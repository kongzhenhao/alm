package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.DateUtils;
import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.entity.CostAccountEffectiveRateEntity;
import com.xl.alm.app.mapper.CostAccountEffectiveRateMapper;
import com.xl.alm.app.query.CostAccountEffectiveRateQuery;
import com.xl.alm.app.service.ICostAccountEffectiveRateService;
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
 * 分账户有效成本率表 服务实现类
 *
 * @author AI Assistant
 */
@Service
public class CostAccountEffectiveRateServiceImpl implements ICostAccountEffectiveRateService {
    private static final Logger log = LoggerFactory.getLogger(CostAccountEffectiveRateServiceImpl.class);

    @Autowired
    private CostAccountEffectiveRateMapper costAccountEffectiveRateMapper;

    /**
     * 查询分账户有效成本率列表
     *
     * @param query 分账户有效成本率查询条件
     * @return 分账户有效成本率列表
     */
    @Override
    public List<CostAccountEffectiveRateEntity> selectCostAccountEffectiveRateList(CostAccountEffectiveRateQuery query) {
        return costAccountEffectiveRateMapper.selectCostAccountEffectiveRateList(query);
    }

    /**
     * 根据ID查询分账户有效成本率
     *
     * @param id 分账户有效成本率ID
     * @return 分账户有效成本率
     */
    @Override
    public CostAccountEffectiveRateEntity selectCostAccountEffectiveRateById(Long id) {
        return costAccountEffectiveRateMapper.selectCostAccountEffectiveRateById(id);
    }

    /**
     * 新增分账户有效成本率
     *
     * @param costAccountEffectiveRate 分账户有效成本率
     * @return 结果
     */
    @Override
    public int insertCostAccountEffectiveRate(CostAccountEffectiveRateEntity costAccountEffectiveRate) {
        costAccountEffectiveRate.setCreateTime(DateUtils.getNowDate());
        return costAccountEffectiveRateMapper.insertCostAccountEffectiveRate(costAccountEffectiveRate);
    }

    /**
     * 修改分账户有效成本率
     *
     * @param costAccountEffectiveRate 分账户有效成本率
     * @return 结果
     */
    @Override
    public int updateCostAccountEffectiveRate(CostAccountEffectiveRateEntity costAccountEffectiveRate) {
        costAccountEffectiveRate.setUpdateTime(DateUtils.getNowDate());
        return costAccountEffectiveRateMapper.updateCostAccountEffectiveRate(costAccountEffectiveRate);
    }

    /**
     * 批量删除分账户有效成本率
     *
     * @param ids 需要删除的分账户有效成本率ID
     * @return 结果
     */
    @Override
    public int deleteCostAccountEffectiveRateByIds(Long[] ids) {
        return costAccountEffectiveRateMapper.deleteCostAccountEffectiveRateByIds(ids);
    }

    /**
     * 删除分账户有效成本率信息
     *
     * @param id 分账户有效成本率ID
     * @return 结果
     */
    @Override
    public int deleteCostAccountEffectiveRateById(Long id) {
        return costAccountEffectiveRateMapper.deleteCostAccountEffectiveRateById(id);
    }

    /**
     * 导入分账户有效成本率数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    @Override
    public String importCostAccountEffectiveRate(MultipartFile file, boolean updateSupport, String operName) {
        try {
            ExcelUtil<CostAccountEffectiveRateEntity> util = new ExcelUtil<>(CostAccountEffectiveRateEntity.class);
            List<CostAccountEffectiveRateEntity> rateList = util.importExcel(file.getInputStream());
            int successNum = 0;
            int failureNum = 0;
            StringBuilder successMsg = new StringBuilder();
            StringBuilder failureMsg = new StringBuilder();

            for (CostAccountEffectiveRateEntity rate : rateList) {
                try {
                    // 验证必填字段
                    if (StringUtils.isBlank(rate.getAccountingPeriod()) ||
                        StringUtils.isBlank(rate.getDesignType())) {
                        failureNum++;
                        failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据账期或设计类型为空");
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
                    CostAccountEffectiveRateEntity existingRate = costAccountEffectiveRateMapper.selectCostAccountEffectiveRateByKey(
                            rate.getAccountingPeriod(), rate.getDesignType());

                    if (existingRate != null) {
                        if (updateSupport) {
                            rate.setId(existingRate.getId());
                            rate.setUpdateBy(operName);
                            costAccountEffectiveRateMapper.updateCostAccountEffectiveRate(rate);
                            successNum++;
                            successMsg.append("<br/>第 ").append(successNum).append(" 条数据更新成功");
                        } else {
                            failureNum++;
                            failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据已存在");
                        }
                    } else {
                        rate.setCreateBy(operName);
                        costAccountEffectiveRateMapper.insertCostAccountEffectiveRate(rate);
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
            log.error("导入分账户有效成本率数据异常", e);
            return "导入分账户有效成本率数据失败，请联系管理员";
        }
    }

    /**
     * 导出分账户有效成本率数据模板
     *
     * @param response HTTP响应对象
     */
    @Override
    public void importTemplateCostAccountEffectiveRate(HttpServletResponse response) {
        ExcelUtil<CostAccountEffectiveRateEntity> util = new ExcelUtil<>(CostAccountEffectiveRateEntity.class);
        util.exportTemplateExcel(response, "分账户有效成本率数据");
    }
}
