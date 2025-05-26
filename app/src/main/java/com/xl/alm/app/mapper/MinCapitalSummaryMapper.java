package com.xl.alm.app.mapper;

import com.xl.alm.app.dto.MinCapitalSummaryDTO;
import com.xl.alm.app.entity.MinCapitalSummaryEntity;
import com.xl.alm.app.query.MinCapitalSummaryQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 最低资本明细汇总表 Mapper接口
 *
 * @author AI Assistant
 */
public interface MinCapitalSummaryMapper {
    /**
     * 查询最低资本明细汇总表列表（关联项目定义表和字典表）
     *
     * @param query 查询条件
     * @return 最低资本明细汇总表DTO集合
     */
    List<MinCapitalSummaryDTO> selectMinCapitalSummaryDtoList(MinCapitalSummaryQuery query);

    /**
     * 查询最低资本明细汇总表列表（原始查询）
     *
     * @param query 查询条件
     * @return 最低资本明细汇总表集合
     */
    List<MinCapitalSummaryEntity> selectMinCapitalSummaryEntityList(MinCapitalSummaryQuery query);

    /**
     * 查询最低资本明细汇总表详情
     *
     * @param id 主键ID
     * @return 最低资本明细汇总表
     */
    MinCapitalSummaryEntity selectMinCapitalSummaryEntityById(Long id);

    /**
     * 根据账期、项目编码和账户编码查询最低资本明细汇总表详情
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @return 最低资本明细汇总表
     */
    MinCapitalSummaryEntity selectMinCapitalSummaryEntityByUniqueKey(@Param("accountingPeriod") String accountingPeriod,
                                                                     @Param("itemCode") String itemCode,
                                                                     @Param("accountCode") String accountCode);

    /**
     * 根据账期、项目编码和账户编码查询有效的最低资本明细汇总表详情（用于检查）
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @return 最低资本明细汇总表
     */
    MinCapitalSummaryEntity selectValidMinCapitalSummaryEntityByUniqueKey(@Param("accountingPeriod") String accountingPeriod,
                                                                          @Param("itemCode") String itemCode,
                                                                          @Param("accountCode") String accountCode);

    /**
     * 根据账期、项目编码和账户编码查询任何记录（包括已删除的，用于调试）
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @return 最低资本明细汇总表
     */
    MinCapitalSummaryEntity selectAnyMinCapitalSummaryEntityByUniqueKey(@Param("accountingPeriod") String accountingPeriod,
                                                                        @Param("itemCode") String itemCode,
                                                                        @Param("accountCode") String accountCode);

    /**
     * 新增最低资本明细汇总表
     *
     * @param entity 最低资本明细汇总表
     * @return 结果
     */
    int insertMinCapitalSummaryEntity(MinCapitalSummaryEntity entity);

    /**
     * 批量新增最低资本明细汇总表
     *
     * @param entityList 最低资本明细汇总表列表
     * @return 结果
     */
    int batchInsertMinCapitalSummaryEntity(List<MinCapitalSummaryEntity> entityList);

    /**
     * 修改最低资本明细汇总表
     *
     * @param entity 最低资本明细汇总表
     * @return 结果
     */
    int updateMinCapitalSummaryEntity(MinCapitalSummaryEntity entity);

    /**
     * 删除最低资本明细汇总表
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteMinCapitalSummaryEntityById(Long id);

    /**
     * 批量删除最低资本明细汇总表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteMinCapitalSummaryEntityByIds(Long[] ids);

    /**
     * 物理删除最低资本明细汇总表
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @return 结果
     */
    int physicalDeleteByUniqueKey(@Param("accountingPeriod") String accountingPeriod,
                                  @Param("itemCode") String itemCode,
                                  @Param("accountCode") String accountCode);
}
