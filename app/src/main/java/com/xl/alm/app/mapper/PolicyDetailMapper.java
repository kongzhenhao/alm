package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.PolicyDetailEntity;
import com.xl.alm.app.query.PolicyDetailQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 保单数据明细表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface PolicyDetailMapper {

    /**
     * 查询保单数据明细列表
     *
     * @param query 保单数据明细查询条件
     * @return 保单数据明细列表
     */
    List<PolicyDetailEntity> selectPolicyDetailEntityList(PolicyDetailQuery query);

    /**
     * 用id查询保单数据明细
     *
     * @param id id
     * @return 保单数据明细
     */
    PolicyDetailEntity selectPolicyDetailEntityById(Long id);

    /**
     * 根据险种号查询保单数据明细
     *
     * @param polNo 险种号
     * @return 保单数据明细
     */
    PolicyDetailEntity selectPolicyDetailEntityByPolNo(@Param("polNo") String polNo);

    /**
     * 新增保单数据明细
     *
     * @param entity 保单数据明细
     * @return 结果
     */
    int insertPolicyDetailEntity(PolicyDetailEntity entity);

    /**
     * 批量新增保单数据明细
     *
     * @param entityList 保单数据明细列表
     * @return 结果
     */
    int batchInsertPolicyDetailEntity(List<PolicyDetailEntity> entityList);

    /**
     * 修改保单数据明细
     *
     * @param entity 保单数据明细
     * @return 结果
     */
    int updatePolicyDetailEntity(PolicyDetailEntity entity);

    /**
     * 删除保单数据明细
     *
     * @param id 保单数据明细主键
     * @return 结果
     */
    int deletePolicyDetailEntityById(Long id);

    /**
     * 批量删除保单数据明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deletePolicyDetailEntityByIds(Long[] ids);
}
