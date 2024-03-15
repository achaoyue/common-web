package com.mwy.stock.modal.dto.easymoney;

import lombok.Data;

/**
 * 股票5分钟排队情况。盘口
 */

@Data
public class StockQueue {
    private Double buyOne;
    private Double buyTwo;
    private Double buyThree;
    private Double buyFour;
    private Double buyFive;

    private Double buyOnePrice;
    private Double buyTwoPrice;
    private Double buyThreePrice;
    private Double buyFourPrice;
    private Double buyFivePrice;

    private Double soldOne;
    private Double soldTwo;
    private Double soldThree;
    private Double soldFour;
    private Double soldFive;

    private Double soldOnePrice;
    private Double soldTwoPrice;
    private Double soldThreePrice;
    private Double soldFourPrice;
    private Double soldFivePrice;
}
