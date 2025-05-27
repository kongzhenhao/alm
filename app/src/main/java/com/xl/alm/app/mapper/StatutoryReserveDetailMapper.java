package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.StatutoryReserveDetailEntity;
import com.xl.alm.app.query.StatutoryReserveDetailQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
     * @param query 法定准备金明细查询条件
     * @return 法定准备金明细列表
     */
    List<StatutoryReserveDetailEntity> selectStatutoryReserveDetailList(StatutoryReserveDetailQuery query);

    /**
     * 根据ID查询法定准备金明细
     *
     * @param id 法定准备金明细ID
     * @return 法定准备金明细
     */
    StatutoryReserveDetailEntity selectStatutoryReserveDetailById(@Param("id") Long id);

    /**
     * 根据精算代码和账期查询法定准备金明细
     *
     * @param actuarialCode 精算代码
     * @param accountingPeriod 账期
     * @return 法定准备金明细
     */
    StatutoryReserveDetailEntity selectStatutoryReserveDetailByCode(@Param("actuarialCode") String actuarialCode, 
                                                       @Param("accountingPeriod") String accountingPeriod);

    /**
     * 新增法定准备金明细
     *
     * @param statutoryReserveDetail 法定准备金明细
     * @return 结果
     */
    int insertStatutoryReserveDetail(StatutoryReserveDetailEntity statutoryReserveDetail);

    /**
     * 修改法定准备金明细
     *
     * @param statutoryReserveDetail 法定准备金明细
     * @return 结果
     */
    int updateStatutoryReserveDetail(StatutoryReserveDetailEntity statutoryReserveDetail);

    /**
     * 删除法定准备金明细
     *
     * @param id 法定准备金明细ID
     * @return 结果
     */
    int deleteStatutoryReserveDetailById(Long id);

    /**
     * 批量删除法定准备金明细
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteStatutoryReserveDetailByIds(Long[] ids);

    /**
     * 批量插入法定准备金明细数据
     *
     * @param statutoryReserveDetailList 法定准备金明细列表
     * @return 影响行数
     */
    int batchInsertStatutoryReserveDetail(List<StatutoryReserveDetailEntity> statutoryReserveDetailList);
}
