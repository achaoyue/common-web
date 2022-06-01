package com.mwy.stock.reponstory.dao;

import com.mwy.base.util.db.BaseDao;
import com.mwy.stock.reponstory.mapper.StockMapper;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class StockDao extends BaseDao<StockDO,StockMapper> implements InitializingBean {
    @Resource
    private StockMapper stockMapper;


    public StockDO getByStockNum(String stockNum){
        WeekendSqls<StockDO> sqls = WeekendSqls.custom();
        sqls.andEqualTo(StockDO::getStockNum, stockNum);
        Example example = Example.builder(StockDO.class).where(sqls).build();
        StockDO stockDO = selectOneByExample(example);
        return stockDO;
    }

    public List<StockDO> getByStockNums(List<String> stockNums){
        if (CollectionUtils.isEmpty(stockNums)){
            return Collections.emptyList();
        }
        WeekendSqls<StockDO> sqls = WeekendSqls.custom();
        sqls.andIn(StockDO::getStockNum,stockNums);
        Example example = Example.builder(StockDO.class).where(sqls).build();
        return selectByExample(example);
    }

    public List<StockDO> getByIndustry(String industry){
        if (StringUtils.isEmpty(industry)){
            return Collections.emptyList();
        }
        WeekendSqls<StockDO> sqls = WeekendSqls.custom();
        sqls.andLike(StockDO::getIndustry,"%"+industry+"%");
        Example example = Example.builder(StockDO.class).where(sqls).build();
        return selectByExample(example);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        stockMapper.createTable();
        log.info("stock 表创建完成");
    }

    public List<String> selectIndustry() {
        return stockMapper.selectIndustry();
    }
}
