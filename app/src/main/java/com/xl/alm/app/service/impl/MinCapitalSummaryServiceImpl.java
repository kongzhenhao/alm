package com.xl.alm.app.service.impl;

import com.xl.alm.app.dto.MinCapitalSummaryDTO;
import com.xl.alm.app.entity.MinCapitalSummaryEntity;
import com.xl.alm.app.mapper.MinCapitalSummaryMapper;
import com.xl.alm.app.query.MinCapitalSummaryQuery;
import com.xl.alm.app.service.MinCapitalSummaryService;
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
 * 最低资本明细汇总表 Service实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class MinCapitalSummaryServiceImpl implements MinCapitalSummaryService {

    @Autowired
    private MinCapitalSummaryMapper minCapitalSummaryMapper;

    /**
     * 查询最低资本明细汇总表列表
     *
     * @param query 查询条件
     * @return 最低资本明细汇总表集合
     */
    @Override
    public List<MinCapitalSummaryDTO> selectMinCapitalSummaryDtoList(MinCapitalSummaryQuery query) {
        // 使用关联查询获取包含项目名称的数据（每个账户一条记录）
        List<MinCapitalSummaryDTO> rawDtoList = minCapitalSummaryMapper.selectMinCapitalSummaryDtoList(query);
        if (rawDtoList == null || rawDtoList.isEmpty()) {
            return new ArrayList<>();
        }

        // 转换为聚合DTO列表
        List<MinCapitalSummaryDTO> dtoList = new ArrayList<>();

        // 使用Map存储已处理的记录，按照项目编码+账期作为键
        Map<String, MinCapitalSummaryDTO> processedMap = new HashMap<>();

        for (MinCapitalSummaryDTO rawDto : rawDtoList) {
            // 构建唯一键：账期+项目编码
            String uniqueKey = rawDto.getAccountingPeriod() + "-" + rawDto.getItemCode();

            // 如果已经处理过该项目编码+账期的记录，则更新账户金额
            if (processedMap.containsKey(uniqueKey)) {
                MinCapitalSummaryDTO existingDto = processedMap.get(uniqueKey);

                // 根据账户编码设置金额
                if ("AC001".equals(rawDto.getAccountCode())) {
                    existingDto.setTraditionalAmount(rawDto.getAmount());
                } else if ("AC002".equals(rawDto.getAccountCode())) {
                    existingDto.setDividendAmount(rawDto.getAmount());
                } else if ("AC003".equals(rawDto.getAccountCode())) {
                    existingDto.setUniversalAmount(rawDto.getAmount());
                } else if ("AC004".equals(rawDto.getAccountCode())) {
                    existingDto.setIndependentAmount(rawDto.getAmount());
                } else if ("AC005".equals(rawDto.getAccountCode())) {
                    existingDto.setCompanyTotalAmount(rawDto.getAmount());
                }
            } else {
                // 创建新的聚合DTO对象
                MinCapitalSummaryDTO dto = new MinCapitalSummaryDTO();
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
                dto.setIndependentAmount(BigDecimal.ZERO);
                dto.setCompanyTotalAmount(BigDecimal.ZERO);

                // 根据账户编码设置金额
                if ("AC001".equals(rawDto.getAccountCode())) {
                    dto.setTraditionalAmount(rawDto.getAmount());
                } else if ("AC002".equals(rawDto.getAccountCode())) {
                    dto.setDividendAmount(rawDto.getAmount());
                } else if ("AC003".equals(rawDto.getAccountCode())) {
                    dto.setUniversalAmount(rawDto.getAmount());
                } else if ("AC004".equals(rawDto.getAccountCode())) {
                    dto.setIndependentAmount(rawDto.getAmount());
                } else if ("AC005".equals(rawDto.getAccountCode())) {
                    dto.setCompanyTotalAmount(rawDto.getAmount());
                }

                // 添加到处理过的记录Map和结果列表
                processedMap.put(uniqueKey, dto);
                dtoList.add(dto);
            }
        }

        return dtoList;
    }



    /**
     * 查询最低资本明细汇总表详情
     *
     * @param id 主键ID
     * @return 最低资本明细汇总表
     */
    @Override
    public MinCapitalSummaryDTO selectMinCapitalSummaryDtoById(Long id) {
        MinCapitalSummaryEntity entity = minCapitalSummaryMapper.selectMinCapitalSummaryEntityById(id);
        return EntityDtoConvertUtil.convertToDTO(entity, MinCapitalSummaryDTO.class);
    }

    /**
     * 根据账期、项目编码和账户编码查询最低资本明细汇总表详情
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @return 最低资本明细汇总表
     */
    @Override
    public MinCapitalSummaryDTO selectMinCapitalSummaryDtoByUniqueKey(String accountingPeriod, String itemCode, String accountCode) {
        MinCapitalSummaryEntity entity = minCapitalSummaryMapper.selectMinCapitalSummaryEntityByUniqueKey(accountingPeriod, itemCode, accountCode);
        return EntityDtoConvertUtil.convertToDTO(entity, MinCapitalSummaryDTO.class);
    }

    /**
     * 根据账期、项目编码和账户编码查询有效的最低资本明细汇总表详情（用于检查）
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @param accountCode 账户编码
     * @return 最低资本明细汇总表
     */
    @Override
    public MinCapitalSummaryDTO selectValidMinCapitalSummaryDtoByUniqueKey(String accountingPeriod, String itemCode, String accountCode) {
        MinCapitalSummaryEntity entity = minCapitalSummaryMapper.selectValidMinCapitalSummaryEntityByUniqueKey(accountingPeriod, itemCode, accountCode);
        return EntityDtoConvertUtil.convertToDTO(entity, MinCapitalSummaryDTO.class);
    }

    /**
     * 新增最低资本明细汇总表
     *
     * @param dto 最低资本明细汇总表
     * @return 结果
     */
    @Override
    public int insertMinCapitalSummaryDto(MinCapitalSummaryDTO dto) {
        // 添加详细的调试日志
        log.info("准备插入记录：账期={}, 项目编码={}, 账户编码={}",
                dto.getAccountingPeriod(), dto.getItemCode(), dto.getAccountCode());

        // 检查是否已存在相同的记录（只查有效记录）
        MinCapitalSummaryEntity existEntity = minCapitalSummaryMapper.selectValidMinCapitalSummaryEntityByUniqueKey(
                dto.getAccountingPeriod(), dto.getItemCode(), dto.getAccountCode());

        log.info("查询有效记录结果：{}", existEntity != null ? "找到重复记录，ID=" + existEntity.getId() : "未找到重复记录");

        // 同时查询所有记录（包括已删除的）进行调试
        MinCapitalSummaryEntity anyEntity = minCapitalSummaryMapper.selectAnyMinCapitalSummaryEntityByUniqueKey(
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
            return updateMinCapitalSummaryDto(dto);
        }

        // 设置默认值
        if (dto.getIsDel() == null) {
            dto.setIsDel(0); // 默认未删除
        }

        log.info("开始执行插入操作...");
        MinCapitalSummaryEntity entity = EntityDtoConvertUtil.convertToEntity(dto, MinCapitalSummaryEntity.class);

        try {
            int result = minCapitalSummaryMapper.insertMinCapitalSummaryEntity(entity);
            log.info("插入成功，影响行数：{}", result);
            return result;
        } catch (Exception e) {
            log.error("插入失败：{}", e.getMessage());
            throw e;
        }
    }

    /**
     * 批量新增最低资本明细汇总表
     *
     * @param dtoList 最低资本明细汇总表列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertMinCapitalSummaryDto(List<MinCapitalSummaryDTO> dtoList) {
        // 检查每条记录是否已存在
        for (MinCapitalSummaryDTO dto : dtoList) {
            MinCapitalSummaryEntity existEntity = minCapitalSummaryMapper.selectValidMinCapitalSummaryEntityByUniqueKey(
                    dto.getAccountingPeriod(), dto.getItemCode(), dto.getAccountCode());

            if (existEntity != null) {
                log.warn("批量插入时发现重复记录：账期={}, 项目编码={}, 账户编码={}",
                        dto.getAccountingPeriod(), dto.getItemCode(), dto.getAccountCode());
                throw new RuntimeException("批量插入失败：账期" + dto.getAccountingPeriod() +
                        "、项目" + dto.getItemCode() + "、账户" + dto.getAccountCode() + "的记录已存在");
            }

            // 设置默认值
            if (dto.getIsDel() == null) {
                dto.setIsDel(0); // 默认未删除
            }
        }

        List<MinCapitalSummaryEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, MinCapitalSummaryEntity.class);
        return minCapitalSummaryMapper.batchInsertMinCapitalSummaryEntity(entityList);
    }

    /**
     * 修改最低资本明细汇总表
     *
     * @param dto 最低资本明细汇总表
     * @return 结果
     */
    @Override
    public int updateMinCapitalSummaryDto(MinCapitalSummaryDTO dto) {
        MinCapitalSummaryEntity entity = EntityDtoConvertUtil.convertToEntity(dto, MinCapitalSummaryEntity.class);
        return minCapitalSummaryMapper.updateMinCapitalSummaryEntity(entity);
    }

    /**
     * 删除最低资本明细汇总表
     *
     * @param id 主键ID
     * @return 结果
     */
    @Override
    public int deleteMinCapitalSummaryDtoById(Long id) {
        return minCapitalSummaryMapper.deleteMinCapitalSummaryEntityById(id);
    }

    /**
     * 批量删除最低资本明细汇总表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    @Override
    public int deleteMinCapitalSummaryDtoByIds(Long[] ids) {
        return minCapitalSummaryMapper.deleteMinCapitalSummaryEntityByIds(ids);
    }
}
