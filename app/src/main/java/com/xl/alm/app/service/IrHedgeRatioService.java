package com.xl.alm.app.service;

import com.xl.alm.app.dto.IrHedgeRatioDTO;
import com.xl.alm.app.query.IrHedgeRatioQuery;

import java.util.List;

/**
 * 利率风险对冲率表Service接口
 *
 * @author AI Assistant
 */
public interface IrHedgeRatioService {

    /**
     * 查询利率风险对冲率表列表
     *
     * @param query 查询条件
     * @return 利率风险对冲率表集合
     */
    List<IrHedgeRatioDTO> selectIrHedgeRatioDtoList(IrHedgeRatioQuery query);

    /**
     * 查询利率风险对冲率表详情
     *
     * @param id 利率风险对冲率表主键
     * @return 利率风险对冲率表
     */
    IrHedgeRatioDTO selectIrHedgeRatioDtoById(Long id);

    /**
     * 根据唯一键查询利率风险对冲率表详情
     *
     * @param accountingPeriod 账期
     * @param itemName 项目名称
     * @param accountCode 账户编码
     * @return 利率风险对冲率表
     */
    IrHedgeRatioDTO selectIrHedgeRatioDtoByUniqueKey(String accountingPeriod, String itemName, String accountCode);

    /**
     * 新增利率风险对冲率表
     *
     * @param dto 利率风险对冲率表
     * @return 结果
     */
    int insertIrHedgeRatioDto(IrHedgeRatioDTO dto);

    /**
     * 批量新增利率风险对冲率表
     *
     * @param dtoList 利率风险对冲率表列表
     * @return 结果
     */
    int batchInsertIrHedgeRatioDto(List<IrHedgeRatioDTO> dtoList);

    /**
     * 修改利率风险对冲率表
     *
     * @param dto 利率风险对冲率表
     * @return 结果
     */
    int updateIrHedgeRatioDto(IrHedgeRatioDTO dto);

    /**
     * 删除利率风险对冲率表
     *
     * @param id 利率风险对冲率表主键
     * @return 结果
     */
    int deleteIrHedgeRatioDtoById(Long id);

    /**
     * 批量删除利率风险对冲率表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteIrHedgeRatioDtoByIds(Long[] ids);

    /**
     * 根据账期删除利率风险对冲率表
     *
     * @param accountingPeriod 账期
     * @return 结果
     */
    int deleteIrHedgeRatioDtoByPeriod(String accountingPeriod);
}
