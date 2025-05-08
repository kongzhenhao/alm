package com.xl.alm.app.service.impl;

import com.jd.lightning.common.utils.StringUtils;
import com.xl.alm.app.dto.DiscountCurveDTO;
import com.xl.alm.app.entity.DiscountCurveEntity;
import com.xl.alm.app.mapper.DiscountCurveMapper;
import com.xl.alm.app.query.DiscountCurveQuery;
import com.xl.alm.app.service.DiscountCurveService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 折现曲线表 Service 实现类
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class DiscountCurveServiceImpl implements DiscountCurveService {

    @Autowired
    private DiscountCurveMapper discountCurveMapper;

    /**
     * 查询折现曲线列表
     *
     * @param discountCurveQuery 折现曲线查询条件
     * @return 折现曲线列表
     */
    @Override
    public List<DiscountCurveDTO> selectDiscountCurveDtoList(DiscountCurveQuery discountCurveQuery) {
        List<DiscountCurveEntity> entityList = discountCurveMapper.selectDiscountCurveEntityList(discountCurveQuery);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, DiscountCurveDTO.class);
    }

    /**
     * 用id查询折现曲线
     *
     * @param id id
     * @return 折现曲线
     */
    @Override
    public DiscountCurveDTO selectDiscountCurveDtoById(Long id) {
        DiscountCurveEntity entity = discountCurveMapper.selectDiscountCurveEntityById(id);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, DiscountCurveDTO.class);
    }

    /**
     * 根据账期、曲线类型、基点类型和久期类型查询折现曲线
     *
     * @param accountPeriod 账期
     * @param curveType 曲线类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @return 折现曲线
     */
    @Override
    public DiscountCurveDTO selectDiscountCurveDtoByCondition(
            String accountPeriod,
            String curveType,
            String bpType,
            String durationType) {
        DiscountCurveEntity entity = discountCurveMapper.selectDiscountCurveEntityByCondition(
                accountPeriod, curveType, bpType, durationType);
        if (entity == null) {
            return null;
        }
        return EntityDtoConvertUtil.convertToDTO(entity, DiscountCurveDTO.class);
    }

    /**
     * 新增折现曲线数据
     *
     * @param dto 折现曲线
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertDiscountCurveDto(DiscountCurveDTO dto) {
        DiscountCurveEntity entity = EntityDtoConvertUtil.convertToEntity(dto, DiscountCurveEntity.class);
        List<DiscountCurveEntity> entityList = new ArrayList<>();
        entityList.add(entity);
        return discountCurveMapper.batchInsertDiscountCurveEntity(entityList);
    }

    /**
     * 批量插入折现曲线数据
     *
     * @param discountCurveDtoList 折现曲线列表
     * @return 影响行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertDiscountCurveDto(List<DiscountCurveDTO> discountCurveDtoList) {
        List<DiscountCurveEntity> entityList = EntityDtoConvertUtil.convertToEntityList(discountCurveDtoList, DiscountCurveEntity.class);
        return discountCurveMapper.batchInsertDiscountCurveEntity(entityList);
    }

    /**
     * 更新折现曲线数据
     *
     * @param dto 折现曲线
     * @return 结果
     */
    @Override
    public int updateDiscountCurveDto(DiscountCurveDTO dto) {
        DiscountCurveEntity entity = EntityDtoConvertUtil.convertToEntity(dto, DiscountCurveEntity.class);
        return discountCurveMapper.updateDiscountCurveEntity(entity);
    }

    /**
     * 删除指定账期的折现曲线数据
     *
     * @param accountPeriod 账期
     * @return 影响行数
     */
    @Override
    public int deleteDiscountCurveDtoByPeriod(String accountPeriod) {
        return discountCurveMapper.deleteDiscountCurveEntityByPeriod(accountPeriod);
    }

    /**
     * 删除指定id的折现曲线数据
     *
     * @param id id
     * @return 影响行数
     */
    @Override
    public int deleteDiscountCurveDtoById(Long id) {
        return discountCurveMapper.deleteDiscountCurveEntityById(id);
    }

    /**
     * 批量删除折现曲线数据
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDiscountCurveDtoByIds(Long[] ids) {
        return discountCurveMapper.deleteDiscountCurveEntityByIds(ids);
    }

    /**
     * 导入折现曲线
     *
     * @param dtoList       折现曲线数据列表
     * @param updateSupport 是否更新支持，如果已存在，是否更新
     * @param username      操作用户
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importDiscountCurveDto(List<DiscountCurveDTO> dtoList, Boolean updateSupport, String username) {
        if (StringUtils.isNull(dtoList) || dtoList.isEmpty()) {
            throw new RuntimeException("导入折现曲线不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder failureMsg = new StringBuilder();

        for (DiscountCurveDTO dto : dtoList) {
            try {
                // 验证是否存在这个折现曲线
                DiscountCurveDTO existDto = selectDiscountCurveDtoByCondition(
                        dto.getAccountPeriod(),
                        dto.getCurveType(),
                        dto.getBpType(),
                        dto.getDurationType());

                if (StringUtils.isNull(existDto)) {
                    dto.setCreateBy(username);
                    dto.setUpdateBy(username);
                    this.insertDiscountCurveDto(dto);
                    successNum++;
//                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod())
//                            .append(" 曲线类型 ").append(dto.getCurveType())
//                            .append(" 基点类型 ").append(dto.getBpType())
//                            .append(" 久期类型 ").append(dto.getDurationType())
//                            .append(" 导入成功");
                } else if (updateSupport) {
                    dto.setId(existDto.getId());
                    dto.setUpdateBy(username);
                    this.updateDiscountCurveDto(dto);
                    successNum++;
//                    successMsg.append("<br/>").append(successNum).append("、账期 ").append(dto.getAccountPeriod())
//                            .append(" 曲线类型 ").append(dto.getCurveType())
//                            .append(" 基点类型 ").append(dto.getBpType())
//                            .append(" 久期类型 ").append(dto.getDurationType())
//                            .append(" 更新成功");
                } else {
                    failureNum++;
                    if (failureNum <= 10) {
                        failureMsg.append("<br/>").append(failureNum).append("、账期 ").append(dto.getAccountPeriod())
                                .append(" 曲线类型 ").append(dto.getCurveType())
                                .append(" 基点类型 ").append(dto.getBpType())
                                .append(" 久期类型 ").append(dto.getDurationType())
                                .append(" 已存在");
                    }
                }
            } catch (Exception e) {
                failureNum++;
                if (failureNum <= 10) {
                    String msg = "<br/>" + failureNum + "、账期 " + dto.getAccountPeriod() +
                            " 曲线类型 " + dto.getCurveType() +
                            " 基点类型 " + dto.getBpType() +
                            " 久期类型 " + dto.getDurationType() +
                            " 导入失败：";
                    failureMsg.append(msg).append(e.getMessage());
                    log.error(msg, e);
                }
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new RuntimeException(failureMsg.toString());
        } else {
            return "成功导入" + successNum + "条数据";
        }
    }
}
