package com.xl.alm.app.mapper;

import com.xl.alm.app.dto.MinCapitalByAccountDTO;
import com.xl.alm.app.entity.MinCapitalByAccountEntity;
import com.xl.alm.app.query.MinCapitalByAccountQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 市场及信用最低资本表Mapper接口
 *
 * @author alm
 * @date 2024-01-01
 */
public interface MinCapitalByAccountMapper {

    /**
     * 查询市场及信用最低资本表列表（关联查询，返回DTO）
     *
     * @param query 查询条件
     * @return 市场及信用最低资本表集合
     */
    List<MinCapitalByAccountDTO> selectMinCapitalByAccountDtoList(MinCapitalByAccountQuery query);

    /**
     * 查询市场及信用最低资本表列表（原始查询，返回Entity）
     *
     * @param query 查询条件
     * @return 市场及信用最低资本表集合
     */
    List<MinCapitalByAccountEntity> selectMinCapitalByAccountEntityList(MinCapitalByAccountQuery query);

    /**
     * 查询市场及信用最低资本表详情
     *
     * @param id 市场及信用最低资本表主键
     * @return 市场及信用最低资本表
     */
    MinCapitalByAccountEntity selectMinCapitalByAccountEntityById(Long id);

    /**
     * 根据账期、项目编码和账户编码查询市场及信用最低资本表详情
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @return 市场及信用最低资本表
     */
    MinCapitalByAccountEntity selectMinCapitalByAccountEntityByUniqueKey(@Param("accountingPeriod") String accountingPeriod, 
                                                                         @Param("itemCode") String itemCode,
                                                                         @Param("accountCode") String accountCode);

    /**
     * 根据账期、项目编码和账户编码查询有效的市场及信用最低资本表详情（用于检查）
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @return 市场及信用最低资本表
     */
    MinCapitalByAccountEntity selectValidMinCapitalByAccountEntityByUniqueKey(@Param("accountingPeriod") String accountingPeriod, 
                                                                              @Param("itemCode") String itemCode,
                                                                              @Param("accountCode") String accountCode);

    /**
     * 根据账期、项目编码和账户编码查询任何记录（包括已删除的，用于调试）
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @return 市场及信用最低资本表
     */
    MinCapitalByAccountEntity selectAnyMinCapitalByAccountEntityByUniqueKey(@Param("accountingPeriod") String accountingPeriod, 
                                                                            @Param("itemCode") String itemCode,
                                                                            @Param("accountCode") String accountCode);

    /**
     * 新增市场及信用最低资本表
     *
     * @param minCapitalByAccountEntity 市场及信用最低资本表
     * @return 结果
     */
    int insertMinCapitalByAccountEntity(MinCapitalByAccountEntity minCapitalByAccountEntity);

    /**
     * 批量新增市场及信用最低资本表
     *
     * @param entityList 市场及信用最低资本表列表
     * @return 结果
     */
    int batchInsertMinCapitalByAccountEntity(List<MinCapitalByAccountEntity> entityList);

    /**
     * 修改市场及信用最低资本表
     *
     * @param minCapitalByAccountEntity 市场及信用最低资本表
     * @return 结果
     */
    int updateMinCapitalByAccountEntity(MinCapitalByAccountEntity minCapitalByAccountEntity);

    /**
     * 删除市场及信用最低资本表
     *
     * @param id 市场及信用最低资本表主键
     * @return 结果
     */
    int deleteMinCapitalByAccountEntityById(Long id);

    /**
     * 批量删除市场及信用最低资本表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteMinCapitalByAccountEntityByIds(Long[] ids);

    /**
     * 物理删除市场及信用最低资本表
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
