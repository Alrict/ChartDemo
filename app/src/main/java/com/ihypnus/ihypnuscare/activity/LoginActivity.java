package com.ihypnus.ihypnuscare.activity;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.CarSanitationListBean;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.AndroidSystemHelper;
import com.ihypnus.ihypnuscare.utils.CountdownManager;
import com.ihypnus.ihypnuscare.utils.LogOut;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 登录页面
 * @date: 2018/5/14 13:44
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private Context context;           //上下文
    private Button mLoginBtn;          //登录按钮
    private Button mYCodeBtn;            //获取验证码按钮
    private EditText mPhoneEdit;       //手机号输入框
    private EditText mCodeEdit;         //验证码输入框
    private ImageView img_clear;                    //帐号清除
    private ImageView img_tips;                     //帐号提示
    private LinearLayout layout_account;           //帐号内容整体布局
    //自动登录
    private RelativeLayout layout_choice_autoLogin;
    private CheckBox cb_autoLogin;
    //动态码
    private RelativeLayout layout_imgcode;           //动态码布局
    private ImageView mImgCodeIcon;                 //图形动态码
    private Bitmap mCodeBitmap;                     //动态码图片
    private ImageView mCodeLoading;           // 验证码请求动画imgview
    private CountDownTimer mCountDownTimerYCode;   // 右边倒计时
    private String isUpdateMacValues;              //登录类型，1永久登录，2一次性登录

    private boolean isAutoLogin = false;           //是否自动登录
    private String lastAccount = "";                 //上一次

    String requestApiLogin;                 //登陆请求
    private String registerErrorCode = "7777";  //账号需要注册的错误码
    private Animation mCodeLoadingAnim;     // 验证码请求动画
    private String mCode;                           //验证码
    private String mPhoneNumber;                   //手机号
    private final static int REQUEST_CODE = 300;

    @Override
    protected int setView() {
        return R.layout.activity_login;
    }

    @Override
    protected void findViews() {
        mLoginBtn = (Button) this.findViewById(R.id.id_login);

        //验证码登录布局控件
        mYCodeBtn = (Button) this.findViewById(R.id.btn_login_get_code2);
        mPhoneEdit = (EditText) this.findViewById(R.id.id_phone_edit2);
        mCodeEdit = (EditText) this.findViewById(R.id.edit_login_code2);
        img_clear = (ImageView) this.findViewById(R.id.img_login_account_clear2);
        img_tips = (ImageView) this.findViewById(R.id.img_login_account_tips2);
        layout_account = (LinearLayout) this.findViewById(R.id.liner_account_edit2);
        //自动登录
        layout_choice_autoLogin = (RelativeLayout) findViewById(R.id.layout_auto_login);
        cb_autoLogin = (CheckBox) findViewById(R.id.cb_auto_login);

        mCodeLoading = (ImageView) findViewById(R.id.img_code_loading);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

        mLoginBtn.setOnClickListener(this);
        mYCodeBtn.setOnClickListener(this);
        img_clear.setOnClickListener(this);
        img_tips.setOnClickListener(this);

        mPhoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 1) {
                    mCodeEdit.setText("");
                    cb_autoLogin.setChecked(false);
                    img_clear.setVisibility(View.GONE);
                    AndroidSystemHelper.ShowKeyboard(mPhoneEdit);
                    if (mCountDownTimerYCode != null) {
                        mCountDownTimerYCode.cancel();
                        mCountDownTimerYCode.onFinish();
                    }
                    lastAccount = "";
                    clearCodeLoaidng();
                } else {
                    if (img_clear.getVisibility() == View.GONE) {
                        img_clear.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        mPhoneEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean fouse) {
                mPhoneNumber = mPhoneEdit.getText()
                        .toString();
                if (fouse && !TextUtils.isEmpty(mPhoneNumber)) {
                    img_clear.setVisibility(View.VISIBLE);
                } else {
                    img_clear.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(mPhoneNumber)) {
                        //                        checkAccount(mPhoneNumber);
                    }
                }
            }
        });
        layout_choice_autoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_autoLogin.isChecked()) {
                    cb_autoLogin.setChecked(false);
                } else {
                    cb_autoLogin.setChecked(true);
                }
            }
        });
        cb_autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isAutoLogin = b;
            }
        });
    }

    @Override
    protected void loadData() {

        mLoginBtn.setEnabled(true);
        //        accountList = PrefUtils.getLoginAccountHistory2();
        //        //获取本地记录的历史帐号，并显示最后登录的帐号
        //        if (accountList != null && accountList.size() > 0) {
        //            img_tips.setVisibility(View.VISIBLE);
        //            String account = DeEncodeUtils.decode(accountList.get(0).getLogin_Account());
        //            if (!TextUtils.isEmpty(StringUtils.getNewAccount(account))) {
        //                account = StringUtils.getNewAccount(account);
        //            }
        //            mPhoneEdit.setText(account);
        //            lastAccount = account;
        //            mCodeEdit.setText(accountList.get(0).getLogin_Code());
        //            if (accountList.get(0).isAutoLogin()) {
        //                cb_autoLogin.setChecked(true);
        //                isAutoLogin = true;
        //            }
        //        } else {
        //            img_tips.setVisibility(View.GONE);
        //        }
        mCodeLoadingAnim = AnimationUtils.loadAnimation(this, R.anim.login_code_loading);
        LinearInterpolator lin = new LinearInterpolator();
        mCodeLoadingAnim.setInterpolator(lin);

        // 权限申请 读写外部存储
        PermissionGen.with(this)
                .addRequestCode(REQUEST_CODE)
                .permissions(Manifest.permission.READ_SMS)
                .request();
        // TODO: 2017/6/6 0006
        CountdownManager.getInstance()
                .clearTimer();
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            //清除帐号
            case R.id.img_login_account_clear2:
                mPhoneEdit.setText("");
                mCodeEdit.setText("");
                if (isAutoLogin) {
                    cb_autoLogin.setChecked(false);
                }
                break;
            // 验证码登录时获取历史帐号
            case R.id.img_login_account_tips2:


                break;
            //  验证码登录获取验证码
            case R.id.btn_login_get_code2:
                if (!ViewUtils.isFastDoubleClick()) {
                    String error = checkRequire2();
                    if (error.equals("")) {
                        mCodeLoading.setVisibility(View.VISIBLE);
                        mCodeLoading.startAnimation(mCodeLoadingAnim);
                        mYCodeBtn.setText("正在获取");
                        AndroidSystemHelper.HideKeyboard(LoginActivity.this);
                        getUserConfirm2();
                    } else {
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                break;
            //登录
            case R.id.id_login:
                if (!ViewUtils.isFastDoubleClick()) {
                    IhyRequest.getUserConfirm2(new Response.Listener<Object>() {
                        @Override
                        public void onResponse(Object response) {
                            CarSanitationListBean bean = (CarSanitationListBean) response;
                            ToastUtils.showToastDefault(LoginActivity.this, bean.getErrMsg());
                            if (bean != null && bean.getResult() != null && bean.getResult().getDatas() != null) {
                                LogOut.d("llw", bean.getResult().getDatas().toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ToastUtils.showToastDefault(LoginActivity.this, error.toString());
                        }
                    });
                  /*  UserInfo userInfo = new UserInfo();
                    userInfo.setPhone("18820000743");
                    userInfo.setPassword("888888");
                    String userInfoJson = new Gson().toJson(userInfo);
                    IhyRequest.getUserRegister(userInfoJson, "CP70100511S", new Response.Listener<Object>() {
                        @Override
                        public void onResponse(Object response) {
//                            ToastUtils.showToastDefault(LoginActivity.this, response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ToastUtils.showToastDefault(LoginActivity.this, error.toString());
                        }
                    });*/
                }
                break;

        }
    }

    /**
     * 登入
     *
     * @param s
     */
    private void doLogin(String s) {

    }


    /**
     * 清除验证码加载动画
     */
    private void clearCodeLoaidng() {
        mCodeLoading.setVisibility(View.GONE);
        mCodeLoading.clearAnimation();
        mYCodeBtn.setEnabled(true);
    }

    public void getUserConfirm2() {
        mYCodeBtn.setEnabled(false);
        // TODO: 2018/5/15  调登入接口
    }

    /**
     * 检测账号是否已经输入
     *
     * @return
     */
    public String checkRequire2() {
        String error = "";
        mPhoneNumber = mPhoneEdit.getText()
                .toString()
                .trim();
        mCode = mCodeEdit.getText()
                .toString()
                .trim();
        if (StringUtils.isNullOrEmpty(mPhoneNumber)) {
            error = getResources().getString(R.string.login_account_hint);
            return error;
        }

        return error;
    }


}
