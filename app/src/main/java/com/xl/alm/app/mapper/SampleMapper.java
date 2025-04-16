package com.xl.alm.app.mapper;

import com.xl.alm.app.dto.SampleDTO;
import com.xl.alm.app.entity.SampleEntity;
import com.xl.alm.app.query.SampleQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 折现因子表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface SampleMapper {

    /**
     * 查询折现因子列表
     *
     * @param sampleQuery 折现因子查询条件
     * @return 折现因子列表
     */
    List<SampleEntity> selectSampleEntityList(SampleQuery sampleQuery);

    /**
     * 用id查询折现因子列表
     *
     * @param id id
     * @return 折现因子
     */
    SampleEntity selectSampleEntityById(Long id);

    /**
     * 根据账期、因子类型和久期类型查询折现因子
     *
     * @param accountPeriod 账期
     * @param factorType 因子类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @param isShortTerm 是否中短期险种
     * @return 折现因子
     */
    SampleEntity selectSampleEntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("factorType") String factorType,
            @Param("durationType") String durationType,
            @Param("designType") String designType,
            @Param("isShortTerm") String isShortTerm);

    /**
     * 批量插入折现因子数据
     *
     * @param entityList 折现因子列表
     * @return 影响行数
     */
    int batchInsertSampleEntity(List<SampleEntity> entityList);

    /**
     * 更新折现因子数据
     *
     * @param entity 折现因子
     * @return 结果
     */
    int updateSampleEntity(SampleEntity entity);

    /**
     * 删除指定账期的折现因子数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    int deleteSampleEntityByPeriod(@Param("accountPeriod") String accountPeriod);

    /**
     * 删除指定id的折现因子数据
     *
     * @param id id
     * @return 影响行数
     */
    int deleteSampleEntityById(@Param("id") Long id);
}