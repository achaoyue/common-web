package com.mwy.stock.reponstory.dao;

import com.mwy.base.util.db.BaseDao;
import com.mwy.stock.reponstory.dao.modal.StockFundInfoDO;
import com.mwy.stock.reponstory.mapper.StockFundInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class StockFundDao extends BaseDao<StockFundInfoDO, StockFundInfoMapper> implements InitializingBean {
    @Resource
    private StockFundInfoMapper stockFundInfoMapper;

    @Override
    public void afterPropertiesSet() {
        stockFundInfoMapper.createTable();
        log.info("StockFundInfoDO 创建完成");
    }
}
