package com.xl.alm.app.service.impl;

import com.jd.lightning.common.exception.ServiceException;
import com.jd.lightning.common.utils.DateUtils;
import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.entity.AccountingReserveDetailEntity;
import com.xl.alm.app.dto.AccountingReserveDetailDTO;
import com.xl.alm.app.mapper.AccountingReserveDetailMapper;
import com.xl.alm.app.query.AccountingReserveDetailQuery;
import com.xl.alm.app.service.IAccountingReserveDetailService;
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
 * 会计准备金明细表 服务实现类
 *
 * @author AI Assistant
 */
@Service
public class AccountingReserveDetailServiceImpl implements IAccountingReserveDetailService {
    private static final Logger log = LoggerFactory.getLogger(AccountingReserveDetailServiceImpl.class);

    @Autowired
    private AccountingReserveDetailMapper accountingReserveDetailMapper;

    /**
     * 查询会计准备金明细列表
     *
     * @param query 会计准备金明细查询条件
     * @return 会计准备金明细列表
     */
    @Override
    public List<AccountingReserveDetailEntity> selectAccountingReserveDetailList(AccountingReserveDetailQuery query) {
        return accountingReserveDetailMapper.selectAccountingReserveDetailList(query);
    }

    /**
     * 根据ID查询会计准备金明细
     *
     * @param id 会计准备金明细ID
     * @return 会计准备金明细
     */
    @Override
    public AccountingReserveDetailEntity selectAccountingReserveDetailById(Long id) {
        return accountingReserveDetailMapper.selectAccountingReserveDetailById(id);
    }

    /**
     * 新增会计准备金明细
     *
     * @param accountingReserveDetail 会计准备金明细
     * @return 结果
     */
    @Override
    public int insertAccountingReserveDetail(AccountingReserveDetailEntity accountingReserveDetail) {
        accountingReserveDetail.setCreateTime(DateUtils.getNowDate());
        // 计算会计准备金合计
        calculateTotalAccountingReserve(accountingReserveDetail);
        return accountingReserveDetailMapper.insertAccountingReserveDetail(accountingReserveDetail);
    }

    /**
     * 修改会计准备金明细
     *
     * @param accountingReserveDetail 会计准备金明细
     * @return 结果
     */
    @Override
    public int updateAccountingReserveDetail(AccountingReserveDetailEntity accountingReserveDetail) {
        accountingReserveDetail.setUpdateTime(DateUtils.getNowDate());
        // 计算会计准备金合计
        calculateTotalAccountingReserve(accountingReserveDetail);
        return accountingReserveDetailMapper.updateAccountingReserveDetail(accountingReserveDetail);
    }

    /**
     * 批量删除会计准备金明细
     *
     * @param ids 需要删除的会计准备金明细ID
     * @return 结果
     */
    @Override
    public int deleteAccountingReserveDetailByIds(Long[] ids) {
        return accountingReserveDetailMapper.deleteAccountingReserveDetailByIds(ids);
    }

    /**
     * 删除会计准备金明细信息
     *
     * @param id 会计准备金明细ID
     * @return 结果
     */
    @Override
    public int deleteAccountingReserveDetailById(Long id) {
        return accountingReserveDetailMapper.deleteAccountingReserveDetailById(id);
    }

    /**
     * 导入会计准备金明细数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importAccountingReserveDetail(MultipartFile file, boolean updateSupport, String operName) {
        try {
            ExcelUtil<AccountingReserveDetailDTO> util = new ExcelUtil<>(AccountingReserveDetailDTO.class);
            List<AccountingReserveDetailDTO> dtoList = util.importExcel(file.getInputStream());

            // 将DTO转换为Entity
            List<AccountingReserveDetailEntity> detailList = new ArrayList<>();
            for (AccountingReserveDetailDTO dto : dtoList) {
                AccountingReserveDetailEntity entity = new AccountingReserveDetailEntity();
                entity.setAccountingPeriod(dto.getAccountingPeriod());
                entity.setActuarialCode(dto.getActuarialCode());
                entity.setBusinessCode(dto.getBusinessCode());
                entity.setProductName(dto.getProductName());
                entity.setTermType(dto.getTermType());
                entity.setDesignType(dto.getDesignType());
                entity.setShortTermFlag(dto.getShortTermFlag());
                entity.setValidPolicyCount(dto.getValidPolicyCount());
                // 验证存量累计规模保费是否超过数据库列的范围
                if (dto.getAccumulatedPremium() != null) {
                    // 判断是否超过DECIMAL(18,10)的范围，整数部分最多8位
                    BigDecimal maxValue = new BigDecimal("99999999.9999999999");
                    if (dto.getAccumulatedPremium().compareTo(maxValue) > 0) {
                        // 如果超过范围，则将其设置为最大值
                        entity.setAccumulatedPremium(maxValue);
                    } else {
                        entity.setAccumulatedPremium(dto.getAccumulatedPremium());
                    }
                } else {
                    entity.setAccumulatedPremium(BigDecimal.ZERO);
                }
                entity.setAccountValue(dto.getAccountValue());
                entity.setDividendProvision(dto.getDividendProvision());
                entity.setBestEstimate(dto.getBestEstimate());
                entity.setRiskMargin(dto.getRiskMargin());
                entity.setResidualMargin(dto.getResidualMargin());
                entity.setUnmodeledReserve(dto.getUnmodeledReserve());
                entity.setWaiverReserve(dto.getWaiverReserve());
                entity.setPersistenceBonusReserve(dto.getPersistenceBonusReserve());
                entity.setLongTermUnearned(dto.getLongTermUnearned());
                entity.setShortTermUnearned(dto.getShortTermUnearned());
                entity.setUnearnedPremiumReserve(dto.getUnearnedPremiumReserve());
                entity.setReportedUnpaid(dto.getReportedUnpaid());
                entity.setIncurredUnreported(dto.getIncurredUnreported());
                entity.setClaimExpenseReserve(dto.getClaimExpenseReserve());
                entity.setOutstandingClaimReserve(dto.getOutstandingClaimReserve());
                entity.setTotalAccountingReserve(dto.getTotalAccountingReserve());
                entity.setReinsuranceUnearned(dto.getReinsuranceUnearned());
                entity.setReinsuranceReported(dto.getReinsuranceReported());
                entity.setReinsuranceUnreported(dto.getReinsuranceUnreported());
                entity.setReinsuranceClaimTotal(dto.getReinsuranceClaimTotal());
                entity.setReinsuranceTotal(dto.getReinsuranceTotal());
                entity.setLapsedPolicyValue(dto.getLapsedPolicyValue());
                entity.setFractionalMonthDividend(dto.getFractionalMonthDividend());
                entity.setUnpaidDividend(dto.getUnpaidDividend());
                entity.setRemark(dto.getRemark());
                detailList.add(entity);
            }
            int successNum = 0;
            int failureNum = 0;
            StringBuilder successMsg = new StringBuilder();
            StringBuilder failureMsg = new StringBuilder();

            for (AccountingReserveDetailEntity detail : detailList) {
                try {
                    // 验证必填字段
                    if (StringUtils.isBlank(detail.getAccountingPeriod()) ||
                        StringUtils.isBlank(detail.getActuarialCode())) {
                        failureNum++;
                        failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据账期或精算代码为空");
                        continue;
                    }

                    // 设置默认值
                    if (StringUtils.isBlank(detail.getTermType())) {
                        detail.setTermType("L");
                    }
                    if (StringUtils.isBlank(detail.getShortTermFlag())) {
                        detail.setShortTermFlag("N");
                    }
                    if (detail.getValidPolicyCount() == null) {
                        detail.setValidPolicyCount(0);
                    }
                    if (detail.getAccumulatedPremium() == null) {
                        detail.setAccumulatedPremium(BigDecimal.ZERO);
                    }
                    if (detail.getAccountValue() == null) {
                        detail.setAccountValue(BigDecimal.ZERO);
                    }
                    if (detail.getDividendProvision() == null) {
                        detail.setDividendProvision(BigDecimal.ZERO);
                    }
                    if (detail.getBestEstimate() == null) {
                        detail.setBestEstimate(BigDecimal.ZERO);
                    }
                    if (detail.getRiskMargin() == null) {
                        detail.setRiskMargin(BigDecimal.ZERO);
                    }
                    if (detail.getResidualMargin() == null) {
                        detail.setResidualMargin(BigDecimal.ZERO);
                    }
                    if (detail.getUnmodeledReserve() == null) {
                        detail.setUnmodeledReserve(BigDecimal.ZERO);
                    }
                    if (detail.getWaiverReserve() == null) {
                        detail.setWaiverReserve(BigDecimal.ZERO);
                    }
                    if (detail.getPersistenceBonusReserve() == null) {
                        detail.setPersistenceBonusReserve(BigDecimal.ZERO);
                    }
                    if (detail.getLongTermUnearned() == null) {
                        detail.setLongTermUnearned(BigDecimal.ZERO);
                    }
                    if (detail.getShortTermUnearned() == null) {
                        detail.setShortTermUnearned(BigDecimal.ZERO);
                    }
                    if (detail.getUnearnedPremiumReserve() == null) {
                        detail.setUnearnedPremiumReserve(BigDecimal.ZERO);
                    }
                    if (detail.getOutstandingClaimReserve() == null) {
                        detail.setOutstandingClaimReserve(BigDecimal.ZERO);
                    }
                    if (detail.getReinsuranceUnearned() == null) {
                        detail.setReinsuranceUnearned(BigDecimal.ZERO);
                    }
                    if (detail.getReinsuranceReported() == null) {
                        detail.setReinsuranceReported(BigDecimal.ZERO);
                    }
                    if (detail.getReinsuranceUnreported() == null) {
                        detail.setReinsuranceUnreported(BigDecimal.ZERO);
                    }
                    if (detail.getReinsuranceClaimTotal() == null) {
                        detail.setReinsuranceClaimTotal(BigDecimal.ZERO);
                    }
                    if (detail.getReinsuranceTotal() == null) {
                        detail.setReinsuranceTotal(BigDecimal.ZERO);
                    }
                    if (detail.getLapsedPolicyValue() == null) {
                        detail.setLapsedPolicyValue(BigDecimal.ZERO);
                    }
                    if (detail.getFractionalMonthDividend() == null) {
                        detail.setFractionalMonthDividend(BigDecimal.ZERO);
                    }
                    if (detail.getUnpaidDividend() == null) {
                        detail.setUnpaidDividend(BigDecimal.ZERO);
                    }

                    // 计算会计准备金合计
                    calculateTotalAccountingReserve(detail);

                    // 查询是否已存在
                    AccountingReserveDetailEntity existingDetail = accountingReserveDetailMapper.selectAccountingReserveDetailByCode(
                            detail.getActuarialCode(), detail.getAccountingPeriod());

                    if (existingDetail != null) {
                        if (updateSupport) {
                            detail.setId(existingDetail.getId());
                            detail.setUpdateBy(operName);
                            accountingReserveDetailMapper.updateAccountingReserveDetail(detail);
                            successNum++;
                            successMsg.append("<br/>第 ").append(successNum).append(" 条数据更新成功");
                        } else {
                            failureNum++;
                            failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据已存在");
                        }
                    } else {
                        detail.setCreateBy(operName);
                        accountingReserveDetailMapper.insertAccountingReserveDetail(detail);
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
            log.error("导入会计准备金明细数据失败", e);
            throw new ServiceException("导入会计准备金明细数据失败：" + e.getMessage());
        }
    }

    /**
     * 导出会计准备金明细数据模板
     *
     * @param response HTTP响应对象
     */
    @Override
    public void importTemplateAccountingReserveDetail(HttpServletResponse response) {
        List<AccountingReserveDetailDTO> templateList = new ArrayList<>();
        AccountingReserveDetailDTO template = new AccountingReserveDetailDTO();
        template.setAccountingPeriod("202401");
        template.setActuarialCode("SAMPLE001");
        template.setBusinessCode("BIZ001");
        template.setProductName("示例产品");
        template.setTermType("L");
        template.setDesignType("传统险");
        template.setShortTermFlag("N");
        template.setValidPolicyCount(100);
        template.setAccumulatedPremium(new BigDecimal("10000.00"));
        template.setAccountValue(new BigDecimal("9500.00"));
        template.setDividendProvision(new BigDecimal("1000.00"));
        template.setBestEstimate(new BigDecimal("5000.00"));
        template.setRiskMargin(new BigDecimal("500.00"));
        template.setResidualMargin(new BigDecimal("1500.00"));
        template.setUnmodeledReserve(new BigDecimal("200.00"));
        template.setWaiverReserve(new BigDecimal("300.00"));
        template.setPersistenceBonusReserve(new BigDecimal("400.00"));
        template.setLongTermUnearned(new BigDecimal("600.00"));
        template.setShortTermUnearned(new BigDecimal("100.00"));
        template.setUnearnedPremiumReserve(new BigDecimal("500.00"));
        template.setReportedUnpaid(new BigDecimal("150.00"));
        template.setIncurredUnreported(new BigDecimal("50.00"));
        template.setClaimExpenseReserve(new BigDecimal("20.00"));
        template.setOutstandingClaimReserve(new BigDecimal("200.00"));
        template.setTotalAccountingReserve(new BigDecimal("9700.00"));
        template.setReinsuranceUnearned(new BigDecimal("100.00"));
        template.setReinsuranceReported(new BigDecimal("50.00"));
        template.setReinsuranceUnreported(new BigDecimal("30.00"));
        template.setReinsuranceClaimTotal(new BigDecimal("80.00"));
        template.setReinsuranceTotal(new BigDecimal("180.00"));
        template.setLapsedPolicyValue(new BigDecimal("300.00"));
        template.setFractionalMonthDividend(new BigDecimal("20.00"));
        template.setUnpaidDividend(new BigDecimal("40.00"));
        template.setRemark("示例数据");
        templateList.add(template);

        ExcelUtil<AccountingReserveDetailDTO> util = new ExcelUtil<>(AccountingReserveDetailDTO.class);
        util.exportExcel(templateList, "会计准备金明细数据模板", response);
    }

    /**
     * 计算会计准备金合计
     *
     * @param detail 会计准备金明细
     */
    private void calculateTotalAccountingReserve(AccountingReserveDetailEntity detail) {
        BigDecimal total = BigDecimal.ZERO;
        if (detail.getDividendProvision() != null) {
            total = total.add(detail.getDividendProvision());
        }
        if (detail.getBestEstimate() != null) {
            total = total.add(detail.getBestEstimate());
        }
        if (detail.getRiskMargin() != null) {
            total = total.add(detail.getRiskMargin());
        }
        if (detail.getResidualMargin() != null) {
            total = total.add(detail.getResidualMargin());
        }
        if (detail.getUnmodeledReserve() != null) {
            total = total.add(detail.getUnmodeledReserve());
        }
        if (detail.getWaiverReserve() != null) {
            total = total.add(detail.getWaiverReserve());
        }
        if (detail.getPersistenceBonusReserve() != null) {
            total = total.add(detail.getPersistenceBonusReserve());
        }
        if (detail.getLongTermUnearned() != null) {
            total = total.add(detail.getLongTermUnearned());
        }
        if (detail.getShortTermUnearned() != null) {
            total = total.add(detail.getShortTermUnearned());
        }
        if (detail.getUnearnedPremiumReserve() != null) {
            total = total.add(detail.getUnearnedPremiumReserve());
        }
        if (detail.getReportedUnpaid() != null) {
            total = total.add(detail.getReportedUnpaid());
        }
        if (detail.getIncurredUnreported() != null) {
            total = total.add(detail.getIncurredUnreported());
        }
        if (detail.getClaimExpenseReserve() != null) {
            total = total.add(detail.getClaimExpenseReserve());
        }
        if (detail.getOutstandingClaimReserve() != null) {
            total = total.add(detail.getOutstandingClaimReserve());
        }
        if (detail.getFractionalMonthDividend() != null) {
            total = total.add(detail.getFractionalMonthDividend());
        }
        if (detail.getUnpaidDividend() != null) {
            total = total.add(detail.getUnpaidDividend());
        }
        detail.setTotalAccountingReserve(total);
    }
}
