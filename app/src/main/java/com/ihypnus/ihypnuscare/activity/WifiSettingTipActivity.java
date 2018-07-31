package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.eventbusfactory.BaseFactory;
import com.ihypnus.ihypnuscare.iface.BaseType;
import com.ihypnus.ihypnuscare.iface.DialogListener;
import com.ihypnus.ihypnuscare.utils.StringUtils;
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
        setTitle(R.string.tv_title_wifi_set);
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
                checkTargetWifi();

                break;
            case R.id.bt_set_wifi:
                //去设置wifi
                String tips = getString(R.string.tv_tip_to_connect_wifi);
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                if (wifiManager != null && wifiManager.getConnectionInfo() != null) {
                    String ssid = wifiManager.getConnectionInfo().getSSID();
                    if (!StringUtils.isNullOrEmpty(ssid)) {
                        tips = getString(R.string.tv_tip_current_wifi) + ssid + getString(R.string.tv_tip_please) + tips;
                    }
                }
                showTipDialog(tips);
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
                jumpToWifiSettingActivity();
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


    private void jumpToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        EventBus.getDefault().post(new BaseFactory.CloseActivityEvent(NewDeviceInformationActivity.class));
        EventBus.getDefault().post(new BaseFactory.RefreshDeviceListInfoEvent());
        finish();
    }

}
