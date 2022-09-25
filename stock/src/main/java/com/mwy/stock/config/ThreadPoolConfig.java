package com.mwy.stock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor bizThreadPool() {
        int cpuNum = Runtime.getRuntime().availableProcessors();
        return obtainExecutor(8, 16, 10000, 60);
    }

    private ThreadPoolTaskExecutor obtainExecutor(int coreSize, int maxSize, int capacity, int aliveSeconds) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(coreSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxSize);
        threadPoolTaskExecutor.setQueueCapacity(capacity);
        threadPoolTaskExecutor.setKeepAliveSeconds(aliveSeconds);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        threadPoolTaskExecutor.setAwaitTerminationSeconds(30);
        return threadPoolTaskExecutor;
    }
}
