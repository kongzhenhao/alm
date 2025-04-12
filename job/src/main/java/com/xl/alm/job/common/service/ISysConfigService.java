package com.xl.alm.job.common.service;


/**
 * 参数配置 服务层
 *
 * * @author lightning
 */
public interface ISysConfigService {


    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    String getConfigByKey(String configKey);


}
