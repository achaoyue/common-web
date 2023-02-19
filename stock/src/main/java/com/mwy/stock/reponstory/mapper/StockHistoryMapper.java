package com.mwy.stock.reponstory.mapper;

import com.mwy.base.util.db.MyBaseMapper;
import com.mwy.stock.reponstory.dao.modal.StockHistoryDO;
import org.springframework.stereotype.Repository;

@Repository
public interface StockHistoryMapper extends MyBaseMapper<StockHistoryDO> {
    int createTable();
}
