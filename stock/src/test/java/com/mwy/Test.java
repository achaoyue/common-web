package com.mwy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mwy.base.util.DingDingUtil;
import com.mwy.stock.config.NoticeConfig;
import com.mwy.stock.util.DateUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Test {
    public static double calculateSimilarity(double[] sequence1, double[] sequence2) {
        // 确保两个序列长度相同
        if (sequence1.length != sequence2.length) {
            throw new IllegalArgumentException("Sequence lengths are not equal");
        }

        // 使用PearsonsCorrelation类计算相关系数

        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double similarity = correlation.correlation(sequence1, sequence2);

        return similarity;
    }
    public static void main(String[] args) {
//        DingDingUtil.sendMsg(NoticeConfig.stockNoticeToken2,"@13017153244 数据爬取完成："+ DateUtils.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"),true);
        double num1[] = {1,2,3,4,5};
        double num2[] = {2,4,6,8,10};
        System.out.println(calculateSimilarity(num1,num2));
    }
}
