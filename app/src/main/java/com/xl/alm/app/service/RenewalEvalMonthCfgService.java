package com.xl.alm.app.service;

import com.xl.alm.app.dto.RenewalEvalMonthCfgDTO;
import com.xl.alm.app.query.RenewalEvalMonthCfgQuery;

import java.util.List;

/**
 * 续保率评估月份配置表 Service 接口
 *
 * @author AI Assistant
 */
public interface RenewalEvalMonthCfgService {

    /**
     * 查询续保率评估月份配置列表
     *
     * @param query 续保率评估月份配置查询条件
     * @return 续保率评估月份配置列表
     */
    List<RenewalEvalMonthCfgDTO> selectRenewalEvalMonthCfgDtoList(RenewalEvalMonthCfgQuery query);

    /**
     * 用id查询续保率评估月份配置
     *
     * @param id id
     * @return 续保率评估月份配置
     */
    RenewalEvalMonthCfgDTO selectRenewalEvalMonthCfgDtoById(Long id);

    /**
     * 根据账期和当季第几月查询续保率评估月份配置
     *
     * @param accountingPeriod 账期
     * @param monthSeqInCurrQuarter 当季第几月
     * @return 续保率评估月份配置
     */
    RenewalEvalMonthCfgDTO selectRenewalEvalMonthCfgDtoByCondition(
            String accountingPeriod,
            Integer monthSeqInCurrQuarter);

    /**
     * 新增续保率评估月份配置
     *
     * @param dto 续保率评估月份配置
     * @return 结果
     */
    int insertRenewalEvalMonthCfgDto(RenewalEvalMonthCfgDTO dto);

    /**
     * 批量新增续保率评估月份配置
     *
     * @param dtoList 续保率评估月份配置列表
     * @return 结果
     */
    int batchInsertRenewalEvalMonthCfgDto(List<RenewalEvalMonthCfgDTO> dtoList);

    /**
     * 修改续保率评估月份配置
     *
     * @param dto 续保率评估月份配置
     * @return 结果
     */
    int updateRenewalEvalMonthCfgDto(RenewalEvalMonthCfgDTO dto);

    /**
     * 批量删除续保率评估月份配置
     *
     * @param ids 需要删除的续保率评估月份配置主键集合
     * @return 结果
     */
    int deleteRenewalEvalMonthCfgDtoByIds(Long[] ids);

    /**
     * 删除续保率评估月份配置信息
     *
     * @param id 续保率评估月份配置主键
     * @return 结果
     */
    int deleteRenewalEvalMonthCfgDtoById(Long id);

    /**
     * 导入续保率评估月份配置
     *
     * @param dtoList 续保率评估月份配置数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    String importRenewalEvalMonthCfgDto(List<RenewalEvalMonthCfgDTO> dtoList, Boolean updateSupport, String username);
}
