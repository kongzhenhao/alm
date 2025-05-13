package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.DateUtils;
import com.jd.lightning.common.utils.StringUtils;
import com.jd.lightning.common.exception.ServiceException;
import com.xl.alm.app.entity.DividendFundCostRateEntity;
import com.xl.alm.app.mapper.DividendFundCostRateMapper;
import com.xl.alm.app.query.DividendFundCostRateQuery;
import com.xl.alm.app.service.IDividendFundCostRateService;
import com.xl.alm.app.util.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 分红方案表 服务实现类
 *
 * @author AI Assistant
 */
@Service
public class DividendFundCostRateServiceImpl implements IDividendFundCostRateService {
    private static final Logger log = LoggerFactory.getLogger(DividendFundCostRateServiceImpl.class);

    @Autowired
    private DividendFundCostRateMapper dividendFundCostRateMapper;

    /**
     * 查询分红方案列表
     *
     * @param query 分红方案查询条件
     * @return 分红方案列表
     */
    @Override
    public List<DividendFundCostRateEntity> selectDividendFundCostRateList(DividendFundCostRateQuery query) {
        return dividendFundCostRateMapper.selectDividendFundCostRateList(query);
    }

    /**
     * 根据ID查询分红方案
     *
     * @param id 分红方案ID
     * @return 分红方案
     */
    @Override
    public DividendFundCostRateEntity selectDividendFundCostRateById(Long id) {
        return dividendFundCostRateMapper.selectDividendFundCostRateById(id);
    }

    /**
     * 新增分红方案
     *
     * @param dividendFundCostRate 分红方案
     * @return 结果
     */
    @Override
    public int insertDividendFundCostRate(DividendFundCostRateEntity dividendFundCostRate) {
        dividendFundCostRate.setCreateTime(DateUtils.getNowDate());
        return dividendFundCostRateMapper.insertDividendFundCostRate(dividendFundCostRate);
    }

    /**
     * 修改分红方案
     *
     * @param dividendFundCostRate 分红方案
     * @return 结果
     */
    @Override
    public int updateDividendFundCostRate(DividendFundCostRateEntity dividendFundCostRate) {
        dividendFundCostRate.setUpdateTime(DateUtils.getNowDate());
        return dividendFundCostRateMapper.updateDividendFundCostRate(dividendFundCostRate);
    }

    /**
     * 批量删除分红方案
     *
     * @param ids 需要删除的分红方案ID
     * @return 结果
     */
    @Override
    public int deleteDividendFundCostRateByIds(Long[] ids) {
        return dividendFundCostRateMapper.deleteDividendFundCostRateByIds(ids);
    }

    /**
     * 删除分红方案信息
     *
     * @param id 分红方案ID
     * @return 结果
     */
    @Override
    public int deleteDividendFundCostRateById(Long id) {
        return dividendFundCostRateMapper.deleteDividendFundCostRateById(id);
    }

    /**
     * 导入分红方案数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importDividendFundCostRate(MultipartFile file, boolean updateSupport, String operName) {
        try {
            ExcelUtil<DividendFundCostRateEntity> util = new ExcelUtil<>(DividendFundCostRateEntity.class);
            List<DividendFundCostRateEntity> rateList = util.importExcel(file.getInputStream());
            int successNum = 0;
            int failureNum = 0;
            StringBuilder successMsg = new StringBuilder();
            StringBuilder failureMsg = new StringBuilder();

            for (DividendFundCostRateEntity rate : rateList) {
                try {
                    // 验证必填字段
                    if (StringUtils.isBlank(rate.getAccountingPeriod()) ||
                        StringUtils.isBlank(rate.getActuarialCode())) {
                        failureNum++;
                        failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据账期或精算代码为空");
                        continue;
                    }

                    // 设置默认值
                    if (StringUtils.isBlank(rate.getShortTermFlag())) {
                        rate.setShortTermFlag("N");
                    }
                    if (rate.getGuaranteedCostRate() == null) {
                        rate.setGuaranteedCostRate(BigDecimal.ZERO);
                    }
                    if (rate.getInvestmentReturnRate() == null) {
                        rate.setInvestmentReturnRate(BigDecimal.ZERO);
                    }
                    if (rate.getDividendRatio() == null) {
                        rate.setDividendRatio(BigDecimal.ZERO);
                    }
                    if (rate.getFundCostRateT0() == null) {
                        rate.setFundCostRateT0(BigDecimal.ZERO);
                    }
                    if (rate.getFundCostRateT1() == null) {
                        rate.setFundCostRateT1(BigDecimal.ZERO);
                    }
                    if (rate.getFundCostRateT2() == null) {
                        rate.setFundCostRateT2(BigDecimal.ZERO);
                    }
                    if (rate.getFundCostRateT3() == null) {
                        rate.setFundCostRateT3(BigDecimal.ZERO);
                    }

                    // 查询是否已存在
                    DividendFundCostRateEntity existingRate = dividendFundCostRateMapper.selectDividendFundCostRateByCode(
                            rate.getActuarialCode(), rate.getAccountingPeriod());

                    if (existingRate != null) {
                        if (updateSupport) {
                            rate.setId(existingRate.getId());
                            rate.setUpdateBy(operName);
                            dividendFundCostRateMapper.updateDividendFundCostRate(rate);
                            successNum++;
                            successMsg.append("<br/>第 ").append(successNum).append(" 条数据更新成功");
                        } else {
                            failureNum++;
                            failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据已存在");
                        }
                    } else {
                        rate.setCreateBy(operName);
                        dividendFundCostRateMapper.insertDividendFundCostRate(rate);
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
                throw new ServiceException(failureMsg.toString());
            } else {
                successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
            }
            return successMsg.toString();
        } catch (Exception e) {
            log.error("导入分红方案数据失败", e);
            throw new ServiceException("导入分红方案数据失败：" + e.getMessage());
        }
    }

    /**
     * 导出分红方案数据模板
     *
     * @param response HTTP响应对象
     */
    @Override
    public void importTemplateDividendFundCostRate(HttpServletResponse response) {
        List<DividendFundCostRateEntity> templateList = new ArrayList<>();
        DividendFundCostRateEntity template = new DividendFundCostRateEntity();
        template.setAccountingPeriod("202401");
        template.setActuarialCode("SAMPLE001");
        template.setBusinessCode("BIZ001");
        template.setProductName("示例分红险产品");
        template.setShortTermFlag("N");
        template.setGuaranteedCostRate(new BigDecimal("0.025"));
        template.setInvestmentReturnRate(new BigDecimal("0.045"));
        template.setDividendRatio(new BigDecimal("0.70"));
        template.setFundCostRateT0(new BigDecimal("0.035"));
        template.setFundCostRateT1(new BigDecimal("0.036"));
        template.setFundCostRateT2(new BigDecimal("0.037"));
        template.setFundCostRateT3(new BigDecimal("0.038"));
        template.setRemark("示例数据");
        templateList.add(template);

        ExcelUtil<DividendFundCostRateEntity> util = new ExcelUtil<>(DividendFundCostRateEntity.class);
        util.exportExcel(templateList, "分红方案数据模板", response);
    }
}
