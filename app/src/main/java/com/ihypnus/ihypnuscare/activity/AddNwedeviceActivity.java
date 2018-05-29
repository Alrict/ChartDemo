package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.zxing.android.CaptureActivity;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 添加新设备
 * @date: 2018/5/29 16:24
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class AddNwedeviceActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private ImageView mIvScan;
    private final int REQUEST_CODE_SCAN = 122;
    private final int REQUEST_CODE_INFO = 123;
    private EditText mEtInputDeviceSn;
    private Button mBtNext;

    @Override
    protected int setView() {
        return R.layout.activity_add_new_device;
    }

    @Override
    protected void findViews() {
        mIvBack = findViewById(R.id.iv_back);
        mEtInputDeviceSn = findViewById(R.id.et_input_device_sn);
        mIvScan = findViewById(R.id.iv_scan);
        mBtNext = findViewById(R.id.bt_next);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mIvBack.setOnClickListener(this);
        mIvScan.setOnClickListener(this);
        mBtNext.setOnClickListener(this);

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

            case R.id.iv_scan:
                //扫描
                jumpToScan();
                break;

            case R.id.bt_next:
                //下一步
                if (!StringUtils.isNullOrEmpty(mEtInputDeviceSn.getText().toString().trim())) {
                    jumpToNewDeviceInformationActivity();
                } else {
                    ToastUtils.showToastDefault(AddNwedeviceActivity.this, mEtInputDeviceSn.getHint().toString());
                }
                break;
        }
    }

    private void jumpToNewDeviceInformationActivity() {
        Intent intent = new Intent(this, NewDeviceInformationActivity.class);
        startActivityForResult(intent, REQUEST_CODE_INFO);
    }

    private void jumpToScan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            String deviceSN = data.getStringExtra("id");
            if (!StringUtils.isNullOrEmpty(deviceSN)) {
                mEtInputDeviceSn.setText(deviceSN);
                mEtInputDeviceSn.setSelection(deviceSN.length());
            }
        }else if (requestCode == REQUEST_CODE_INFO && resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }
}
