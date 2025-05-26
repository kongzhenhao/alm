package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.KeyDurationDiscountCurveDTO;
import com.xl.alm.app.entity.KeyDurationDiscountCurveEntity;
import com.xl.alm.app.mapper.KeyDurationDiscountCurveMapper;
import com.xl.alm.app.query.KeyDurationDiscountCurveQuery;
import com.xl.alm.app.service.KeyDurationDiscountCurveService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 关键久期折现曲线表 Service 实现
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class KeyDurationDiscountCurveServiceImpl implements KeyDurationDiscountCurveService {

    @Autowired
    private KeyDurationDiscountCurveMapper keyDurationDiscountCurveMapper;

    /**
     * 查询关键久期折现曲线列表
     *
     * @param query 关键久期折现曲线查询条件
     * @return 关键久期折现曲线列表
     */
    @Override
    public List<KeyDurationDiscountCurveDTO> selectKeyDurationDiscountCurveDtoList(KeyDurationDiscountCurveQuery query) {
        List<KeyDurationDiscountCurveEntity> entityList = keyDurationDiscountCurveMapper.selectKeyDurationDiscountCurveEntityList(query);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, KeyDurationDiscountCurveDTO.class);
    }

    /**
     * 根据ID查询关键久期折现曲线
     *
     * @param id ID
     * @return 关键久期折现曲线
     */
    @Override
    public KeyDurationDiscountCurveDTO selectKeyDurationDiscountCurveDtoById(Long id) {
        KeyDurationDiscountCurveEntity entity = keyDurationDiscountCurveMapper.selectKeyDurationDiscountCurveEntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, KeyDurationDiscountCurveDTO.class);
    }

    /**
     * 根据条件查询关键久期折现曲线
     *
     * @param accountPeriod 账期
     * @param curveType 曲线类型
     * @param keyDuration 关键期限点
     * @param stressDirection 压力方向
     * @param durationType 久期类型
     * @return 关键久期折现曲线
     */
    @Override
    public KeyDurationDiscountCurveDTO selectKeyDurationDiscountCurveDtoByCondition(
            String accountPeriod,
            String curveType,
            String keyDuration,
            String stressDirection,
            String durationType) {
        KeyDurationDiscountCurveEntity entity = keyDurationDiscountCurveMapper.selectKeyDurationDiscountCurveEntityByCondition(
                accountPeriod, curveType, keyDuration, stressDirection, durationType);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, KeyDurationDiscountCurveDTO.class);
    }

    /**
     * 新增关键久期折现曲线
     *
     * @param dto 关键久期折现曲线
     * @return 结果
     */
    @Override
    public int insertKeyDurationDiscountCurveDto(KeyDurationDiscountCurveDTO dto) {
        KeyDurationDiscountCurveEntity entity = EntityDtoConvertUtil.convertToEntity(dto, KeyDurationDiscountCurveEntity.class);
        return keyDurationDiscountCurveMapper.insertKeyDurationDiscountCurveEntity(entity);
    }

    /**
     * 批量新增关键久期折现曲线
     *
     * @param dtoList 关键久期折现曲线列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertKeyDurationDiscountCurveDto(List<KeyDurationDiscountCurveDTO> dtoList) {
        List<KeyDurationDiscountCurveEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, KeyDurationDiscountCurveEntity.class);
        return keyDurationDiscountCurveMapper.batchInsertKeyDurationDiscountCurveEntity(entityList);
    }

    /**
     * 修改关键久期折现曲线
     *
     * @param dto 关键久期折现曲线
     * @return 结果
     */
    @Override
    public int updateKeyDurationDiscountCurveDto(KeyDurationDiscountCurveDTO dto) {
        KeyDurationDiscountCurveEntity entity = EntityDtoConvertUtil.convertToEntity(dto, KeyDurationDiscountCurveEntity.class);
        return keyDurationDiscountCurveMapper.updateKeyDurationDiscountCurveEntity(entity);
    }

    /**
     * 删除关键久期折现曲线
     *
     * @param id ID
     * @return 结果
     */
    @Override
    public int deleteKeyDurationDiscountCurveDtoById(Long id) {
        return keyDurationDiscountCurveMapper.deleteKeyDurationDiscountCurveEntityById(id);
    }

    /**
     * 批量删除关键久期折现曲线
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteKeyDurationDiscountCurveDtoByIds(Long[] ids) {
        return keyDurationDiscountCurveMapper.deleteKeyDurationDiscountCurveEntityByIds(ids);
    }

    /**
     * 根据账期删除关键久期折现曲线
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    @Override
    public int deleteKeyDurationDiscountCurveDtoByAccountPeriod(String accountPeriod) {
        return keyDurationDiscountCurveMapper.deleteKeyDurationDiscountCurveEntityByAccountPeriod(accountPeriod);
    }

    /**
     * 导入关键久期折现曲线数据
     *
     * @param dtoList 关键久期折现曲线数据列表
     * @param updateSupport 是否支持更新，如果已存在，则进行更新
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importKeyDurationDiscountCurveDto(List<KeyDurationDiscountCurveDTO> dtoList, boolean updateSupport, String operName) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入关键久期折现曲线数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (KeyDurationDiscountCurveDTO dto : dtoList) {
            try {
                // 验证是否存在这个关键久期折现曲线
                KeyDurationDiscountCurveDTO existDto = selectKeyDurationDiscountCurveDtoByCondition(
                        dto.getAccountPeriod(), dto.getCurveType(), dto.getKeyDuration(), 
                        dto.getStressDirection(), dto.getDurationType());
                if (StringUtils.isNull(existDto)) {
                    dto.setCreateBy(operName);
                    dto.setUpdateBy(operName);
                    insertKeyDurationDiscountCurveDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod())
                            .append(" 曲线类型 ").append(dto.getCurveType())
                            .append(" 关键期限点 ").append(dto.getKeyDuration())
                            .append(" 压力方向 ").append(dto.getStressDirection())
                            .append(" 久期类型 ").append(dto.getDurationType())
                            .append(" 导入成功");
                } else if (updateSupport) {
                    dto.setId(existDto.getId());
                    dto.setUpdateBy(operName);
                    updateKeyDurationDiscountCurveDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod())
                            .append(" 曲线类型 ").append(dto.getCurveType())
                            .append(" 关键期限点 ").append(dto.getKeyDuration())
                            .append(" 压力方向 ").append(dto.getStressDirection())
                            .append(" 久期类型 ").append(dto.getDurationType())
                            .append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、账期 ").append(dto.getAccountPeriod())
                            .append(" 曲线类型 ").append(dto.getCurveType())
                            .append(" 关键期限点 ").append(dto.getKeyDuration())
                            .append(" 压力方向 ").append(dto.getStressDirection())
                            .append(" 久期类型 ").append(dto.getDurationType())
                            .append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账期 " + dto.getAccountPeriod() + 
                        " 曲线类型 " + dto.getCurveType() + 
                        " 关键期限点 " + dto.getKeyDuration() + 
                        " 压力方向 " + dto.getStressDirection() + 
                        " 久期类型 " + dto.getDurationType() + 
                        " 导入失败：";
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
