package com.mwy.stock.reponstory.dao.modal;

import com.mwy.base.util.db.YesOrNoEnum;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "stock_notice_history")
public class StockNoticeHistoryDO {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModify;

    /**
     * 股票编码
     */
    private String stockNum;

    /**
     * 股票名称
     */
    private String stockName;

    /**
     * 通知时间
     */
    private String noticeDay;

    /**
     * 扩展
     */
    private String attribute;

    /**
     * 通知次数
     */
    private Integer noticeCount;

    /**
     * 发送标记
     */
    private String sendLog;
}
