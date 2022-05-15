package com.mwy.stock.reponstory.dao.modal;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "stock_score_strategy")
@Data
public class ScoreStrategyDO {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    /**
     * 策略Id
     */
    private String strategyId;
    /**
     * 策略名称
     */
    private String strategyName;

    /**
     * 策略名称
     */
    private String strategyDesc;

    /**
     * 策略详细参数
     */
    private String detail;

    /**
     * 更新时间
     */
    private Date updateDate;
}
