package com.mwy.stock.reponstory.dao;

import com.mwy.stock.reponstory.dao.modal.StockHistoryDO;
import com.mwy.stock.reponstory.dao.modal.StockNoticeHistoryDO;
import com.mwy.stock.reponstory.mapper.StockNoticeHistoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class StockNoticeHistoryDao implements InitializingBean {
    @Resource
    private StockNoticeHistoryMapper stockNoticeHistoryMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        stockNoticeHistoryMapper.createTable();
        log.info("StockNoticeHistoryDO 表创建完成");
    }

    public StockNoticeHistoryDO getByStockNum(String stockNum, String dayStr) {
        StockNoticeHistoryDO noticeHistoryDO = new StockNoticeHistoryDO();
        noticeHistoryDO.setStockNum(stockNum);
        noticeHistoryDO.setNoticeDay(dayStr);
        return stockNoticeHistoryMapper.selectOne(noticeHistoryDO);
    }

    public void upsert(StockNoticeHistoryDO noticeHistoryDO) {
        noticeHistoryDO.setGmtModify(null);
        stockNoticeHistoryMapper.upsert(noticeHistoryDO);
    }

    public List<StockNoticeHistoryDO> getAllBySendLog(String nowStr) {
        StockNoticeHistoryDO noticeHistoryDO = new StockNoticeHistoryDO();
        noticeHistoryDO.setSendLog(nowStr);
        return stockNoticeHistoryMapper.select(noticeHistoryDO);
    }

    public List<StockNoticeHistoryDO> getAllByDay(String day) {
        WeekendSqls<StockNoticeHistoryDO> sqls = WeekendSqls.custom();
        sqls.andEqualTo(StockNoticeHistoryDO::getNoticeDay,day);
        Example example = Example.builder(StockNoticeHistoryDO.class)
                .where(sqls)
                .orderByDesc("gmtModify")
                .build();
        return stockNoticeHistoryMapper.selectByExample(example);
    }
}
