package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.KeyDurationDiscountCurveEntity;
import com.xl.alm.app.query.KeyDurationDiscountCurveQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 关键久期折现曲线表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface KeyDurationDiscountCurveMapper {

    /**
     * 查询关键久期折现曲线列表
     *
     * @param query 关键久期折现曲线查询条件
     * @return 关键久期折现曲线列表
     */
    List<KeyDurationDiscountCurveEntity> selectKeyDurationDiscountCurveEntityList(KeyDurationDiscountCurveQuery query);

    /**
     * 根据ID查询关键久期折现曲线
     *
     * @param id ID
     * @return 关键久期折现曲线
     */
    KeyDurationDiscountCurveEntity selectKeyDurationDiscountCurveEntityById(Long id);

    /**
     * 根据条件查询关键久期折现曲线
     *
     * @param accountPeriod 账期
     * @param curveType 曲线类型
     * @param keyDuration 关键期限点
     * @param stressDirection 压力方向
     * @param durationType 久期类型
     * @return 关键久期折现曲线
     */
    KeyDurationDiscountCurveEntity selectKeyDurationDiscountCurveEntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("curveType") String curveType,
            @Param("keyDuration") String keyDuration,
            @Param("stressDirection") String stressDirection,
            @Param("durationType") String durationType);

    /**
     * 新增关键久期折现曲线
     *
     * @param entity 关键久期折现曲线
     * @return 结果
     */
    int insertKeyDurationDiscountCurveEntity(KeyDurationDiscountCurveEntity entity);

    /**
     * 批量新增关键久期折现曲线
     *
     * @param entityList 关键久期折现曲线列表
     * @return 结果
     */
    int batchInsertKeyDurationDiscountCurveEntity(List<KeyDurationDiscountCurveEntity> entityList);

    /**
     * 修改关键久期折现曲线
     *
     * @param entity 关键久期折现曲线
     * @return 结果
     */
    int updateKeyDurationDiscountCurveEntity(KeyDurationDiscountCurveEntity entity);

    /**
     * 删除关键久期折现曲线
     *
     * @param id ID
     * @return 结果
     */
    int deleteKeyDurationDiscountCurveEntityById(Long id);

    /**
     * 批量删除关键久期折现曲线
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteKeyDurationDiscountCurveEntityByIds(Long[] ids);

    /**
     * 根据账期删除关键久期折现曲线
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteKeyDurationDiscountCurveEntityByAccountPeriod(String accountPeriod);
}
