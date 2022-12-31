package com.mwy.stock.reponstory.dao.modal;

import lombok.Data;

@Data
public class UpDownSize {
    /**
     * 行业
     */
    private String industry;
    /**
     * 上涨数量
     */
    private int upSize;
    /**
     * 下跌数量
     */
    private int downSize;

    /**
     * 全部数量
     */
    private int allSize;
}
