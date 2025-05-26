package com.xl.alm.app.service;

import com.xl.alm.app.dto.RiskItemAmountDTO;
import com.xl.alm.app.query.RiskItemAmountQuery;

import java.util.List;

/**
 * 风险项目金额表 服务层
 *
 * @author AI Assistant
 */
public interface RiskItemAmountService {
    /**
     * 查询风险项目金额列表
     *
     * @param query 查询条件
     * @return 风险项目金额集合
     */
    List<RiskItemAmountDTO> selectRiskItemAmountDtoList(RiskItemAmountQuery query);

    /**
     * 查询风险项目金额详情
     *
     * @param id 主键ID
     * @return 风险项目金额
     */
    RiskItemAmountDTO selectRiskItemAmountDtoById(Long id);

    /**
     * 新增风险项目金额
     *
     * @param dto 风险项目金额
     * @return 结果
     */
    int insertRiskItemAmountDto(RiskItemAmountDTO dto);

    /**
     * 批量新增风险项目金额
     *
     * @param dtoList 风险项目金额列表
     * @return 结果
     */
    int batchInsertRiskItemAmountDto(List<RiskItemAmountDTO> dtoList);

    /**
     * 修改风险项目金额
     *
     * @param dto 风险项目金额
     * @return 结果
     */
    int updateRiskItemAmountDto(RiskItemAmountDTO dto);

    /**
     * 删除风险项目金额
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteRiskItemAmountDtoById(Long id);

    /**
     * 批量删除风险项目金额
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteRiskItemAmountDtoByIds(Long[] ids);

    /**
     * 根据账期删除风险项目金额
     *
     * @param accountingPeriod 账期
     * @return 结果
     */
    int deleteRiskItemAmountDtoByPeriod(String accountingPeriod);

    /**
     * 根据条件删除风险项目金额
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @return 结果
     */
    int deleteRiskItemAmountDtoByCondition(String accountingPeriod, String itemCode);

    /**
     * 导入风险项目金额数据
     *
     * @param dtoList 风险项目金额数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    String importRiskItemAmountDto(List<RiskItemAmountDTO> dtoList, Boolean updateSupport, String username);
}
