package com.mwy.controller;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author mouwenyao 2020/8/23 11:36 上午
 */
@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(Exception.class)
    public Object exception(Exception e){
        log.error("系统错误",e);
        Map<String,String> map = new HashMap<>();
        map.put("msg",e.getMessage());
        map.put("code","500");
        return map;
    }

}
