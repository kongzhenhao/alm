package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.DateUtils;
import com.jd.lightning.common.utils.StringUtils;
import com.jd.lightning.common.exception.ServiceException;
import com.xl.alm.app.entity.StatutoryReserveDetailEntity;
import com.xl.alm.app.dto.StatutoryReserveDetailDTO;
import com.xl.alm.app.mapper.StatutoryReserveDetailMapper;
import com.xl.alm.app.query.StatutoryReserveDetailQuery;
import com.xl.alm.app.service.IStatutoryReserveDetailService;
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
 * 法定准备金明细表 服务实现类
 *
 * @author AI Assistant
 */
@Service
public class StatutoryReserveDetailServiceImpl implements IStatutoryReserveDetailService {
    private static final Logger log = LoggerFactory.getLogger(StatutoryReserveDetailServiceImpl.class);

    @Autowired
    private StatutoryReserveDetailMapper statutoryReserveDetailMapper;

    /**
     * 查询法定准备金明细列表
     *
     * @param query 法定准备金明细查询条件
     * @return 法定准备金明细列表
     */
    @Override
    public List<StatutoryReserveDetailEntity> selectStatutoryReserveDetailList(StatutoryReserveDetailQuery query) {
        return statutoryReserveDetailMapper.selectStatutoryReserveDetailList(query);
    }

    /**
     * 根据ID查询法定准备金明细
     *
     * @param id 法定准备金明细ID
     * @return 法定准备金明细
     */
    @Override
    public StatutoryReserveDetailEntity selectStatutoryReserveDetailById(Long id) {
        return statutoryReserveDetailMapper.selectStatutoryReserveDetailById(id);
    }

    /**
     * 新增法定准备金明细
     *
     * @param statutoryReserveDetail 法定准备金明细
     * @return 结果
     */
    @Override
    public int insertStatutoryReserveDetail(StatutoryReserveDetailEntity statutoryReserveDetail) {
        statutoryReserveDetail.setCreateTime(DateUtils.getNowDate());
        // 计算法定准备金合计
        calculateTotalStatutoryReserve(statutoryReserveDetail);
        return statutoryReserveDetailMapper.insertStatutoryReserveDetail(statutoryReserveDetail);
    }

    /**
     * 修改法定准备金明细
     *
     * @param statutoryReserveDetail 法定准备金明细
     * @return 结果
     */
    @Override
    public int updateStatutoryReserveDetail(StatutoryReserveDetailEntity statutoryReserveDetail) {
        statutoryReserveDetail.setUpdateTime(DateUtils.getNowDate());
        // 计算法定准备金合计
        calculateTotalStatutoryReserve(statutoryReserveDetail);
        return statutoryReserveDetailMapper.updateStatutoryReserveDetail(statutoryReserveDetail);
    }

    /**
     * 批量删除法定准备金明细
     *
     * @param ids 需要删除的法定准备金明细ID
     * @return 结果
     */
    @Override
    public int deleteStatutoryReserveDetailByIds(Long[] ids) {
        return statutoryReserveDetailMapper.deleteStatutoryReserveDetailByIds(ids);
    }

    /**
     * 删除法定准备金明细信息
     *
     * @param id 法定准备金明细ID
     * @return 结果
     */
    @Override
    public int deleteStatutoryReserveDetailById(Long id) {
        return statutoryReserveDetailMapper.deleteStatutoryReserveDetailById(id);
    }

    /**
     * 导入法定准备金明细数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importStatutoryReserveDetail(MultipartFile file, boolean updateSupport, String operName) {
        try {
            ExcelUtil<StatutoryReserveDetailDTO> util = new ExcelUtil<>(StatutoryReserveDetailDTO.class);
            List<StatutoryReserveDetailDTO> dtoList = util.importExcel(file.getInputStream());

            // 将DTO转换为Entity
            List<StatutoryReserveDetailEntity> detailList = new ArrayList<>();
            for (StatutoryReserveDetailDTO dto : dtoList) {
                StatutoryReserveDetailEntity entity = new StatutoryReserveDetailEntity();
                entity.setAccountingPeriod(dto.getAccountingPeriod());
                entity.setActuarialCode(dto.getActuarialCode());
                entity.setBusinessCode(dto.getBusinessCode());
                entity.setProductName(dto.getProductName());
                entity.setTermType(dto.getTermType());
                entity.setDesignType(dto.getDesignType());
                entity.setShortTermFlag(dto.getShortTermFlag());
                entity.setValidPolicyCount(dto.getValidPolicyCount());
                entity.setAccumulatedPremium(dto.getAccumulatedPremium());
                entity.setAccountValue(dto.getAccountValue());
                entity.setStatutoryReserve(dto.getStatutoryReserve());
                entity.setUnearnedPremiumReserve(dto.getUnearnedPremiumReserve());
                entity.setOutstandingClaimReserve(dto.getOutstandingClaimReserve());
                entity.setTotalStatutoryReserve(dto.getTotalStatutoryReserve());
                entity.setRemark(dto.getRemark());
                detailList.add(entity);
            }
            int successNum = 0;
            int failureNum = 0;
            StringBuilder successMsg = new StringBuilder();
            StringBuilder failureMsg = new StringBuilder();

            for (StatutoryReserveDetailEntity detail : detailList) {
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
                    } else {
                        // 验证存量累计规模保费是否超过数据库列的范围
                        // 判断是否超过DECIMAL(18,10)的范围，整数部分最多8位
                        BigDecimal maxValue = new BigDecimal("99999999.9999999999");
                        if (detail.getAccumulatedPremium().compareTo(maxValue) > 0) {
                            // 如果超过范围，则将其设置为最大值
                            detail.setAccumulatedPremium(maxValue);
                        }
                    }
                    if (detail.getAccountValue() == null) {
                        detail.setAccountValue(BigDecimal.ZERO);
                    } else {
                        // 验证账户价值是否超过数据库列的范围
                        BigDecimal maxValue = new BigDecimal("99999999.9999999999");
                        if (detail.getAccountValue().compareTo(maxValue) > 0) {
                            detail.setAccountValue(maxValue);
                        }
                    }
                    if (detail.getStatutoryReserve() == null) {
                        detail.setStatutoryReserve(BigDecimal.ZERO);
                    } else {
                        // 验证法定/非单位准备金是否超过数据库列的范围
                        BigDecimal maxValue = new BigDecimal("99999999.9999999999");
                        if (detail.getStatutoryReserve().compareTo(maxValue) > 0) {
                            detail.setStatutoryReserve(maxValue);
                        }
                    }
                    if (detail.getUnearnedPremiumReserve() == null) {
                        detail.setUnearnedPremiumReserve(BigDecimal.ZERO);
                    } else {
                        // 验证未到期责任准备金是否超过数据库列的范围
                        BigDecimal maxValue = new BigDecimal("99999999.9999999999");
                        if (detail.getUnearnedPremiumReserve().compareTo(maxValue) > 0) {
                            detail.setUnearnedPremiumReserve(maxValue);
                        }
                    }
                    if (detail.getOutstandingClaimReserve() == null) {
                        detail.setOutstandingClaimReserve(BigDecimal.ZERO);
                    } else {
                        // 验证未决赔款准备金是否超过数据库列的范围
                        BigDecimal maxValue = new BigDecimal("99999999.9999999999");
                        if (detail.getOutstandingClaimReserve().compareTo(maxValue) > 0) {
                            detail.setOutstandingClaimReserve(maxValue);
                        }
                    }

                    // 验证新添加的字段是否超过数据库列的范围
                    BigDecimal maxValue = new BigDecimal("99999999.9999999999");

                    // 保证利率准备金
                    if (detail.getGuaranteedRateReserve() != null && detail.getGuaranteedRateReserve().compareTo(maxValue) > 0) {
                        detail.setGuaranteedRateReserve(maxValue);
                    }

                    // 失效单现价
                    if (detail.getLapsedPolicyValue() != null && detail.getLapsedPolicyValue().compareTo(maxValue) > 0) {
                        detail.setLapsedPolicyValue(maxValue);
                    }

                    // 豁免责任准备金
                    if (detail.getWaiverReserve() != null && detail.getWaiverReserve().compareTo(maxValue) > 0) {
                        detail.setWaiverReserve(maxValue);
                    }

                    // 未建模准备金
                    if (detail.getUnmodeledReserve() != null && detail.getUnmodeledReserve().compareTo(maxValue) > 0) {
                        detail.setUnmodeledReserve(maxValue);
                    }

                    // 持续奖准备金
                    if (detail.getPersistenceBonusReserve() != null && detail.getPersistenceBonusReserve().compareTo(maxValue) > 0) {
                        detail.setPersistenceBonusReserve(maxValue);
                    }

                    // 长期未到期准备金
                    if (detail.getLongTermUnearned() != null && detail.getLongTermUnearned().compareTo(maxValue) > 0) {
                        detail.setLongTermUnearned(maxValue);
                    }

                    // 短险未到期准备金
                    if (detail.getShortTermUnearned() != null && detail.getShortTermUnearned().compareTo(maxValue) > 0) {
                        detail.setShortTermUnearned(maxValue);
                    }

                    // 已报未决赔款
                    if (detail.getReportedUnpaid() != null && detail.getReportedUnpaid().compareTo(maxValue) > 0) {
                        detail.setReportedUnpaid(maxValue);
                    }

                    // 未报未决赔款
                    if (detail.getIncurredUnreported() != null && detail.getIncurredUnreported().compareTo(maxValue) > 0) {
                        detail.setIncurredUnreported(maxValue);
                    }

                    // 理赔费用准备金
                    if (detail.getClaimExpenseReserve() != null && detail.getClaimExpenseReserve().compareTo(maxValue) > 0) {
                        detail.setClaimExpenseReserve(maxValue);
                    }

                    // 计算法定准备金合计
                    calculateTotalStatutoryReserve(detail);

                    // 查询是否已存在
                    StatutoryReserveDetailEntity existingDetail = statutoryReserveDetailMapper.selectStatutoryReserveDetailByCode(
                            detail.getActuarialCode(), detail.getAccountingPeriod());

                    if (existingDetail != null) {
                        if (updateSupport) {
                            detail.setId(existingDetail.getId());
                            detail.setUpdateBy(operName);
                            statutoryReserveDetailMapper.updateStatutoryReserveDetail(detail);
                            successNum++;
                            successMsg.append("<br/>第 ").append(successNum).append(" 条数据更新成功");
                        } else {
                            failureNum++;
                            failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据已存在");
                        }
                    } else {
                        detail.setCreateBy(operName);
                        statutoryReserveDetailMapper.insertStatutoryReserveDetail(detail);
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
            log.error("导入法定准备金明细数据失败", e);
            throw new ServiceException("导入法定准备金明细数据失败：" + e.getMessage());
        }
    }

    /**
     * 导出法定准备金明细数据模板
     *
     * @param response HTTP响应对象
     */
    @Override
    public void importTemplateStatutoryReserveDetail(HttpServletResponse response) {
        List<StatutoryReserveDetailDTO> templateList = new ArrayList<>();
        StatutoryReserveDetailDTO template = new StatutoryReserveDetailDTO();
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
        template.setStatutoryReserve(new BigDecimal("9000.00"));
        template.setGuaranteedRateReserve(new BigDecimal("1000.00"));
        template.setLapsedPolicyValue(new BigDecimal("500.00"));
        template.setWaiverReserve(new BigDecimal("300.00"));
        template.setUnmodeledReserve(new BigDecimal("200.00"));
        template.setPersistenceBonusReserve(new BigDecimal("100.00"));
        template.setLongTermUnearned(new BigDecimal("400.00"));
        template.setShortTermUnearned(new BigDecimal("100.00"));
        template.setUnearnedPremiumReserve(new BigDecimal("500.00"));
        template.setReportedUnpaid(new BigDecimal("150.00"));
        template.setIncurredUnreported(new BigDecimal("50.00"));
        template.setClaimExpenseReserve(new BigDecimal("20.00"));
        template.setOutstandingClaimReserve(new BigDecimal("200.00"));
        template.setTotalStatutoryReserve(new BigDecimal("12520.00"));
        template.setRemark("示例数据");
        templateList.add(template);

        ExcelUtil<StatutoryReserveDetailDTO> util = new ExcelUtil<>(StatutoryReserveDetailDTO.class);
        util.exportExcel(templateList, "法定准备金明细数据模板", response);
    }

    /**
     * 计算法定准备金合计
     *
     * @param detail 法定准备金明细
     */
    private void calculateTotalStatutoryReserve(StatutoryReserveDetailEntity detail) {
        BigDecimal total = BigDecimal.ZERO;
        if (detail.getStatutoryReserve() != null) {
            total = total.add(detail.getStatutoryReserve());
        }
        if (detail.getGuaranteedRateReserve() != null) {
            total = total.add(detail.getGuaranteedRateReserve());
        }
        if (detail.getLapsedPolicyValue() != null) {
            total = total.add(detail.getLapsedPolicyValue());
        }
        if (detail.getWaiverReserve() != null) {
            total = total.add(detail.getWaiverReserve());
        }
        if (detail.getUnmodeledReserve() != null) {
            total = total.add(detail.getUnmodeledReserve());
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

        // 验证法定准备金合计是否超过数据库列的范围
        BigDecimal maxValue = new BigDecimal("99999999.9999999999");
        if (total.compareTo(maxValue) > 0) {
            total = maxValue;
        }

        detail.setTotalStatutoryReserve(total);
    }
}
