package com.xl.alm.app.mapper;

import com.xl.alm.app.dto.CorrelationCoefDTO;
import com.xl.alm.app.entity.CorrelationCoefEntity;
import com.xl.alm.app.query.CorrelationCoefQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 相关系数表 Mapper接口
 *
 * @author AI Assistant
 */
public interface CorrelationCoefMapper {
    /**
     * 查询相关系数表列表（关联项目定义表）
     *
     * @param query 查询条件
     * @return 相关系数表DTO集合
     */
    List<CorrelationCoefDTO> selectCorrelationCoefDtoList(CorrelationCoefQuery query);

    /**
     * 查询相关系数表列表（原始查询）
     *
     * @param query 查询条件
     * @return 相关系数表集合
     */
    List<CorrelationCoefEntity> selectCorrelationCoefEntityList(CorrelationCoefQuery query);

    /**
     * 查询相关系数表详情
     *
     * @param id 主键ID
     * @return 相关系数表
     */
    CorrelationCoefEntity selectCorrelationCoefEntityById(Long id);

    /**
     * 根据账期和项目编码查询相关系数表详情
     *
     * @param accountingPeriod 账期
     * @param itemCodeX 第一个风险项目编码
     * @param itemCodeY 第二个风险项目编码
     * @return 相关系数表
     */
    CorrelationCoefEntity selectCorrelationCoefEntityByUniqueKey(@Param("accountingPeriod") String accountingPeriod,
                                                                 @Param("itemCodeX") String itemCodeX,
                                                                 @Param("itemCodeY") String itemCodeY);

    /**
     * 根据账期和项目编码查询有效的相关系数表详情（用于导入检查）
     *
     * @param accountingPeriod 账期
     * @param itemCodeX 第一个风险项目编码
     * @param itemCodeY 第二个风险项目编码
     * @return 相关系数表
     */
    CorrelationCoefEntity selectValidCorrelationCoefEntityByUniqueKey(@Param("accountingPeriod") String accountingPeriod,
                                                                      @Param("itemCodeX") String itemCodeX,
                                                                      @Param("itemCodeY") String itemCodeY);

    /**
     * 新增相关系数表
     *
     * @param entity 相关系数表
     * @return 结果
     */
    int insertCorrelationCoefEntity(CorrelationCoefEntity entity);

    /**
     * 批量新增相关系数表
     *
     * @param entityList 相关系数表列表
     * @return 结果
     */
    int batchInsertCorrelationCoefEntity(List<CorrelationCoefEntity> entityList);

    /**
     * 修改相关系数表
     *
     * @param entity 相关系数表
     * @return 结果
     */
    int updateCorrelationCoefEntity(CorrelationCoefEntity entity);

    /**
     * 删除相关系数表
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteCorrelationCoefEntityById(Long id);

    /**
     * 批量删除相关系数表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteCorrelationCoefEntityByIds(Long[] ids);

    /**
     * 物理删除相关系数表
     *
     * @param accountingPeriod 账期
     * @param itemCodeX 第一个风险项目编码
     * @param itemCodeY 第二个风险项目编码
     * @return 结果
     */
    int physicalDeleteByUniqueKey(@Param("accountingPeriod") String accountingPeriod,
                                  @Param("itemCodeX") String itemCodeX,
                                  @Param("itemCodeY") String itemCodeY);
}
