package com.mwy.stock.modal.co;

import lombok.Data;

@Data
public class CommonPair<T> {
    private String key;
    private T value;

    public static <T> CommonPair of(String key, T value){
        CommonPair commonPair = new CommonPair();
        commonPair.setKey(key);
        commonPair.setValue(value);
        return commonPair;
    }
}
