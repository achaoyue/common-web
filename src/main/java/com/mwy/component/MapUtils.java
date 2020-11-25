package com.mwy.component;

import java.util.HashMap;
import java.util.Map;
import org.checkerframework.checker.units.qual.K;

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

    public static void main(String[] args) {
        Map<String,Object> map1 = new HashMap<>();
        map1.put("1","2");
        Map<String,Object> map2 = new HashMap<>();
        map2.put("2","3");

        map1.put("t",new HashMap<>());
        ((Map)map1.get("t")).put("a","a");
//        map2.put("t",new HashMap<>());
//        ((Map)map2.get("t")).put("a","b");

        merge(map1,map2);

        System.out.println(map1);
    }
}
