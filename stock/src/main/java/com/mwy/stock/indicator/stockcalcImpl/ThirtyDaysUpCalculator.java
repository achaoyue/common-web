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
import java.util.Date;
import java.util.List;

@Slf4j
@Component(CalculatorConsts.ThirtyDaysUp_Calc)
public class ThirtyDaysUpCalculator implements StockCalculator {
    @Resource
    private TrendsCalculator trendsCalculator;
    @Resource
    private StockDayInfoDao stockDayInfoDao;

    @Override
    public StockScoreDTO calc(String stockNum, String date) {

        StockScoreDTO stockScoreDTO = new StockScoreDTO();
        stockScoreDTO.setStockNum(stockNum);
        stockScoreDTO.setDate(date);
        stockScoreDTO.setStrategyId(CalculatorConsts.ThirtyDaysUp_Calc);
        stockScoreDTO.setStrategyName(CalculatorConsts.ThirtyDaysUp_Calc_DESC);
        stockScoreDTO.setScore(Integer.MIN_VALUE);
        stockScoreDTO.setScoreDesc("无");
        stockScoreDTO.setUpdateDate(new Date());

        int length = 32;
        List<StockDayInfoDO> stockDayInfos = stockDayInfoDao.selectTopN(stockNum, date, length);

        if (CollectionUtils.isEmpty(stockDayInfos) || stockDayInfos.size() < length) {
            log.error("k线太少，忽略计分.{}", stockNum);
            return null;
        }

        StockDayInfoDO currentDayInfo = stockDayInfos.get(0);
        if (!currentDayInfo.getDate().equals(date)) {
            log.error("K线无最新数据.{}", stockNum);
            return null;
        }

        stockDayInfos = stockDayInfos.subList(5,stockDayInfos.size());

        double maxClose = stockDayInfos.stream()
                .mapToDouble(e -> e.getClose())
                .max().getAsDouble();
        double minClose = stockDayInfos.stream()
                .mapToDouble(e -> e.getClose())
                .min().getAsDouble();

        double diff = maxClose * Math.pow(0.9, 3);

        if (diff < minClose && currentDayInfo.getClose() > maxClose){
            stockScoreDTO.setScore((int) (stockDayInfos.get(0).getTurnOverrate()*100));
            stockScoreDTO.setScoreDesc("");
        }else {
            log.info("diff:{},maxClose:{},minClose:{}",diff,maxClose,minClose);
            return null;
        }

        return stockScoreDTO;
    }
}
