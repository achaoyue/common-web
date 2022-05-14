package com.mwy.stock.modal.dto.easymoney;

import lombok.Data;

@Data
public class EasyMoneyStockMonthInfoDTO {
    /**
     * 日期
     */
    private String date;

    /**
     * 股票编码
     */
    private String stockNum;

    /**
     * 开盘价
     */
    private Double open;

    /**
     * 最高价
     */
    private Double high;

    /**
     * 最低价
     */
    private Double low;

    /**
     * 收盘价
     */
    private Double close;

    /**
     * 昨收
     */
    private Double preClose;

    /**
     * 成交量
     */
    private Double volume;

    /**
     * 成交额
     */
    private Double amount;

    /**
     * 换手率
     */
    private Double turnOverrate;

    /**
     * 振幅，一天最大涨幅-最小涨幅
     */
    private Double amplitude;

    /**
     * 涨跌额度
     */
    private Double upDownPrices;

    /**
     * 涨幅
     */
    private Double upDownRange;

    /**
     * 5日均价
     */
    private Double ma5;

    /**
     * 10日均价
     */
    private Double ma10;

    /**
     * 20日均价
     */
    private Double ma20;

    /**
     * 30日均价
     */
    private Double ma30;

    /**
     * 60日均价
     */
    private Double ma60;

    /**
     * 120日均价
     */
    private Double ma120;

    /**
     * 200日均价
     */
    private Double ma200;

    /**
     * 250日均价
     */
    private Double ma250;

    /**
     * 120日成交量
     */
    private Double volume120;

    /**
     * k
     */
    private Double k;

    /**
     * d
     */
    private Double d;

    /**
     * j
     */
    private Double j;

    /**
     * dif
     */
    private Double dif;

    /**
     * dea
     */
    private Double dea;

    /**
     * macd
     */
    private Double macd;

    /**
     * rsi6
     */
    private Double rsi6;

    /**
     * rsi12
     */
    private Double rsi12;

    /**
     * rsi24
     */
    private Double rsi24;

    /**
     * wr6
     */
    private Double wr6;

    /**
     * wr10
     */
    private Double wr10;

    /**
     * cci
     */
    private Double cci;
}
