package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.os.Bundle;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.DeviceInfoVO;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description:
 * @date: 2018/6/18 22:00
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class DeviceDetailActivity extends BaseActivity {
    @Override
    protected int setView() {
        return R.layout.activity_device_detail;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        DeviceInfoVO bean = intent.getParcelableExtra("DATA_BEAN");
        if (bean != null) {
            setTitle(bean.getDeviceNo());
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void loadData() {

    }
}
