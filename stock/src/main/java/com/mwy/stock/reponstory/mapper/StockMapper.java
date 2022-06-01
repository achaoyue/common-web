package com.mwy.stock.reponstory.mapper;

import com.mwy.base.util.db.MyBaseMapper;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMapper extends MyBaseMapper<StockDO> {

    int createTable();

    List<String> selectIndustry();
}
