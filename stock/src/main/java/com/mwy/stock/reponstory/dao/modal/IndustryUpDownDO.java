package com.mwy.stock.reponstory.dao.modal;

import lombok.Data;

@Data
public class IndustryUpDownDO {
    private String date;
    private String industry;
    private Long value;
}
