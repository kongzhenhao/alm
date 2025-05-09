package com.xl.alm.app.service;

import com.xl.alm.app.dto.SampleDTO;
import com.xl.alm.app.entity.SampleEntity;
import com.xl.alm.app.query.SampleQuery;

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
     * @param sampleQuery 折现因子查询条件
     * @return 折现因子列表
     */
    List<SampleDTO> selectSampleDtoList(SampleQuery sampleQuery);

    /**
     * 用id查询折现因子列表
     *
     * @param id id
     * @return 折现因子
     */
    SampleDTO selectSampleDtoById(Long id);

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
    SampleDTO selectSampleDtoByCondition(
            String accountPeriod,
            String factorType,
            String durationType,
            String designType,
            String isShortTerm);

    /**
     * 批量插入折现因子数据
     *
     * @param sampleDtoList 折现因子列表
     * @return 影响行数
     */
    int batchInsertSampleDto(List<SampleDTO> sampleDtoList);

    /**
     * 更新折现因子数据
     *
     * @param dto 折现因子
     * @return 结果
     */
    public int updateSampleDto(SampleDTO dto);

    /**
     * 删除指定账期的折现因子数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    int deleteSampleDtoByPeriod(String accountPeriod);

    /**
     * 删除指定id的折现因子数据
     *
     * @param id id
     * @return 影响行数
     */
    int deleteSampleDtoById(Long id);

    /**
     * 导入折现因子
     *
     * @param dtoList       折现因子数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username      操作用户
     * @return 结果
     */
    String importSampleDto(List<SampleDTO> dtoList, Boolean updateSupport, String username);
}
