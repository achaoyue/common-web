package com.mwy.stock.indicator.stockcalcImpl;

import com.mwy.stock.config.CalculatorConsts;
import com.mwy.stock.indicator.StockCalculator;
import com.mwy.stock.indicator.TrendsCalculator;
import com.mwy.stock.indicator.Wave;
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
import java.util.stream.Collectors;

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
        stockScoreDTO.setScore(-100D);
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

        double maxClose1 = stockDayInfos.subList(1,stockDayInfos.size()).stream()
                .mapToDouble(e -> e.getClose())
                .max().getAsDouble();

        double maxClose2 = stockDayInfos.subList(2,stockDayInfos.size()).stream()
                .mapToDouble(e -> e.getClose())
                .max().getAsDouble();

        double maxClose3 = stockDayInfos.subList(1,4).stream()
                .mapToDouble(e -> e.getClose())
                .max().getAsDouble();


        if (stockDayInfos.get(0).getClose() > maxClose1
                && stockDayInfos.get(0).getClose() > maxClose3
                && stockDayInfos.get(1).getClose() < maxClose2){
            stockScoreDTO.setScore((stockDayInfos.get(0).getTurnOverrate()*100));
            stockScoreDTO.setScoreDesc("X");
        }else {
            log.info("diff:{},maxClose:{},minClose:{}",maxClose1,maxClose2);
            return null;
        }

        return stockScoreDTO;
    }
}
