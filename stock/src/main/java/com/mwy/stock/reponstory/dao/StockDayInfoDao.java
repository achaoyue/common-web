package com.mwy.stock.reponstory.dao;

import com.mwy.base.util.db.BaseDao;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.reponstory.dao.modal.StockDayInfoDO;
import com.mwy.stock.reponstory.mapper.StockDayInfoMapper;
import com.mwy.stock.reponstory.mapper.StockMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class StockDayInfoDao extends BaseDao<StockDayInfoDO, StockDayInfoMapper> implements InitializingBean {
    @Resource
    private StockDayInfoMapper stockDayInfoMapper;

    public int upsert(List<StockDayInfoDO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return stockDayInfoMapper.upsert(list);
    }

    @Override
    public void afterPropertiesSet() {
        stockDayInfoMapper.createTable();
        log.info("stockDayInfo 表创建完成");
    }

    public StockDayInfoDO getLatest(String stockNum) {
        return stockDayInfoMapper.getLatest(stockNum);
    }
}
