package com.xl.alm.job.dur.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xl.alm.job.dur.entity.KeyDurationDiscountCurveEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 关键久期折现曲线表 Mapper 接口
 *
 * @author AI
 */
@Mapper
public interface KeyDurationDiscountCurveMapper extends BaseMapper<KeyDurationDiscountCurveEntity> {
    
    /**
     * 根据账期删除关键久期折现曲线
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteByAccountPeriod(@Param("accountPeriod") String accountPeriod);
    
    /**
     * 批量插入关键久期折现曲线
     *
     * @param list 关键久期折现曲线列表
     * @return 结果
     */
    int batchInsert(List<KeyDurationDiscountCurveEntity> list);
}
