package com.xl.alm.app.service;

import com.xl.alm.app.dto.LiabilityDurationSummaryDTO;
import com.xl.alm.app.query.LiabilityDurationSummaryQuery;

import java.util.List;

/**
 * 负债久期汇总表 Service 接口
 *
 * @author AI Assistant
 */
public interface LiabilityDurationSummaryService {

    /**
     * 查询负债久期汇总列表
     *
     * @param query 负债久期汇总查询条件
     * @return 负债久期汇总列表
     */
    List<LiabilityDurationSummaryDTO> selectLiabilityDurationSummaryDtoList(LiabilityDurationSummaryQuery query);

    /**
     * 用id查询负债久期汇总
     *
     * @param id id
     * @return 负债久期汇总
     */
    LiabilityDurationSummaryDTO selectLiabilityDurationSummaryDtoById(Long id);

    /**
     * 根据条件查询负债久期汇总
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @param isShortTerm 是否为中短期险种
     * @return 负债久期汇总
     */
    LiabilityDurationSummaryDTO selectLiabilityDurationSummaryDtoByCondition(
            String accountPeriod,
            String cashFlowType,
            String durationType,
            String designType,
            String isShortTerm);

    /**
     * 新增负债久期汇总
     *
     * @param dto 负债久期汇总
     * @return 结果
     */
    int insertLiabilityDurationSummaryDto(LiabilityDurationSummaryDTO dto);

    /**
     * 批量新增负债久期汇总
     *
     * @param dtoList 负债久期汇总列表
     * @return 结果
     */
    int batchInsertLiabilityDurationSummaryDto(List<LiabilityDurationSummaryDTO> dtoList);

    /**
     * 修改负债久期汇总
     *
     * @param dto 负债久期汇总
     * @return 结果
     */
    int updateLiabilityDurationSummaryDto(LiabilityDurationSummaryDTO dto);

    /**
     * 批量删除负债久期汇总
     *
     * @param ids 需要删除的负债久期汇总主键集合
     * @return 结果
     */
    int deleteLiabilityDurationSummaryDtoByIds(Long[] ids);

    /**
     * 根据账期删除负债久期汇总
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteLiabilityDurationSummaryDtoByPeriod(String accountPeriod);

    /**
     * 导入负债久期汇总数据
     *
     * @param dtoList 负债久期汇总数据列表
     * @param updateSupport 是否支持更新，如果已存在，则进行更新
     * @param operName 操作用户
     * @return 结果
     */
    String importLiabilityDurationSummaryDto(List<LiabilityDurationSummaryDTO> dtoList, boolean updateSupport, String operName);
}
