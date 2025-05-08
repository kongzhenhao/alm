package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.SubAccountLiabilityPresentValueEntity;
import com.xl.alm.app.query.SubAccountLiabilityPresentValueQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分账户负债现金流现值汇总表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface SubAccountLiabilityPresentValueMapper {

    /**
     * 查询分账户负债现金流现值汇总列表
     *
     * @param query 分账户负债现金流现值汇总查询条件
     * @return 分账户负债现金流现值汇总列表
     */
    List<SubAccountLiabilityPresentValueEntity> selectSubAccountLiabilityPresentValueEntityList(SubAccountLiabilityPresentValueQuery query);

    /**
     * 用id查询分账户负债现金流现值汇总
     *
     * @param id id
     * @return 分账户负债现金流现值汇总
     */
    SubAccountLiabilityPresentValueEntity selectSubAccountLiabilityPresentValueEntityById(Long id);

    /**
     * 根据条件查询分账户负债现金流现值汇总
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @return 分账户负债现金流现值汇总
     */
    SubAccountLiabilityPresentValueEntity selectSubAccountLiabilityPresentValueEntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("cashFlowType") String cashFlowType,
            @Param("bpType") String bpType,
            @Param("durationType") String durationType,
            @Param("designType") String designType);

    /**
     * 新增分账户负债现金流现值汇总
     *
     * @param entity 分账户负债现金流现值汇总
     * @return 结果
     */
    int insertSubAccountLiabilityPresentValueEntity(SubAccountLiabilityPresentValueEntity entity);

    /**
     * 批量插入分账户负债现金流现值汇总数据
     *
     * @param entityList 分账户负债现金流现值汇总列表
     * @return 影响行数
     */
    int batchInsertSubAccountLiabilityPresentValueEntity(List<SubAccountLiabilityPresentValueEntity> entityList);

    /**
     * 修改分账户负债现金流现值汇总
     *
     * @param entity 分账户负债现金流现值汇总
     * @return 结果
     */
    int updateSubAccountLiabilityPresentValueEntity(SubAccountLiabilityPresentValueEntity entity);

    /**
     * 删除分账户负债现金流现值汇总
     *
     * @param id 分账户负债现金流现值汇总主键
     * @return 结果
     */
    int deleteSubAccountLiabilityPresentValueEntityById(@Param("id") Long id);

    /**
     * 批量删除分账户负债现金流现值汇总
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSubAccountLiabilityPresentValueEntityByIds(Long[] ids);

    /**
     * 删除指定账期的分账户负债现金流现值汇总数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    int deleteSubAccountLiabilityPresentValueEntityByPeriod(@Param("accountPeriod") String accountPeriod);
}
