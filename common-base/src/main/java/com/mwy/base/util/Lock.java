package com.mwy.base.util;

import java.util.concurrent.ConcurrentHashMap;

public class Lock {
    private ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();

    public boolean lock(String key){
        synchronized (this){
            if (concurrentHashMap.containsKey(key)){
                return false;
            }
            concurrentHashMap.put(key,new Object());
            return true;
        }
    }
    public boolean unlock(String key){
        synchronized (this){
            concurrentHashMap.remove(key);
            return true;
        }
    }
}
