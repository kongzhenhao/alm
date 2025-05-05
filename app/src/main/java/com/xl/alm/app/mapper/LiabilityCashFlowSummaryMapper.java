package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.LiabilityCashFlowSummaryEntity;
import com.xl.alm.app.query.LiabilityCashFlowSummaryQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 负债现金流汇总表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface LiabilityCashFlowSummaryMapper {

    /**
     * 查询负债现金流汇总列表
     *
     * @param query 负债现金流汇总查询条件
     * @return 负债现金流汇总列表
     */
    List<LiabilityCashFlowSummaryEntity> selectLiabilityCashFlowSummaryEntityList(LiabilityCashFlowSummaryQuery query);

    /**
     * 用id查询负债现金流汇总
     *
     * @param id id
     * @return 负债现金流汇总
     */
    LiabilityCashFlowSummaryEntity selectLiabilityCashFlowSummaryEntityById(Long id);

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
    LiabilityCashFlowSummaryEntity selectLiabilityCashFlowSummaryEntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("cashFlowType") String cashFlowType,
            @Param("durationType") String durationType,
            @Param("designType") String designType,
            @Param("isShortTerm") String isShortTerm);

    /**
     * 新增负债现金流汇总
     *
     * @param entity 负债现金流汇总
     * @return 结果
     */
    int insertLiabilityCashFlowSummaryEntity(LiabilityCashFlowSummaryEntity entity);

    /**
     * 批量新增负债现金流汇总
     *
     * @param entityList 负债现金流汇总列表
     * @return 结果
     */
    int batchInsertLiabilityCashFlowSummaryEntity(List<LiabilityCashFlowSummaryEntity> entityList);

    /**
     * 修改负债现金流汇总
     *
     * @param entity 负债现金流汇总
     * @return 结果
     */
    int updateLiabilityCashFlowSummaryEntity(LiabilityCashFlowSummaryEntity entity);

    /**
     * 删除负债现金流汇总
     *
     * @param id 负债现金流汇总主键
     * @return 结果
     */
    int deleteLiabilityCashFlowSummaryEntityById(Long id);

    /**
     * 批量删除负债现金流汇总
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteLiabilityCashFlowSummaryEntityByIds(Long[] ids);

    /**
     * 根据账期删除负债现金流汇总
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteLiabilityCashFlowSummaryEntityByPeriod(String accountPeriod);
}
