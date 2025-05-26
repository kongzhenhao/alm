package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.LiabilityDv10ByDurationEntity;
import com.xl.alm.app.query.LiabilityDv10ByDurationQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分中短负债基点价值DV10表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface LiabilityDv10ByDurationMapper {

    /**
     * 查询分中短负债基点价值DV10列表
     *
     * @param query 分中短负债基点价值DV10查询条件
     * @return 分中短负债基点价值DV10列表
     */
    List<LiabilityDv10ByDurationEntity> selectLiabilityDv10ByDurationEntityList(LiabilityDv10ByDurationQuery query);

    /**
     * 根据ID查询分中短负债基点价值DV10
     *
     * @param id ID
     * @return 分中短负债基点价值DV10
     */
    LiabilityDv10ByDurationEntity selectLiabilityDv10ByDurationEntityById(Long id);

    /**
     * 根据条件查询分中短负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param designType 设计类型
     * @param shortTermFlag 是否为中短期险种
     * @param valueType 现值类型
     * @return 分中短负债基点价值DV10
     */
    LiabilityDv10ByDurationEntity selectLiabilityDv10ByDurationEntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("cashFlowType") String cashFlowType,
            @Param("designType") String designType,
            @Param("shortTermFlag") String shortTermFlag,
            @Param("valueType") String valueType);

    /**
     * 新增分中短负债基点价值DV10
     *
     * @param entity 分中短负债基点价值DV10
     * @return 结果
     */
    int insertLiabilityDv10ByDurationEntity(LiabilityDv10ByDurationEntity entity);

    /**
     * 批量新增分中短负债基点价值DV10
     *
     * @param entityList 分中短负债基点价值DV10列表
     * @return 结果
     */
    int batchInsertLiabilityDv10ByDurationEntity(List<LiabilityDv10ByDurationEntity> entityList);

    /**
     * 修改分中短负债基点价值DV10
     *
     * @param entity 分中短负债基点价值DV10
     * @return 结果
     */
    int updateLiabilityDv10ByDurationEntity(LiabilityDv10ByDurationEntity entity);

    /**
     * 删除分中短负债基点价值DV10
     *
     * @param id 分中短负债基点价值DV10ID
     * @return 结果
     */
    int deleteLiabilityDv10ByDurationEntityById(Long id);

    /**
     * 批量删除分中短负债基点价值DV10
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteLiabilityDv10ByDurationEntityByIds(Long[] ids);

    /**
     * 删除指定账期的分中短负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteLiabilityDv10ByDurationEntityByPeriod(String accountPeriod);
}
