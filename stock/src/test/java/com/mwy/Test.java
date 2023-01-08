package com.mwy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mwy.base.util.DingDingUtil;
import com.mwy.stock.util.DateUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        DingDingUtil.sendMsg("","数据爬取完成："+ DateUtils.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
    }
}
