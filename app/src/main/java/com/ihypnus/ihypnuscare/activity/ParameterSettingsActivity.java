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
import com.ihypnus.ihypnuscare.iface.BaseType;
import com.ihypnus.ihypnuscare.iface.DialogListener;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
    private TextView mTvTitle09;
    private TextView mTvValue09;
    private TextView mTvTitle10;
    private TextView mTvValue10;
    private TextView mTvTitle11;
    private TextView mTvValue11;
    private Button mBtnCancel;
    private Button mBtnConfirm;
    private static final String[] sCurrentModel = {"CPAP", "APAP", "S", "Auto-S", "T", "ST"};
    private LinearLayout mLayoutContent03;
    private LinearLayout mLayoutContent09;
    private LinearLayout mLayoutContent10;
    private LinearLayout mLayoutContent11;
    private LinearLayout mLayoutContent04;
    private int mCureMode;
    private ArrayList<String> mKpaList;
    private ArrayList<String> mDelayTimeList;
    private ArrayList<String> mReleaseKpaLv;
    private ArrayList<String> mSpeed;
    private ArrayList<String> mComfortLv;
    private Map<String, Object> mParams = new HashMap<String, Object>();
    private ArrayList<String> mPressureRelease;

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
        mTvTitle09 = (TextView) findViewById(R.id.tv_title09);
        mTvValue09 = (TextView) findViewById(R.id.tv_value09);
        mTvTitle10 = (TextView) findViewById(R.id.tv_title10);
        mTvValue10 = (TextView) findViewById(R.id.tv_value10);
        mTvTitle11 = (TextView) findViewById(R.id.tv_title11);
        mTvValue11 = (TextView) findViewById(R.id.tv_value11);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mLayoutContent03 = (LinearLayout) findViewById(R.id.layout_content03);
        mLayoutContent04 = (LinearLayout) findViewById(R.id.layout_content04);
        mLayoutContent09 = (LinearLayout) findViewById(R.id.layout_content09);
        mLayoutContent10 = (LinearLayout) findViewById(R.id.layout_content10);
        mLayoutContent11 = (LinearLayout) findViewById(R.id.layout_content11);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String deviceId = intent.getStringExtra("DEVICE_ID");
        setTitle(deviceId);
        //压力范围
        mKpaList = new ArrayList<>();
        //释压水平
        mReleaseKpaLv = new ArrayList<>();
        //升压/降压速度
        mSpeed = new ArrayList<>();
        //舒适度
        mComfortLv = new ArrayList<>();
        //呼气释压
        mPressureRelease = new ArrayList<>();

        for (int i = 0; i <= 52; i++) {
            float value = (float) (4 + (i * 0.5));
            mKpaList.add(value + " cmH₂O");
        }
        mDelayTimeList = new ArrayList<>();

        String[] delaytimeArray = getResources().getStringArray(R.array.delaytimelist);
        mDelayTimeList.addAll(Arrays.asList(delaytimeArray));

        String[] release_kpa_lv = getResources().getStringArray(R.array.release_kpa_lv);
        mReleaseKpaLv.addAll(Arrays.asList(release_kpa_lv));

        String[] kpa_speed = getResources().getStringArray(R.array.kpa_speed);
        mSpeed.addAll(Arrays.asList(kpa_speed));

        String[] comfort_lv = getResources().getStringArray(R.array.comfort_lv);
        mComfortLv.addAll(Arrays.asList(comfort_lv));

        String[] expiratory_pressure_release = getResources().getStringArray(R.array.expiratory_pressure_release);
        mPressureRelease.addAll(Arrays.asList(expiratory_pressure_release));

        ShadowDeviceBean deviceBean = intent.getParcelableExtra("DEVICE_BEAN");
        if (deviceBean == null) {
            getParameterInfos(deviceId);
        } else {
            bindView(deviceBean);
        }
    }


    @Override
    protected void initEvent() {
        mTvValue05.setOnClickListener(this);
        mTvValue06.setOnClickListener(this);
        mTvValue07.setOnClickListener(this);
        mTvValue08.setOnClickListener(this);
        mTvValue09.setOnClickListener(this);
        mTvValue10.setOnClickListener(this);
        mTvValue11.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    private void getParameterInfos(String deviceId) {
        BaseDialogHelper.showLoadingDialog(this, true, getString(R.string.tv_isloading));
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
        mCureMode = deviceBean.getCure_mode();
        mTvTitle01.setText(R.string.tv_text_current_model);
        mTvValue01.setText(sCurrentModel[mCureMode]);
        switch (mCureMode) {
            //"CPAP", "APAP", "S", "Auto-S", "T", "ST"
            case 0:
                //CPAP
                mTvTitle02.setText(R.string.tv_text_zlyl);
                mTvValue02.setText(initTextView(deviceBean.getCpap_p() + ""));

                mLayoutContent03.setVisibility(View.INVISIBLE);
                mLayoutContent04.setVisibility(View.INVISIBLE);

                mTvTitle05.setText(R.string.tv_text_qsyl);
                mTvValue05.setText(initTextView(deviceBean.getStart_pressure() + ""));

                mTvTitle06.setText(R.string.tv_text_ycsj);
                mTvValue06.setText(deviceBean.getCure_delay() + " min");

                mTvTitle07.setText(R.string.tv_hqyl);
                mTvValue07.setText(deviceBean.getDep_type() + "");

                mTvTitle08.setText(R.string.tv_text_sysp);
                mTvValue08.setText(initTextView(deviceBean.getDep_level() + ""));

                mLayoutContent09.setVisibility(View.INVISIBLE);
                mLayoutContent10.setVisibility(View.INVISIBLE);
                mLayoutContent11.setVisibility(View.INVISIBLE);

                break;

            case 1:
                //APAP
                mTvTitle02.setText(R.string.tv_text_min_pressure);
                mTvValue02.setText(initTextView(deviceBean.getApap_min_p() + ""));

                mLayoutContent03.setVisibility(View.INVISIBLE);

                mTvTitle04.setText(R.string.tv_text_zdyl);
                mTvValue04.setText(initTextView(deviceBean.getApap_max_p() + ""));

                mTvTitle05.setText(R.string.tv_text_start_pressure);
                mTvValue05.setText(initTextView(deviceBean.getStart_pressure() + ""));

                mTvTitle06.setText(R.string.tv_text_ycsj);
                mTvValue06.setText(deviceBean.getCure_delay() + "min");

                mTvTitle07.setText(R.string.tv_hqyl);
                mTvValue07.setText(deviceBean.getDep_type() + "");

                mTvTitle08.setText(R.string.tv_text_sysp);
                mTvValue08.setText(initTextView(deviceBean.getDep_level() + ""));

                mLayoutContent09.setVisibility(View.INVISIBLE);
                mLayoutContent10.setVisibility(View.INVISIBLE);
                mLayoutContent11.setVisibility(View.INVISIBLE);
                break;

            case 2:
            case 3:
            case 4:
            case 5:
                //ST
                //T
                //S //Auto-S
                mTvTitle02.setText(R.string.tv_text_xqyl);
                mTvValue02.setText(initTextView(deviceBean.getBpap_in_p() + ""));

                mLayoutContent03.setVisibility(View.INVISIBLE);

                mTvTitle04.setText(R.string.tv_text_hqyl);
                mTvValue04.setText(initTextView(deviceBean.getBpap_ex_p() + ""));

                mTvTitle05.setText(R.string.tv_text_qsyl);
                mTvValue05.setText(initTextView(deviceBean.getStart_pressure() + ""));

                mTvTitle06.setText(R.string.tv_text_ycsj);
                mTvValue06.setText(deviceBean.getCure_delay() + "min");

                mTvTitle07.setText(R.string.tv_hqssd);
                mTvValue07.setText(deviceBean.getBreath_fit() + "");

                mTvTitle08.setText(R.string.tv_text_sysd);
                mTvValue08.setText(deviceBean.getBoostslope() + "");

                mTvTitle09.setText(R.string.tv_text_xqlmd);
                mTvValue09.setText(deviceBean.getInhale_sensitive() + "");

                mTvTitle10.setText(R.string.tv_text_down_speed);
                mTvValue10.setText(deviceBean.getBuckslope() + "");

                mTvTitle11.setText(R.string.tv_hqlmd);
                mTvValue11.setText(deviceBean.getExhale_sensitive() + "");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (v.getId()) {

            case R.id.tv_value05:
                //起始压力
                showListDialog(mKpaList, mTvValue05, "start_pressure");
                break;
            case R.id.tv_value06:
                //延迟时间
                showListDialog(mDelayTimeList, mTvValue06, "cure_delay");

                break;
            case R.id.tv_value07:

                if (mCureMode <= 1) {
                    //呼气释压
                    showListDialog(mPressureRelease, mTvValue07, "dep_type");
                } else {
                    //呼气舒适度
                    showListDialog(mComfortLv, mTvValue07, "breath_fit");
                }
                break;
            case R.id.tv_value08:

                if (mCureMode <= 1) {
                    //释压水平
                    showListDialog(mReleaseKpaLv, mTvValue08, "dep_level");
                } else {
                    //升压速度
                    showListDialog(mSpeed, mTvValue08, "boostslope");
                }
                break;
            case R.id.tv_value09:
                //吸气灵敏度
                showListDialog(mSpeed, mTvValue09, "inhale_sensitive");
                break;
            case R.id.tv_value10:
                //降压速度
                showListDialog(mSpeed, mTvValue10, "buckslope");
                break;
            case R.id.tv_value11:
                //呼气灵敏度
                showListDialog(mSpeed, mTvValue11, "exhale_sensitive");
                break;


            case R.id.btn_cancel:
                finish();
                break;

            case R.id.btn_confirm:
                updateParameterInfos();
                break;
        }
    }

    private void updateParameterInfos() {
        mParams.put("JSESSIONID", Constants.JSESSIONID);
        mParams.put("isCookie", true);
        mParams.put("deviceId", Constants.DEVICEID);
        mParams.put("deviceID", Constants.DEVICEID);

        BaseDialogHelper.showLoadingDialog(this, true, getString(R.string.tv_isloading));
        IhyRequest.updateShadowDevice(mParams, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                BaseDialogHelper.showSimpleDialog(ParameterSettingsActivity.this, getString(R.string.tv_tittle_setparmeter), getString(R.string.tv_tip_use_new_setting), getString(R.string.ok), new DialogListener() {
                    @Override
                    public void onClick(BaseType baseType) {
                        finish();
                    }

                    @Override
                    public void onItemClick(long postion, String s) {

                    }
                });

            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }
        });
    }

    /**
     * 显示选择列表
     *
     * @param list
     * @param textView
     */
    private void showListDialog(ArrayList<String> list, final TextView textView, final String key) {
        BaseDialogHelper.showListDialog(this, "", getString(R.string.tv_btn_back), list, new DialogListener() {
            @Override
            public void onClick(BaseType baseType) {

            }

            @Override
            public void onItemClick(long postion, String s) {
                textView.setText(s);
                if (s.contains(" min")) {
                    s = s.replace(" min", "");
                }
                if (s.contains(" cmH₂O")) {
                    s = s.replace(" cmH₂O", "");
                    if (key.equals("start_pressure")) {
                        double v = Double.parseDouble(s);
                        int startPressure = (int) (v * 10);
                        s = String.valueOf(startPressure);
                    }
                }
                if (s.equals(getString(R.string.tv_text_close))) {
                    s = "0";
                }
                mParams.put(key, s);
            }
        });
    }

    /**
     * 处理压力单位
     *
     * @param text
     * @return
     */
    private String initTextView(String text) {
        if (!StringUtils.isNullOrEmpty(text)) {
            int i = Integer.parseInt(text);
            float i1 = i / 10f;
            return i1 + "cmH₂O";
        }
        return "";
    }
}