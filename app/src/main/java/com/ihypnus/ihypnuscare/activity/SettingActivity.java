package com.ihypnus.ihypnuscare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 设置
 * @date: 2018/6/7 11:32
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mIvMultiLanguage;
    private ImageView mIvAbout;

    @Override
    protected int setView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void findViews() {
        mIvMultiLanguage = findViewById(R.id.iv_multi_language);
        mIvAbout = findViewById(R.id.iv_about);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("设置");
    }

    @Override
    protected void initEvent() {
        mIvMultiLanguage.setOnClickListener(this);
        mIvAbout.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (v.getId()) {
            case R.id.iv_multi_language:
                jumpToActivity(MultiLanguageActivity.class);
                break;

            case R.id.iv_about:

                jumpToActivity(AboutCompanyInfosActivity.class);
                break;
        }
    }

    private void jumpToActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

}
