package com.ihypnus.ihypnuscare.controller;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
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
    private List<Float> mYValues1;
    private List<Float> mYValues2;

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
        mYValues1 = new ArrayList<>();
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

    public void updateUI(List<Double> averageInp, List<Double> ahi) {

        mYValues1.clear();
        mYValues2.clear();

        for (int i = 0; i < averageInp.size(); i++) {
            float aDouble = averageInp.get(i).floatValue();
            mYValues1.add(aDouble);
        }

        for (int i = 0; i < ahi.size(); i++) {
            float aDouble = ahi.get(i).floatValue();
            mYValues2.add(aDouble);
        }
        //创建多条折线的图表
        mBarChartManager1.showBarChart(mXValues, mYValues1, "平均治疗压力", "平均治疗压力(厘米水柱)");
        mBarChartManager2.showBarChart(mXValues, mYValues2, "AHI", "AHI(次/小时)");
    }
}
