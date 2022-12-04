package com.mwy.stock.reponstory.dao.modal;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;

@Data
public class StockTimeInfoDO {
    @Id
    @KeySql(useGeneratedKeys = true)
    private long id;
    private String stockNum;
    private String date;
    private String time;
    private double price;
    private double amount;
    private double volume;
    /**
     * 涨跌幅
     */
    private double upDownRange;
    private double avgPrice;
}