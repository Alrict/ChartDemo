package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.eventbusfactory.BaseFactory;
import com.ihypnus.ihypnuscare.iface.BaseType;
import com.ihypnus.ihypnuscare.iface.DialogListener;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.StringUtils;
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
        //仅当选中的设备具有 WiFi 模块时，进入设备配置界面有此按钮。
//        若选中的设备是带有移动传输模块，则无“更改 WiFi”按钮。
//        点击“更改 WiFi”按钮后，进入到 WiFi 配置界面，可更改设备连接使
//        用的 WiFi 网络
        Intent intent = new Intent(this, WifiSettingHistoryActivity.class);
        intent.putExtra("NEW_DEVICE_ID", mNewDeviceId);
        startActivity(intent);
    }

    @Override
    protected void loadData() {
        Intent intent = getIntent();
        mNewDeviceId = intent.getStringExtra("NEW_DEVICE_ID");
        //todo 获取设备model并判断该设备是否具有wifi模块
        //根据第六位的'W'
   /*     String model = "";
        if (model.length() >= 6 && String.valueOf(model.indexOf(5)).equals("W")) {
            mBtSetWifi.setVisibility(View.VISIBLE);
        } else {
            mBtSetWifi.setVisibility(View.GONE);
        }*/


    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.bt_next:
                //设置好了
                checkTargetWifi();
                break;
            case R.id.bt_set_wifi:
                //配置wifi
                jumpToWifiSettingActivity();
                break;
        }
    }

    /**
     * 校验当前连接的wifi
     */
    private void checkTargetWifi() {
        String tips = getString(R.string.tv_tip_to_connect_wifi);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null && wifiInfo.getSSID().equals("\"Hypnus_AP\"")) {
                jumpToHomeActivity();
            } else {
                String ssid = wifiInfo.getSSID();
                if (!StringUtils.isNullOrEmpty(ssid)) {
                    tips = getString(R.string.tv_tip_current_wifi) + ssid + getString(R.string.tv_tip_please) + tips;
                }
                showTipDialog(tips);
            }
        } else {
            showTipDialog(tips);
        }
    }

    private void showTipDialog(String tip) {
        BaseDialogHelper.showNormalDialog(WifiSettingTipActivity.this, getString(R.string.tip_msg), tip, getString(R.string.cancel), getString(R.string.ok), new DialogListener() {
            @Override
            public void onClick(BaseType baseType) {
                if (BaseType.OK == baseType) {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemClick(long postion, String s) {

            }
        });
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
