package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.RenewalEvalMonthCfgEntity;
import com.xl.alm.app.query.RenewalEvalMonthCfgQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 续保率评估月份配置表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface RenewalEvalMonthCfgMapper {

    /**
     * 查询续保率评估月份配置列表
     *
     * @param query 续保率评估月份配置查询条件
     * @return 续保率评估月份配置列表
     */
    List<RenewalEvalMonthCfgEntity> selectRenewalEvalMonthCfgEntityList(RenewalEvalMonthCfgQuery query);

    /**
     * 用id查询续保率评估月份配置
     *
     * @param id id
     * @return 续保率评估月份配置
     */
    RenewalEvalMonthCfgEntity selectRenewalEvalMonthCfgEntityById(Long id);

    /**
     * 根据账期和当季第几月查询续保率评估月份配置
     *
     * @param accountingPeriod 账期
     * @param monthSeqInCurrQuarter 当季第几月
     * @return 续保率评估月份配置
     */
    RenewalEvalMonthCfgEntity selectRenewalEvalMonthCfgEntityByCondition(
            @Param("accountingPeriod") String accountingPeriod,
            @Param("monthSeqInCurrQuarter") Integer monthSeqInCurrQuarter);

    /**
     * 新增续保率评估月份配置
     *
     * @param entity 续保率评估月份配置
     * @return 结果
     */
    int insertRenewalEvalMonthCfgEntity(RenewalEvalMonthCfgEntity entity);

    /**
     * 批量新增续保率评估月份配置
     *
     * @param entityList 续保率评估月份配置列表
     * @return 结果
     */
    int batchInsertRenewalEvalMonthCfgEntity(List<RenewalEvalMonthCfgEntity> entityList);

    /**
     * 修改续保率评估月份配置
     *
     * @param entity 续保率评估月份配置
     * @return 结果
     */
    int updateRenewalEvalMonthCfgEntity(RenewalEvalMonthCfgEntity entity);

    /**
     * 删除续保率评估月份配置
     *
     * @param id 续保率评估月份配置主键
     * @return 结果
     */
    int deleteRenewalEvalMonthCfgEntityById(Long id);

    /**
     * 批量删除续保率评估月份配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteRenewalEvalMonthCfgEntityByIds(Long[] ids);
}
