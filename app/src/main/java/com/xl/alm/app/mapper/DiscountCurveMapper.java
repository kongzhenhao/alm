package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.DiscountCurveEntity;
import com.xl.alm.app.query.DiscountCurveQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 折现曲线表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface DiscountCurveMapper {

    /**
     * 查询折现曲线列表
     *
     * @param discountCurveQuery 折现曲线查询条件
     * @return 折现曲线列表
     */
    List<DiscountCurveEntity> selectDiscountCurveEntityList(DiscountCurveQuery discountCurveQuery);

    /**
     * 用id查询折现曲线
     *
     * @param id id
     * @return 折现曲线
     */
    DiscountCurveEntity selectDiscountCurveEntityById(Long id);

    /**
     * 根据账期、曲线类型、基点类型和久期类型查询折现曲线
     *
     * @param accountPeriod 账期
     * @param curveType 曲线类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @return 折现曲线
     */
    DiscountCurveEntity selectDiscountCurveEntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("curveType") String curveType,
            @Param("bpType") String bpType,
            @Param("durationType") String durationType);

    /**
     * 批量插入折现曲线数据
     *
     * @param entityList 折现曲线列表
     * @return 影响行数
     */
    int batchInsertDiscountCurveEntity(List<DiscountCurveEntity> entityList);

    /**
     * 更新折现曲线数据
     *
     * @param entity 折现曲线
     * @return 结果
     */
    int updateDiscountCurveEntity(DiscountCurveEntity entity);

    /**
     * 删除指定账期的折现曲线数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    int deleteDiscountCurveEntityByPeriod(@Param("accountPeriod") String accountPeriod);

    /**
     * 删除指定id的折现曲线数据
     *
     * @param id id
     * @return 影响行数
     */
    int deleteDiscountCurveEntityById(@Param("id") Long id);
    
    /**
     * 批量删除折现曲线数据
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteDiscountCurveEntityByIds(Long[] ids);
}
