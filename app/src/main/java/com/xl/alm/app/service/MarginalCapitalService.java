package com.xl.alm.app.service;

import com.xl.alm.app.dto.MarginalCapitalDTO;
import com.xl.alm.app.query.MarginalCapitalQuery;

import java.util.List;

/**
 * 边际最低资本贡献率表 Service接口
 *
 * @author AI Assistant
 */
public interface MarginalCapitalService {
    /**
     * 查询边际最低资本贡献率表列表
     *
     * @param query 查询条件
     * @return 边际最低资本贡献率表集合
     */
    List<MarginalCapitalDTO> selectMarginalCapitalDtoList(MarginalCapitalQuery query);

    /**
     * 查询边际最低资本贡献率表详情
     *
     * @param id 主键ID
     * @return 边际最低资本贡献率表
     */
    MarginalCapitalDTO selectMarginalCapitalDtoById(Long id);

    /**
     * 根据账期和项目编码查询边际最低资本贡献率表详情
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @return 边际最低资本贡献率表
     */
    MarginalCapitalDTO selectMarginalCapitalDtoByUniqueKey(String accountingPeriod, String itemCode);

    /**
     * 根据账期和项目编码查询有效的边际最低资本贡献率表详情（用于导入检查）
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @return 边际最低资本贡献率表
     */
    MarginalCapitalDTO selectValidMarginalCapitalDtoByUniqueKey(String accountingPeriod, String itemCode);

    /**
     * 新增边际最低资本贡献率表
     *
     * @param dto 边际最低资本贡献率表
     * @return 结果
     */
    int insertMarginalCapitalDto(MarginalCapitalDTO dto);

    /**
     * 批量新增边际最低资本贡献率表
     *
     * @param dtoList 边际最低资本贡献率表列表
     * @return 结果
     */
    int batchInsertMarginalCapitalDto(List<MarginalCapitalDTO> dtoList);

    /**
     * 修改边际最低资本贡献率表
     *
     * @param dto 边际最低资本贡献率表
     * @return 结果
     */
    int updateMarginalCapitalDto(MarginalCapitalDTO dto);

    /**
     * 删除边际最低资本贡献率表
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteMarginalCapitalDtoById(Long id);

    /**
     * 批量删除边际最低资本贡献率表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteMarginalCapitalDtoByIds(Long[] ids);

    /**
     * 导入边际最低资本贡献率表
     *
     * @param dtoList 边际最低资本贡献率表数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    String importMarginalCapitalDto(List<MarginalCapitalDTO> dtoList, Boolean updateSupport, String username);
}
