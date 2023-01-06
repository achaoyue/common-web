package com.mwy.stock.modal.dto;

import com.mwy.stock.reponstory.dao.modal.StockDO;
import com.mwy.stock.reponstory.dao.modal.UpDownSize;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DataBoardDTO {
    /**
     * 上涨数量
     */
    private int upSize;
    /**
     * 下跌数量
     */
    private int downSize;
    /**
     * 各行业涨跌数据
     */
    private List<UpDownSize> industryUpDown;

    /**
     * 行业龙头数据
     */
    private Map<String,StockDO> industryTopMap;

    /**
     * 涨幅排行
     */
    private List<StockDO> topUpStockList;

    /**
     * 跌幅排行
     */
    private List<StockDO> topDownStockList;

    /**
     * 收藏列表
     */
    private List<StockDO> favoriteStockList;
}
