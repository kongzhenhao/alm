package com.xl.alm.app.service;

import com.xl.alm.app.dto.LiabilityCashFlowDTO;
import com.xl.alm.app.query.LiabilityCashFlowQuery;

import java.util.List;

/**
 * 负债现金流表 Service 接口
 *
 * @author AI Assistant
 */
public interface LiabilityCashFlowService {

    /**
     * 查询负债现金流列表
     *
     * @param query 负债现金流查询条件
     * @return 负债现金流列表
     */
    List<LiabilityCashFlowDTO> selectLiabilityCashFlowDtoList(LiabilityCashFlowQuery query);

    /**
     * 用id查询负债现金流
     *
     * @param id id
     * @return 负债现金流
     */
    LiabilityCashFlowDTO selectLiabilityCashFlowDtoById(Long id);

    /**
     * 根据条件查询负债现金流
     *
     * @param accountPeriod 账期
     * @param cashFlowType 现金流类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @param designType 设计类型
     * @param isShortTerm 是否中短期险种
     * @param actuarialCode 精算代码
     * @return 负债现金流
     */
    LiabilityCashFlowDTO selectLiabilityCashFlowDtoByCondition(
            String accountPeriod,
            String cashFlowType,
            String bpType,
            String durationType,
            String designType,
            String isShortTerm,
            String actuarialCode);

    /**
     * 批量插入负债现金流数据
     *
     * @param dtoList 负债现金流列表
     * @return 影响行数
     */
    int batchInsertLiabilityCashFlowDto(List<LiabilityCashFlowDTO> dtoList);

    /**
     * 更新负债现金流数据
     *
     * @param dto 负债现金流
     * @return 结果
     */
    int updateLiabilityCashFlowDto(LiabilityCashFlowDTO dto);

    /**
     * 删除指定账期的负债现金流数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    int deleteLiabilityCashFlowDtoByPeriod(String accountPeriod);

    /**
     * 删除指定id的负债现金流数据
     *
     * @param id id
     * @return 影响行数
     */
    int deleteLiabilityCashFlowDtoById(Long id);
    
    /**
     * 批量删除负债现金流
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteLiabilityCashFlowDtoByIds(Long[] ids);

    /**
     * 导入负债现金流
     *
     * @param dtoList 负债现金流数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    String importLiabilityCashFlowDto(List<LiabilityCashFlowDTO> dtoList, Boolean updateSupport, String username);
}
