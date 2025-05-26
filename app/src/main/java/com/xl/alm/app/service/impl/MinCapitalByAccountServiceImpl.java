package com.xl.alm.app.service.impl;

import com.xl.alm.app.dto.MinCapitalByAccountDTO;
import com.xl.alm.app.entity.MinCapitalByAccountEntity;
import com.xl.alm.app.mapper.MinCapitalByAccountMapper;
import com.xl.alm.app.query.MinCapitalByAccountQuery;
import com.xl.alm.app.service.MinCapitalByAccountService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 市场及信用最低资本表Service业务层处理
 *
 * @author alm
 * @date 2024-01-01
 */
@Slf4j
@Service
public class MinCapitalByAccountServiceImpl implements MinCapitalByAccountService {

    @Autowired
    private MinCapitalByAccountMapper minCapitalByAccountMapper;

    /**
     * 查询市场及信用最低资本表列表
     *
     * @param query 查询条件
     * @return 市场及信用最低资本表集合
     */
    @Override
    public List<MinCapitalByAccountDTO> selectMinCapitalByAccountDtoList(MinCapitalByAccountQuery query) {
        // 使用关联查询获取包含项目名称的数据（每个账户一条记录）
        List<MinCapitalByAccountDTO> rawDtoList = minCapitalByAccountMapper.selectMinCapitalByAccountDtoList(query);
        if (rawDtoList == null || rawDtoList.isEmpty()) {
            return new ArrayList<>();
        }

        // 转换为聚合DTO列表
        List<MinCapitalByAccountDTO> dtoList = new ArrayList<>();

        // 使用Map存储已处理的记录，按照项目编码+账期作为键
        Map<String, MinCapitalByAccountDTO> processedMap = new HashMap<>();

        for (MinCapitalByAccountDTO rawDto : rawDtoList) {
            // 构建唯一键：账期+项目编码
            String uniqueKey = rawDto.getAccountingPeriod() + "-" + rawDto.getItemCode();

            // 如果已经处理过该项目编码+账期的记录，则更新账户金额
            if (processedMap.containsKey(uniqueKey)) {
                MinCapitalByAccountDTO existingDto = processedMap.get(uniqueKey);

                // 根据账户编码设置金额
                if ("AC001".equals(rawDto.getAccountCode())) {
                    existingDto.setTraditionalAmount(rawDto.getAmount());
                } else if ("AC002".equals(rawDto.getAccountCode())) {
                    existingDto.setDividendAmount(rawDto.getAmount());
                } else if ("AC003".equals(rawDto.getAccountCode())) {
                    existingDto.setUniversalAmount(rawDto.getAmount());
                } else if ("AC006".equals(rawDto.getAccountCode())) {
                    existingDto.setGeneralAmount(rawDto.getAmount());
                }
            } else {
                // 创建新的聚合DTO对象
                MinCapitalByAccountDTO dto = new MinCapitalByAccountDTO();
                dto.setId(rawDto.getId()); // 使用第一条记录的ID
                dto.setAccountingPeriod(rawDto.getAccountingPeriod());
                dto.setItemCode(rawDto.getItemCode());
                dto.setItemName(rawDto.getItemName()); // 使用关联查询得到的项目名称
                dto.setRiskType(rawDto.getRiskType()); // 使用关联查询得到的风险类型
                dto.setCreateTime(rawDto.getCreateTime());
                dto.setCreateBy(rawDto.getCreateBy());
                dto.setUpdateTime(rawDto.getUpdateTime());
                dto.setUpdateBy(rawDto.getUpdateBy());
                dto.setIsDel(rawDto.getIsDel());

                // 初始化所有账户金额为0
                dto.setTraditionalAmount(BigDecimal.ZERO);
                dto.setDividendAmount(BigDecimal.ZERO);
                dto.setUniversalAmount(BigDecimal.ZERO);
                dto.setGeneralAmount(BigDecimal.ZERO);

                // 根据账户编码设置金额
                if ("AC001".equals(rawDto.getAccountCode())) {
                    dto.setTraditionalAmount(rawDto.getAmount());
                } else if ("AC002".equals(rawDto.getAccountCode())) {
                    dto.setDividendAmount(rawDto.getAmount());
                } else if ("AC003".equals(rawDto.getAccountCode())) {
                    dto.setUniversalAmount(rawDto.getAmount());
                } else if ("AC006".equals(rawDto.getAccountCode())) {
                    dto.setGeneralAmount(rawDto.getAmount());
                }

                // 添加到处理过的记录Map和结果列表
                processedMap.put(uniqueKey, dto);
                dtoList.add(dto);
            }
        }

        return dtoList;
    }

    /**
     * 查询市场及信用最低资本表详情
     *
     * @param id 市场及信用最低资本表主键
     * @return 市场及信用最低资本表
     */
    @Override
    public MinCapitalByAccountDTO selectMinCapitalByAccountDtoById(Long id) {
        MinCapitalByAccountEntity entity = minCapitalByAccountMapper.selectMinCapitalByAccountEntityById(id);
        return EntityDtoConvertUtil.convertToDTO(entity, MinCapitalByAccountDTO.class);
    }

    /**
     * 根据账期、项目编码和账户编码查询市场及信用最低资本表详情
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @return 市场及信用最低资本表
     */
    @Override
    public MinCapitalByAccountDTO selectMinCapitalByAccountDtoByUniqueKey(String accountingPeriod, String itemCode, String accountCode) {
        MinCapitalByAccountEntity entity = minCapitalByAccountMapper.selectMinCapitalByAccountEntityByUniqueKey(accountingPeriod, itemCode, accountCode);
        return EntityDtoConvertUtil.convertToDTO(entity, MinCapitalByAccountDTO.class);
    }

    /**
     * 根据账期、项目编码和账户编码查询有效的市场及信用最低资本表详情（用于检查）
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @return 市场及信用最低资本表
     */
    @Override
    public MinCapitalByAccountDTO selectValidMinCapitalByAccountDtoByUniqueKey(String accountingPeriod, String itemCode, String accountCode) {
        MinCapitalByAccountEntity entity = minCapitalByAccountMapper.selectValidMinCapitalByAccountEntityByUniqueKey(accountingPeriod, itemCode, accountCode);
        return EntityDtoConvertUtil.convertToDTO(entity, MinCapitalByAccountDTO.class);
    }

    /**
     * 新增市场及信用最低资本表
     *
     * @param dto 市场及信用最低资本表
     * @return 结果
     */
    @Override
    @Transactional
    public int insertMinCapitalByAccountDto(MinCapitalByAccountDTO dto) {
        // 添加详细的调试日志
        log.info("准备插入记录：账期={}, 项目编码={}, 账户编码={}",
                dto.getAccountingPeriod(), dto.getItemCode(), dto.getAccountCode());

        // 检查是否已存在相同的记录（只查有效记录）
        MinCapitalByAccountEntity existEntity = minCapitalByAccountMapper.selectValidMinCapitalByAccountEntityByUniqueKey(
                dto.getAccountingPeriod(), dto.getItemCode(), dto.getAccountCode());

        log.info("查询有效记录结果：{}", existEntity != null ? "找到重复记录，ID=" + existEntity.getId() : "未找到重复记录");

        // 同时查询所有记录（包括已删除的）进行调试
        MinCapitalByAccountEntity anyEntity = minCapitalByAccountMapper.selectAnyMinCapitalByAccountEntityByUniqueKey(
                dto.getAccountingPeriod(), dto.getItemCode(), dto.getAccountCode());

        log.info("查询所有记录结果：{}", anyEntity != null ?
                "找到记录，ID=" + anyEntity.getId() + ", isDel=" + anyEntity.getIsDel() : "未找到任何记录");

        if (existEntity != null) {
            log.warn("尝试插入重复记录：账期={}, 项目编码={}, 账户编码={}, 已存在记录ID={}",
                    dto.getAccountingPeriod(), dto.getItemCode(), dto.getAccountCode(), existEntity.getId());
            throw new RuntimeException("该账期、项目和账户的记录已存在，不能重复添加");
        }

        // 如果有已删除的记录，考虑恢复它而不是插入新记录
        if (anyEntity != null && anyEntity.getIsDel() != null && anyEntity.getIsDel() == 1) {
            log.info("发现已删除的记录，将恢复并更新：ID={}", anyEntity.getId());
            // 恢复记录并更新数据
            dto.setId(anyEntity.getId());
            dto.setIsDel(0); // 恢复为有效状态
            return updateMinCapitalByAccountDto(dto);
        }

        try {
            MinCapitalByAccountEntity entity = EntityDtoConvertUtil.convertToEntity(dto, MinCapitalByAccountEntity.class);
            int result = minCapitalByAccountMapper.insertMinCapitalByAccountEntity(entity);
            log.info("插入成功，影响行数：{}, 生成ID：{}", result, entity.getId());
            return result;
        } catch (Exception e) {
            log.error("插入记录失败：账期={}, 项目编码={}, 账户编码={}",
                    dto.getAccountingPeriod(), dto.getItemCode(), dto.getAccountCode(), e);
            throw new RuntimeException("插入记录失败：" + e.getMessage(), e);
        }
    }

    /**
     * 批量新增市场及信用最低资本表
     *
     * @param dtoList 市场及信用最低资本表列表
     * @return 结果
     */
    @Override
    @Transactional
    public int batchInsertMinCapitalByAccountDto(List<MinCapitalByAccountDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return 0;
        }

        // 检查每条记录是否已存在
        for (MinCapitalByAccountDTO dto : dtoList) {
            MinCapitalByAccountEntity existEntity = minCapitalByAccountMapper.selectValidMinCapitalByAccountEntityByUniqueKey(
                    dto.getAccountingPeriod(), dto.getItemCode(), dto.getAccountCode());

            if (existEntity != null) {
                log.warn("批量插入时发现重复记录：账期={}, 项目编码={}, 账户编码={}",
                        dto.getAccountingPeriod(), dto.getItemCode(), dto.getAccountCode());
                throw new RuntimeException("批量插入失败：账期" + dto.getAccountingPeriod() +
                        "、项目" + dto.getItemCode() + "、账户" + dto.getAccountCode() + "的记录已存在");
            }
        }

        List<MinCapitalByAccountEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, MinCapitalByAccountEntity.class);
        return minCapitalByAccountMapper.batchInsertMinCapitalByAccountEntity(entityList);
    }

    /**
     * 修改市场及信用最低资本表
     *
     * @param dto 市场及信用最低资本表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateMinCapitalByAccountDto(MinCapitalByAccountDTO dto) {
        MinCapitalByAccountEntity entity = EntityDtoConvertUtil.convertToEntity(dto, MinCapitalByAccountEntity.class);
        return minCapitalByAccountMapper.updateMinCapitalByAccountEntity(entity);
    }

    /**
     * 批量删除市场及信用最低资本表
     *
     * @param ids 需要删除的市场及信用最低资本表主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteMinCapitalByAccountDtoByIds(Long[] ids) {
        return minCapitalByAccountMapper.deleteMinCapitalByAccountEntityByIds(ids);
    }

    /**
     * 删除市场及信用最低资本表信息
     *
     * @param id 市场及信用最低资本表主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteMinCapitalByAccountDtoById(Long id) {
        return minCapitalByAccountMapper.deleteMinCapitalByAccountEntityById(id);
    }
}
