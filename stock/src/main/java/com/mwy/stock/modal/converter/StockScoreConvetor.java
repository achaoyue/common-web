package com.mwy.stock.modal.converter;

import com.mwy.stock.modal.co.StockScoreCO;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.reponstory.dao.modal.StockScoreDO;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StockScoreConvetor {
    public static List<StockScoreCO> toCO(List<StockScoreDO> stockScoreDOS, List<StockDO> stockDOS) {
        if (CollectionUtils.isEmpty(stockScoreDOS)) {
            return Collections.emptyList();
        }
        Map<String, StockDO> stockDOMap = Optional.ofNullable(stockDOS).orElseGet(Collections::emptyList).stream()
                .collect(Collectors.toMap(e -> e.getStockNum(), e -> e,(e1,e2)->e1));
        return stockScoreDOS.stream()
                .map(e->toCO(e,stockDOMap.get(e.getStockNum())))
                .collect(Collectors.toList());
    }

    private static StockScoreCO toCO(StockScoreDO stockScoreDO,StockDO stockDO) {
        StockScoreCO stockScoreCO = new StockScoreCO();

        stockScoreCO.setStockNum(stockScoreDO.getStockNum());
        stockScoreCO.setStockName(stockScoreDO.getStockName());
        stockScoreCO.setScore(stockScoreDO.getScore());
        stockScoreCO.setScoreDesc(stockScoreDO.getScoreDesc());

        if (stockDO != null){
            stockScoreCO.setUpDownRange(stockDO.getUpDownRange());
            stockScoreCO.setTotalFlowShares(stockDO.getTotalFlowShares());
            stockScoreCO.setTotalMarketValue(stockDO.getTotalMarketValue());
            stockScoreCO.setTotalShares(stockDO.getTotalShares());
            stockScoreCO.setQuantityRelativeRatio(stockDO.getQuantityRelativeRatio());
            stockScoreCO.setIndustry(stockDO.getIndustry());
            stockScoreCO.setPlate(stockDO.getPlate());
            stockScoreCO.setBelongPlate(stockDO.getBelongPlate());
            stockScoreCO.setFavorite(stockDO.getFavorite());
        }

        return stockScoreCO;
    }
}
