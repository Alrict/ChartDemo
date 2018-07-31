package com.ihypnus.ihypnuscare.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * @Package com.ihypnus.ihypnuscare.utils
 * @author: llw
 * @Description: X轴数据格式化
 * @date: 2018/7/31 18:59
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class XAxisValueFormatter implements IAxisValueFormatter {


    private int mEndDay; //周日期中的结束天
    private int mMonthMaxDay;//上个月最大的天数

    public XAxisValueFormatter(int endDay, int monthMaxDay) {
        mEndDay = endDay;
        mMonthMaxDay = monthMaxDay;
    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        //如果最后一天是2号
        //
        float result = mMonthMaxDay + mEndDay - 7 + value + 1;
        if (result > mMonthMaxDay) {
            result = result - mMonthMaxDay;
        }

        return String.valueOf((int) result);
    }

}
