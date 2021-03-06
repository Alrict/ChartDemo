package com.ihypnus.ihypnuscare.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.CountryCodeVO;
import com.ihypnus.ihypnuscare.bean.UserInfo;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.dialog.IhyBaseDialog;
import com.ihypnus.ihypnuscare.iface.BaseType;
import com.ihypnus.ihypnuscare.iface.DialogListener;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.HandlerErrorUtils;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.ihypnus.ihypnuscare.widget.SpannableStringUtil;
import com.ihypnus.zxing.android.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 注册
 * @date: 2018/5/14 13:45
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher {
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
    private TextView mEtDeviceCode;
    private ImageView mIvScan;
    private CheckBox mCbProtocol;
    private TextView mTvProtocol;
    private Button mIdRegister;
    private TextView mTvLogin;
    private ImageView mIvWechart;
    private ImageView mIvQq;
    private ImageView mIvOther;
    private String TAG = "RegisterActivity";
    private static final int REQUEST_CAMERA = 132;
    private static final int REQUEST_CODE_SCAN = 122;
    private SpannableString mSpannableString;
    private String mProtocol;
    private Gson mGson = new Gson();
    private TimerCountDown mTimerCountDown = new TimerCountDown(120 * 1000 + 500, 1000);
    private boolean mVertifySuccess = false;

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
        mEtDeviceCode = (TextView) findViewById(R.id.et_device_code);
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
        mSpannableString = SpannableStringUtil.addForeColorSpan(mProtocol, 0, mProtocol.length(), Color.parseColor("#0F84C0"));

    }

    @Override
    protected void initEvent() {
        mTvLocalCode.setOnClickListener(this);
        mIvDownArrow.setOnClickListener(this);
        mEtCount.addTextChangedListener(this);
        mEtPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int index = mEtPassWord.getSelectionStart() - 1;
                String password = mEtPassWord.getText().toString().trim();
                if (s.length() > 0 && index >= 0) {
                    if (!StringUtils.vertifyIllegal(password)) {
                        ToastUtils.showToastDefault(getString(R.string.tv_toast_illegal_string));
                        s.delete(index, index + 1);
                    }
                }

            }
        });
        mBtnVcerificationCode.setOnClickListener(this);
        mIvScan.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
        mIdRegister.setOnClickListener(this);
        mIvWechart.setOnClickListener(this);
        mIvQq.setOnClickListener(this);
        mIvOther.setOnClickListener(this);
        mTvProtocol.setOnClickListener(this);
        mImgLoginAccountClear.setOnClickListener(this);
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
            case R.id.tv_local_code:
            case R.id.iv_down_arrow:
                //国家区号选择
                getCountryCodeByNet();
                break;
            case R.id.img_login_account_clear2:
                //清除手机号
                mEtCount.setText("");
                break;

            case R.id.btn_vcerification_code:
                //获取验证码
                String countNo = mEtCount.getText().toString().trim();
                if (StringUtils.isNullOrEmpty(countNo)) {
                    ToastUtils.showToastDefault(RegisterActivity.this, mEtCount.getHint().toString());
                    return;
                }
                if (countNo.length() < 11) {
                    ToastUtils.showToastDefault(RegisterActivity.this, getString(R.string.tv_toast_phone_error));
                    return;
                }
                mIvCodeLoading.setVisibility(View.VISIBLE);
                mBtnVcerificationCode.setVisibility(View.GONE);
                mIvCodeLoading.startAnimation(mCodeLoadingAnim);
                String code = mTvLocalCode.getText().toString().trim();
                getVerifyCodeByNet(countNo, code);
                break;

            case R.id.iv_scan:
                //检查相机权限
                LogOut.d("llw", "点击扫描");
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
            case R.id.tv_protocol:
                //协议
                showProtocol();
                break;
        }
    }

    private void showProtocol() {
        IhyBaseDialog.createKyeBaseDialog(this, R.layout.dialog_protocol, new IhyBaseDialog.DialogListener() {
            @Override
            public void bindView(View view, final IhyBaseDialog kyeBaseDialog) {
                TextView title = (TextView) view.findViewById(R.id.title);
                TextView content = (TextView) view.findViewById(R.id.content);
                title.setText(R.string.tv_protocol_title);
                content.setText(R.string.tv_protocol);
                TextView back = (TextView) view.findViewById(R.id.btn_back);
                back.setText(getString(R.string.tv_btn_back));
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kyeBaseDialog.dismiss();
                    }
                });
            }
        });
    }

    private void registerAppByNet() {
        BaseDialogHelper.showLoadingDialog(this, true, getString(R.string.onloading));
        final String account = mEtCount.getText().toString().trim();
        String password = mEtPassWord.getText().toString().trim();
        UserInfo userInfo = new UserInfo(account, password);
        String vertifyCode = mEtVcerificationCode.getText().toString().trim();
        String deviceId = mEtDeviceCode.getText().toString().trim();
        String region = mTvLocalCode.getText().toString().trim();

        IhyRequest.registerApp(userInfo, vertifyCode, deviceId, region, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(RegisterActivity.this, var3);
                String s2 = String.valueOf(var1);
                try {
                    JSONObject jsonObject = new JSONObject(s2);
                    String jsessionid = jsonObject.getString("JSESSIONID");
                    if (StringUtils.isNullOrEmpty(jsessionid)) {
                        jsessionid = "";
                    }
                    Constants.JSESSIONID = jsessionid;
                    Volley.me.addInitRequestHead("Cookie", "JSESSIONID=" + jsessionid);
                    //注册成功之后跳转至登录页面
                    jumpToPersonMsg(account);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                String s = HandlerErrorUtils.handlerErrorMsg(RegisterActivity.this, var2, var3);
                ToastUtils.showToastDefault(RegisterActivity.this, s);
            }
        });
    }

    /**
     * 跳转至个人信息页面
     */
    private void jumpToPersonMsg(String account) {
        Intent intent = new Intent(this, PersonalInformationActivity.class);
        intent.putExtra("TYPE", 1);
        intent.putExtra("ACCOUNT", account);
        LogOut.d("llw001", "account:" + account + ",type:1");
        startActivity(intent);
        finish();
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

        if (account.length() < 11) {
            ToastUtils.showToastDefault(RegisterActivity.this, getString(R.string.tv_toast_phone_error));
            return false;
        }

        String passWord = mEtPassWord.getText().toString().trim();
        if (StringUtils.isNullOrEmpty(passWord)) {
            ToastUtils.showToastDefault(this, mEtPassWord.getHint().toString());
            return false;
        }
        if (!StringUtils.vertifyPassWord(passWord)) {
            ToastUtils.showToastDefault(getString(R.string.pwd_error_tip));
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
        boolean checked = mCbProtocol.isChecked();
        if (!checked) {
            ToastUtils.showToastDefault(this, getString(R.string.tv_toast_agree));
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
        LogOut.d("llw", "正在申请权限");
        PermissionGen.with(this)
                .addRequestCode(REQUEST_CAMERA)
                .permissions(Manifest.permission.CAMERA)
                .request();
    }

    private void jumpToScan() {
        LogOut.d("llw", "准备调用相机");
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
//                mEtDeviceCode.setSelection(deviceSN.length());
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = REQUEST_CAMERA)
    public void doSomething() {
        //成功之后的处理
        //同意相机权限
        LogOut.d("llw", "申请成功");
        jumpToScan();
    }

    @PermissionFail(requestCode = REQUEST_CAMERA)
    public void requestPhotoFail() {
        //失败之后的处理，我一般是跳到设置界面
        LogOut.d("llw", "申请失败");
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
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

    /**
     * 倒计时
     */
    class TimerCountDown extends CountDownTimer {
        public TimerCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            String tip = String.valueOf(l / 1000) + getString(R.string.tv_text_second);
            mBtnVcerificationCode.setText(tip);
        }

        @Override
        public void onFinish() {
            mBtnVcerificationCode.setEnabled(true);
            mBtnVcerificationCode.setText(getString(R.string.tv_get_vertify_code));
        }
    }

    /**
     * 获取手机验证码
     *
     * @param phoneNo
     */
    private void getVerifyCodeByNet(String phoneNo, String region) {
        Volley.me.removeInitRequestHead("Cookie");
        IhyRequest.getVerifyCode(phoneNo, region, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                ToastUtils.showToastDefault(var3);
                String s2 = String.valueOf(var1);
                try {
                    JSONObject jsonObject = new JSONObject(s2);
                    String jsessionid = jsonObject.getString("JSESSIONID");
                    if (StringUtils.isNullOrEmpty(jsessionid)) {
                        jsessionid = "";
                    }

                    Volley.me.addInitRequestHead("Cookie", "JSESSIONID=" + jsessionid);
                    LogOut.d("llw", "注册页面获取手机验证码jsessionid:" + jsessionid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mVertifySuccess = true;
                mIvCodeLoading.clearAnimation();
                mBtnVcerificationCode.setVisibility(View.VISIBLE);
                mIvCodeLoading.setVisibility(View.GONE);
                mBtnVcerificationCode.setEnabled(false);
                if (mTimerCountDown != null) {
                    mTimerCountDown.cancel();
                    mTimerCountDown.start();
                }
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                String s = HandlerErrorUtils.handlerErrorMsg(RegisterActivity.this, var2, var3);
                ToastUtils.showToastDefault(s);
                mBtnVcerificationCode.setEnabled(true);
                mIvCodeLoading.clearAnimation();
                mBtnVcerificationCode.setVisibility(View.VISIBLE);
                mIvCodeLoading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String account = mEtCount.getText().toString().trim();
        mImgLoginAccountClear.setVisibility(account.length() > 0 ? View.VISIBLE : View.INVISIBLE);
        if (account.length() == 11) {
            String region = mTvLocalCode.getText().toString().trim();
            vertifyPhoneNum(account, region);
        }
    }

    private void vertifyPhoneNum(final String account, String region) {
        BaseDialogHelper.showLoadingDialog(this, true, getString(R.string.tv_verifying));
        IhyRequest.VerifyPhoneNumber(account, region, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                if (!StringUtils.isNullOrEmpty(var3)) {
//                    BaseDialogHelper.showMsgTipDialog(RegisterActivity.this, var3);
                    BaseDialogHelper.showSimpleDialog(RegisterActivity.this, getString(R.string.tip_msg), account + " " + var3);
                    mEtCount.setText("");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimerCountDown != null) {
            mTimerCountDown.cancel();
        }
    }

    /**
     * 获取国家区号
     */
    private void getCountryCodeByNet() {
        BaseDialogHelper.showLoadingDialog(this, true, getString(R.string.onloading));
        IhyRequest.getCountryCode(new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                CountryCodeVO codeVO = (CountryCodeVO) var1;
                if (codeVO != null) {
                    List<String> result = codeVO.getResult();
                    if (result != null && result.size() > 0)
                        showCountryCodeDialog(result);
                }
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
            }
        });
    }

    /**
     * @param arrayList
     */
    private void showCountryCodeDialog(List<String> arrayList) {
        BaseDialogHelper.showListDialog(this, "", getString(R.string.tv_btn_back), arrayList, new DialogListener() {
            @Override
            public void onClick(BaseType baseType) {

            }

            @Override
            public void onItemClick(long postion, String s) {
                mTvLocalCode.setText(s);
            }
        });
    }

}
