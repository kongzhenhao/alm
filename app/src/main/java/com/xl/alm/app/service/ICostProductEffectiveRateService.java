package com.xl.alm.app.service;

import com.xl.alm.app.entity.CostProductEffectiveRateEntity;
import com.xl.alm.app.query.CostProductEffectiveRateQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分产品有效成本率表 服务接口
 *
 * @author AI Assistant
 */
public interface ICostProductEffectiveRateService {
    /**
     * 查询分产品有效成本率列表
     *
     * @param query 分产品有效成本率查询条件
     * @return 分产品有效成本率列表
     */
    List<CostProductEffectiveRateEntity> selectCostProductEffectiveRateList(CostProductEffectiveRateQuery query);

    /**
     * 根据ID查询分产品有效成本率
     *
     * @param id 分产品有效成本率ID
     * @return 分产品有效成本率
     */
    CostProductEffectiveRateEntity selectCostProductEffectiveRateById(Long id);

    /**
     * 新增分产品有效成本率
     *
     * @param costProductEffectiveRate 分产品有效成本率
     * @return 结果
     */
    int insertCostProductEffectiveRate(CostProductEffectiveRateEntity costProductEffectiveRate);

    /**
     * 修改分产品有效成本率
     *
     * @param costProductEffectiveRate 分产品有效成本率
     * @return 结果
     */
    int updateCostProductEffectiveRate(CostProductEffectiveRateEntity costProductEffectiveRate);

    /**
     * 批量删除分产品有效成本率
     *
     * @param ids 需要删除的分产品有效成本率ID
     * @return 结果
     */
    int deleteCostProductEffectiveRateByIds(Long[] ids);

    /**
     * 删除分产品有效成本率信息
     *
     * @param id 分产品有效成本率ID
     * @return 结果
     */
    int deleteCostProductEffectiveRateById(Long id);

    /**
     * 导入分产品有效成本率数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    String importCostProductEffectiveRate(MultipartFile file, boolean updateSupport, String operName);

    /**
     * 导出分产品有效成本率数据模板
     *
     * @param response HTTP响应对象
     */
    void importTemplateCostProductEffectiveRate(HttpServletResponse response);
}
