package com.mwy.base.util.db;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesOrNoEnum implements CodeEnum{
    N(0,"否"),
    Y(1,"是")
    ;

    int code;
    String desc;
}
