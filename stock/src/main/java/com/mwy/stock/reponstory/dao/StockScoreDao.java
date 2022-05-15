package com.mwy.stock.reponstory.dao;

import com.mwy.base.util.db.BaseDao;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.reponstory.dao.modal.StockScoreDO;
import com.mwy.stock.reponstory.mapper.StockMapper;
import com.mwy.stock.reponstory.mapper.StockScoreMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class StockScoreDao extends BaseDao<StockScoreDO,StockScoreMapper> implements InitializingBean {
    @Resource
    private StockScoreMapper stockScoreMapper;

    @Override
    public void afterPropertiesSet() {
        stockScoreMapper.createTable();
        log.info("stock_score 表创建完成");
    }
}
