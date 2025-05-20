package com.xl.alm.app.service;

import com.xl.alm.app.dto.PolicyDetailDTO;
import com.xl.alm.app.query.PolicyDetailQuery;

import java.util.List;

/**
 * 保单数据明细表 Service 接口
 *
 * @author AI Assistant
 */
public interface PolicyDetailService {

    /**
     * 查询保单数据明细列表
     *
     * @param query 保单数据明细查询条件
     * @return 保单数据明细列表
     */
    List<PolicyDetailDTO> selectPolicyDetailDtoList(PolicyDetailQuery query);

    /**
     * 用id查询保单数据明细
     *
     * @param id id
     * @return 保单数据明细
     */
    PolicyDetailDTO selectPolicyDetailDtoById(Long id);

    /**
     * 根据险种号查询保单数据明细
     *
     * @param polNo 险种号
     * @return 保单数据明细
     */
    PolicyDetailDTO selectPolicyDetailDtoByPolNo(String polNo);

    /**
     * 新增保单数据明细
     *
     * @param dto 保单数据明细
     * @return 结果
     */
    int insertPolicyDetailDto(PolicyDetailDTO dto);

    /**
     * 批量新增保单数据明细
     *
     * @param dtoList 保单数据明细列表
     * @return 结果
     */
    int batchInsertPolicyDetailDto(List<PolicyDetailDTO> dtoList);

    /**
     * 修改保单数据明细
     *
     * @param dto 保单数据明细
     * @return 结果
     */
    int updatePolicyDetailDto(PolicyDetailDTO dto);

    /**
     * 批量删除保单数据明细
     *
     * @param ids 需要删除的保单数据明细主键集合
     * @return 结果
     */
    int deletePolicyDetailDtoByIds(Long[] ids);

    /**
     * 删除保单数据明细信息
     *
     * @param id 保单数据明细主键
     * @return 结果
     */
    int deletePolicyDetailDtoById(Long id);

    /**
     * 导入保单数据明细
     *
     * @param dtoList 保单数据明细数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    String importPolicyDetailDto(List<PolicyDetailDTO> dtoList, Boolean updateSupport, String username);
}
