package com.xl.alm.app.service;

import com.xl.alm.app.entity.UniversalAvgSettlementRateEntity;
import com.xl.alm.app.query.UniversalAvgSettlementRateQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 万能平均结算利率表 服务接口
 *
 * @author AI Assistant
 */
public interface IUniversalAvgSettlementRateService {
    /**
     * 查询万能平均结算利率列表
     *
     * @param query 万能平均结算利率查询条件
     * @return 万能平均结算利率列表
     */
    List<UniversalAvgSettlementRateEntity> selectUniversalAvgSettlementRateList(UniversalAvgSettlementRateQuery query);

    /**
     * 根据ID查询万能平均结算利率
     *
     * @param id 万能平均结算利率ID
     * @return 万能平均结算利率
     */
    UniversalAvgSettlementRateEntity selectUniversalAvgSettlementRateById(Long id);

    /**
     * 新增万能平均结算利率
     *
     * @param universalAvgSettlementRate 万能平均结算利率
     * @return 结果
     */
    int insertUniversalAvgSettlementRate(UniversalAvgSettlementRateEntity universalAvgSettlementRate);

    /**
     * 修改万能平均结算利率
     *
     * @param universalAvgSettlementRate 万能平均结算利率
     * @return 结果
     */
    int updateUniversalAvgSettlementRate(UniversalAvgSettlementRateEntity universalAvgSettlementRate);

    /**
     * 批量删除万能平均结算利率
     *
     * @param ids 需要删除的万能平均结算利率ID
     * @return 结果
     */
    int deleteUniversalAvgSettlementRateByIds(Long[] ids);

    /**
     * 删除万能平均结算利率信息
     *
     * @param id 万能平均结算利率ID
     * @return 结果
     */
    int deleteUniversalAvgSettlementRateById(Long id);

    /**
     * 导入万能平均结算利率数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    String importUniversalAvgSettlementRate(MultipartFile file, boolean updateSupport, String operName);

    /**
     * 导出万能平均结算利率数据模板
     *
     * @param response HTTP响应对象
     */
    void importTemplateUniversalAvgSettlementRate(HttpServletResponse response);
}
