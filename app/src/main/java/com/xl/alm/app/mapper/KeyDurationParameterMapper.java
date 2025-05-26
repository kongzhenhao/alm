package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.KeyDurationParameterEntity;
import com.xl.alm.app.query.KeyDurationParameterQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 关键久期参数表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface KeyDurationParameterMapper {

    /**
     * 查询关键久期参数列表
     *
     * @param query 关键久期参数查询条件
     * @return 关键久期参数列表
     */
    List<KeyDurationParameterEntity> selectKeyDurationParameterEntityList(KeyDurationParameterQuery query);

    /**
     * 根据ID查询关键久期参数
     *
     * @param id ID
     * @return 关键久期参数
     */
    KeyDurationParameterEntity selectKeyDurationParameterEntityById(Long id);

    /**
     * 根据账期和关键期限点查询关键久期参数
     *
     * @param accountPeriod 账期
     * @param keyDuration 关键期限点
     * @return 关键久期参数
     */
    KeyDurationParameterEntity selectKeyDurationParameterEntityByCondition(@Param("accountPeriod") String accountPeriod,
                                                                          @Param("keyDuration") String keyDuration);

    /**
     * 新增关键久期参数
     *
     * @param entity 关键久期参数
     * @return 结果
     */
    int insertKeyDurationParameterEntity(KeyDurationParameterEntity entity);

    /**
     * 批量新增关键久期参数
     *
     * @param entityList 关键久期参数列表
     * @return 结果
     */
    int batchInsertKeyDurationParameterEntity(List<KeyDurationParameterEntity> entityList);

    /**
     * 修改关键久期参数
     *
     * @param entity 关键久期参数
     * @return 结果
     */
    int updateKeyDurationParameterEntity(KeyDurationParameterEntity entity);

    /**
     * 删除关键久期参数
     *
     * @param id ID
     * @return 结果
     */
    int deleteKeyDurationParameterEntityById(Long id);

    /**
     * 批量删除关键久期参数
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteKeyDurationParameterEntityByIds(Long[] ids);

    /**
     * 根据账期删除关键久期参数
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteKeyDurationParameterEntityByAccountPeriod(String accountPeriod);

    /**
     * 根据ID查询关键久期参数（包含参数值集）
     *
     * @param id ID
     * @return 关键久期参数
     */
    KeyDurationParameterEntity selectKeyDurationParameterEntityWithValSetById(Long id);
}
