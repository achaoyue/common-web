package com.mwy.stock.controller;

import com.mwy.base.co.Result;
import com.mwy.stock.modal.dto.DataBoardDTO;
import com.mwy.stock.modal.qry.FavoriteEditParam;
import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.reponstory.dao.modal.StockDayInfoDO;
import com.mwy.stock.service.StockService;
import com.mwy.stock.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/industryList")
    public Result industryList(){
        List<String> industryList = stockService.industryList();
        return Result.ofSuccess(industryList);
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

    @GetMapping("/getDayInfoList")
    public Result getDayInfoList(@RequestParam("stockNums") List<String> stockNums, String startTime,String endTime){
        if (endTime == null){
            endTime = DateUtils.date2String(new Date(),"yyyy-MM-dd");
        }
        if (startTime == null){
            Date date = DateUtils.string2Date(endTime, "yyyy-MM-dd");
            date = DateUtils.addDay(date,-10);
            startTime = DateUtils.date2String(date,"yyyy-MM-dd");
        }
        Map<String, List<StockDayInfoDO>> stockClosePrice = stockService.getStockClosePrice(stockNums, startTime, endTime);
        return Result.ofSuccess(stockClosePrice);

    }

    @GetMapping("/getDataBoard")
    public Result getDataBoard(){
        DataBoardDTO dataBoardDTO = stockService.queryDataBoard();
        return Result.ofSuccess(dataBoardDTO);
    }

    @PostMapping("/editFavorite")
    public Result editFavorite(@RequestBody FavoriteEditParam favoriteEditParam){
        stockService.editFavorite(favoriteEditParam);
        return Result.ofSuccess(true);
    }

    @GetMapping("/queryFavorite")
    public Result queryFavorite(){
        List<StockDO> list =  stockService.queryFavorite();
        return Result.ofSuccess(list);
    }
}
