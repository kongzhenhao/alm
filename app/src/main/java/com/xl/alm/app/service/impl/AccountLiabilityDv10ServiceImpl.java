package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.AccountLiabilityDv10DTO;
import com.xl.alm.app.entity.AccountLiabilityDv10Entity;
import com.xl.alm.app.mapper.AccountLiabilityDv10Mapper;
import com.xl.alm.app.query.AccountLiabilityDv10Query;
import com.xl.alm.app.service.AccountLiabilityDv10Service;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 分账户负债基点价值DV10表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class AccountLiabilityDv10ServiceImpl implements AccountLiabilityDv10Service {

    @Autowired
    private AccountLiabilityDv10Mapper accountLiabilityDv10Mapper;

    /**
     * 查询分账户负债基点价值DV10列表
     *
     * @param query 分账户负债基点价值DV10查询条件
     * @return 分账户负债基点价值DV10列表
     */
    @Override
    public List<AccountLiabilityDv10DTO> selectAccountLiabilityDv10DtoList(AccountLiabilityDv10Query query) {
        List<AccountLiabilityDv10Entity> entityList = accountLiabilityDv10Mapper.selectAccountLiabilityDv10EntityList(query);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, AccountLiabilityDv10DTO.class);
    }

    /**
     * 根据ID查询分账户负债基点价值DV10
     *
     * @param id ID
     * @return 分账户负债基点价值DV10
     */
    @Override
    public AccountLiabilityDv10DTO selectAccountLiabilityDv10DtoById(Long id) {
        AccountLiabilityDv10Entity entity = accountLiabilityDv10Mapper.selectAccountLiabilityDv10EntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, AccountLiabilityDv10DTO.class);
    }

    /**
     * 根据条件查询分账户负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @param cashFlowType  现金流类型
     * @param designType    设计类型
     * @param valueType     现值类型
     * @return 分账户负债基点价值DV10
     */
    @Override
    public AccountLiabilityDv10DTO selectAccountLiabilityDv10DtoByCondition(
            String accountPeriod,
            String cashFlowType,
            String designType,
            String valueType) {
        AccountLiabilityDv10Entity entity = accountLiabilityDv10Mapper.selectAccountLiabilityDv10EntityByCondition(
                accountPeriod, cashFlowType, designType, valueType);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, AccountLiabilityDv10DTO.class);
    }

    /**
     * 新增分账户负债基点价值DV10
     *
     * @param dto 分账户负债基点价值DV10
     * @return 结果
     */
    @Override
    public int insertAccountLiabilityDv10Dto(AccountLiabilityDv10DTO dto) {
        AccountLiabilityDv10Entity entity = EntityDtoConvertUtil.convertToEntity(dto, AccountLiabilityDv10Entity.class);
        return accountLiabilityDv10Mapper.insertAccountLiabilityDv10Entity(entity);
    }

    /**
     * 批量新增分账户负债基点价值DV10
     *
     * @param dtoList 分账户负债基点价值DV10列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertAccountLiabilityDv10Dto(List<AccountLiabilityDv10DTO> dtoList) {
        List<AccountLiabilityDv10Entity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, AccountLiabilityDv10Entity.class);
        return accountLiabilityDv10Mapper.batchInsertAccountLiabilityDv10Entity(entityList);
    }

    /**
     * 修改分账户负债基点价值DV10
     *
     * @param dto 分账户负债基点价值DV10
     * @return 结果
     */
    @Override
    public int updateAccountLiabilityDv10Dto(AccountLiabilityDv10DTO dto) {
        AccountLiabilityDv10Entity entity = EntityDtoConvertUtil.convertToEntity(dto, AccountLiabilityDv10Entity.class);
        return accountLiabilityDv10Mapper.updateAccountLiabilityDv10Entity(entity);
    }

    /**
     * 删除分账户负债基点价值DV10
     *
     * @param id 分账户负债基点价值DV10ID
     * @return 结果
     */
    @Override
    public int deleteAccountLiabilityDv10DtoById(Long id) {
        return accountLiabilityDv10Mapper.deleteAccountLiabilityDv10EntityById(id);
    }

    /**
     * 批量删除分账户负债基点价值DV10
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAccountLiabilityDv10DtoByIds(Long[] ids) {
        return accountLiabilityDv10Mapper.deleteAccountLiabilityDv10EntityByIds(ids);
    }

    /**
     * 删除指定账期的分账户负债基点价值DV10
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    @Override
    public int deleteAccountLiabilityDv10DtoByPeriod(String accountPeriod) {
        return accountLiabilityDv10Mapper.deleteAccountLiabilityDv10EntityByPeriod(accountPeriod);
    }

    /**
     * 导入分账户负债基点价值DV10
     *
     * @param dtoList       分账户负债基点价值DV10数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username      操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importAccountLiabilityDv10Dto(List<AccountLiabilityDv10DTO> dtoList, Boolean updateSupport, String username) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (AccountLiabilityDv10DTO dto : dtoList) {
            try {
                // 设置创建者和更新者
                dto.setCreateBy(username);
                dto.setUpdateBy(username);

                // 验证是否存在相同的记录
                AccountLiabilityDv10DTO existDto = selectAccountLiabilityDv10DtoByCondition(
                        dto.getAccountPeriod(), dto.getCashFlowType(), dto.getDesignType(), dto.getValueType());

                if (StringUtils.isNull(existDto)) {
                    // 不存在，执行新增
                    insertAccountLiabilityDv10Dto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod())
                            .append(" 导入成功");
                } else if (updateSupport) {
                    // 存在且允许更新，执行更新
                    dto.setId(existDto.getId());
                    updateAccountLiabilityDv10Dto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod())
                            .append(" 更新成功");
                } else {
                    // 存在但不允许更新，记录失败
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、账期 ").append(dto.getAccountPeriod())
                            .append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账期 " + dto.getAccountPeriod() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new RuntimeException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
