package com.mwy.stock.indicator.indicatorImpl;

import com.mwy.stock.indicator.Indicator;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDayInfoDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockMonthInfoDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockWeekInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MACD implements Indicator {
    private static final Logger LOG = LoggerFactory.getLogger(MACD.class);


    /**
     * 多头行情
     * 操作：可买入开仓或多头持仓
     *
     * @return
     */
    public boolean isBullMarket(EasyMoneyStockDayInfoDTO preStockdayinfo, EasyMoneyStockDayInfoDTO currentStockdayinfo) {

        if (isDIFAndDEAGreaterThanZero(currentStockdayinfo)
                && isUpDIFAndDEA(preStockdayinfo, currentStockdayinfo)) {
            return true;
        }
        return false;
    }

    /**
     * 空头行情
     * 卖出开仓或观望
     *
     * @return
     */
    public boolean isBearMarket(EasyMoneyStockDayInfoDTO preStockdayinfo, EasyMoneyStockDayInfoDTO currentStockdayinfo) {
        if (isDIFAndDEALessThanZero(currentStockdayinfo)
                && isDownDIFAndDEA(preStockdayinfo, currentStockdayinfo)) {
            return true;
        }
        return false;
    }

    /**
     * 上升趋势
     * 股票将上涨，可以买入开仓或多头持仓
     *
     * @return
     */
    public boolean isUpTrend(EasyMoneyStockDayInfoDTO preStockdayinfo, EasyMoneyStockDayInfoDTO currentStockdayinfo) {
        if (isDIFAndDEALessThanZero(currentStockdayinfo)
                && isUpDIFAndDEA(preStockdayinfo, currentStockdayinfo)) {
            return true;
        }
        return false;
    }

    /**
     * 下降趋势
     * 卖出开仓或观望
     *
     * @return
     */
    public boolean isDownTrend(EasyMoneyStockDayInfoDTO preStockdayinfo, EasyMoneyStockDayInfoDTO currentStockdayinfo) {
        if (isDIFAndDEAGreaterThanZero(currentStockdayinfo)
                && isDownDIFAndDEA(preStockdayinfo, currentStockdayinfo)) {
            return true;
        }
        return false;
    }

    /**
     * macd死叉
     * 卖出信号
     *
     * @return
     */
    public boolean isDIFDownCrossDEA(EasyMoneyStockDayInfoDTO preStockdayinfo, EasyMoneyStockDayInfoDTO currentStockdayinfo) {
        if (preStockdayinfo.getDif() > preStockdayinfo.getDea()
                && currentStockdayinfo.getDif() < currentStockdayinfo.getDea()) {
            return true;
        }
        return false;
    }

    /**
     * macd金叉
     * 买入信号
     *
     * @return
     */
    public boolean isDIFUpCrossDEA(EasyMoneyStockDayInfoDTO preStockdayinfo, EasyMoneyStockDayInfoDTO currentStockdayinfo) {

        if (preStockdayinfo.getDif() < preStockdayinfo.getDea()
                && currentStockdayinfo.getDif() > currentStockdayinfo.getDea()) {
            return true;
        }
        return false;
    }

    /**
     * 市场由空头转多头
     *
     * @return
     */
    public boolean isMACDGreaterThanZero(EasyMoneyStockDayInfoDTO preStockdayinfo, EasyMoneyStockDayInfoDTO currentStockdayinfo) {
        double premacd = preStockdayinfo.getMacd();
        double currentmacd = currentStockdayinfo.getMacd();
        if (premacd < 0 && currentmacd > 0) {
            return true;
        }
        return false;
    }

    /**
     * 市场由多头转空头
     *
     * @return
     */
    public boolean isMACDLessThanZero(EasyMoneyStockDayInfoDTO preStockdayinfo, EasyMoneyStockDayInfoDTO currentStockdayinfo) {
        double premacd = preStockdayinfo.getMacd();
        double currentmacd = currentStockdayinfo.getMacd();
        if (premacd > 0 && currentmacd < 0) {
            return true;
        }
        return false;
    }

    public boolean isDIFAndDEAGreaterThanZero(EasyMoneyStockDayInfoDTO stockdayinfo) {
        double dif = stockdayinfo.getDif();
        double dea = stockdayinfo.getDea();
        if (dif > 0 && dea > 0) {
            return true;
        }
        return false;
    }

    public boolean isDIFAndDEALessThanZero(EasyMoneyStockDayInfoDTO stockdayinfo) {
        double dif = stockdayinfo.getDif();
        double dea = stockdayinfo.getDea();
        if (dif < 0 && dea < 0) {
            return true;
        }
        return false;
    }

    public boolean isUpDIFAndDEA(EasyMoneyStockDayInfoDTO preStockdayinfo, EasyMoneyStockDayInfoDTO currentStockdayinfo) {

        if (isUpDIF(preStockdayinfo, currentStockdayinfo)
                && isUpDEA(preStockdayinfo, currentStockdayinfo)) {
            return true;
        }
        return false;
    }

    public boolean isDownDIFAndDEA(EasyMoneyStockDayInfoDTO preStockdayinfo, EasyMoneyStockDayInfoDTO currentStockdayinfo) {

        if (!isUpDIF(preStockdayinfo, currentStockdayinfo)
                && !isUpDEA(preStockdayinfo, currentStockdayinfo)) {
            return true;
        }
        return false;
    }

    public boolean isUpDIF(EasyMoneyStockDayInfoDTO preStockdayinfo, EasyMoneyStockDayInfoDTO currentStockdayinfo) {
        if (preStockdayinfo.getDif() < currentStockdayinfo.getDif()) {
            return true;
        }
        return false;
    }

    public boolean isUpDEA(EasyMoneyStockDayInfoDTO preStockdayinfo, EasyMoneyStockDayInfoDTO currentStockdayinfo) {
        if (preStockdayinfo.getDea() < currentStockdayinfo.getDea()) {
            return true;
        }
        return false;
    }

    /**
     * 顶背离
     *
     * @return
     */
    public Map<String, Boolean> topDivergence(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        List<EasyMoneyStockDayInfoDTO> topStockDayInfoList = new ArrayList<>();
        Map<String, Boolean> topDivergenceMap = new HashMap<>();
        for (int i = 0; i < stockdayinfoList.size(); i++) {
            if (i == 0 || i == stockdayinfoList.size() - 1) {
                continue;
            }
            EasyMoneyStockDayInfoDTO preStockDayInfo = stockdayinfoList.get(i - 1);
            EasyMoneyStockDayInfoDTO currentStockDayInfo = stockdayinfoList.get(i);
            EasyMoneyStockDayInfoDTO afterStockDayInfo = stockdayinfoList.get(i + 1);
            if (preStockDayInfo.getDif() < currentStockDayInfo.getDif()
                    && currentStockDayInfo.getDif() > afterStockDayInfo.getDif()) {
                topStockDayInfoList.add(currentStockDayInfo);
            }
        }

        for (int i = 0; i < topStockDayInfoList.size(); i++) {
            if (i == 0 || i == topStockDayInfoList.size() - 1) {
                continue;
            }
            if (topStockDayInfoList.get(i).getHigh() > topStockDayInfoList.get(i - 1).getHigh() &&
                    topStockDayInfoList.get(i).getDif() < topStockDayInfoList.get(i - 1).getDif()) {
                topDivergenceMap.put(topStockDayInfoList.get(i).getDate(), true);
            }
        }
        return topDivergenceMap;
    }

    /**
     * 底背离
     *
     * @return
     */
    public Map<String, Boolean> lowDivergence(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        List<EasyMoneyStockDayInfoDTO> lowStockDayInfoList = new ArrayList<>();
        Map<String, Boolean> lowDivergenceMap = new HashMap();
        for (int i = 0; i < stockdayinfoList.size(); i++) {
            if (i == 0 || i == stockdayinfoList.size() - 1) {
                continue;
            }
            EasyMoneyStockDayInfoDTO preStockDayInfo = stockdayinfoList.get(i - 1);
            EasyMoneyStockDayInfoDTO currentStockDayInfo = stockdayinfoList.get(i);
            EasyMoneyStockDayInfoDTO afterStockDayInfo = stockdayinfoList.get(i + 1);
            if (preStockDayInfo.getDif() > currentStockDayInfo.getDif()
                    && currentStockDayInfo.getDif() < afterStockDayInfo.getDif()) {
                lowStockDayInfoList.add(currentStockDayInfo);
            }

        }

        for (int i = 0; i < lowStockDayInfoList.size(); i++) {
            if (i == 0 || i == lowStockDayInfoList.size() - 1) {
                continue;
            }
            if (lowStockDayInfoList.get(i).getHigh() > lowStockDayInfoList.get(i - 1).getHigh() &&
                    lowStockDayInfoList.get(i).getDif() < lowStockDayInfoList.get(i - 1).getDif()) {
                lowDivergenceMap.put(lowStockDayInfoList.get(i).getDate(), true);
            }
        }
        return lowDivergenceMap;
    }

    @Override
    public List<EasyMoneyStockDayInfoDTO> getDayIndicatorList(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        List<EasyMoneyStockDayInfoDTO> newstockDayinfoList = new ArrayList<EasyMoneyStockDayInfoDTO>();
        EasyMoneyStockDayInfoDTO preStockdayinfo = null;
        double preEMA12 = 0;
        double preEMA26 = 0;
        double preDEA = 0;
        for (EasyMoneyStockDayInfoDTO stockdayinfo : stockdayinfoList) {
            if (preStockdayinfo == null) {
                preStockdayinfo = stockdayinfo;
                continue;
            }
            double ema12 = preEMA12 * 11 / 13 + stockdayinfo.getClose() * 2 / 13;
            double ema26 = preEMA26 * 25 / 27 + stockdayinfo.getClose() * 2 / 27;
            double dif = ema12 - ema26;
            double dea = preDEA * 8 / 10 + dif * 2 / 10;
            double macd = (dif - dea) * 2;
            stockdayinfo.setDif(dif);
            stockdayinfo.setDea(dea);
            stockdayinfo.setMacd(macd);

            newstockDayinfoList.add(stockdayinfo);
            preStockdayinfo = stockdayinfo;
            preEMA12 = ema12;
            preEMA26 = ema26;
            preDEA = dea;
        }
        return newstockDayinfoList;
    }

    @Override
    public List<EasyMoneyStockWeekInfoDTO> getWeekIndicatorList(List<EasyMoneyStockWeekInfoDTO> stockweekinfoList) {
        List<EasyMoneyStockWeekInfoDTO> newstockWeekinfoList = new ArrayList<EasyMoneyStockWeekInfoDTO>();
        EasyMoneyStockWeekInfoDTO preStockdayinfo = null;
        double preEMA12 = 0;
        double preEMA26 = 0;
        double preDEA = 0;
        for (EasyMoneyStockWeekInfoDTO stockWeekInfo : stockweekinfoList) {
            if (preStockdayinfo == null) {
                preStockdayinfo = stockWeekInfo;
                continue;
            }
            double ema12 = preEMA12 * 11 / 13 + stockWeekInfo.getClose() * 2 / 13;
            double ema26 = preEMA26 * 25 / 27 + stockWeekInfo.getClose() * 2 / 27;
            double dif = ema12 - ema26;
            double dea = preDEA * 8 / 10 + dif * 2 / 10;
            double macd = (dif - dea) * 2;
            stockWeekInfo.setDif(dif);
            stockWeekInfo.setDea(dea);
            stockWeekInfo.setMacd(macd);

            newstockWeekinfoList.add(stockWeekInfo);
            preStockdayinfo = stockWeekInfo;
            preEMA12 = ema12;
            preEMA26 = ema26;
            preDEA = dea;
        }
        return newstockWeekinfoList;
    }

    @Override
    public List<EasyMoneyStockMonthInfoDTO> getMonthIndicatorList(List<EasyMoneyStockMonthInfoDTO> stockmonthinfoList) {
        List<EasyMoneyStockMonthInfoDTO> newstockMonthinfoList = new ArrayList<EasyMoneyStockMonthInfoDTO>();
        EasyMoneyStockMonthInfoDTO preStockdayinfo = null;
        double preEMA12 = 0;
        double preEMA26 = 0;
        double preDEA = 0;
        for (EasyMoneyStockMonthInfoDTO stockMonthInfo : stockmonthinfoList) {
            if (preStockdayinfo == null) {
                preStockdayinfo = stockMonthInfo;
                continue;
            }
            double ema12 = preEMA12 * 11 / 13 + stockMonthInfo.getClose() * 2 / 13;
            double ema26 = preEMA26 * 25 / 27 + stockMonthInfo.getClose() * 2 / 27;
            double dif = ema12 - ema26;
            double dea = preDEA * 8 / 10 + dif * 2 / 10;
            double macd = (dif - dea) * 2;
            stockMonthInfo.setDif(dif);
            stockMonthInfo.setDea(dea);
            stockMonthInfo.setMacd(macd);

            newstockMonthinfoList.add(stockMonthInfo);
            preStockdayinfo = stockMonthInfo;
            preEMA12 = ema12;
            preEMA26 = ema26;
            preDEA = dea;
        }
        return newstockMonthinfoList;
    }
}
