package com.ihypnus.ihypnuscare.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.zxing.android.CaptureActivity;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 添加新设备
 * @date: 2018/5/29 16:24
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class AddNwedeviceActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvScan;
    private final int REQUEST_CODE_SCAN = 122;
    private final int REQUEST_CODE_INFO = 123;
    private EditText mEtInputDeviceSn;
    private Button mBtNext;
    private String TAG = "AddNwedeviceActivity";
    private int REQUEST_CAMERA = 132;

    @Override
    protected int setView() {
        return R.layout.activity_add_new_device;
    }

    @Override
    protected void findViews() {
        mEtInputDeviceSn = findViewById(R.id.et_input_device_sn);
        mIvScan = findViewById(R.id.iv_scan);
        mBtNext = findViewById(R.id.bt_next);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("添加设备");
    }

    @Override
    protected void initEvent() {
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

            case R.id.iv_scan:
                //检查相机权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestCameraPermission();
                } else {
                    //扫描
                    jumpToScan();
                }

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

    /**
     * 申请相机权限
     */
    private void requestCameraPermission() {

        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(Manifest.permission.CAMERA)
                .request();
        Log.i(TAG, "相机权限未被授予，需要申请！");
        // 相机权限未被授予，需要申请！
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                REQUEST_CAMERA);

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
        } else if (requestCode == REQUEST_CODE_INFO && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    /**
     * 申请权限的回调，
     *
     * @param requestCode  requestCode
     * @param permissions  permissions
     * @param grantResults grantResults 多个权限一起返回
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意相机权限
                jumpToScan();
            } else {
                //拒接,再次申请权限
                requestCameraPermission();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
