package com.ihypnus.ihypnuscare.fragment;

import android.content.Intent;
import android.nfc.FormatException;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.activity.SelectDateActivity;
import com.ihypnus.ihypnuscare.adapter.VerticalPagerAdapter;
import com.ihypnus.ihypnuscare.controller.BaseController;
import com.ihypnus.ihypnuscare.controller.ChartsPage1Controller;
import com.ihypnus.ihypnuscare.controller.ChartsPage2Controller;
import com.ihypnus.ihypnuscare.controller.ChartsPage3Controller;
import com.ihypnus.ihypnuscare.controller.HomePageController;
import com.ihypnus.ihypnuscare.utils.DateTimeUtils;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.ihypnuscare.widget.VerticalViewPager;

import java.util.ArrayList;
import java.util.Random;

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
    private Animation mReLoadingAnim;
    private ImageView mIvRefresh;
    private ImageView mIvData;
    public static final int REQUEST_START_TIME = 200;
    public static final int RESPONSE_SELECT_OK = 202;
    private HomePageController mHomePageController;
    private ChartsPage1Controller mChartsPage1Controller;
    private ChartsPage2Controller mChartsPage2Controller;
    private ChartsPage3Controller mChartsPage3Controller;
    private LinearLayout mLayoutWeekData;
    private TextView mTvDate;
    private float mOldPositionOffset;

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


    }


    @Override
    protected void init() {

        ArrayList<BaseController> fragmentList = new ArrayList<>();
        mHomePageController = new HomePageController(mAct);
        mChartsPage1Controller = new ChartsPage1Controller(mAct);
        mChartsPage2Controller = new ChartsPage2Controller(mAct);
        mChartsPage3Controller = new ChartsPage3Controller(mAct);
        fragmentList.add(mHomePageController);
        fragmentList.add(mChartsPage1Controller);
        fragmentList.add(mChartsPage2Controller);
        fragmentList.add(mChartsPage3Controller);
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

    }


    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_refresh:
                //刷新
                mIvRefresh.startAnimation(mReLoadingAnim);

                break;
            case R.id.iv_data:
                String currentDate = DateTimeUtils.getCurrentDate();
                jumpSelecteDateActivity("日期选择", currentDate, REQUEST_START_TIME);
                break;
        }
    }

    /**
     * 跳转选择日期
     *
     * @param title
     * @param time
     * @param requestCode
     */
    private void jumpSelecteDateActivity(String title, String time, int requestCode) {
        Intent intent = new Intent(mAct, SelectDateActivity.class);
        intent.putExtra("time", time);
        intent.putExtra("title", title);
        this.startActivityForResult(intent, requestCode);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("llw", "position:" + position + ",positionOffset:" + positionOffset + ",positionOffsetPixels:" + positionOffsetPixels);
        //v小于0,向上滑,反之向下滑
//        if (positionOffset == 0 || positionOffset == 1) {
//            mOldPositionOffset = 0;
//        }
//        float v = positionOffset - mOldPositionOffset;
//        if (position == 0) {
//            mLayoutWeekData.setVisibility(View.GONE);
//        } else if (position == 1 && v < 0) {
//            mLayoutWeekData.setVisibility(View.GONE);
//        } else if (position >= 1) {
//            mLayoutWeekData.setVisibility(View.VISIBLE);
//        }
//        mOldPositionOffset = positionOffset;
//        mLayoutWeekData.setAnimation();
    }

    @Override
    public void onPageSelected(int position) {
        LogOut.d("llw", "position:" + position);
//        mLayoutWeekData.setVisibility(position > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        LogOut.d("llw", "state:" + state);
        int position = mViewPager.getVerticalScrollbarPosition();

//        if (state == 0) {
//            mOldPositionOffset = 0;
//        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_START_TIME && resultCode == RESPONSE_SELECT_OK) {
            if (null != data) {
                String time = initTime(data);
                long longTime = DateTimeUtils.getSimpleLongTime(time);


                try {
                    String date = DateTimeUtils.date2Chinese(time);
                    Random random = new Random();
                    int i = random.nextInt(100);
                    mHomePageController.setDate(longTime);
                    if (mHomePageController != null) {
                        mHomePageController.refreshDatas(date, i);
                    }
                } catch (FormatException e) {
                    e.printStackTrace();

                }
            }
        }
    }

    private String initTime(Intent data) {
        int year = data.getIntExtra("year", -1);
        int month = data.getIntExtra("month", -1);
        int day = data.getIntExtra("day", -1);
        return year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day);
    }
}
