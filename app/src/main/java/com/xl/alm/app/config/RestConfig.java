package com.xl.alm.app.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Configuration
public class RestConfig {
    /**
     * 访问外部接口，无loadbalance，不从nacos取服务
     *
     * @return
     */
    @Bean
    public RestTemplate externalRestTemplate() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //设置连接超时
                .connectTimeout(10, TimeUnit.SECONDS)
                //设置读超时
                .readTimeout(30, TimeUnit.SECONDS)
                //设置写超时
                .writeTimeout(10, TimeUnit.SECONDS)
                //是否自动重连OCR保存身份证信息接口 用户
                .retryOnConnectionFailure(true)
                //.addInterceptor(new HeaderInterceptor())
                .connectionPool(new ConnectionPool(10, 55, TimeUnit.SECONDS))
                .build();
        OkHttp3ClientHttpRequestFactory okHttp3ClientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory(okHttpClient);
        RestTemplate restTemplate = new RestTemplate(okHttp3ClientHttpRequestFactory);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

}
