package com.xl.alm.job.cost.mapper;

import com.xl.alm.job.cost.entity.StatutoryReserveForecastEntity;
import com.xl.alm.job.cost.query.StatutoryReserveForecastQuery;
import org.apache.ibatis.annotations.Mapper;

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
     * @param query 查询条件
     * @return 法定准备金预测集合
     */
    List<StatutoryReserveForecastEntity> selectStatutoryReserveForecastList(StatutoryReserveForecastQuery query);
}
