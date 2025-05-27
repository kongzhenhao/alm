package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.jd.lightning.common.core.domain.entity.SysDictData;
import com.jd.lightning.system.service.ISysDictTypeService;
import com.xl.alm.app.dto.RiskItemAmountDTO;
import com.xl.alm.app.dto.RiskItemAmountExportDTO;
import com.xl.alm.app.entity.RiskItemAmountEntity;
import com.xl.alm.app.mapper.RiskItemAmountMapper;
import com.xl.alm.app.query.RiskItemAmountQuery;
import com.xl.alm.app.service.RiskItemAmountService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import com.xl.alm.app.util.ExcelUtil;
import com.jd.lightning.common.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 风险项目金额表 服务层实现
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class RiskItemAmountServiceImpl implements RiskItemAmountService {

    @Autowired
    private RiskItemAmountMapper riskItemAmountMapper;

    /**
     * 查询风险项目金额列表
     *
     * @param query 查询条件
     * @return 风险项目金额集合
     */
    @Override
    public List<RiskItemAmountDTO> selectRiskItemAmountDtoList(RiskItemAmountQuery query) {
        List<RiskItemAmountEntity> entityList = riskItemAmountMapper.selectRiskItemAmountEntityList(query);
        List<RiskItemAmountDTO> dtoList = EntityDtoConvertUtil.convertToDTOList(entityList, RiskItemAmountDTO.class);

        // 设置项目名称
        for (RiskItemAmountDTO dto : dtoList) {
            if (dto.getItemCode() != null) {
                dto.setItemName(getItemName(dto.getItemCode()));
            }
        }

        return dtoList;
    }

    /**
     * 查询风险项目金额详情
     *
     * @param id 主键ID
     * @return 风险项目金额
     */
    @Override
    public RiskItemAmountDTO selectRiskItemAmountDtoById(Long id) {
        RiskItemAmountEntity entity = riskItemAmountMapper.selectRiskItemAmountEntityById(id);
        if (entity == null) {
            return null;
        }
        RiskItemAmountDTO dto = EntityDtoConvertUtil.convertToDTO(entity, RiskItemAmountDTO.class);

        // 设置项目名称
        if (dto.getItemCode() != null) {
            dto.setItemName(getItemName(dto.getItemCode()));
        }

        return dto;
    }

    /**
     * 新增风险项目金额
     *
     * @param dto 风险项目金额
     * @return 结果
     */
    @Override
    public int insertRiskItemAmountDto(RiskItemAmountDTO dto) {
        RiskItemAmountEntity entity = EntityDtoConvertUtil.convertToEntity(dto, RiskItemAmountEntity.class);
        return riskItemAmountMapper.insertRiskItemAmountEntity(entity);
    }

    /**
     * 批量新增风险项目金额
     *
     * @param dtoList 风险项目金额列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertRiskItemAmountDto(List<RiskItemAmountDTO> dtoList) {
        List<RiskItemAmountEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, RiskItemAmountEntity.class);
        return riskItemAmountMapper.batchInsertRiskItemAmountEntity(entityList);
    }

    /**
     * 修改风险项目金额
     *
     * @param dto 风险项目金额
     * @return 结果
     */
    @Override
    public int updateRiskItemAmountDto(RiskItemAmountDTO dto) {
        RiskItemAmountEntity entity = EntityDtoConvertUtil.convertToEntity(dto, RiskItemAmountEntity.class);
        return riskItemAmountMapper.updateRiskItemAmountEntity(entity);
    }

    /**
     * 删除风险项目金额
     *
     * @param id 主键ID
     * @return 结果
     */
    @Override
    public int deleteRiskItemAmountDtoById(Long id) {
        return riskItemAmountMapper.deleteRiskItemAmountEntityById(id);
    }

    /**
     * 批量删除风险项目金额
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    @Override
    public int deleteRiskItemAmountDtoByIds(Long[] ids) {
        return riskItemAmountMapper.deleteRiskItemAmountEntityByIds(ids);
    }

    /**
     * 根据账期删除风险项目金额
     *
     * @param accountingPeriod 账期
     * @return 结果
     */
    @Override
    public int deleteRiskItemAmountDtoByPeriod(String accountingPeriod) {
        return riskItemAmountMapper.deleteRiskItemAmountEntityByPeriod(accountingPeriod);
    }

    /**
     * 根据条件删除风险项目金额
     *
     * @param accountingPeriod 账期
     * @param itemCode 项目编码
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRiskItemAmountDtoByCondition(String accountingPeriod, String itemCode) {
        return riskItemAmountMapper.deleteRiskItemAmountEntityByCondition(accountingPeriod, itemCode);
    }

    /**
     * 导入风险项目金额数据
     *
     * @param dtoList 风险项目金额数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importRiskItemAmountDto(List<RiskItemAmountDTO> dtoList, Boolean updateSupport, String username) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        // 获取当前账期（从导入数据中获取，如果没有则使用默认值）
        String currentPeriod = null;

        // 尝试从导入数据中获取账期
        if (!dtoList.isEmpty() && dtoList.get(0).getAccountingPeriod() != null) {
            currentPeriod = dtoList.get(0).getAccountingPeriod();
        }

        // 如果没有获取到账期，则使用当前日期作为账期
        if (StringUtils.isEmpty(currentPeriod)) {
            // 获取当前日期，格式为YYYYMM
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMM");
            currentPeriod = sdf.format(new java.util.Date());
            log.info("未从导入数据中获取到账期，使用当前日期作为账期：" + currentPeriod);
        }

        List<RiskItemAmountEntity> entityList = new ArrayList<>();

        int sortOrder = 1; // 从1开始的行号
        for (RiskItemAmountDTO dto : dtoList) {
            try {
                // 检查必要字段
                if (StringUtils.isEmpty(dto.getItemName())) {
                    throw new RuntimeException("项目名称不能为空");
                }

                // 根据项目名称获取项目编码
                String itemCode = getItemCodeByName(dto.getItemName());
                if ("UNKNOWN".equals(itemCode)) {
                    throw new RuntimeException("未找到项目名称对应的项目编码：" + dto.getItemName());
                }
                dto.setItemCode(itemCode);

                // 如果账期为空，则使用当前账期
                if (StringUtils.isEmpty(dto.getAccountingPeriod())) {
                    dto.setAccountingPeriod(currentPeriod);
                }

                // 处理金额，确保不为null
                if (dto.getS05Amount() == null) {
                    dto.setS05Amount(BigDecimal.ZERO);
                }
                if (dto.getIr05Amount() == null) {
                    dto.setIr05Amount(BigDecimal.ZERO);
                }

                // 设置操作人
                dto.setCreateBy(username);
                dto.setUpdateBy(username);

                // 转换为实体对象
                RiskItemAmountEntity entity = EntityDtoConvertUtil.convertToEntity(dto, RiskItemAmountEntity.class);
                // 设置导入顺序
                entity.setSortOrder(sortOrder++);
                entityList.add(entity);

                successNum++;
                successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountingPeriod())
                        .append(" 项目 ").append(dto.getItemCode()).append(" 导入成功");
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账期 " + dto.getAccountingPeriod() + " 项目 " + dto.getItemCode() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }

        // 方案1：直接插入所有数据，允许重复（需要先删除数据库唯一索引）
        if (!entityList.isEmpty()) {
            // 批量插入所有数据，不检查重复
            riskItemAmountMapper.batchInsertRiskItemAmountEntity(entityList);
            log.info("批量插入{}条记录", entityList.size());
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new RuntimeException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 根据项目编码获取项目名称
     *
     * @param itemCode 项目编码
     * @return 项目名称
     */
    private String getItemName(String itemCode) {
        // 从字典表中获取项目名称
        try {
            // 这里应该调用字典服务获取项目名称
            // 由于没有直接访问字典服务的方式，我们可以使用一个映射表
            Map<String, String> itemCodeNameMap = getItemCodeNameMap();

            // 如果找到对应的项目名称，则返回
            if (itemCodeNameMap.containsKey(itemCode)) {
                return itemCodeNameMap.get(itemCode);
            }

            // 如果没有找到，则返回项目编码
            log.warn("未找到项目编码对应的项目名称：" + itemCode);
            return itemCode;
        } catch (Exception e) {
            log.error("获取项目名称时出错：" + itemCode, e);
            return itemCode;
        }
    }

    /**
     * 获取项目编码和名称的映射表
     *
     * @return 项目编码和名称的映射表
     */
    private Map<String, String> getItemCodeNameMap() {
        Map<String, String> itemCodeNameMap = new HashMap<>();

        try {
            // 从字典表中获取项目编码和名称的映射关系
            ISysDictTypeService dictTypeService = SpringUtils.getBean(ISysDictTypeService.class);
            if (dictTypeService != null) {
                List<SysDictData> dictDataList = dictTypeService.selectDictDataByType("minc_risk_item");
                if (dictDataList != null && !dictDataList.isEmpty()) {
                    for (SysDictData dictData : dictDataList) {
                        itemCodeNameMap.put(dictData.getDictValue(), dictData.getDictLabel());
                    }
                    /*log.info("从字典表中获取到{}个风险项目", dictDataList.size());*/
                    // 未知项目
                    itemCodeNameMap.put("UNKNOWN", "未知项目");
                    return itemCodeNameMap;
                }
            }
        } catch (Exception e) {
            log.error("从字典表获取风险项目失败", e);
        }

        // 如果从字典表获取失败，使用默认的映射关系
        log.warn("从字典表获取风险项目失败，使用默认的映射关系");

        // 基础风险项目
        itemCodeNameMap.put("MR001_01", "寿险业务保险风险最低资本");
        itemCodeNameMap.put("MR001_02", "非寿险业务保险风险最低资本");
        itemCodeNameMap.put("MR002_01", "市场风险最低资本");
        itemCodeNameMap.put("MR002_02", "信用风险最低资本");
        itemCodeNameMap.put("MR003_01", "风险分散效应");
        itemCodeNameMap.put("MR003_02", "控制风险最低资本");

        // 量化风险相关（备用映射）
        itemCodeNameMap.put("QR001", "量化风险最低资本");
        itemCodeNameMap.put("QR002", "量化风险最低资本（未考虑特征系数前）");
        itemCodeNameMap.put("QR003", "量化风险分散效应");

        // 未知项目
        itemCodeNameMap.put("UNKNOWN", "未知项目");

        return itemCodeNameMap;
    }

    /**
     * 根据项目名称获取项目编码
     *
     * @param itemName 项目名称
     * @return 项目编码
     */
    private String getItemCodeByName(String itemName) {
        // 从字典表中获取项目编码
        try {
            // 清理项目名称（去除前后空格、换行符等）
            String cleanItemName = itemName != null ? itemName.trim().replaceAll("\\s+", " ") : "";

            // 直接从字典服务查询
            ISysDictTypeService dictTypeService = SpringUtils.getBean(ISysDictTypeService.class);
            if (dictTypeService != null) {
                List<SysDictData> dictDataList = dictTypeService.selectDictDataByType("minc_risk_item");
                if (dictDataList != null && !dictDataList.isEmpty()) {
                    // 直接遍历字典数据，找到匹配的项目名称
                    for (SysDictData dictData : dictDataList) {
                        String dictLabel = dictData.getDictLabel() != null ? dictData.getDictLabel().trim() : "";
                        if (cleanItemName.equals(dictLabel)) {
                            String itemCode = dictData.getDictValue();
                            log.debug("找到匹配项目：{} -> {}", cleanItemName, itemCode);
                            return itemCode;
                        }
                    }
                }
            }

            // 如果没有找到，尝试使用备用映射表
            Map<String, String> backupMap = getBackupItemNameCodeMap();
            if (backupMap.containsKey(cleanItemName)) {
                String itemCode = backupMap.get(cleanItemName);
                log.debug("从备用映射表找到匹配项目：{} -> {}", cleanItemName, itemCode);
                return itemCode;
            }

            // 如果没有找到，则返回未知项目编码
            log.warn("未找到项目名称对应的项目编码：{}", cleanItemName);
            log.warn("可用的项目名称列表：");
            if (dictTypeService != null) {
                List<SysDictData> dictDataList = dictTypeService.selectDictDataByType("minc_risk_item");
                if (dictDataList != null) {
                    for (SysDictData dictData : dictDataList) {
                        log.warn("  {} -> {}", dictData.getDictLabel(), dictData.getDictValue());
                    }
                }
            }
            return "UNKNOWN";
        } catch (Exception e) {
            log.error("获取项目编码时出错：" + itemName, e);
            return "UNKNOWN";
        }
    }

    /**
     * 获取备用的项目名称到编码的映射表
     *
     * @return 备用映射表
     */
    private Map<String, String> getBackupItemNameCodeMap() {
        Map<String, String> backupMap = new HashMap<>();

        // 基础风险项目
        backupMap.put("寿险业务保险风险最低资本", "MR001_01");
        backupMap.put("非寿险业务保险风险最低资本", "MR001_02");
        backupMap.put("市场风险最低资本", "MR002_01");
        backupMap.put("信用风险最低资本", "MR002_02");
        backupMap.put("风险分散效应", "MR003_01");
        backupMap.put("控制风险最低资本", "MR003_02");

        // 量化风险相关
        backupMap.put("量化风险最低资本", "QR001");  // 使用QR001避免与QR002冲突
        backupMap.put("量化风险最低资本（未考虑特征系数前）", "QR002");
        backupMap.put("量化风险分散效应", "QR003");

        return backupMap;
    }
}
