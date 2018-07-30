package com.ihypnus.ihypnuscare.controller;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.fragment.ReportFragment;
import com.ihypnus.ihypnuscare.utils.BarChartManager;
import com.ihypnus.ihypnuscare.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Collections;
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
    private LinearLayout mLayoutChart1;
    private LinearLayout mLayoutChart2;
    private List<Integer> mColours;
    private ArrayList<String> mLables;

    public ChartsPage2Controller(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View rootView = View.inflate(mContext, R.layout.fragment_third, null);
        mChart1 = (BarChart) rootView.findViewById(R.id.chart1);

        mLayoutChart1 = (LinearLayout) rootView.findViewById(R.id.layout_chart1);
        mLayoutChart2 = (LinearLayout) rootView.findViewById(R.id.layout_chart2);

        mChart2 = (BarChart) rootView.findViewById(R.id.chart2);
//        XYMarkerView mv = new XYMarkerView(mContext, new MyAxisValueFormatter(0));
//        mv.setChartView(mChart1); // For bounds control
//        mChart1.setMarker(mv); // Set the marker to the chart
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

        //设置y轴的数据()
        mYValue1 = new ArrayList<BarEntry>();
        mYValues2 = new ArrayList<>();
        //颜色集合
        mColours = new ArrayList<>();
        //        mColours.add(ColorTemplate.MATERIAL_COLORS[0]);
//        mColours.add(ColorTemplate.MATERIAL_COLORS[1]);
        mColours.add(Color.GREEN);
        mColours.add(Color.YELLOW);

        mLables = new ArrayList<>();
        mLables.add("90%呼气压力");
        mLables.add("90%吸气压力");
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

    public void updateUI(List<Double> averageInp, List<Double> averageExp, List<Double> ahi) {
        mLayoutChart1.setVisibility(View.VISIBLE);
        mLayoutChart2.setVisibility(View.VISIBLE);

        //标签
        List<String> labels = new ArrayList<>();
        labels.add("90%吸气压力");
        labels.add("90%呼气压力");

        //颜色集合
        List<Integer> colours = new ArrayList<>();
        colours.add(ColorTemplate.MATERIAL_COLORS[1]);
        colours.add(ColorTemplate.MATERIAL_COLORS[0]);

        mYValue1.clear();
        mYValues2.clear();
        float totalBreath = 0;
        float totalAHI = 0;
        int type1 = 0;
        int type2 = 0;
        //X坐标放日期,吸气压力大于呼气压力,AHI显示以为小数;3.新增/注册的时设备只能扫描,不可输入
        long currentTime = ReportFragment.sCurrentTime;


        //设置y轴的数据()
        List<List<Float>> yValues = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<Float> yValue = new ArrayList<>();

            for (int j = 0; j <= 6; j++) {
                float inp = averageInp.get(j).floatValue()/ 10;
                float exp = averageExp.get(j).floatValue()/ 10;
                totalBreath += (inp + exp);
                float value = i == 0 ? exp : inp;
                yValue.add(value);
            }
            yValues.add(yValue);
        }

        mXValues.clear();
        for (int i = 7; i >= 1; i--) {
            long l = currentTime - (i * 24L * 60L * 60L * 1000L);
            String monthDayDateTime = DateTimeUtils.getMonthDayDateTime(l);
            String[] split = monthDayDateTime.split("-");
            if (split.length == 2) {
                String s = split[1];
                int i1 = Integer.parseInt(s);
                mXValues.add((float) i1);
            }
        }
        for (int i = 0; i <= 6; i++) {
            float inp = averageInp.get(i).floatValue();
            float exp = averageExp.get(i).floatValue();
            totalBreath += (inp + exp);
            mYValue1.add(new BarEntry(
                    mXValues.get(i),
                    new float[]{exp / 10, inp / 10},
                    mContext.getResources().getDrawable(R.mipmap.star)));
        }
        if (totalBreath == 0) {
            //吸气压和呼气压均为0
            mChart2.setNoDataText(mContext.getString(R.string.tv_has_no_data));
            type1 = 5;
        } else {
            type1 = 6;
        }


        for (int i = 0; i < ahi.size(); i++) {
            float aDouble = ahi.get(i).floatValue();
            totalAHI += aDouble;
            mYValues2.add(aDouble);
        }
        Float max = Collections.max(mYValues2);
        if (totalAHI == 0 || max <= 40) {
            type2 = 7;
            mChart2.setNoDataText(mContext.getString(R.string.tv_has_no_data));
        } else {
            type2 = 8;
        }
        //创建图表
//        mBarChartManager1.showStackedBarChart(mXValues, mYValue1, "90%压力", type1, averageInp, averageExp);
        mBarChartManager1.showBarChart(mXValues, yValues, type1, averageInp, averageExp, labels, colours);
//        mBarChartManager1.setXAxis(mXValues.get(6), 0, 7);
        mBarChartManager1.setXAxis(7, 0, 7);
        mBarChartManager2.showBarChart(mXValues, mYValues2, "AHI", false, type2, 3);
    }

    public void showErrorView() {
        mLayoutChart1.setVisibility(View.INVISIBLE);
        mLayoutChart1.setVisibility(View.INVISIBLE);
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
