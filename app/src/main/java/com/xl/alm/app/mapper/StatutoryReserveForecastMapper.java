package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.StatutoryReserveForecastEntity;
import com.xl.alm.app.query.StatutoryReserveForecastQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 法定准备金预测表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface StatutoryReserveForecastMapper {
    /**
     * 查询法定准备金预测列表
     *
     * @param query 法定准备金预测查询条件
     * @return 法定准备金预测列表
     */
    List<StatutoryReserveForecastEntity> selectStatutoryReserveForecastList(StatutoryReserveForecastQuery query);

    /**
     * 根据ID查询法定准备金预测
     *
     * @param id 法定准备金预测ID
     * @return 法定准备金预测
     */
    StatutoryReserveForecastEntity selectStatutoryReserveForecastById(@Param("id") Long id);

    /**
     * 根据业务类型、账期和精算代码查询法定准备金预测
     *
     * @param businessType 业务类型
     * @param accountingPeriod 账期
     * @param actuarialCode 精算代码
     * @return 法定准备金预测
     */
    StatutoryReserveForecastEntity selectStatutoryReserveForecastByCode(
            @Param("businessType") String businessType,
            @Param("accountingPeriod") String accountingPeriod,
            @Param("actuarialCode") String actuarialCode);

    /**
     * 新增法定准备金预测
     *
     * @param statutoryReserveForecast 法定准备金预测
     * @return 结果
     */
    int insertStatutoryReserveForecast(StatutoryReserveForecastEntity statutoryReserveForecast);

    /**
     * 修改法定准备金预测
     *
     * @param statutoryReserveForecast 法定准备金预测
     * @return 结果
     */
    int updateStatutoryReserveForecast(StatutoryReserveForecastEntity statutoryReserveForecast);

    /**
     * 删除法定准备金预测
     *
     * @param id 法定准备金预测ID
     * @return 结果
     */
    int deleteStatutoryReserveForecastById(Long id);

    /**
     * 批量删除法定准备金预测
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteStatutoryReserveForecastByIds(Long[] ids);

    /**
     * 批量插入法定准备金预测数据
     *
     * @param statutoryReserveForecastList 法定准备金预测列表
     * @return 影响行数
     */
    int batchInsertStatutoryReserveForecast(List<StatutoryReserveForecastEntity> statutoryReserveForecastList);
}
