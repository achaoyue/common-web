package com.mwy.stock.reponstory.dao;

import com.mwy.base.util.db.BaseDao;
import com.mwy.stock.reponstory.dao.modal.ScoreStrategyDO;
import com.mwy.stock.reponstory.dao.modal.StockScoreDO;
import com.mwy.stock.reponstory.mapper.StockScoreMapper;
import com.mwy.stock.reponstory.mapper.StockScoreStrategyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class StockScoreStrategyDao extends BaseDao<ScoreStrategyDO,StockScoreStrategyMapper> implements InitializingBean {
    @Resource
    private StockScoreStrategyMapper stockScoreStrategyMapper;

    @Override
    public void afterPropertiesSet() {
        stockScoreStrategyMapper.createTable();
        log.info("stock_score_strategy 表创建完成");
    }
}
