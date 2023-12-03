package com.mwy.stock.reponstory.dao;

import com.mwy.stock.modal.co.CommonPair;
import com.mwy.stock.reponstory.dao.modal.StockTimeInfoDO;
import com.mwy.stock.reponstory.mapper.StockTimeInfoMapper;
import com.mwy.stock.reponstory.remote.EasyMoneyRepository;
import com.mwy.stock.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class StockTimeInfoDao implements InitializingBean {

    @Resource
    private EasyMoneyRepository easyMoneyRepository;
    @Resource
    private StockTimeInfoMapper stockTimeInfoMapper;

    public void crowTimeInfo(String stockNum){
        log.info("创建分时表:{}", stockNum);
        List<StockTimeInfoDO> timeInfoDOS = easyMoneyRepository.crawlStockTimeInfoList(stockNum);
        if (CollectionUtils.isEmpty(timeInfoDOS)){
            return;
        }
        stockTimeInfoMapper.createTable(stockNum);


        //删除历史抓取数据
        String date = timeInfoDOS.get(0).getDate();

        stockTimeInfoMapper.deleteByTable(stockNum,date);

        stockTimeInfoMapper.insertByTables(timeInfoDOS,stockNum);
//        for (StockTimeInfoDO timeInfoDO : timeInfoDOS) {
//            stockTimeInfoMapper.insertByTable(timeInfoDO,timeInfoDO.getStockNum());
//        }
        log.info("抓取保存分时数据:{}", stockNum);
    }

    public double abnormal(String stockNum, List<String> dates) {
        return stockTimeInfoMapper.abnormal(stockNum, dates);
    }

    public List<CommonPair> abnormal(String stockNum, String startDate, String endDate) {
        return stockTimeInfoMapper.abnormalPeriod(stockNum, startDate, endDate);
    }

    @Override
    public void afterPropertiesSet() {

    }

    public List<StockTimeInfoDO> getLastDay(String stockNum) {
        String date = stockTimeInfoMapper.maxDate(stockNum);
        return stockTimeInfoMapper.selectByDate(stockNum,date);
    }
}
