package com.mwy.stock.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mwy.base.co.PageResult;
import com.mwy.stock.indicator.AbsStockCalculator;
import com.mwy.stock.indicator.StockCalculator;
import com.mwy.stock.modal.co.StockScoreCO;
import com.mwy.stock.modal.converter.StockConvertor;
import com.mwy.stock.modal.converter.StockScoreConvetor;
import com.mwy.stock.modal.dto.StockScoreDTO;
import com.mwy.stock.modal.enums.CalculatorEnum;
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
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
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

    private Map<String, StockCalculator> stockCalculatorMap = new HashMap<>();

    @Resource
    public void initStockCalculatorMap(Map<String, StockCalculator> stockCalculatorMap){
        this.stockCalculatorMap.putAll(stockCalculatorMap);
        for (StockCalculator value : stockCalculatorMap.values()) {
            if (value instanceof AbsStockCalculator){
                CalculatorEnum type = ((AbsStockCalculator) value).type();
                this.stockCalculatorMap.put(type.getCode(),value);
            }
        }

    }

    public void calc(String date, String strategyId) {
        List<StockDO> stockDOS = stockDao.selectAll();
        STOP = false;
        //清除历史计算数据
        StockScoreDO deleteDo = new StockScoreDO();
        deleteDo.setStrategyId(strategyId);
        deleteDo.setDate(date);
        int deleteCount = stockScoreDao.delete(deleteDo);
        log.info("历史数据清除完毕:{},{},删除条数:{}",date,strategyId,deleteCount);
        for (StockDO stockDO : stockDOS) {
            threadPoolTaskExecutor.submit(() -> {
                calc(stockDO, date, strategyId);
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

    public List<StockScoreDTO> calcTest(List<String> stockNums, String date, String strategyId) {
        List<StockScoreDTO> list = new ArrayList<>();
        for (String stockNum : stockNums) {
            StockScoreDTO scoreDTO = stockCalculatorMap.get(strategyId).calc(stockNum, date);
            list.add(scoreDTO);
        }
        return list;
    }

    public void calc(StockDO stockDO, String date, String strategyId) {
        if (STOP) {
            log.info("已停止计算");
            return;
        }
        //计算
        StockScoreDTO scoreDTO = stockCalculatorMap.get(strategyId).calc(stockDO.getStockNum(), date);

        if (scoreDTO == null){
            log.info("忽略计算:{}",stockDO.getStockNum());
            return;
        }

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
        if(StringUtils.isNotEmpty(qry.getIndustry())){
            List<StockDO> industry = stockDao.getByIndustry(qry.getIndustry());
            if (CollectionUtils.isEmpty(industry)){
                throw new RuntimeException("未知行业");
            }
            List<String> industryList = industry.stream().map(e -> e.getStockNum()).collect(Collectors.toList());
            sqls.andIn(StockScoreDO::getStockNum,industryList);
        }
        Example example = Example.builder(StockScoreDO.class)
                .where(sqls)
                .orderByDesc("score")
                .build();
        Page<Object> page = PageHelper.startPage(qry.getPageNum(), qry.getPageSize());
        List<StockScoreDO> stockScoreDOS = stockScoreDao.selectByExample(example);

        List<String> stockNums = stockScoreDOS.stream()
                .map(StockScoreDO::getStockNum)
                .collect(Collectors.toList());
        List<StockDO> stockDOS = stockDao.getByStockNums(stockNums);

        List<StockScoreCO> list = StockScoreConvetor.toCO(stockScoreDOS,stockDOS);
        return PageResult.ofSuccess(list,qry.getPageNum(),qry.getPageSize(),page.getTotal());
    }
}
