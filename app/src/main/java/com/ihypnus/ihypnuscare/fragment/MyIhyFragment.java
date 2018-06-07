package com.ihypnus.ihypnuscare.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.ihypnus.ihypnuscare.R;
import com.ihypnus.ihypnuscare.activity.FeedbackActivity;
import com.ihypnus.ihypnuscare.activity.HelpCenterActivity;
import com.ihypnus.ihypnuscare.activity.MyInfosActivity;
import com.ihypnus.ihypnuscare.activity.SettingActivity;
import com.ihypnus.ihypnuscare.utils.ViewUtils;

import static android.app.Activity.RESULT_OK;

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
                jumpToActivity(101, MyInfosActivity.class);
                break;

            case R.id.layout_help:
                jumpToActivity(102, HelpCenterActivity.class);
                break;

            case R.id.layout_suggestions:
                jumpToActivity(103, FeedbackActivity.class);
                break;

            case R.id.layout_settings:
                jumpToActivity(104, SettingActivity.class);
                break;

        }

    }

    private void jumpToActivity(int requestCode, Class<?> cls) {
        Intent intent = new Intent(mAct, cls);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 101:
                    //个人资料

                    break;

                case 102:
                    //帮助中心

                    break;

                case 103:
                    //意见反馈

                    break;

                case 104:
                    //设置

                    break;
            }
        }
    }
}
