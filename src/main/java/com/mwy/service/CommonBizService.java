package com.mwy.service;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * @author mouwenyao 2020/8/22 7:42 下午
 */
public interface CommonBizService {

    List<Map<String,String>> queryBySql(String user, Long tableId, String method, Map<Object, Object> param);

    Object save(String user, Long tableId, String method, Map<Object, Object> param);

    Object exe(Long id, HttpServletRequest request);

    Object exec(Long id, JSONObject map);
}
