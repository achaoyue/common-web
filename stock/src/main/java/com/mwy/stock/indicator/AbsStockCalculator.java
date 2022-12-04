package com.mwy.stock.indicator;

import com.mwy.stock.modal.dto.StockScoreDTO;
import com.mwy.stock.modal.enums.CalculatorEnum;

public abstract class AbsStockCalculator implements StockCalculator{
    public abstract CalculatorEnum type();

    public StockScoreDTO calc(String stockNum, String date) {
        StockScoreDTO stockScoreDTO = doCalc(stockNum, date);
        stockScoreDTO.setStrategyId(type().getCode());
        stockScoreDTO.setStrategyName(type().getDesc());
        return stockScoreDTO;
    }

    public abstract StockScoreDTO doCalc(String stockNum, String date);

}
