package com.mwy.stock.controller;

import com.mwy.base.co.Result;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.service.StockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @Resource
    private StockService stockService;

    @GetMapping("/get")
    public Result getOne(@RequestParam("id") long id){
        StockDO stockDO = stockService.get(id);
        return Result.ofSuccess(stockDO);
    }

    @GetMapping("/crowStock")
    public Result crowStock(){
        stockService.crowStock();
        return Result.ofSuccess(true);
    }

    @GetMapping("/crowStockDayInfo")
    public Result crowStockDayInfo(@RequestParam("stockNum")String stockNum){
        if (StringUtils.isEmpty(stockNum)){
            stockService.crowStockDayInfo();
        }else {
            stockService.crowStockDayInfo(stockNum);
        }
        return Result.ofSuccess(true);
    }

    @GetMapping("/bigThan")
    public Result bigThan(@RequestParam("date")String date){
        int day = stockService.bigThan(date);
        return Result.ofSuccess(day);
    }
}
