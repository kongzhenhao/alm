package com.xl.alm.job.dur;

import java.math.BigDecimal;
import java.math.RoundingMode;



public class Test {
    public static void main(String[] args) {
        BigDecimal bd = new BigDecimal("1111111111111.123456784324324");
        double d = bd.setScale(20, RoundingMode.HALF_UP).doubleValue();
    }
}
