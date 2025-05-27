package com.xl.alm.app.service;

import com.xl.alm.app.entity.DividendFundCostRateEntity;
import com.xl.alm.app.query.DividendFundCostRateQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 分红方案表 服务接口
 *
 * @author AI Assistant
 */
public interface IDividendFundCostRateService {
    /**
     * 查询分红方案列表
     *
     * @param query 分红方案查询条件
     * @return 分红方案列表
     */
    List<DividendFundCostRateEntity> selectDividendFundCostRateList(DividendFundCostRateQuery query);

    /**
     * 根据ID查询分红方案
     *
     * @param id 分红方案ID
     * @return 分红方案
     */
    DividendFundCostRateEntity selectDividendFundCostRateById(Long id);

    /**
     * 新增分红方案
     *
     * @param dividendFundCostRate 分红方案
     * @return 结果
     */
    int insertDividendFundCostRate(DividendFundCostRateEntity dividendFundCostRate);

    /**
     * 修改分红方案
     *
     * @param dividendFundCostRate 分红方案
     * @return 结果
     */
    int updateDividendFundCostRate(DividendFundCostRateEntity dividendFundCostRate);

    /**
     * 批量删除分红方案
     *
     * @param ids 需要删除的分红方案ID
     * @return 结果
     */
    int deleteDividendFundCostRateByIds(Long[] ids);

    /**
     * 删除分红方案信息
     *
     * @param id 分红方案ID
     * @return 结果
     */
    int deleteDividendFundCostRateById(Long id);

    /**
     * 导入分红方案数据
     *
     * @param file 导入文件
     * @param updateSupport 是否更新已存在数据
     * @param operName 操作人员
     * @return 结果
     */
    String importDividendFundCostRate(MultipartFile file, boolean updateSupport, String operName);

    /**
     * 导出分红方案数据模板
     *
     * @param response HTTP响应对象
     */
    void importTemplateDividendFundCostRate(HttpServletResponse response);
}
