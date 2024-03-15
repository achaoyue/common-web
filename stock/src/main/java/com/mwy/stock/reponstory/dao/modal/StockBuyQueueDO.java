package com.mwy.stock.reponstory.dao.modal;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "stock_buy_queue")
public class StockBuyQueueDO {
    private Long id;
    private String stockNum;
    private String detail;
}
