package com.mwy.stock.reponstory.mapper;

import com.mwy.base.util.db.MyBaseMapper;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.reponstory.dao.modal.StockDayInfoDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockDayInfoMapper extends MyBaseMapper<StockDayInfoDO> {

    int createTable();

    int upsert(List<StockDayInfoDO> list);

    StockDayInfoDO getLatest(@Param("stockNum") String stockNum);
}
