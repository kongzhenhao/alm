package com.xl.alm.app.service;

import com.xl.alm.app.dto.DiscountFactorDTO;
import com.xl.alm.app.query.DiscountFactorQuery;

import java.util.List;

/**
 * 折现因子表 Service 接口
 *
 * @author AI Assistant
 */
public interface DiscountFactorService {

    /**
     * 查询折现因子列表
     *
     * @param discountFactorQuery 折现因子查询条件
     * @return 折现因子列表
     */
    List<DiscountFactorDTO> selectDiscountFactorDtoList(DiscountFactorQuery discountFactorQuery);

    /**
     * 用id查询折现因子列表
     *
     * @param id id
     * @return 折现因子
     */
    DiscountFactorDTO selectDiscountFactorDtoById(Long id);

    /**
     * 根据账期、因子类型、基点类型和久期类型查询折现因子
     *
     * @param accountPeriod 账期
     * @param factorType 因子类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @return 折现因子
     */
    DiscountFactorDTO selectDiscountFactorDtoByCondition(
            String accountPeriod,
            String factorType,
            String bpType,
            String durationType);

    /**
     * 新增折现因子
     *
     * @param discountFactorDto 折现因子
     * @return 结果
     */
    int insertDiscountFactorDto(DiscountFactorDTO discountFactorDto);

    /**
     * 批量插入折现因子数据
     *
     * @param discountFactorDtoList 折现因子列表
     * @return 影响行数
     */
    int batchInsertDiscountFactorDto(List<DiscountFactorDTO> discountFactorDtoList);

    /**
     * 修改折现因子
     *
     * @param discountFactorDto 折现因子
     * @return 结果
     */
    int updateDiscountFactorDto(DiscountFactorDTO discountFactorDto);

    /**
     * 批量删除折现因子
     *
     * @param ids 需要删除的折现因子主键集合
     * @return 结果
     */
    int deleteDiscountFactorDtoByIds(Long[] ids);

    /**
     * 删除折现因子信息
     *
     * @param id 折现因子主键
     * @return 结果
     */
    int deleteDiscountFactorDtoById(Long id);

    /**
     * 删除指定账期的折现因子数据
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteDiscountFactorDtoByPeriod(String accountPeriod);

    /**
     * 导入折现因子
     *
     * @param dtoList 折现因子数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    String importDiscountFactorDto(List<DiscountFactorDTO> dtoList, Boolean updateSupport, String username);
}
