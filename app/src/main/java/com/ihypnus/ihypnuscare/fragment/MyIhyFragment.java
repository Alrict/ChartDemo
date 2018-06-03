package com.ihypnus.ihypnuscare.fragment;

import android.view.View;
import android.widget.RelativeLayout;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

/**
 * @Package com.ihypnus.ihypnuscare.fragment
 * @author: llw
 * @Description: 我的fragment
 * @date: 2018/5/28 18:35
 * @copyright copyright(c)2016 Shenzhen Kye Technology Co., Ltd. Inc. All rights reserved.
 */
public class MyIhyFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout mLayoutPersonInfo;
    private RelativeLayout mLayoutHelp;
    private RelativeLayout mLayoutSuggestions;
    private RelativeLayout mLayoutSettings;

    @Override
    protected int setView() {
        return R.layout.fragment_my_ihy;
    }

    @Override
    protected void findViews() {
        mLayoutPersonInfo = (RelativeLayout) findViewById(R.id.layout_person_info);
        mLayoutHelp = (RelativeLayout) findViewById(R.id.layout_help);
        mLayoutSuggestions = (RelativeLayout) findViewById(R.id.layout_suggestions);
        mLayoutSettings = (RelativeLayout) findViewById(R.id.layout_settings);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initEvent() {
        mLayoutPersonInfo.setOnClickListener(this);
        mLayoutHelp.setOnClickListener(this);
        mLayoutSuggestions.setOnClickListener(this);
        mLayoutSettings.setOnClickListener(this);

    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View view) {
        if (ViewUtils.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.layout_person_info:

                break;

            case R.id.layout_help:

                break;

            case R.id.layout_suggestions:

                break;

            case R.id.layout_settings:

                break;

        }

    }
}
