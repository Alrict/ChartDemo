package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
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
import com.ihypnus.ihypnuscare.IhyApplication;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.CountryCodeVO;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.iface.BaseType;
import com.ihypnus.ihypnuscare.iface.DialogListener;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 忘记密码
 * @date: 2018/6/9 16:12
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener {

    private TextView mTvLocalCode;
    private ImageView mIvDownArrow;
    private EditText mEtCount;
    private ImageView mIvCleanAccount;
    private EditText mEtVcerificationCode;
    private Button mBtnVerificationCode;
    private EditText mEtNewPassword;
    private CheckBox mCbPw1;
    private EditText mEtNewPassword2;
    private CheckBox mCbPw2;
    private Button mBtnConfirm;
    private ImageView mIvCodeLoading;
    private Animation mCodeLoadingAnim;
    private TimerCountDown mTimerCountDown = new TimerCountDown(120 * 1000 + 500, 1000);

    @Override
    protected int setView() {
        return R.layout.activity_forget_pass;
    }

    @Override
    protected void findViews() {
        //区号
        mTvLocalCode = (TextView) findViewById(R.id.tv_local_code);
        //区号下拉箭头
        mIvDownArrow = (ImageView) findViewById(R.id.iv_down_arrow);
        //注册的手机号
        mEtCount = (EditText) findViewById(R.id.et_count);
        //清除手机号
        mIvCleanAccount = (ImageView) findViewById(R.id.img_login_account_clear2);

        //验证码
        mEtVcerificationCode = (EditText) findViewById(R.id.et_vcerification_code);
        //获取验证码动画图片
        mIvCodeLoading = (ImageView) findViewById(R.id.iv_code_loading);
        //获取验证码按钮
        mBtnVerificationCode = (Button) findViewById(R.id.btn_vcerification_code);
        //输入新的密码
        mEtNewPassword = (EditText) findViewById(R.id.new_password);
        mCbPw1 = (CheckBox) findViewById(R.id.cb_pw1);
        mEtNewPassword2 = (EditText) findViewById(R.id.et_pw2);
        mCbPw2 = (CheckBox) findViewById(R.id.cb_pw2);
        //确定
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle(getString(R.string.tv_title_pwd_back));
        //获取验证码动画
        mCodeLoadingAnim = AnimationUtils.loadAnimation(this, R.anim.login_code_loading);
        LinearInterpolator lin = new LinearInterpolator();
        mCodeLoadingAnim.setInterpolator(lin);
    }

    @Override
    protected void initEvent() {
        mEtCount.addTextChangedListener(this);
        mTvLocalCode.setOnClickListener(this);
        mIvDownArrow.setOnClickListener(this);
        mIvCleanAccount.setOnClickListener(this);
        mBtnVerificationCode.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        mCbPw1.setOnCheckedChangeListener(this);
        mCbPw2.setOnCheckedChangeListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.img_login_account_clear2:
                //清空手机号
                mEtCount.setText("");
                break;

            case R.id.tv_local_code:
            case R.id.iv_down_arrow:
                //国家区号选择
                getCountryCodeByNet();
                break;

            case R.id.btn_vcerification_code:
                //获取验证码
                String phone = mEtCount.getText().toString().trim();
                if (phone.length() == 11) {
                    mBtnVerificationCode.setVisibility(View.GONE);
                    mIvCodeLoading.setVisibility(View.VISIBLE);
                    mIvCodeLoading.startAnimation(mCodeLoadingAnim);
                    String code = mTvLocalCode.getText().toString().trim();
                    getVerificationCodeByNet(phone, code);
                } else {
                    mEtCount.setText("");
                    ToastUtils.showToastDefault(mEtCount.getHint().toString());
                }
                break;

            case R.id.btn_confirm:
                submit();

                break;
        }
    }

    private void submit() {
        String phone = mEtCount.getText().toString().trim();
        if (phone.length() != 11) {
            mEtCount.setText("");
            ToastUtils.showToastDefault(mEtCount.getHint().toString());
            return;
        }

        String code = mEtVcerificationCode.getText().toString().trim();
        if (StringUtils.isNullOrEmpty(code)) {
            ToastUtils.showToastDefault(mEtVcerificationCode.getHint().toString());
            return;
        }

        String pw1 = mEtNewPassword.getText().toString().trim();
        if (StringUtils.isNullOrEmpty(pw1)) {
            ToastUtils.showToastDefault(mEtNewPassword.getHint().toString());
            return;
        }
        if (pw1.length() < 6) {
            mEtNewPassword.setText("");
            mEtNewPassword2.setText("");
            BaseDialogHelper.showMsgTipDialog(this, getString(R.string.tv_pwd_error_length));
            return;
        }

        String pw2 = mEtNewPassword2.getText().toString().trim();
        if (StringUtils.isNullOrEmpty(pw2)) {
            ToastUtils.showToastDefault(mEtNewPassword2.getHint().toString());
            return;
        }

        if (pw2.length() < 6) {
            mEtNewPassword.setText("");
            mEtNewPassword2.setText("");
            BaseDialogHelper.showMsgTipDialog(this, getString(R.string.tv_pwd_error_length));
            return;
        }

        if (!pw1.equals(pw2)) {
            mEtNewPassword.setText("");
            mEtNewPassword2.setText("");
            BaseDialogHelper.showMsgTipDialog(this, getString(R.string.tv_pwd_error_different));
            return;
        }
        BaseDialogHelper.showLoadingDialog(this, true, getString(R.string.onloading));
        String region = mTvLocalCode.getText().toString().trim();
        IhyRequest.getBackPassword(Constants.JSESSIONID, true, phone, code, pw2, region, new ResponseCallback() {

            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                jumpToActivity(LoginActivity.class);
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }
        });
    }

    private void jumpToActivity(final Class<?> cls) {
        BaseDialogHelper.showSimpleDialog(this, null, getString(R.string.tv_reset_pwd_succee), getString(R.string.ok), new DialogListener() {
            @Override
            public void onClick(BaseType baseType) {
                //登出
                MobclickAgent.onProfileSignOff();
                IhyApplication.mInstance.setUser(null);
                Intent intent = new Intent(ForgetPasswordActivity.this, cls);
                startActivity(intent);
            }

            @Override
            public void onItemClick(long postion, String s) {

            }
        });
    }

    /**
     * 获取验证码
     *
     * @param phone 手机号码
     */
    private void getVerificationCodeByNet(String phone, String region) {
        IhyRequest.getVerifyCode(phone, region, new ResponseCallback() {
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
                    Constants.JSESSIONID = jsessionid;
                    LogOut.d("llw", "获取手机验证码jsessionid:" + jsessionid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mBtnVerificationCode.setVisibility(View.VISIBLE);
                mIvCodeLoading.setVisibility(View.GONE);
                mIvCodeLoading.clearAnimation();
                mBtnVerificationCode.setEnabled(false);
                if (mTimerCountDown != null) {
                    mTimerCountDown.cancel();
                    mTimerCountDown.start();
                }
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                ToastUtils.showToastDefault(var3);
                mBtnVerificationCode.setEnabled(true);
                mBtnVerificationCode.setVisibility(View.VISIBLE);
                mIvCodeLoading.setVisibility(View.GONE);
                mIvCodeLoading.clearAnimation();
            }
        });
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
            mBtnVerificationCode.setText(tip);
        }

        @Override
        public void onFinish() {
            mBtnVerificationCode.setEnabled(true);
            mBtnVerificationCode.setText(R.string.tv_get_vertify_code);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String phone = mEtCount.getText().toString().trim();
        mIvCleanAccount.setVisibility(phone.length() > 0 ? View.VISIBLE : View.INVISIBLE);
        if (phone.length() == 11) {
            String region = mTvLocalCode.getText().toString().trim();
            vertifyPhoneNum(phone, region);
        }

    }

    /**
     *
     * @param account
     * @param region
     */
    private void vertifyPhoneNum(final String account, String region) {
        BaseDialogHelper.showLoadingDialog(this, true, getString(R.string.tv_verifying));
        IhyRequest.VerifyPhoneNumber(account, region, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                BaseDialogHelper.showSimpleDialog(ForgetPasswordActivity.this, getString(R.string.tip_msg), account + getString(R.string.tv_msg_unregister) );
                mEtCount.setText("");
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                if (!StringUtils.isNullOrEmpty(var3)) {
//                    BaseDialogHelper.showMsgTipDialog(RegisterActivity.this, var3);
                    BaseDialogHelper.showSimpleDialog(ForgetPasswordActivity.this, getString(R.string.tip_msg), account + " " + var3);
                    mEtCount.setText("");
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.cb_pw1:
                setPasswordVisible(isChecked, mEtNewPassword);
                break;

            case R.id.cb_pw2:
                setPasswordVisible(isChecked, mEtNewPassword2);
                break;
        }

    }


    private void setPasswordVisible(boolean isChecked, EditText editText) {
        String passWord = editText.getText().toString();
        if (isChecked) {
            //设置EditText的密码为可见的
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //设置密码为隐藏的
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        editText.setSelection(passWord.length());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimerCountDown != null) {
            mTimerCountDown.cancel();
        }
    }
}
