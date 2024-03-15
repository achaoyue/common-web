package com.mwy.stock.reponstory.dao;

import com.mwy.base.util.db.BaseDao;
import com.mwy.stock.reponstory.dao.modal.StockBuyQueueDO;
import com.mwy.stock.reponstory.mapper.StockBuyQueueMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class StockBuyQueueDao extends BaseDao<StockBuyQueueDO, StockBuyQueueMapper> implements InitializingBean {
    @Resource
    private StockBuyQueueMapper stockBuyQueueMapper;

    @Override
    public void afterPropertiesSet() {
        stockBuyQueueMapper.createTable();
        log.info("stock_buy_queue 创建完成");
    }

    public StockBuyQueueDO getByStockNum(String stockNum) {
        StockBuyQueueDO stockBuyQueueDO = new StockBuyQueueDO();
        stockBuyQueueDO.setStockNum(stockNum);
        return stockBuyQueueMapper.selectOne(stockBuyQueueDO);
    }
}
