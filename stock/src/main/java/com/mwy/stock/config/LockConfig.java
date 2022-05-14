package com.mwy.stock.config;

import com.mwy.base.util.Lock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LockConfig {
    @Bean
    public Lock lock(){
        return new Lock();
    }
}
