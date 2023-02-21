package com.mwy.stock.reponstory.dao;

import com.github.pagehelper.PageHelper;
import com.mwy.base.util.db.BaseDao;
import com.mwy.stock.reponstory.dao.modal.StockHistoryDO;
import com.mwy.stock.reponstory.mapper.StockHistoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class StockHistoryDao extends BaseDao<StockHistoryDO, StockHistoryMapper> implements InitializingBean {
    @Resource
    private StockHistoryMapper stockHistoryMapper;

    @Override
    public void afterPropertiesSet() {
        stockHistoryMapper.createTable();
        log.info("StockHistoryDO 表创建完成");
    }

    public List<StockHistoryDO> getLatest(String stockNum, int count) {
        PageHelper.startPage(1,count,false);
        WeekendSqls<StockHistoryDO> sqls = WeekendSqls.custom();
        sqls.andEqualTo(StockHistoryDO::getStockNum, stockNum);
        Example example = Example.builder(StockHistoryDO.class)
                .where(sqls)
                .orderByDesc("update_date")
                .build();
        return stockHistoryMapper.selectByExample(example);
    }

    public void deleteAll() {
        WeekendSqls<StockHistoryDO> sqls = WeekendSqls.custom();
        Example example = Example.builder(StockHistoryDO.class)
                .where(sqls)
                .build();
        stockHistoryMapper.deleteByExample(example);
    }
}
