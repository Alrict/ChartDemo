package com.ihypnus.ihypnuscare.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 注册
 * @date: 2018/5/14 13:45
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
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
    }

    @Override
    protected void initEvent() {
        mBtnVcerificationCode.setOnClickListener(this);

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
        }
    }
}
