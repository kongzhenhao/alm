package com.xl.alm.app.service;

import com.xl.alm.app.dto.KeyDurationDiscountCurveDTO;
import com.xl.alm.app.query.KeyDurationDiscountCurveQuery;

import java.util.List;

/**
 * 关键久期折现曲线表 Service 接口
 *
 * @author AI Assistant
 */
public interface KeyDurationDiscountCurveService {

    /**
     * 查询关键久期折现曲线列表
     *
     * @param query 关键久期折现曲线查询条件
     * @return 关键久期折现曲线列表
     */
    List<KeyDurationDiscountCurveDTO> selectKeyDurationDiscountCurveDtoList(KeyDurationDiscountCurveQuery query);

    /**
     * 根据ID查询关键久期折现曲线
     *
     * @param id ID
     * @return 关键久期折现曲线
     */
    KeyDurationDiscountCurveDTO selectKeyDurationDiscountCurveDtoById(Long id);

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
    KeyDurationDiscountCurveDTO selectKeyDurationDiscountCurveDtoByCondition(
            String accountPeriod,
            String curveType,
            String keyDuration,
            String stressDirection,
            String durationType);

    /**
     * 新增关键久期折现曲线
     *
     * @param dto 关键久期折现曲线
     * @return 结果
     */
    int insertKeyDurationDiscountCurveDto(KeyDurationDiscountCurveDTO dto);

    /**
     * 批量新增关键久期折现曲线
     *
     * @param dtoList 关键久期折现曲线列表
     * @return 结果
     */
    int batchInsertKeyDurationDiscountCurveDto(List<KeyDurationDiscountCurveDTO> dtoList);

    /**
     * 修改关键久期折现曲线
     *
     * @param dto 关键久期折现曲线
     * @return 结果
     */
    int updateKeyDurationDiscountCurveDto(KeyDurationDiscountCurveDTO dto);

    /**
     * 删除关键久期折现曲线
     *
     * @param id ID
     * @return 结果
     */
    int deleteKeyDurationDiscountCurveDtoById(Long id);

    /**
     * 批量删除关键久期折现曲线
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteKeyDurationDiscountCurveDtoByIds(Long[] ids);

    /**
     * 根据账期删除关键久期折现曲线
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteKeyDurationDiscountCurveDtoByAccountPeriod(String accountPeriod);

    /**
     * 导入关键久期折现曲线数据
     *
     * @param dtoList 关键久期折现曲线数据列表
     * @param updateSupport 是否支持更新，如果已存在，则进行更新
     * @param operName 操作用户
     * @return 结果
     */
    String importKeyDurationDiscountCurveDto(List<KeyDurationDiscountCurveDTO> dtoList, boolean updateSupport, String operName);
}
