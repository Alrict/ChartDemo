package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.os.Bundle;

import com.ihypnus.ihypnuscare.R;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 参数设置页面
 * @date: 2018/7/16 17:39
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ParameterSettingsActivity extends BaseActivity {
    @Override
    protected int setView() {
        return R.layout.activity_parameter_settings;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String deviceId = intent.getStringExtra("DEVICE_ID");
        setTitle(deviceId);
//        setTitle(getResources().getString(R.string.parameter_setting));
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void loadData() {

    }
}
