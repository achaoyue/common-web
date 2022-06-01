package com.mwy.stock.modal.co;

import lombok.Data;

@Data
public class StockScoreCO {
    private String stockNum;
    private String stockName;
    private Double upDownRange;
    private int score;
    private String scoreDesc;


    /**
     * 流通股
     */
    private Double totalFlowShares;

    /**
     * 总市值
     */
    private Double totalMarketValue;

    /**
     * 总股本
     */
    private Double totalShares;

    /**
     * 量比
     */
    private Double quantityRelativeRatio;

    /**
     * 行业
     */
    private String industry;

    /**
     * 板块
     */
    private String plate;

    /**
     * 所属板块
     */
    private String belongPlate;
}
