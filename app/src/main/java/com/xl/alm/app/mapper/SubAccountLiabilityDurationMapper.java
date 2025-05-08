package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.SubAccountLiabilityDurationEntity;
import com.xl.alm.app.query.SubAccountLiabilityDurationQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分账户负债久期汇总表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface SubAccountLiabilityDurationMapper {

    /**
     * 查询分账户负债久期汇总列表
     *
     * @param query 分账户负债久期汇总查询条件
     * @return 分账户负债久期汇总列表
     */
    List<SubAccountLiabilityDurationEntity> selectSubAccountLiabilityDurationEntityList(SubAccountLiabilityDurationQuery query);

    /**
     * 用id查询分账户负债久期汇总
     *
     * @param id id
     * @return 分账户负债久期汇总
     */
    SubAccountLiabilityDurationEntity selectSubAccountLiabilityDurationEntityById(Long id);

    /**
     * 根据条件查询分账户负债久期汇总
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @return 分账户负债久期汇总
     */
    SubAccountLiabilityDurationEntity selectSubAccountLiabilityDurationEntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("cashFlowType") String cashFlowType,
            @Param("durationType") String durationType,
            @Param("designType") String designType);

    /**
     * 新增分账户负债久期汇总
     *
     * @param entity 分账户负债久期汇总
     * @return 结果
     */
    int insertSubAccountLiabilityDurationEntity(SubAccountLiabilityDurationEntity entity);

    /**
     * 批量新增分账户负债久期汇总
     *
     * @param entityList 分账户负债久期汇总列表
     * @return 结果
     */
    int batchInsertSubAccountLiabilityDurationEntity(List<SubAccountLiabilityDurationEntity> entityList);

    /**
     * 修改分账户负债久期汇总
     *
     * @param entity 分账户负债久期汇总
     * @return 结果
     */
    int updateSubAccountLiabilityDurationEntity(SubAccountLiabilityDurationEntity entity);

    /**
     * 删除分账户负债久期汇总
     *
     * @param id 分账户负债久期汇总主键
     * @return 结果
     */
    int deleteSubAccountLiabilityDurationEntityById(Long id);

    /**
     * 批量删除分账户负债久期汇总
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSubAccountLiabilityDurationEntityByIds(Long[] ids);

    /**
     * 删除指定账期的分账户负债久期汇总
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteSubAccountLiabilityDurationEntityByPeriod(@Param("accountPeriod") String accountPeriod);
}
