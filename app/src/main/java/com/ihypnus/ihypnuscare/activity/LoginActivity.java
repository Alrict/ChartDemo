package com.ihypnus.ihypnuscare.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
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
import com.ihypnus.ihypnuscare.bean.LoginBean;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.iface.BaseType;
import com.ihypnus.ihypnuscare.iface.DialogListener;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.AndroidSystemHelper;
import com.ihypnus.ihypnuscare.utils.HandlerErrorUtils;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.SP;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 登录页面
 * @date: 2018/5/14 13:44
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private Context context;           //上下文
    private Button mLoginBtn;          //登录按钮
    private EditText mEtCount;       //手机号输入框
    private EditText mEtPassWord;         //密码输入框
    private ImageView img_clear;                    //帐号清除

    private CountDownTimer mCountDownTimerYCode;   // 右边倒计时
    private boolean isAutoLogin = false;           //是否自动登录
    private String lastAccount = "";                 //上一次
    private Animation mCodeLoadingAnim;     // 验证码请求动画
    private String mCode;                           //验证码
    private String mPhoneNumber;                   //手机号
    private final static int REQUEST_CODE = 300;
    private TextView mTvRegist;
    private TextView mTvFogotPassWord;
    private TextView mTvLocalCode;
    private ImageView mIvDownArrow;
    private CheckBox mCbVisible;
    private SP mSP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
        }
    }

    @Override
    protected int setView() {
        return R.layout.activity_login;
    }

    @Override
    protected void findViews() {

        //帐号 选择地区
        mTvLocalCode = (TextView) this.findViewById(R.id.tv_local_code);
        mIvDownArrow = (ImageView) this.findViewById(R.id.iv_down_arrow);
        //帐号输入
        mEtCount = (EditText) this.findViewById(R.id.id_phone_edit2);
        //清除帐号
        img_clear = (ImageView) this.findViewById(R.id.img_login_account_clear2);


        //密码
        mEtPassWord = (EditText) this.findViewById(R.id.edit_login_code2);
        //密码是否可见
        mCbVisible = (CheckBox) this.findViewById(R.id.cb_visible);


        //登入
        mLoginBtn = (Button) this.findViewById(R.id.id_login);


        //注册
        mTvRegist = (TextView) findViewById(R.id.tv_regist);
        mTvRegist.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mTvRegist.getPaint().setAntiAlias(true);//抗锯齿
        //忘记密码
        mTvFogotPassWord = (TextView) findViewById(R.id.tv_fogot_pass_word);
        mTvFogotPassWord.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mTvFogotPassWord.getPaint().setAntiAlias(true);//抗锯齿
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        getSupportedActionBar().setVisibility(View.GONE);
        mSP = SP.getSP(Constants.LOGIN_ACCOUNT_PASSWORD);
        String login_account = mSP.getString(Constants.LOGIN_ACCOUNT);
        String login_password = mSP.getString(Constants.LOGIN_PASSWORD);
        if (!StringUtils.isNullOrEmpty(login_account)) {
            mEtCount.setText(login_account);
            mEtCount.setSelection(login_account.length());
        }
        if (!StringUtils.isNullOrEmpty(login_password)) {
            mEtPassWord.setText(login_password);
            mEtPassWord.setSelection(login_password.length());
        }

    }

    @Override
    protected void initEvent() {
        mTvLocalCode.setOnClickListener(this);
        mIvDownArrow.setOnClickListener(this);

        mLoginBtn.setOnClickListener(this);
        img_clear.setOnClickListener(this);

        //输入的手机号码
        mEtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1) {
                    mEtPassWord.setText("");
                    img_clear.setVisibility(View.GONE);
                    AndroidSystemHelper.ShowKeyboard(mEtCount);
                    if (mCountDownTimerYCode != null) {
                        mCountDownTimerYCode.cancel();
                        mCountDownTimerYCode.onFinish();
                    }
                } else {
                    if (img_clear.getVisibility() == View.GONE) {
                        img_clear.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        mEtCount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mPhoneNumber = mEtCount.getText()
                        .toString();
                if (hasFocus && !TextUtils.isEmpty(mPhoneNumber)) {
                    img_clear.setVisibility(View.VISIBLE);
                } else {
                    img_clear.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(mPhoneNumber)) {
                        //                        checkAccount(mPhoneNumber);
                    }
                }
            }
        });
        mCbVisible.setOnCheckedChangeListener(this);

        mTvRegist.setOnClickListener(this);
        mTvFogotPassWord.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

        mLoginBtn.setEnabled(true);
        mCodeLoadingAnim = AnimationUtils.loadAnimation(this, R.anim.login_code_loading);
        LinearInterpolator lin = new LinearInterpolator();
        mCodeLoadingAnim.setInterpolator(lin);

        // 权限申请 读写外部存储
        PermissionGen.with(this)
                .addRequestCode(REQUEST_CODE)
                .permissions(Manifest.permission.READ_SMS)
                .request();

    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.tv_local_code:
            case R.id.iv_down_arrow:
                //选择区号
                getCountryCodeByNet();

                break;
            //清除帐号
            case R.id.img_login_account_clear2:
                mEtCount.setText("");
                mEtPassWord.setText("");
                break;

            //登录
            case R.id.id_login:

                if (!ViewUtils.isFastDoubleClick()) {
                    if (checkInput()) {
                        jumpToHomeActiity();
                    }
                }
                break;
            //注册
            case R.id.tv_regist:
                if (!ViewUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivityForResult(intent, 122);
                }
                break;

            //忘记密码
            case R.id.tv_fogot_pass_word:
                if (!ViewUtils.isFastDoubleClick()) {
                    Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                    startActivityForResult(intent, 121);
                }
                break;

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

    /**
     * 检查输入的帐号密码信息
     */
    private boolean checkInput() {
        String count = mEtCount.getText().toString().trim();
        String passWord = mEtPassWord.getText().toString().trim();
        if (StringUtils.isNullOrEmpty(count)) {
            ToastUtils.showToastDefault(this, mEtCount.getHint().toString());
            return false;
        }

        if (StringUtils.isNullOrEmpty(passWord)) {
            ToastUtils.showToastDefault(this, mEtPassWord.getHint().toString());
            return false;
        }

        return true;
    }

    private void jumpToHomeActiity() {
        BaseDialogHelper.showLoadingDialog(this, false, getString(R.string.tv_loading_login));
        final String countNum = mEtCount.getText().toString().trim();
        String passWord = mEtPassWord.getText().toString().trim();
        IhyApplication.mInstance.setUser(null);
        IhyRequest.login(countNum, passWord, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                LoginBean loginBean = (LoginBean) var1;
                IhyApplication.mInstance.setUser(loginBean);
                if (loginBean != null && loginBean.getJSESSIONID() != null) {
                    String jsessionid = loginBean.getJSESSIONID();
                    jumpToHome(jsessionid);
                    //当用户使用自有账号登录时，可以这样统计：
                    MobclickAgent.onProfileSignIn(countNum);

                    LogOut.d("llw", jsessionid);
                    finish();
                }
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                String s = HandlerErrorUtils.handlerErrorMsg(LoginActivity.this, var2, var3);
                ToastUtils.showToastDefault(LoginActivity.this, s);
//                jumpToHome();
            }
        });

    }

    private void jumpToHome(String jessionid) {
        String account = mEtCount.getText().toString().trim();
        String password = mEtPassWord.getText().toString().trim();
        mSP.putString(Constants.LOGIN_ACCOUNT, account);
        mSP.putString(Constants.LOGIN_PASSWORD, password);
        mSP.putString(account + "_jessionid", jessionid);
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    /**
     * 登入
     *
     * @param s
     */
    private void doLogin(String s) {

    }

    /**
     * 检测账号是否已经输入
     *
     * @return
     */
    public String checkRequire2() {
        String error = "";
        mPhoneNumber = mEtPassWord.getText()
                .toString()
                .trim();
        mCode = mEtPassWord.getText()
                .toString()
                .trim();
        if (StringUtils.isNullOrEmpty(mPhoneNumber)) {
            error = getResources().getString(R.string.login_account_hint);
            return error;
        }

        return error;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 121 && resultCode == RESULT_OK) {
            //找回密码

        } else if (requestCode == 122 && resultCode == RESULT_OK) {
            //注册
        }
    }

    //密码是否可见
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
