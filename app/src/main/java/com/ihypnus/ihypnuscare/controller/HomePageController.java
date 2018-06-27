package com.ihypnus.ihypnuscare.controller;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.nfc.FormatException;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.DateTimeUtils;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.ihypnuscare.widget.CircleProgressBarView;

import java.util.Random;

/**
 * @Package com.ihypnus.ihypnuscare.controller
 * @author: llw
 * @Description: 报告 首页
 * @date: 2018/6/21 14:31
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class HomePageController extends BaseController implements View.OnClickListener {
    private TextView mTvData;
    private CircleProgressBarView mPb;
    private LinearLayout mLayoutWeekData;
    private TextView mTvDate;
    private ImageView mIvLastWeek;
    private ImageView mIvNextWeek;

    public HomePageController(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View rootView = View.inflate(mContext, R.layout.fragment_home_first, null);
        //日期
        mTvData = (TextView) rootView.findViewById(R.id.tv_data);
        //前一天
        mIvLastWeek = (ImageView) rootView.findViewById(R.id.iv_last_week);
        //后一天
        mIvNextWeek = (ImageView) rootView.findViewById(R.id.iv_next_week);
        //进度条
        mPb = (CircleProgressBarView) rootView.findViewById(R.id.pb);
        //周统计数据
        mLayoutWeekData = (LinearLayout) rootView.findViewById(R.id.layout_week_data);
        //周统计数据时间范围
        mTvDate = (TextView) rootView.findViewById(R.id.tv_date);
        return rootView;
    }

    @Override
    public void initData() {
        mIvLastWeek.setOnClickListener(this);
        mIvNextWeek.setOnClickListener(this);
        startAni(-1);
        try {
            String currentDate = DateTimeUtils.getCurrentDate();
            String date = DateTimeUtils.date2Chinese(currentDate);
            setDate(System.currentTimeMillis());
            mTvData.setText(date);
        } catch (FormatException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onDestroy() {

    }

    private void startAni(float sweep) {
        if (sweep == -1) {
            mPb.setProgress(sweep);
        } else {
            ObjectAnimator a = ObjectAnimator.ofFloat(mPb, "progress", 0f, sweep);
            a.setInterpolator(new AccelerateDecelerateInterpolator());
            a.setDuration(3000);
            a.start();
        }

    }

    public void refreshDatas(String date, float sweep) {
        mTvData.setText(date);
        startAni(sweep);
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (v.getId()) {
            case R.id.iv_last_week:
                updateData(-1);

                break;

            case R.id.iv_next_week:
                updateData(1);
                break;
        }
    }

    private void updateData(int type) {
        long date = getDate();
        long lastDay = date + ((24l * 60l * 60l * 1000l) * type);
        String dateByTime = DateTimeUtils.getStringDateByMounthDay(lastDay);
        LogOut.d("llw", "当前日期:" + dateByTime);
        try {
            String dayLast = DateTimeUtils.date2Chinese(dateByTime);
            Random random = new Random();
            int i = random.nextInt(100);
            setDate(lastDay);
            refreshDatas(dayLast, i);
        } catch (FormatException e) {
            e.printStackTrace();

        }
    }
}
