package com.mwy.stock.reponstory.dao.modal;

import lombok.Data;

import javax.persistence.Table;

/**
 * 股票资金流
 */
@Data
@Table(name = "stock_fund_info")
public class StockFundInfoDO {
    /**
     * id
     */
    private Long id;
    /**
     * 股票编码
     */
    private String stockNum;
    /**
     * 日期
     */
    private String date;
    /**
     * 主力流入金额
     */
    private Double mainMoneyIn;

    /**
     * 主力流出金额
     */
    private Double mainMoneyOut;

    /**
     * 超大单流入金额
     */
    private Double superBigMoneyIn;

    /**
     * 超大单流出
     */
    private Double superBigMoneyOut;

    /**
     * 大单流入金额
     */
    private Double bigMoneyIn;

    /**
     * 大单流出金额
     */
    private Double bigMoneyOut;

    /**
     * 中单流入金额
     */
    private Double middleMoneyIn;

    /**
     * 中单流出金额
     */
    private Double middleMoneyOut;

    /**
     * 小单流入金额
     */
    private Double smallMoneyIn;

    /**
     * 小单流出金额
     */
    private Double smallMoneyOut;
}
