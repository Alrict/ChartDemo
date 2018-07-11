package com.ihypnus.ihypnuscare.controller;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.github.mikephil.charting.charts.BarChart;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.HistogramData;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.fragment.ReportFragment;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.BarChartManager;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.controller
 * @author: llw
 * @Description: 第二页柱状图controller
 * @date: 2018/6/21 14:18
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ChartsPage1Controller extends BaseController {
    private BarChart mChart1;
    private BarChart mChart2;
    private LinearLayout mLayoutWeekData;
    private TextView mTvDate;

    public ChartsPage1Controller(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View rootView = View.inflate(mContext, R.layout.fragment_second, null);
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
        BarChartManager barChartManager1 = new BarChartManager(mChart1);
        BarChartManager barChartManager2 = new BarChartManager(mChart2);
        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            xValues.add((float) i);
        }

        //设置y轴的数据()
        List<List<Float>> yValues = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            List<Float> yValue = new ArrayList<>();
            for (int j = 0; j <= 6; j++) {
                yValue.add((float) (Math.random() * 100));
            }
            yValues.add(yValue);
        }

        //颜色集合
        List<Integer> colours = new ArrayList<>();
        colours.add(Color.GREEN);
        colours.add(Color.BLUE);
        colours.add(Color.RED);
        colours.add(Color.CYAN);

        //线的名字集合
        List<String> names = new ArrayList<>();
        names.add("睡眠时间");
        names.add("折线二");
        names.add("折线三");
        names.add("折线四");

        //创建多条折线的图表
        barChartManager1.showBarChart(xValues, yValues.get(0), names.get(0), "睡眠分数");
        barChartManager2.showBarChart(xValues, yValues.get(1), names.get(1), "使用时长");
    }

    @Override
    public void loadData() {
        loadFromNet();
        setWeekDate();
    }

    @Override
    public void refreshData() {
        LogOut.d("llw", "page2更新");
        setWeekDate();
        loadFromNet();
    }

    private void loadFromNet() {
        IhyRequest.getHistogramData(Constants.JSESSIONID, true, Constants.DEVICEID, getStartTime(ReportFragment.sCurrentTime), getEndTime(ReportFragment.sCurrentTime), new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                HistogramData data = (HistogramData) var1;
                if (data != null) {

                }
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                ToastUtils.showToastDefault(var3);
            }
        });
    }

    public void setWeekDate() {
        mTvDate.setText(getWeekRang(ReportFragment.sCurrentTime));
    }

    @Override
    public void onDestroy() {

    }



}
