package com.xl.alm.job.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
public class ThreadPoolConfig {
    // 定义线程池
    @Bean("customThreadPool")
    public Executor customThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        /** 核心线程数（默认线程数） */
        executor.setCorePoolSize(100);
        /** 最大线程数 */
        executor.setMaxPoolSize(100);
        /** 缓冲队列大小 */
        executor.setQueueCapacity(1500);
        /** 允许线程空闲时间（单位：默认为秒） */
        executor.setKeepAliveSeconds(60);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        /** 线程池名前缀 */
        executor.setThreadNamePrefix("custom-task-thread-");
        // 拒绝策略：缓存队列满了之后由调用线程处理，一般是主线程
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.initialize();
        return executor;
    }
}
