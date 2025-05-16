package com.xl.alm.job.dur.service;

/**
 * 关键久期折现曲线服务接口
 *
 * @author AI
 */
public interface KeyDurationDiscountCurveService {
    
    /**
     * 计算关键久期折现曲线
     * 
     * @param accountPeriod 账期
     * @return 执行结果
     */
    boolean calculateKeyDurationDiscountCurve(String accountPeriod);
}
