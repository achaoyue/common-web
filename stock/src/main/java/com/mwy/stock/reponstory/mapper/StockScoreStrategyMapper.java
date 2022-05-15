package com.mwy.stock.reponstory.mapper;

import com.mwy.base.util.db.MyBaseMapper;
import com.mwy.stock.reponstory.dao.modal.ScoreStrategyDO;
import com.mwy.stock.reponstory.dao.modal.StockScoreDO;
import org.springframework.stereotype.Repository;

@Repository
public interface StockScoreStrategyMapper extends MyBaseMapper<ScoreStrategyDO> {

    int createTable();
}
