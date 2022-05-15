package com.mwy.stock.indicator.indicatorImpl;

import com.mwy.stock.indicator.Indicator;
import com.mwy.stock.indicator.Indicators;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDayInfoDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockMonthInfoDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockWeekInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CCI implements Indicator {
    private static final Logger LOG = LoggerFactory.getLogger(CCI.class);

    @Override
    public List<EasyMoneyStockDayInfoDTO> getDayIndicatorList(List<EasyMoneyStockDayInfoDTO> stockDayInfoList) {
        double[] highArr = stockDayInfoList.stream()
                .mapToDouble(e -> e.getHigh())
                .toArray();

        double[] lowArr = stockDayInfoList.stream()
                .mapToDouble(e -> e.getLow())
                .toArray();

        double[] closeArr = stockDayInfoList.stream()
                .mapToDouble(e -> e.getClose())
                .toArray();

        double[] cci = new Indicators().cci(highArr, lowArr, closeArr, 14);
        for (int i = 0; i < cci.length; i++) {
            stockDayInfoList.get(i).setCci(cci[i]);
        }

        return stockDayInfoList;
    }

    @Override
    public List<EasyMoneyStockWeekInfoDTO> getWeekIndicatorList(List<EasyMoneyStockWeekInfoDTO> stockWeekInfoList) {

        double[] highArr = stockWeekInfoList.stream()
                .mapToDouble(e -> e.getHigh())
                .toArray();

        double[] lowArr = stockWeekInfoList.stream()
                .mapToDouble(e -> e.getLow())
                .toArray();

        double[] closeArr = stockWeekInfoList.stream()
                .mapToDouble(e -> e.getClose())
                .toArray();

        double[] cci = new Indicators().cci(highArr, lowArr, closeArr, 14);
        for (int i = 0; i < cci.length; i++) {
            stockWeekInfoList.get(i).setCci(cci[i]);
        }

        return stockWeekInfoList;
    }

    @Override
    public List<EasyMoneyStockMonthInfoDTO> getMonthIndicatorList(List<EasyMoneyStockMonthInfoDTO> stockMonthInfoList) {
        double[] highArr = stockMonthInfoList.stream()
                .mapToDouble(e -> e.getHigh())
                .toArray();

        double[] lowArr = stockMonthInfoList.stream()
                .mapToDouble(e -> e.getLow())
                .toArray();

        double[] closeArr = stockMonthInfoList.stream()
                .mapToDouble(e -> e.getClose())
                .toArray();

        double[] cci = new Indicators().cci(highArr, lowArr, closeArr, 14);
        for (int i = 0; i < cci.length; i++) {
            stockMonthInfoList.get(i).setCci(cci[i]);
        }
        return stockMonthInfoList;
    }
}
