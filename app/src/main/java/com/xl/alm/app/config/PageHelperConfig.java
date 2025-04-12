package com.xl.alm.app.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

/**
 * PageHelper配置类，用于解决PageHelper与JSqlParser版本不兼容问题
 */
@Configuration
@EnableAutoConfiguration(exclude = {
    com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration.class
})
public class PageHelperConfig {
    // No additional configuration needed
}
