package com.mwy.stock.reponstory.mapper;

import com.mwy.base.util.db.MyBaseMapper;
import com.mwy.stock.reponstory.dao.modal.StockNoticeHistoryDO;

public interface StockNoticeHistoryMapper extends MyBaseMapper<StockNoticeHistoryDO> {
    int createTable();
}
