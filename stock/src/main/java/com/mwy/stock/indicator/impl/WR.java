package com.mwy.stock.indicator.impl;

import com.mwy.stock.indicator.Indicator;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDayInfoDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockMonthInfoDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockWeekInfoDTO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class WR implements Indicator {

    @Override
    public List<EasyMoneyStockDayInfoDTO> getDayIndicatorList(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        Queue<EasyMoneyStockDayInfoDTO> queue6 = new LinkedList<EasyMoneyStockDayInfoDTO>();

        Queue<EasyMoneyStockDayInfoDTO> queue10 = new LinkedList<EasyMoneyStockDayInfoDTO>();

        double higherPrice6 = 0;
        double lowerPrice6 = 0;
        double higherPrice10 = 0;
        double lowerPrice10 = 0;

        List<EasyMoneyStockDayInfoDTO> newstockDayinfoList = new ArrayList<EasyMoneyStockDayInfoDTO>();
        for (EasyMoneyStockDayInfoDTO stockdayinfo : stockdayinfoList) {
            higherPrice6 = 0;
            lowerPrice6 = 1000000;
            higherPrice10 = 0;
            lowerPrice10 = 1000000;

            queue6.offer(stockdayinfo);
            queue10.offer(stockdayinfo);


            if (queue6.size() == 6) {
                for (EasyMoneyStockDayInfoDTO sdi : queue6) {
                    double currhigh = sdi.getHigh();
                    double currlow = sdi.getLow();

                    if (currhigh > higherPrice6) {
                        higherPrice6 = currhigh;
                    }
                    if (currlow < lowerPrice6) {
                        lowerPrice6 = currlow;
                    }
                }
                queue6.poll();
            }
            if (queue10.size() == 10) {
                for (EasyMoneyStockDayInfoDTO sdi : queue10) {
                    double currhigh = sdi.getHigh();
                    double currlow = sdi.getLow();

                    if (currhigh > higherPrice10) {
                        higherPrice10 = currhigh;
                    }

                    if (currlow < lowerPrice10) {
                        lowerPrice10 = currlow;
                    }
                }
                queue10.poll();
            }
            double currentClosePrice = stockdayinfo.getClose();
            double wr10 = 100 * (higherPrice10 - currentClosePrice) / (higherPrice10 - lowerPrice10);
            double wr6 = 100 * (higherPrice6 - currentClosePrice) / (higherPrice6 - lowerPrice6);
            stockdayinfo.setWr6(wr6);
            stockdayinfo.setWr10(wr10);
            newstockDayinfoList.add(stockdayinfo);
        }
        return newstockDayinfoList;
    }

    @Override
    public List<EasyMoneyStockWeekInfoDTO> getWeekIndicatorList(List<EasyMoneyStockWeekInfoDTO> stockweekinfoList) {
        Queue<EasyMoneyStockWeekInfoDTO> queue6 = new LinkedList<EasyMoneyStockWeekInfoDTO>();

        Queue<EasyMoneyStockWeekInfoDTO> queue10 = new LinkedList<EasyMoneyStockWeekInfoDTO>();

        double higherPrice6 = 0;
        double lowerPrice6 = 0;
        double higherPrice10 = 0;
        double lowerPrice10 = 0;

        List<EasyMoneyStockWeekInfoDTO> newstockDayinfoList = new ArrayList<EasyMoneyStockWeekInfoDTO>();
        for (EasyMoneyStockWeekInfoDTO stockweekinfo : stockweekinfoList) {
            higherPrice6 = 0;
            lowerPrice6 = 1000000;
            higherPrice10 = 0;
            lowerPrice10 = 1000000;

            queue6.offer(stockweekinfo);
            queue10.offer(stockweekinfo);


            if (queue6.size() == 6) {
                for (EasyMoneyStockWeekInfoDTO sdi : queue6) {
                    double currhigh = sdi.getHigh();
                    double currlow = sdi.getLow();

                    if (currhigh > higherPrice6) {
                        higherPrice6 = currhigh;
                    }
                    if (currlow < lowerPrice6) {
                        lowerPrice6 = currlow;
                    }
                }
                queue6.poll();
            }
            if (queue10.size() == 10) {
                for (EasyMoneyStockWeekInfoDTO sdi : queue10) {
                    double currhigh = sdi.getHigh();
                    double currlow = sdi.getLow();

                    if (currhigh > higherPrice10) {
                        higherPrice10 = currhigh;
                    }

                    if (currlow < lowerPrice10) {
                        lowerPrice10 = currlow;
                    }
                }
                queue10.poll();
            }
            double currentClosePrice = stockweekinfo.getClose();
            double wr10 = 100 * (higherPrice10 - currentClosePrice) / (higherPrice10 - lowerPrice10);
            double wr6 = 100 * (higherPrice6 - currentClosePrice) / (higherPrice6 - lowerPrice6);
            stockweekinfo.setWr6(wr6);
            stockweekinfo.setWr10(wr10);
            newstockDayinfoList.add(stockweekinfo);
        }
        return newstockDayinfoList;
    }

    @Override
    public List<EasyMoneyStockMonthInfoDTO> getMonthIndicatorList(List<EasyMoneyStockMonthInfoDTO> stockmonthinfoList) {
        Queue<EasyMoneyStockMonthInfoDTO> queue6 = new LinkedList<EasyMoneyStockMonthInfoDTO>();

        Queue<EasyMoneyStockMonthInfoDTO> queue10 = new LinkedList<EasyMoneyStockMonthInfoDTO>();

        double higherPrice6 = 0;
        double lowerPrice6 = 0;
        double higherPrice10 = 0;
        double lowerPrice10 = 0;

        List<EasyMoneyStockMonthInfoDTO> newstockDayinfoList = new ArrayList<EasyMoneyStockMonthInfoDTO>();
        for (EasyMoneyStockMonthInfoDTO stockmonthinfo : stockmonthinfoList) {
            higherPrice6 = 0;
            lowerPrice6 = 1000000;
            higherPrice10 = 0;
            lowerPrice10 = 1000000;

            queue6.offer(stockmonthinfo);
            queue10.offer(stockmonthinfo);


            if (queue6.size() == 6) {
                for (EasyMoneyStockMonthInfoDTO sdi : queue6) {
                    double currhigh = sdi.getHigh();
                    double currlow = sdi.getLow();

                    if (currhigh > higherPrice6) {
                        higherPrice6 = currhigh;
                    }
                    if (currlow < lowerPrice6) {
                        lowerPrice6 = currlow;
                    }
                }
                queue6.poll();
            }
            if (queue10.size() == 10) {
                for (EasyMoneyStockMonthInfoDTO sdi : queue10) {
                    double currhigh = sdi.getHigh();
                    double currlow = sdi.getLow();

                    if (currhigh > higherPrice10) {
                        higherPrice10 = currhigh;
                    }

                    if (currlow < lowerPrice10) {
                        lowerPrice10 = currlow;
                    }
                }
                queue10.poll();
            }
            double currentClosePrice = stockmonthinfo.getClose();
            double wr10 = 100 * (higherPrice10 - currentClosePrice) / (higherPrice10 - lowerPrice10);
            double wr6 = 100 * (higherPrice6 - currentClosePrice) / (higherPrice6 - lowerPrice6);
            stockmonthinfo.setWr6(wr6);
            stockmonthinfo.setWr10(wr10);
            newstockDayinfoList.add(stockmonthinfo);
        }
        return newstockDayinfoList;
    }
}
