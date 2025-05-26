package com.xl.alm.app.service;

import com.xl.alm.app.dto.KeyDurationDiscountFactorsDTO;
import com.xl.alm.app.query.KeyDurationDiscountFactorsQuery;

import java.util.List;

/**
 * 关键久期折现因子表 Service 接口
 *
 * @author AI Assistant
 */
public interface KeyDurationDiscountFactorsService {

    /**
     * 查询关键久期折现因子列表
     *
     * @param query 关键久期折现因子查询条件
     * @return 关键久期折现因子列表
     */
    List<KeyDurationDiscountFactorsDTO> selectKeyDurationDiscountFactorsDtoList(KeyDurationDiscountFactorsQuery query);

    /**
     * 根据ID查询关键久期折现因子
     *
     * @param id ID
     * @return 关键久期折现因子
     */
    KeyDurationDiscountFactorsDTO selectKeyDurationDiscountFactorsDtoById(Long id);

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
    KeyDurationDiscountFactorsDTO selectKeyDurationDiscountFactorsDtoByCondition(
            String accountPeriod,
            String curveType,
            String keyDuration,
            String stressDirection,
            String durationType);

    /**
     * 新增关键久期折现因子
     *
     * @param dto 关键久期折现因子
     * @return 结果
     */
    int insertKeyDurationDiscountFactorsDto(KeyDurationDiscountFactorsDTO dto);

    /**
     * 批量新增关键久期折现因子
     *
     * @param dtoList 关键久期折现因子列表
     * @return 结果
     */
    int batchInsertKeyDurationDiscountFactorsDto(List<KeyDurationDiscountFactorsDTO> dtoList);

    /**
     * 修改关键久期折现因子
     *
     * @param dto 关键久期折现因子
     * @return 结果
     */
    int updateKeyDurationDiscountFactorsDto(KeyDurationDiscountFactorsDTO dto);

    /**
     * 删除关键久期折现因子
     *
     * @param id 关键久期折现因子ID
     * @return 结果
     */
    int deleteKeyDurationDiscountFactorsDtoById(Long id);

    /**
     * 批量删除关键久期折现因子
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteKeyDurationDiscountFactorsDtoByIds(Long[] ids);

    /**
     * 删除指定账期的关键久期折现因子
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteKeyDurationDiscountFactorsDtoByPeriod(String accountPeriod);

    /**
     * 导入关键久期折现因子
     *
     * @param dtoList 关键久期折现因子数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    String importKeyDurationDiscountFactorsDto(List<KeyDurationDiscountFactorsDTO> dtoList, Boolean updateSupport, String username);
}
