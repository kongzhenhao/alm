package com.xl.alm.job.dur;

import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.FieldVector;
import org.apache.commons.math3.util.BigReal;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigDecimalMatrixOperationSample {
    public static void main(String[] args) {
        MathContext precision = new MathContext(30); // 30位精度
        BigDecimal[] v1 = {new BigDecimal("0.1"), new BigDecimal("0.2"), new BigDecimal("0.3")};
        BigDecimal[] v2 = {new BigDecimal("0.1"), new BigDecimal("0.2"), new BigDecimal("0.3")};
        BigReal[] v1BigReal = new BigReal[v1.length];
        for (int i = 0; i < v1.length; i++) {
            v1BigReal[i] = new BigReal(v1[i]);
        }
        BigReal[] v2BigReal = new BigReal[v2.length];
        for (int i = 0; i < v2.length; i++) {
            v2BigReal[i] = new BigReal(v2[i]);
        }
        FieldVector<BigReal> vector1 = new ArrayFieldVector<BigReal>(v1BigReal);
        FieldVector<BigReal> vector2 = new ArrayFieldVector<BigReal>(v2BigReal);
        // 计算点积
        FieldVector<BigReal> vector3  = vector1.ebeMultiply(vector2);

        // 计算向量点积和
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < vector3.getDimension(); i++) {
            sum = sum.add(vector3.getEntry(i).bigDecimalValue());
        }
        System.out.println(sum);

        // 计算向量内积
        sum = vector1.dotProduct(vector2).bigDecimalValue();
        System.out.println(sum);
    }
}