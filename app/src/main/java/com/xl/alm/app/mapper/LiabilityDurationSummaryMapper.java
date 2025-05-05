package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.LiabilityDurationSummaryEntity;
import com.xl.alm.app.query.LiabilityDurationSummaryQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 负债久期汇总表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface LiabilityDurationSummaryMapper {

    /**
     * 查询负债久期汇总列表
     *
     * @param query 负债久期汇总查询条件
     * @return 负债久期汇总列表
     */
    List<LiabilityDurationSummaryEntity> selectLiabilityDurationSummaryEntityList(LiabilityDurationSummaryQuery query);

    /**
     * 用id查询负债久期汇总
     *
     * @param id id
     * @return 负债久期汇总
     */
    LiabilityDurationSummaryEntity selectLiabilityDurationSummaryEntityById(Long id);

    /**
     * 根据条件查询负债久期汇总
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @param isShortTerm 是否为中短期险种
     * @return 负债久期汇总
     */
    LiabilityDurationSummaryEntity selectLiabilityDurationSummaryEntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("cashFlowType") String cashFlowType,
            @Param("bpType") String bpType,
            @Param("durationType") String durationType,
            @Param("designType") String designType,
            @Param("isShortTerm") String isShortTerm);

    /**
     * 新增负债久期汇总
     *
     * @param entity 负债久期汇总
     * @return 结果
     */
    int insertLiabilityDurationSummaryEntity(LiabilityDurationSummaryEntity entity);

    /**
     * 批量插入负债久期汇总数据
     *
     * @param entityList 负债久期汇总列表
     * @return 影响行数
     */
    int batchInsertLiabilityDurationSummaryEntity(List<LiabilityDurationSummaryEntity> entityList);

    /**
     * 修改负债久期汇总
     *
     * @param entity 负债久期汇总
     * @return 结果
     */
    int updateLiabilityDurationSummaryEntity(LiabilityDurationSummaryEntity entity);

    /**
     * 删除负债久期汇总
     *
     * @param id 负债久期汇总主键
     * @return 结果
     */
    int deleteLiabilityDurationSummaryEntityById(@Param("id") Long id);

    /**
     * 批量删除负债久期汇总
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteLiabilityDurationSummaryEntityByIds(Long[] ids);

    /**
     * 删除指定账期的负债久期汇总数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    int deleteLiabilityDurationSummaryEntityByPeriod(@Param("accountPeriod") String accountPeriod);
}
