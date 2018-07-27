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
import com.ihypnus.ihypnuscare.bean.DeviceModelVO;
import com.ihypnus.ihypnuscare.bean.ShadowDeviceBean;
import com.ihypnus.ihypnuscare.config.Constants;
import com.ihypnus.ihypnuscare.dialog.BaseDialogHelper;
import com.ihypnus.ihypnuscare.iface.BaseType;
import com.ihypnus.ihypnuscare.iface.DialogListener;
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
                mTvModelType.setText(R.string.tv_unknow);
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
        getDeviceInfos(mDeviceId);
        //todo 获取设备model并判断该设备是否具有wifi模块
        //根据第六位的'W'
   /*     String model = "";
        if (model.length() >= 6 && String.valueOf(model.indexOf(5)).equals("W")) {
            mBtSetWifi.setVisibility(View.VISIBLE);
        } else {
            mBtSetWifi.setVisibility(View.GONE);
        }*/
    }

    /**
     * 获取设备model,判断是否可以使用wifi设置模块
     *
     * @param deviceId
     */
    private void getDeviceInfos(final String deviceId) {
        BaseDialogHelper.showLoadingDialog(this, true, getString(R.string.tv_isloading));
        IhyRequest.getDeviceInfos(deviceId, new ResponseCallback() {
            @Override
            public void onSuccess(Object var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                DeviceModelVO deviceModelVO = (DeviceModelVO) var1;
                if (deviceModelVO != null && !StringUtils.isNullOrEmpty(deviceModelVO.getModel())) {
                    String model = deviceModelVO.getModel();
                    //判断该设备号是否支持wifi设置
                    if (model.length() >= 6 && model.substring(5, 6).equals("W")) {
                        //设备是V2.0版本以上的,且支持wifi设置
                        mBtnModify.setVisibility(View.VISIBLE);
                    } else {
                        //调用接口获取该设备的信息,根据model字段判断是否需要显示wifi配置按钮
                        mBtnModify.setVisibility(View.INVISIBLE);
                    }
                } else {
                    mBtnModify.setVisibility(View.INVISIBLE);
                }
                loadParameterInfos();

            }

            @Override
            public void onError(VolleyError var1, String var2, String var3) {
                BaseDialogHelper.dismissLoadingDialog();
                ToastUtils.showToastDefault(var3);
                mBtnModify.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 获取影子设备信息
     */
    private void loadParameterInfos() {
        BaseDialogHelper.showLoadingDialog(this, true, getString(R.string.onloading));
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
                BaseDialogHelper.showNormalDialog(DeviceDetailActivity.this, getString(R.string.tip_msg), getString(R.string.tv_text_load_errot), getString(R.string.cancel), getString(R.string.ok), new DialogListener() {
                    @Override
                    public void onClick(BaseType baseType) {
                        if (BaseType.OK == baseType) {
                            loadParameterInfos();
                        } else {
                            finish();
                        }
                    }

                    @Override
                    public void onItemClick(long postion, String s) {

                    }
                });
//                ToastUtils.showToastDefault(var3);
            }
        });
    }

    private void bindView(ShadowDeviceBean deviceBean) {
        if (deviceBean != null) {
            String dataVersion = deviceBean.getData_version();
            mCurState = deviceBean.getCur_state();
            mTvVersion.setText(dataVersion);
            if (mCurState.equals("ONLINE")) {
                mTvStatus.setText(R.string.tv_online);
                mIvStatus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_online));
            } else {
                mTvStatus.setText(R.string.tv_offline);
                mIvStatus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_out_line));
            }
        }

    }

    @Override
    public void onClick(View view) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.btn_setting:
                //参数设置
                if (mCurState.equals("ONLINE")) {
                    jumpToParameterSettingsActivity();
                } else {
                    BaseDialogHelper.showMsgTipDialog(DeviceDetailActivity.this, getString(R.string.tv_toast_curent_device_offline));
                }

                break;

            case R.id.btn_modify:
                //wifi设置
                jumpToWifi();
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


        startActivityForResult(intent, 122);
    }

    /**
     * 进入wifi配置页面
     */
    private void jumpToWifi() {
        //  点击配置wifi时校验当前链接的热点是否是目标热点
        Intent intent = new Intent(this, NewDeviceInformationActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 122) {
            loadParameterInfos();
        }
    }
}
