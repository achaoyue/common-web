package com.mwy.stock.reponstory.dao.modal;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author mouwenyao
 */
@Table(name = "stock_score_info")
@Data
public class StockScoreDO {
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
    private Integer score;

    /**
     * 得分描述
     */
    private String scoreDesc;

    /**
     * 更新时间
     */
    private Date updateDate;
}
