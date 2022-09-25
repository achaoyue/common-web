package com.mwy.stock.indicator.stockcalcImpl;

import com.mwy.stock.config.CalculatorConsts;
import com.mwy.stock.indicator.StockCalculator;
import com.mwy.stock.modal.dto.StockScoreDTO;
import com.mwy.stock.reponstory.dao.StockDayInfoDao;
import com.mwy.stock.reponstory.dao.modal.StockDayInfoDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Component(CalculatorConsts.Amplitude_Calc)
public class AmplitudeCalculator implements StockCalculator {
    @Resource
    private StockDayInfoDao stockDayInfoDao;

    @Override
    public StockScoreDTO calc(String stockNum, String date) {
        StockScoreDTO stockScoreDTO = new StockScoreDTO();
        stockScoreDTO.setStockNum(stockNum);
        stockScoreDTO.setDate(date);
        stockScoreDTO.setStrategyId(CalculatorConsts.Amplitude_Calc);
        stockScoreDTO.setStrategyName(CalculatorConsts.Amplitude_Calc_DESC);
        stockScoreDTO.setScore(-100D);
        stockScoreDTO.setScoreDesc("无");
        stockScoreDTO.setUpdateDate(new Date());

        int length = 30;
        List<StockDayInfoDO> stockDayInfos = stockDayInfoDao.selectTopN(stockNum, date, length);

        if (CollectionUtils.isEmpty(stockDayInfos) || stockDayInfos.size() < length) {
            log.error("k线太少，忽略计分.{}", stockNum);
            return stockScoreDTO;
        }

        if (!stockDayInfos.get(0).getDate().equals(date)) {
            log.error("K线无最新数据.{}", stockNum);
            return stockScoreDTO;
        }

        double[] closeArray = stockDayInfos.stream()
                .mapToDouble(e -> e.getClose())
                .toArray();

        double[] cciArray = stockDayInfos.stream()
                .mapToDouble(e -> e.getCci())
                .toArray();

        double[] macdArray = stockDayInfos.stream()
                .mapToDouble(e -> e.getMacd())
                .toArray();


        double[] volumArray = stockDayInfos.stream()
                .mapToDouble(e -> e.getVolume())
                .toArray();


        double[] kdjArray = stockDayInfos.stream()
                .mapToDouble(e -> e.getJ())
                .toArray();

        double[] ma5Array = stockDayInfos.stream()
                .mapToDouble(e -> e.getMa5())
                .toArray();


        double cciX = NumberUtils.max(cciArray) - NumberUtils.min(cciArray);
        double macdX = NumberUtils.max(macdArray) - NumberUtils.min(macdArray);
        double volumX = NumberUtils.max(volumArray) - NumberUtils.min(volumArray);
        double kdjX = NumberUtils.max(kdjArray) - NumberUtils.min(kdjArray);
        double ma5X = NumberUtils.max(ma5Array) - NumberUtils.min(ma5Array);
        double closeX = NumberUtils.max(closeArray) - NumberUtils.min(closeArray);

        StockDayInfoDO stockDayInfo1 = stockDayInfos.get(0);
        StockDayInfoDO stockDayInfo2 = stockDayInfos.get(1);
        //cciK
        double cciK = (stockDayInfo1.getCci() - stockDayInfo2.getCci())/cciX*100;

        //macdK
        double macdK = (stockDayInfo1.getMacd() - stockDayInfo2.getMacd())/macdX*100;

        //volumeK
        double volumeK = (stockDayInfo1.getVolume() - stockDayInfo2.getVolume())/volumX*100;

        //kdjK
        double kdjK = (stockDayInfo1.getJ() - stockDayInfo2.getJ())/kdjX*100;

        //kdj金叉
        double goldKdj = 0;
        if ((stockDayInfo1.getK()-stockDayInfo1.getJ()) * (stockDayInfo2.getK()-stockDayInfo2.getJ()) < 0){
            if (stockDayInfo1.getK() - stockDayInfo2.getK() < 0){
                goldKdj = -50;
            }else{
                goldKdj = 50;
            }
        }


        //ma5K
        double ma5K = (stockDayInfo1.getMa5() - stockDayInfo2.getMa5())/ma5X*100;

        //剩余空间
        double closeK = (NumberUtils.max(closeArray) - stockDayInfo1.getClose()) / closeX * 100;

        //cci剩余空间
        double cciLeft = (NumberUtils.max(cciArray) - stockDayInfo1.getCci())/cciX*100;

        double right[] = new double[]{
                1.3,//0 cciK
                0.8,//1 mackK
                0.7,//2 volumeK
                1.5,//3 kdjK
                1.5,//4 ma5K
                0.1,//5 closeK
                0, //6 cciLeft
                1.1 //7 goldKdj
        };

        stockScoreDTO.setScore((cciK*right[0]
                + macdK*right[1]
                + volumeK*right[2]
                + kdjK*right[3]
                + ma5K*right[4]
                + closeK * right[5]
                + cciLeft * right[6]
                + goldKdj*right[7]));

        stockScoreDTO.setScoreDesc("cciK:"+(int)cciK
                +" macdK:"+ (int)macdK
                + " volumeK:" + (int)volumeK
                + " kdjK:" + (int)kdjK
                + " ma5K:"+ (int)ma5K
                + " closeK:"+ (int)closeK
                + " cciLeft:" + (int)cciLeft
                + " goldKdj:" + (int)goldKdj);

        return stockScoreDTO;
    }
}
