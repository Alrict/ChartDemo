package com.ihypnus.ihypnuscare.controller;

import android.content.Context;
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
    private CharsDataChangedListener mCharsDataChangedListener;
    private BarChartManager mBarChartManager1;
    private BarChartManager mBarChartManager2;
    private ArrayList<Float> mXValues;
    private List<Float> mYValues1;
    private List<Float> mYValues2;

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
        loadFromNet();
    }

    @Override
    public void refreshData() {
        LogOut.d("llw", "page2更新");
        setWeekDate();
        loadFromNet();
    }

    private void loadFromNet() {
        IhyRequest.getHistogramData(Constants.JSESSIONID, true, Constants.DEVICEID, getWeekStartTime(ReportFragment.sCurrentTime), getEndTime(ReportFragment.sCurrentTime), new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                HistogramData data = (HistogramData) var1;
                if (data != null) {
                    if (mCharsDataChangedListener != null) {
                        mCharsDataChangedListener.onCharsDataChangedListener(data);
                    }
                    updateUI(data);
                } else {
                    if (mCharsDataChangedListener != null) {
                        mCharsDataChangedListener.onCharsDataChangedErrorListener("请求data为null");
                    }
                }
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                ToastUtils.showToastDefault(var3);
                if (mCharsDataChangedListener != null) {
                    mCharsDataChangedListener.onCharsDataChangedErrorListener(var3);
                }
            }
        });
    }

    private void updateUI(HistogramData data) {
        List<Double> scoreValues = data.getScoreValues();
        List<Double> usedTimeSecond = data.getUsedTimeSecond();
        mYValues1.clear();
        mYValues2.clear();

        for (int i = 0; i < scoreValues.size(); i++) {
            float aDouble = scoreValues.get(i).floatValue();
            mYValues1.add(aDouble);
        }

        for (int i = 0; i < usedTimeSecond.size(); i++) {
            float aDouble = usedTimeSecond.get(i).floatValue();
            mYValues2.add(aDouble);
        }
        //创建多条折线的图表
        mBarChartManager1.showBarChart(mXValues, mYValues1, "睡眠时间", "睡眠分数", false);
        mBarChartManager2.showBarChart(mXValues, mYValues2, "使用时长", "使用时长", false);

    }

    public void setWeekDate() {
        mTvDate.setText(getWeekRang(ReportFragment.sCurrentTime));
    }

    @Override
    public void onDestroy() {

    }


    public interface CharsDataChangedListener {
        void onCharsDataChangedListener(HistogramData data);

        void onCharsDataChangedErrorListener(String errMsg);
    }

    public void setOnCharsDataChangedListener(CharsDataChangedListener listener) {
        this.mCharsDataChangedListener = listener;
    }


}
