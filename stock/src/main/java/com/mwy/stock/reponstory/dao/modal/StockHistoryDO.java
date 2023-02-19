package com.mwy.stock.reponstory.dao.modal;

import com.mwy.base.util.db.YesOrNoEnum;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "stock_history_minute")
public class StockHistoryDO {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /**
     * 股票编码
     */
    private String stockNum;

    /**
     * 股票名称
     */
    private String stockName;

    /**
     * 最高价
     */
    private Double high;

    /**
     * 最低价
     */
    private Double low;

    /**
     * 开盘价
     */
    private Double open;

    /**
     * 收盘价
     */
    private Double close;

    /**
     * 昨收盘价
     */
    private Double preClose;

    /**
     * 换手率
     */
    private Double turnOverrate;

    /**
     * 振幅，一天最大涨幅-最小涨幅
     */
    private Double amplitude;

    /**
     * 涨跌额度
     */
    private Double upDownPrices;

    /**
     * 涨幅
     */
    private Double upDownRange;

    /**
     * 三日涨幅
     */
    private Double upDownRange3;

    /**
     * 5日涨幅
     */
    private Double upDownRange5;

    /**
     * 成交量
     */
    private Double volume;

    /**
     * 成交额
     */
    private Double amount;

    /**
     * 流通市值
     */
    private Double flowMarketValue;

    /**
     * 流通股
     */
    private Double totalFlowShares;

    /**
     * 总市值
     */
    private Double totalMarketValue;

    /**
     * 总股本
     */
    private Double totalShares;

    /**
     * 量比
     */
    private Double quantityRelativeRatio;

    /**
     * 行业
     */
    private String industry;

    /**
     * 板块
     */
    private String plate;

    /**
     * 所属板块
     */
    private String belongPlate;

    /**
     * 上市时间
     */
    private String listingDate;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 扩张
     */
    private String attribute;

    /**
     * 是否收藏中。1是，0否
     */
    private YesOrNoEnum favorite;
}
