package com.mwy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mouwenyao 2020/8/22 7:42 下午
 */
public interface CommonBizService {

    /**
     *  查询详情
     * @param user
     * @param tableId
     * @param method
     * @param param
     * @return
     */
    Object query(String user, Long tableId, String method, Map<String, String> param);


    List<Map<String,String>> queryBySql(String user, Long tableId, String method, Map<String, String> param);

    Object save(String user, Long tableId, String method, Map<String, String> param);

}
