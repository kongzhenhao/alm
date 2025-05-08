package com.xl.alm.app.service;

import com.xl.alm.app.dto.DiscountCurveDTO;
import com.xl.alm.app.query.DiscountCurveQuery;

import java.util.List;

/**
 * 折现曲线表 Service 接口
 *
 * @author AI Assistant
 */
public interface DiscountCurveService {

    /**
     * 查询折现曲线列表
     *
     * @param discountCurveQuery 折现曲线查询条件
     * @return 折现曲线列表
     */
    List<DiscountCurveDTO> selectDiscountCurveDtoList(DiscountCurveQuery discountCurveQuery);

    /**
     * 用id查询折现曲线
     *
     * @param id id
     * @return 折现曲线
     */
    DiscountCurveDTO selectDiscountCurveDtoById(Long id);

    /**
     * 根据账期、曲线类型、基点类型和久期类型查询折现曲线
     *
     * @param accountPeriod 账期
     * @param curveType 曲线类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @return 折现曲线
     */
    DiscountCurveDTO selectDiscountCurveDtoByCondition(
            String accountPeriod,
            String curveType,
            String bpType,
            String durationType);

    /**
     * 新增折现曲线数据
     *
     * @param dto 折现曲线
     * @return 结果
     */
    int insertDiscountCurveDto(DiscountCurveDTO dto);

    /**
     * 批量插入折现曲线数据
     *
     * @param discountCurveDtoList 折现曲线列表
     * @return 影响行数
     */
    int batchInsertDiscountCurveDto(List<DiscountCurveDTO> discountCurveDtoList);

    /**
     * 更新折现曲线数据
     *
     * @param dto 折现曲线
     * @return 结果
     */
    int updateDiscountCurveDto(DiscountCurveDTO dto);

    /**
     * 删除指定账期的折现曲线数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    int deleteDiscountCurveDtoByPeriod(String accountPeriod);

    /**
     * 删除指定id的折现曲线数据
     *
     * @param id id
     * @return 影响行数
     */
    int deleteDiscountCurveDtoById(Long id);
    
    /**
     * 批量删除折现曲线数据
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteDiscountCurveDtoByIds(Long[] ids);

    /**
     * 导入折现曲线
     *
     * @param dtoList       折现曲线数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username      操作用户
     * @return 结果
     */
    String importDiscountCurveDto(List<DiscountCurveDTO> dtoList, Boolean updateSupport, String username);
}
