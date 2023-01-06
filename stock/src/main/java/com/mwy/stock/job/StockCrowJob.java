package com.mwy.stock.job;

import com.mwy.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class StockCrowJob {
    @Resource
    private StockService stockService;

    @Scheduled(cron = "0 1 10 * * ?")
    public void startCrowStock(){
        log.info("开始抓取股票10点");
        stockService.crowStock();
        log.info("股票抓取正常结束");
    }

    @Scheduled(cron = "0 1 13 * * ?")
    public void startCrowStock2(){
        log.info("开始抓取股票13点");
        stockService.crowStock();
        log.info("股票抓取正常结束");
    }

    @Scheduled(cron = "0 10 15 * * ?")
    public void startCrowStock3(){
        log.info("开始抓取股票15点");
        stockService.crowStock();
        log.info("股票抓取正常结束");
    }

    //5分钟检查涨幅通知
}
