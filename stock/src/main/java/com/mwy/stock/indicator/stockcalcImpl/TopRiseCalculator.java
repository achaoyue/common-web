package com.mwy.stock.indicator.stockcalcImpl;

import com.mwy.stock.config.CalculatorConsts;
import com.mwy.stock.indicator.StockCalculator;
import com.mwy.stock.indicator.TrendsCalculator;
import com.mwy.stock.modal.dto.StockScoreDTO;
import com.mwy.stock.reponstory.dao.StockDayInfoDao;
import com.mwy.stock.reponstory.dao.modal.StockDayInfoDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Component(CalculatorConsts.Top_Rise_Up_Calc)
public class TopRiseCalculator implements StockCalculator {
    @Resource
    private TrendsCalculator trendsCalculator;
    @Resource
    private StockDayInfoDao stockDayInfoDao;

    @Override
    public StockScoreDTO calc(String stockNum, String date) {

        StockScoreDTO stockScoreDTO = new StockScoreDTO();
        stockScoreDTO.setStockNum(stockNum);
        stockScoreDTO.setDate(date);
        stockScoreDTO.setStrategyId(CalculatorConsts.Top_Rise_Up_Calc);
        stockScoreDTO.setStrategyName(CalculatorConsts.Top_Rise_Up_Calc_DESC);
        stockScoreDTO.setScore(Integer.MIN_VALUE);
        stockScoreDTO.setScoreDesc("无");
        stockScoreDTO.setUpdateDate(new Date());

        int length = 4;
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
        stockScoreDTO.setScore((int) (currentDayInfo.getUpDownRange()*1000));
        stockScoreDTO.setScoreDesc("当日涨幅"+currentDayInfo.getUpDownRange());

        return stockScoreDTO;
    }
}
