package com.mwy.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CommoneWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommoneWebApplication.class, args);
    }

}
