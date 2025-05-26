package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.DeptMincapDetailDTO;
import com.xl.alm.app.entity.DeptMincapDetailEntity;
import com.xl.alm.app.mapper.DeptMincapDetailMapper;
import com.xl.alm.app.query.DeptMincapDetailQuery;
import com.xl.alm.app.service.DeptMincapDetailService;
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
 * 分部门最低资本明细表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class DeptMincapDetailServiceImpl implements DeptMincapDetailService {

    @Autowired
    private DeptMincapDetailMapper deptMincapDetailMapper;

    /**
     * 查询分部门最低资本明细列表
     *
     * @param query 分部门最低资本明细查询条件
     * @return 分部门最低资本明细列表
     */
    @Override
    public List<DeptMincapDetailDTO> selectDeptMincapDetailDtoList(DeptMincapDetailQuery query) {
        List<DeptMincapDetailEntity> entityList = deptMincapDetailMapper.selectDeptMincapDetailEntityList(query);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }

        // 转换为DTO列表
        List<DeptMincapDetailDTO> dtoList = new ArrayList<>();

        // 使用Map存储已处理的记录，按照部门+项目编码+账期作为键
        Map<String, DeptMincapDetailDTO> processedMap = new HashMap<>();

        // 处理每条记录
        for (DeptMincapDetailEntity entity : entityList) {
            // 构建唯一键
            String uniqueKey = entity.getAccountingPeriod() + "-" +
                              entity.getDepartment() + "-" +
                              entity.getItemCode();

            // 获取项目名称
            String itemName = getItemName(entity.getItemCode());

            // 如果已经处理过该部门+项目编码+账期的记录，则更新账户金额
            if (processedMap.containsKey(uniqueKey)) {
                DeptMincapDetailDTO existingDto = processedMap.get(uniqueKey);

                // 根据账户编码设置金额
                if ("AC001".equals(entity.getAccountCode())) {
                    existingDto.setTraditionalAmount(entity.getAmount());
                } else if ("AC002".equals(entity.getAccountCode())) {
                    existingDto.setDividendAmount(entity.getAmount());
                } else if ("AC003".equals(entity.getAccountCode())) {
                    existingDto.setUniversalAmount(entity.getAmount());
                } else if ("AC004".equals(entity.getAccountCode())) {
                    existingDto.setIndependentAmount(entity.getAmount());
                } else if ("AC005".equals(entity.getAccountCode())) {
                    existingDto.setCompanyTotalAmount(entity.getAmount());
                }
            } else {
                // 创建新的DTO
                DeptMincapDetailDTO dto = new DeptMincapDetailDTO();
                dto.setId(entity.getId());
                dto.setAccountingPeriod(entity.getAccountingPeriod());
                dto.setDepartment(entity.getDepartment());
                dto.setItemCode(entity.getItemCode());
                dto.setItemName(itemName);

                // 初始化账户金额为0
                dto.setTraditionalAmount(BigDecimal.ZERO);
                dto.setDividendAmount(BigDecimal.ZERO);
                dto.setUniversalAmount(BigDecimal.ZERO);
                dto.setIndependentAmount(BigDecimal.ZERO);
                dto.setCompanyTotalAmount(BigDecimal.ZERO);

                // 根据账户编码设置金额
                if ("AC001".equals(entity.getAccountCode())) {
                    dto.setTraditionalAmount(entity.getAmount());
                } else if ("AC002".equals(entity.getAccountCode())) {
                    dto.setDividendAmount(entity.getAmount());
                } else if ("AC003".equals(entity.getAccountCode())) {
                    dto.setUniversalAmount(entity.getAmount());
                } else if ("AC004".equals(entity.getAccountCode())) {
                    dto.setIndependentAmount(entity.getAmount());
                } else if ("AC005".equals(entity.getAccountCode())) {
                    dto.setCompanyTotalAmount(entity.getAmount());
                }

                // 添加到处理过的记录Map和结果列表
                processedMap.put(uniqueKey, dto);
                dtoList.add(dto);
            }
        }

        return dtoList;
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
        // 直接使用硬编码的映射表，避免依赖可能不存在的Bean
        Map<String, String> itemCodeNameMap = new HashMap<>();

        // 市场风险相关
        itemCodeNameMap.put("MR001", "市场风险-最低资本合计");
        itemCodeNameMap.put("MR001_01", "市场风险-利率风险最低资本");
        itemCodeNameMap.put("MR001_02", "市场风险-权益价格风险最低资本");
        itemCodeNameMap.put("MR001_03", "市场风险-房地产价格风险最低资本");
        itemCodeNameMap.put("MR001_04", "市场风险-境外固定收益类资产价格风险最低资本");
        itemCodeNameMap.put("MR001_05", "市场风险-境外权益类资产价格风险最低资本");
        itemCodeNameMap.put("MR001_06", "市场风险-汇率风险最低资本");
        itemCodeNameMap.put("MR001_07", "市场风险-风险分散效应");

        // 信用风险相关
        itemCodeNameMap.put("CR001", "信用风险-最低资本合计");
        itemCodeNameMap.put("CR001_01", "信用风险-利差风险最低资本");
        itemCodeNameMap.put("CR001_02", "信用风险-交易对手违约风险最低资本");
        itemCodeNameMap.put("CR001_03", "信用风险-风险分散效应");

        // 量化风险相关
        itemCodeNameMap.put("QR001", "量化风险最低资本");
        itemCodeNameMap.put("QR002", "量化风险最低资本（未考虑特征系数前）");
        itemCodeNameMap.put("QR003", "量化风险分散效应");

        // 寿险业务保险风险相关
        itemCodeNameMap.put("IR001", "寿险业务保险风险最低资本合计");
        itemCodeNameMap.put("IR001_01", "寿险业务保险风险-损失发生风险最低资本");
        itemCodeNameMap.put("IR001_02", "寿险业务保险风险-退保风险最低资本");
        itemCodeNameMap.put("IR001_03", "寿险业务保险风险-费用风险最低资本");
        itemCodeNameMap.put("IR001_04", "寿险业务保险风险-风险分散效应");

        // 非寿险业务保险风险相关
        itemCodeNameMap.put("NR001", "非寿险业务保险风险最低资本合计");
        itemCodeNameMap.put("NR001_01", "非寿险业务保险风险-保费及准备金风险最低资本");
        itemCodeNameMap.put("NR001_02", "非寿险业务保险风险-巨灾风险最低资本");
        itemCodeNameMap.put("NR001_03", "非寿险业务保险风险-风险分散效应");

        // 精算假设和现值情景相关
        itemCodeNameMap.put("AA001", "AA基础情景");
        itemCodeNameMap.put("PV001", "PV基础情景");
        itemCodeNameMap.put("AA002", "AA利率下降情景");
        itemCodeNameMap.put("PV002", "PV利率下降情景");

        // 未知项目
        itemCodeNameMap.put("UNKNOWN", "未知项目");

        return itemCodeNameMap;
    }

    /**
     * 查询分部门最低资本明细详情
     *
     * @param id 主键ID
     * @return 分部门最低资本明细
     */
    @Override
    public DeptMincapDetailDTO selectDeptMincapDetailDtoById(Long id) {
        DeptMincapDetailEntity entity = deptMincapDetailMapper.selectDeptMincapDetailEntityById(id);
        if (entity == null) {
            return null;
        }
        DeptMincapDetailDTO dto = EntityDtoConvertUtil.convertToDTO(entity, DeptMincapDetailDTO.class);

        // 设置项目名称
        if (dto.getItemCode() != null) {
            dto.setItemName(getItemName(dto.getItemCode()));
        }

        return dto;
    }

    /**
     * 新增分部门最低资本明细
     *
     * @param dto 分部门最低资本明细
     * @return 结果
     */
    @Override
    public int insertDeptMincapDetailDto(DeptMincapDetailDTO dto) {
        DeptMincapDetailEntity entity = EntityDtoConvertUtil.convertToEntity(dto, DeptMincapDetailEntity.class);
        return deptMincapDetailMapper.insertDeptMincapDetailEntity(entity);
    }

    /**
     * 批量新增分部门最低资本明细
     *
     * @param dtoList 分部门最低资本明细列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertDeptMincapDetailDto(List<DeptMincapDetailDTO> dtoList) {
        List<DeptMincapDetailEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, DeptMincapDetailEntity.class);
        return deptMincapDetailMapper.batchInsertDeptMincapDetailEntity(entityList);
    }

    /**
     * 修改分部门最低资本明细
     *
     * @param dto 分部门最低资本明细
     * @return 结果
     */
    @Override
    public int updateDeptMincapDetailDto(DeptMincapDetailDTO dto) {
        DeptMincapDetailEntity entity = EntityDtoConvertUtil.convertToEntity(dto, DeptMincapDetailEntity.class);
        return deptMincapDetailMapper.updateDeptMincapDetailEntity(entity);
    }

    /**
     * 删除分部门最低资本明细
     *
     * @param id 主键ID
     * @return 结果
     */
    @Override
    public int deleteDeptMincapDetailDtoById(Long id) {
        return deptMincapDetailMapper.deleteDeptMincapDetailEntityById(id);
    }

    /**
     * 根据条件删除分部门最低资本明细
     *
     * @param accountingPeriod 账期
     * @param department 部门
     * @param itemCode 项目编码
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteDeptMincapDetailDtoByCondition(String accountingPeriod, String department, String itemCode) {
        return deptMincapDetailMapper.physicalDeleteByCondition(accountingPeriod, department, itemCode);
    }

    /**
     * 批量删除分部门最低资本明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    @Override
    public int deleteDeptMincapDetailDtoByIds(Long[] ids) {
        return deptMincapDetailMapper.deleteDeptMincapDetailEntityByIds(ids);
    }

    /**
     * 根据账期删除分部门最低资本明细
     *
     * @param accountingPeriod 账期
     * @return 结果
     */
    @Override
    public int deleteDeptMincapDetailDtoByPeriod(String accountingPeriod) {
        return deptMincapDetailMapper.deleteDeptMincapDetailEntityByPeriod(accountingPeriod);
    }

    /**
     * 批量更新分部门最低资本明细（事务方法）
     *
     * @param accountingPeriod 账期
     * @param department 部门
     * @param itemCode 项目编码
     * @param dtoList 数据列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBatchDeptMincapDetailDto(String accountingPeriod, String department, String itemCode, List<DeptMincapDetailDTO> dtoList) {
        try {
            // 使用物理删除，一次性删除所有符合条件的记录
            int deletedCount = deptMincapDetailMapper.physicalDeleteDeptMincapDetailEntity(accountingPeriod, department, itemCode);
            log.info("物理删除记录数：" + deletedCount);

            // 确保所有DTO对象都设置了正确的字段值
            for (DeptMincapDetailDTO dto : dtoList) {
                dto.setAccountingPeriod(accountingPeriod);
                dto.setDepartment(department);
                dto.setItemCode(itemCode);
                dto.setIsDel(0); // 确保新记录不是删除状态
            }

            // 转换为实体对象
            List<DeptMincapDetailEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, DeptMincapDetailEntity.class);

            // 确保每个实体对象都有正确的创建者和更新者
            for (DeptMincapDetailEntity entity : entityList) {
                if (entity.getCreateBy() == null) {
                    entity.setCreateBy(entity.getUpdateBy());
                }
                if (entity.getUpdateBy() == null && entity.getCreateBy() != null) {
                    entity.setUpdateBy(entity.getCreateBy());
                }
                entity.setIsDel(0); // 确保新记录不是删除状态
            }

            // 批量添加新记录
            if (!entityList.isEmpty()) {
                try {
                    // 添加延迟，确保物理删除操作已完成
                    Thread.sleep(200);
                    return deptMincapDetailMapper.batchInsertDeptMincapDetailEntity(entityList);
                } catch (Exception e) {
                    // 如果插入失败，尝试单条插入
                    log.warn("批量插入失败，尝试单条插入", e);
                    int insertCount = 0;
                    for (DeptMincapDetailEntity entity : entityList) {
                        try {
                            insertCount += deptMincapDetailMapper.insertDeptMincapDetailEntity(entity);
                        } catch (Exception ex) {
                            log.error("单条插入失败：" + entity.getAccountCode(), ex);
                        }
                    }
                    return insertCount;
                }
            }

            return deletedCount;
        } catch (Exception e) {
            log.error("批量更新分部门最低资本明细失败", e);
            throw new RuntimeException("批量更新失败：" + e.getMessage(), e); // 抛出异常，触发事务回滚
        }
    }

    /**
     * 导入分部门最低资本明细
     *
     * @param dtoList 分部门最低资本明细数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importDeptMincapDetailDto(List<DeptMincapDetailDTO> dtoList, Boolean updateSupport, String username) {
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

        // 账户编码映射
        Map<String, String> accountCodeMap = new HashMap<>();
        accountCodeMap.put("traditionalAmount", "AC001"); // 传统保险账户
        accountCodeMap.put("dividendAmount", "AC002");    // 分红保险账户
        accountCodeMap.put("universalAmount", "AC003");   // 万能保险账户
        accountCodeMap.put("independentAmount", "AC004"); // 独立账户
        accountCodeMap.put("companyTotalAmount", "AC005"); // 资本补充债账户

        List<DeptMincapDetailEntity> entityList = new ArrayList<>();

        for (DeptMincapDetailDTO dto : dtoList) {
            try {
                // 检查必要字段
                if (StringUtils.isEmpty(dto.getDepartment()) || StringUtils.isEmpty(dto.getItemName())) {
                    throw new RuntimeException("部门名称或项目名称不能为空");
                }

                // 根据项目名称获取项目编码
                String itemCode = getItemCodeByName(dto.getItemName());
                if (StringUtils.isEmpty(itemCode)) {
                    throw new RuntimeException("无法找到项目编码：" + dto.getItemName());
                }

                // 处理各账户类型的数据
                for (Map.Entry<String, String> entry : accountCodeMap.entrySet()) {
                    String fieldName = entry.getKey();
                    String accountCode = entry.getValue();

                    // 获取字段值（不使用反射，直接根据字段名获取）
                    BigDecimal amount = null;
                    try {
                        if ("traditionalAmount".equals(fieldName)) {
                            amount = dto.getTraditionalAmount();
                        } else if ("dividendAmount".equals(fieldName)) {
                            amount = dto.getDividendAmount();
                        } else if ("universalAmount".equals(fieldName)) {
                            amount = dto.getUniversalAmount();
                        } else if ("independentAmount".equals(fieldName)) {
                            amount = dto.getIndependentAmount();
                        } else if ("companyTotalAmount".equals(fieldName)) {
                            amount = dto.getCompanyTotalAmount();
                        }
                    } catch (Exception e) {
                        log.error("获取字段值失败：" + fieldName, e);
                        continue;
                    }

                    // 如果金额为空，则设置为0
                    if (amount == null) {
                        amount = BigDecimal.ZERO;
                    }
                    // 不再跳过金额为0的记录

                    // 处理金额，确保不超出数据库字段范围
                    // 数据库字段为decimal(25,10)，最多支持15位整数和10位小数
                    try {
                        // 设置精度为10位小数
                        amount = amount.setScale(10, BigDecimal.ROUND_HALF_UP);

                        // 检查整数部分是否超过15位
                        BigDecimal absAmount = amount.abs();
                        String amountStr = absAmount.toPlainString();
                        int decimalPointIndex = amountStr.indexOf('.');
                        String integerPart = decimalPointIndex > 0 ? amountStr.substring(0, decimalPointIndex) : amountStr;

                        if (integerPart.length() > 15) {
                            log.warn("金额超出范围，将被截断：" + amount);
                            // 截断为最大值或最小值
                            BigDecimal maxValue = new BigDecimal("999999999999999.9999999999");
                            BigDecimal minValue = new BigDecimal("-999999999999999.9999999999");
                            amount = amount.compareTo(BigDecimal.ZERO) > 0 ? maxValue : minValue;
                        }
                    } catch (Exception e) {
                        log.error("处理金额时出错：" + amount, e);
                        continue;
                    }

                    // 创建实体对象
                    DeptMincapDetailEntity entity = new DeptMincapDetailEntity();
                    // 使用导入数据中的账期，如果为空则使用当前账期
                    entity.setAccountingPeriod(StringUtils.isEmpty(dto.getAccountingPeriod()) ? currentPeriod : dto.getAccountingPeriod());
                    entity.setDepartment(dto.getDepartment());
                    entity.setItemCode(itemCode);
                    entity.setAccountCode(accountCode);
                    entity.setAmount(amount);
                    entity.setCreateBy(username);
                    entity.setUpdateBy(username);

                    entityList.add(entity);
                }

                successNum++;
                successMsg.append("<br/>").append(successNum).append("、部门 ").append(dto.getDepartment())
                        .append(" 项目 ").append(dto.getItemName()).append(" 导入成功");
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、部门 " + dto.getDepartment() + " 项目 " + dto.getItemName() + " 导入失败：";
                failureMsg.append(msg).append(e.getMessage());
                log.error(msg, e);
            }
        }

        // 处理数据，避免重复插入
        if (!entityList.isEmpty()) {
            // 按照唯一键分组，避免重复插入
            Map<String, DeptMincapDetailEntity> uniqueMap = new HashMap<>();

            for (DeptMincapDetailEntity entity : entityList) {
                // 构建唯一键
                String uniqueKey = entity.getAccountingPeriod() + "-" +
                                  entity.getDepartment() + "-" +
                                  entity.getItemCode() + "-" +
                                  entity.getAccountCode();

                // 如果已存在相同的唯一键，则累加金额
                if (uniqueMap.containsKey(uniqueKey)) {
                    DeptMincapDetailEntity existingEntity = uniqueMap.get(uniqueKey);
                    BigDecimal newAmount = existingEntity.getAmount().add(entity.getAmount());
                    existingEntity.setAmount(newAmount);
                } else {
                    uniqueMap.put(uniqueKey, entity);
                }
            }

            // 转换为列表
            List<DeptMincapDetailEntity> uniqueEntityList = new ArrayList<>(uniqueMap.values());

            // 查询已存在的记录
            for (DeptMincapDetailEntity entity : uniqueEntityList) {
                // 构建查询条件
                DeptMincapDetailQuery query = new DeptMincapDetailQuery();
                query.setAccountingPeriod(entity.getAccountingPeriod());
                query.setDepartment(entity.getDepartment());
                query.setItemCode(entity.getItemCode());
                query.setAccountCode(entity.getAccountCode());

                List<DeptMincapDetailEntity> existingList = deptMincapDetailMapper.selectDeptMincapDetailEntityList(query);

                if (!existingList.isEmpty()) {
                    // 已存在记录
                    if (updateSupport) {
                        // 如果支持更新，则执行更新
                        DeptMincapDetailEntity existingEntity = existingList.get(0);
                        existingEntity.setAmount(entity.getAmount());
                        existingEntity.setUpdateBy(entity.getUpdateBy());
                        deptMincapDetailMapper.updateDeptMincapDetailEntity(existingEntity);
                        log.info("更新记录：" + entity.getDepartment() + "-" + entity.getItemCode() + "-" + entity.getAccountCode());
                    } else {
                        // 如果不支持更新，则跳过
                        log.info("跳过已存在记录：" + entity.getDepartment() + "-" + entity.getItemCode() + "-" + entity.getAccountCode());
                    }
                } else {
                    // 不存在记录，执行插入
                    deptMincapDetailMapper.insertDeptMincapDetailEntity(entity);
                    log.info("插入新记录：" + entity.getDepartment() + "-" + entity.getItemCode() + "-" + entity.getAccountCode());
                }
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

    /**
     * 根据项目名称获取项目编码
     *
     * @param itemName 项目名称
     * @return 项目编码
     */
    private String getItemCodeByName(String itemName) {
        // 从字典表中获取项目编码
        try {
            // 获取项目编码和名称的映射表
            Map<String, String> itemCodeNameMap = getItemCodeNameMap();

            // 反转映射表，使用项目名称作为键，项目编码作为值
            Map<String, String> itemNameCodeMap = new HashMap<>();
            for (Map.Entry<String, String> entry : itemCodeNameMap.entrySet()) {
                itemNameCodeMap.put(entry.getValue(), entry.getKey());
            }

            // 精确匹配
            if (itemNameCodeMap.containsKey(itemName)) {
                return itemNameCodeMap.get(itemName);
            }

            // 特殊项目名称处理
            if (itemName.equals("AA基础情景")) {
                return "AA001";
            } else if (itemName.equals("PV基础情景")) {
                return "PV001";
            } else if (itemName.equals("AA利率下降情景")) {
                return "AA002";
            } else if (itemName.equals("PV利率下降情景")) {
                return "PV002";
            }

            // 模糊匹配 - 先尝试完全包含关系
            for (Map.Entry<String, String> entry : itemNameCodeMap.entrySet()) {
                String dictLabel = entry.getKey();
                if (itemName.equals(dictLabel) || itemName.contains(dictLabel) || dictLabel.contains(itemName)) {
                    return entry.getValue();
                }
            }

            // 更宽松的模糊匹配 - 尝试关键词匹配
            Map<String, String> keywordMap = new HashMap<>();
            keywordMap.put("权益价格", "MR001_02");
            keywordMap.put("房地产价格", "MR001_03");
            keywordMap.put("境外固定收益", "MR001_04");
            keywordMap.put("境外权益", "MR001_05");
            keywordMap.put("汇率", "MR001_06");
            keywordMap.put("利差", "CR001_01");
            keywordMap.put("交易对手违约", "CR001_02");
            keywordMap.put("利率风险", "MR001_01");

            for (Map.Entry<String, String> entry : keywordMap.entrySet()) {
                if (itemName.contains(entry.getKey())) {
                    return entry.getValue();
                }
            }

            // 如果没有找到，则返回UNKNOWN
            log.warn("未找到项目名称对应的项目编码：" + itemName);
            return "UNKNOWN";
        } catch (Exception e) {
            log.error("获取项目编码时出错：" + itemName, e);
            return "UNKNOWN";
        }
    }
}
