package com.mwy.stock.modal.qry;

import com.mwy.base.qry.PageBaseQry;
import lombok.Data;

@Data
public class StockScoreQry extends PageBaseQry {
    /**
     * 日期
     */
    private String date;
    /**
     * 策略
     */
    private String strategyId;

    /**
     * 行业
     */
    private String industry;
}
