package com.xl.alm.app.service;

import com.xl.alm.app.entity.CostAccountEffectiveRateEntity;
import com.xl.alm.app.query.CostAccountEffectiveRateQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分账户有效成本率表 服务接口
 *
 * @author AI Assistant
 */
public interface ICostAccountEffectiveRateService {
    /**
     * 查询分账户有效成本率列表
     *
     * @param query 分账户有效成本率查询条件
     * @return 分账户有效成本率列表
     */
    List<CostAccountEffectiveRateEntity> selectCostAccountEffectiveRateList(CostAccountEffectiveRateQuery query);

    /**
     * 根据ID查询分账户有效成本率
     *
     * @param id 分账户有效成本率ID
     * @return 分账户有效成本率
     */
    CostAccountEffectiveRateEntity selectCostAccountEffectiveRateById(Long id);

    /**
     * 新增分账户有效成本率
     *
     * @param costAccountEffectiveRate 分账户有效成本率
     * @return 结果
     */
    int insertCostAccountEffectiveRate(CostAccountEffectiveRateEntity costAccountEffectiveRate);

    /**
     * 修改分账户有效成本率
     *
     * @param costAccountEffectiveRate 分账户有效成本率
     * @return 结果
     */
    int updateCostAccountEffectiveRate(CostAccountEffectiveRateEntity costAccountEffectiveRate);

    /**
     * 批量删除分账户有效成本率
     *
     * @param ids 需要删除的分账户有效成本率ID
     * @return 结果
     */
    int deleteCostAccountEffectiveRateByIds(Long[] ids);

    /**
     * 删除分账户有效成本率信息
     *
     * @param id 分账户有效成本率ID
     * @return 结果
     */
    int deleteCostAccountEffectiveRateById(Long id);

    /**
     * 导入分账户有效成本率数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    String importCostAccountEffectiveRate(MultipartFile file, boolean updateSupport, String operName);

    /**
     * 导出分账户有效成本率数据模板
     *
     * @param response HTTP响应对象
     */
    void importTemplateCostAccountEffectiveRate(HttpServletResponse response);
}
