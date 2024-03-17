package com.mwy.stock.reponstory.mapper;

import com.mwy.base.util.db.MyBaseMapper;
import com.mwy.stock.reponstory.dao.modal.StockFundInfoDO;
import org.springframework.stereotype.Repository;

@Repository
public interface StockFundInfoMapper extends MyBaseMapper<StockFundInfoDO> {
    int createTable();
}
