package com.mwy.stock.indicator.stockcalcImpl;

import com.mwy.stock.config.CalculatorConsts;
import com.mwy.stock.indicator.StockCalculator;
import com.mwy.stock.indicator.TrendsCalculator;
import com.mwy.stock.indicator.enums.TrendEnum;
import com.mwy.stock.modal.dto.StockScoreDTO;
import com.mwy.stock.reponstory.dao.StockDayInfoDao;
import com.mwy.stock.reponstory.dao.modal.StockDayInfoDO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Component(CalculatorConsts.Beautiful_Calc)
public class BeautifulCalculator implements StockCalculator {
    @Resource
    private TrendsCalculator trendsCalculator;
    @Resource
    private StockDayInfoDao stockDayInfoDao;

    @Override
    public StockScoreDTO calc(String stockNum, String date) {

        StockScoreDTO stockScoreDTO = new StockScoreDTO();
        stockScoreDTO.setStockNum(stockNum);
        stockScoreDTO.setDate(date);
        stockScoreDTO.setStrategyId(CalculatorConsts.Beautiful_Calc);
        stockScoreDTO.setStrategyName(CalculatorConsts.Beautiful_Calc_DESC);
        stockScoreDTO.setScore(-100D);
        stockScoreDTO.setScoreDesc("无");
        stockScoreDTO.setUpdateDate(new Date());

        int length = 5;
        List<StockDayInfoDO> stockDayInfos = stockDayInfoDao.selectTopN(stockNum, date, length);

        if (CollectionUtils.isEmpty(stockDayInfos) || stockDayInfos.size() < length) {
            log.error("k线太少，忽略计分.{}", stockNum);
            return null;
        }

        StockDayInfoDO currentDayInfo = stockDayInfos.get(0);
        StockDayInfoDO earliestDayInfo = stockDayInfos.get(stockDayInfos.size() - 1);
        if (!currentDayInfo.getDate().equals(date)) {
            log.error("K线无最新数据.{}", stockNum);
            return null;
        }

        //突破30日均线
        if (currentDayInfo.getMa30() == null
                || !(currentDayInfo.getMa30() < currentDayInfo.getClose() && earliestDayInfo.getMa30() > earliestDayInfo.getClose())){
            log.info("非突破30日线.{}",stockNum);
            return null;
        }

        //kdj上涨
        double[] jArray = stockDayInfos.stream()
                .mapToDouble(e -> e.getJ())
                .toArray();
        ArrayUtils.reverse(jArray);
        TrendEnum trend = trendsCalculator.trend(jArray, 4);
        if (trend == null || trend.getCode() < 100){
            log.info("kdj 不符合要求.{}",stockNum);
            return null;
        }

        //cci上涨
        double[] cciArray = stockDayInfos.stream()
                .mapToDouble(e -> e.getCci())
                .toArray();
        ArrayUtils.reverse(cciArray);
        trend = trendsCalculator.trend(cciArray, 4);
        if (trend == null || trend.getCode() < 100){
            log.info("cci 不符合要求.{}",stockNum);
            return null;
        }

        //macd上涨
        double[] macdArray = stockDayInfos.stream()
                .mapToDouble(e -> e.getCci())
                .toArray();
        ArrayUtils.reverse(macdArray);
        trend = trendsCalculator.trend(macdArray, 4);
        if (trend == null || trend.getCode() < 100){
            log.info("macd 不符合要求.{}",stockNum);
            return null;
        }

        stockScoreDTO.setScore((currentDayInfo.getTurnOverrate()*100));
        stockScoreDTO.setScoreDesc("");

        return stockScoreDTO;
    }
}
