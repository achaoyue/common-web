package com.mwy.stock.modal.co;

import lombok.Data;

@Data
public class StockScoreCO {
    private String stockNum;
    private String stockName;
    private Double upDownRange;
    private int score;
    private String scoreDesc;
}
