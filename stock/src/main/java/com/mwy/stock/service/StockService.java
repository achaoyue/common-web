package com.mwy.stock.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mwy.base.util.DingDingUtil;
import com.mwy.base.util.Lock;
import com.mwy.base.util.db.YesOrNoEnum;
import com.mwy.stock.config.NoticeConfig;
import com.mwy.stock.config.SwitchConfig;
import com.mwy.stock.indicator.indicatorImpl.IndicatorProxy;
import com.mwy.stock.modal.co.CommonPair;
import com.mwy.stock.modal.co.LineCO;
import com.mwy.stock.modal.co.LinesCO;
import com.mwy.stock.modal.co.MarkPoint;
import com.mwy.stock.modal.converter.StockBuyQueueConvertor;
import com.mwy.stock.modal.converter.StockConvertor;
import com.mwy.stock.modal.dto.*;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockDayInfoDTO;
import com.mwy.stock.modal.dto.easymoney.EasyMoneyStockFundDTO;
import com.mwy.stock.modal.dto.easymoney.StockQueue;
import com.mwy.stock.modal.qry.FavoriteEditParam;
import com.mwy.stock.reponstory.dao.*;
import com.mwy.stock.reponstory.dao.modal.*;
import com.mwy.stock.reponstory.remote.EasyMoneyRepository;
import com.mwy.stock.util.DateUtils;
import com.mwy.stock.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mouwenyao
 */
@Slf4j
@Service
public class StockService {
    private static String isTradeDay = "";
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

    @Resource
    private StockHistoryDao stockHistoryDao;

    @Resource(name = "bizThreadPool")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private StockNoticeHistoryDao stockNoticeHistoryDao;

    @Resource
    private StockBuyQueueDao stockBuyQueueDao;

    @Resource
    private StockFundDao stockFundDao;

    private static boolean STOP = false;

    public StockDO get(long id) {
        StockDO stockDO = stockDao.selectById(id);
        return stockDO;
    }

    public List stockList() {
        List<StockDO> dbStockDOs = stockDao.selectAll();
        List<CommonPair> stockList = dbStockDOs.stream()
                .map(e -> CommonPair.of(e.getStockName(), e.getStockNum()))
                .collect(Collectors.toList());
        return stockList;
    }

    /**
     * 抓取股票列表
     */
    public void crowStock() {
        if (!isTradeDay()) {
            DingDingUtil.sendMsg("", "非交易日，忽略爬取:" + DateUtils.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            return;
        }


        List<StockDO> dbStockDOs = stockDao.selectAll();
        Map<String, StockDO> stockDOMap = dbStockDOs.stream()
                .collect(Collectors.toMap(e -> e.getStockNum(), e -> e));

        List<EasyMoneyStockDTO> stockDTOList = easyMoneyRepository.getStockListByScript();

        //更新股票
        List<StockDO> updateStockDos = stockDTOList.stream()
                .map(e -> StockConvertor.toDO(e))
                .filter(e -> stockDOMap.containsKey(e.getStockNum()))
                .peek(e -> e.setId(stockDOMap.get(e.getStockNum()).getId()))
                .collect(Collectors.toList());

        for (StockDO stockDO : updateStockDos) {
            stockDao.updateByIdSelective(stockDO);
        }

        //新增股票
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
            if ("-".equals(moneyStockDTO.getIndustry())) {
                continue;
            }

            /**
             * 分时成交抓取
             */
            stockTimeInfoDao.crowTimeInfo(moneyStockDTO.getStockNum());
            /**
             * 每日成交抓取
             */
            crowStockDayInfo(moneyStockDTO.getStockNum());
            /**
             * 资金流向抓取
             */
            crowFundInfo(moneyStockDTO.getStockNum());
            log.info("process:{}", i / stockDTOList.size());

        }
        DingDingUtil.sendMsg("", "数据爬取完成：" + DateUtils.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
    }

    private boolean isTradeDay() {
        String today = DateUtils.date2String(new Date(), "yyyy-MM-dd");
        if (StringUtils.isNotBlank(isTradeDay) && today.startsWith(isTradeDay)) {
            String[] split = isTradeDay.split(":");
            return Boolean.valueOf(split[1]);
        }
        String nowTime = DateUtils.date2String(new Date(), "HH:mm");
        boolean inTime = nowTime.compareTo("09:30") > 0;

        boolean tradeDay = true;
        List<EasyMoneyStockDayInfoDTO> moneyStockDayInfoDTOS
                = easyMoneyRepository.crawlStockDayInfoListByStockBean("000001");
        EasyMoneyStockDayInfoDTO stockDayInfoDTO = moneyStockDayInfoDTOS.get(moneyStockDayInfoDTOS.size() - 1);
        if (!today.equals(stockDayInfoDTO.getDate())) {
            tradeDay = false;
        }
        if (inTime) {
            isTradeDay = today + ":" + tradeDay;
        }
        return tradeDay;
    }

    public void stop() {
        STOP = true;
    }

    public void crowStockDayInfo() {
        STOP = false;
        List<StockDO> dbStockDOs = stockDao.selectAll();
        ProgressDTO progressDTO = new ProgressDTO();
        progressDTO.setTotal(dbStockDOs.size());
        for (int i = 0; i < dbStockDOs.size(); i++) {
            StockDO stockDO = dbStockDOs.get(i);
            threadPoolTaskExecutor.submit(() -> {
                crowStockDayInfo(stockDO);
                progressDTO.add();
                log.info("当前抓取进度,{}:{}", progressDTO.getCurrent(), progressDTO.getPercent());
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
        if (STOP) {
            return;
        }
        if (dbStockDO == null) {
            log.error("股票不存在");
            return;
        }
        long start = System.currentTimeMillis();
        log.info("开始抓取{}-{}每日信息", dbStockDO.getStockNum(), dbStockDO.getStockName());

        List<EasyMoneyStockDayInfoDTO> moneyStockDayInfoDTOS
                = easyMoneyRepository.crawlStockDayInfoListByStockBean(dbStockDO.getStockNum());
        log.info("完成每日信息抓取{}-{}.耗时:{}", dbStockDO.getStockNum(), dbStockDO.getStockName(), System.currentTimeMillis() - start);
        if (CollectionUtils.isEmpty(moneyStockDayInfoDTOS)) {
            log.info("抓取数据为空,{}", dbStockDO.getStockNum());
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
                .filter(e -> date == null || e.getDate().compareTo(date) >= 0)
                .collect(Collectors.toList());

        Lists.partition(dayInfoDOS, 10000).forEach(e -> {
            stockDayInfoDao.upsertList(e);
        });
        log.info("完成每日信息存储{}-{}.耗时:{}", dbStockDO.getStockNum(), dbStockDO.getStockName(), System.currentTimeMillis() - start);

        Double currClose = dayInfoDOS.get(dayInfoDOS.size() - 1).getClose();
        if (dayInfoDOS.size() >= 3) {
            Double curr3Close = dayInfoDOS.get(dayInfoDOS.size() - 3).getClose();
            dbStockDO.setUpDownRange3((currClose - curr3Close) / curr3Close);
        }
        if (dayInfoDOS.size() >= 5) {
            Double curr5Close = dayInfoDOS.get(dayInfoDOS.size() - 5).getClose();
            dbStockDO.setUpDownRange5((currClose - curr5Close) / curr5Close);
        }
        stockDao.updateByIdSelective(dbStockDO);
    }

    public int bigThan(String date) {
        return stockDayInfoDao.bigThan(date);
    }

    public Map<String, List<StockDayInfoDO>> getStockClosePrice(List<String> stockNums, String startTime, String endTime) {
        WeekendSqls<StockDayInfoDO> sqls = WeekendSqls.custom();
        sqls.andBetween(StockDayInfoDO::getDate, startTime, endTime);
        sqls.andIn(StockDayInfoDO::getStockNum, stockNums);
        Example example = Example.builder(StockScoreDO.class)
                .where(sqls)
                .build();
        List<StockDayInfoDO> stockScoreDOS = stockDayInfoDao.selectByExample(example);
        return stockScoreDOS.stream()
                .collect(Collectors.groupingBy(e -> e.getStockNum(), Collectors.toList()));

    }

    public List<String> industryList() {
        List<String> list = stockDao.selectIndustry();
        return list;
    }

    public DataBoardDTO queryDataBoard(String date) {
        //涨跌数量
        UpDownSize upDownSize = stockDao.queryUpDownSize(date);
        //各行业涨跌情况
        List<UpDownSize> upDownSizes = stockDao.queryUpDownSizeByIndustry(date);
        //各行业龙头
        List<StockDO> stockDOS = stockDao.queryTopByIndustry(date);
        Map<String, StockDO> topIndustryMap = stockDOS.stream()
                .collect(Collectors.toMap(e -> e.getIndustry(), e -> e, (e1, e2) -> e1));

        List<StockDO> upTopList = stockDao.queryUpTop(100, date);
        List<StockDO> downTopList = stockDao.queryDownTop(100, date);

        DataBoardDTO boardDTO = new DataBoardDTO();
        boardDTO.setUpSize(upDownSize.getUpSize());
        boardDTO.setDownSize(upDownSize.getDownSize());
        boardDTO.setTopSize(upDownSize.getTopSize());
        boardDTO.setIndustryUpDown(upDownSizes);
        boardDTO.setIndustryTopMap(topIndustryMap);
        boardDTO.setTopUpStockList(upTopList);
        boardDTO.setTopDownStockList(downTopList);
        boardDTO.setFavoriteStockList(queryFavorite());

        return boardDTO;
    }

    public List<UpDownSize> queryUpDownSizeByIndustry(String date) {
        return stockDao.queryUpDownSizeByIndustry(date);
    }

    public void editFavorite(FavoriteEditParam favoriteEditParam) {
        StockDO stockDO = stockDao.getByStockNum(favoriteEditParam.getStockNum());
        if (favoriteEditParam.getOpType() == FavoriteEditParam.OpType.ADD) {
            StockAttribute stockAttribute = new StockAttribute();
            stockAttribute.setAddFavoriteDate(new Date());
            stockAttribute.setInitPrice(stockDO.getClose());
            stockDO.setAttribute(JSON.toJSONString(stockAttribute));
            stockDO.setFavorite(YesOrNoEnum.Y);
        } else if (favoriteEditParam.getOpType() == FavoriteEditParam.OpType.DELETE) {
            stockDO.setFavorite(YesOrNoEnum.N);
        }
        stockDao.updateById(stockDO);
    }

    public List<StockDO> queryFavorite() {
        StockDO query = new StockDO();
        query.setFavorite(YesOrNoEnum.Y);
        List<StockDO> stockDOList = stockDao.select(query);
        stockDOList.sort((e1, e2) -> {
            StockAttribute attribute1 = StockAttribute.fromJson(e1.getAttribute());
            StockAttribute attribute2 = StockAttribute.fromJson(e2.getAttribute());
            if (attribute1 == null || attribute2 == null) {
                return 0;
            }
            return attribute2.getAddFavoriteDate().compareTo(attribute1.getAddFavoriteDate());
        });
        return stockDOList;
    }

    public void doNotice() {
        String nowStr = DateUtils.nowStr();
        if (!isTradeDay()) {
            log.info("非交易日忽略监控,{}", nowStr);
            return;
        }
        if (SwitchConfig.stopWatch) {
            log.info("停止监控,{}", nowStr);
            return;
        }

        List<EasyMoneyStockDTO> stockDTOList = easyMoneyRepository.getStockListByScript();
        List<StockHistoryDO> updateStockDos = stockDTOList.stream()
                .map(e -> StockConvertor.toHistoryDO(e))
                .collect(Collectors.toList());
        stockHistoryDao.insertList(updateStockDos);
        log.info("保存分钟监控结束,{}", nowStr);

        for (EasyMoneyStockDTO easyMoneyStockDTO : stockDTOList) {
            List<StockHistoryDO> stockHistoryDOList = stockHistoryDao.getLatest(easyMoneyStockDTO.getStockNum(), 50);
            boolean needSendNotice = isNeedSendNotice(easyMoneyStockDTO.getStockNum(), stockHistoryDOList);
            //满足顺滑上涨要求
            if (needSendNotice) {
                boolean goodTrend = isGoodTrend(easyMoneyStockDTO);
                buildNotice(easyMoneyStockDTO, stockHistoryDOList, goodTrend, nowStr);
            }
        }

        sendNotice(nowStr);
    }

    private boolean isGoodTrend(EasyMoneyStockDTO easyMoneyStockDTO) {
        String stockNum = easyMoneyStockDTO.getStockNum();
        int size = 33;
        List<StockDayInfoDO> stockDayInfoDOS = stockDayInfoDao.selectTopN(stockNum, DateUtils.nowDayStr(), size);
        if (stockDayInfoDOS.size() < size) {
            log.info("数据量太少,忽略:{}", stockNum);
            return false;
        }
        //macd向上
        boolean macdUpTrend = stockDayInfoDOS.get(0).getMacd() > stockDayInfoDOS.get(1).getMacd()
                && stockDayInfoDOS.get(1).getMacd() > stockDayInfoDOS.get(2).getMacd()
                && stockDayInfoDOS.get(2).getMacd() > stockDayInfoDOS.get(3).getMacd();

        //cci向上
        boolean cciUpTrend = stockDayInfoDOS.get(0).getCci() > stockDayInfoDOS.get(2).getCci();

        //资金流向上
        EasyMoneyStockFundDTO stockFund = easyMoneyRepository.getStockFund(stockNum);
        boolean mainIn = stockFund.getMainMoneyIn() > stockFund.getMainMoneyOut();

        //价格在低位或突破
        Double maxClose = stockDayInfoDOS.stream()
                .max(Comparator.comparing(StockDayInfoDO::getClose))
                .map(e -> e.getClose())
                .orElse(0D);
        boolean perfectPosition = maxClose > easyMoneyStockDTO.getClose() * 1.2 || easyMoneyStockDTO.getClose() > maxClose;
        log.info("{}趋势分析结论:macd向上{},cci向上{},资金流入{},价格在低位{}", stockNum, macdUpTrend, cciUpTrend, mainIn, perfectPosition);
        return macdUpTrend && cciUpTrend && mainIn && perfectPosition;
    }

    private void sendNotice(String nowStr) {
        List<StockNoticeHistoryDO> historyDOList = stockNoticeHistoryDao.getAllBySendLog(nowStr);
        List<List<StockNoticeHistoryDO>> partitions = Lists.partition(historyDOList, 20);
        for (List<StockNoticeHistoryDO> partition : partitions) {
            String noticeAllMsg = partition.stream().map(e -> StockNoticeHistoryAttribute.fromJson(e.getAttribute()))
                    .map(e -> e.isGoodTrend() ? e.getNoticeMsg() + "**" : e.getNoticeMsg())
                    .collect(Collectors.joining("\n--------\n"));
            boolean atAll = partition.stream().map(e -> StockNoticeHistoryAttribute.fromJson(e.getAttribute()))
                    .anyMatch(e -> e.isGoodTrend());
            DingDingUtil.sendMsg(NoticeConfig.stockNoticeToken2, noticeAllMsg, atAll);
        }
    }

    private void buildNotice(EasyMoneyStockDTO stockDTO, List<StockHistoryDO> stockHistoryDOList, boolean goodTrend, String sendLog) {

        String noticeDay = DateUtils.nowDayStr();
        StockNoticeHistoryDO noticeHistoryDO = stockNoticeHistoryDao.getByStockNum(stockDTO.getStockNum(), noticeDay);
        if (noticeHistoryDO == null) {
            noticeHistoryDO = new StockNoticeHistoryDO();
        }

        noticeHistoryDO.setStockNum(stockDTO.getStockNum());
        noticeHistoryDO.setStockName(stockDTO.getStockName());
        noticeHistoryDO.setNoticeCount(ObjectUtils.defaultIfNull(noticeHistoryDO.getNoticeCount(), 0) + 1);
        noticeHistoryDO.setSendLog(sendLog);
        noticeHistoryDO.setNoticeDay(noticeDay);

        String msg = stockDTO.getStockNum()
                + "-" + stockDTO.getStockName() + "\n"
                + "行业:" + stockDTO.getIndustry() + "\n"
                + "当前价格:" + stockDTO.getClose() + "\n"
                + "市值:" + NumberUtil.format(stockDTO.getTotalMarketValue()) + "\n"
                + "当前涨幅:" + stockDTO.getUpDownRange() + "\n"
                + "第" + noticeHistoryDO.getNoticeCount() + "次通知";
        StockNoticeHistoryAttribute attribute = new StockNoticeHistoryAttribute();
        attribute.setNoticeMsg(msg);
        attribute.setGoodTrend(goodTrend);
        noticeHistoryDO.setAttribute(JSON.toJSONString(attribute));
        stockNoticeHistoryDao.upsert(noticeHistoryDO);
    }

    public boolean test(String stockNum) {
//        String nowStr = DateUtils.nowStr();
//        List<EasyMoneyStockDTO> stockDTOList = easyMoneyRepository.getStockListByScript();
//        buildNotice(stockDTOList.get(0), null,false, nowStr);
////        buildNotice(stockDTOList.get(1),null, nowStr);
//        buildNotice(stockDTOList.get(2), null,false, nowStr);
//        sendNotice(nowStr);
        EasyMoneyStockDTO easyMoneyStockDTO = new EasyMoneyStockDTO();
        easyMoneyStockDTO.setStockNum(stockNum);
        easyMoneyStockDTO.setClose(33.55);
        boolean goodTrend = isGoodTrend(easyMoneyStockDTO);
        System.out.println(goodTrend);

        return true;
    }

    private boolean isNeedSendNotice(String stockNum, List<StockHistoryDO> stockHistoryDOList) {
        if (stockHistoryDOList.size() < 5) {
            log.info("数量太少忽略监控");
            return false;
        }
        //最近1分钟涨幅大于1，最近三分钟大于2
        StockHistoryDO now = stockHistoryDOList.get(0);
        StockHistoryDO one = stockHistoryDOList.get(1);
        StockHistoryDO two = stockHistoryDOList.get(2);
        StockHistoryDO tree = stockHistoryDOList.get(3);

        double minUpDownRange = Stream.of(one.getUpDownRange(), two.getUpDownRange(), tree.getUpDownRange()).mapToDouble(e -> e).min().getAsDouble();
        double percent = now.getUpDownRange() - minUpDownRange;
        boolean upRangeNeedNotice = percent > 1 && now.getUpDownRange() < 7 && now.getUpDownPrices() > -6;

        boolean abnormal = false;
        if (upRangeNeedNotice) {
            List<StockTimeInfoDO> timeInfoDOS = easyMoneyRepository.crawlStockTimeInfoList(stockNum);
            List<StockTimeInfoDO> lastDay = stockTimeInfoDao.getLastDay(stockNum);
            double lastDayAvg = lastDay.stream()
                    .mapToDouble(e -> e.getVolume())
                    .average()
                    .orElse(0);
            double threeMax = timeInfoDOS.stream()
                    .sorted(Comparator.comparing(StockTimeInfoDO::getTime).reversed())
                    .limit(3)
                    .mapToDouble(e -> e.getVolume())
                    .max()
                    .getAsDouble();
            double avgVol = timeInfoDOS.stream()
                    .mapToDouble(e -> e.getVolume())
                    .average()
                    .getAsDouble();

            abnormal = threeMax / avgVol > 5 || (lastDayAvg > 0 && threeMax / lastDayAvg > 5);
        }

        log.info("监控结果,{}:涨幅{},成交量{}", stockNum, upRangeNeedNotice, abnormal);
        return upRangeNeedNotice && abnormal;
    }

    public void clearHistory() {
        stockHistoryDao.deleteAll();
    }

    public List<StockNoticeHistoryDO> queryNoticeList(String day) {
        if (StringUtils.isEmpty(day)) {
            day = DateUtils.nowDayStr();
        }
        List<StockNoticeHistoryDO> noticeHistoryDOS = stockNoticeHistoryDao.getAllByDay(day);
        return noticeHistoryDOS;
    }

    public LinesCO queryStockLine(String startDate, String endDate) {
        List<UpDownSize> downSizes = stockDao.queryUpDownSizeByIndustry(startDate, endDate);

        //全部日期
        List<String> allDate = downSizes.stream()
                .map(e -> e.getDate())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        //按照行业分组排序
        Map<String, List<Long>> allLines = downSizes.stream()
                .sorted(Comparator.comparing(UpDownSize::getDate))
                .collect(Collectors.groupingBy(e -> e.getIndustry(), Collectors.mapping(e -> e.getUpSize() * 100L / e.getAllSize(), Collectors.toList())));
        List<LineCO> lines = toLines(allLines);


        LinesCO linesCO = new LinesCO();
        linesCO.setXAxis(allDate);
        linesCO.setSeries(lines);

        return linesCO;
    }

    private List<LineCO> toLines(Map<String, List<Long>> allLines) {
        if (MapUtils.isEmpty(allLines)) {
            return Lists.newArrayList();
        }
        List<LineCO> result = Lists.newArrayList();
        for (String industry : allLines.keySet()) {
            List points = allLines.get(industry);
            LineCO lineCO = new LineCO();
            lineCO.setName(industry);
            lineCO.setData(points);
            result.add(lineCO);
        }
        return result;
    }

    public LinesCO queryKLine(String industry, List<String> stockNums, String startDate, String endDate) {

        if (StringUtils.isNotBlank(industry)) {
            List<StockDO> stockDos = stockDao.getByIndustry(industry);
            stockNums = stockDos.stream()
                    .map(e -> e.getStockNum())
                    .collect(Collectors.toList());
        }

        List<StockDayInfoDO> allStockDayInfoDOS = stockDayInfoDao.selectByPeriod(stockNums, startDate, endDate);

        Map<String, List<StockDayInfoDO>> stockGroups = allStockDayInfoDOS.stream()
                .collect(Collectors.groupingBy(e -> e.getStockNum(), Collectors.toList()));

        List<String> dates = allStockDayInfoDOS.stream()
                .map(e -> e.getDate())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        boolean needWave = stockGroups.size() == 1;
        LinesCO linesCO = new LinesCO();
        linesCO.setXAxis(dates);
        List<LineCO> lines = Lists.newArrayList();
        for (String stockNum : stockGroups.keySet()) {
            List<StockDayInfoDO> stockDayInfoDOS = stockGroups.get(stockNum);

            List closePrices = Lists.newArrayList();
            Map<String, Double> vmap = new HashMap<>();
            double start = 100;
            for (StockDayInfoDO stockDayInfoDO : stockDayInfoDOS) {
                if (needWave) {
                    start = stockDayInfoDO.getClose();
                } else {
                    start = start * (1 + stockDayInfoDO.getUpDownRange() / 100);
                }
                closePrices.add(start);
                vmap.put(stockDayInfoDO.getDate(), start);
            }

            LineCO lineCO = new LineCO();
            lineCO.setName("收盘价" + stockNum);
            lineCO.setData(closePrices);

            List listTrends = toTrends(stockDayInfoDOS);
            LineCO waveCO = new LineCO();
            waveCO.setName("波浪" + stockNum);
            waveCO.setData(listTrends);

            //异动数据
            List<CommonPair> abnormalDays = stockTimeInfoDao.abnormal(stockNum, startDate, endDate);
            List<Map<String, Object>> list = new ArrayList<>();
            for (CommonPair abnormal : abnormalDays) {
                if (abnormal.getValue() == null) {
                    log.error("some error:{}", abnormal);
                    continue;
                }
                double abValue = (double) abnormal.getValue();
                if (abValue < 15) {
                    continue;
                }
                Double v = vmap.get(abnormal.getKey());
                Map m = new HashMap<>();
                m.put("xAxis", abnormal.getKey());
                m.put("yAxis", v);
                m.put("value", (int) abValue);
                list.add(m);
            }
            MarkPoint markPoint = new MarkPoint();
            markPoint.setData(list);
            lineCO.setMarkPoint(markPoint);

            lines.add(lineCO);
            if (needWave) {
                lines.add(waveCO);
            }
        }

        linesCO.setSeries(lines);

        return linesCO;
    }

    private List<Object[]> toTrends(List<StockDayInfoDO> stockDayInfoDOS) {
        List result = Lists.newArrayList();
        result.add(new Object[]{stockDayInfoDOS.get(0).getDate(), stockDayInfoDOS.get(0).getClose()});
        double[] arr = stockDayInfoDOS.stream()
                .mapToDouble(e -> e.getClose())
                .toArray();
        int step = 5;
        for (int i = 0; i + (step - 1) * 2 < arr.length; i++) {
            int maxIdx1 = max(arr, i, i + step - 1);
            int maxIdx2 = max(arr, i + step - 1, i + (step - 1) * 2);
            if (maxIdx1 == maxIdx2) {
                StockDayInfoDO stockDayInfoDO = stockDayInfoDOS.get(maxIdx1);
                result.add(new Object[]{stockDayInfoDO.getDate(), stockDayInfoDO.getClose()});
            }

            int minIdx1 = min(arr, i, i + step - 1);
            int minIdx2 = min(arr, i + step - 1, i + (step - 1) * 2);
            if (minIdx1 == minIdx2) {
                StockDayInfoDO stockDayInfoDO = stockDayInfoDOS.get(minIdx1);
                result.add(new Object[]{stockDayInfoDO.getDate(), stockDayInfoDO.getClose()});
            }
        }
        result.add(new Object[]{stockDayInfoDOS.get(stockDayInfoDOS.size() - 1).getDate(), stockDayInfoDOS.get(stockDayInfoDOS.size() - 1).getClose()});

        return result;
    }

    private int max(double[] arr, int start, int end) {
        int max = start;
        for (; start < arr.length && start <= end; start++) {
            if (arr[max] < arr[start]) {
                max = start;
            }
        }
        return max;
    }

    private int min(double[] arr, int start, int end) {
        int min = start;
        for (; start < arr.length && start <= end; start++) {
            if (arr[min] > arr[start]) {
                min = start;
            }
        }
        return min;
    }

    public List<StockDayInfoDO> queryDayLine(String stockNum, String startDate, String endDate) {
        List<StockDayInfoDO> allStockDayInfoDOS = stockDayInfoDao.selectByPeriod(Lists.newArrayList(stockNum), startDate, endDate);

        return allStockDayInfoDOS;
    }

    public void crowBuyQueue(String today, boolean force) {
        if (!isTradeDay() && !force) {
            DingDingUtil.sendMsg("", "非交易日，忽略爬取:" + DateUtils.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            return;
        }

        List<StockDO> dbStockDOs = stockDao.selectAll();
        for (StockDO dbStockDO : dbStockDOs) {
            String stockNum = dbStockDO.getStockNum();
            crowQueueByStockNum(today, stockNum);
        }
    }

    /**
     * 爬取股票盘口
     * @param today
     * @param stockNum
     */
    public void crowQueueByStockNum(String today, String stockNum) {
        try {
            StockBuyQueueDO stockBuyQueueDO = stockBuyQueueDao.getByStockNum(stockNum, today);
            StockQueue stockQueue = easyMoneyRepository.crowQueue(stockNum);
            StockBuyQueueDTO stockBuyQueueDTO = StockBuyQueueConvertor.toDTO(stockBuyQueueDO);
            if (stockBuyQueueDTO == null) {
                stockBuyQueueDTO = new StockBuyQueueDTO();
                stockBuyQueueDTO.setDate(today);
                stockBuyQueueDTO.setStockNum(stockNum);
                stockBuyQueueDTO.setDetail(new HashMap<>());
            }
            Map<String, StockBuyQueueDetailDTO> detail = stockBuyQueueDTO.getDetail();
            detail.put(stockQueue.getBuyOnePrice().toString(), StockBuyQueueDetailDTO.build(stockQueue.getBuyOnePrice(), stockQueue.getBuyOne(), "buyOne", detail));
            detail.put(stockQueue.getBuyTwoPrice().toString(), StockBuyQueueDetailDTO.build(stockQueue.getBuyTwoPrice(), stockQueue.getBuyTwo(), "buyTwo", detail));
            detail.put(stockQueue.getBuyThreePrice().toString(), StockBuyQueueDetailDTO.build(stockQueue.getBuyThreePrice(), stockQueue.getBuyThree(), "buyThree",detail));
            detail.put(stockQueue.getBuyFourPrice().toString(), StockBuyQueueDetailDTO.build(stockQueue.getBuyFourPrice(), stockQueue.getBuyFour(), "buyFour",detail));
            detail.put(stockQueue.getBuyFivePrice().toString(), StockBuyQueueDetailDTO.build(stockQueue.getBuyFivePrice(), stockQueue.getBuyFive(), "buyFive",detail));

            detail.put(stockQueue.getSoldOnePrice().toString(), StockBuyQueueDetailDTO.build(stockQueue.getSoldOnePrice(), stockQueue.getSoldOne(), "soldOne",detail));
            detail.put(stockQueue.getSoldTwoPrice().toString(), StockBuyQueueDetailDTO.build(stockQueue.getSoldTwoPrice(), stockQueue.getSoldTwo(), "soldTwo",detail));
            detail.put(stockQueue.getSoldThreePrice().toString(), StockBuyQueueDetailDTO.build(stockQueue.getSoldThreePrice(), stockQueue.getSoldThree(), "soldThree",detail));
            detail.put(stockQueue.getSoldFourPrice().toString(), StockBuyQueueDetailDTO.build(stockQueue.getSoldFourPrice(), stockQueue.getSoldFour(), "soldFour",detail));
            detail.put(stockQueue.getSoldFivePrice().toString(), StockBuyQueueDetailDTO.build(stockQueue.getSoldFivePrice(), stockQueue.getSoldFive(), "soldFive",detail));

            //小于买1的卖清空，大于卖1的买需要清空
            for (StockBuyQueueDetailDTO detailDTO : detail.values()) {
                if (detailDTO.getPrice() < stockQueue.getBuyOnePrice() && detailDTO.getType().startsWith("sold")){
                    detailDTO.setType("");
                }else if (detailDTO.getPrice() > stockQueue.getSoldOnePrice() && detailDTO.getType().startsWith("buy")){
                    detailDTO.setType("");
                }
            }

            stockBuyQueueDO = StockBuyQueueConvertor.toDO(stockBuyQueueDTO);
            stockBuyQueueDao.upsert(stockBuyQueueDO);
            log.info("盘口抓取OK:{}",stockNum);
        } catch (Exception e) {
            log.error("盘口抓取错误:{}", stockNum, e);
        }
    }

    public void crowFundInfo(String stockNum) {
        EasyMoneyStockFundDTO stockFund = easyMoneyRepository.getStockFund(stockNum);
        StockFundInfoDO stockFundInfoDO = StockConvertor.toDO(stockFund);
        if (stockFundInfoDO == null) {
            return;
        }
        stockFundDao.upsert(stockFundInfoDO);
    }
}
