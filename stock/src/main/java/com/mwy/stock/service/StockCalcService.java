package com.mwy.stock.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mwy.base.co.PageResult;
import com.mwy.stock.indicator.StockCalculator;
import com.mwy.stock.modal.co.StockScoreCO;
import com.mwy.stock.modal.converter.StockConvertor;
import com.mwy.stock.modal.converter.StockScoreConvetor;
import com.mwy.stock.modal.dto.StockScoreDTO;
import com.mwy.stock.modal.qry.StockScoreQry;
import com.mwy.stock.reponstory.dao.StockDao;
import com.mwy.stock.reponstory.dao.StockDayInfoDao;
import com.mwy.stock.reponstory.dao.StockScoreDao;
import com.mwy.stock.reponstory.dao.StockScoreStrategyDao;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.reponstory.dao.modal.StockScoreDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 股票得分计算器
 *
 * @author mouwenyao
 */
@Slf4j
@Service
public class StockCalcService {
    private boolean STOP = false;
    @Resource
    private StockDayInfoDao stockDayInfoDao;
    @Resource
    private StockDao stockDao;
    @Resource
    private StockScoreStrategyDao stockScoreStrategyDao;
    @Resource
    private StockScoreDao stockScoreDao;
    @Resource(name = "bizThreadPool")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private Map<String, StockCalculator> stockCalculatorMap;

    public void calc(String date, String strategyName) {
        List<StockDO> stockDOS = stockDao.selectAll();
        STOP = false;
        for (StockDO stockDO : stockDOS) {
            threadPoolTaskExecutor.submit(() -> {
                calc(stockDO, date, strategyName);
            });
        }
    }

    public void calc(String stockNum, String date, String strategyId) {
        StockDO stockDO = stockDao.getByStockNum(stockNum);
        if (stockDO == null) {
            log.info("股票不存在:{}", stockNum);
            return;
        }
        STOP = false;
        calc(stockDO, date, strategyId);
    }

    public void calc(StockDO stockDO, String date, String strategyId) {
        if (STOP) {
            log.info("已停止计算");
            return;
        }
        //计算
        StockScoreDTO scoreDTO = stockCalculatorMap.get(strategyId).calc(stockDO.getStockNum(), date);

        StockScoreDO stockScoreDO = StockConvertor.toScoreDO(scoreDTO);
        stockScoreDO.setStockName(stockDO.getStockName());

        //保存到计分表
        stockScoreDao.upsert(stockScoreDO);
        log.info("股票计分完成:{},{}", stockDO.getStockNum(), stockDO.getStockName());
    }

    /**
     * 计分查询，拿到目标分数的股票
     *
     * @return
     */
    public PageResult<StockScoreCO> query(StockScoreQry qry) {
        Assert.isTrue(StringUtils.isNotBlank(qry.getDate()),"date必填");
        Assert.isTrue(StringUtils.isNotBlank(qry.getStrategyId()),"strategyId 必填");
        WeekendSqls<StockScoreDO> sqls = WeekendSqls.custom();
        sqls.andEqualTo(StockScoreDO::getDate, qry.getDate());
        sqls.andEqualTo(StockScoreDO::getStrategyId,qry.getStrategyId());
        Example example = Example.builder(StockScoreDO.class)
                .where(sqls)
                .orderByDesc("score")
                .build();
        Page<Object> page = PageHelper.startPage(qry.getPageNum(), qry.getPageSize());
        List<StockScoreDO> stockScoreDOS = stockScoreDao.selectByExample(example);
        List<StockScoreCO> list = StockScoreConvetor.toCO(stockScoreDOS);
        return PageResult.ofSuccess(list,qry.getPageNum(),qry.getPageSize(),page.getTotal());
    }
}
