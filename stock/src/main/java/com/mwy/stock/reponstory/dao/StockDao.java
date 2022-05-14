package com.mwy.stock.reponstory.dao;

import com.mwy.base.util.db.BaseDao;
import com.mwy.stock.reponstory.mapper.StockMapper;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class StockDao extends BaseDao<StockDO,StockMapper> implements InitializingBean {
    @Resource
    private StockMapper stockMapper;


    @Override
    public void afterPropertiesSet() throws Exception {
        stockMapper.createTable();
        log.info("stock 表创建完成");
    }
}
