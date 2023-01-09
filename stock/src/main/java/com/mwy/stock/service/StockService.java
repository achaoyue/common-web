package com.mwy.stock.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mwy.base.util.DingDingUtil;
import com.mwy.base.util.Lock;
import com.mwy.base.util.db.YesOrNoEnum;
import com.mwy.stock.indicator.indicatorImpl.IndicatorProxy;
import com.mwy.stock.modal.converter.StockConvertor;
import com.mwy.stock.modal.dto.DataBoardDTO;
import com.mwy.stock.modal.dto.ProgressDTO;
import com.mwy.stock.modal.dto.StockAttribute;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDayInfoDTO;
import com.mwy.stock.modal.qry.FavoriteEditParam;
import com.mwy.stock.reponstory.dao.StockDao;
import com.mwy.stock.reponstory.dao.StockDayInfoDao;
import com.mwy.stock.reponstory.dao.StockTimeInfoDao;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.reponstory.dao.modal.StockDayInfoDO;
import com.mwy.stock.reponstory.dao.modal.StockScoreDO;
import com.mwy.stock.reponstory.dao.modal.UpDownSize;
import com.mwy.stock.reponstory.remote.EasyMoneyRepository;
import com.mwy.stock.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
    @Resource
    private StockTimeInfoDao stockTimeInfoDao;
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
            stockDao.updateByIdSelective(stockDO);
        }

        List<StockDO> insertStockDOs = stockDTOList.stream()
                .map(e -> StockConvertor.toDO(e))
                .filter(e -> !stockDOMap.containsKey(e.getStockNum()))
                .filter(e -> !"-".equals(e.getIndustry()))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(insertStockDOs)) {
            stockDao.insertList(insertStockDOs);
        }

        double i = 0;
        for (EasyMoneyStockDTO moneyStockDTO : stockDTOList) {
            i++;
            if ("-".equals(moneyStockDTO.getIndustry())){
                continue;
            }
            stockTimeInfoDao.crowTimeInfo(moneyStockDTO.getStockNum());
            crowStockDayInfo(moneyStockDTO.getStockNum());
            log.info("process:{}",i/stockDTOList.size());
        }
        DingDingUtil.sendMsg("","数据爬取完成："+ DateUtils.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
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
        StockDO stockDO = stockDao.getByStockNum(stockNum);
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
        if (dbStockDO == null){
            log.error("股票不存在");
            return;
        }
        long start = System.currentTimeMillis();
        log.info("开始抓取{}-{}每日信息", dbStockDO.getStockNum(), dbStockDO.getStockName());

        List<EasyMoneyStockDayInfoDTO> moneyStockDayInfoDTOS
                = easyMoneyRepository.crawlStockDayInfoListByStockBean(dbStockDO.getStockNum());
        log.info("完成每日信息抓取{}-{}.耗时:{}", dbStockDO.getStockNum(), dbStockDO.getStockName(), System.currentTimeMillis() - start);
        if (CollectionUtils.isEmpty(moneyStockDayInfoDTOS)){
            log.info("抓取数据为空,{}",dbStockDO.getStockNum());
            return;
        }
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

        Lists.partition(dayInfoDOS,10000).forEach(e->{
            stockDayInfoDao.upsertList(e);
        });
        log.info("完成每日信息存储{}-{}.耗时:{}", dbStockDO.getStockNum(), dbStockDO.getStockName(), System.currentTimeMillis() - start);

        Double currClose = dayInfoDOS.get(dayInfoDOS.size() -1).getClose();
        if (dayInfoDOS.size() >= 3){
            Double curr3Close = dayInfoDOS.get(dayInfoDOS.size() -3).getClose();
            dbStockDO.setUpDownRange3((currClose - curr3Close)/curr3Close);
        }
        if (dayInfoDOS.size() >= 5){
            Double curr5Close = dayInfoDOS.get(dayInfoDOS.size() -5).getClose();
            dbStockDO.setUpDownRange5((currClose - curr5Close)/curr5Close);
        }
        stockDao.updateByIdSelective(dbStockDO);
    }

    public int bigThan(String date) {
        return stockDayInfoDao.bigThan(date);
    }

    public Map<String, List<StockDayInfoDO>> getStockClosePrice(List<String> stockNums, String startTime, String endTime){
        WeekendSqls<StockDayInfoDO> sqls = WeekendSqls.custom();
        sqls.andBetween(StockDayInfoDO::getDate,startTime,endTime);
        sqls.andIn(StockDayInfoDO::getStockNum,stockNums);
        Example example = Example.builder(StockScoreDO.class)
                .where(sqls)
                .build();
        List<StockDayInfoDO> stockScoreDOS = stockDayInfoDao.selectByExample(example);
        return stockScoreDOS.stream()
                .collect(Collectors.groupingBy(e->e.getStockNum(),Collectors.toList()));

    }

    public List<String> industryList() {
        List<String> list = stockDao.selectIndustry();
        return list;
    }

    public DataBoardDTO queryDataBoard(){
        //涨跌数量
        UpDownSize upDownSize = stockDao.queryUpDownSize();
        //各行业涨跌情况
        List<UpDownSize> upDownSizes = stockDao.queryUpDownSizeByIndustry();
        //各行业龙头
        List<StockDO> stockDOS = stockDao.queryTopByIndustry();
        Map<String, StockDO> topIndustryMap = stockDOS.stream()
                .collect(Collectors.toMap(e -> e.getIndustry(), e -> e));

        List<StockDO> upTopList = stockDao.queryUpTop(100);
        List<StockDO> downTopList = stockDao.queryDownTop(100);

        DataBoardDTO boardDTO = new DataBoardDTO();
        boardDTO.setUpSize(upDownSize.getUpSize());
        boardDTO.setDownSize(upDownSize.getDownSize());
        boardDTO.setIndustryUpDown(upDownSizes);
        boardDTO.setIndustryTopMap(topIndustryMap);
        boardDTO.setTopUpStockList(upTopList);
        boardDTO.setTopDownStockList(downTopList);
        boardDTO.setFavoriteStockList(queryFavorite());

        return boardDTO;
    }

    public void editFavorite(FavoriteEditParam favoriteEditParam) {
        StockDO stockDO = stockDao.getByStockNum(favoriteEditParam.getStockNum());
        if (favoriteEditParam.getOpType() == FavoriteEditParam.OpType.ADD){
            StockAttribute stockAttribute = new StockAttribute();
            stockAttribute.setAddFavoriteDate(new Date());
            stockAttribute.setInitPrice(stockDO.getClose());
            stockDO.setAttribute(JSON.toJSONString(stockAttribute));
            stockDO.setFavorite(YesOrNoEnum.Y);
        }else if (favoriteEditParam.getOpType() == FavoriteEditParam.OpType.DELETE){
            stockDO.setFavorite(YesOrNoEnum.N);
        }
        stockDao.updateById(stockDO);
    }

    public List<StockDO> queryFavorite() {
        StockDO query = new StockDO();
        query.setFavorite(YesOrNoEnum.Y);
        List<StockDO> stockDOList = stockDao.select(query);
        stockDOList.sort((e1,e2)->{
            StockAttribute attribute1 = StockAttribute.fromJson(e1.getAttribute());
            StockAttribute attribute2 = StockAttribute.fromJson(e2.getAttribute());
            if (attribute1 == null || attribute2 == null){
                return 0;
            }
            return attribute2.getAddFavoriteDate().compareTo(attribute1.getAddFavoriteDate());
        });
        return stockDOList;
    }
}
