package com.mwy.stock.job;

import com.mwy.stock.service.StockService;
import com.mwy.stock.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Component
public class StockCrowJob {
    @Resource
    private StockService stockService;

    @Scheduled(cron = "0 1 10 * * ?")
    public void startCrowStock() {
        log.info("开始抓取股票10点");
        stockService.crowStock();
        log.info("股票抓取正常结束");
    }

    @Scheduled(cron = "0 1 13 * * ?")
    public void startCrowStock2() {
        log.info("开始抓取股票13点");
        stockService.crowStock();
        log.info("股票抓取正常结束");
    }

    @Scheduled(cron = "0 10 15 * * ?")
    public void startCrowStock3() {
        log.info("开始抓取股票15点");
        stockService.crowStock();
        log.info("股票抓取正常结束");
    }

    //1分钟检查涨幅通知
    @Scheduled(cron = "10 * 9,10,11,13,14 * * ?")
    public void startUpDownNotice() {
        String nowTime = DateUtils.date2String(new Date(), "HH:mm");
        boolean inTime = nowTime.compareTo("09:30") > 0 && nowTime.compareTo("15:00") < 0;
        if (!inTime) {
            return;
        }
        try {
            stockService.doNotice();
            log.info("监控正常结束");
        } catch (Exception e) {
            log.error("监控异常结束", e);
        }
    }

    //1分钟检查涨幅通知
    @Scheduled(cron = "10 * 9 * * ?")
    public void clearHistory() {
        stockService.clearHistory();
        log.info("清理正常结束");
    }
}
