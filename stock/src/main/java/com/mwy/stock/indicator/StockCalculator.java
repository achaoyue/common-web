package com.mwy.stock.indicator;

import com.mwy.stock.modal.dto.StockScoreDTO;

public interface StockCalculator {

    /**
     * 计算
     */
    StockScoreDTO calc(String stockNum, String date);
}
