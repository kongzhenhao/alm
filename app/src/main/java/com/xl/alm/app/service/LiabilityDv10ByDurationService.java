package com.xl.alm.app.service;

import com.xl.alm.app.dto.LiabilityDv10ByDurationDTO;
import com.xl.alm.app.query.LiabilityDv10ByDurationQuery;

import java.util.List;

/**
 * 分中短负债基点价值DV10表 Service 接口
 *
 * @author AI Assistant
 */
public interface LiabilityDv10ByDurationService {

    /**
     * 查询分中短负债基点价值DV10列表
     *
     * @param query 分中短负债基点价值DV10查询条件
     * @return 分中短负债基点价值DV10列表
     */
    List<LiabilityDv10ByDurationDTO> selectLiabilityDv10ByDurationDtoList(LiabilityDv10ByDurationQuery query);

    /**
     * 根据ID查询分中短负债基点价值DV10
     *
     * @param id ID
     * @return 分中短负债基点价值DV10
     */
    LiabilityDv10ByDurationDTO selectLiabilityDv10ByDurationDtoById(Long id);

    /**
     * 根据条件查询分中短负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @param cashFlowType  现金流类型
     * @param designType    设计类型
     * @param shortTermFlag 是否为中短期险种
     * @param valueType     现值类型
     * @return 分中短负债基点价值DV10
     */
    LiabilityDv10ByDurationDTO selectLiabilityDv10ByDurationDtoByCondition(
            String accountPeriod,
            String cashFlowType,
            String designType,
            String shortTermFlag,
            String valueType);

    /**
     * 新增分中短负债基点价值DV10
     *
     * @param dto 分中短负债基点价值DV10
     * @return 结果
     */
    int insertLiabilityDv10ByDurationDto(LiabilityDv10ByDurationDTO dto);

    /**
     * 批量新增分中短负债基点价值DV10
     *
     * @param dtoList 分中短负债基点价值DV10列表
     * @return 结果
     */
    int batchInsertLiabilityDv10ByDurationDto(List<LiabilityDv10ByDurationDTO> dtoList);

    /**
     * 修改分中短负债基点价值DV10
     *
     * @param dto 分中短负债基点价值DV10
     * @return 结果
     */
    int updateLiabilityDv10ByDurationDto(LiabilityDv10ByDurationDTO dto);

    /**
     * 删除分中短负债基点价值DV10
     *
     * @param id 分中短负债基点价值DV10ID
     * @return 结果
     */
    int deleteLiabilityDv10ByDurationDtoById(Long id);

    /**
     * 批量删除分中短负债基点价值DV10
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteLiabilityDv10ByDurationDtoByIds(Long[] ids);

    /**
     * 删除指定账期的分中短负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteLiabilityDv10ByDurationDtoByPeriod(String accountPeriod);

    /**
     * 导入分中短负债基点价值DV10数据
     *
     * @param dtoList       分中短负债基点价值DV10列表
     * @param updateSupport 是否更新已存在的数据
     * @param operName      操作人
     * @return 结果
     */
    String importLiabilityDv10ByDurationDto(List<LiabilityDv10ByDurationDTO> dtoList, boolean updateSupport, String operName);
}
