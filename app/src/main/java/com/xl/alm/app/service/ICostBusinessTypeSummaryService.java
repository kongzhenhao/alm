package com.xl.alm.app.service;

import com.xl.alm.app.entity.CostBusinessTypeSummaryEntity;
import com.xl.alm.app.query.CostBusinessTypeSummaryQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分业务类型汇总表 服务接口
 *
 * @author AI Assistant
 */
public interface ICostBusinessTypeSummaryService {
    /**
     * 查询分业务类型汇总列表
     *
     * @param query 分业务类型汇总查询条件
     * @return 分业务类型汇总列表
     */
    List<CostBusinessTypeSummaryEntity> selectCostBusinessTypeSummaryList(CostBusinessTypeSummaryQuery query);

    /**
     * 根据ID查询分业务类型汇总
     *
     * @param id 分业务类型汇总ID
     * @return 分业务类型汇总
     */
    CostBusinessTypeSummaryEntity selectCostBusinessTypeSummaryById(Long id);

    /**
     * 新增分业务类型汇总
     *
     * @param costBusinessTypeSummary 分业务类型汇总
     * @return 结果
     */
    int insertCostBusinessTypeSummary(CostBusinessTypeSummaryEntity costBusinessTypeSummary);

    /**
     * 修改分业务类型汇总
     *
     * @param costBusinessTypeSummary 分业务类型汇总
     * @return 结果
     */
    int updateCostBusinessTypeSummary(CostBusinessTypeSummaryEntity costBusinessTypeSummary);

    /**
     * 批量删除分业务类型汇总
     *
     * @param ids 需要删除的分业务类型汇总ID
     * @return 结果
     */
    int deleteCostBusinessTypeSummaryByIds(Long[] ids);

    /**
     * 删除分业务类型汇总信息
     *
     * @param id 分业务类型汇总ID
     * @return 结果
     */
    int deleteCostBusinessTypeSummaryById(Long id);

    /**
     * 导入分业务类型汇总数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    String importCostBusinessTypeSummary(MultipartFile file, boolean updateSupport, String operName);

    /**
     * 导出分业务类型汇总数据模板
     *
     * @param response HTTP响应对象
     */
    void importTemplateCostBusinessTypeSummary(HttpServletResponse response);
}
