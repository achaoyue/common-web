package com.mwy.stock.controller;

import com.mwy.base.co.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/checkuserlogininfo")
    @ResponseBody
    public Result<Object> checkUserLoginInfo(@RequestHeader("token") String token) {
        Map<String,String> map = new HashMap<>();
        map.put("username","test");
        return Result.ofSuccess(map);
    }
}
