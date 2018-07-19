package com.ihypnus.ihypnuscare.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.eventbusfactory.BaseFactory;
import com.ihypnus.ihypnuscare.fragment.DeviceFragment;
import com.ihypnus.ihypnuscare.fragment.MyIhyFragment;
import com.ihypnus.ihypnuscare.fragment.ReportFragment;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;

public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "HomeActivity";
    private DeviceFragment mDeviceFragment;
    private ReportFragment mReportFragment;
    private MyIhyFragment mMyIhyFragment;
    private RadioGroup mRgpTab;
    private long exitTime;


    @Override
    protected int setView() {
//        EventBus.getDefault().register(this);
        return R.layout.activity_home;
    }

    @Override
    protected void findViews() {
        mRgpTab = (RadioGroup) findViewById(R.id.rgp_tab);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mRgpTab.check(R.id.rb_report);
        getSupportedActionBar().setVisibility(View.GONE);
    }

    @Override
    protected void initEvent() {
        mRgpTab.setOnCheckedChangeListener(this);
    }

    @Override
    protected void loadData() {
        checkReportFragment();
    }


    private void checkReportFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //设备
        if (mReportFragment == null) {
            mReportFragment = new ReportFragment();
            transaction.add(R.id.fragment_container, mReportFragment);
        }
        transaction.show(mReportFragment);
        if (null != mDeviceFragment) transaction.hide(mDeviceFragment);
        if (null != mMyIhyFragment) transaction.hide(mMyIhyFragment);

        transaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (checkedId) {
            case R.id.rb_device:
                //设备
                if (mDeviceFragment == null) {
                    mDeviceFragment = new DeviceFragment();
                    transaction.add(R.id.fragment_container, mDeviceFragment);
                }
                transaction.show(mDeviceFragment);
                if (null != mReportFragment) transaction.hide(mReportFragment);
                if (null != mMyIhyFragment) transaction.hide(mMyIhyFragment);

                break;

            case R.id.rb_report:
                //报告
                if (mReportFragment == null) {
                    mReportFragment = new ReportFragment();
                    transaction.add(R.id.fragment_container, mReportFragment);
                }
                transaction.show(mReportFragment);
                if (null != mDeviceFragment) transaction.hide(mDeviceFragment);
                if (null != mMyIhyFragment) transaction.hide(mMyIhyFragment);

                break;

            case R.id.rb_my_ihy:
                //我的
                if (mMyIhyFragment == null) {
                    mMyIhyFragment = new MyIhyFragment();
                    transaction.add(R.id.fragment_container, mMyIhyFragment);
                }
                transaction.show(mMyIhyFragment);
                if (null != mDeviceFragment) transaction.hide(mDeviceFragment);
                if (null != mReportFragment) transaction.hide(mReportFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            reTryExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 在主界面按两次back键退出App
     */
    private void reTryExit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.showToastInCenter(getApplicationContext(), getString(R.string.tv_toast_exit_if_double));
            exitTime = System.currentTimeMillis();
        } else {
            //登出
            MobclickAgent.onProfileSignOff();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (mMyIhyFragment != null && requestCode == 1 || requestCode == 2 || requestCode == 3) {
                mMyIhyFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mMyIhyFragment != null && requestCode == 1) {
            mMyIhyFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Subscribe
    public void onEventMainThread(BaseFactory.CheckFragment event) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        int type = event.getType();
        switch (type) {
            case 0:
                //设备
                if (mDeviceFragment == null) {
                    mDeviceFragment = new DeviceFragment();
                    transaction.add(R.id.fragment_container, mDeviceFragment);
                }
                transaction.show(mDeviceFragment);
                if (null != mReportFragment) transaction.hide(mReportFragment);
                if (null != mMyIhyFragment) transaction.hide(mMyIhyFragment);
                break;

            case 1:
                //报告
                if (mReportFragment == null) {
                    mReportFragment = new ReportFragment();
                    transaction.add(R.id.fragment_container, mReportFragment);
                }
                transaction.show(mReportFragment);
                if (null != mDeviceFragment) transaction.hide(mDeviceFragment);
                if (null != mMyIhyFragment) transaction.hide(mMyIhyFragment);
                break;

            case 2:
                //我的
                if (mMyIhyFragment == null) {
                    mMyIhyFragment = new MyIhyFragment();
                    transaction.add(R.id.fragment_container, mMyIhyFragment);
                }
                transaction.show(mMyIhyFragment);
                if (null != mDeviceFragment) transaction.hide(mDeviceFragment);
                if (null != mReportFragment) transaction.hide(mReportFragment);
                break;
        }
        transaction.commit();

    }
}
