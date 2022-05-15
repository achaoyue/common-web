package com.mwy.stock.modal.converter;

import com.mwy.stock.modal.dto.StockScoreDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDayInfoDTO;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDTO;
import com.mwy.stock.reponstory.dao.modal.StockDayInfoDO;
import com.mwy.stock.reponstory.dao.modal.StockScoreDO;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StockConvertor {
    public static StockDO toDO(EasyMoneyStockDTO stockDTO) {
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

    public static List<StockDayInfoDO> toDayInfoDO(List<EasyMoneyStockDayInfoDTO> moneyStockDayInfoDTOS) {
        if (CollectionUtils.isEmpty(moneyStockDayInfoDTOS)) {
            return Collections.emptyList();
        }
        return moneyStockDayInfoDTOS.stream()
                .map(e -> toDayInfoDO(e))
                .collect(Collectors.toList());
    }

    private static StockDayInfoDO toDayInfoDO(EasyMoneyStockDayInfoDTO stockDayInfoDTO) {
        StockDayInfoDO stockDayInfoDO = new StockDayInfoDO();
        stockDayInfoDO.setDate(stockDayInfoDTO.getDate());
        stockDayInfoDO.setStockNum(stockDayInfoDTO.getStockNum());
        stockDayInfoDO.setOpen(scale(stockDayInfoDTO.getOpen()));
        stockDayInfoDO.setHigh(scale(stockDayInfoDTO.getHigh()));
        stockDayInfoDO.setLow(scale(stockDayInfoDTO.getLow()));
        stockDayInfoDO.setClose(scale(stockDayInfoDTO.getClose()));
        stockDayInfoDO.setPreClose(scale(stockDayInfoDTO.getPreClose()));
        stockDayInfoDO.setVolume(scale(stockDayInfoDTO.getVolume()));
        stockDayInfoDO.setAmount(scale(stockDayInfoDTO.getAmount()));
        stockDayInfoDO.setTurnOverrate(scale(stockDayInfoDTO.getTurnOverrate()));
        stockDayInfoDO.setAmplitude(scale(stockDayInfoDTO.getAmplitude()));
        stockDayInfoDO.setUpDownPrices(scale(stockDayInfoDTO.getUpDownPrices()));
        stockDayInfoDO.setUpDownRange(scale(stockDayInfoDTO.getUpDownRange()));
        stockDayInfoDO.setMa5(scale(stockDayInfoDTO.getMa5()));
        stockDayInfoDO.setMa10(scale(stockDayInfoDTO.getMa10()));
        stockDayInfoDO.setMa20(scale(stockDayInfoDTO.getMa20()));
        stockDayInfoDO.setMa30(scale(stockDayInfoDTO.getMa30()));
        stockDayInfoDO.setMa60(scale(stockDayInfoDTO.getMa60()));
        stockDayInfoDO.setMa120(scale(stockDayInfoDTO.getMa120()));
        stockDayInfoDO.setMa200(scale(stockDayInfoDTO.getMa200()));
        stockDayInfoDO.setMa250(scale(stockDayInfoDTO.getMa250()));
        stockDayInfoDO.setVolume120(scale(stockDayInfoDTO.getVolume120()));
        stockDayInfoDO.setK(scale(stockDayInfoDTO.getK()));
        stockDayInfoDO.setD(scale(stockDayInfoDTO.getD()));
        stockDayInfoDO.setJ(scale(stockDayInfoDTO.getJ()));
        stockDayInfoDO.setDif(scale(stockDayInfoDTO.getDif()));
        stockDayInfoDO.setDea(scale(stockDayInfoDTO.getDea()));
        stockDayInfoDO.setMacd(scale(stockDayInfoDTO.getMacd()));
        stockDayInfoDO.setRsi6(scale(stockDayInfoDTO.getRsi6()));
        stockDayInfoDO.setRsi12(scale(stockDayInfoDTO.getRsi12()));
        stockDayInfoDO.setRsi24(scale(stockDayInfoDTO.getRsi24()));
        stockDayInfoDO.setWr6(scale(stockDayInfoDTO.getWr6()));
        stockDayInfoDO.setWr10(scale(stockDayInfoDTO.getWr10()));
        stockDayInfoDO.setCci(stockDayInfoDTO.getCci());
        return stockDayInfoDO;
    }

    public static Double scale(Double d) {
        if (d == null){
            return null;
        }
        if (d.isNaN()) {
            return 0D;
        }
        return d;
    }

    public static StockScoreDO toScoreDO(StockScoreDTO scoreDTO) {
        StockScoreDO stockScoreDO = new StockScoreDO();
        stockScoreDO.setStockNum(scoreDTO.getStockNum());
        stockScoreDO.setDate(scoreDTO.getDate());
        stockScoreDO.setStrategyId(scoreDTO.getStrategyId());
        stockScoreDO.setStrategyName(scoreDTO.getStrategyName());
        stockScoreDO.setScore(scoreDTO.getScore());
        stockScoreDO.setScoreDesc(scoreDTO.getScoreDesc());
        stockScoreDO.setUpdateDate(scoreDTO.getUpdateDate());
        return stockScoreDO;
    }
}
