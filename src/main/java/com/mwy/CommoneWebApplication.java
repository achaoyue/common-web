package com.mwy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.mwy.reponstory.mapper")
@SpringBootApplication
public class CommoneWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommoneWebApplication.class, args);
    }

}
