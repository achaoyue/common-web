package com.mwy.stock.modal.dto;

import lombok.Data;

import java.util.Map;

@Data
public class StockBuyQueueDTO {
    private Long id;
    private String stockNum;
    private Map<String,StockBuyQueueDetailDTO> detail;

}
