package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.LiabilityCashFlowEntity;
import com.xl.alm.app.query.LiabilityCashFlowQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 负债现金流表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface LiabilityCashFlowMapper {

    /**
     * 查询负债现金流列表
     *
     * @param query 负债现金流查询条件
     * @return 负债现金流列表
     */
    List<LiabilityCashFlowEntity> selectLiabilityCashFlowEntityList(LiabilityCashFlowQuery query);

    /**
     * 用id查询负债现金流
     *
     * @param id id
     * @return 负债现金流
     */
    LiabilityCashFlowEntity selectLiabilityCashFlowEntityById(Long id);

    /**
     * 根据条件查询负债现金流
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @param isShortTerm 是否中短期险种
     * @param actuarialCode 精算代码
     * @return 负债现金流
     */
    LiabilityCashFlowEntity selectLiabilityCashFlowEntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("cashFlowType") String cashFlowType,
            @Param("bpType") String bpType,
            @Param("durationType") String durationType,
            @Param("designType") String designType,
            @Param("isShortTerm") String isShortTerm,
            @Param("actuarialCode") String actuarialCode);

    /**
     * 批量插入负债现金流数据
     *
     * @param entityList 负债现金流列表
     * @return 影响行数
     */
    int batchInsertLiabilityCashFlowEntity(List<LiabilityCashFlowEntity> entityList);

    /**
     * 更新负债现金流数据
     *
     * @param entity 负债现金流
     * @return 结果
     */
    int updateLiabilityCashFlowEntity(LiabilityCashFlowEntity entity);

    /**
     * 删除指定账期的负债现金流数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    int deleteLiabilityCashFlowEntityByPeriod(@Param("accountPeriod") String accountPeriod);

    /**
     * 删除指定id的负债现金流数据
     *
     * @param id id
     * @return 影响行数
     */
    int deleteLiabilityCashFlowEntityById(@Param("id") Long id);
    
    /**
     * 批量删除负债现金流
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteLiabilityCashFlowEntityByIds(Long[] ids);
}
