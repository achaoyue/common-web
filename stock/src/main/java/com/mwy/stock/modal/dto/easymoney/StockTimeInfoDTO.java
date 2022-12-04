package com.mwy.stock.modal.dto.easymoney;

import lombok.Data;

@Data
public class StockTimeInfoDTO {
    private int timeInfoId;
    private int stockId;
    private String stockCode;
    private double amount;
    private double price;
    private double avgPrice;
    private String date;
    private String time;
    private double volume;
    /**
     * 涨跌幅
     */
    private double upDownRange;
    private String stockTableName;

}