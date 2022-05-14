package com.mwy.stock.indicator.impl;

import com.mwy.stock.indicator.Indicator;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDayInfoDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockMonthInfoDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockWeekInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MA implements Indicator {
    private static final Logger LOG = LoggerFactory.getLogger(MA.class);

    public boolean isGoldenCross(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        if (isUpMAShotAndLong(stockdayinfoList) && isShortUpCrossLong(stockdayinfoList)) {
            return true;
        }
        return false;
    }

    public boolean isDeathCross(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        if (!isUpMAShotAndLong(stockdayinfoList) && isShortDownCrossLong(stockdayinfoList)) {
            return true;
        }
        return false;
    }

    /**
     * 技术回档
     *
     * @param stockdayinfoList
     * @return
     */
    public boolean isTechnicalreturn(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        if (isDownMA10(stockdayinfoList) && isUpMA20(stockdayinfoList)) {
            return true;
        }
        return false;
    }

    public boolean isShortUpCrossLong(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        EasyMoneyStockDayInfoDTO stockdayinfofirst = stockdayinfoList.get(stockdayinfoListSize - 2);
        EasyMoneyStockDayInfoDTO stockdayinfolast = stockdayinfoList.get(stockdayinfoListSize - 1);
        if (isMA5UpCrossMA10(stockdayinfofirst, stockdayinfolast)
                || isMA10UpCrossMA30(stockdayinfofirst, stockdayinfolast)) {
            return true;
        }
        return false;

    }

    public boolean isShortDownCrossLong(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        EasyMoneyStockDayInfoDTO stockdayinfofirst = stockdayinfoList.get(stockdayinfoListSize - 2);
        EasyMoneyStockDayInfoDTO stockdayinfolast = stockdayinfoList.get(stockdayinfoListSize - 1);
        if (isMA5DownCrossMA10(stockdayinfofirst, stockdayinfolast)
                || isMA10DownCrossMA30(stockdayinfofirst, stockdayinfolast)) {
            return true;
        }
        return false;

    }

    public Boolean isMA5DownCrossMA10(EasyMoneyStockDayInfoDTO stockdayinfofirst, EasyMoneyStockDayInfoDTO stockdayinfolast) {
        Double firstMa5 = stockdayinfofirst.getMa5();
        Double firstMa10 = stockdayinfofirst.getMa10();
        Double lastMa5 = stockdayinfolast.getMa5();
        Double lastMa10 = stockdayinfolast.getMa10();
        if (firstMa5 > firstMa10 && lastMa5 < lastMa10) {
            return true;
        }
        return false;

    }

    public Boolean isMA10DownCrossMA30(EasyMoneyStockDayInfoDTO stockdayinfofirst, EasyMoneyStockDayInfoDTO stockdayinfolast) {
        Double firstMa10 = stockdayinfofirst.getMa10();
        Double firstMa30 = stockdayinfofirst.getMa30();
        Double lastMa10 = stockdayinfolast.getMa10();
        Double lastMa30 = stockdayinfolast.getMa30();
        if (firstMa10 > firstMa30 && lastMa10 < lastMa30) {
            return true;
        }
        return false;

    }

    public Boolean isMA5UpCrossMA10(EasyMoneyStockDayInfoDTO stockdayinfofirst, EasyMoneyStockDayInfoDTO stockdayinfolast) {
        Double firstMa5 = stockdayinfofirst.getMa5();
        Double firstMa10 = stockdayinfofirst.getMa10();
        Double lastMa5 = stockdayinfolast.getMa5();
        Double lastMa10 = stockdayinfolast.getMa10();
        if (firstMa5 < firstMa10 && lastMa5 > lastMa10) {
            return true;
        }
        return false;

    }

    public Boolean isMA10UpCrossMA30(EasyMoneyStockDayInfoDTO stockdayinfofirst, EasyMoneyStockDayInfoDTO stockdayinfolast) {
        Double firstMa10 = stockdayinfofirst.getMa10();
        Double firstMa30 = stockdayinfofirst.getMa30();
        Double lastMa10 = stockdayinfolast.getMa10();
        Double lastMa30 = stockdayinfolast.getMa30();
        if (firstMa10 < firstMa30 && lastMa10 > lastMa30) {
            return true;
        }
        return false;

    }

    public Boolean isUpMAShotAndLong(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        return isUpMA5(stockdayinfoList) && isUpMA10(stockdayinfoList) && isUpMA20(stockdayinfoList)
                && isUpMA30(stockdayinfoList);
    }

    public boolean isUpMA30(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        double preMa30 = stockdayinfoList.get(stockdayinfoListSize - 2).getMa30();
        double ma30 = stockdayinfoList.get(stockdayinfoListSize - 1).getMa30();
        if (preMa30 < ma30) {
            return true;
        }
        return false;
    }

    public boolean isUpMA20(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        double preMa20 = stockdayinfoList.get(stockdayinfoListSize - 2).getMa20();
        double ma20 = stockdayinfoList.get(stockdayinfoListSize - 1).getMa20();
        if (preMa20 < ma20) {
            return true;
        }
        return false;
    }

    public boolean isUpMA10(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        double preMa10 = stockdayinfoList.get(stockdayinfoListSize - 2).getMa10();
        double ma10 = stockdayinfoList.get(stockdayinfoListSize - 1).getMa10();
        if (preMa10 < ma10) {
            return true;
        }
        return false;
    }

    public boolean isDownMA10(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        double preMa10 = stockdayinfoList.get(stockdayinfoListSize - 2).getMa10();
        double ma10 = stockdayinfoList.get(stockdayinfoListSize - 1).getMa10();
        if (preMa10 > ma10) {
            return true;
        }
        return false;
    }

    public boolean isUpMA5(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        int stockdayinfoListSize = stockdayinfoList.size();
        double preMa5 = stockdayinfoList.get(stockdayinfoListSize - 2).getMa5();
        double ma5 = stockdayinfoList.get(stockdayinfoListSize - 1).getMa5();
        if (preMa5 < ma5) {
            return true;
        }
        return false;
    }

    @Override
    public List<EasyMoneyStockDayInfoDTO> getDayIndicatorList(List<EasyMoneyStockDayInfoDTO> stockdayinfoList) {
        Queue<Double> queue5 = new LinkedList<Double>();
        Queue<Double> queue10 = new LinkedList<Double>();
        Queue<Double> queue20 = new LinkedList<Double>();
        Queue<Double> queue30 = new LinkedList<Double>();
        Queue<Double> queue60 = new LinkedList<Double>();
        Queue<Double> queue120 = new LinkedList<Double>();
        Queue<Double> queue200 = new LinkedList<Double>();
        Queue<Double> queue250 = new LinkedList<Double>();
        Queue<Double> queueVolume120 = new LinkedList<Double>();
        double subMA5 = 0;
        double subMA10 = 0;
        double subMA20 = 0;
        double subMA30 = 0;
        double subMA60 = 0;
        double subMA120 = 0;
        double subMA200 = 0;
        double subMA250 = 0;
        double subVolume120 = 0;
        List<EasyMoneyStockDayInfoDTO> newstockDayinfoList = new ArrayList<EasyMoneyStockDayInfoDTO>();
        for (EasyMoneyStockDayInfoDTO stockdayinfo : stockdayinfoList) {
            subMA5 = 0;
            subMA10 = 0;
            subMA20 = 0;
            subMA30 = 0;
            subMA60 = 0;
            subMA120 = 0;
            subMA200 = 0;
            subMA250 = 0;
            subVolume120 = 0;

            queue5.offer(stockdayinfo.getClose());
            queue10.offer(stockdayinfo.getClose());
            queue20.offer(stockdayinfo.getClose());
            queue30.offer(stockdayinfo.getClose());
            queue60.offer(stockdayinfo.getClose());
            queue120.offer(stockdayinfo.getClose());
            queue200.offer(stockdayinfo.getClose());
            queue250.offer(stockdayinfo.getClose());
            queueVolume120.offer(stockdayinfo.getVolume());
            if (queue5.size() == 5) {
                for (Double d : queue5) {
                    subMA5 += d;
                }
                stockdayinfo.setMa5(subMA5 / 5);
                queue5.poll();
            }
            if (queue10.size() == 10) {
                for (Double d : queue10) {
                    subMA10 += d;
                }
                stockdayinfo.setMa10(subMA10 / 10);
                queue10.poll();
            }
            if (queue20.size() == 20) {
                for (Double d : queue20) {
                    subMA20 += d;
                }
                stockdayinfo.setMa20(subMA20 / 20);
                queue20.poll();
            }
            if (queue30.size() == 30) {
                for (Double d : queue30) {
                    subMA30 += d;
                }
                stockdayinfo.setMa30(subMA30 / 30);
                queue30.poll();
            }
            if (queue60.size() == 60) {
                for (Double d : queue60) {
                    subMA60 += d;
                }
                stockdayinfo.setMa60(subMA60 / 60);
                queue60.poll();
            }
            if (queue120.size() == 120) {
                for (Double d : queue120) {
                    subMA120 += d;
                }
                stockdayinfo.setMa120(subMA120 / 120);
                queue120.poll();
            }
            if (queue200.size() == 200) {
                for (Double d : queue200) {
                    subMA200 += d;
                }
                stockdayinfo.setMa200(subMA200 / 200);
                queue200.poll();
            }
            if (queue250.size() == 250) {
                for (Double d : queue250) {
                    subMA250 += d;
                }
                stockdayinfo.setMa250(subMA250 / 250);
                queue250.poll();
            }
            if (queueVolume120.size() == 120) {
                for (Double d : queueVolume120) {
                    subVolume120 += d;
                }
                stockdayinfo.setVolume120(subVolume120 / 120);
                queueVolume120.poll();
            }
            newstockDayinfoList.add(stockdayinfo);
        }
        return newstockDayinfoList;
    }

    @Override
    public List<EasyMoneyStockWeekInfoDTO> getWeekIndicatorList(List<EasyMoneyStockWeekInfoDTO> stockweekinfoList) {
        Queue<Double> queue5 = new LinkedList<Double>();
        Queue<Double> queue10 = new LinkedList<Double>();
        Queue<Double> queue20 = new LinkedList<Double>();
        Queue<Double> queue30 = new LinkedList<Double>();
        Queue<Double> queue60 = new LinkedList<Double>();
        Queue<Double> queue120 = new LinkedList<Double>();
        Queue<Double> queue200 = new LinkedList<Double>();
        Queue<Double> queue250 = new LinkedList<Double>();
        double subMA5 = 0;
        double subMA10 = 0;
        double subMA20 = 0;
        double subMA30 = 0;
        double subMA60 = 0;
        double subMA120 = 0;
        double subMA200 = 0;
        double subMA250 = 0;
        List<EasyMoneyStockWeekInfoDTO> newstockWeekinfoList = new ArrayList<EasyMoneyStockWeekInfoDTO>();
        for (EasyMoneyStockWeekInfoDTO stockWeekInfo : stockweekinfoList) {
            subMA5 = 0;
            subMA10 = 0;
            subMA20 = 0;
            subMA30 = 0;
            subMA60 = 0;
            subMA120 = 0;
            subMA200 = 0;
            subMA250 = 0;

            queue5.offer(stockWeekInfo.getClose());
            queue10.offer(stockWeekInfo.getClose());
            queue20.offer(stockWeekInfo.getClose());
            queue30.offer(stockWeekInfo.getClose());
            queue60.offer(stockWeekInfo.getClose());
            queue120.offer(stockWeekInfo.getClose());
            queue200.offer(stockWeekInfo.getClose());
            queue250.offer(stockWeekInfo.getClose());
            if (queue5.size() == 5) {
                for (Double d : queue5) {
                    subMA5 += d;
                }
                stockWeekInfo.setMa5(subMA5 / 5);
                queue5.poll();
            }
            if (queue10.size() == 10) {
                for (Double d : queue10) {
                    subMA10 += d;
                }
                stockWeekInfo.setMa10(subMA10 / 10);
                queue10.poll();
            }
            if (queue20.size() == 20) {
                for (Double d : queue20) {
                    subMA20 += d;
                }
                stockWeekInfo.setMa20(subMA20 / 20);
                queue20.poll();
            }
            if (queue30.size() == 30) {
                for (Double d : queue30) {
                    subMA30 += d;
                }
                stockWeekInfo.setMa30(subMA30 / 30);
                queue30.poll();
            }
            if (queue60.size() == 60) {
                for (Double d : queue60) {
                    subMA60 += d;
                }
                stockWeekInfo.setMa60(subMA60 / 60);
                queue60.poll();
            }
            if (queue120.size() == 120) {
                for (Double d : queue120) {
                    subMA120 += d;
                }
                stockWeekInfo.setMa120(subMA120 / 120);
                queue120.poll();
            }
            if (queue200.size() == 200) {
                for (Double d : queue200) {
                    subMA200 += d;
                }
                stockWeekInfo.setMa200(subMA200 / 200);
                queue200.poll();
            }
            if (queue250.size() == 250) {
                for (Double d : queue250) {
                    subMA250 += d;
                }
                stockWeekInfo.setMa250(subMA250 / 250);
                queue250.poll();
            }
            newstockWeekinfoList.add(stockWeekInfo);
        }
        return newstockWeekinfoList;
    }

    @Override
    public List<EasyMoneyStockMonthInfoDTO> getMonthIndicatorList(List<EasyMoneyStockMonthInfoDTO> stockmonthinfoList) {
        Queue<Double> queue5 = new LinkedList<Double>();
        Queue<Double> queue10 = new LinkedList<Double>();
        Queue<Double> queue20 = new LinkedList<Double>();
        Queue<Double> queue30 = new LinkedList<Double>();
        Queue<Double> queue60 = new LinkedList<Double>();
        Queue<Double> queue120 = new LinkedList<Double>();
        Queue<Double> queue200 = new LinkedList<Double>();
        Queue<Double> queue250 = new LinkedList<Double>();
        double subMA5 = 0;
        double subMA10 = 0;
        double subMA20 = 0;
        double subMA30 = 0;
        double subMA60 = 0;
        double subMA120 = 0;
        double subMA200 = 0;
        double subMA250 = 0;
        List<EasyMoneyStockMonthInfoDTO> newstockMonthinfoList = new ArrayList<EasyMoneyStockMonthInfoDTO>();
        for (EasyMoneyStockMonthInfoDTO stockMonthInfo : stockmonthinfoList) {
            subMA5 = 0;
            subMA10 = 0;
            subMA20 = 0;
            subMA30 = 0;
            subMA60 = 0;
            subMA120 = 0;
            subMA200 = 0;
            subMA250 = 0;

            queue5.offer(stockMonthInfo.getClose());
            queue10.offer(stockMonthInfo.getClose());
            queue20.offer(stockMonthInfo.getClose());
            queue30.offer(stockMonthInfo.getClose());
            queue60.offer(stockMonthInfo.getClose());
            queue120.offer(stockMonthInfo.getClose());
            queue200.offer(stockMonthInfo.getClose());
            queue250.offer(stockMonthInfo.getClose());
            if (queue5.size() == 5) {
                for (Double d : queue5) {
                    subMA5 += d;
                }
                stockMonthInfo.setMa5(subMA5 / 5);
                queue5.poll();
            }
            if (queue10.size() == 10) {
                for (Double d : queue10) {
                    subMA10 += d;
                }
                stockMonthInfo.setMa10(subMA10 / 10);
                queue10.poll();
            }
            if (queue20.size() == 20) {
                for (Double d : queue20) {
                    subMA20 += d;
                }
                stockMonthInfo.setMa20(subMA20 / 20);
                queue20.poll();
            }
            if (queue30.size() == 30) {
                for (Double d : queue30) {
                    subMA30 += d;
                }
                stockMonthInfo.setMa30(subMA30 / 30);
                queue30.poll();
            }
            if (queue60.size() == 60) {
                for (Double d : queue60) {
                    subMA60 += d;
                }
                stockMonthInfo.setMa60(subMA60 / 60);
                queue60.poll();
            }
            if (queue120.size() == 120) {
                for (Double d : queue120) {
                    subMA120 += d;
                }
                stockMonthInfo.setMa120(subMA120 / 120);
                queue120.poll();
            }
            if (queue200.size() == 200) {
                for (Double d : queue200) {
                    subMA200 += d;
                }
                stockMonthInfo.setMa200(subMA200 / 200);
                queue200.poll();
            }
            if (queue250.size() == 250) {
                for (Double d : queue250) {
                    subMA250 += d;
                }
                stockMonthInfo.setMa250(subMA250 / 250);
                queue250.poll();
            }
            newstockMonthinfoList.add(stockMonthInfo);
        }
        return newstockMonthinfoList;
    }
}
