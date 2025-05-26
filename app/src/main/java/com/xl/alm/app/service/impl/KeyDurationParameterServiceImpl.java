package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.KeyDurationParameterDTO;
import com.xl.alm.app.entity.KeyDurationParameterEntity;
import com.xl.alm.app.mapper.KeyDurationParameterMapper;
import com.xl.alm.app.query.KeyDurationParameterQuery;
import com.xl.alm.app.service.KeyDurationParameterService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 关键久期参数表 Service 实现
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class KeyDurationParameterServiceImpl implements KeyDurationParameterService {

    @Autowired
    private KeyDurationParameterMapper keyDurationParameterMapper;

    /**
     * 查询关键久期参数列表
     *
     * @param query 关键久期参数查询条件
     * @return 关键久期参数列表
     */
    @Override
    public List<KeyDurationParameterDTO> selectKeyDurationParameterDtoList(KeyDurationParameterQuery query) {
        List<KeyDurationParameterEntity> entityList = keyDurationParameterMapper.selectKeyDurationParameterEntityList(query);
        return EntityDtoConvertUtil.convertToDTOList(entityList, KeyDurationParameterDTO.class);
    }

    /**
     * 根据ID查询关键久期参数
     *
     * @param id ID
     * @return 关键久期参数
     */
    @Override
    public KeyDurationParameterDTO selectKeyDurationParameterDtoById(Long id) {
        // 使用不包含参数值集的查询，提高性能
        KeyDurationParameterEntity entity = keyDurationParameterMapper.selectKeyDurationParameterEntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, KeyDurationParameterDTO.class);
    }

    /**
     * 根据ID查询关键久期参数（包含参数值集）
     *
     * @param id ID
     * @return 关键久期参数
     */
    @Override
    public KeyDurationParameterDTO selectKeyDurationParameterDtoWithValSetById(Long id) {
        // 使用包含参数值集的查询
        KeyDurationParameterEntity entity = keyDurationParameterMapper.selectKeyDurationParameterEntityWithValSetById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, KeyDurationParameterDTO.class);
    }

    /**
     * 根据账期和关键期限点查询关键久期参数
     *
     * @param accountPeriod 账期
     * @param keyDuration 关键期限点
     * @return 关键久期参数
     */
    @Override
    public KeyDurationParameterDTO selectKeyDurationParameterDtoByCondition(String accountPeriod, String keyDuration) {
        KeyDurationParameterEntity entity = keyDurationParameterMapper.selectKeyDurationParameterEntityByCondition(accountPeriod, keyDuration);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, KeyDurationParameterDTO.class);
    }

    /**
     * 新增关键久期参数
     *
     * @param dto 关键久期参数
     * @return 结果
     */
    @Override
    public int insertKeyDurationParameterDto(KeyDurationParameterDTO dto) {
        KeyDurationParameterEntity entity = EntityDtoConvertUtil.convertToEntity(dto, KeyDurationParameterEntity.class);
        return keyDurationParameterMapper.insertKeyDurationParameterEntity(entity);
    }

    /**
     * 批量新增关键久期参数
     *
     * @param dtoList 关键久期参数列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertKeyDurationParameterDto(List<KeyDurationParameterDTO> dtoList) {
        List<KeyDurationParameterEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, KeyDurationParameterEntity.class);
        return keyDurationParameterMapper.batchInsertKeyDurationParameterEntity(entityList);
    }

    /**
     * 修改关键久期参数
     *
     * @param dto 关键久期参数
     * @return 结果
     */
    @Override
    public int updateKeyDurationParameterDto(KeyDurationParameterDTO dto) {
        KeyDurationParameterEntity entity = EntityDtoConvertUtil.convertToEntity(dto, KeyDurationParameterEntity.class);
        return keyDurationParameterMapper.updateKeyDurationParameterEntity(entity);
    }

    /**
     * 删除关键久期参数
     *
     * @param id ID
     * @return 结果
     */
    @Override
    public int deleteKeyDurationParameterDtoById(Long id) {
        return keyDurationParameterMapper.deleteKeyDurationParameterEntityById(id);
    }

    /**
     * 批量删除关键久期参数
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteKeyDurationParameterDtoByIds(Long[] ids) {
        return keyDurationParameterMapper.deleteKeyDurationParameterEntityByIds(ids);
    }

    /**
     * 根据账期删除关键久期参数
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    @Override
    public int deleteKeyDurationParameterDtoByAccountPeriod(String accountPeriod) {
        return keyDurationParameterMapper.deleteKeyDurationParameterEntityByAccountPeriod(accountPeriod);
    }

    /**
     * 导入关键久期参数数据
     *
     * @param dtoList 关键久期参数数据列表
     * @param updateSupport 是否支持更新，如果已存在，则进行更新
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importKeyDurationParameterDto(List<KeyDurationParameterDTO> dtoList, boolean updateSupport, String operName) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入关键久期参数数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (KeyDurationParameterDTO dto : dtoList) {
            try {
                // 验证是否存在这个关键久期参数
                KeyDurationParameterDTO existDto = selectKeyDurationParameterDtoByCondition(dto.getAccountPeriod(), dto.getKeyDuration());
                if (StringUtils.isNull(existDto)) {
                    dto.setCreateBy(operName);
                    dto.setUpdateBy(operName);
                    insertKeyDurationParameterDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod())
                            .append(" 关键期限点 ").append(dto.getKeyDuration()).append(" 导入成功");
                } else if (updateSupport) {
                    dto.setId(existDto.getId());
                    dto.setUpdateBy(operName);
                    updateKeyDurationParameterDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod())
                            .append(" 关键期限点 ").append(dto.getKeyDuration()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、账期 ").append(dto.getAccountPeriod())
                            .append(" 关键期限点 ").append(dto.getKeyDuration()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账期 " + dto.getAccountPeriod() + " 关键期限点 " + dto.getKeyDuration() + " 导入失败：";
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
