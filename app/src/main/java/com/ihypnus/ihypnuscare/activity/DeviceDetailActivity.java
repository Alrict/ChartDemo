package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.bean.DeviceListVO;
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
            setTitle(bean.getDevice_id());
            mTvModelType.setText(bean.getModel());
            mTvVersion.setText(bean.getFactory_id());

            mTvStatus.setText("在线");
            mIvStatus.setImageDrawable(getResources().getDrawable(R.mipmap.icon_out_line));

        }
    }

    @Override
    protected void initEvent() {
        mBtnSetting.setOnClickListener(this);
        mBtnModify.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.btn_setting:

                jumpToWifi();
                break;

            case R.id.btn_modify:

                break;
        }
    }

    private void jumpToWifi() {
        Intent intent = new Intent(this, WifiSettingHistoryActivity.class);
        startActivity(intent);
    }
}
