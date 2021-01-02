package com.mwy.starter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mouwenyao 2020/8/22 7:12 下午
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("alive")
    public String hello(){
        return "ok";
    }
}
