package com.mwy.stock.modal.converter;

import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.reponstory.remote.modal.EasyMoneyStockDTO;

public class StockConvertor {
    public static StockDO toDO(EasyMoneyStockDTO stockDTO){
        StockDO stockDO = new StockDO();
        stockDO.setId(stockDTO.getId());
        stockDO.setStockNum(stockDTO.getStockNum());
        stockDO.setStockName(stockDTO.getStockName());
        stockDO.setHigh(stockDTO.getHigh());
        stockDO.setLow(stockDTO.getLow());
        stockDO.setOpen(stockDTO.getOpen());
        stockDO.setClose(stockDTO.getClose());
        stockDO.setPreClose(stockDTO.getPreClose());
        stockDO.setTurnOverrate(stockDTO.getTurnOverrate());
        stockDO.setAmplitude(stockDTO.getAmplitude());
        stockDO.setUpDownPrices(stockDTO.getUpDownPrices());
        stockDO.setUpDownRange(stockDTO.getUpDownRange());
        stockDO.setUpDownRange3(stockDTO.getUpDownRange3());
        stockDO.setUpDownRange5(stockDTO.getUpDownRange5());
        stockDO.setVolume(stockDTO.getVolume());
        stockDO.setAmount(stockDTO.getAmount());
        stockDO.setFlowMarketValue(stockDTO.getFlowMarketValue());
        stockDO.setTotalFlowShares(stockDTO.getTotalFlowShares());
        stockDO.setTotalMarketValue(stockDTO.getTotalMarketValue());
        stockDO.setTotalShares(stockDTO.getTotalShares());
        stockDO.setIndustry(stockDTO.getIndustry());
        stockDO.setPlate(stockDTO.getPlate());
        stockDO.setBelongPlate(stockDTO.getBelongPlate());
        stockDO.setQuantityRelativeRatio(stockDTO.getQuantityRelativeRatio());
        stockDO.setListingDate(stockDTO.getListingDate());
        stockDO.setUpdateDate(stockDTO.getUpdateDate());
        return stockDO;
    }
}
