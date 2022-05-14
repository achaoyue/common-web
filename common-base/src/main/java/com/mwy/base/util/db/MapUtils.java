package com.mwy.base.util.db;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mouwenyao 2020/11/25 5:20 下午
 */
public class MapUtils {

    /**
     *
     * @param targetMap
     * @param rMap
     */
    public static<K,V> void merge(Map<K,V> targetMap,Map<K,V> rMap){
        if(rMap == null){
            return;
        }
        targetMap.entrySet().forEach(e->{
            Object key = e.getKey();
            V value = e.getValue();

            V rValue = rMap.get(key);
            if(value instanceof Map){
                if(rValue != null && rValue instanceof Map){
                    merge((Map)value,(Map) rValue);
                }
                return;
            }
            if(rValue != null){
              e.setValue(rValue);
            }
        });
    }
}
