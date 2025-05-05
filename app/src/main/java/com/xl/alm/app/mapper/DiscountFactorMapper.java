package com.xl.alm.app.mapper;

import com.xl.alm.app.entity.DiscountFactorEntity;
import com.xl.alm.app.query.DiscountFactorQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 折现因子表 Mapper 接口
 *
 * @author AI Assistant
 */
@Mapper
public interface DiscountFactorMapper {

    /**
     * 查询折现因子列表
     *
     * @param discountFactorQuery 折现因子查询条件
     * @return 折现因子列表
     */
    List<DiscountFactorEntity> selectDiscountFactorEntityList(DiscountFactorQuery discountFactorQuery);

    /**
     * 用id查询折现因子列表
     *
     * @param id id
     * @return 折现因子
     */
    DiscountFactorEntity selectDiscountFactorEntityById(Long id);

    /**
     * 根据账期、因子类型、基点类型和久期类型查询折现因子
     *
     * @param accountPeriod 账期
     * @param factorType 因子类型
     * @param bpType 基点类型
     * @param durationType 久期类型
     * @return 折现因子
     */
    DiscountFactorEntity selectDiscountFactorEntityByCondition(
            @Param("accountPeriod") String accountPeriod,
            @Param("factorType") String factorType,
            @Param("bpType") String bpType,
            @Param("durationType") String durationType);

    /**
     * 新增折现因子
     *
     * @param discountFactorEntity 折现因子
     * @return 结果
     */
    int insertDiscountFactorEntity(DiscountFactorEntity discountFactorEntity);

    /**
     * 批量插入折现因子数据
     *
     * @param discountFactorEntityList 折现因子列表
     * @return 影响行数
     */
    int batchInsertDiscountFactorEntity(List<DiscountFactorEntity> discountFactorEntityList);

    /**
     * 修改折现因子
     *
     * @param discountFactorEntity 折现因子
     * @return 结果
     */
    int updateDiscountFactorEntity(DiscountFactorEntity discountFactorEntity);

    /**
     * 删除折现因子
     *
     * @param id 折现因子主键
     * @return 结果
     */
    int deleteDiscountFactorEntityById(Long id);

    /**
     * 批量删除折现因子
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteDiscountFactorEntityByIds(Long[] ids);

    /**
     * 删除指定账期的折现因子数据
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteDiscountFactorEntityByPeriod(String accountPeriod);
}
