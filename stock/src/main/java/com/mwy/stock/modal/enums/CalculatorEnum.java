package com.mwy.stock.modal.enums;

import lombok.Getter;

@Getter
public enum CalculatorEnum {
    M_TEST("M_TEST","我的测试"),
    LAST_SAME("LAST_SAME","过去相似K")
    ;
    private String code;
    private String desc;

    CalculatorEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
