package com.xl.alm.app.service;

import com.xl.alm.app.dto.SubAccountLiabilityPresentValueDTO;
import com.xl.alm.app.query.SubAccountLiabilityPresentValueQuery;

import java.util.List;

/**
 * 分账户负债现金流现值汇总表 Service 接口
 *
 * @author AI Assistant
 */
public interface SubAccountLiabilityPresentValueService {

    /**
     * 查询分账户负债现金流现值汇总列表
     *
     * @param query 分账户负债现金流现值汇总查询条件
     * @return 分账户负债现金流现值汇总列表
     */
    List<SubAccountLiabilityPresentValueDTO> selectSubAccountLiabilityPresentValueDtoList(SubAccountLiabilityPresentValueQuery query);

    /**
     * 用id查询分账户负债现金流现值汇总
     *
     * @param id id
     * @return 分账户负债现金流现值汇总
     */
    SubAccountLiabilityPresentValueDTO selectSubAccountLiabilityPresentValueDtoById(Long id);

    /**
     * 根据条件查询分账户负债现金流现值汇总
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @return 分账户负债现金流现值汇总
     */
    SubAccountLiabilityPresentValueDTO selectSubAccountLiabilityPresentValueDtoByCondition(
            String accountPeriod,
            String cashFlowType,
            String bpType,
            String durationType,
            String designType);

    /**
     * 新增分账户负债现金流现值汇总
     *
     * @param dto 分账户负债现金流现值汇总
     * @return 结果
     */
    int insertSubAccountLiabilityPresentValueDto(SubAccountLiabilityPresentValueDTO dto);

    /**
     * 批量新增分账户负债现金流现值汇总
     *
     * @param dtoList 分账户负债现金流现值汇总列表
     * @return 结果
     */
    int batchInsertSubAccountLiabilityPresentValueDto(List<SubAccountLiabilityPresentValueDTO> dtoList);

    /**
     * 修改分账户负债现金流现值汇总
     *
     * @param dto 分账户负债现金流现值汇总
     * @return 结果
     */
    int updateSubAccountLiabilityPresentValueDto(SubAccountLiabilityPresentValueDTO dto);

    /**
     * 批量删除分账户负债现金流现值汇总
     *
     * @param ids 需要删除的分账户负债现金流现值汇总主键集合
     * @return 结果
     */
    int deleteSubAccountLiabilityPresentValueDtoByIds(Long[] ids);

    /**
     * 删除指定账期的分账户负债现金流现值汇总数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    int deleteSubAccountLiabilityPresentValueDtoByPeriod(String accountPeriod);

    /**
     * 导入分账户负债现金流现值汇总数据
     *
     * @param dtoList 分账户负债现金流现值汇总数据列表
     * @param updateSupport 是否支持更新，如果已存在，则进行更新
     * @param operName 操作用户
     * @return 结果
     */
    String importSubAccountLiabilityPresentValueDto(List<SubAccountLiabilityPresentValueDTO> dtoList, boolean updateSupport, String operName);
}
