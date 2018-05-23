package com.ihypnus.ihypnuscare.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.adapter.VerticalPagerAdapter;
import com.ihypnus.ihypnuscare.widget.CircleProgressView;
import com.ihypnus.ihypnuscare.widget.ProgressBarView2;
import com.ihypnus.ihypnuscare.widget.VerticalViewPager;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "llw";
    private VerticalViewPager mViewPager;
    private VerticalPagerAdapter mPagerAdapter;
    private View mainView;
    private LayoutInflater mInflater;
    private View mSecondView;
    private ProgressBarView2 mPb;
    private CircleProgressView mCicleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (VerticalViewPager) findViewById(R.id.view_pager);

        mInflater = getLayoutInflater();

        mainView = mInflater.inflate(R.layout.fragment_main, null);
        TextView tv_load = (TextView) mainView.findViewById(R.id.tv_load);


        mPb = (ProgressBarView2) mainView.findViewById(R.id.pb);
        mPb.setMax(100);

        tv_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPb.setProgress(77);
            }
        });
        mSecondView = mInflater.inflate(R.layout.fragment_second, null);
        mCicleProgressBar = mSecondView.findViewById(R.id.cpv);
        mCicleProgressBar.setMaxProgress(100);

        final ArrayList<View> fragmentList = new ArrayList<>();

        fragmentList.add(mainView);
        fragmentList.add(mSecondView);
//        mViewPager.setPageTransformer(true, new DefaultTransformer());

        mPagerAdapter = new VerticalPagerAdapter(fragmentList);
        //设置最小偏移量
        mViewPager.setMinPageOffset(0.1f);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("llw", "position:" + position + ",positionOffset:" + positionOffset + ",positionOffsetPixels:" + positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                //position 被选中页面
                if (position==1){
                    mCicleProgressBar.setProgressNotInUiThread(73);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


}
