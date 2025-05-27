package com.xl.alm.app.service;

import com.xl.alm.app.entity.CostAccountSummaryEntity;
import com.xl.alm.app.query.CostAccountSummaryQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分账户汇总表 服务接口
 *
 * @author AI Assistant
 */
public interface ICostAccountSummaryService {
    /**
     * 查询分账户汇总列表
     *
     * @param query 分账户汇总查询条件
     * @return 分账户汇总列表
     */
    List<CostAccountSummaryEntity> selectCostAccountSummaryList(CostAccountSummaryQuery query);

    /**
     * 根据ID查询分账户汇总
     *
     * @param id 分账户汇总ID
     * @return 分账户汇总
     */
    CostAccountSummaryEntity selectCostAccountSummaryById(Long id);

    /**
     * 新增分账户汇总
     *
     * @param costAccountSummary 分账户汇总
     * @return 结果
     */
    int insertCostAccountSummary(CostAccountSummaryEntity costAccountSummary);

    /**
     * 修改分账户汇总
     *
     * @param costAccountSummary 分账户汇总
     * @return 结果
     */
    int updateCostAccountSummary(CostAccountSummaryEntity costAccountSummary);

    /**
     * 批量删除分账户汇总
     *
     * @param ids 需要删除的分账户汇总ID
     * @return 结果
     */
    int deleteCostAccountSummaryByIds(Long[] ids);

    /**
     * 删除分账户汇总信息
     *
     * @param id 分账户汇总ID
     * @return 结果
     */
    int deleteCostAccountSummaryById(Long id);

    /**
     * 导入分账户汇总数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    String importCostAccountSummary(MultipartFile file, boolean updateSupport, String operName);

    /**
     * 导出分账户汇总数据模板
     *
     * @param response HTTP响应对象
     */
    void importTemplateCostAccountSummary(HttpServletResponse response);
}
