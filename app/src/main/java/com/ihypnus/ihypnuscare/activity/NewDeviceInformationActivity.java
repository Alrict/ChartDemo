package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 新增设备提示信息
 * @date: 2018/5/29 17:06
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class NewDeviceInformationActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mIvBack;
    private Button mBtNext;
    private final int REQUEST_CODE = 123;

    @Override
    protected int setView() {
        return R.layout.activity_new_device_information;
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
                //下一步
                jumpToWifiSettingActivity();
                break;


        }
    }

    /**
     * 跳转至wifi设置页面
     */
    private void jumpToWifiSettingActivity() {
        Intent intent = new Intent(this, WifiSettingTipActivity.class);
        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
