package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.RiskItemAmountEntity;
import com.xl.alm.app.query.RiskItemAmountQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 风险项目金额表 数据层
 *
 * @author AI Assistant
 */
@Mapper
public interface RiskItemAmountMapper {
    /**
     * 查询风险项目金额列表
     *
     * @param query 查询条件
     * @return 风险项目金额集合
     */
    List<RiskItemAmountEntity> selectRiskItemAmountEntityList(RiskItemAmountQuery query);

    /**
     * 查询风险项目金额详情
     *
     * @param id 主键ID
     * @return 风险项目金额
     */
    RiskItemAmountEntity selectRiskItemAmountEntityById(Long id);

    /**
     * 新增风险项目金额
     *
     * @param entity 风险项目金额
     * @return 结果
     */
    int insertRiskItemAmountEntity(RiskItemAmountEntity entity);

    /**
     * 批量新增风险项目金额
     *
     * @param entityList 风险项目金额列表
     * @return 结果
     */
    int batchInsertRiskItemAmountEntity(List<RiskItemAmountEntity> entityList);

    /**
     * 修改风险项目金额
     *
     * @param entity 风险项目金额
     * @return 结果
     */
    int updateRiskItemAmountEntity(RiskItemAmountEntity entity);

    /**
     * 删除风险项目金额
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteRiskItemAmountEntityById(Long id);

    /**
     * 批量删除风险项目金额
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteRiskItemAmountEntityByIds(Long[] ids);

    /**
     * 根据账期删除风险项目金额
     *
     * @param accountingPeriod 账期
     * @return 结果
     */
    int deleteRiskItemAmountEntityByPeriod(@Param("accountingPeriod") String accountingPeriod);

    /**
     * 根据条件删除风险项目金额
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @return 结果
     */
    int deleteRiskItemAmountEntityByCondition(@Param("accountingPeriod") String accountingPeriod,
                                             @Param("itemCode") String itemCode);
}
