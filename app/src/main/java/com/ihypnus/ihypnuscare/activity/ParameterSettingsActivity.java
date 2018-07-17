package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.ShadowDeviceBean;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 参数设置页面
 * @date: 2018/7/16 17:39
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class ParameterSettingsActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTvTitle01;
    private TextView mTvValue01;
    private TextView mTvTitle02;
    private TextView mTvValue02;
    private TextView mTvTitle03;
    private TextView mTvValue03;
    private TextView mTvTitle04;
    private TextView mTvValue04;
    private TextView mTvTitle05;
    private TextView mTvValue05;
    private TextView mTvTitle06;
    private TextView mTvValue06;
    private TextView mTvTitle07;
    private TextView mTvValue07;
    private TextView mTvTitle08;
    private TextView mTvValue08;
    private LinearLayout mLayoutTriggerSensitivity;
    private TextView mTvTitle09;
    private TextView mTvValue09;
    private TextView mTvTitle10;
    private TextView mTvValue10;
    private LinearLayout mLayoutBreathSensitivity;
    private TextView mTvTitle11;
    private TextView mTvValue11;
    private Button mBtnCancel;
    private Button mBtnConfirm;
    private static final String[] sCurrentModel = {"CPAP", "APAP", "S", "Auto-S", "T", "ST"};

    @Override
    protected int setView() {
        return R.layout.activity_parameter_settings;
    }

    @Override
    protected void findViews() {
        mTvTitle01 = (TextView) findViewById(R.id.tv_title01);
        mTvValue01 = (TextView) findViewById(R.id.tv_value01);
        mTvTitle02 = (TextView) findViewById(R.id.tv_title02);
        mTvValue02 = (TextView) findViewById(R.id.tv_value02);
        mTvTitle03 = (TextView) findViewById(R.id.tv_title03);
        mTvValue03 = (TextView) findViewById(R.id.tv_value03);
        mTvTitle04 = (TextView) findViewById(R.id.tv_title04);
        mTvValue04 = (TextView) findViewById(R.id.tv_value04);
        mTvTitle05 = (TextView) findViewById(R.id.tv_title05);
        mTvValue05 = (TextView) findViewById(R.id.tv_value05);
        mTvTitle06 = (TextView) findViewById(R.id.tv_title06);
        mTvValue06 = (TextView) findViewById(R.id.tv_value06);
        mTvTitle07 = (TextView) findViewById(R.id.tv_title07);
        mTvValue07 = (TextView) findViewById(R.id.tv_value07);
        mTvTitle08 = (TextView) findViewById(R.id.tv_title08);
        mTvValue08 = (TextView) findViewById(R.id.tv_value08);
        mLayoutTriggerSensitivity = (LinearLayout) findViewById(R.id.layout_trigger_sensitivity);
        mTvTitle09 = (TextView) findViewById(R.id.tv_title09);
        mTvValue09 = (TextView) findViewById(R.id.tv_value09);
        mTvTitle10 = (TextView) findViewById(R.id.tv_title10);
        mTvValue10 = (TextView) findViewById(R.id.tv_value10);
        mLayoutBreathSensitivity = (LinearLayout) findViewById(R.id.layout_breath_sensitivity);
        mTvTitle11 = (TextView) findViewById(R.id.tv_title11);
        mTvValue11 = (TextView) findViewById(R.id.tv_value11);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String deviceId = intent.getStringExtra("DEVICE_ID");
        setTitle(deviceId);
        ShadowDeviceBean deviceBean = intent.getParcelableExtra("DEVICE_BEAN");
        if (deviceBean == null) {
            getParameterInfos(deviceId);
        } else {
            bindView(deviceBean);
        }
    }

    @Override
    protected void initEvent() {
        mBtnCancel.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    private void getParameterInfos(String deviceId) {
        BaseDialogHelper.showLoadingDialog(this, true, "正在加载...");
        IhyRequest.getShadowDevice(Constants.JSESSIONID, true, deviceId, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ShadowDeviceBean deviceBean = (ShadowDeviceBean) var1;
                bindView(deviceBean);
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }
        });
    }

    private void bindView(ShadowDeviceBean deviceBean) {
        int cureMode = deviceBean.getCure_mode();
        mTvTitle01.setText("当前模式");
        mTvValue01.setText(sCurrentModel[cureMode]);
        switch (cureMode) {
            case 0:
                mTvTitle02.setText("治疗压力");
                mTvValue02.setText(deviceBean.getCpap_p() + "");

                mTvTitle05.setText("起始压力");
                mTvValue05.setText(deviceBean.getStart_pressure() + "");

                mTvTitle06.setText("延迟时间");
                mTvValue06.setText(deviceBean.getCure_delay() + "");

                mTvTitle07.setText("呼气释压");
                mTvValue07.setText(deviceBean.getDep_type() + "");

                mTvTitle08.setText("释压水平");
                mTvValue08.setText(deviceBean.getDep_level() + "");
                break;

            case 1:
                mTvTitle02.setText("最小压力");
                mTvValue02.setText(deviceBean.getApap_min_p() + "");

                mTvTitle04.setText("最大压力");
                mTvValue04.setText(deviceBean.getApap_max_p() + "");

                mTvTitle05.setText("起始压力");
                mTvValue05.setText(deviceBean.getStart_pressure() + "");

                mTvTitle06.setText("延迟时间");
                mTvValue06.setText(deviceBean.getCure_delay() + "");

                mTvTitle07.setText("呼气释压");
                mTvValue07.setText(deviceBean.getDep_type() + "");

                mTvTitle08.setText("释压水平");
                mTvValue08.setText(deviceBean.getDep_level() + "");
                break;

            case 2:
                mTvTitle02.setText("吸气压力");
                mTvValue02.setText(deviceBean.getBpap_in_p() + "");

                mTvTitle04.setText("呼气压力");
                mTvValue04.setText(deviceBean.getBpap_ex_p() + "");

                mTvTitle05.setText("起始压力");
                mTvValue05.setText(deviceBean.getStart_pressure() + "");

                mTvTitle06.setText("延迟时间");
                mTvValue06.setText(deviceBean.getCure_delay() + "");

                mTvTitle07.setText("呼气释压");
                mTvValue07.setText(deviceBean.getDep_type() + "");

                mTvTitle08.setText("释压水平");
                mTvValue08.setText(deviceBean.getDep_level() + "");
                break;

            case 3:

                break;


            case 4:

                break;

            case 5:

                break;
        }
        mTvTitle02.setText("治疗压力");
        mTvValue02.setText(deviceBean.getFlight_mode() + "");

//        mTvTitle02.setText("最小压力");
//        mTvValue02.setText(deviceBean.getFlight_mode() + "");
//
//        mTvTitle02.setText("吸气压力");
//        mTvValue02.setText(deviceBean.getFlight_mode() + "");

//        mTvTitle03.setText("当前模式");
//        mTvValue03.setText(deviceBean.getFlight_mode() + "");

        mTvTitle04.setText("最大压力");
        mTvValue04.setText(deviceBean.getFlight_mode() + "");


//        mTvTitle04.setText("呼气压力");
//        mTvValue04.setText(deviceBean.getFlight_mode() + "");

        mTvTitle05.setText("起始压力");
        mTvValue05.setText(deviceBean.getFlight_mode() + "");

        mTvTitle06.setText("延迟时间");
        mTvValue06.setText(deviceBean.getFlight_mode() + "");

        mTvTitle07.setText("呼气释压");
        mTvValue07.setText(deviceBean.getFlight_mode() + "");

//        mTvTitle07.setText("呼气舒适度");
//        mTvValue07.setText(deviceBean.getFlight_mode() + "");

        mTvTitle08.setText("释压水平");
        mTvValue08.setText(deviceBean.getFlight_mode() + "");

//        mTvTitle08.setText("升压速度");
//        mTvValue08.setText(deviceBean.getFlight_mode() + "");

        mTvTitle09.setText("触发灵敏度");
        mTvValue09.setText(deviceBean.getFlight_mode() + "");

        mTvTitle10.setText("降压速度");
        mTvValue10.setText(deviceBean.getFlight_mode() + "");

        mTvTitle10.setText("呼气灵敏度");
        mTvValue10.setText(deviceBean.getFlight_mode() + "");
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (v.getId()) {
            case R.id.btn_cancel:
                finish();
                break;

            case R.id.btn_confirm:


                break;
        }
    }
}
