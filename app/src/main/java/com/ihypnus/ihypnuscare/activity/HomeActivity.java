package com.ihypnus.ihypnuscare.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.DefaultDeviceIdVO;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.fragment.DeviceFragment;
import com.ihypnus.ihypnuscare.fragment.MyIhyFragment;
import com.ihypnus.ihypnuscare.fragment.ReportFragment;
import com.ihypnus.ihypnuscare.iface.BaseType;
import com.ihypnus.ihypnuscare.iface.DialogListener;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;

public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "HomeActivity";
    private DeviceFragment mDeviceFragment;
    private ReportFragment mReportFragment;
    private MyIhyFragment mMyIhyFragment;
    private RadioGroup mRgpTab;
    private long exitTime;


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
        mRgpTab.check(R.id.rb_report);
        getSupportedActionBar().setVisibility(View.GONE);
    }

    @Override
    protected void initEvent() {
        mRgpTab.setOnCheckedChangeListener(this);
    }

    @Override
    protected void loadData() {
        getDefaultDeviceInfos();
//        checkReportFragment();

    }

    private void getDefaultDeviceInfos() {
        BaseDialogHelper.showLoadingDialog(this, true, "正在加载...");
        IhyRequest.getDefaultDeviceId(Constants.JSESSIONID, true, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                DefaultDeviceIdVO defaultDeviceIdVO = (DefaultDeviceIdVO) var1;
                if (defaultDeviceIdVO != null && !StringUtils.isNullOrEmpty(defaultDeviceIdVO.getDeviceId())) {
                    LogOut.d("llw", "获取默认设备id:" + defaultDeviceIdVO.getDeviceId());
                    Constants.DEVICEID = defaultDeviceIdVO.getDeviceId();
                    checkReportFragment();
                } else {
                    BaseDialogHelper.showSimpleDialog(HomeActivity.this, "温馨提示", "您目前未绑定任何相关设备", "前去绑定", new DialogListener() {
                        @Override
                        public void onClick(BaseType baseType) {
                            jumpToBindDeviceActivity();
                        }

                        @Override
                        public void onItemClick(long postion, String s) {

                        }
                    });
                }

            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
            }
        });
    }

    /**
     * 跳转至绑定设备页面
     */
    private void jumpToBindDeviceActivity() {
        Intent intent = new Intent(this, AddNwedeviceActivity.class);
        startActivity(intent);
        finish();

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
            ToastUtils.showToastInCenter(getApplicationContext(), "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
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
}
