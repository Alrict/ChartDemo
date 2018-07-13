package com.ihypnus.ihypnuscare.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class MyAxisValueFormatter implements IAxisValueFormatter {

    private DecimalFormat mFormat;
    private int mType;

    public MyAxisValueFormatter(int type) {
        mType = type;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

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
