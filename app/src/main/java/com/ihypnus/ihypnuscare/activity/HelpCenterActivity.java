package com.ihypnus.ihypnuscare.activity;

import android.os.Bundle;

import com.ihypnus.ihypnuscare.R;

/**
 * @Package com.ihypnus.ihypnuscare.activity
 * @author: llw
 * @Description: 帮助中心
 * @date: 2018/6/7 11:30
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class HelpCenterActivity extends BaseActivity {
    @Override
    protected int setView() {
        return R.layout.activity_help_center;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle(getString(R.string.tv_help_center));
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void loadData() {

    }
}
