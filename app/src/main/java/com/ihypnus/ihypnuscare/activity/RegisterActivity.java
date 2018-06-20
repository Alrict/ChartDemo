package com.ihypnus.ihypnuscare.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.UserInfo;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.ihypnuscare.widget.SpannableStringUtil;
import com.ihypnus.zxing.android.CaptureActivity;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 注册
 * @date: 2018/5/14 13:45
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Animation mCodeLoadingAnim;
    private EditText mEtCount;
    private ImageView mIvCodeLoading;
    private Button mBtnVcerificationCode;
    private TextView mTvLocalCode;
    private ImageView mIvDownArrow;
    private ImageView mImgLoginAccountClear;
    private EditText mEtPassWord;
    private CheckBox mCbVisible;
    private EditText mEtVcerificationCode;
    private EditText mEtDeviceCode;
    private ImageView mIvScan;
    private CheckBox mCbProtocol;
    private TextView mTvProtocol;
    private Button mIdRegister;
    private TextView mTvLogin;
    private ImageView mIvWechart;
    private ImageView mIvQq;
    private ImageView mIvOther;
    private String TAG = "RegisterActivity";
    private int REQUEST_CAMERA = 132;
    private final int REQUEST_CODE_SCAN = 122;
    private SpannableString mSpannableString;
    private String mProtocol;
    private Gson mGson = new Gson();

    @Override
    protected int setView() {
        return R.layout.activity_register;
    }

    @Override
    protected void findViews() {

        //帐号
        mEtCount = findViewById(R.id.et_count);

        //地区号
        mTvLocalCode = (TextView) findViewById(R.id.tv_local_code);
        mIvDownArrow = (ImageView) findViewById(R.id.iv_down_arrow);


        //清除帐号
        mImgLoginAccountClear = (ImageView) findViewById(R.id.img_login_account_clear2);

        //密码
        mEtPassWord = (EditText) findViewById(R.id.edit_login_code2);
        //密码是否可见
        mCbVisible = (CheckBox) findViewById(R.id.cb_visible);

        //验证码
        mEtVcerificationCode = (EditText) findViewById(R.id.et_vcerification_code);
        //正在获取验证码动画
        mIvCodeLoading = (ImageView) findViewById(R.id.iv_code_loading);
        //获取验证码
        mBtnVcerificationCode = (Button) findViewById(R.id.btn_vcerification_code);

        //设备SN
        mEtDeviceCode = (EditText) findViewById(R.id.et_device_code);
        mIvScan = (ImageView) findViewById(R.id.iv_scan);

        //协议
        mCbProtocol = (CheckBox) findViewById(R.id.cb_protocol);
        mTvProtocol = (TextView) findViewById(R.id.tv_protocol);

        //注册
        mIdRegister = (Button) findViewById(R.id.id_register);
        //有账号去登入
        mTvLogin = (TextView) findViewById(R.id.tv_login);

        mIvWechart = (ImageView) findViewById(R.id.iv_wechart);
        mIvQq = (ImageView) findViewById(R.id.iv_qq);
        mIvOther = (ImageView) findViewById(R.id.iv_other);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getSupportedActionBar().setVisibility(View.GONE);
        //获取验证码动画
        mCodeLoadingAnim = AnimationUtils.loadAnimation(this, R.anim.login_code_loading);
        LinearInterpolator lin = new LinearInterpolator();
        mCodeLoadingAnim.setInterpolator(lin);
        mProtocol = getResources().getString(R.string.use_protocol);
        mSpannableString = SpannableStringUtil.addForeColorSpan(mProtocol, 2, mProtocol.length(), R.color.main_color_blue);

    }

    @Override
    protected void initEvent() {
        mBtnVcerificationCode.setOnClickListener(this);
        mIvScan.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
        mIdRegister.setOnClickListener(this);
        mIvWechart.setOnClickListener(this);
        mIvQq.setOnClickListener(this);
        mIvOther.setOnClickListener(this);
//        mSpannableString.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View view) {
//                ToastUtils.showToastDefault(RegisterActivity.this, "浏览协议");
//            }
//        }, 2, mProtocol.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        mTvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
        mTvProtocol.setText(mSpannableString);

        mCbVisible.setOnCheckedChangeListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (v.getId()) {
            case R.id.btn_vcerification_code:
                String countNo = mEtCount.getText().toString().trim();
                if (StringUtils.isNullOrEmpty(countNo)) {
                    ToastUtils.showToastDefault(RegisterActivity.this, mEtCount.getHint().toString());
                    return;
                }
                mIvCodeLoading.setVisibility(View.VISIBLE);
                mBtnVcerificationCode.setVisibility(View.GONE);
                mIvCodeLoading.startAnimation(mCodeLoadingAnim);
                break;

            case R.id.iv_scan:
                //检查相机权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestCameraPermission();
                } else {
                    //扫描
                    jumpToScan();
                }
                break;

            case R.id.id_register:
                //注册
                if (checkInfos()) {
                    registerAppByNet();

                }

                break;

            case R.id.tv_login:
                //有账号去登入
                jumpToLogin();
                break;

            case R.id.iv_wechart:
                //微信

                break;
            case R.id.iv_qq:
                //qq


                break;

            case R.id.iv_other:
                //其他

                break;
        }
    }

    private void registerAppByNet() {
        BaseDialogHelper.showLoadingDialog(this, false, "正在登入");
        UserInfo userInfo = new UserInfo(mEtCount.getText().toString().trim(), mEtPassWord.getText().toString().trim());
//        String userInfos = mGson.toJson(userInfo);
        String deviceId = mEtDeviceCode.getText().toString().trim();
        IhyRequest.registerApp(userInfo, deviceId, new Response.Listener<Object>() {
            @Override
            public void onResponse(Object response) {
                BaseDialogHelper.dismissLoadingDialog();
                jumpToPersonMsg();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                BaseDialogHelper.dismissLoadingDialog();
            }
        });
    }

    /**
     * 跳转至个人信息页面
     */
    private void jumpToPersonMsg() {
        Intent intent = new Intent(this, PersonalInformationActivity.class);
        startActivity(intent);
    }

    /**
     * 校验输入内容
     */
    private boolean checkInfos() {
        String account = mEtCount.getText().toString().trim();
        if (StringUtils.isNullOrEmpty(account)) {
            ToastUtils.showToastDefault(this, mEtCount.getHint().toString());
            return false;
        }

        String passWord = mEtPassWord.getText().toString().trim();
        if (StringUtils.isNullOrEmpty(passWord)) {
            ToastUtils.showToastDefault(this, mEtPassWord.getHint().toString());
            return false;
        }

        String vcerificationCode = mEtVcerificationCode.getText().toString().trim();
        if (StringUtils.isNullOrEmpty(vcerificationCode)) {
            ToastUtils.showToastDefault(this, mEtVcerificationCode.getHint().toString());
            return false;
        }

        String deviceSn = mEtDeviceCode.getText().toString().trim();
        if (StringUtils.isNullOrEmpty(deviceSn)) {
            ToastUtils.showToastDefault(this, mEtDeviceCode.getHint().toString());
            return false;
        }

        return true;

    }

    private void jumpToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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
                mEtDeviceCode.setText(deviceSN);
                mEtDeviceCode.setSelection(deviceSN.length());
            }
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        String passWord = mEtPassWord.getText().toString();
        if (isChecked) {
            //设置EditText的密码为可见的
            mEtPassWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //设置密码为隐藏的
            mEtPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        mEtPassWord.setSelection(passWord.length());
    }
}
