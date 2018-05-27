package com.ihypnus.ihypnuscare.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.adapter.VerticalPagerAdapter;
import com.ihypnus.ihypnuscare.widget.CircleProgressBarView;
import com.ihypnus.ihypnuscare.widget.VerticalViewPager;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity {

    private static final String TAG = "llw";
    private VerticalViewPager mViewPager;
    private VerticalPagerAdapter mPagerAdapter;
    private View mainView;
    private LayoutInflater mInflater;
    private View mSecondView;
    private CircleProgressBarView mPb;
    private TextView mTvLoad;


    @Override
    protected int setView() {
        return R.layout.activity_main;
    }

    @Override
    protected void findViews() {
        mViewPager = (VerticalViewPager) findViewById(R.id.view_pager);

        mInflater = getLayoutInflater();
        mainView = mInflater.inflate(R.layout.fragment_main, null);
        mTvLoad = (TextView) mainView.findViewById(R.id.tv_load);
        mPb = (CircleProgressBarView) mainView.findViewById(R.id.pb);
        mPb.setMax(100);
        mSecondView = mInflater.inflate(R.layout.fragment_second, null);


//        mViewPager.setPageTransformer(true, new DefaultTransformer());


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
    }

    @Override
    protected void initEvent() {
        mTvLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAni(88f);
            }
        });

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

    }

    private void startAni(float sweep) {
        ObjectAnimator a = ObjectAnimator.ofFloat(mPb, "progress", 0f, sweep);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a.setDuration(3000);
        a.start();
    }

}
