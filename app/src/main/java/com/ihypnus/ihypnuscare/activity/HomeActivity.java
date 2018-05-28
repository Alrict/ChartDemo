package com.ihypnus.ihypnuscare.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.adapter.VerticalPagerAdapter;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.ihypnuscare.widget.CircleProgressBarView;
import com.ihypnus.ihypnuscare.widget.VerticalViewPager;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "llw";
    private VerticalViewPager mViewPager;
    private VerticalPagerAdapter mPagerAdapter;
    private View mainView;
    private LayoutInflater mInflater;
    private View mSecondView;
    private CircleProgressBarView mPb;
    private Animation mReLoadingAnim;
    private ImageView mIvRefresh;
    private ImageView mIvData;
    private RadioButton mRbDevice;
    private RadioButton mRbReport;
    private RadioButton mRbMyIhy;


    @Override
    protected int setView() {
        return R.layout.activity_main;
    }

    @Override
    protected void findViews() {
        //头部刷新
        mIvRefresh = (ImageView) findViewById(R.id.iv_refresh);
        //头部 日历
        mIvData = (ImageView) findViewById(R.id.iv_data);
        //viewPager
        mViewPager = (VerticalViewPager) findViewById(R.id.view_pager);

        mInflater = getLayoutInflater();

        //第一屏
        mainView = mInflater.inflate(R.layout.fragment_main, null);
        //
        mPb = (CircleProgressBarView) mainView.findViewById(R.id.pb);
        mPb.setMax(100);

        mRbDevice = (RadioButton) mainView.findViewById(R.id.rb_device);
        mRbReport = (RadioButton) mainView.findViewById(R.id.rb_report);
        mRbMyIhy = (RadioButton) mainView.findViewById(R.id.rb_my_ihy);
        mSecondView = mInflater.inflate(R.layout.fragment_second, null);


    }

    @Override
    protected void init(Bundle savedInstanceState) {

        final ArrayList<View> fragmentList = new ArrayList<>();
        fragmentList.add(mainView);
        fragmentList.add(mSecondView);
        mPagerAdapter = new VerticalPagerAdapter(fragmentList);

        //设置最小偏移量
        mViewPager.setMinPageOffset(0.15f);
        mViewPager.setAdapter(mPagerAdapter);

        //头部刷新旋转动画
        mReLoadingAnim = AnimationUtils.loadAnimation(this, R.anim.login_code_loading);
        LinearInterpolator lin = new LinearInterpolator();
        mReLoadingAnim.setInterpolator(lin);
    }

    @Override
    protected void initEvent() {
        mIvData.setOnClickListener(this);
        mIvRefresh.setOnClickListener(this);

        mRbDevice.setChecked(true);
        mRbDevice.setOnCheckedChangeListener(this);
        mRbReport.setOnCheckedChangeListener(this);
        mRbMyIhy.setOnCheckedChangeListener(this);


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("llw", "position:" + position + ",positionOffset:" + positionOffset + ",positionOffsetPixels:" + positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                //position 被选中页面
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void loadData() {
        startAni(88f);
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            return;
        }
        if (buttonView == mRbDevice) {
            //设备


        } else if (buttonView == mRbReport) {
            //报告


        } else if (buttonView == mRbMyIhy) {
            //我的

        }

    }
}
