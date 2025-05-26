package com.xl.alm.app.service;

import com.xl.alm.app.dto.DeptMincapDetailDTO;
import com.xl.alm.app.query.DeptMincapDetailQuery;

import java.util.List;

/**
 * 分部门最低资本明细表 Service 接口
 *
 * @author AI Assistant
 */
public interface DeptMincapDetailService {

    /**
     * 查询分部门最低资本明细列表
     *
     * @param query 分部门最低资本明细查询条件
     * @return 分部门最低资本明细列表
     */
    List<DeptMincapDetailDTO> selectDeptMincapDetailDtoList(DeptMincapDetailQuery query);

    /**
     * 查询分部门最低资本明细详情
     *
     * @param id 主键ID
     * @return 分部门最低资本明细
     */
    DeptMincapDetailDTO selectDeptMincapDetailDtoById(Long id);

    /**
     * 新增分部门最低资本明细
     *
     * @param dto 分部门最低资本明细
     * @return 结果
     */
    int insertDeptMincapDetailDto(DeptMincapDetailDTO dto);

    /**
     * 批量新增分部门最低资本明细
     *
     * @param dtoList 分部门最低资本明细列表
     * @return 结果
     */
    int batchInsertDeptMincapDetailDto(List<DeptMincapDetailDTO> dtoList);

    /**
     * 修改分部门最低资本明细
     *
     * @param dto 分部门最低资本明细
     * @return 结果
     */
    int updateDeptMincapDetailDto(DeptMincapDetailDTO dto);

    /**
     * 删除分部门最低资本明细
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteDeptMincapDetailDtoById(Long id);

    /**
     * 根据条件删除分部门最低资本明细
     *
     * @param accountingPeriod 账期
     * @param department 部门
     * @param itemCode 项目编码
     * @return 结果
     */
    int deleteDeptMincapDetailDtoByCondition(String accountingPeriod, String department, String itemCode);

    /**
     * 批量删除分部门最低资本明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteDeptMincapDetailDtoByIds(Long[] ids);

    /**
     * 根据账期删除分部门最低资本明细
     *
     * @param accountingPeriod 账期
     * @return 结果
     */
    int deleteDeptMincapDetailDtoByPeriod(String accountingPeriod);

    /**
     * 批量更新分部门最低资本明细（事务方法）
     *
     * @param accountingPeriod 账期
     * @param department 部门
     * @param itemCode 项目编码
     * @param dtoList 数据列表
     * @return 结果
     */
    int updateBatchDeptMincapDetailDto(String accountingPeriod, String department, String itemCode, List<DeptMincapDetailDTO> dtoList);

    /**
     * 导入分部门最低资本明细
     *
     * @param dtoList 分部门最低资本明细数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    String importDeptMincapDetailDto(List<DeptMincapDetailDTO> dtoList, Boolean updateSupport, String username);
}
