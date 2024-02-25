package com.mwy.stock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author mouwenyao 2021/1/2 10:58 上午
 */
@MapperScan("com.mwy.stock.reponstory.mapper")
@ComponentScan(basePackages = "com.mwy.stock")
@ConditionalOnProperty(value = "spring.stock",havingValue = "true")
public class StockConfiguration {

}
