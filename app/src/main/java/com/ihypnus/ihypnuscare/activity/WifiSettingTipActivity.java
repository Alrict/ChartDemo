package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.eventbusfactory.BaseFactory;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: wifi设置提示页面
 * @date: 2018/5/29 17:24
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class WifiSettingTipActivity extends BaseActivity implements View.OnClickListener {
    private Button mBtNext;
    private Button mBtSetWifi;
    private String mNewDeviceId;


    @Override
    protected int setView() {
        return R.layout.activity_wifi_setting_tip;
    }

    @Override
    protected void findViews() {
        mBtSetWifi = findViewById(R.id.bt_set_wifi);
        mBtNext = findViewById(R.id.bt_next);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle(getResources().getString(R.string.tv_add_new_device));
    }

    @Override
    protected void initEvent() {
        mBtSetWifi.setOnClickListener(this);
        mBtNext.setOnClickListener(this);
    }


    private void jumpToWifiSettingActivity() {
        Intent intent = new Intent(this, WifiSettingHistoryActivity.class);
        intent.putExtra("NEW_DEVICE_ID", mNewDeviceId);
        startActivity(intent);
    }

    @Override
    protected void loadData() {
        Intent intent = getIntent();
        mNewDeviceId = intent.getStringExtra("NEW_DEVICE_ID");

    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.bt_next:
                //设置好了
                bindDevice();
                break;
            case R.id.bt_set_wifi:
                //配置wifi
                jumpToWifiSettingActivity();
                break;
        }
    }

    /**
     * 绑定设备
     */
    private void bindDevice() {
        BaseDialogHelper.showLoadingDialog(this, true, "正在提交...");
        IhyRequest.bindDevice(Constants.JSESSIONID, true, mNewDeviceId, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
                jumpToHomeActivity();
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }
        });
    }

    private void jumpToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        EventBus.getDefault().post(new BaseFactory.CloseActivityEvent(NewDeviceInformationActivity.class));
        EventBus.getDefault().post(new BaseFactory.RefreshDeviceListInfoEvent());
        finish();
    }

}
