package com.mwy.stock.modal.dto;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
public class StockAttribute {
    /**
     * 添加收藏时间
     */
    private Date addFavoriteDate;
    /**
     * 收藏时价格
     */
    private Double initPrice;

    public static StockAttribute fromJson(String attribute) {
        if (StringUtils.isEmpty(attribute)){
            return null;
        }
        return JSON.parseObject(attribute,StockAttribute.class);
    }
}
