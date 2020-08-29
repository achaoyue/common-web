package com.mwy.controller;

import com.mwy.service.CommonBizService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/{user}/get/{table}/detail")
    public Object get(@PathVariable("user") String user,
        @PathVariable("table") Long table,
        @RequestParam HashMap<String,String> param){
        return commonBizService.query(user,table,"detail",param);
    }

    @GetMapping("/{user}/get/{table}/{method}")
    public Object get2(@PathVariable("user") String user,
        @PathVariable("table") Long table,
        @PathVariable("method") String method,
        @RequestParam HashMap<String,String> param){
        return commonBizService.queryBySql(user,table,method,param);
    }

    @GetMapping("/{user}/save/{table}/{method}")
    public Object save(@PathVariable("user") String user,
        @PathVariable("table") Long table,
        @PathVariable("method") String method,
        @RequestParam HashMap<String,String> param){
        return commonBizService.save(user,table,method,param);
    }
}
