package com.ihypnus.ihypnuscare.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ihypnus.ihypnuscare.IhyApplication;
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
    private RadioButton[] mRbs;


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
        initRadioButtom();
    }

    private void initRadioButtom() {
        mRbs = new RadioButton[3];
//初始化控件，中间大个的，周围小弟
        mRbs[0] = (RadioButton) findViewById(R.id.rb_device);
        mRbs[1] = (RadioButton) findViewById(R.id.rb_report);
        mRbs[2] = (RadioButton) findViewById(R.id.rb_my_ihy);

        for (RadioButton rb : mRbs) {
            //挨着给每个RadioButton加入drawable限制边距以控制显示大小
            Drawable[] drs = rb.getCompoundDrawables();
            //获取drawables
            Rect r = new Rect(0, 0, drs[1].getMinimumWidth() * 3 / 3, drs[1].getMinimumHeight() * 3 / 3);
            //定义一个Rect边界
            drs[1].setBounds(r);
            //给drawable设置边界
            if (rb.getId() == R.id.rb_report) {
                r = new Rect(0, 0, drs[1].getMinimumWidth(), drs[1].getMinimumHeight());
                drs[1].setBounds(r);
            }

            rb.setCompoundDrawables(null, drs[1], null, null);
            //添加限制给控件
        }
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
//            if (mReportFragment != null) {
//                ReportFragment.sCurrentTime = System.currentTimeMillis();
//            }
            IhyApplication.mInstance.setUser(null);
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
                    transaction.show(mDeviceFragment);
                    if (null != mReportFragment) transaction.hide(mReportFragment);
                    if (null != mMyIhyFragment) transaction.hide(mMyIhyFragment);
                    transaction.commit();
                } else {
                    if (null != mReportFragment) transaction.hide(mReportFragment);
                    if (null != mMyIhyFragment) transaction.hide(mMyIhyFragment);
                    mDeviceFragment.getDataList();
                }
                break;
        }

    }

    @Subscribe
    public void eventMainThread(BaseFactory.CloseActivityEvent event) {
        if (event.getCls() == HomeActivity.class) {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
