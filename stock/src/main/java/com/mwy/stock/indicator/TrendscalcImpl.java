package com.mwy.stock.indicator;

import com.mwy.stock.indicator.enums.TrendEnum;
import org.springframework.stereotype.Component;

@Component
public class TrendscalcImpl implements TrendsCalculator{
    @Override
    public TrendEnum trend(double []arr, int length){
        if (arr.length - 1 < length){
            throw new RuntimeException("length  < arr");
        }
        if (arr.length < 4 || length < 4){
            throw new RuntimeException("arr.length < 4");
        }
        //指数上涨
        TrendEnum trend = TrendEnum.Index_Rise;
        int i = arr.length - length;
        for (;i<arr.length-2;i++){
            double t1 = arr[i+1] - arr[i];
            double t2 = arr[i+2] - arr[i+1];
            if (!(t1 > 0 && t2 > 0 && t2 > t1)){
                trend = null;
            }
        }
        if (trend != null){
            return trend;
        }
        //直线上涨
        trend = TrendEnum.Straight_Rise;
        i = arr.length - length;
        for (;i<arr.length-1;i++){
            if (arr[i] > arr[i+1]){
                trend = null;
            }
        }
        if (trend != null){
            return trend;
        }
        return TrendEnum.Unknown;
    }
}
