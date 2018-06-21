package com.ihypnus.ihypnuscare.controller;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.nfc.FormatException;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.DateTimeUtils;
import com.ihypnus.ihypnuscare.widget.CircleProgressBarView;

/**
 * @Package com.ihypnus.ihypnuscare.controller
 * @author: llw
 * @Description: 报告 首页
 * @date: 2018/6/21 14:31
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class HomePageController extends BaseController {
    private TextView mTvData;
    private CircleProgressBarView mPb;

    public HomePageController(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View rootView = View.inflate(mContext, R.layout.fragment_home_first, null);
        //日期
        mTvData = (TextView) rootView.findViewById(R.id.tv_data);
        //进度条
        mPb = (CircleProgressBarView) rootView.findViewById(R.id.pb);
        return rootView;
    }

    @Override
    public void initData() {
        startAni(83);
        try {
            String currentDate = DateTimeUtils.getCurrentDate();
            String date = DateTimeUtils.date2Chinese(currentDate);
            mTvData.setText(date);
        } catch (FormatException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onDestroy() {

    }

    private void startAni(float sweep) {
        ObjectAnimator a = ObjectAnimator.ofFloat(mPb, "progress", 0f, sweep);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a.setDuration(3000);
        a.start();
    }

    public void refreshDatas(String date, float sweep) {
        mTvData.setText(date);
        startAni(sweep);
    }
}
