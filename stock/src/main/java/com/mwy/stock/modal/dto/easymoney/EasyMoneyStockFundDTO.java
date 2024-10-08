package com.mwy.stock.modal.dto.easymoney;

import lombok.Data;

/**
 * 股票资金流
 */
@Data
public class EasyMoneyStockFundDTO {
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
