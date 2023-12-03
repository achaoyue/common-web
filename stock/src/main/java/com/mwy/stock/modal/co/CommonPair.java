package com.mwy.stock.modal.co;

import lombok.Data;

@Data
public class CommonPair {
    private String key;
    private Object value;

    public static CommonPair of(String key, Object value){
        CommonPair commonPair = new CommonPair();
        commonPair.setKey(key);
        commonPair.setValue(value);
        return commonPair;
    }
}
