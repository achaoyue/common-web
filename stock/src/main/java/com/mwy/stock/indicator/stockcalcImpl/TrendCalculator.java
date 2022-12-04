package com.mwy.stock.indicator.stockcalcImpl;

import java.util.Date;

import com.mwy.stock.config.CalculatorConsts;
import com.mwy.stock.indicator.StockCalculator;
import com.mwy.stock.modal.dto.StockScoreDTO;
import com.mwy.stock.reponstory.dao.StockDayInfoDao;
import com.mwy.stock.reponstory.dao.modal.StockDayInfoDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component(CalculatorConsts.Trend_Calc)
public class TrendCalculator implements StockCalculator {

    @Resource
    private StockDayInfoDao stockDayInfoDao;

    @Override
    public StockScoreDTO calc(String stockNum, String date) {

        StockScoreDTO stockScoreDTO = new StockScoreDTO();
        stockScoreDTO.setStockNum(stockNum);
        stockScoreDTO.setDate(date);
        stockScoreDTO.setStrategyId(CalculatorConsts.Trend_Calc);
        stockScoreDTO.setStrategyName(CalculatorConsts.Trend_Calc_DESC);
        stockScoreDTO.setScore(-100D);
        stockScoreDTO.setScoreDesc("无");
        stockScoreDTO.setUpdateDate(new Date());

        int length = 4;
        List<StockDayInfoDO> stockDayInfos = stockDayInfoDao.selectTopN(stockNum, date, length);

        if (CollectionUtils.isEmpty(stockDayInfos) || stockDayInfos.size() < length) {
            log.error("k线太少，忽略计分.{}", stockNum);
            return stockScoreDTO;
        }

        if (!stockDayInfos.get(0).getDate().equals(date)) {
            log.error("K线无最新数据.{}", stockNum);
            return stockScoreDTO;
        }

        double height = 0;
        StringBuilder sb = new StringBuilder(":");
        StockDayInfoDO stockDayInfoDO1 = stockDayInfos.get(0);
        StockDayInfoDO stockDayInfoDO2 = stockDayInfos.get(1);
        StockDayInfoDO stockDayInfoDO3 = stockDayInfos.get(2);
        // k
        double k1 = stockDayInfoDO1.getCci() - stockDayInfoDO2.getCci();

        double k2 = stockDayInfoDO2.getCci() - stockDayInfoDO3.getCci();

        if (k1 > 0) {
            height++;
            sb.append("cciK大于0;+1");
        }
        if (k1 > k2) {
            height++;
            sb.append("cciK变大;+1");
        }

        if (k1 > k2 && k2 > 0) {
            double d = (k1 - k2) * stockDayInfoDO1.getUpDownRange() / 100;
            height +=d;
            sb.append("cciK幅度:").append(String.format("%.2f",d)).append(";");
        }
        if (stockDayInfoDO1.getCci() > 100 && stockDayInfoDO2.getCci() < 100) {
            height += 10;
            sb.append("cciK上穿100;+10");
        }

        if (stockDayInfoDO1.getCci() > -100 && stockDayInfoDO2.getCci() < -100) {
            height += 15;
            sb.append("cciK上穿-100;+15");
        }


        double macd1 = stockDayInfoDO1.getMacd() - stockDayInfoDO2.getMacd();
        double macd2 = stockDayInfoDO2.getMacd() - stockDayInfoDO3.getMacd();

        if (macd1 > 0) {
            height++;
            sb.append("macdK大于0;"+1);
        }

        if (macd1 > macd2) {
            height++;
            sb.append("macdK变大;+1");
        }

        if (macd1 > macd2 && macd2 > 0) {
            height += (macd1 - macd2) * 100;
            sb.append("macd幅度:").append(String.format("%.2f",(macd1 - macd2) * 100)).append(";");
        }

        if (stockDayInfoDO1.getMacd() > 0 && stockDayInfoDO2.getMacd() < 0) {
            height += 50;
            sb.append("macd上穿0;+50");
        }

        double volume1 = stockDayInfoDO1.getVolume() - stockDayInfoDO2.getVolume();
        double volume2 = stockDayInfoDO2.getVolume() - stockDayInfoDO3.getVolume();
        if (volume1 > 0) {
            height += 50;
            sb.append("成交量变大;+50");
        }
        if (volume1 > volume2 && volume2 > 0) {
            height += 20;
            sb.append("成交量连续变大;+20");
        }

        stockScoreDTO.setScore(height);
        stockScoreDTO.setScoreDesc(sb.toString());
        return stockScoreDTO;
    }
}
