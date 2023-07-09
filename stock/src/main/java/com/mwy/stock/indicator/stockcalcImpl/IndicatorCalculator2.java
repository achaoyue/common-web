package com.mwy.stock.indicator.stockcalcImpl;

import com.mwy.stock.config.CalculatorConsts;
import com.mwy.stock.indicator.StockCalculator;
import com.mwy.stock.modal.dto.StockScoreDTO;
import com.mwy.stock.reponstory.dao.StockDao;
import com.mwy.stock.reponstory.dao.StockDayInfoDao;
import com.mwy.stock.reponstory.dao.StockNoticeHistoryDao;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.reponstory.dao.modal.StockDayInfoDO;
import com.mwy.stock.reponstory.dao.modal.StockNoticeHistoryDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Component(CalculatorConsts.Indicator_Calc2)
public class IndicatorCalculator2 implements StockCalculator {

    @Resource
    private StockDayInfoDao stockDayInfoDao;
    @Resource
    private StockDao stockDao;
    @Resource
    private StockNoticeHistoryDao stockNoticeHistoryDao;

    @Override
    public StockScoreDTO calc(String stockNum, String date) {

        StockScoreDTO stockScoreDTO = new StockScoreDTO();
        stockScoreDTO.setStockNum(stockNum);
        stockScoreDTO.setDate(date);
        stockScoreDTO.setStrategyId(CalculatorConsts.Indicator_Calc2);
        stockScoreDTO.setStrategyName(CalculatorConsts.Indicator_Calc_DESC2);
        stockScoreDTO.setScore(-100D);
        stockScoreDTO.setScoreDesc("无");
        stockScoreDTO.setUpdateDate(new Date());

//        StockDO stockDO = stockDao.getByStockNum(stockNum);

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


        //量价齐升
        double volUp = ((int)(stockDayInfoDO1.getUpDownRange() * 100))/100;
        if (stockDayInfoDO1.getClose() > stockDayInfoDO2.getClose() && stockDayInfoDO1.getVolume() > stockDayInfoDO2.getVolume()){
            height += volUp;
            sb.append("量价齐升+"+volUp+";");

            height += stockDayInfoDO1.getTurnOverrate()/100;
            sb.append("换手率:").append(stockDayInfoDO1.getTurnOverrate()/100).append(" ");
        }

        double k1 = stockDayInfoDO1.getCci() - stockDayInfoDO2.getCci();

        double k2 = stockDayInfoDO2.getCci() - stockDayInfoDO3.getCci();

//        //k向上
//        Double k = (k1 - k2)*2/(k1+k2);
//        if (k1>0){
//            height += k;
//            sb.append("k+"+String.format("%.2f",k)+";");
//        }

        //k向上
        int kUp = 2;
        if (k1>0){
            height += kUp;
            sb.append("k线向上+"+kUp+";");
        }

        boolean ignore = k1 > 100 && k2 < -100;

        //k上穿-100
        int kUpLow100 = 3;
        if (k1>-100 && k2< -100 && !ignore){
            height += kUpLow100;
            sb.append("k线上穿-100+"+kUpLow100+";");
        }

        //k上穿100
        int kUpHigh100 = 3;
        if (k1>100 && k2< 100 && !ignore){
            height += kUpHigh100;
            sb.append("k线上穿-100+"+kUpHigh100+";");
        }

        //有通知
        StockNoticeHistoryDO noticeHistoryDO = stockNoticeHistoryDao.getByStockNum(stockNum, date);
        int notice = 4;
        if (noticeHistoryDO != null){
            height += notice;
            sb.append("今日有通知+"+notice+";");
        }


        stockScoreDTO.setScore(height);
        stockScoreDTO.setScoreDesc(sb.toString());
        return stockScoreDTO;
    }
}
