package com.xl.alm.app.config;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

@Configuration
public class FastjsonConfiguration {
    @Bean
    public HttpMessageConverter<?> fastJsonHttpMessageConverter() {
        // 创建FastJson消息转换器
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        // 创建配置类
        FastJsonConfig config = new FastJsonConfig();
        // 设置序列化特性，如：将BigDecimal序列化为普通字符串
        config.setWriterFeatures(JSONWriter.Feature.WriteBigDecimalAsPlain);

        // 设置配置
        converter.setFastJsonConfig(config);

        return converter;
    }
}
