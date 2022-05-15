package com.mwy.stock.modal.converter;

import com.mwy.stock.modal.co.StockScoreCO;
import com.mwy.stock.reponstory.dao.modal.StockScoreDO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StockScoreConvetor {
    public static List<StockScoreCO> toCO(List<StockScoreDO> stockScoreDOS) {
        if (CollectionUtils.isEmpty(stockScoreDOS)) {
            return Collections.emptyList();
        }
        return stockScoreDOS.stream()
                .map(e->toCO(e))
                .collect(Collectors.toList());
    }

    private static StockScoreCO toCO(StockScoreDO stockScoreDO) {
        StockScoreCO stockScoreCO = new StockScoreCO();
        stockScoreCO.setStockNum(stockScoreDO.getStockNum());
        stockScoreCO.setStockName(stockScoreDO.getStockName());
        stockScoreCO.setScore(stockScoreDO.getScore());
        stockScoreCO.setScoreDesc(stockScoreDO.getScoreDesc());
        return stockScoreCO;
    }
}
