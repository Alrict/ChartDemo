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
import android.widget.ImageView;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.DeviceModelVO;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.eventbusfactory.BaseFactory;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.widget.AppTextView;
import com.ihypnus.zxing.android.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
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
    private AppTextView mEtInputDeviceSn;
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
        setTitle(R.string.add_new_device);
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
            String model = data.getStringExtra("model");
            if (!StringUtils.isNullOrEmpty(deviceSN)) {
                mEtInputDeviceSn.setText(deviceSN);
                //绑定次设备
                bindDevice(deviceSN, model);
            }
        } else if (requestCode == REQUEST_CODE_INFO && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    /**
     * 绑定设备
     */
    private void bindDevice(final String deviceId, final String model) {
        BaseDialogHelper.showLoadingDialog(this, true, "正在提交...");
        IhyRequest.bindDevice(Constants.JSESSIONID, true, deviceId, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
//                BaseDialogHelper.dismissLoadingDialog();
//                ToastUtils.showToastDefault(var3);
                //判断该设备号是否支持wifi设置
                EventBus.getDefault().post(new BaseFactory.CheckFragment(0));
                if (model.length() >= 6 && model.substring(5, 6).equals("W")) {

                    //设备是V2.0版本以上的,且支持wifi设置
                    BaseDialogHelper.dismissLoadingDialog();
                    mBtNext.setVisibility(View.VISIBLE);

                } else if (model.length() >= 6 && !String.valueOf(model.indexOf(5)).equals("W")) {
                    //设备是V2.0版本以上的,不支持wifi设置,返回设备列表界面并刷新
                    BaseDialogHelper.dismissLoadingDialog();

                    finish();
                } else {
                    //调用接口获取该设备的信息,根据model字段判断是否需要显示下一步按钮
                    getDeviceInfos(deviceId);
                }
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }
        });
    }

    /**
     * 获取设备model,判断是否可以使用wifi设置模块
     *
     * @param deviceId
     */
    private void getDeviceInfos(final String deviceId) {

        IhyRequest.getDeviceInfos(deviceId, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                DeviceModelVO deviceModelVO = (DeviceModelVO) var1;
                if (deviceModelVO != null && !StringUtils.isNullOrEmpty(deviceModelVO.getModel())) {
                    String model = deviceModelVO.getModel();
                    //判断该设备号是否支持wifi设置
                    if (model.length() >= 6 && String.valueOf(model.indexOf(5)).equals("W")) {
                        //设备是V2.0版本以上的,且支持wifi设置
                        mBtNext.setVisibility(View.VISIBLE);
                    } else {
                        //调用接口获取该设备的信息,根据model字段判断是否需要显示下一步按钮
                        mBtNext.setVisibility(View.INVISIBLE);
                        EventBus.getDefault().post(new BaseFactory.CheckFragment(0));
                        finish();
                    }
                } else {
                    //调用接口获取该设备的信息,根据model字段判断是否需要显示下一步按钮
                    mBtNext.setVisibility(View.INVISIBLE);
                    EventBus.getDefault().post(new BaseFactory.CheckFragment(0));
                    finish();
                }

            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }
        });
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
