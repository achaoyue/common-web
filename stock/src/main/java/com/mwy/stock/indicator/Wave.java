package com.mwy.stock.indicator;

import java.util.ArrayList;
import java.util.List;

public class Wave {
    public List<Integer> wave(double []arr){
        List<Integer> list = new ArrayList<>();
        int step = 5;
        for (int i = 0; i + (step-1)*2 < arr.length; i++) {
            int maxIdx1 = max(arr, i, i + step-1);
            int maxIdx2 = max(arr, i + step-1, i + (step-1)*2);
            if (maxIdx1 == maxIdx2){
                list.add(maxIdx1);
            }

            int minIdx1 = min(arr, i, i + step);
            int minIdx2 = min(arr, i + step, i + step*2);
            if (minIdx1 == minIdx2){
                list.add(minIdx1);
            }

        }

        return list;
    }

    private int max(double[] arr, int start, int end) {
        int max = start;
        for (;start<arr.length && start<= end;start++){
            if (arr[max] < arr[start]){
                max = start;
            }
        }
        return max;
    }

    private int min(double[] arr, int start, int end) {
        int min = start;
        for (;start<arr.length && start<= end;start++){
            if (arr[min] > arr[start]){
                min = start;
            }
        }
        return min;
    }

    public static void main(String[] args) {
        double [] arr= new double[]{1,1,1,1,1,1,-1,5,6,7,8,9,10,11};
        Wave wave = new Wave();
        List<Integer> wave1 = wave.wave(arr);
        System.out.println(wave1);
    }
}
