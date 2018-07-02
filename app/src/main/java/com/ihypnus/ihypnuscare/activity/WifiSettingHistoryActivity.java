package com.ihypnus.ihypnuscare.activity;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description:
 * @date: 2018/7/1 23:13
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class WifiSettingHistoryActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private TextView mTvSsid;
    private TextView mTvPwd;
    private CheckBox mCbVisible;
    private Button mBtnConfirm;
    private LinearLayout mLayoutSsid;
    private LinearLayout mLayoutPwd;

    @Override
    protected int setView() {
        return R.layout.activity_wifi_setting_history;
    }

    @Override
    protected void findViews() {
        mLayoutSsid = (LinearLayout) findViewById(R.id.layout_ssid);
        mLayoutPwd = (LinearLayout) findViewById(R.id.layout_pwd);
        mTvSsid = (TextView) findViewById(R.id.tv_ssid);
        mTvPwd = (TextView) findViewById(R.id.tv_pwd);
        mCbVisible = (CheckBox) findViewById(R.id.cb_visible);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("配置WiFi");
    }

    @Override
    protected void initEvent() {
        mCbVisible.setOnCheckedChangeListener(this);
        mLayoutSsid.setOnClickListener(this);
        mLayoutPwd.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String passWord = mTvPwd.getText().toString();
        if (isChecked) {
            //设置EditText的密码为可见的
            mTvPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //设置密码为隐藏的
            mTvPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
//        mTvPwd.setSelection(passWord.length());
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick())return;
        switch (v.getId()){
            case R.id.layout_ssid:

                break;

            case R.id.layout_pwd:

                break;
            case R.id.btn_confirm:

                break;

        }
    }
}
