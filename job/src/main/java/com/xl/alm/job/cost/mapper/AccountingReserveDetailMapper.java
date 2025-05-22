package com.xl.alm.job.cost.mapper;

import com.xl.alm.job.cost.entity.AccountingReserveDetailEntity;
import com.xl.alm.job.cost.query.AccountingReserveDetailQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 会计准备金明细表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface AccountingReserveDetailMapper {

    /**
     * 查询会计准备金明细列表
     *
     * @param query 查询条件
     * @return 会计准备金明细集合
     */
    List<AccountingReserveDetailEntity> selectAccountingReserveDetailList(AccountingReserveDetailQuery query);
}
