package com.mwy.stock.modal.co;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class LinesCO {
    private List<String> xAxis;
    private List<LineCO> series;

    public void addLines(LineCO line){
        if (series == null){
            series = Lists.newArrayList();
        }
        series.add(line);
    }
}
