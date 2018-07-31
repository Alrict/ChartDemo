package com.ihypnus.ihypnuscare.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class MyAxisValueFormatter implements IAxisValueFormatter {

    private int mBigMonth;//上个月最大天数
    private DecimalFormat mFormat;
    private int mType;
    private List<Double> mAverageInp;
    private List<Double> mAverageExp;

    public MyAxisValueFormatter(int type, int bigMonth) {
        mType = type;
        mBigMonth = bigMonth;
    }


    public MyAxisValueFormatter(int type, List<Double> averageInp, List<Double> averageExp) {
        mType = type;
        mAverageInp = averageInp;
        mAverageExp = averageExp;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (mAverageExp != null && mAverageInp != null) {
            if (value == 0) return "";
        }
        if (mType == 7 || mType == 8) {
            //ahi范围
            BigDecimal bg = new BigDecimal(value);
            double f1 = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            return f1 + "";
        }

        return Math.round(value) + "";
    }

}
