package com.xl.alm.app.service.impl;

import com.xl.alm.app.dto.IrHedgeRatioDTO;
import com.xl.alm.app.entity.IrHedgeRatioEntity;
import com.xl.alm.app.mapper.IrHedgeRatioMapper;
import com.xl.alm.app.query.IrHedgeRatioQuery;
import com.xl.alm.app.service.IrHedgeRatioService;
import com.xl.alm.app.util.EntityDtoConvertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 利率风险对冲率表Service业务层处理
 *
 * @author AI Assistant
 */
@Slf4j
@Service
public class IrHedgeRatioServiceImpl implements IrHedgeRatioService {

    @Autowired
    private IrHedgeRatioMapper irHedgeRatioMapper;

    /**
     * 查询利率风险对冲率表列表
     *
     * @param query 查询条件
     * @return 利率风险对冲率表集合
     */
    @Override
    public List<IrHedgeRatioDTO> selectIrHedgeRatioDtoList(IrHedgeRatioQuery query) {
        List<IrHedgeRatioEntity> entityList = irHedgeRatioMapper.selectIrHedgeRatioEntityList(query);
        if (entityList == null || entityList.isEmpty()) {
            return new ArrayList<>();
        }
        return EntityDtoConvertUtil.convertToDTOList(entityList, IrHedgeRatioDTO.class);
    }

    /**
     * 查询利率风险对冲率表详情
     *
     * @param id 利率风险对冲率表主键
     * @return 利率风险对冲率表
     */
    @Override
    public IrHedgeRatioDTO selectIrHedgeRatioDtoById(Long id) {
        IrHedgeRatioEntity entity = irHedgeRatioMapper.selectIrHedgeRatioEntityById(id);
        return EntityDtoConvertUtil.convertToDTO(entity, IrHedgeRatioDTO.class);
    }

    /**
     * 根据唯一键查询利率风险对冲率表详情
     *
     * @param accountingPeriod 账期
     * @param itemName 项目名称
     * @param accountCode 账户编码
     * @return 利率风险对冲率表
     */
    @Override
    public IrHedgeRatioDTO selectIrHedgeRatioDtoByUniqueKey(String accountingPeriod, String itemName, String accountCode) {
        IrHedgeRatioEntity entity = irHedgeRatioMapper.selectIrHedgeRatioEntityByUniqueKey(accountingPeriod, itemName, accountCode);
        return EntityDtoConvertUtil.convertToDTO(entity, IrHedgeRatioDTO.class);
    }

    /**
     * 新增利率风险对冲率表
     *
     * @param dto 利率风险对冲率表
     * @return 结果
     */
    @Override
    @Transactional
    public int insertIrHedgeRatioDto(IrHedgeRatioDTO dto) {
        IrHedgeRatioEntity entity = EntityDtoConvertUtil.convertToEntity(dto, IrHedgeRatioEntity.class);
        entity.setIsDel(0);
        return irHedgeRatioMapper.insertIrHedgeRatioEntity(entity);
    }

    /**
     * 批量新增利率风险对冲率表
     *
     * @param dtoList 利率风险对冲率表列表
     * @return 结果
     */
    @Override
    @Transactional
    public int batchInsertIrHedgeRatioDto(List<IrHedgeRatioDTO> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return 0;
        }
        List<IrHedgeRatioEntity> entityList = EntityDtoConvertUtil.convertToEntityList(dtoList, IrHedgeRatioEntity.class);
        for (IrHedgeRatioEntity entity : entityList) {
            entity.setIsDel(0);
        }
        return irHedgeRatioMapper.batchInsertIrHedgeRatioEntity(entityList);
    }

    /**
     * 修改利率风险对冲率表
     *
     * @param dto 利率风险对冲率表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateIrHedgeRatioDto(IrHedgeRatioDTO dto) {
        IrHedgeRatioEntity entity = EntityDtoConvertUtil.convertToEntity(dto, IrHedgeRatioEntity.class);
        return irHedgeRatioMapper.updateIrHedgeRatioEntity(entity);
    }

    /**
     * 删除利率风险对冲率表
     *
     * @param id 利率风险对冲率表主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteIrHedgeRatioDtoById(Long id) {
        return irHedgeRatioMapper.deleteIrHedgeRatioEntityById(id);
    }

    /**
     * 批量删除利率风险对冲率表
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteIrHedgeRatioDtoByIds(Long[] ids) {
        return irHedgeRatioMapper.deleteIrHedgeRatioEntityByIds(ids);
    }

    /**
     * 根据账期删除利率风险对冲率表
     *
     * @param accountingPeriod 账期
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteIrHedgeRatioDtoByPeriod(String accountingPeriod) {
        return irHedgeRatioMapper.deleteIrHedgeRatioEntityByPeriod(accountingPeriod);
    }
}
