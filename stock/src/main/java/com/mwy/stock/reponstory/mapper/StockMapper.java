package com.mwy.stock.reponstory.mapper;

import com.mwy.base.util.db.MyBaseMapper;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import org.springframework.stereotype.Repository;

@Repository
public interface StockMapper extends MyBaseMapper<StockDO> {

    int createTable();
}
