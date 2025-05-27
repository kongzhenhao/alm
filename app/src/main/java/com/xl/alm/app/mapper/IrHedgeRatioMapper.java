package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.IrHedgeRatioEntity;
import com.xl.alm.app.query.IrHedgeRatioQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 利率风险对冲率表Mapper接口
 *
 * @author AI Assistant
 */
public interface IrHedgeRatioMapper {

    /**
     * 查询利率风险对冲率表列表
     *
     * @param query 查询条件
     * @return 利率风险对冲率表集合
     */
    List<IrHedgeRatioEntity> selectIrHedgeRatioEntityList(IrHedgeRatioQuery query);

    /**
     * 查询利率风险对冲率表详情
     *
     * @param id 利率风险对冲率表主键
     * @return 利率风险对冲率表
     */
    IrHedgeRatioEntity selectIrHedgeRatioEntityById(Long id);

    /**
     * 根据唯一键查询利率风险对冲率表详情
     *
     * @param accountingPeriod 账期
     * @param itemName 项目名称
     * @param accountCode 账户编码
     * @return 利率风险对冲率表
     */
    IrHedgeRatioEntity selectIrHedgeRatioEntityByUniqueKey(@Param("accountingPeriod") String accountingPeriod, 
                                                           @Param("itemName") String itemName, 
                                                           @Param("accountCode") String accountCode);

    /**
     * 新增利率风险对冲率表
     *
     * @param irHedgeRatioEntity 利率风险对冲率表
     * @return 结果
     */
    int insertIrHedgeRatioEntity(IrHedgeRatioEntity irHedgeRatioEntity);

    /**
     * 批量新增利率风险对冲率表
     *
     * @param entityList 利率风险对冲率表列表
     * @return 结果
     */
    int batchInsertIrHedgeRatioEntity(List<IrHedgeRatioEntity> entityList);

    /**
     * 修改利率风险对冲率表
     *
     * @param irHedgeRatioEntity 利率风险对冲率表
     * @return 结果
     */
    int updateIrHedgeRatioEntity(IrHedgeRatioEntity irHedgeRatioEntity);

    /**
     * 删除利率风险对冲率表
     *
     * @param id 利率风险对冲率表主键
     * @return 结果
     */
    int deleteIrHedgeRatioEntityById(Long id);

    /**
     * 批量删除利率风险对冲率表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteIrHedgeRatioEntityByIds(Long[] ids);

    /**
     * 根据账期删除利率风险对冲率表
     *
     * @param accountingPeriod 账期
     * @return 结果
     */
    int deleteIrHedgeRatioEntityByPeriod(String accountingPeriod);
}
