package com.mwy.stock.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NumberUtil {
    private static final String dan2[] = new String[]{"", "W", "亿", "万亿", "兆"};

    public static String format(Double num) {
        try {
            if (num == null) {
                return "";
            }
            int idx = 0;
            long numInt = num.longValue();
            while (numInt / 10000 > 0) {
                idx++;
                numInt = numInt / 10000;
            }
            return numInt + dan2[idx];
        } catch (Exception e) {
            log.info("数字格式化错误:{}", num);
            return num + "";
        }
    }

    public static String formatX(Double num) {
        try {
            if (num == null) {
                return "";
            }
            if (num > (Math.pow(10,12))){
                return ((int)(num/(Math.pow(10,12))*100))/100.0+"万亿";
            }else if (num > (Math.pow(10,8))){
                return ((int)(num/(Math.pow(10,8))*100))/100.0+"亿";
            }else if (num > (Math.pow(10,4))){
                return ((int)(num/(Math.pow(10,4))*100))/100.0+"万";
            }
            return num+"";
        } catch (Exception e) {
            log.info("数字格式化错误:{}", num);
            return num + "";
        }
    }
}
