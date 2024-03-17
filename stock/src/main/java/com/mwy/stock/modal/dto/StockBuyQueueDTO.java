package com.mwy.stock.modal.dto;

import lombok.Data;

import java.util.Map;

@Data
public class StockBuyQueueDTO {
    private Long id;
    private String stockNum;
    private String date;
    private Map<String,StockBuyQueueDetailDTO> detail;

}
