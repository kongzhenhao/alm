package com.xl.alm.job.cost.mapper;

import com.xl.alm.job.cost.entity.StatutoryReserveDetailEntity;
import com.xl.alm.job.cost.query.StatutoryReserveDetailQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 法定准备金明细表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface StatutoryReserveDetailMapper {

    /**
     * 查询法定准备金明细列表
     *
     * @param query 查询条件
     * @return 法定准备金明细集合
     */
    List<StatutoryReserveDetailEntity> selectStatutoryReserveDetailList(StatutoryReserveDetailQuery query);
}
