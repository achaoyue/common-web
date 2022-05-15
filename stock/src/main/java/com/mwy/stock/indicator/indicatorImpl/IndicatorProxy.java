package com.mwy.stock.indicator.indicatorImpl;


import com.mwy.stock.indicator.Indicator;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDayInfoDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockMonthInfoDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockWeekInfoDTO;

import java.util.List;

/**
 * 指标代理
 */
public class IndicatorProxy implements Indicator {

    /**
     * 用来获取一系列指标
     * 用户新增加一项指标，只需在该类中添加即可
     *
     * @param stockDayInfoList
     * @return
     */
    @Override
    public List<EasyMoneyStockDayInfoDTO> getDayIndicatorList(List<EasyMoneyStockDayInfoDTO> stockDayInfoList) {
        Indicator technicalIndicators = new WR();
        stockDayInfoList = technicalIndicators.getDayIndicatorList(stockDayInfoList);

        technicalIndicators = new MACD();
        stockDayInfoList = technicalIndicators.getDayIndicatorList(stockDayInfoList);

        technicalIndicators = new KDJ();
        stockDayInfoList = technicalIndicators.getDayIndicatorList(stockDayInfoList);

        technicalIndicators = new MA();
        stockDayInfoList = technicalIndicators.getDayIndicatorList(stockDayInfoList);

        technicalIndicators = new RSI();
        stockDayInfoList = technicalIndicators.getDayIndicatorList(stockDayInfoList);

        technicalIndicators = new CCI();
        stockDayInfoList = technicalIndicators.getDayIndicatorList(stockDayInfoList);
        return stockDayInfoList;
    }

    @Override
    public List<EasyMoneyStockWeekInfoDTO> getWeekIndicatorList(List<EasyMoneyStockWeekInfoDTO> stockweekinfoList) {
        Indicator technicalIndicators = new WR();
        stockweekinfoList = technicalIndicators.getWeekIndicatorList(stockweekinfoList);

        technicalIndicators = new MACD();
        stockweekinfoList = technicalIndicators.getWeekIndicatorList(stockweekinfoList);

        technicalIndicators = new KDJ();
        stockweekinfoList = technicalIndicators.getWeekIndicatorList(stockweekinfoList);

        technicalIndicators = new MA();
        stockweekinfoList = technicalIndicators.getWeekIndicatorList(stockweekinfoList);

        technicalIndicators = new RSI();
        stockweekinfoList = technicalIndicators.getWeekIndicatorList(stockweekinfoList);

        technicalIndicators = new CCI();
        stockweekinfoList = technicalIndicators.getWeekIndicatorList(stockweekinfoList);
        return stockweekinfoList;
    }

    @Override
    public List<EasyMoneyStockMonthInfoDTO> getMonthIndicatorList(List<EasyMoneyStockMonthInfoDTO> stockmonthinfoList) {
        Indicator technicalIndicators = new WR();
        stockmonthinfoList = technicalIndicators.getMonthIndicatorList(stockmonthinfoList);

        technicalIndicators = new MACD();
        stockmonthinfoList = technicalIndicators.getMonthIndicatorList(stockmonthinfoList);

        technicalIndicators = new KDJ();
        stockmonthinfoList = technicalIndicators.getMonthIndicatorList(stockmonthinfoList);

        technicalIndicators = new MA();
        stockmonthinfoList = technicalIndicators.getMonthIndicatorList(stockmonthinfoList);

        technicalIndicators = new RSI();
        stockmonthinfoList = technicalIndicators.getMonthIndicatorList(stockmonthinfoList);

        technicalIndicators = new CCI();
        stockmonthinfoList = technicalIndicators.getMonthIndicatorList(stockmonthinfoList);
        return stockmonthinfoList;
    }
}
