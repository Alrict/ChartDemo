package com.ihypnus.ihypnuscare.fragment;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.adapter.VerticalPagerAdapter;
import com.ihypnus.ihypnuscare.utils.BarChartManager;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.ihypnuscare.widget.CircleProgressBarView;
import com.ihypnus.ihypnuscare.widget.VerticalViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.fragment
 * @author: llw
 * @Description: 报告fragment
 * @date: 2018/5/28 18:34
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ReportFragment extends BaseFragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final String TAG = "ReportFragment";
    private VerticalViewPager mViewPager;
    private VerticalPagerAdapter mPagerAdapter;
    private View mHomeFirstView;
    private LayoutInflater mInflater;
    private View mSecondView;
    private CircleProgressBarView mPb;
    private Animation mReLoadingAnim;
    private ImageView mIvRefresh;
    private ImageView mIvData;
    private BarChart mChart;

    @Override
    protected int setView() {
        return R.layout.fragment_report;
    }

    @Override
    protected void findViews() {
        //头部刷新
        mIvRefresh = (ImageView) findViewById(R.id.iv_refresh);
        //头部 日历
        mIvData = (ImageView) findViewById(R.id.iv_data);
        //viewPager
        mViewPager = (VerticalViewPager) findViewById(R.id.view_pager);

        mInflater = mAct.getLayoutInflater();

        //第一屏
        mHomeFirstView = mInflater.inflate(R.layout.fragment_home_first, null);
        //进度条
        mPb = (CircleProgressBarView) mHomeFirstView.findViewById(R.id.pb);
//        mPb.setMax(100);
        mSecondView = mInflater.inflate(R.layout.fragment_second, null);
        mChart = (BarChart) mSecondView.findViewById(R.id.chart1);
        initChart();
    }

    /**
     * 初始化柱状图
     */
    private void initChart() {
//        使用chart时，一般都要经过以下几个步骤：
// （1）定义该chart；
// （2）设置chart的样式：包括chart的样式、坐标轴的样式和图例的样式等 ；
// （3）为chart添加数据：先定义相应的Entry列表，并添加到DataSet中，然后再添加到ChartData对象中，最后再赋值给该Chart并刷新即可。
        BarChartManager barChartManager = new BarChartManager(mChart);
        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        for (int i = 0; i <= 31; i++) {
            xValues.add((float) i);
        }

        //设置y轴的数据()
        List<List<Float>> yValues = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            List<Float> yValue = new ArrayList<>();
            for (int j = 0; j <= 31; j++) {
                yValue.add((float) (Math.random() * 80));
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
        names.add("折线一");
        names.add("折线二");
        names.add("折线三");
        names.add("折线四");

        //创建多条折线的图表
        barChartManager.showBarChart(xValues, yValues.get(0), names.get(1), colours.get(3));

    }

    @Override
    protected void init() {
        final ArrayList<View> fragmentList = new ArrayList<>();
        fragmentList.add(mHomeFirstView);
        fragmentList.add(mSecondView);
        mPagerAdapter = new VerticalPagerAdapter(fragmentList);

        //设置最小偏移量
        mViewPager.setMinPageOffset(0.15f);
        mViewPager.setAdapter(mPagerAdapter);

        //头部刷新旋转动画
        mReLoadingAnim = AnimationUtils.loadAnimation(mAct, R.anim.login_code_loading);
        LinearInterpolator lin = new LinearInterpolator();
        mReLoadingAnim.setInterpolator(lin);
    }

    @Override
    protected void initEvent() {
        mIvData.setOnClickListener(this);
        mIvRefresh.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    protected void loadData() {
//        BaseDialogHelper.showLoadingDialog(mAct, true, "正在加载...");
//        BaseDialogHelper.dismissLoadingDialog();
        startAni(82);
    }

    private void startAni(float sweep) {
        ObjectAnimator a = ObjectAnimator.ofFloat(mPb, "progress", 0f, sweep);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a.setDuration(3000);
        a.start();
    }


    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_refresh:
                //刷新
//                mIvRefresh.setAnimation(mReLoadingAnim);
                mIvRefresh.startAnimation(mReLoadingAnim);
//                mIvRefresh.clearAnimation();

                break;
            case R.id.iv_data:

                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("llw", "position:" + position + ",positionOffset:" + positionOffset + ",positionOffsetPixels:" + positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
