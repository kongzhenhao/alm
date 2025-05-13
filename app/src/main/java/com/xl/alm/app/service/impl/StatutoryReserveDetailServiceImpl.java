package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.DateUtils;
import com.jd.lightning.common.utils.StringUtils;
import com.jd.lightning.common.exception.ServiceException;
import com.xl.alm.app.entity.StatutoryReserveDetailEntity;
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
            ExcelUtil<StatutoryReserveDetailEntity> util = new ExcelUtil<>(StatutoryReserveDetailEntity.class);
            List<StatutoryReserveDetailEntity> detailList = util.importExcel(file.getInputStream());
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
                    }
                    if (detail.getAccountValue() == null) {
                        detail.setAccountValue(BigDecimal.ZERO);
                    }
                    if (detail.getStatutoryReserve() == null) {
                        detail.setStatutoryReserve(BigDecimal.ZERO);
                    }
                    if (detail.getUnearnedPremiumReserve() == null) {
                        detail.setUnearnedPremiumReserve(BigDecimal.ZERO);
                    }
                    if (detail.getOutstandingClaimReserve() == null) {
                        detail.setOutstandingClaimReserve(BigDecimal.ZERO);
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
        List<StatutoryReserveDetailEntity> templateList = new ArrayList<>();
        StatutoryReserveDetailEntity template = new StatutoryReserveDetailEntity();
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
        template.setUnearnedPremiumReserve(new BigDecimal("500.00"));
        template.setOutstandingClaimReserve(new BigDecimal("200.00"));
        template.setTotalStatutoryReserve(new BigDecimal("9700.00"));
        template.setRemark("示例数据");
        templateList.add(template);

        ExcelUtil<StatutoryReserveDetailEntity> util = new ExcelUtil<>(StatutoryReserveDetailEntity.class);
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
        if (detail.getUnearnedPremiumReserve() != null) {
            total = total.add(detail.getUnearnedPremiumReserve());
        }
        if (detail.getOutstandingClaimReserve() != null) {
            total = total.add(detail.getOutstandingClaimReserve());
        }
        detail.setTotalStatutoryReserve(total);
    }
}
