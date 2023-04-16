package com.mwy.stock.reponstory.dao.modal;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class StockNoticeHistoryAttribute {
    private String noticeMsg;
    private boolean goodTrend;

    public static StockNoticeHistoryAttribute fromJson(String json){
        return JSON.parseObject(json,StockNoticeHistoryAttribute.class);
    }
}
