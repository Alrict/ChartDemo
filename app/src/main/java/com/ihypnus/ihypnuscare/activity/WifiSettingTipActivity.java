package com.ihypnus.ihypnuscare.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: wifi设置提示页面
 * @date: 2018/5/29 17:24
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class WifiSettingTipActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mIvBack;
    private Button mBtNext;

    @Override
    protected int setView() {
        return R.layout.activity_wifi_setting_tip;
    }

    @Override
    protected void findViews() {
        mIvBack = findViewById(R.id.iv_back);
        mBtNext = findViewById(R.id.bt_next);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mIvBack.setOnClickListener(this);
        mBtNext.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_back:
                //返回
                finish();
                break;

            case R.id.bt_next:
                //设置好了
                jumpToWifiSettingActivity();
                break;
            case R.id.bt_set_wifi:
                //下一步
                jumpToWifiSetting();
                break;
        }
    }

    /**
     * 进入系统wifi设置界面
     */
    private void jumpToWifiSetting() {

    }


    private void jumpToWifiSettingActivity() {
        setResult(RESULT_OK);
        finish();
    }
}
