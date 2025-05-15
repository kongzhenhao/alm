package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.DateUtils;
import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.util.ExcelUtil;
import com.xl.alm.app.dto.UniversalAvgSettlementRateDTO;
import com.jd.lightning.common.exception.ServiceException;
import com.xl.alm.app.entity.UniversalAvgSettlementRateEntity;
import com.xl.alm.app.mapper.UniversalAvgSettlementRateMapper;
import com.xl.alm.app.query.UniversalAvgSettlementRateQuery;
import com.xl.alm.app.service.IUniversalAvgSettlementRateService;
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
 * 万能平均结算利率表 服务实现类
 *
 * @author AI Assistant
 */
@Service
public class UniversalAvgSettlementRateServiceImpl implements IUniversalAvgSettlementRateService {
    private static final Logger log = LoggerFactory.getLogger(UniversalAvgSettlementRateServiceImpl.class);

    @Autowired
    private UniversalAvgSettlementRateMapper universalAvgSettlementRateMapper;

    /**
     * 查询万能平均结算利率列表
     *
     * @param query 万能平均结算利率查询条件
     * @return 万能平均结算利率列表
     */
    @Override
    public List<UniversalAvgSettlementRateEntity> selectUniversalAvgSettlementRateList(UniversalAvgSettlementRateQuery query) {
        return universalAvgSettlementRateMapper.selectUniversalAvgSettlementRateList(query);
    }

    /**
     * 根据ID查询万能平均结算利率
     *
     * @param id 万能平均结算利率ID
     * @return 万能平均结算利率
     */
    @Override
    public UniversalAvgSettlementRateEntity selectUniversalAvgSettlementRateById(Long id) {
        return universalAvgSettlementRateMapper.selectUniversalAvgSettlementRateById(id);
    }

    /**
     * 新增万能平均结算利率
     *
     * @param universalAvgSettlementRate 万能平均结算利率
     * @return 结果
     */
    @Override
    public int insertUniversalAvgSettlementRate(UniversalAvgSettlementRateEntity universalAvgSettlementRate) {
        universalAvgSettlementRate.setCreateTime(DateUtils.getNowDate());
        return universalAvgSettlementRateMapper.insertUniversalAvgSettlementRate(universalAvgSettlementRate);
    }

    /**
     * 修改万能平均结算利率
     *
     * @param universalAvgSettlementRate 万能平均结算利率
     * @return 结果
     */
    @Override
    public int updateUniversalAvgSettlementRate(UniversalAvgSettlementRateEntity universalAvgSettlementRate) {
        universalAvgSettlementRate.setUpdateTime(DateUtils.getNowDate());
        return universalAvgSettlementRateMapper.updateUniversalAvgSettlementRate(universalAvgSettlementRate);
    }

    /**
     * 批量删除万能平均结算利率
     *
     * @param ids 需要删除的万能平均结算利率ID
     * @return 结果
     */
    @Override
    public int deleteUniversalAvgSettlementRateByIds(Long[] ids) {
        return universalAvgSettlementRateMapper.deleteUniversalAvgSettlementRateByIds(ids);
    }

    /**
     * 删除万能平均结算利率信息
     *
     * @param id 万能平均结算利率ID
     * @return 结果
     */
    @Override
    public int deleteUniversalAvgSettlementRateById(Long id) {
        return universalAvgSettlementRateMapper.deleteUniversalAvgSettlementRateById(id);
    }

    /**
     * 导入万能平均结算利率数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importUniversalAvgSettlementRate(MultipartFile file, boolean updateSupport, String operName) {
        try {
            ExcelUtil<UniversalAvgSettlementRateDTO> util = new ExcelUtil<>(UniversalAvgSettlementRateDTO.class);
            List<UniversalAvgSettlementRateDTO> dtoList = util.importExcel(file.getInputStream());

            // 将DTO转换为Entity
            List<UniversalAvgSettlementRateEntity> rateList = new ArrayList<>();
            for (UniversalAvgSettlementRateDTO dto : dtoList) {
                UniversalAvgSettlementRateEntity entity = new UniversalAvgSettlementRateEntity();
                entity.setAccountingPeriod(dto.getAccountingPeriod());
                entity.setActuarialCode(dto.getActuarialCode());
                entity.setBusinessCode(dto.getBusinessCode());
                entity.setProductName(dto.getProductName());
                entity.setShortTermFlag(dto.getShortTermFlag());
                entity.setGuaranteedCostRate(dto.getGuaranteedCostRate());
                entity.setAvgRateT0(dto.getAvgRateT0());
                entity.setAvgRateT1(dto.getAvgRateT1());
                entity.setAvgRateT2(dto.getAvgRateT2());
                entity.setAvgRateT3(dto.getAvgRateT3());
                entity.setRemark(dto.getRemark());
                rateList.add(entity);
            }
            int successNum = 0;
            int failureNum = 0;
            StringBuilder successMsg = new StringBuilder();
            StringBuilder failureMsg = new StringBuilder();

            for (UniversalAvgSettlementRateEntity rate : rateList) {
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
                    if (rate.getAvgRateT0() == null) {
                        rate.setAvgRateT0(BigDecimal.ZERO);
                    }
                    if (rate.getAvgRateT1() == null) {
                        rate.setAvgRateT1(BigDecimal.ZERO);
                    }
                    if (rate.getAvgRateT2() == null) {
                        rate.setAvgRateT2(BigDecimal.ZERO);
                    }
                    if (rate.getAvgRateT3() == null) {
                        rate.setAvgRateT3(BigDecimal.ZERO);
                    }

                    // 查询是否已存在
                    UniversalAvgSettlementRateEntity existingRate = universalAvgSettlementRateMapper.selectUniversalAvgSettlementRateByCode(
                            rate.getActuarialCode(), rate.getAccountingPeriod());

                    if (existingRate != null) {
                        if (updateSupport) {
                            rate.setId(existingRate.getId());
                            rate.setUpdateBy(operName);
                            universalAvgSettlementRateMapper.updateUniversalAvgSettlementRate(rate);
                            successNum++;
                            successMsg.append("<br/>第 ").append(successNum).append(" 条数据更新成功");
                        } else {
                            failureNum++;
                            failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据已存在");
                        }
                    } else {
                        rate.setCreateBy(operName);
                        universalAvgSettlementRateMapper.insertUniversalAvgSettlementRate(rate);
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
            log.error("导入万能平均结算利率数据失败", e);
            throw new ServiceException("导入万能平均结算利率数据失败：" + e.getMessage());
        }
    }

    /**
     * 导出万能平均结算利率数据模板
     *
     * @param response HTTP响应对象
     */
    @Override
    public void importTemplateUniversalAvgSettlementRate(HttpServletResponse response) {
        List<UniversalAvgSettlementRateDTO> templateList = new ArrayList<>();
        UniversalAvgSettlementRateDTO template = new UniversalAvgSettlementRateDTO();
        template.setAccountingPeriod("202401");
        template.setActuarialCode("SAMPLE001");
        template.setBusinessCode("BIZ001");
        template.setProductName("示例万能险产品");
        template.setShortTermFlag("N");
        template.setGuaranteedCostRate(new BigDecimal("0.025"));
        template.setAvgRateT0(new BigDecimal("0.035"));
        template.setAvgRateT1(new BigDecimal("0.036"));
        template.setAvgRateT2(new BigDecimal("0.037"));
        template.setAvgRateT3(new BigDecimal("0.038"));
        template.setRemark("示例数据");
        templateList.add(template);

        ExcelUtil<UniversalAvgSettlementRateDTO> util = new ExcelUtil<>(UniversalAvgSettlementRateDTO.class);
        util.exportExcel(templateList, "万能平均结算利率数据模板", response);
    }
}
