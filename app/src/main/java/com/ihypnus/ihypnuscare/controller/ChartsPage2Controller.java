package com.ihypnus.ihypnuscare.controller;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.fragment.ReportFragment;
import com.ihypnus.ihypnuscare.utils.BarChartManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.controller
 * @author: llw
 * @Description: 第三页柱状图controller
 * @date: 2018/6/21 14:18
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ChartsPage2Controller extends BaseController {
    private BarChart mChart1;
    private BarChart mChart2;
    private LinearLayout mLayoutWeekData;
    private TextView mTvDate;
    private BarChartManager mBarChartManager1;
    private BarChartManager mBarChartManager2;
    private ArrayList<Float> mXValues;
    private List<Float> mYValues2;
    private ArrayList<BarEntry> mYValue1;

    public ChartsPage2Controller(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View rootView = View.inflate(mContext, R.layout.fragment_third, null);
        mChart1 = (BarChart) rootView.findViewById(R.id.chart1);
        mChart2 = (BarChart) rootView.findViewById(R.id.chart2);
        //周统计数据
        mLayoutWeekData = (LinearLayout) rootView.findViewById(R.id.layout_week_data);
        //周统计数据时间范围
        mTvDate = (TextView) rootView.findViewById(R.id.tv_date);
        return rootView;
    }

    @Override
    public void initData() {
        mBarChartManager1 = new BarChartManager(mChart1);
        mBarChartManager2 = new BarChartManager(mChart2);
        //设置x轴的数据
        mXValues = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            mXValues.add((float) i);
        }

        //设置y轴的数据()
        mYValue1 = new ArrayList<BarEntry>();
        mYValues2 = new ArrayList<>();


    }

    @Override
    public void loadData() {
        setWeekDate();
    }

    @Override
    public void refreshData() {
        setWeekDate();
    }


    public void setWeekDate() {
        mTvDate.setText(getWeekRang(ReportFragment.sCurrentTime));
    }

    @Override
    public void onDestroy() {

    }

    public void updateUI(List<Double> averageInp, List<Double> averageExp, List<Double> ahi) {

        mYValue1.clear();
        mYValues2.clear();
        float totalBreath = 0;
        float totalAHI = 0;
        int type1 = 0;
        int type2 = 0;
        for (int i = 0; i <= 6; i++) {
            float inp = averageInp.get(i).floatValue();
            float exp = averageExp.get(i).floatValue();
            totalBreath += (inp + exp);
            mYValue1.add(new BarEntry(
                    i,
                    new float[]{inp, exp},
                    mContext.getResources().getDrawable(R.mipmap.star)));
        }
        if (totalBreath == 0) {
            //吸气压和呼气压均为0
            type1 = 5;
        } else {
            type1 = 6;
        }


        for (int i = 0; i < ahi.size(); i++) {
            float aDouble = ahi.get(i).floatValue();
            totalAHI += aDouble;
            mYValues2.add(aDouble);
        }
        if (totalAHI == 0) {
            type2 = 7;
        } else {
            type2 = 8;
        }
        //创建图表
        mBarChartManager1.showStackedBarChart(mXValues, mYValue1, "平均治疗压力", type1);
        mBarChartManager2.showBarChart(mXValues, mYValues2, "AHI", false, type2);
    }

    private int[] getColors() {

        int stacksize = 3;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }

        return colors;
    }
}
