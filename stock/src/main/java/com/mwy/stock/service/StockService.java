package com.mwy.stock.service;

import com.google.common.collect.Lists;
import com.mwy.base.util.Lock;
import com.mwy.stock.indicator.impl.IndicatorProxy;
import com.mwy.stock.modal.converter.StockConvertor;
import com.mwy.stock.modal.dto.ProgressDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDayInfoDTO;
import com.mwy.stock.reponstory.dao.StockDao;
import com.mwy.stock.reponstory.dao.StockDayInfoDao;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.reponstory.dao.modal.StockDayInfoDO;
import com.mwy.stock.reponstory.remote.EasyMoneyRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mouwenyao
 */
@Slf4j
@Service
public class StockService {
    @Resource
    private Lock lock;
    @Resource
    private StockDao stockDao;
    @Resource
    private StockDayInfoDao stockDayInfoDao;
    @Resource
    private EasyMoneyRepository easyMoneyRepository;
    @Resource(name = "bizThreadPool")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private static boolean STOP = false;

    public StockDO get(long id) {
        StockDO stockDO = stockDao.selectById(id);
        return stockDO;
    }

    /**
     * 抓取股票列表
     */
    public void crowStock() {
        List<StockDO> dbStockDOs = stockDao.selectAll();
        Map<String, StockDO> stockDOMap = dbStockDOs.stream()
                .collect(Collectors.toMap(e -> e.getStockNum(), e -> e));

        List<EasyMoneyStockDTO> stockDTOList = easyMoneyRepository.getStockListByScript();

        List<StockDO> updateStockDos = stockDTOList.stream()
                .map(e -> StockConvertor.toDO(e))
                .filter(e -> stockDOMap.containsKey(e.getStockNum()))
                .peek(e -> e.setId(stockDOMap.get(e.getStockNum()).getId()))
                .collect(Collectors.toList());
        for (StockDO stockDO : updateStockDos) {
            stockDao.updateById(stockDO);
        }

        List<StockDO> insertStockDOs = stockDTOList.stream()
                .map(e -> StockConvertor.toDO(e))
                .filter(e -> !stockDOMap.containsKey(e.getStockNum()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(insertStockDOs)) {
            stockDao.insertList(insertStockDOs);
        }
    }

    public void stop(){
        STOP = true;
    }

    public void crowStockDayInfo() {
        STOP = false;
        List<StockDO> dbStockDOs = stockDao.selectAll();
        ProgressDTO progressDTO = new ProgressDTO();
        progressDTO.setTotal(dbStockDOs.size());
        for (int i = 0; i < dbStockDOs.size(); i++) {
            StockDO stockDO = dbStockDOs.get(i);
            threadPoolTaskExecutor.submit(()->{
                crowStockDayInfo(stockDO);
                progressDTO.add();
                log.info("当前抓取进度,{}:{}",progressDTO.getCurrent(),progressDTO.getPercent());
            });
        }
    }

    public void crowStockDayInfo(String stockNum) {
        STOP = false;
        WeekendSqls<StockDO> sqls = WeekendSqls.custom();
        sqls.andEqualTo(StockDO::getStockNum, stockNum);
        Example example = Example.builder(StockDO.class).where(sqls).build();
        StockDO stockDO = stockDao.selectOneByExample(example);
        crowStockDayInfo(stockDO);
    }

    /**
     * 抓取股票k线，顺带计算指标
     *
     * @param dbStockDO
     */
    public void crowStockDayInfo(StockDO dbStockDO) {
        if (STOP){
            return;
        }
        long start = System.currentTimeMillis();
        log.info("开始抓取{}-{}每日信息", dbStockDO.getStockNum(), dbStockDO.getStockName());

        List<EasyMoneyStockDayInfoDTO> moneyStockDayInfoDTOS
                = easyMoneyRepository.crawlStockDayInfoListByStockBean(dbStockDO.getStockNum());
        log.info("完成每日信息抓取{}-{}.耗时:{}", dbStockDO.getStockNum(), dbStockDO.getStockName(), System.currentTimeMillis() - start);
        //指标计算
        IndicatorProxy indicatorProxy = new IndicatorProxy();
        indicatorProxy.getDayIndicatorList(moneyStockDayInfoDTOS);
        List<StockDayInfoDO> dayInfoDOS = StockConvertor.toDayInfoDO(moneyStockDayInfoDTOS);

        //已经保存的则忽略
        StockDayInfoDO stockDayInfoDO = stockDayInfoDao.getLatest(dbStockDO.getStockNum());
        String date = stockDayInfoDO == null ? null : LocalDate.parse(stockDayInfoDO.getDate()).plusDays(-10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        dayInfoDOS = dayInfoDOS.stream()
                .filter(e -> date == null || e.getDate().compareTo(date)>=0)
                .collect(Collectors.toList());

        Lists.partition(dayInfoDOS,2000).forEach(e->{
            stockDayInfoDao.upsert(e);
        });
        log.info("完成每日信息存储{}-{}.耗时:{}", dbStockDO.getStockNum(), dbStockDO.getStockName(), System.currentTimeMillis() - start);
    }
}
