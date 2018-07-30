package com.ihypnus.ihypnuscare.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class MyAxisValueFormatter implements IAxisValueFormatter {

    private DecimalFormat mFormat;
    private int mType;
    private List<Double> mAverageInp;
    private List<Double> mAverageExp;

    public MyAxisValueFormatter(int type) {
        mType = type;
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
    /*    if (mType == 1 || mType == 2) {
            //睡眠分数
            return String.valueOf(Math.round(value));
        } else if (mType == 3 || mType == 4) {
            ////使用时长范围 是0 默认转化成1.2.3...小时
            if (value > 24) {
                return String.valueOf(value / 3600);
            }
            return String.valueOf(value);
        } else if (mType == 5 || mType == 6) {
            //90%吸气压范围
            return String.valueOf(Math.round(value));
        } else if (mType == 7 || mType == 8) {
            //ahi范围
            return String.valueOf(Math.round(value));
        }*/
        return Math.round(value) + "";
    }

}
