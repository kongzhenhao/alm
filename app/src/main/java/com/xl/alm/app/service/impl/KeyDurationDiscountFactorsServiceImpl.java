package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.KeyDurationDiscountFactorsDTO;
import com.xl.alm.app.entity.KeyDurationDiscountFactorsEntity;
import com.xl.alm.app.mapper.KeyDurationDiscountFactorsMapper;
import com.xl.alm.app.query.KeyDurationDiscountFactorsQuery;
import com.xl.alm.app.service.KeyDurationDiscountFactorsService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 关键久期折现因子表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class KeyDurationDiscountFactorsServiceImpl implements KeyDurationDiscountFactorsService {

    @Autowired
    private KeyDurationDiscountFactorsMapper keyDurationDiscountFactorsMapper;

    /**
     * 查询关键久期折现因子列表
     *
     * @param query 关键久期折现因子查询条件
     * @return 关键久期折现因子列表
     */
    @Override
    public List<KeyDurationDiscountFactorsDTO> selectKeyDurationDiscountFactorsDtoList(KeyDurationDiscountFactorsQuery query) {
        List<KeyDurationDiscountFactorsEntity> entityList = keyDurationDiscountFactorsMapper.selectKeyDurationDiscountFactorsEntityList(query);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, KeyDurationDiscountFactorsDTO.class);
    }

    /**
     * 根据ID查询关键久期折现因子
     *
     * @param id ID
     * @return 关键久期折现因子
     */
    @Override
    public KeyDurationDiscountFactorsDTO selectKeyDurationDiscountFactorsDtoById(Long id) {
        KeyDurationDiscountFactorsEntity entity = keyDurationDiscountFactorsMapper.selectKeyDurationDiscountFactorsEntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, KeyDurationDiscountFactorsDTO.class);
    }

    /**
     * 根据条件查询关键久期折现因子
     *
     * @param accountPeriod   账期
     * @param curveType       曲线类型
     * @param keyDuration     关键期限点
     * @param stressDirection 压力方向
     * @param durationType    久期类型
     * @return 关键久期折现因子
     */
    @Override
    public KeyDurationDiscountFactorsDTO selectKeyDurationDiscountFactorsDtoByCondition(
            String accountPeriod,
            String curveType,
            String keyDuration,
            String stressDirection,
            String durationType) {
        KeyDurationDiscountFactorsEntity entity = keyDurationDiscountFactorsMapper.selectKeyDurationDiscountFactorsEntityByCondition(
                accountPeriod, curveType, keyDuration, stressDirection, durationType);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, KeyDurationDiscountFactorsDTO.class);
    }

    /**
     * 新增关键久期折现因子
     *
     * @param dto 关键久期折现因子
     * @return 结果
     */
    @Override
    public int insertKeyDurationDiscountFactorsDto(KeyDurationDiscountFactorsDTO dto) {
        KeyDurationDiscountFactorsEntity entity = EntityDtoConvertUtil.convertToEntity(dto, KeyDurationDiscountFactorsEntity.class);
        return keyDurationDiscountFactorsMapper.insertKeyDurationDiscountFactorsEntity(entity);
    }

    /**
     * 批量新增关键久期折现因子
     *
     * @param dtoList 关键久期折现因子列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertKeyDurationDiscountFactorsDto(List<KeyDurationDiscountFactorsDTO> dtoList) {
        List<KeyDurationDiscountFactorsEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, KeyDurationDiscountFactorsEntity.class);
        return keyDurationDiscountFactorsMapper.batchInsertKeyDurationDiscountFactorsEntity(entityList);
    }

    /**
     * 修改关键久期折现因子
     *
     * @param dto 关键久期折现因子
     * @return 结果
     */
    @Override
    public int updateKeyDurationDiscountFactorsDto(KeyDurationDiscountFactorsDTO dto) {
        KeyDurationDiscountFactorsEntity entity = EntityDtoConvertUtil.convertToEntity(dto, KeyDurationDiscountFactorsEntity.class);
        return keyDurationDiscountFactorsMapper.updateKeyDurationDiscountFactorsEntity(entity);
    }

    /**
     * 删除关键久期折现因子
     *
     * @param id 关键久期折现因子ID
     * @return 结果
     */
    @Override
    public int deleteKeyDurationDiscountFactorsDtoById(Long id) {
        return keyDurationDiscountFactorsMapper.deleteKeyDurationDiscountFactorsEntityById(id);
    }

    /**
     * 批量删除关键久期折现因子
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteKeyDurationDiscountFactorsDtoByIds(Long[] ids) {
        return keyDurationDiscountFactorsMapper.deleteKeyDurationDiscountFactorsEntityByIds(ids);
    }

    /**
     * 删除指定账期的关键久期折现因子
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    @Override
    public int deleteKeyDurationDiscountFactorsDtoByPeriod(String accountPeriod) {
        return keyDurationDiscountFactorsMapper.deleteKeyDurationDiscountFactorsEntityByPeriod(accountPeriod);
    }

    /**
     * 导入关键久期折现因子
     *
     * @param dtoList       关键久期折现因子数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username      操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importKeyDurationDiscountFactorsDto(List<KeyDurationDiscountFactorsDTO> dtoList, Boolean updateSupport, String username) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (KeyDurationDiscountFactorsDTO dto : dtoList) {
            try {
                // 设置创建者和更新者
                dto.setCreateBy(username);
                dto.setUpdateBy(username);

                // 验证是否存在相同的记录
                KeyDurationDiscountFactorsDTO existDto = selectKeyDurationDiscountFactorsDtoByCondition(
                        dto.getAccountPeriod(), dto.getCurveType(), dto.getKeyDuration(),
                        dto.getStressDirection(), dto.getDurationType());

                if (StringUtils.isNull(existDto)) {
                    // 不存在，执行新增
                    insertKeyDurationDiscountFactorsDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod())
                            .append(" 导入成功");
                } else if (updateSupport) {
                    // 存在且允许更新，执行更新
                    dto.setId(existDto.getId());
                    updateKeyDurationDiscountFactorsDto(dto);
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
