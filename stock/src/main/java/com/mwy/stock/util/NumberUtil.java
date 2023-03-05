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
}
