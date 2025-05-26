package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.KeyDurationDiscountFactorsEntity;
import com.xl.alm.app.query.KeyDurationDiscountFactorsQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 关键久期折现因子表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface KeyDurationDiscountFactorsMapper {

    /**
     * 查询关键久期折现因子列表
     *
     * @param query 关键久期折现因子查询条件
     * @return 关键久期折现因子列表
     */
    List<KeyDurationDiscountFactorsEntity> selectKeyDurationDiscountFactorsEntityList(KeyDurationDiscountFactorsQuery query);

    /**
     * 根据ID查询关键久期折现因子
     *
     * @param id ID
     * @return 关键久期折现因子
     */
    KeyDurationDiscountFactorsEntity selectKeyDurationDiscountFactorsEntityById(Long id);

    /**
     * 根据条件查询关键久期折现因子
     *
     * @param accountPeriod 账期
     * @param curveType 曲线类型
     * @param keyDuration 关键期限点
     * @param stressDirection 压力方向
     * @param durationType 久期类型
     * @return 关键久期折现因子
     */
    KeyDurationDiscountFactorsEntity selectKeyDurationDiscountFactorsEntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("curveType") String curveType,
            @Param("keyDuration") String keyDuration,
            @Param("stressDirection") String stressDirection,
            @Param("durationType") String durationType);

    /**
     * 新增关键久期折现因子
     *
     * @param entity 关键久期折现因子
     * @return 结果
     */
    int insertKeyDurationDiscountFactorsEntity(KeyDurationDiscountFactorsEntity entity);

    /**
     * 批量新增关键久期折现因子
     *
     * @param entityList 关键久期折现因子列表
     * @return 结果
     */
    int batchInsertKeyDurationDiscountFactorsEntity(List<KeyDurationDiscountFactorsEntity> entityList);

    /**
     * 修改关键久期折现因子
     *
     * @param entity 关键久期折现因子
     * @return 结果
     */
    int updateKeyDurationDiscountFactorsEntity(KeyDurationDiscountFactorsEntity entity);

    /**
     * 删除关键久期折现因子
     *
     * @param id 关键久期折现因子ID
     * @return 结果
     */
    int deleteKeyDurationDiscountFactorsEntityById(Long id);

    /**
     * 批量删除关键久期折现因子
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteKeyDurationDiscountFactorsEntityByIds(Long[] ids);

    /**
     * 删除指定账期的关键久期折现因子
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteKeyDurationDiscountFactorsEntityByPeriod(String accountPeriod);
}
