package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.ihypnus.ihypnuscare.IhyApplication;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.eventbusfactory.BaseFactory;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 修改密码
 * @date: 2018/7/16 14:18
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ChanedPassWordActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private EditText mEtOldPassword;
    private CheckBox mCbOldPw1;
    private EditText mNewPassword;
    private CheckBox mCbPw1;
    private EditText mEtNewPWD2;
    private CheckBox mCbPw2;
    private Button mBtnConfirm;

    @Override
    protected int setView() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void findViews() {

        mEtOldPassword = (EditText) findViewById(R.id.et_old_password);
        mCbOldPw1 = (CheckBox) findViewById(R.id.cb_old_pw1);

        mNewPassword = (EditText) findViewById(R.id.new_password);
        mCbPw1 = (CheckBox) findViewById(R.id.cb_pw1);

        mEtNewPWD2 = (EditText) findViewById(R.id.et_pw2);
        mCbPw2 = (CheckBox) findViewById(R.id.cb_pw2);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("修改密码");
    }

    @Override
    protected void initEvent() {
        mCbOldPw1.setOnCheckedChangeListener(this);
        mCbPw1.setOnCheckedChangeListener(this);
        mCbPw2.setOnCheckedChangeListener(this);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyPwd();
            }
        });
    }

    private void modifyPwd() {

        String oldPwd = mEtOldPassword.getText().toString().trim();
        String newPwd1 = mNewPassword.getText().toString().trim();
        String newPwd2 = mEtNewPWD2.getText().toString().trim();
        if (StringUtils.isNullOrEmpty(oldPwd)) {
            ToastUtils.showToastDefault(mEtOldPassword.getHint().toString());
            return;
        }

        if (StringUtils.isNullOrEmpty(newPwd1)) {
            ToastUtils.showToastDefault(mNewPassword.getHint().toString());
            return;
        }
        if (!StringUtils.vertifyPassWord(newPwd1)) {
            ToastUtils.showToastDefault("请输入6-14位的密码,支持数字与大小写字母");
            mNewPassword.setText("");
            return;
        }

        if (StringUtils.isNullOrEmpty(newPwd2)) {
            ToastUtils.showToastDefault(mEtNewPWD2.getHint().toString());
            return;
        }

        if (!StringUtils.vertifyPassWord(newPwd2)) {
            ToastUtils.showToastDefault("请输入6-14位的密码,支持数字与大小写字母");
            mEtNewPWD2.setText("");
            return;
        }

        if (!newPwd1.equals(newPwd2)) {
            ToastUtils.showToastDefault("两次输入的密码不一致,请重新输入");
            return;
        }
        BaseDialogHelper.showLoadingDialog(this, true, "正在提交...");

        IhyRequest.resetPassword(Constants.JSESSIONID, true, oldPwd, newPwd1, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                EventBus.getDefault().post(new BaseFactory.CloseAllEvent());
                IhyApplication.mInstance.setUser(null);
                jumpToActivity(LoginActivity.class);

            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }
        });

    }

    private void jumpToActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch (id) {
            case R.id.cb_old_pw1:
                setPasswordVisible(isChecked, mEtOldPassword);
                break;

            case R.id.cb_pw1:
                setPasswordVisible(isChecked, mNewPassword);
                break;

            case R.id.cb_pw2:
                setPasswordVisible(isChecked, mEtNewPWD2);
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
}
