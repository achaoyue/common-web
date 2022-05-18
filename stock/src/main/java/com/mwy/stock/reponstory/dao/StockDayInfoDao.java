package com.mwy.stock.reponstory.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mwy.base.util.db.BaseDao;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.reponstory.dao.modal.StockDayInfoDO;
import com.mwy.stock.reponstory.mapper.StockDayInfoMapper;
import com.mwy.stock.reponstory.mapper.StockMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class StockDayInfoDao extends BaseDao<StockDayInfoDO, StockDayInfoMapper> implements InitializingBean {
    @Resource
    private StockDayInfoMapper stockDayInfoMapper;

    @Override
    public int upsertList(List<StockDayInfoDO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        return stockDayInfoMapper.upsertList(list);
    }

    @Override
    public void afterPropertiesSet() {
        stockDayInfoMapper.createTable();
        log.info("stockDayInfo 表创建完成");
    }

    public StockDayInfoDO getLatest(String stockNum) {
        return stockDayInfoMapper.getLatest(stockNum);
    }

    public List<StockDayInfoDO> selectTopN(String stockNum, String date, int size) {
        Page<Object> page = PageHelper.startPage(0, size, false);
        WeekendSqls<StockDayInfoDO> sqls = WeekendSqls.custom();
        sqls.andEqualTo(StockDayInfoDO::getStockNum, stockNum);
        sqls.andLessThanOrEqualTo(StockDayInfoDO::getDate, date);
        Example example = Example.builder(StockDayInfoDO.class)
                .where(sqls)
                .orderByDesc("date")
                .build();
        return stockDayInfoMapper.selectByExample(example);
    }

    public int bigThan(String date) {
        WeekendSqls<StockDayInfoDO> sqls = WeekendSqls.custom();
        sqls.andEqualTo(StockDayInfoDO::getStockNum, "600479");
        sqls.andGreaterThan(StockDayInfoDO::getDate, date);
        Example example = Example.builder(StockDayInfoDO.class)
                .where(sqls)
                .orderByDesc("date")
                .build();
        return selectCountByExample(example);
    }
}
