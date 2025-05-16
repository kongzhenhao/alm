package com.xl.alm.job.dur.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xl.alm.job.dur.entity.KeyDurationDiscountFactorsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 关键久期折现因子表 Mapper 接口
 *
 * @author AI
 */
@Mapper
public interface KeyDurationDiscountFactorsMapper extends BaseMapper<KeyDurationDiscountFactorsEntity> {

    /**
     * 根据账期删除关键久期折现因子
     *
     * @param accountPeriod 账期
     * @return 结果
     */
    int deleteByAccountPeriod(@Param("accountPeriod") String accountPeriod);

    /**
     * 批量插入关键久期折现因子
     *
     * @param list 关键久期折现因子列表
     * @return 结果
     */
    int batchInsert(List<KeyDurationDiscountFactorsEntity> list);

    /**
     * 执行SQL删除语句
     *
     * @param sql SQL语句
     * @return 结果
     */
    int deleteBySql(@Param("sql") String sql);
}
