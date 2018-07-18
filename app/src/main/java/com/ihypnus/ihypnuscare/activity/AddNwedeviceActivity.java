package com.ihypnus.ihypnuscare.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.eventbusfactory.BaseFactory;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.zxing.android.CaptureActivity;

import org.greenrobot.eventbus.Subscribe;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

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
        setTitle(getResources().getString(R.string.add_new_device));
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
                String deviceId = mEtInputDeviceSn.getText().toString().trim();
                if (!StringUtils.isNullOrEmpty(deviceId)) {
                    jumpToNewDeviceInformationActivity(deviceId);
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
                .addRequestCode(REQUEST_CAMERA)
                .permissions(Manifest.permission.CAMERA)
                .request();

    }


    private void jumpToNewDeviceInformationActivity(String deviceId) {
        Intent intent = new Intent(this, NewDeviceInformationActivity.class);
        intent.putExtra("NEW_DEVICE_ID", deviceId);
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
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 132)
    private void requestPhotoSuccess() {
        //成功之后的处理
        //同意相机权限
        jumpToScan();
    }

    @PermissionFail(requestCode = 132)
    public void requestPhotoFail() {
        //失败之后的处理，我一般是跳到设置界面
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
    }

    @Subscribe
    public void onEventMainThread(BaseFactory.CloseActivityEvent event) {
        Class<?> cls = event.getCls();
        if (cls == AddNwedeviceActivity.class) {
            finish();
        }
    }
}
