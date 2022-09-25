package com.mwy.stock.modal.dto;

import lombok.Data;

import java.util.Date;

@Data
public class StockScoreDTO {
    /**
     * 股票编码
     */
    private String stockNum;

    /**
     * 计算日期
     */
    private String date;

    /**
     * 策略id
     */
    private String strategyId;

    /**
     * 策略id
     */
    private String strategyName;

    /**
     * 股票得分
     */
    private Double score;

    /**
     * 得分描述
     */
    private String scoreDesc;

    /**
     * 更新时间
     */
    private Date updateDate;
}
