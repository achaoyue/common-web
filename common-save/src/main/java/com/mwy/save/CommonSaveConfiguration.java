package com.mwy.save;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author mouwenyao 2021/1/2 10:58 上午
 */
@MapperScan("com.mwy.save.reponstory.mapper")
@ComponentScan(basePackages = "com.mwy.save")
@ConditionalOnProperty(value = "spring.common-save",havingValue = "true")
public class CommonSaveConfiguration {

}
