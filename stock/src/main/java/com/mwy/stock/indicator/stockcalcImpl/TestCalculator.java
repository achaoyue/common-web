package com.mwy.stock.indicator.stockcalcImpl;

import com.google.common.collect.Lists;
import com.mwy.stock.indicator.AbsStockCalculator;
import com.mwy.stock.modal.dto.StockScoreDTO;
import com.mwy.stock.modal.enums.CalculatorEnum;
import com.mwy.stock.reponstory.dao.StockDayInfoDao;
import com.mwy.stock.reponstory.dao.StockTimeInfoDao;
import com.mwy.stock.reponstory.dao.modal.StockDayInfoDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class TestCalculator extends AbsStockCalculator {

    @Resource
    private StockDayInfoDao stockDayInfoDao;
    @Resource
    private StockTimeInfoDao stockTimeInfoDao;

    @Override
    public CalculatorEnum type() {
        return CalculatorEnum.M_TEST;
    }

    @Override
    public StockScoreDTO doCalc(String stockNum, String date) {

        StockScoreDTO stockScoreDTO = new StockScoreDTO();
        stockScoreDTO.setStockNum(stockNum);
        stockScoreDTO.setDate(date);
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

        //cci 上涨
        boolean cciUp = stockDayInfoDO1.getCci() > stockDayInfoDO2.getCci() && stockDayInfoDO2.getCci() > stockDayInfoDO3.getCci();
        if (cciUp){
            height += 1;
            sb.append("cci上涨");
        }
        //cci 上穿100
        boolean cciUpL100 = stockDayInfoDO1.getCci() > -100 && stockDayInfoDO3.getCci() < -100 ;
        boolean cciUpH100 = stockDayInfoDO1.getCci() > 100 && stockDayInfoDO3.getCci() < 100 ;
        if (cciUpL100 || cciUpH100){
            height += 1;
            if (cciUpL100){
                sb.append("cci上穿-100");
            }
            if (cciUpH100){
                sb.append("cci上穿100");
            }
        }

        //macd 上涨
        boolean macdUp = stockDayInfoDO1.getMacd() > stockDayInfoDO2.getMacd() && stockDayInfoDO2.getMacd() > stockDayInfoDO3.getMacd();
        if (macdUp){
            height += 1;
            sb.append("macdUp上涨");
        }

        boolean macdUp0 = stockDayInfoDO1.getMacd() > 0 && stockDayInfoDO3.getMacd() < 0 ;
        if (macdUp0){
            height += 1;
            sb.append("macd上穿0");
        }

        //过30均线
        double v = (stockDayInfoDO1.getClose() - stockDayInfoDO1.getMa30()) / stockDayInfoDO1.getClose();
        if (v > 0.05 && stockDayInfoDO3.getClose() < stockDayInfoDO3.getMa30()){
            height += 2;
            sb.append("过MA30");
        }
        //有异动
        double abnormal = stockTimeInfoDao.abnormal(stockNum, Lists.newArrayList(stockDayInfoDO1.getDate()));
        if (abnormal > 15){
            height += (3+abnormal/100);
            sb.append("有异动").append((int)(3+abnormal/100));
        }

        log.info("进入测试:{}", stockNum);
        stockScoreDTO.setScore(height);
        stockScoreDTO.setScoreDesc(sb.toString());


        return stockScoreDTO;
    }
}
