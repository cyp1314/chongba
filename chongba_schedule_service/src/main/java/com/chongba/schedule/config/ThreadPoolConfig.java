package com.chongba.schedule.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Mr-CHEN
 * @version 1.0
 * @description: TODO
 * @date 2020-11-24 17:42
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor myThreadPool(){
        ThreadPoolTaskExecutor threadPool = new VisiableThreadPool();

        threadPool.setCorePoolSize(5);

        threadPool.setMaxPoolSize(100);

        threadPool.setKeepAliveSeconds(60);

        threadPool.setQueueCapacity(100);

        threadPool.setThreadNamePrefix("my-");

        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        threadPool.initialize();

        return threadPool;
    }
}
