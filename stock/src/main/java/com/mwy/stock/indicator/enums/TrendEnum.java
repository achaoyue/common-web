package com.mwy.stock.indicator.enums;

import lombok.Getter;

/**
 * 趋势枚举
 * @author mouwenyao
 */

@Getter
public enum TrendEnum {
    Unknown(-1, "未知"),

    Index_Rise(100, "指数上涨"),
    Straight_Rise(101, "直线上涨"),

    Index_Down(0, "指数下跌"),
    Straight_Down(1, "直线下跌"),
    ;

    int code;
    String desc;

    TrendEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}