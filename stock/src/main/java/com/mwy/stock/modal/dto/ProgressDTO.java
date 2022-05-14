package com.mwy.stock.modal.dto;

import lombok.Data;

@Data
public class ProgressDTO {
    private int total;
    private int current;

    public synchronized void add(){
        current++;
    }

    public double getPercent(){
        return ((int)(current*1.0/total*10000))/100;
    }
}
