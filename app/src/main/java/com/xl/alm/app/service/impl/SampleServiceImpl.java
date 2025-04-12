package com.xl.alm.app.service.impl;

import com.xl.alm.app.entity.SampleEntity;
import com.xl.alm.app.mapper.SampleMapper;
import com.xl.alm.app.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 折现因子表 Service 实现类
 *
 * @author AI Assistant
 */
@Service
public class SampleServiceImpl implements SampleService {

    @Autowired
    private SampleMapper sampleMapper;

    /**
     * 查询折现因子列表
     *
     * @param discountFactor 折现因子查询条件
     * @return 折现因子列表
     */
    @Override
    public List<SampleEntity> selectSampleEntityList(SampleEntity discountFactor) {
        return sampleMapper.selectSampleEntityList(discountFactor);
    }

    /**
     * 用id查询折现因子列表
     *
     * @param id id
     * @return 折现因子
     */
    @Override
    public SampleEntity selectSampleEntityById(Long id) {
        return sampleMapper.selectSampleEntityById(id);
    }

    /**
     * 根据账期、因子类型和久期类型查询折现因子
     *
     * @param accountPeriod 账期
     * @param factorType    因子类型
     * @param durationType  久期类型
     * @param designType    设计类型
     * @param isShortTerm   是否中短期险种
     * @return 折现因子
     */
    @Override
    public SampleEntity selectSampleEntityByCondition(
            String accountPeriod,
            String factorType,
            String durationType,
            String designType,
            String isShortTerm) {
        return sampleMapper.selectSampleEntityByCondition(
                accountPeriod,
                factorType,
                durationType,
                designType,
                isShortTerm);
    }

    /**
     * 批量插入折现因子数据
     *
     * @param discountFactorList 折现因子列表
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertSampleEntity(List<SampleEntity> discountFactorList) {
        return sampleMapper.batchInsertSampleEntity(discountFactorList);
    }

    /**
     * 删除指定账期的折现因子数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSampleEntityByPeriod(String accountPeriod) {
        return sampleMapper.deleteSampleEntityByPeriod(accountPeriod);
    }

    /**
     * 删除指定id的折现因子数据
     *
     * @param id id
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSampleEntityById(Long id) {
        return sampleMapper.deleteSampleEntityById(id);
    }
}
