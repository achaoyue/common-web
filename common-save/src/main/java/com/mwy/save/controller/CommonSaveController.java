package com.mwy.save.controller;

import com.alibaba.fastjson.JSONObject;
import com.mwy.save.service.CommonBizService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用存储接口
 * @author mouwenyao 2020/8/22 7:15 下午
 */
@RestController
@RequestMapping("/data")
public class CommonSaveController {

    @Resource
    CommonBizService commonBizService;

    @GetMapping("/test/{key}")
    public Object get(@PathVariable("key")String key,
        HttpServletRequest request){

        return commonBizService.exe(key,request);
    }

    @PostMapping("/test/{key}")
    public Object post(@PathVariable("key")String key,
        @RequestBody JSONObject object){

        return commonBizService.exec(key,object);
    }

    @PostMapping("/refresh")
    public Object refresh(){
        return commonBizService.refresh();
    }
}
