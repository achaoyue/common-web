package com.mwy.stock.reponstory.dao.modal;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "stock_buy_queue")
public class StockBuyQueueDO {
    private Long id;
    private Date gmtModify;
    private String stockNum;
    private String date;
    private String detail;
}
