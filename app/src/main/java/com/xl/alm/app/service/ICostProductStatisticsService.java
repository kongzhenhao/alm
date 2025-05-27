package com.xl.alm.app.service;

import com.xl.alm.app.entity.CostProductStatisticsEntity;
import com.xl.alm.app.query.CostProductStatisticsQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分产品统计表 服务接口
 *
 * @author AI Assistant
 */
public interface ICostProductStatisticsService {
    /**
     * 查询分产品统计列表
     *
     * @param query 分产品统计查询条件
     * @return 分产品统计列表
     */
    List<CostProductStatisticsEntity> selectCostProductStatisticsList(CostProductStatisticsQuery query);

    /**
     * 根据ID查询分产品统计
     *
     * @param id 分产品统计ID
     * @return 分产品统计
     */
    CostProductStatisticsEntity selectCostProductStatisticsById(Long id);

    /**
     * 新增分产品统计
     *
     * @param costProductStatistics 分产品统计
     * @return 结果
     */
    int insertCostProductStatistics(CostProductStatisticsEntity costProductStatistics);

    /**
     * 修改分产品统计
     *
     * @param costProductStatistics 分产品统计
     * @return 结果
     */
    int updateCostProductStatistics(CostProductStatisticsEntity costProductStatistics);

    /**
     * 批量删除分产品统计
     *
     * @param ids 需要删除的分产品统计ID
     * @return 结果
     */
    int deleteCostProductStatisticsByIds(Long[] ids);

    /**
     * 删除分产品统计信息
     *
     * @param id 分产品统计ID
     * @return 结果
     */
    int deleteCostProductStatisticsById(Long id);

    /**
     * 导入分产品统计数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    String importCostProductStatistics(MultipartFile file, boolean updateSupport, String operName);

    /**
     * 导出分产品统计数据模板
     *
     * @param response HTTP响应对象
     */
    void importTemplateCostProductStatistics(HttpServletResponse response);
}
