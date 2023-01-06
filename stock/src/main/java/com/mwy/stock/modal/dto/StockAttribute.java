package com.mwy.stock.modal.dto;

import lombok.Data;

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
}
