package com.xl.alm.app.service.impl;

import com.jd.lightning.common.exception.ServiceException;
import com.jd.lightning.common.utils.DateUtils;
import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.entity.StatutoryReserveForecastEntity;
import com.xl.alm.app.mapper.StatutoryReserveForecastMapper;
import com.xl.alm.app.query.StatutoryReserveForecastQuery;
import com.xl.alm.app.service.IStatutoryReserveForecastService;
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
 * 法定准备金预测表 服务实现类
 *
 * @author AI Assistant
 */
@Service
public class StatutoryReserveForecastServiceImpl implements IStatutoryReserveForecastService {
    private static final Logger log = LoggerFactory.getLogger(StatutoryReserveForecastServiceImpl.class);

    @Autowired
    private StatutoryReserveForecastMapper statutoryReserveForecastMapper;

    /**
     * 查询法定准备金预测列表
     *
     * @param query 法定准备金预测查询条件
     * @return 法定准备金预测列表
     */
    @Override
    public List<StatutoryReserveForecastEntity> selectStatutoryReserveForecastList(StatutoryReserveForecastQuery query) {
        return statutoryReserveForecastMapper.selectStatutoryReserveForecastList(query);
    }

    /**
     * 根据ID查询法定准备金预测
     *
     * @param id 法定准备金预测ID
     * @return 法定准备金预测
     */
    @Override
    public StatutoryReserveForecastEntity selectStatutoryReserveForecastById(Long id) {
        return statutoryReserveForecastMapper.selectStatutoryReserveForecastById(id);
    }

    /**
     * 新增法定准备金预测
     *
     * @param statutoryReserveForecast 法定准备金预测
     * @return 结果
     */
    @Override
    public int insertStatutoryReserveForecast(StatutoryReserveForecastEntity statutoryReserveForecast) {
        statutoryReserveForecast.setCreateTime(DateUtils.getNowDate());

        // 设置业务类型默认值
        if (StringUtils.isBlank(statutoryReserveForecast.getBusinessType())) {
            statutoryReserveForecast.setBusinessType("LIFE");
        }

        // 设置长短期标识默认值
        if (StringUtils.isBlank(statutoryReserveForecast.getShortTermFlag())) {
            statutoryReserveForecast.setShortTermFlag("N");
        }

        // 计算法定准备金预测合计
        calculateTotalStatutoryReserveForecast(statutoryReserveForecast);
        return statutoryReserveForecastMapper.insertStatutoryReserveForecast(statutoryReserveForecast);
    }

    /**
     * 修改法定准备金预测
     *
     * @param statutoryReserveForecast 法定准备金预测
     * @return 结果
     */
    @Override
    public int updateStatutoryReserveForecast(StatutoryReserveForecastEntity statutoryReserveForecast) {
        statutoryReserveForecast.setUpdateTime(DateUtils.getNowDate());

        // 设置业务类型默认值
        if (StringUtils.isBlank(statutoryReserveForecast.getBusinessType())) {
            statutoryReserveForecast.setBusinessType("LIFE");
        }

        // 设置长短期标识默认值
        if (StringUtils.isBlank(statutoryReserveForecast.getShortTermFlag())) {
            statutoryReserveForecast.setShortTermFlag("N");
        }

        // 计算法定准备金预测合计
        calculateTotalStatutoryReserveForecast(statutoryReserveForecast);
        return statutoryReserveForecastMapper.updateStatutoryReserveForecast(statutoryReserveForecast);
    }

    /**
     * 批量删除法定准备金预测
     *
     * @param ids 需要删除的法定准备金预测ID
     * @return 结果
     */
    @Override
    public int deleteStatutoryReserveForecastByIds(Long[] ids) {
        return statutoryReserveForecastMapper.deleteStatutoryReserveForecastByIds(ids);
    }

    /**
     * 删除法定准备金预测信息
     *
     * @param id 法定准备金预测ID
     * @return 结果
     */
    @Override
    public int deleteStatutoryReserveForecastById(Long id) {
        return statutoryReserveForecastMapper.deleteStatutoryReserveForecastById(id);
    }

    /**
     * 导入法定准备金预测数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importStatutoryReserveForecast(MultipartFile file, boolean updateSupport, String operName) {
        try {
            ExcelUtil<StatutoryReserveForecastEntity> util = new ExcelUtil<>(StatutoryReserveForecastEntity.class);
            List<StatutoryReserveForecastEntity> forecastList = util.importExcel(file.getInputStream());
            int successNum = 0;
            int failureNum = 0;
            StringBuilder successMsg = new StringBuilder();
            StringBuilder failureMsg = new StringBuilder();

            for (StatutoryReserveForecastEntity forecast : forecastList) {
                try {
                    // 验证必填字段
                    if (StringUtils.isBlank(forecast.getAccountingPeriod()) ||
                        StringUtils.isBlank(forecast.getActuarialCode())) {
                        failureNum++;
                        failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据账期或精算代码为空");
                        continue;
                    }

                    // 设置业务类型默认值
                    if (StringUtils.isBlank(forecast.getBusinessType())) {
                        forecast.setBusinessType("LIFE");
                    }

                    // 设置默认值
                    if (StringUtils.isBlank(forecast.getShortTermFlag())) {
                        forecast.setShortTermFlag("N");
                    }
                    if (forecast.getStatutoryReserveT1() == null) {
                        forecast.setStatutoryReserveT1(BigDecimal.ZERO);
                    }
                    if (forecast.getStatutoryReserveT2() == null) {
                        forecast.setStatutoryReserveT2(BigDecimal.ZERO);
                    }
                    if (forecast.getStatutoryReserveT3() == null) {
                        forecast.setStatutoryReserveT3(BigDecimal.ZERO);
                    }

                    // 查询是否已存在
                    StatutoryReserveForecastEntity existingForecast = statutoryReserveForecastMapper.selectStatutoryReserveForecastByCode(
                            forecast.getBusinessType(), forecast.getAccountingPeriod(), forecast.getActuarialCode());

                    if (existingForecast != null) {
                        if (updateSupport) {
                            forecast.setId(existingForecast.getId());
                            forecast.setUpdateBy(operName);
                            statutoryReserveForecastMapper.updateStatutoryReserveForecast(forecast);
                            successNum++;
                            successMsg.append("<br/>第 ").append(successNum).append(" 条数据更新成功");
                        } else {
                            failureNum++;
                            failureMsg.append("<br/>第 ").append(failureNum).append(" 条数据已存在");
                        }
                    } else {
                        forecast.setCreateBy(operName);
                        statutoryReserveForecastMapper.insertStatutoryReserveForecast(forecast);
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
            log.error("导入法定准备金预测数据失败", e);
            throw new ServiceException("导入法定准备金预测数据失败：" + e.getMessage());
        }
    }

    /**
     * 导出法定准备金预测数据模板
     *
     * @param response HTTP响应对象
     */
    @Override
    public void importTemplateStatutoryReserveForecast(HttpServletResponse response) {
        List<StatutoryReserveForecastEntity> templateList = new ArrayList<>();
        StatutoryReserveForecastEntity template = new StatutoryReserveForecastEntity();
        template.setBusinessType("LIFE");
        template.setAccountingPeriod("202401");
        template.setActuarialCode("SAMPLE001");
        template.setBusinessCode("BIZ001");
        template.setProductName("示例产品");
        template.setDesignType("传统险");

        template.setShortTermFlag("N");
        template.setStatutoryReserveT1(new BigDecimal("9000.00"));
        template.setStatutoryReserveT2(new BigDecimal("9500.00"));
        template.setStatutoryReserveT3(new BigDecimal("10000.00"));
        template.setRemark("示例数据");
        templateList.add(template);

        ExcelUtil<StatutoryReserveForecastEntity> util = new ExcelUtil<>(StatutoryReserveForecastEntity.class);
        util.exportExcel(templateList, "法定准备金预测数据模板", response);
    }

    /**
     * 计算法定准备金预测合计
     *
     * @param forecast 法定准备金预测
     */
    private void calculateTotalStatutoryReserveForecast(StatutoryReserveForecastEntity forecast) {
        // 注意：在实际表结构中没有合计字段，所以这里不需要计算
        // 如果需要在前端显示合计，可以在前端计算
    }
}
