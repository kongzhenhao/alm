package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.AccountingReserveDetailEntity;
import com.xl.alm.app.query.AccountingReserveDetailQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
     * @param query 会计准备金明细查询条件
     * @return 会计准备金明细列表
     */
    List<AccountingReserveDetailEntity> selectAccountingReserveDetailList(AccountingReserveDetailQuery query);

    /**
     * 根据ID查询会计准备金明细
     *
     * @param id 会计准备金明细ID
     * @return 会计准备金明细
     */
    AccountingReserveDetailEntity selectAccountingReserveDetailById(@Param("id") Long id);

    /**
     * 根据精算代码和账期查询会计准备金明细
     *
     * @param actuarialCode 精算代码
     * @param accountingPeriod 账期
     * @return 会计准备金明细
     */
    AccountingReserveDetailEntity selectAccountingReserveDetailByCode(@Param("actuarialCode") String actuarialCode, 
                                                       @Param("accountingPeriod") String accountingPeriod);

    /**
     * 新增会计准备金明细
     *
     * @param accountingReserveDetail 会计准备金明细
     * @return 结果
     */
    int insertAccountingReserveDetail(AccountingReserveDetailEntity accountingReserveDetail);

    /**
     * 修改会计准备金明细
     *
     * @param accountingReserveDetail 会计准备金明细
     * @return 结果
     */
    int updateAccountingReserveDetail(AccountingReserveDetailEntity accountingReserveDetail);

    /**
     * 删除会计准备金明细
     *
     * @param id 会计准备金明细ID
     * @return 结果
     */
    int deleteAccountingReserveDetailById(Long id);

    /**
     * 批量删除会计准备金明细
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteAccountingReserveDetailByIds(Long[] ids);

    /**
     * 批量插入会计准备金明细数据
     *
     * @param accountingReserveDetailList 会计准备金明细列表
     * @return 影响行数
     */
    int batchInsertAccountingReserveDetail(List<AccountingReserveDetailEntity> accountingReserveDetailList);
}
