package com.mwy.stock.modal.co;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LineCO {
    private List<Object> data;
    private String name;
    private String type;
    private MarkPoint markPoint;
}
