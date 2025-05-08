package com.xl.alm.app.service;

import com.xl.alm.app.dto.SubAccountLiabilityDurationDTO;
import com.xl.alm.app.query.SubAccountLiabilityDurationQuery;

import java.util.List;

/**
 * 分账户负债久期汇总表 Service 接口
 *
 * @author AI Assistant
 */
public interface SubAccountLiabilityDurationService {

    /**
     * 查询分账户负债久期汇总列表
     *
     * @param query 分账户负债久期汇总查询条件
     * @return 分账户负债久期汇总列表
     */
    List<SubAccountLiabilityDurationDTO> selectSubAccountLiabilityDurationDtoList(SubAccountLiabilityDurationQuery query);

    /**
     * 用id查询分账户负债久期汇总
     *
     * @param id id
     * @return 分账户负债久期汇总
     */
    SubAccountLiabilityDurationDTO selectSubAccountLiabilityDurationDtoById(Long id);

    /**
     * 根据条件查询分账户负债久期汇总
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @return 分账户负债久期汇总
     */
    SubAccountLiabilityDurationDTO selectSubAccountLiabilityDurationDtoByCondition(
            String accountPeriod,
            String cashFlowType,
            String durationType,
            String designType);

    /**
     * 新增分账户负债久期汇总
     *
     * @param dto 分账户负债久期汇总
     * @return 结果
     */
    int insertSubAccountLiabilityDurationDto(SubAccountLiabilityDurationDTO dto);

    /**
     * 批量新增分账户负债久期汇总
     *
     * @param dtoList 分账户负债久期汇总列表
     * @return 结果
     */
    int batchInsertSubAccountLiabilityDurationDto(List<SubAccountLiabilityDurationDTO> dtoList);

    /**
     * 修改分账户负债久期汇总
     *
     * @param dto 分账户负债久期汇总
     * @return 结果
     */
    int updateSubAccountLiabilityDurationDto(SubAccountLiabilityDurationDTO dto);

    /**
     * 批量删除分账户负债久期汇总
     *
     * @param ids 需要删除的分账户负债久期汇总主键集合
     * @return 结果
     */
    int deleteSubAccountLiabilityDurationDtoByIds(Long[] ids);

    /**
     * 删除分账户负债久期汇总信息
     *
     * @param id 分账户负债久期汇总主键
     * @return 结果
     */
    int deleteSubAccountLiabilityDurationDtoById(Long id);

    /**
     * 删除指定账期的分账户负债久期汇总
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteSubAccountLiabilityDurationDtoByPeriod(String accountPeriod);

    /**
     * 导入分账户负债久期汇总数据
     *
     * @param dtoList 分账户负债久期汇总数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param operName 操作用户
     * @return 结果
     */
    String importSubAccountLiabilityDurationDto(List<SubAccountLiabilityDurationDTO> dtoList, boolean updateSupport, String operName) throws Exception;
}
