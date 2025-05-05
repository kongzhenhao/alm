package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.DiscountFactorDTO;
import com.xl.alm.app.entity.DiscountFactorEntity;
import com.xl.alm.app.mapper.DiscountFactorMapper;
import com.xl.alm.app.query.DiscountFactorQuery;
import com.xl.alm.app.service.DiscountFactorService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 折现因子表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class DiscountFactorServiceImpl implements DiscountFactorService {

    @Autowired
    private DiscountFactorMapper discountFactorMapper;

    /**
     * 查询折现因子列表
     *
     * @param discountFactorQuery 折现因子查询条件
     * @return 折现因子列表
     */
    @Override
    public List<DiscountFactorDTO> selectDiscountFactorDtoList(DiscountFactorQuery discountFactorQuery) {
        List<DiscountFactorEntity> entityList = discountFactorMapper.selectDiscountFactorEntityList(discountFactorQuery);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, DiscountFactorDTO.class);
    }

    /**
     * 用id查询折现因子列表
     *
     * @param id id
     * @return 折现因子
     */
    @Override
    public DiscountFactorDTO selectDiscountFactorDtoById(Long id) {
        DiscountFactorEntity entity = discountFactorMapper.selectDiscountFactorEntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, DiscountFactorDTO.class);
    }

    /**
     * 根据账期、因子类型、基点类型和久期类型查询折现因子
     *
     * @param accountPeriod 账期
     * @param factorType 因子类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @return 折现因子
     */
    @Override
    public DiscountFactorDTO selectDiscountFactorDtoByCondition(
            String accountPeriod,
            String factorType,
            String bpType,
            String durationType) {
        DiscountFactorEntity entity = discountFactorMapper.selectDiscountFactorEntityByCondition(
                accountPeriod,
                factorType,
                bpType,
                durationType);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, DiscountFactorDTO.class);
    }

    /**
     * 新增折现因子
     *
     * @param discountFactorDto 折现因子
     * @return 结果
     */
    @Override
    public int insertDiscountFactorDto(DiscountFactorDTO discountFactorDto) {
        DiscountFactorEntity entity = EntityDtoConvertUtil.convertToEntity(discountFactorDto, DiscountFactorEntity.class);
        return discountFactorMapper.insertDiscountFactorEntity(entity);
    }

    /**
     * 批量插入折现因子数据
     *
     * @param discountFactorDtoList 折现因子列表
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertDiscountFactorDto(List<DiscountFactorDTO> discountFactorDtoList) {
        List<DiscountFactorEntity> entityList = EntityDtoConvertUtil.convertToEntityList(discountFactorDtoList, DiscountFactorEntity.class);
        return discountFactorMapper.batchInsertDiscountFactorEntity(entityList);
    }

    /**
     * 修改折现因子
     *
     * @param discountFactorDto 折现因子
     * @return 结果
     */
    @Override
    public int updateDiscountFactorDto(DiscountFactorDTO discountFactorDto) {
        DiscountFactorEntity entity = EntityDtoConvertUtil.convertToEntity(discountFactorDto, DiscountFactorEntity.class);
        return discountFactorMapper.updateDiscountFactorEntity(entity);
    }

    /**
     * 批量删除折现因子
     *
     * @param ids 需要删除的折现因子主键集合
     * @return 结果
     */
    @Override
    public int deleteDiscountFactorDtoByIds(Long[] ids) {
        return discountFactorMapper.deleteDiscountFactorEntityByIds(ids);
    }

    /**
     * 删除折现因子信息
     *
     * @param id 折现因子主键
     * @return 结果
     */
    @Override
    public int deleteDiscountFactorDtoById(Long id) {
        return discountFactorMapper.deleteDiscountFactorEntityById(id);
    }

    /**
     * 删除指定账期的折现因子数据
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    @Override
    public int deleteDiscountFactorDtoByPeriod(String accountPeriod) {
        return discountFactorMapper.deleteDiscountFactorEntityByPeriod(accountPeriod);
    }

    /**
     * 导入折现因子
     *
     * @param dtoList 折现因子数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username 操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importDiscountFactorDto(List<DiscountFactorDTO> dtoList, Boolean updateSupport, String username) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入折现因子不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (DiscountFactorDTO dto : dtoList) {
            try {
                // 验证是否存在这个折现因子
                DiscountFactorDTO existDto = selectDiscountFactorDtoByCondition(
                        dto.getAccountPeriod(),
                        dto.getFactorType(),
                        dto.getBpType(),
                        dto.getDurationType());
                
                if (StringUtils.isNull(existDto)) {
                    dto.setCreateBy(username);
                    dto.setUpdateBy(username);
                    this.insertDiscountFactorDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod()).append(" 导入成功");
                } else if (updateSupport) {
                    dto.setId(existDto.getId());
                    dto.setUpdateBy(username);
                    this.updateDiscountFactorDto(dto);
                    successNum++;
                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod()).append(" 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、账期 ").append(dto.getAccountPeriod()).append(" 已存在");
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
