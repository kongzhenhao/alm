package com.xl.alm.app.mapper;

import com.xl.alm.app.dto.MarginalCapitalDTO;
import com.xl.alm.app.entity.MarginalCapitalEntity;
import com.xl.alm.app.query.MarginalCapitalQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 边际最低资本贡献率表 Mapper接口
 *
 * @author AI Assistant
 */
public interface MarginalCapitalMapper {
    /**
     * 查询边际最低资本贡献率表列表（关联项目定义表）
     *
     * @param query 查询条件
     * @return 边际最低资本贡献率表DTO集合
     */
    List<MarginalCapitalDTO> selectMarginalCapitalDtoList(MarginalCapitalQuery query);

    /**
     * 查询边际最低资本贡献率表列表（原始查询）
     *
     * @param query 查询条件
     * @return 边际最低资本贡献率表集合
     */
    List<MarginalCapitalEntity> selectMarginalCapitalEntityList(MarginalCapitalQuery query);

    /**
     * 查询边际最低资本贡献率表详情
     *
     * @param id 主键ID
     * @return 边际最低资本贡献率表
     */
    MarginalCapitalEntity selectMarginalCapitalEntityById(Long id);

    /**
     * 根据账期和项目编码查询边际最低资本贡献率表详情
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @return 边际最低资本贡献率表
     */
    MarginalCapitalEntity selectMarginalCapitalEntityByUniqueKey(@Param("accountingPeriod") String accountingPeriod, 
                                                                 @Param("itemCode") String itemCode);

    /**
     * 根据账期和项目编码查询有效的边际最低资本贡献率表详情（用于导入检查）
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @return 边际最低资本贡献率表
     */
    MarginalCapitalEntity selectValidMarginalCapitalEntityByUniqueKey(@Param("accountingPeriod") String accountingPeriod, 
                                                                      @Param("itemCode") String itemCode);

    /**
     * 新增边际最低资本贡献率表
     *
     * @param entity 边际最低资本贡献率表
     * @return 结果
     */
    int insertMarginalCapitalEntity(MarginalCapitalEntity entity);

    /**
     * 批量新增边际最低资本贡献率表
     *
     * @param entityList 边际最低资本贡献率表列表
     * @return 结果
     */
    int batchInsertMarginalCapitalEntity(List<MarginalCapitalEntity> entityList);

    /**
     * 修改边际最低资本贡献率表
     *
     * @param entity 边际最低资本贡献率表
     * @return 结果
     */
    int updateMarginalCapitalEntity(MarginalCapitalEntity entity);

    /**
     * 删除边际最低资本贡献率表
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteMarginalCapitalEntityById(Long id);

    /**
     * 批量删除边际最低资本贡献率表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteMarginalCapitalEntityByIds(Long[] ids);

    /**
     * 物理删除边际最低资本贡献率表
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @return 结果
     */
    int physicalDeleteByUniqueKey(@Param("accountingPeriod") String accountingPeriod, 
                                  @Param("itemCode") String itemCode);
}
