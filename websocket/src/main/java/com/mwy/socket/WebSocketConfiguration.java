package com.mwy.socket;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author mouwenyao 2021/1/2 11:00 上午
 */
@ComponentScan(basePackages = {"com.mwy.socket","com.mwy.ai","com.mwy.task"})
@ConditionalOnProperty(value = "spring.websocket",havingValue = "true")
public class WebSocketConfiguration {

}
