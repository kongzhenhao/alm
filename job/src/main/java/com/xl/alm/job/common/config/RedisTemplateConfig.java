package com.xl.alm.job.common.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @ClassName RedisTemplateConfig
 * @Author luyu
 * @date 2024.01.30 16:11
 * @Description
 */
//@Configuration
public class RedisTemplateConfig {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Bean
    public RedisTemplate<String, Object> redisTemplateInit() {
        // 创建序列化器实例
        GenericFastJsonRedisSerializer genericSerializer = new GenericFastJsonRedisSerializer();
        FastJsonRedisSerializer<Object> objectSerializer = new FastJsonRedisSerializer<>(Object.class);

        // 设置序列化Key的实例化对象
        redisTemplate.setKeySerializer(genericSerializer);
        // 设置序列化Value的实例化对象
        redisTemplate.setValueSerializer(genericSerializer);
        // 设置序列化HashKey的实例化对象
        redisTemplate.setHashKeySerializer(genericSerializer);
        // 设置序列化Hash Value的实例化对象
        redisTemplate.setHashValueSerializer(genericSerializer);
        // 对object类型的序列化
        redisTemplate.setDefaultSerializer(objectSerializer);

        return redisTemplate;
    }

}
