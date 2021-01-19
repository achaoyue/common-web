package com.mwy.util;

/**
 * @author mouwenyao 2021/1/18 11:55 下午
 */
public class DistanceUtil {

    public static int getDistance(int x1,int y1,int x2,int y2){
        double sqrt = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
        return (int) Math.round(sqrt);
    }
}
