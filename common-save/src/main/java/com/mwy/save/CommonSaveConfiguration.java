package com.mwy.save;

import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author mouwenyao 2021/1/2 10:58 上午
 */
@MapperScan("com.mwy.save.reponstory.mapper")
@ComponentScan(basePackages = "com.mwy.save")
public class CommonSaveConfiguration {

}
