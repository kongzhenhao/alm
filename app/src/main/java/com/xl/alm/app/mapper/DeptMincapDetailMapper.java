package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.DeptMincapDetailEntity;
import com.xl.alm.app.query.DeptMincapDetailQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分部门最低资本明细表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface DeptMincapDetailMapper {

    /**
     * 查询分部门最低资本明细列表
     *
     * @param query 分部门最低资本明细查询条件
     * @return 分部门最低资本明细列表
     */
    List<DeptMincapDetailEntity> selectDeptMincapDetailEntityList(DeptMincapDetailQuery query);

    /**
     * 查询分部门最低资本明细详情
     *
     * @param id 主键ID
     * @return 分部门最低资本明细
     */
    DeptMincapDetailEntity selectDeptMincapDetailEntityById(Long id);

    /**
     * 新增分部门最低资本明细
     *
     * @param entity 分部门最低资本明细
     * @return 结果
     */
    int insertDeptMincapDetailEntity(DeptMincapDetailEntity entity);

    /**
     * 批量新增分部门最低资本明细
     *
     * @param entityList 分部门最低资本明细列表
     * @return 结果
     */
    int batchInsertDeptMincapDetailEntity(List<DeptMincapDetailEntity> entityList);

    /**
     * 逻辑删除分部门最低资本明细
     *
     * @param accountingPeriod 账期
     * @param department 部门
     * @param itemCode 项目编码
     * @return 结果
     */
    int logicDeleteDeptMincapDetailEntity(@Param("accountingPeriod") String accountingPeriod,
                                         @Param("department") String department,
                                         @Param("itemCode") String itemCode);

    /**
     * 物理删除分部门最低资本明细
     *
     * @param accountingPeriod 账期
     * @param department 部门
     * @param itemCode 项目编码
     * @return 结果
     */
    int physicalDeleteDeptMincapDetailEntity(@Param("accountingPeriod") String accountingPeriod,
                                           @Param("department") String department,
                                           @Param("itemCode") String itemCode);

    /**
     * 修改分部门最低资本明细
     *
     * @param entity 分部门最低资本明细
     * @return 结果
     */
    int updateDeptMincapDetailEntity(DeptMincapDetailEntity entity);

    /**
     * 删除分部门最低资本明细
     *
     * @param id 主键ID
     * @return 结果
     */
    int deleteDeptMincapDetailEntityById(Long id);

    /**
     * 根据条件物理删除分部门最低资本明细
     *
     * @param accountingPeriod 账期
     * @param department 部门
     * @param itemCode 项目编码
     * @return 结果
     */
    int physicalDeleteByCondition(@Param("accountingPeriod") String accountingPeriod,
                                 @Param("department") String department,
                                 @Param("itemCode") String itemCode);

    /**
     * 批量删除分部门最低资本明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteDeptMincapDetailEntityByIds(@Param("ids") Long[] ids);

    /**
     * 根据账期删除分部门最低资本明细
     *
     * @param accountingPeriod 账期
     * @return 结果
     */
    int deleteDeptMincapDetailEntityByPeriod(String accountingPeriod);
}
