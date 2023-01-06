package com.mwy.stock.modal.qry;

import lombok.Data;

@Data
public class FavoriteEditParam {
    private String stockNum;
    private OpType opType;

    public enum OpType{
        ADD,
        DELETE
    }
}
