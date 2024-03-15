package com.mwy.stock.reponstory.mapper;

import com.mwy.base.util.db.MyBaseMapper;
import com.mwy.stock.reponstory.dao.modal.StockBuyQueueDO;
import org.springframework.stereotype.Repository;

@Repository
public interface StockBuyQueueMapper extends MyBaseMapper<StockBuyQueueDO> {
    int createTable();
}
