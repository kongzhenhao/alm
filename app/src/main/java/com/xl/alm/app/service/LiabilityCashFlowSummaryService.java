package com.xl.alm.app.service;

import com.xl.alm.app.dto.LiabilityCashFlowSummaryDTO;
import com.xl.alm.app.query.LiabilityCashFlowSummaryQuery;

import java.util.List;

/**
 * 负债现金流汇总表 Service 接口
 *
 * @author AI Assistant
 */
public interface LiabilityCashFlowSummaryService {

    /**
     * 查询负债现金流汇总列表
     *
     * @param query 负债现金流汇总查询条件
     * @return 负债现金流汇总列表
     */
    List<LiabilityCashFlowSummaryDTO> selectLiabilityCashFlowSummaryDtoList(LiabilityCashFlowSummaryQuery query);

    /**
     * 用id查询负债现金流汇总
     *
     * @param id id
     * @return 负债现金流汇总
     */
    LiabilityCashFlowSummaryDTO selectLiabilityCashFlowSummaryDtoById(Long id);

    /**
     * 根据条件查询负债现金流汇总
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @param isShortTerm 是否中短期险种
     * @return 负债现金流汇总
     */
    LiabilityCashFlowSummaryDTO selectLiabilityCashFlowSummaryDtoByCondition(
            String accountPeriod,
            String cashFlowType,
            String durationType,
            String designType,
            String isShortTerm);

    /**
     * 新增负债现金流汇总
     *
     * @param dto 负债现金流汇总
     * @return 结果
     */
    int insertLiabilityCashFlowSummaryDto(LiabilityCashFlowSummaryDTO dto);

    /**
     * 批量新增负债现金流汇总
     *
     * @param dtoList 负债现金流汇总列表
     * @return 结果
     */
    int batchInsertLiabilityCashFlowSummaryDto(List<LiabilityCashFlowSummaryDTO> dtoList);

    /**
     * 修改负债现金流汇总
     *
     * @param dto 负债现金流汇总
     * @return 结果
     */
    int updateLiabilityCashFlowSummaryDto(LiabilityCashFlowSummaryDTO dto);

    /**
     * 批量删除负债现金流汇总
     *
     * @param ids 需要删除的负债现金流汇总主键集合
     * @return 结果
     */
    int deleteLiabilityCashFlowSummaryDtoByIds(Long[] ids);

    /**
     * 删除负债现金流汇总信息
     *
     * @param id 负债现金流汇总主键
     * @return 结果
     */
    int deleteLiabilityCashFlowSummaryDtoById(Long id);

    /**
     * 根据账期删除负债现金流汇总
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteLiabilityCashFlowSummaryDtoByPeriod(String accountPeriod);

    /**
     * 导入负债现金流汇总数据
     *
     * @param dtoList 负债现金流汇总数据列表
     * @param updateSupport 是否支持更新，如果已存在，则进行更新
     * @param operName 操作用户
     * @return 结果
     */
    String importLiabilityCashFlowSummaryDto(List<LiabilityCashFlowSummaryDTO> dtoList, boolean updateSupport, String operName);
}
