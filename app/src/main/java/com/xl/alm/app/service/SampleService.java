package com.xl.alm.app.service;

import com.xl.alm.app.entity.SampleEntity;

import java.util.List;

/**
 * 折现因子表 Service 接口
 *
 * @author AI Assistant
 */
public interface SampleService {

    /**
     * 查询折现因子列表
     *
     * @param discountFactor 折现因子查询条件
     * @return 折现因子列表
     */
    List<SampleEntity> selectSampleEntityList(SampleEntity discountFactor);

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
            String accountPeriod,
            String factorType,
            String durationType,
            String designType,
            String isShortTerm);

    /**
     * 批量插入折现因子数据
     *
     * @param discountFactorList 折现因子列表
     * @return 影响行数
     */
    int batchInsertSampleEntity(List<SampleEntity> discountFactorList);

    /**
     * 删除指定账期的折现因子数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    int deleteSampleEntityByPeriod(String accountPeriod);

    /**
     * 删除指定id的折现因子数据
     *
     * @param id id
     * @return 影响行数
     */
    int deleteSampleEntityById(Long id);
}
