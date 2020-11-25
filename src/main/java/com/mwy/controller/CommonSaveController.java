package com.mwy.controller;

import com.mwy.service.CommonBizService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/{user}/get/{table}/{method}")
    public Object get(@PathVariable("user") String user,
        @PathVariable("table") Long table,
        @PathVariable("method") String method,
        @RequestBody HashMap<Object,Object> param){
        return commonBizService.queryBySql(user,table,method,param);
    }

    @PostMapping("/{user}/save/{table}/{method}")
    public Object save(@PathVariable("user") String user,
        @PathVariable("table") Long table,
        @PathVariable("method") String method,
        @RequestBody HashMap<Object,Object> param){
        return commonBizService.save(user,table,method,param);
    }
}
