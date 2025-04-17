package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.SampleDTO;
import com.xl.alm.app.entity.SampleEntity;
import com.xl.alm.app.mapper.SampleMapper;
import com.xl.alm.app.query.SampleQuery;
import com.xl.alm.app.service.SampleService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 折现因子表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class SampleServiceImpl implements SampleService {

    @Autowired
    private SampleMapper sampleMapper;

    /**
     * 查询折现因子列表
     *
     * @param sampleQuery 折现因子查询条件
     * @return 折现因子列表
     */
    @Override
    public List<SampleDTO> selectSampleDtoList(SampleQuery sampleQuery) {
        List<SampleEntity> entityList = sampleMapper.selectSampleEntityList(sampleQuery);
        if (entityList == null || entityList.isEmpty()) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, SampleDTO.class);
    }

    /**
     * 用id查询折现因子列表
     *
     * @param id id
     * @return 折现因子
     */
    @Override
    public SampleDTO selectSampleDtoById(Long id) {
        SampleEntity entity = sampleMapper.selectSampleEntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, SampleDTO.class);
    }

    /**
     * 根据账期、因子类型和久期类型查询折现因子
     *
     * @param accountPeriod 账期
     * @param factorType    因子类型
     * @param durationType  久期类型
     * @param designType    设计类型
     * @param isShortTerm   是否中短期险种
     * @return 折现因子
     */
    @Override
    public SampleDTO selectSampleDtoByCondition(
            String accountPeriod,
            String factorType,
            String durationType,
            String designType,
            String isShortTerm) {
        SampleEntity entity = sampleMapper.selectSampleEntityByCondition(
                accountPeriod,
                factorType,
                durationType,
                designType,
                isShortTerm);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, SampleDTO.class);
    }

    /**
     * 批量插入折现因子数据
     *
     * @param sampleDtoList 折现因子列表
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertSampleDto(List<SampleDTO> sampleDtoList) {
        List<SampleEntity> entityList = EntityDtoConvertUtil.convertToEntityList(sampleDtoList, SampleEntity.class);
        return sampleMapper.batchInsertSampleEntity(entityList);
    }

    /**
     * 更新折现因子数据
     *
     * @param dto 折现因子
     * @return 结果
     */
    @Override
    public int updateSampleDto(SampleDTO dto) {
        SampleEntity entity = EntityDtoConvertUtil.convertToEntity(dto, SampleEntity.class);
        return sampleMapper.updateSampleEntity(entity);
    }

    /**
     * 删除指定账期的折现因子数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSampleDtoByPeriod(String accountPeriod) {
        return sampleMapper.deleteSampleEntityByPeriod(accountPeriod);
    }

    /**
     * 删除指定id的折现因子数据
     *
     * @param id id
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSampleDtoById(Long id) {
        return sampleMapper.deleteSampleEntityById(id);
    }

    /**
     * 导入折现因子
     *
     * @param dtoList       折现因子数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username      操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importSampleDto(List<SampleDTO> dtoList, Boolean updateSupport, String username) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入折现因子不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (SampleDTO dto : dtoList) {
            try {
                // 验证是否存在这个折现因子
                SampleDTO existDto = selectSampleDtoByCondition(
                        dto.getAccountPeriod(),
                        dto.getFactorType(),
                        dto.getDurationType(),
                        dto.getDesignType(),
                        dto.getIsShortTerm());

                if (StringUtils.isNull(existDto)) {
                    dto.setCreateBy(username);
                    dto.setUpdateBy(username);
                    List<SampleDTO> list = new ArrayList<>();
                    list.add(dto);
                    batchInsertSampleDto(list);
                    successNum++;
                } else if (updateSupport) {
                    dto.setId(existDto.getId());
                    dto.setUpdateBy(username);
                    updateSampleDto(dto);
                    successNum++;
                } else {
                    failureNum++;
                    if (failureNum <= 10) {
                        failureMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod())
                                .append(" 因子类型 ").append(dto.getFactorType())
                                .append(" 久期类型 ").append(dto.getDurationType())
                                .append(" 设计类型 ").append(dto.getDesignType())
                                .append(" 是否中短期险种 ").append(dto.getIsShortTerm()).append(" 已存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                if (failureNum <= 10) {
                    failureMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod())
                            .append(" 因子类型 ").append(dto.getFactorType())
                            .append(" 久期类型 ").append(dto.getDurationType())
                            .append(" 设计类型 ").append(dto.getDesignType())
                            .append(" 是否中短期险种 ").append(dto.getIsShortTerm()).append(" 数据处理异常");
                }
                log.error("导入折现因子数据异常", e);
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
