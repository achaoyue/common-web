package com.mwy.stock.reponstory.mapper;

import com.mwy.base.util.db.MyBaseMapper;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.reponstory.dao.modal.StockScoreDO;
import org.springframework.stereotype.Repository;

@Repository
public interface StockScoreMapper extends MyBaseMapper<StockScoreDO> {

    int createTable();
}
