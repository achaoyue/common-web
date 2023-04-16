package com.mwy.stock.modal.dto.easymoney;

import lombok.Data;

/**
 * 股票资金流
 */
@Data
public class EasyMoneyStockFundDTO {
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
}
