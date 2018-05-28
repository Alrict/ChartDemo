package com.ihypnus.ihypnuscare.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.fragment.HomeFrament;
import com.ihypnus.ihypnuscare.fragment.MyIhyFragment;
import com.ihypnus.ihypnuscare.fragment.ReportFragment;

public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "HomeActivity";
    private HomeFrament mHomeFrament;
    private ReportFragment mReportFragment;
    private MyIhyFragment mMyIhyFragment;
    private RadioGroup mRgpTab;


    @Override
    protected int setView() {
        return R.layout.activity_home;
    }

    @Override
    protected void findViews() {
        mRgpTab = (RadioGroup) findViewById(R.id.rgp_tab);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mRgpTab.check(R.id.rb_device);

    }

    @Override
    protected void initEvent() {
        mRgpTab.setOnCheckedChangeListener(this);
    }

    @Override
    protected void loadData() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //设备
        if (mHomeFrament == null) {
            mHomeFrament = new HomeFrament();
            transaction.add(R.id.fragment_container, mHomeFrament);
        }
        transaction.show(mHomeFrament);
        if (null != mReportFragment) transaction.hide(mReportFragment);
        if (null != mMyIhyFragment) transaction.hide(mMyIhyFragment);

        transaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (checkedId) {
            case R.id.rb_device:
                //设备
                if (mHomeFrament == null) {
                    mHomeFrament = new HomeFrament();
                    transaction.add(R.id.fragment_container, mHomeFrament);
                } else {
                    transaction.show(mHomeFrament);
                    if (null != mReportFragment) transaction.hide(mReportFragment);
                    if (null != mMyIhyFragment) transaction.hide(mMyIhyFragment);
                }
                break;

            case R.id.rb_report:
                //报告
                if (mReportFragment == null) {
                    mReportFragment = new ReportFragment();
                    transaction.add(R.id.fragment_container, mReportFragment);
                } else {
                    transaction.show(mReportFragment);
                    if (null != mHomeFrament) transaction.hide(mHomeFrament);
                    if (null != mMyIhyFragment) transaction.hide(mMyIhyFragment);
                }
                break;

            case R.id.rb_my_ihy:
                //我的
                if (mMyIhyFragment == null) {
                    mMyIhyFragment = new MyIhyFragment();
                    transaction.add(R.id.fragment_container, mMyIhyFragment);
                } else {
                    transaction.show(mMyIhyFragment);
                    if (null != mHomeFrament) transaction.hide(mHomeFrament);
                    if (null != mReportFragment) transaction.hide(mReportFragment);
                }
                break;
        }
        transaction.commit();
    }
}
