package com.xl.alm.job.common.service.impl;

import com.xl.alm.job.common.service.ISysConfigService;
import com.xl.alm.job.common.entity.SysConfig;
import com.xl.alm.job.common.mapper.SysConfigMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SysConfigServiceImpl implements ISysConfigService {
    private final SysConfigMapper configMapper;
    private final Map<String, SysConfig> sysConfigMap = new HashMap<>();

    @Override
    public String getConfigByKey(String configKey) {
        SysConfig sysConfig = sysConfigMap.get(configKey);
        if (null != sysConfig) {
            return sysConfig.getConfigValue();
        } else {
            SysConfig config = new SysConfig();
            config.setConfigKey(configKey);
            SysConfig retConfig = this.configMapper.selectConfig(config);
            if (null != retConfig) {
                sysConfigMap.put(retConfig.getConfigKey(), retConfig);
                return retConfig.getConfigValue();
            } else {
                return "";
            }
        }

    }
}
