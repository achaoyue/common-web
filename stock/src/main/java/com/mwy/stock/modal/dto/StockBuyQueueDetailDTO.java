package com.mwy.stock.modal.dto;

import com.mwy.stock.util.DateUtils;
import lombok.Data;

import java.util.Map;

/**
 * 盘口情况。价格。
 */
@Data
public class StockBuyQueueDetailDTO {
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 排队价格
     */
    private Double price;
    /**
     * 排队大小
     */
    private Double queueSize;

    /**
     * 买卖类型
     */
    private String type;

    /**
     * 今日历史最大买
     */
    private Double historyMaxBuyQueue;

    /**
     * 今日历史最大卖
     */
    private Double historyMaxSoldQueue;

    /**
     * 构建盘口详情。保留历史最大值
     * @param price
     * @param queueSize
     * @param type
     * @param detail
     * @return
     */
    public static StockBuyQueueDetailDTO build(Double price, Double queueSize, String type, Map<String, StockBuyQueueDetailDTO> detail){

        StockBuyQueueDetailDTO stockBuyQueueDetailDTO = new StockBuyQueueDetailDTO();
        stockBuyQueueDetailDTO.setPrice(price);
        stockBuyQueueDetailDTO.setUpdateTime(DateUtils.nowStr());
        stockBuyQueueDetailDTO.setQueueSize(queueSize);
        stockBuyQueueDetailDTO.setType(type);
        if (type.startsWith("buy")){
            stockBuyQueueDetailDTO.setHistoryMaxBuyQueue(queueSize);
            stockBuyQueueDetailDTO.setHistoryMaxSoldQueue(0d);
        }else {
            stockBuyQueueDetailDTO.setHistoryMaxBuyQueue(0d);
            stockBuyQueueDetailDTO.setHistoryMaxSoldQueue(queueSize);
        }
        StockBuyQueueDetailDTO historyDTO = detail.get(String.valueOf(price));
        if (historyDTO != null){
            stockBuyQueueDetailDTO.setHistoryMaxBuyQueue(Math.max(historyDTO.getHistoryMaxBuyQueue(), stockBuyQueueDetailDTO.getHistoryMaxBuyQueue()));
            stockBuyQueueDetailDTO.setHistoryMaxSoldQueue(Math.max(historyDTO.getHistoryMaxSoldQueue(), stockBuyQueueDetailDTO.getHistoryMaxSoldQueue()));
        }
        return stockBuyQueueDetailDTO;
    }
}
