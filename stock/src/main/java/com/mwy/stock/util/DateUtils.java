package com.mwy.stock.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DateUtils {
    /**
     * 时间格式化
     *
     * @param date
     * @param format
     * @return
     */
    public static String date2String(Date date, String format) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 将字符串按format格式转换为date类型
     *
     * @param str
     * @param format
     *
     * @return
     */
    public static Date string2Date(String str, String format) {
        if(StringUtils.isBlank(str) || StringUtils.isBlank(format)){
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            log.info("日期解析错误:",e);
            throw new RuntimeException(e);
        }
    }

    public static Date addDay(Date now, int day){
        return new Date(now.getTime() + 1000*60*60*24*day);
    }

    public static String nowStr() {
        return date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
    }
}
