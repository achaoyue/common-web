package com.mwy.stock.controller;

import com.mwy.base.co.PageResult;
import com.mwy.base.co.Result;
import com.mwy.stock.modal.co.StockScoreCO;
import com.mwy.stock.modal.qry.StockScoreQry;
import com.mwy.stock.service.StockCalcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/stock/score")
public class StockScoreController {

    @Resource
    private StockCalcService stockCalcService;

    @GetMapping("/calc")
    public Result crowStockDayInfo(@RequestParam("stockNum") String stockNum,
                                   @RequestParam(value = "date") String date,
                                   @RequestParam("strategyId") String strategyId) {
        if (StringUtils.isEmpty(stockNum)) {
            stockCalcService.calc(date, strategyId);
        } else {
            stockCalcService.calc(stockNum, date, strategyId);
        }
        return Result.ofSuccess(true);
    }

    @GetMapping("/query")
    public PageResult<StockScoreCO> query(@ModelAttribute StockScoreQry qry) {
        return stockCalcService.query(qry);
    }
}
