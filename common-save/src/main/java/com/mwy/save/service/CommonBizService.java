package com.mwy.save.service;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * @author mouwenyao 2020/8/22 7:42 下午
 */
public interface CommonBizService {

    Object exe(String id, HttpServletRequest request);

    Object exec(String id, JSONObject map);

    /**
     * 刷新mapper
     * @return
     */
    Object refresh();
}
