package com.mwy.stock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * hello world
 * @author mouwenyao
 */
@RestController
@RequestMapping("/api/stock")
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello world !";
    }
}
