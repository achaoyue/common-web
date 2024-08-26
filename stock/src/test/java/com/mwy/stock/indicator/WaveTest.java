package com.mwy.stock.indicator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaveTest {

    @Test
    void calculateSimilarity() {
        double [] arr1= new double[]{2.910,2.950,2.940,2.940,2.940,2.900,2.910,2.880,2.880,2.870,2.930,2.940,2.930,2.960,2.920,2.910,2.910,2.900,2.860,2.860};
        double [] arr2= new double[]{14.160,14.410,14.620,14.590,14.620,14.670,14.680,14.810,14.900,14.900,15.230,15.360,15.500,15.130,15.250,15.180,15.560,15.400,15.600,15.270};
        double [] arr3= new double[]{4.270,4.320,4.300,4.320,4.290,4.220,4.270,4.250,4.250,4.260,4.390,4.390,4.320,4.300,4.180,4.130,4.070,4.000,4.070,4.030};

        double similarity = Wave.calculateSimilarity(arr1, arr3);
        System.out.println(similarity);
    }
}