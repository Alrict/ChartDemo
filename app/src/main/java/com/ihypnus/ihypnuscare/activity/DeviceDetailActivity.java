package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.ResponseCallback;
import com.android.volley.VolleyError;
import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.DeviceListVO;
import com.ihypnus.ihypnuscare.bean.ShadowDeviceBean;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.net.IhyRequest;
import com.ihypnus.ihypnuscare.utils.StringUtils;
import com.ihypnus.ihypnuscare.utils.ToastUtils;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description:
 * @date: 2018/6/18 22:00
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class DeviceDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTvModelType;
    private TextView mTvVersion;
    private TextView mTvStatus;
    private ImageView mIvStatus;
    private Button mBtnSetting;
    private Button mBtnModify;
    private String mDeviceId;
    private String mCurState;
    private ShadowDeviceBean mDeviceBean;

    @Override
    protected int setView() {
        return R.layout.activity_device_detail;
    }

    @Override
    protected void findViews() {
        mTvModelType = (TextView) findViewById(R.id.tv_model_type);
        mTvVersion = (TextView) findViewById(R.id.tv_version);
        mTvStatus = (TextView) findViewById(R.id.tv_status);
        mIvStatus = (ImageView) findViewById(R.id.iv_status);
        mBtnSetting = (Button) findViewById(R.id.btn_setting);
        mBtnModify = (Button) findViewById(R.id.btn_modify);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        DeviceListVO.ContentBean bean = intent.getParcelableExtra("DATA_BEAN");
        if (bean != null) {
            mDeviceId = bean.getDevice_id();
            setTitle(mDeviceId);
            String model = bean.getModel();
            if (StringUtils.isNullOrEmpty(model)) {
                mTvModelType.setText("未知");
            } else {
                mTvModelType.setText(bean.getModel());
            }

        }
    }

    @Override
    protected void initEvent() {
        mBtnSetting.setOnClickListener(this);
        mBtnModify.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        BaseDialogHelper.showLoadingDialog(this, true, "正在加载...");
        IhyRequest.getShadowDevice(Constants.JSESSIONID, true, mDeviceId, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                mDeviceBean = (ShadowDeviceBean) var1;
                bindView(mDeviceBean);
            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
            }
        });
    }

    private void bindView(ShadowDeviceBean deviceBean) {
        if (deviceBean != null) {
            String dataVersion = deviceBean.getData_version();
            mCurState = deviceBean.getCur_state();
            mTvVersion.setText(dataVersion);
            if (mCurState.equals("ONLINE")) {
                mTvStatus.setText("在线");
                mIvStatus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_online));
            } else {
                mTvStatus.setText("离线");
                mIvStatus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_out_line));
            }
        }

    }

    @Override
    public void onClick(View view) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.btn_setting:
                jumpToParameterSettingsActivity();
//                if (mCurState.equals("ONLINE")) {
//                    jumpToParameterSettingsActivity();
//                } else {
//                    BaseDialogHelper.showMsgTipDialog(DeviceDetailActivity.this, "该设备处于离线状态,请检查设备网络状态");
//                }

                break;

            case R.id.btn_modify:
                jumpToWifi();
//                if (mCurState.equals("ONLINE")) {
//                    jumpToWifi();
//                } else {
//                    BaseDialogHelper.showMsgTipDialog(DeviceDetailActivity.this, "该设备处于离线状态,请检查设备网络状态");
//                }

                break;
        }
    }

    /**
     * 跳转至参数设置页面
     */
    private void jumpToParameterSettingsActivity() {
        Intent intent = new Intent(this, ParameterSettingsActivity.class);
        if (mDeviceBean != null) {
            intent.putExtra("DEVICE_BEAN", mDeviceBean);
        }
        intent.putExtra("DEVICE_ID", mDeviceId);


        startActivity(intent);
    }

    /**
     * 进入wifi配置页面
     */
    private void jumpToWifi() {
        Intent intent = new Intent(this, WifiSettingHistoryActivity.class);
        startActivity(intent);
    }
}
