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
 * @Description: 第二页柱状图controller
 * @date: 2018/6/21 14:18
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ChartsPage3Controller extends BaseController {
    private BarChart mChart1;
    private BarChart mChart2;
    private LinearLayout mLayoutWeekData;
    private TextView mTvDate;
    private BarChartManager mBarChartManager1;
    private BarChartManager mBarChartManager2;
    private ArrayList<Float> mXValues;
    private List<Float> mYValues1;
    private List<Float> mYValues2;
    private LinearLayout mLayoutChart1;
    private LinearLayout mLayoutChart2;

    public ChartsPage3Controller(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View rootView = View.inflate(mContext, R.layout.fragment_fourth, null);
        mChart1 = (BarChart) rootView.findViewById(R.id.chart1);
        mChart2 = (BarChart) rootView.findViewById(R.id.chart2);
        mLayoutChart1 = (LinearLayout) rootView.findViewById(R.id.layout_chart1);
        mLayoutChart2 = (LinearLayout) rootView.findViewById(R.id.layout_chart2);
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
//        setWeekDate();
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

    /**
     * 更新柱状图
     *
     * @param tpInValues
     * @param ahiValues
     */
    public void updateUI(List<Double> tpInValues, List<Double> ahiValues) {

        mYValues1.clear();
        mYValues2.clear();

        for (int i = 0; i < tpInValues.size(); i++) {
            float aDouble = tpInValues.get(i).floatValue();
            mYValues1.add(aDouble);
        }

        for (int i = 0; i < ahiValues.size(); i++) {
            float aDouble = ahiValues.get(i).floatValue();
            mYValues2.add(aDouble);
        }
        //创建多条折线的图表
        mBarChartManager1.showBarChart(mXValues, mYValues1, "90%吸气压力(厘米水柱)", false, 0, 4);
        mBarChartManager2.showBarChart(mXValues, mYValues2, "AHI(次/小时)", false, 0, 5);
    }


    public void showErrorView() {
        mLayoutChart1.setVisibility(View.INVISIBLE);
        mLayoutChart1.setVisibility(View.INVISIBLE);
    }
}
