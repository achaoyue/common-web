package com.mwy.stock.indicator;

import com.mwy.stock.indicator.enums.TrendEnum;

/**
 * 趋势计算器
 * @author mouwenyao
 */
public interface TrendsCalculator {
    TrendEnum trend(double[] arr, int length);
}
