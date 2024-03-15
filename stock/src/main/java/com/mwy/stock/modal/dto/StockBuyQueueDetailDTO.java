package com.mwy.stock.modal.dto;

import com.mwy.stock.util.DateUtils;
import lombok.Data;

import java.util.Date;

@Data
public class StockBuyQueueDetailDTO {
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 排队价格
     */
    private String price;
    /**
     * 排队大小
     */
    private String queueSize;

    /**
     * 买卖类型
     */
    private String type;

    public static StockBuyQueueDetailDTO build(Double price, Double queueSize, String type){
        StockBuyQueueDetailDTO stockBuyQueueDetailDTO = new StockBuyQueueDetailDTO();
        stockBuyQueueDetailDTO.setPrice(price.toString());
        stockBuyQueueDetailDTO.setUpdateTime(DateUtils.nowStr());
        stockBuyQueueDetailDTO.setQueueSize(queueSize.toString());
        stockBuyQueueDetailDTO.setType(type);
        return stockBuyQueueDetailDTO;
    }
}
